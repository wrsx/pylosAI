
import java.util.ArrayList;


public class AI {
        final static int MAX = 1;
        final static int MIN = -1;
        Board node;
        Board bestBoard;

        
        /*Returns the score of the best move*/
        public int alphaBeta(Board node, int depth, int a, int b, int player) {
                if (depth == 0 || node.gameFinished()) {
                        return node.getBoardScore();
                }                
                ArrayList<Board> childNodes = node.getPossibleBoards(player);
                if (player ==  MAX) {
                            for (int i = 0; i < childNodes.size(); i++) {
                                    a = max(a, alphaBeta(childNodes.get(i), depth - 1, a, b, -player), node, childNodes.get(i));
                                    if (b <= a) {
                                            break;
                                    }
                            }
                            return a;
                }
                else {
                        for (int i = 0; i < childNodes.size(); i++) {
                                b = min(b, alphaBeta(childNodes.get(i), depth - 1, a, b, -player), node, childNodes.get(i));
                                if (b <= a) {
                                        break;
                                }
                        }
                        return b;                                    
                }               
        }
        
        private int min(int b, int alphaBeta, Board node, Board child) {
                if (b > alphaBeta) {
                        bestBoard = child;
                        return alphaBeta;
                }
                else {
                        return b;
                }
        }
        
        private int max(int a, int alphaBeta, Board node, Board child) {
                if (a > alphaBeta) {
                        bestBoard = child;
                        return alphaBeta;
                }
                else {
                        return a;
                }
        }        
}
