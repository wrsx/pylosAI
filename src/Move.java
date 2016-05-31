
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tom
 */
public class Move {
    
    Scanner user_input;
    
    private enum Action {
        PLACE, PROMOTE, REMOVE, DEFAULT;
    }     
    
    
    public Move(String a, Board b, int player) { //create board and initialise, all initialised to 0
        
        user_input = new Scanner(System.in);
        
        switch(a) {
            case "place" :
                    break;
            case "promote" :
                    break;
            case "remove" :
                    break;
            default:
                System.err.println("Not a valid move!");         
        }
    }

    private boolean place(Board b, int player) {
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
                    if(validMove(c, b, Action.PLACE)) {
                        result = executeMove(c, b, Action.PLACE);
                        if(!result) System.err.println("ERROR: was unable to place sphere.");
                    }
                    System.out.println("Move is not valid.");    
                    result = false;
                }
        }
        System.out.println("Succesfully places sphere at coordinates: " + c);
        return true;
    }

    private boolean promote(Board b, int player) {
        return true;
    }

    private boolean remove(Board b, int player) {
        return true;
    } 
    
    //checks if the entered coordinate is an actual coordinate
    private boolean validCoordinate(String coordinate) {
        return true;
    }

    //checks if the entered coordinate is an actual coordinate
    private boolean validMove(String coordinate, Board b, Action a) {
        return true;
    }

    //checks if the entered coordinate is an actual coordinate
    private boolean executeMove(String coordinate, Board b, Action a) {
        return true;
    }    
    
}
