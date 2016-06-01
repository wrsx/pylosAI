/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tom
 */
public class Coordinate {
    
    public int firstIndex;
    public int secondIndex;
    public int level;
    public String value;
    public Board gameBoard;
    
    Coordinate(String coordinate, Board board) {
        gameBoard = board;
        value = coordinate;
        firstIndex = letterToIndex(coordinate);
        secondIndex = Integer.parseInt(coordinate.substring(1, 2))-1;
        level = getLevel(coordinate);     
    }
    
    //checks if the entered coordinate is an actual coordinate
    private static boolean validCoordinate(String coordinate) {
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
    
    //translates the letter coordinate into a real array index
    private static int letterToIndex(String coordinate) {
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
    
    private static int getLevel(String coordinate) {        
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
    
 
}
