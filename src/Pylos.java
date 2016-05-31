import java.util.*;

public class Pylos {
    
    public int whiteSpheres;
    public int blackSpheres;
    public int currentPlayer;
    public Board gameBoard;
    private Scanner user_input;
    
    public Pylos() {
        whiteSpheres = 15;
        blackSpheres = 15;
        currentPlayer = 1;
        gameBoard = new Board();
        user_input = new Scanner(System.in);
    }
    
    private enum Action {
        PLACE, PROMOTE, REMOVE, DEFAULT;
    }
 
    public void idle() {     
        while (true) {
            System.out.println("Please enter an action: ");
                try {            
                    if(move(user_input.next(), gameBoard, currentPlayer)) {
                        currentPlayer = -currentPlayer;
                    }  
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
    
    private boolean move(String a, Board b, int p) { //create board and initialise, all initialised to 0      
        
        switch(a) {
            case "place" :
                place(b, p);
                break;
            case "promote" :
                promote(b, p);
                break;
            case "remove" :
                remove(b, p);
                break;
            default:
                System.out.println("Not a valid action! Valid actions are: place, promote and remove");  
                return false;
        }
        return true;
    }

    private void place(Board b, int player) {
        String c = null;
        boolean result = false;
        
        while (result == false) {
            System.out.println("Please enter the board coordinates where you would like to place your sphere: ");
                try {            
                    c = user_input.next();

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                if(validCoordinate(c)) {
                    if(validMove(c, b, Action.PLACE, player)) {
                        result = executeMove(c, b, Action.PLACE, player);
                        if(result) {
                            break;
                        }
                        System.err.println("ERROR: was unable to place sphere.");
                    }
                    System.out.println("Move is not valid.");    
                    result = false;
                }
                System.out.println("Please enter a valid board coordinate:");
                System.out.println(" -The bottom tier is squares [a,b,c,d][1-4]");
                System.out.println(" -The second tier up is squares [e,f,g][1-3]");
                System.out.println(" -The third tier up is squares [h,i][1-2]");
                System.out.println(" -The top tier is square j1");
        }
        System.out.println("Succesfully places sphere at coordinates: " + c);
    }

    private boolean promote(Board b, int player) {
        return true;
    }

    private boolean remove(Board b, int player) {
        return true;
    } 
    
    //checks if the entered coordinate is an actual coordinate
    private boolean validCoordinate(String coordinate) {
        if(coordinate.length() == 2) 
            if("abcd".contains(coordinate.substring(0, 1))) {
                return "1234".contains(coordinate.substring(1, 2));
            } else if("efg".contains(coordinate.substring(0, 1))) {
                return "123".contains(coordinate.substring(1, 2));
            } else if("hi".contains(coordinate.substring(0, 1))) {
                return "12".contains(coordinate.substring(1, 2));
            } else if("j".equals(coordinate.substring(0, 1))) {
                return "1".equals(coordinate.substring(1,2));
        }
        return false;
    }

    //checks if the entered coordinate is an actual coordinate
    private boolean validMove(String coordinate, Board b, Action a, int player) {
        return true;
    }

    //checks if the entered coordinate is an actual coordinate
    private boolean executeMove(String coordinate, Board b, Action a, int player) {
        return true;
    }   
    
    public static void main(String[] args) {
        Pylos game = new Pylos();
        game.idle();
    }
    
}