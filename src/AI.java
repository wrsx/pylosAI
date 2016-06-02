
import java.util.ArrayList;


public class AI {
        final static int MAX = 1;
        final static int MIN = -1;
        Board node;
        Board bestBoard;



        public Board findBestBoard(int maxDepth, Board root, int player) {
            root.bestBoard = null;
            alpha_beta(maxDepth, root, Integer.MIN_VALUE, Integer.MAX_VALUE, player);
            return root.bestBoard;
        }

        private int alpha_beta(final int depth, Board b, int alpha, int beta, int player) {
            if (depth == 0) {
                return b.getBoardScore(player);
            }

            ArrayList<Board> boards = b.getPossibleBoards(player);
            for (Board currentBoard: boards) {
                int score = -alpha_beta(depth - 1, currentBoard, -beta, -alpha, -player);
                if (alpha < score) {
                    alpha = score;
                    b.bestBoard = currentBoard;
                }
                if (beta <= alpha) {
                    return alpha;
                }
            }
            return alpha;
        } 
}
