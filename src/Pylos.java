import java.awt.Color;
import java.awt.GridLayout;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;
import javax.swing.JFrame;

public class Pylos {
    
    final static int WHITE = 1;
    final static int BLACK = -1;
    public int currentPlayer;
    public Board gameBoard;
    private final Scanner userInput;
    private AI player;
    
    public Pylos() {  
        currentPlayer = 1;       
    
        gameBoard = new Board();
        gameBoard.setSize(1500,600); 
        gameBoard.setLocationRelativeTo(null); 
        gameBoard.setBackground(Color.WHITE); 
        gameBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        gameBoard.setVisible(true);
        gameBoard.setTitle("Pylos");
        gameBoard.setLayout(new GridLayout(4,4));

        userInput = new Scanner(System.in);
        //test();
        
        player = new AI();
    }
    
    public enum Action {
        PLACE, PROMOTE, REMOVE, DEFAULT;
    }
 
    public void idle() {     
        //continue to allow moves until a player runs out of spheres or reaches the top of the pyramid
        while (!gameBoard.gameFinished()) {   
            System.out.println("\nPlease enter an action: ");
                try {
                    if(currentPlayer == WHITE) {
                        System.out.println("\nWhite's turn!");
                        if(move(userInput.next())) {
                            currentPlayer = -currentPlayer;
                        }        
                    } else {
                        System.out.println("\nBlack's turn!");
                        AIMove(player);
                    }

                        
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
        }
        winner();
        userInput.close();
    }
    
    private void AIMove(AI p) {
        gameBoard.setBoard(player.findBestBoard(4, gameBoard, currentPlayer));
        currentPlayer = -currentPlayer;
        gameBoard.repaint();
    }
    
    private void winner() {
        //reverse the player change from the previous move
        currentPlayer = -currentPlayer; 
        //black ran out of spheres
        if(gameBoard.whiteSpheres > 0) {
            System.out.println("Congratulations! White has won the game by exhausting black's spheres");
        //white ran out of spheres    
        } else if(gameBoard.blackSpheres > 0) {
            System.out.println("Congratulations! Black has won the game by exhausting white's spheres");
        //black reached the top    
        } else if(gameBoard.level3[0][0] < 0) {
            System.out.println("Congratulations! Black has won the game by reaching the top");
        //white reached the top.    
        } else {
            System.out.println("Congratulations! White has won the game by reaching the top");
        }
    }
    
    private boolean move(String move) { //create board and initialise, all initialised to 0      
        Move m;
        Coordinate nc = null;
        Coordinate oc = null;
        Action a = Action.DEFAULT;
        try {        
            switch(move) {
                case "place" :
                    a = Action.PLACE;                    
                    System.out.println("Please enter the board coordinates where you would like to place your sphere: ");
                    nc = new Coordinate (userInput.next(), gameBoard);
                    if(!nc.isValid()) return false;
                    break;
                case "promote" :
                    a = Action.PROMOTE;                    
                    System.out.println("Please enter the board coordinates of the sphere you would like to promote: ");
                    oc = new Coordinate (userInput.next(), gameBoard);
                    if(!oc.isValid()) return false;                   
                    System.out.println("Please enter the new board coordinates for the sphere: ");
                    nc = new Coordinate (userInput.next(), gameBoard);
                    if(!nc.isValid()) return false;                
                    break;
                default :
                    System.out.println("Not a valid action!\nValid actions are: place and promote");  
                    return false;                    
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        m = new Move(gameBoard, a, currentPlayer, nc, oc); 
        
        Boolean result = false;
        if(Move.checkValidMove(m)) {
             result = m.execute();
        }    
        if(Move.checkSpecialMove(m) && result) {
            int deleting = 0;
            try {
                System.out.println("Please enter the number of spheres you would like to delete: 1, 2 or 0");
                deleting = Integer.parseInt(userInput.next());
                while(deleting > 2 || deleting < 0) {
                    System.out.println("Please enter a valid number: 1, 2, or 0");
                    deleting = Integer.parseInt(userInput.next());
                }
            } catch (Exception e) {
                System.err.printf("Number of spheres to be deleted not specified");
            }
            while(deleting != 0) {
                System.out.println("Please enter the board coordinates of the sphere you would like to remove: "); 
                oc = new Coordinate (userInput.next(), gameBoard);
                if(!oc.isValid()) {
                    deleting -= 1;
                    continue;
                } 
                m = new Move(gameBoard, Action.REMOVE, currentPlayer, null, oc);
                if(!Move.checkValidMove(m)) continue;
                if(m.execute()) deleting -= 1;                    
            }
        }
        return result;
    }
    
    void test() {
        gameBoard.level0[2][0] = 1;
        gameBoard.level0[2][1] = 1;
        gameBoard.level0[2][2] = 1;
        gameBoard.level0[2][3] = 1;
        gameBoard.level0[1][1] = -1;
        gameBoard.level0[1][2] = -1;
        gameBoard.level0[1][3] = -1;  
        gameBoard.getPossibleBoards(-1);
    }

    public static void main(String[] args) {
        Pylos game = new Pylos();
        game.idle();
        System.exit(0);
    }
    
}