
public class AI {
        final static boolean MAX = true;
        final static boolean MIN = false;
        Board node;
        Board bestMove;
        int depth = 4;
        
        public AI(Board board) {
                node = board;
        }
        /*Returns the score of the best move*/
        public int alphaBeta(Board node, int depth, int a, int b, boolean player) {
                if (depth == 0 || node.gameFinished()) {
                        return node.Score();
                }                
                ArrayList<Board> childNodes = node.getPosibleBoards();
                if (player ==  MAX) {
                            for (int i = 0; i < childNodes.size(); i++) {
                                    a = max(a, alphaBeta(childNodes[i], depth - 1, a, b, !player), node, childNodes[i]);
                                    if (b <= a) {
                                            break;
                                    }
                            }
                            return a;
                }
                else {
                        for (int i = 0; i < childNodes.size(); i++) {
                                b = min(b, alphaBeta(childNodes[i], depth - 1, a, b, !player), node, childNodes[i]);
                                if (b <= a) {
                                        break;
                                }
                        }
                        return b;                                    
                }               
        }
        
        private int min(int b, int alphaBeta, Board node, Board child) {
                if (b > alphaBeta) {
                        bestMove = child;
                        return alphaBeta;
                }
                else {
                        return b;
                }
        }
        
        private int max(int a, int alphaBeta, Board node, Board child) {
                if (a > alphaBeta) {
                        bestMove = child;
                        return alphaBeta;
                }
                else {
                        return a;
                }
        }        
}
