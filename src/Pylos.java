import java.awt.Color;
import java.awt.GridLayout;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;
import javax.swing.JFrame;

public class Pylos {
    
    public int currentPlayer;
    public Board gameBoard;
    private final Scanner userInput;
    
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
        test();
    }
    
    public enum Action {
        PLACE, PROMOTE, REMOVE, DEFAULT;
    }
 
    public void idle() {     
        //continue to allow moves until a player runs out of spheres or reaches the top of the pyramid
        while (!gameBoard.gameFinished()) {   
            System.out.println("\nPlease enter an action: ");
                try {            
                    if(move(userInput.next())) {
                        currentPlayer = -currentPlayer;
                        if(currentPlayer > 0) {
                            System.out.println("\nWhite's turn!");
                        } else System.out.println("\nBlack's turn!");
                    }  
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
        }
        winner();
        userInput.close();
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

    private static ArrayList<Coordinate> getAllCoordinates(Board b) {
        ArrayList<Coordinate> allCoordinates = new ArrayList<Coordinate>();
        String characters = "abcdefghij";
        int x = 0;
        //loop over the letters and numbers a1 -> j1
        for(int i = 0; i < characters.length(); i++) {
            for(int j = 1; j < 5-x; j++) {
                allCoordinates.add(new Coordinate(characters.substring(i, i+1) + j, b));
            }
            //corrects for level 1 being 3x3, level 2 being 2x2....
            if(i == 3 || i == 6 || i == 8 ) x++;
        }     
        return allCoordinates;
    }
    
    private static ArrayList<Move> getValidMoves(Board b, int player) {
        ArrayList<Move> validMoves = new ArrayList<Move>();
        ArrayList<Coordinate> allCoordinates = getAllCoordinates(b);
        //loops through all of the coordinates and checks if a new sphere can be added, it it can it is added as a valid move
        for(Coordinate nc : allCoordinates) {
            Move m = new Move(new Board(b), Action.PLACE, player, nc, null);
            if(Move.checkValidMove(m)) validMoves.add(m);
        }
        //loops through all the valid PLACE moves, for each valid place, loop through all coordinates and check if ones on a lower level can be promoted to the new coordinate from the PLACE move
        int size = validMoves.size();
        for(int i = 0; i < size; i++) {
            Coordinate nc = validMoves.get(i).newCoordinate;
            for(Coordinate oc : allCoordinates) {
                if(nc.level > oc.level) {
                    Move m = new Move(new Board(b), Action.PROMOTE, player, nc, oc);
                        if(Move.checkValidMove(m)) validMoves.add(m);
                }
            }
        }
        return validMoves;
    }
    
    public static ArrayList<Board> getPossibleBoards(Board b, int player) {
         //disable stdout to stop all of not valid messages from move
        enableStdout(false);
        ArrayList<Board> possibleBoards = new ArrayList<Board>();
        ArrayList<Move> validMoves = getValidMoves(b, player);
        ArrayList<Coordinate> allCoordinates = getAllCoordinates(b);
        
        int size = validMoves.size();
        for(int i = 0; i < size; i++) {
            Move m = validMoves.get(i);
            m.execute();
            possibleBoards.add(m.gameBoard);            
            if(Move.checkSpecialMove(m)) {
                for(Coordinate oc : allCoordinates) {
                    Move deleteMove = new Move(new Board(m.gameBoard), Action.REMOVE, player, null, oc);
                    if(Move.checkValidMove(deleteMove)) {
                        //delete 1
                        deleteMove.execute();
                        possibleBoards.add(deleteMove.gameBoard);
                        for(Coordinate oc2 : allCoordinates) {
                            Move deleteMove2 = new Move(new Board(deleteMove.gameBoard), Action.REMOVE, player, null, oc2);
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
    
    private static int getBoardScore(Board b) {
        int score = b.whiteSpheres - b.blackSpheres;
        return score;
        
    }
    
    void test() {
        gameBoard.level0[2][0] = 1;
        gameBoard.level0[2][1] = 1;
        gameBoard.level0[2][2] = 1;
        gameBoard.level0[2][3] = 1;
        gameBoard.level0[1][1] = -1;
        gameBoard.level0[1][2] = -1;
        gameBoard.level0[1][3] = -1;  
        getPossibleBoards(gameBoard, -1);
    }

    public static void main(String[] args) {
        Pylos game = new Pylos();
        game.idle();
        System.exit(0);
    }
    
}