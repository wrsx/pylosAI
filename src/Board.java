
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tom
 */
public class Board extends JFrame {
    
    public int[][] level0;
    public int[][] level1;
    public int[][] level2;
    public int[][] level3;
    
    public int whiteSpheres;
    public int blackSpheres;
    
    public String lastmove = "";
    
    
    public Board() { //create board and initialise, all initialised to 0
        level0 = new int[4][4];
        level1 = new int[3][3];
        level2 = new int[2][2];
        level3 = new int[1][1];
        
        whiteSpheres = 15;
        blackSpheres = 15;        
    }
    
    //Copy constructor
    public Board(Board another) {
        this();
        for(int i = 0; i < another.level0.length; i++) level0[i] = Arrays.copyOf(another.level0[i], another.level0[i].length);
        for(int i = 0; i < another.level1.length; i++) level1[i] = Arrays.copyOf(another.level1[i], another.level1[i].length);
        for(int i = 0; i < another.level2.length; i++) level2[i] = Arrays.copyOf(another.level2[i], another.level2[i].length);
        for(int i = 0; i < another.level3.length; i++) level3[i] = Arrays.copyOf(another.level3[i], another.level3[i].length);
        
        this.whiteSpheres = another.whiteSpheres;
        this.blackSpheres = another.blackSpheres;
        this.lastmove = another.lastmove;
  }    
    
    //returns the 2D array from the board for that level
    public int[][] getLevelTable(int level) {
        switch(level) {
            case 0 :
                return level0;
            case 1 :
                return level1;
            case 2 :
                return level2;
            case 3 :
                return level3;
        }
        return null;
    }
    
    public boolean gameFinished() {
        return !(whiteSpheres > 0 && blackSpheres > 0 && level3[0][0] == 0);
    }
    
    private  ArrayList<Coordinate> getAllCoordinates() {
        ArrayList<Coordinate> allCoordinates = new ArrayList<Coordinate>();
        String characters = "abcdefghij";
        int x = 0;
        //loop over the letters and numbers a1 -> j1
        for(int i = 0; i < characters.length(); i++) {
            for(int j = 1; j < 5-x; j++) {
                allCoordinates.add(new Coordinate(characters.substring(i, i+1) + j, this));
            }
            //corrects for level 1 being 3x3, level 2 being 2x2....
            if(i == 3 || i == 6 || i == 8 ) x++;
        }     
        return allCoordinates;
    }
    
    private  ArrayList<Move> getValidMoves(int player) {
        ArrayList<Move> validMoves = new ArrayList<Move>();
        ArrayList<Coordinate> allCoordinates = getAllCoordinates();
        //loops through all of the coordinates and checks if a new sphere can be added, it it can it is added as a valid move
        for(Coordinate nc : allCoordinates) {
            Move m = new Move(new Board(this), Pylos.Action.PLACE, player, nc, null);
            if(Move.checkValidMove(m)) validMoves.add(m);
        }
        //loops through all the valid PLACE moves, for each valid place, loop through all coordinates and check if ones on a lower level can be promoted to the new coordinate from the PLACE move
        int size = validMoves.size();
        for(int i = 0; i < size; i++) {
            Coordinate nc = validMoves.get(i).newCoordinate;
            for(Coordinate oc : allCoordinates) {
                if(nc.level > oc.level) {
                    Move m = new Move(new Board(this), Pylos.Action.PROMOTE, player, nc, oc);
                        if(Move.checkValidMove(m)) validMoves.add(m);
                }
            }
        }
        return validMoves;
    }
    
    public ArrayList<Board> getPossibleBoards(int player) {
         //disable stdout to stop all of not valid messages from move
        enableStdout(false);
        ArrayList<Board> possibleBoards = new ArrayList<Board>();
        ArrayList<Move> validMoves = getValidMoves(player);
        ArrayList<Coordinate> allCoordinates = getAllCoordinates();
        
        int size = validMoves.size();
        for(int i = 0; i < size; i++) {
            Move m = validMoves.get(i);
            m.execute();
            possibleBoards.add(m.gameBoard);            
            if(Move.checkSpecialMove(m)) {
                for(Coordinate oc : allCoordinates) {
                    Move deleteMove = new Move(new Board(m.gameBoard), Pylos.Action.REMOVE, player, null, oc);
                    if(Move.checkValidMove(deleteMove)) {
                        //delete 1
                        deleteMove.execute();
                        possibleBoards.add(deleteMove.gameBoard);
                        for(Coordinate oc2 : allCoordinates) {
                            Move deleteMove2 = new Move(new Board(deleteMove.gameBoard), Pylos.Action.REMOVE, player, null, oc2);
                            if(Move.checkValidMove(deleteMove2)) {
                                //delete 2
                                deleteMove2.execute();
                                possibleBoards.add(deleteMove2.gameBoard); 
                            }
                        }
                    }
                }
                
            }
        }
        for(Board board : possibleBoards) {
            board.setSize(1500,600); 
            board.setLocationRelativeTo(null); 
            board.setBackground(Color.WHITE); 
            board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
            board.setVisible(true);
            board.setTitle("Pylos");
            board.setLayout(new GridLayout(4,4));            
        }
        enableStdout(true);
        return possibleBoards;
    }
    
