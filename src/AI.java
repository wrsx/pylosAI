
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JFrame;


public class AI {
        final static int MAX = 1;
        final static int MIN = -1;
        Board node;
        Move[] bestFound;
      
        public Board bestBoard(Board state, int depth, int player) {
            //create empty (invalid) move to hold best move

            return alpha_beta(depth, state, Integer.MIN_VALUE, Integer.MAX_VALUE, player);


                    }
        
        private Board alpha_beta(final int depth, Board b, int alpha, int beta, int player) {
            if (depth == 0) {
                b.score = b.getBoardScore(-player);
                return b;
            }
            if(player == MAX) {
                Board v = new Board(b);
                v.score = Integer.MIN_VALUE;
                ArrayList<Board> boards = b.getPossibleBoards(player);
                for (Board currentBoard: boards) {
                    currentBoard.score = currentBoard.getBoardScore(player);

                    Board result = alpha_beta(depth - 1, currentBoard, beta, alpha, -player);

                    if (v.score < result.score) {
                        v = currentBoard;
                    }
                    if(alpha < v.score) {
                        alpha = v.score;
                    }
                    if (beta <= alpha) {
                        break;
                    }
                }
                return v;
            }
            else {
                Board v = new Board(b);
                v.score = Integer.MAX_VALUE;
                ArrayList<Board> boards = b.getPossibleBoards(player);
                for (Board currentBoard: boards) {
                    currentBoard.score = currentBoard.getBoardScore(player);                   
                    Board result = alpha_beta(depth - 1, currentBoard, beta, alpha, -player);                  
                    if (v.score > result.score) {
                        v = currentBoard;
                    }
                    if(beta > v.score) {
                        beta = v.score;
                    }
                    if (beta <= alpha) {
                        break;
                    }
                }
                return v;                
            }
        }        
        
        
    /*public Board bestBoard(Board state, int depth, int player) {
        //create empty (invalid) move to hold best move
        Move[] best = new Move[3];
        bestFound = best;
        minimax(state, best, Integer.MIN_VALUE, Integer.MAX_VALUE, depth, player);

        state.executeMoveSet(bestFound);
        return state;
                }
    
    private int minimax(Board board, Move[] best, int alpha, int beta, int depth, int player)
    {             
        Move[] garbage = new Move[3];             

        if (board.gameFinished())
        {                        
            return board.getBoardScore(player);
        }

        ArrayList<Move[]> moves = board.getValidMoveSets(player);             

        for (Move[] mv : moves)
        {          
            Board executed = new Board(board);
            executed.executeMoveSet(mv);          
            int score = - minimax(board, garbage, -beta,  -alpha, depth - 1, -player);                     

            if(score > alpha)
            {  
                //Set Best move here
                alpha = score;                
                best = mv;
                           
            }

            if (alpha >= beta)
                break;                

        }                
        return alpha;
    }
    
    Board rootAlphaBeta(Board b, int depth, int player) {
            Board.enableStdout(false);
            Board backup = new Board(b);
            Move[] best_move = null;
            int max_eval = Integer.MIN_VALUE;

            ArrayList<Move[]> moves = b.getValidMoveSets(player);             
            for (Move[] mv : moves) {
                Board executed = new Board(backup);
                executed.executeMoveSet(mv);  
                int current_eval = - alphaBeta(executed, depth - 1, max_eval, Integer.MAX_VALUE, player);

                if (current_eval > max_eval) {
                    max_eval = current_eval;
                    best_move = mv;
}               }
            backup.executeMoveSet(best_move);
            Board.enableStdout(true);
            return backup;
     }
                    
    public int alphaBeta(Board node, int depth, int alpha, int beta, int player) {

        if (depth == 0 || node.gameFinished()) {
            return node.getBoardScore(player);
        }

        ArrayList<Move[]> childNodes = node.getValidMoveSets(player); //All valid moves from current the board state
        if (player == MAX) {
            for (Move[] currentMove: childNodes) {
                Board executed = new Board(node);
                executed.executeMoveSet(currentMove);                  
                int result = alphaBeta(executed, depth-1, alpha, beta, -player);      
                if (alpha < result) {
                    alpha = result;
                }
                if (beta <= alpha) {
                    break; //alpha cut-off                            
                }
            }                    
        return alpha;
        }
        else { 
            for (Move[] currentMove: childNodes) {
                Board executed = new Board(node);
                executed.executeMoveSet(currentMove);   
                int result = alphaBeta(executed, depth-1, alpha, beta, -player);
                if (beta > result) {
                    beta = result;
                }
                if (beta <= alpha) {
                        break; //alpha cut-off
                }                            
            }
            return beta;
        }
    }*/
}
