import java.awt.Color;
import java.awt.GridLayout;
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
    }
    
    public enum Action {
        PLACE, PROMOTE, REMOVE, DEFAULT;
    }
 
    public void idle() {     
        //continue to allow moves until a player runs out of spheres or reaches the top of the pyramid
        while (gameBoard.whiteSpheres > 0 && gameBoard.blackSpheres > 0 && gameBoard.level3[0][0] == 0) {

                    
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
                    break;
                case "promote" :
                    a = Action.PROMOTE;                    
                    System.out.println("Please enter the board coordinates of the sphere you would like to promote: ");
                    oc = new Coordinate (userInput.next(), gameBoard);
                    System.out.println("Please enter the new board coordinates for the sphere: ");
                    nc = new Coordinate (userInput.next(), gameBoard);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        m = new Move(gameBoard, a, currentPlayer, nc, oc); 
        Boolean result = m.execute();
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
                m = new Move(gameBoard, Action.REMOVE, currentPlayer, null, oc);
                if(m.execute()) deleting -= 1;                    
            }
        }
        return result;
        
    }      

    public static void main(String[] args) {
        Pylos game = new Pylos();
        game.idle();
        System.exit(0);
    }
    
}