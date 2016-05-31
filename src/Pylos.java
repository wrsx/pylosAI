import java.util.*;

public class Pylos {

    public enum Action {
        PLACE, MOUNT, REMOVE, WAIT;
    } 
    
    public Action action;
    public int whiteSpheres;
    public int blackSpheres;
    public Board gameBoard;
    
    public Pylos() {
        whiteSpheres = 15;
        blackSpheres = 15;
        gameBoard = new Board();
    }
 
    public void idle() {
        action = Action.PLACE;
        Scanner user_input = new Scanner(System.in);
        
        while (true) {
            System.out.println("Please select a valid move: ");
                try {
                    move(user_input.next());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
    
    public void move(String arg) {
        System.out.println("You chose move: " + arg);
        action = Action.WAIT;
    }
    
    public static void main(String[] args) {
        Pylos game = new Pylos();
        game.idle();
    }
    
}
