
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
    
    public Board gameBoard;
    public Pylos.Action action;
    public int player;
    public Coordinate newCoordinate = null;
    public Coordinate oldCoordinate = null;
    
    Move(Board b, Pylos.Action a, int p, Coordinate nc, Coordinate oc) {
        gameBoard = b;
        action = a;
        player = p;
        newCoordinate = nc;
        oldCoordinate = oc;
    }
    
    void test() {
        if(newCoordinate != null && oldCoordinate == null) {
            gameBoard.lastmove = gameBoard.lastmove + " " + action + " " + newCoordinate.value;
        } else if(newCoordinate == null && oldCoordinate != null) {
            gameBoard.lastmove = gameBoard.lastmove + " " + action + " " + oldCoordinate.value;
        } else if(newCoordinate != null && oldCoordinate != null) {
            gameBoard.lastmove = gameBoard.lastmove + " " + action + " " + oldCoordinate.value + " " + newCoordinate.value;
        }
    }
        
    public boolean execute() {
        switch(this.action) {
            case PLACE :
                test();
                return place(this);
            case PROMOTE : 
                test();
                return promote(this);
            case REMOVE :
                test();
                return remove(this);
        }
        return false;        
    }

    private static boolean place(Move m) {      
        try {
            setSquare(m.gameBoard, m.newCoordinate, m.player);
            System.out.println("Succesfully placed sphere at coordinates: " + m.newCoordinate.value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean promote(Move m) {    
        try {
            setSquare(m.gameBoard, m.newCoordinate, m.player);
            setSquare(m.gameBoard, m.oldCoordinate, 0);
            System.out.println("Succesfully promoted sphere from coordinates: " + m.oldCoordinate.value + " to coordinates: " + m.newCoordinate.value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean remove(Move m) {     
        try {
            setSquare(m.gameBoard, m.oldCoordinate, 0); 
            System.out.println("Succesfully removed sphere from coordinates: " + m.oldCoordinate.value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //checks if the entered coordinate is an actual coordinate
    public static boolean checkValidMove(Move m) {
        boolean valid = false;
        if(m.newCoordinate != null) {
            //Promotion move, first we check whether the new level is greater than the old one
            if(m.oldCoordinate != null) {
                if(m.newCoordinate.level <= m.oldCoordinate.level) {
                    System.out.println("You can only move spheres to a higher level.");
                    return false;
                }
            }
            //check whether the position is already occupied
            if(m.gameBoard.getLevelTable(m.newCoordinate.level)[m.newCoordinate.firstIndex][m.newCoordinate.secondIndex] != 0) {
                System.out.println("This coordinate already contains a sphere!");
                return false;
            }
            //unoccupied so check if it needs other spheres to sit on top of
            if(m.newCoordinate.level > 0) {
                //checks whether there are four spheres directly below
                if(canPlaceOnTop(m)) {
                    valid = true; //we cant return true just yet, gotta make sure the old sphere can be moved (promotion move)
                } else {
                    System.out.println("Unable to place a sphere without four spheres below.");
                    return false;
                }
                
            }
            //Simply adding a sphere to level 0, no promotion so we can return
            valid = true;
        }
        
        if(m.oldCoordinate != null) {
            ///gets the current value of the that cell
            int spherePlayer = m.gameBoard.getLevelTable(m.oldCoordinate.level)[m.oldCoordinate.firstIndex][m.oldCoordinate.secondIndex];
            //check whether there is a sphere at those coordinates that can be moved/removed
            if(spherePlayer == 0) {
                System.out.println("This coordinate does not contain a sphere!");
                return false;
            }
            if(spherePlayer != m.player) {
                System.out.println("You can only remove your own spheres.");
                return false;
            }
            //unocuppied so check if there are any spheres in the level above blocking its removal
            if(m.oldCoordinate.level < 3) {
                if(valid = freeAbove(m)) {
                } else System.out.println("Unable to change this sphere, it has a sphere above resting on it.");
            }
        }
        return valid;
    }
    
    //checks if their are four spheres below
    private static boolean canPlaceOnTop(Move m) {
        
        int[][] levelBelow = m.gameBoard.getLevelTable(m.newCoordinate.level-1);
        //first we check if the sphere being moved is one of the four supporting spheres
        if(m.oldCoordinate != null) {          
            if(m.oldCoordinate.firstIndex == m.newCoordinate.firstIndex) {
                if(m.oldCoordinate.secondIndex == m.newCoordinate.secondIndex || m.oldCoordinate.secondIndex == m.newCoordinate.secondIndex + 1) return false;
            }
            if(m.oldCoordinate.firstIndex == m.newCoordinate.firstIndex + 1)
                if(m.oldCoordinate.secondIndex == m.newCoordinate.secondIndex || m.oldCoordinate.secondIndex == m.newCoordinate.secondIndex + 1) return false;
        }
        if(levelBelow[m.newCoordinate.firstIndex][m.newCoordinate.secondIndex] != 0) {
            if(levelBelow[m.newCoordinate.firstIndex+1][m.newCoordinate.secondIndex] != 0) {
                if(levelBelow[m.newCoordinate.firstIndex][m.newCoordinate.secondIndex+1] != 0) {
                    if(levelBelow[m.newCoordinate.firstIndex+1][m.newCoordinate.secondIndex+1] != 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    //checks whether there are any spheres in the level above stopping a sphere from being moved
    private static boolean freeAbove(Move m) {
        //first get the level above the sphere being moved/deleted
        int[][] levelAbove = m.gameBoard.getLevelTable(m.oldCoordinate.level+1);
        //loop through all the spheres in the above level
        for(int i = 0; i < levelAbove.length; i++) {
            for(int j = 0; j < levelAbove.length; j++) {
                if(levelAbove[i][j] != 0) {
                    //check if the sphere is resting on our sphere 
                    if(i == m.oldCoordinate.firstIndex && j == m.oldCoordinate.secondIndex) {
                        return false;
                    }
                    else if(i+1 == m.oldCoordinate.firstIndex && j == m.oldCoordinate.secondIndex) {
                        return false;
                    }
                    else if(i == m.oldCoordinate.firstIndex && j+1 == m.oldCoordinate.secondIndex) {
                        return false;
                    } 
                    else if(i+1 == m.oldCoordinate.firstIndex && j+1 == m.oldCoordinate.secondIndex) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean checkSpecialMove(Move m) {
        int[][] levelTable = m.gameBoard.getLevelTable(m.newCoordinate.level);
        boolean special = false;
        
        //temporarily place the sphere on the board so we can run some checks
        int origValue = levelTable[m.newCoordinate.firstIndex][m.newCoordinate.secondIndex];
        levelTable[m.newCoordinate.firstIndex][m.newCoordinate.secondIndex] = m.player;
        
        //check for square
        for(int i = 0; i < levelTable.length-1; i++) {
            for(int j = 0; j < levelTable.length-1; j++) {
                //Check that the current cell is occupied by the current player
                if(levelTable[i][j] == m.player) {
                    //Check that our recently placed sphere is within 1 cell of the current sphere
                    if((i-m.newCoordinate.firstIndex) < 2 && (j-m.newCoordinate.secondIndex) < 2 ) {
                        //Check that the other cells in the square are the current players
                        if(levelTable[i+1][j] == m.player && levelTable[i][j+1] == m.player && levelTable[i+1][j+1] == m.player) {
                            System.out.println("Square formed!");
                            special = true;
                        } 
                    }
                    
                }
            }
        }
        
        //Check for straight lines        
        int hcount = 0;
        int vcount = 0;
        for(int i = 0; i < levelTable.length; i++) {
            if(levelTable[m.newCoordinate.firstIndex][i] == m.player) {
                vcount++;
            }
            if(levelTable[i][m.newCoordinate.secondIndex] == m.player) {
                hcount++;
            }            
        }
        if(hcount == levelTable.length || vcount == levelTable.length) {
            System.out.println("Line of " + levelTable.length + " formed!");
            special = true;
        }
        //remove the temporarily added sphere from the board as we've run the checks
        levelTable[m.newCoordinate.firstIndex][m.newCoordinate.secondIndex] = origValue;
        return special;
    }
    
    private static void setSquare(Board gameBoard, Coordinate newCoordinate, int value) {        
        int[][] level = gameBoard.getLevelTable(newCoordinate.level);    
        level[newCoordinate.firstIndex][newCoordinate.secondIndex] = value;
        gameBoard.repaint();
    }         
}
