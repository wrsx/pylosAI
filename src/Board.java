
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tom
 */
public class Board extends JFrame {
    
    int[][] level0;
    int[][] level1;
    int[][] level2;
    int[][] level3;
    
    int whiteSpheres;
    int blackSpheres;
    
    
    public Board() { //create board and initialise, all initialised to 0
        level0 = new int[4][4];
        level1 = new int[3][3];
        level2 = new int[2][2];
        level3 = new int[1][1];
        
        whiteSpheres = 15;
        blackSpheres = 15;        
    }

    private void paintLevel(Graphics g, int size, int offset, int[][] level) {
        
        //Draw board 
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(100+offset, 100, size*100, size*100); 
        g.setColor(Color.DARK_GRAY);
        g.drawRect(100+offset, 100, size*100, size*100); 
        for(int x = 100+offset; x <= 100*size+offset; x+=200){ 
            for(int y = 100; y <= 100*size; y+=200){ 
                g.setColor(Color.DARK_GRAY);
                g.drawRect(x, y, 100, 100); 

            }
        }
        for(int x = 200+offset; x <= 100*size+offset; x+=200){ 
            for(int y = 200; y <= 100*size; y+=200){ 
                g.setColor(Color.DARK_GRAY);
                g.drawRect(x, y, 100, 100); 
 
            }
        }

        //Draw spheres
        for(int i = 0; i<size; i++) {
            for(int j = 0; j<size; j++) {
                int player = level[i][j];
                if(player > 0){
                    g.setColor(Color.WHITE);
                    g.fillOval((i+1)*100+5+offset, (j+1)*100+5, 90, 90);
                    g.setColor(Color.BLACK);
                    g.drawOval((i+1)*100+5+offset, (j+1)*100+5, 90, 90);
                }
                else if(player < 0){
                    g.setColor(Color.BLACK);
                    g.fillOval((i+1)*100+5+offset, (j+1)*100+5, 90, 90);
                    g.drawOval((i+1)*100+5+offset, (j+1)*100+5, 90, 90); 
                } 
            }
        }        
    }      
    
    public void paint(Graphics g){

        g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 14));
        int offset = 0;
        paintLevel(g, 4, 0, level0);
        g.setColor(Color.BLACK);
        g.drawString("a", 145, 80);
        g.drawString("b", 245, 80);
        g.drawString("c", 345, 80);
        g.drawString("d", 445, 80);        
        g.drawString("1", 75, 150);
        g.drawString("2", 75, 250);
        g.drawString("3", 75, 350);  
        g.drawString("4", 75, 450);
        g.drawString("Level 0", 280, 530);  
        
        offset = 500;        
        paintLevel(g, 3, offset, level1);
        g.setColor(Color.BLACK);
        g.drawString("e", 145+offset, 80);
        g.drawString("f", 245+offset, 80);
        g.drawString("g", 345+offset, 80);
        g.drawString("1", 75+offset, 150);
        g.drawString("2", 75+offset, 250);
        g.drawString("3", 75+offset, 350);  
        g.drawString("Level 1", 730, 430);  
        
        offset = 900; 
        paintLevel(g, 2, offset, level2);
        g.setColor(Color.BLACK);
        g.drawString("h", 145+offset, 80);
        g.drawString("i", 245+offset, 80);
        g.drawString("1", 75+offset, 150);
        g.drawString("2", 75+offset, 250);           
        g.drawString("Level 2", 1080, 330); 
        
        offset = 1200;
        paintLevel(g, 1, offset, level3);
        g.setColor(Color.BLACK);
        g.drawString("j", 145+offset, 80);
        g.drawString("1", 75+offset, 150);
        g.drawString("Level 3", 1330, 230);
        
        //Draw scoreboard
        g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 30));

        g.setColor(Color.WHITE);
        g.fillOval(1330, 350, 100, 100); 
        
        g.setColor(Color.BLACK);
        g.fillOval(1330, 470, 100, 100); 
        g.drawOval(1330, 350, 100, 100); 
        
        g.drawString(Integer.toString(whiteSpheres), 1364, 415);
        g.setColor(Color.WHITE);
        g.drawString(Integer.toString(blackSpheres), 1364, 530);
    }
    
}
