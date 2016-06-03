
import java.util.ArrayList;


public class AI {
        final static int MAX = -1;
        final static int MIN = 1;
        Board node;
        Board bestBoard;



        public Board findBestBoard(int maxDepth, Board root, int player) {
            Board alpha = new Board(root);
            alpha.score = Integer.MIN_VALUE;
            Board beta = new Board(root);
            beta.score = Integer.MAX_VALUE;
            return alphaBeta(root, maxDepth, alpha, beta, player);
            //return bestBoard;
        }
        
        public Board alphaBeta(Board node, int depth, Board alpha, Board beta, int player) {
            node.score = node.getBoardScore(player);
            if (depth == 0 || node.gameFinished()) {
                
                return node;
            }

            ArrayList<Board> childNodes = node.getPossibleBoards(player); //All valid moves from current the board state
            if (player == MAX) {
                for (Board currentBoard: childNodes) {
                    currentBoard.score = currentBoard.getBoardScore(player);
                    Board result = alphaBeta(currentBoard, depth-1, alpha, beta, -player);      
                    if (alpha.score < result.score) {
                        alpha = result;
                    }
                    if (beta.score <= alpha.score) {
                        break; //alpha cut-off                            
                    }
                }
            return alpha;
            }
            else { 
                for (Board currentBoard: childNodes) {
                    currentBoard.score = currentBoard.getBoardScore(player);
                    Board result = alphaBeta(currentBoard, depth-1, alpha, beta, -player);
                    if (beta.score > result.score) {
                        beta = result;
                    }
                    if (beta.score <= alpha.score) {
                            break; //alpha cut-off
                    }                            
                }
                return beta;
            }
        }        


}