    private static void enableStdout(Boolean on) {
        if(on) {
             System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        } else {
            System.setOut(new PrintStream(new OutputStream() {
                public void write(int b) {
                    //DO NOTHING
                }
            }));
        }
    }
    
    private int getBoardScore() {
        int score = this.whiteSpheres - this.blackSpheres;
        return score;
        
    }    

    private void paintLevel(Graphics g, int size, int offset, int[][] level) {
        
        //Draw board 
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(100+offset, 100, size*100, size*100); 
        g.setColor(Color.DARK_GRAY);
        g.drawRect(100+offset, 100, size*100, size*100); 
        for(int x = 100+offset; x <= 100*size+offset; x+=200){ 
            for(int y = 100; y <= 100*size; y+=200){ 
                g.setColor(Color.DARK_GRAY);
                g.drawRect(x, y, 100, 100); 

            }
        }
        for(int x = 200+offset; x <= 100*size+offset; x+=200){ 
            for(int y = 200; y <= 100*size; y+=200){ 
                g.setColor(Color.DARK_GRAY);
                g.drawRect(x, y, 100, 100); 
 
            }
        }

        //Draw spheres
        for(int i = 0; i<size; i++) {
            for(int j = 0; j<size; j++) {
                int player = level[i][j];
                if(player > 0){
                    g.setColor(Color.WHITE);
                    g.fillOval((i+1)*100+5+offset, (j+1)*100+5, 90, 90);
                    g.setColor(Color.BLACK);
                    g.drawOval((i+1)*100+5+offset, (j+1)*100+5, 90, 90);
                }
                else if(player < 0){
                    g.setColor(Color.BLACK);
                    g.fillOval((i+1)*100+5+offset, (j+1)*100+5, 90, 90);
                    g.drawOval((i+1)*100+5+offset, (j+1)*100+5, 90, 90); 
                } 
            }
        }        
    }      
    
    public void paint(Graphics g){

        g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 14));
        int offset = 0;
        paintLevel(g, 4, 0, level0);
        g.setColor(Color.BLACK);
        g.drawString("a", 145, 80);
        g.drawString("b", 245, 80);
        g.drawString("c", 345, 80);
        g.drawString("d", 445, 80);        
        g.drawString("1", 75, 150);
        g.drawString("2", 75, 250);
        g.drawString("3", 75, 350);  
        g.drawString("4", 75, 450);
        g.drawString("Level 0", 280, 530);  
        
        offset = 500;        
        paintLevel(g, 3, offset, level1);
        g.setColor(Color.BLACK);
        g.drawString("e", 145+offset, 80);
        g.drawString("f", 245+offset, 80);
        g.drawString("g", 345+offset, 80);
        g.drawString("1", 75+offset, 150);
        g.drawString("2", 75+offset, 250);
        g.drawString("3", 75+offset, 350);  
        g.drawString("Level 1", 730, 430);  
        
        offset = 900; 
        paintLevel(g, 2, offset, level2);
        g.setColor(Color.BLACK);
        g.drawString("h", 145+offset, 80);
        g.drawString("i", 245+offset, 80);
        g.drawString("1", 75+offset, 150);
        g.drawString("2", 75+offset, 250);           
        g.drawString("Level 2", 1080, 330); 
        
        offset = 1200;
        paintLevel(g, 1, offset, level3);
        g.setColor(Color.BLACK);
        g.drawString("j", 145+offset, 80);
        g.drawString("1", 75+offset, 150);
        g.drawString("Level 3", 1330, 230);
        
        //Draw scoreboard
        g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 30));

        g.setColor(Color.WHITE);
        g.fillOval(1330, 350, 100, 100); 
        
        g.setColor(Color.BLACK);
        g.fillOval(1330, 470, 100, 100); 
        g.drawOval(1330, 350, 100, 100); 
        
        ///////
        g.drawString(lastmove, 550, 500);
        ///////
        
        g.drawString(Integer.toString(whiteSpheres), 1364, 415);
        g.setColor(Color.WHITE);
        g.drawString(Integer.toString(blackSpheres), 1364, 530);
    }
    
}
