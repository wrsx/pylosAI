public class StateNode {
	public Move[] ply;
	public Board state;
	public StateNode bestMove;

	public StateNode(Move[] ply, Board state) {
		this.ply = ply;
		this.state = state;
	}

	public Move[] getBestMove() {
		return bestMove != null ? bestMove.ply : null;
	}
}