
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tom
 */
public class Board {
    int[][] level0;
    int[][] level1;
    int[][] level2;
    int level3;
    
    
    public Board() { //create board and initialise, all initialised to 0
        level0 = new int[4][4];
        level1 = new int[3][3];
        level1 = new int[2][2];
        level3 = 0;
    }
    
}
