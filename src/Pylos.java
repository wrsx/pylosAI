import java.awt.Color;
import java.awt.GridLayout;
import java.util.*;
import javax.swing.JFrame;

public class Pylos {
    
    public int currentPlayer;
    public Board gameBoard;
    private final Scanner user_input;
    
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

        user_input = new Scanner(System.in);
    }
    
    private enum Action {
        PLACE, PROMOTE, REMOVE, DEFAULT;
    }
 
    public void idle() {     
        //continue to allow moves until a player runs out of spheres or reaches the top of the pyramid
        while (gameBoard.whiteSpheres > 0 && gameBoard.blackSpheres > 0 && gameBoard.level3[0][0] == 0) {

                    
            System.out.println("\nPlease enter an action: ");
                try {            
                    if(move(user_input.next(), currentPlayer)) {
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
        user_input.close();
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
    
    private boolean move(String a, int p) { //create board and initialise, all initialised to 0      
        switch(a) {
            case "place" :
                return place(p);
            case "promote" :
                return promote(p);
            default:
                System.out.println("Not a valid action!\nValid actions are: place and promote");  
                return false;
        }
    }

    private boolean place(int player) {
        String c = null;
        boolean result = false;
        
        System.out.println("Please enter the board coordinates where you would like to place your sphere: ");
        try {            
            c = user_input.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(validCoordinate(c)) {
            if(validMove(Action.PLACE, player, c, null)) {
                result = executeMove(Action.PLACE, player, c, null);
                if(result) {
                    System.out.println("Succesfully placed sphere at coordinates: " + c);
                } else System.err.println("ERROR: was unable to place sphere.");
            }
        }
        return result;
    }

    private boolean promote(int player) {
        String oc = null; //old coordinate
        String nc = null; //new coordinate
        boolean result = false;
        
        System.out.println("Please enter the board coordinates of the sphere you would like to promote: ");
        try {            
            oc = user_input.next();
            System.out.println("Please enter the new board coordinates for the sphere: ");
            nc = user_input.next();
        } catch (Exception e) {
            e.printStackTrace();
        }        
        if(validCoordinate(nc) && validCoordinate(oc)) {
            if(validMove(Action.PROMOTE, player, nc, oc)) {
                result = executeMove(Action.PROMOTE, player, nc, oc);
                if(result) {
                    System.out.println("Succesfully promoted sphere from coordinates: " + oc + " to coordinates: " + nc);
                } else System.err.println("ERROR: was unable to promote sphere.");
            } 
        }
        return result;
    }
    
    private boolean remove(int player) {
        String c = null;
        boolean result = false;
        int deleting = 0;
        System.out.println("Please enter the number of spheres you would like to delete: 1, 2 or 0");
        try {
            deleting = Integer.parseInt(user_input.next());
            while(deleting > 2 || deleting < 0) {
                System.out.println("Please enter a valid number: 1, 2, or 0");
                deleting = Integer.parseInt(user_input.next());
            }
        } catch (Exception e) {
            System.err.printf("Number of spheres to be deleted not specified");
        }
        while(deleting != 0) {
            try {
                System.out.println("Please enter the board coordinates of the sphere you would like to remove: ");            
                c = user_input.next();
            } catch (Exception e) {
                e.printStackTrace();
            }          
            if(validCoordinate(c)) {
                if(validMove(Action.REMOVE, player, null, c)) {
                    result = executeMove(Action.REMOVE, player, null, c);
                    if(result) {
                        System.out.println("Succesfully removed sphere from coordinates: " + c);
                        deleting -= 1;
                    } else System.err.println("ERROR: was unable to remove sphere.");
                }
            }
        }
        return result;
    }
    
    //checks if the entered coordinate is an actual coordinate
    private boolean validCoordinate(String coordinate) {
        boolean valid = false;
        if(coordinate.length() == 2) 
            if("abcd".contains(coordinate.substring(0, 1))) {
                valid = "1234".contains(coordinate.substring(1, 2));
            } else if("efg".contains(coordinate.substring(0, 1))) {
                valid = "123".contains(coordinate.substring(1, 2));
            } else if("hi".contains(coordinate.substring(0, 1))) {
                valid = "12".contains(coordinate.substring(1, 2));
            } else if("j".equals(coordinate.substring(0, 1))) {
                valid = "1".equals(coordinate.substring(1,2));
        }
        if(valid) return true;
        System.out.println("Please enter a valid board coordinate:");
        System.out.println(" -The bottom tier is squares [a,b,c,d][1-4]");
        System.out.println(" -The second tier up is squares [e,f,g][1-3]");
        System.out.println(" -The third tier up is squares [h,i][1-2]");
        System.out.println(" -The top tier is square j1");        
        return false;
    }

    //checks if the entered coordinate is an actual coordinate
    private boolean validMove(Action a, int player, String newCoordinate, String oldCoordinate) {
        int level;
        boolean valid = false;
        
        
        if(newCoordinate != null) {
            level = getLevel(newCoordinate);
            
            //Promotion move, first we check whether the new level is greater than the old one
            if(oldCoordinate != null) {
                if(level <= getLevel(oldCoordinate)) {
                    System.out.println("You can only move spheres to a higher level.");
                    return false;
                }
            }
            
            //get the actual matrix index from the coordinates
            int firstIndex = letterToIndex(newCoordinate);
            int secondIndex = Integer.parseInt(newCoordinate.substring(1, 2))-1;
            
            //check whether the position is already occupied
            if(getLevelTable(level)[firstIndex][secondIndex] != 0) {
                System.out.println("This coordinate already contains a sphere!");
                return false;
            }
            //unoccupied so check if it needs other spheres to sit on top of
            if(level > 0) {
                //checks whether there are four spheres directly below
                if(canPlaceOnTop(oldCoordinate, firstIndex, secondIndex, getLevelTable(level-1))) {
                    valid = true; //we cant return true just yet, gotta make sure the old sphere can be moved (promotion move)
                } else {
                    System.out.println("Unable to place a sphere without four spheres below.");
                    return false;
                }
                
            }
            //Simply adding a sphere to level 0, no promotion so we can return
            valid = true;
        }
        
        if(oldCoordinate != null) {
            level = getLevel(oldCoordinate);        
            //get the actual matrix index from the coordinates
            int firstIndex = letterToIndex(oldCoordinate);
            int secondIndex = Integer.parseInt(oldCoordinate.substring(1, 2))-1;
            
           
            ///gets the current value of the that cell
            int spherePlayer = getLevelTable(level)[firstIndex][secondIndex];
            //check whether there is a sphere at those coordinates that can be moved/removed
            if(spherePlayer == 0) {
                System.out.println("This coordinate does not contain a sphere!");
                return false;
            }
            if(spherePlayer != currentPlayer) {
                System.out.println("You can only remove your own spheres.");
                return false;
            }
            //unocuppied so check if there are any spheres in the level above blocking its removal
            if(level < 3) {
                if(valid = freeAbove(firstIndex, secondIndex, level)) {
                } else System.out.println("Unable to change this sphere, it has a sphere above resting on it.");
            }
        }
        return valid;
    }
    
    //checks if their are four spheres below
    private boolean canPlaceOnTop(String oldCoordinate, int firstIndex, int secondIndex, int[][] levelBelow) {
        //first we check if the sphere being moved is one of the four supporting spheres
        if(oldCoordinate != null) {
            int oldFirstIndex = letterToIndex(oldCoordinate);
            int oldSecondIndex = Integer.parseInt(oldCoordinate.substring(1, 2))-1;            
            if(oldFirstIndex == firstIndex) {
                if(oldSecondIndex == secondIndex || oldSecondIndex == secondIndex + 1) return false;
            }
            if(oldFirstIndex == firstIndex + 1)
                if(oldSecondIndex == secondIndex || oldSecondIndex == secondIndex + 1) return false;
        }
        if(gameBoard.level0[firstIndex][secondIndex] != 0) {
            if(gameBoard.level0[firstIndex+1][secondIndex] != 0) {
                if(gameBoard.level0[firstIndex][secondIndex+1] != 0) {
                    if(gameBoard.level0[firstIndex+1][secondIndex+1] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    //checks whether there are any spheres in the level above stopping a sphere from being moved
    private boolean freeAbove(int firstIndex, int secondIndex, int level) {
        int[][] levelAbove = getLevelTable(level+1);
        //loop through all the spheres in the above level
        for(int i = 0; i < levelAbove.length; i++) {
            for(int j = 0; j < levelAbove.length; j++) {
                if(levelAbove[i][j] != 0) {
                    //check if the sphere is resting on our sphere 
                    if(i == firstIndex && j == secondIndex) {
                        return false;
                    }
                    else if(i+1 == firstIndex && j == secondIndex) {
                        return false;
                    }
                    else if(i == firstIndex && j+1 == secondIndex) {
                        return false;
                    } 
                    else if(i+1 == firstIndex && j+1 == secondIndex) {
                        return false;
                    }
                }
            }
        }
        return true;

    }
    
    //translates the letter coordinate into a real array index
    private int letterToIndex(String coordinate) {
        switch(getLevel(coordinate)) {
            case 0 :
                //returns the array index by getting the ascii value of the char and shifting it so a is 0, b is 1....
                return (int)coordinate.charAt(0) - 97;
            case 1 :
                return (int)coordinate.charAt(0) - 101;
            case 2 :
                return (int)coordinate.charAt(0) - 104;
            case 3 :
                return (int)coordinate.charAt(0) - 106;
            default :
                return -1;
        }        
    }
    
    //returns the 2D array from the board for that level
    private int[][] getLevelTable(int level) {
        switch(level) {
            case 0 :
                return gameBoard.level0;
            case 1 :
                return gameBoard.level1;
            case 2 :
                return gameBoard.level2;
            case 3 :
                return gameBoard.level3;
        }
        return null;
    }
    
    private boolean checkSpecialMove(String newCoordinate, int player) {
        int level = getLevel(newCoordinate);
        int[][] levelTable = getLevelTable(level);
        
        int firstIndex = letterToIndex(newCoordinate);
        int secondIndex = Integer.parseInt(newCoordinate.substring(1, 2))-1;
        
        //check for square
        for(int i = 0; i < levelTable.length-1; i++) {
            for(int j = 0; j < levelTable.length-1; j++) {
                //Check that the current cell is occupied by the current player
                if(levelTable[i][j] == player) {
                    //Check that our recently placed sphere is within 1 cell of the current sphere
                    if((i-firstIndex) < 2 && (j-secondIndex) < 2 ) {
                        //Check that the other cells in the square are the current players
                        if(levelTable[i+1][j] == player && levelTable[i][j+1] == player && levelTable[i+1][j+1] == player) {
                            System.out.println("Square formed!");
                            return true;
                        } 
                    }
                    
                }
            }
        }
        
        //Check for straight lines        
        int hcount = 0;
        int vcount = 0;
        for(int i = 0; i < levelTable.length; i++) {
            if(levelTable[firstIndex][i] == player) {
                vcount++;
            }
            if(levelTable[i][secondIndex] == player) {
                hcount++;
            }            
        }
        if(hcount == levelTable.length || vcount == levelTable.length) {
            System.out.println("Line of " + levelTable.length + " formed!");
            return true;
        } else return false;
    }
    
    

    //checks if the entered coordinate is an actual coordinate
    private boolean executeMove(Action a, int player, String newCoordinate, String oldCoordinate) {      
        switch(a) {
            case PLACE :
                setSquare(newCoordinate, player);
                if(player > 0) {
                    gameBoard.whiteSpheres += -1;
                } else  gameBoard.blackSpheres += -1;           
                if(checkSpecialMove(newCoordinate, player)) remove(player);
                break;
            case PROMOTE :
                setSquare(newCoordinate, player);
                setSquare(oldCoordinate, 0);
                if(checkSpecialMove(newCoordinate, player)) remove(player);
            case REMOVE :
                setSquare(oldCoordinate, 0);
                if(player > 0) {
                    gameBoard.whiteSpheres += 1;
                } else  gameBoard.blackSpheres += 1;
                gameBoard.repaint();                
                break;
        }
        return true;
    }
    
    private void setSquare(String coordinate, int player) {
        
        int[][] level = getLevelTable(getLevel(coordinate));    
        level[letterToIndex(coordinate)][Integer.parseInt(coordinate.substring(1, 2))-1] = player;
        gameBoard.repaint();
    }
    
    private int getLevel(String coordinate) {
        
        if("abcd".contains(coordinate.substring(0, 1))) {
            return 0;
        } else if("efg".contains(coordinate.substring(0, 1))) {
           return 1;
        } else if("hi".contains(coordinate.substring(0, 1))) {
            return 2;
        } else if("j".equals(coordinate.substring(0, 1))) {
            return 3;
        }
        return -1;
    }
    
    
    public static void main(String[] args) {
        Pylos game = new Pylos();
        game.idle();
        System.exit(0);
    }
    
}