package checkers;
/* A Checkers play that plays randomly
 * @author Iris Shaker-Check
 * basted off of random Mancala Player by Michael Skalak
 * @version Dec. 2 2020
 */

import java.util.ArrayList;
import java.util.Random;

public class RandomPlayer extends CheckersPlayer{
	
	Random thisRandom = new Random();

	@Override
	public Move getMove(GameState gs, long deadline) {
		
		ArrayList<Move> legalMoves = gs.getLegalMoves();
		Move m = legalMoves.get(thisRandom.nextInt(legalMoves.size()));
		return m;
	}
	public String toString() {
		return "Random";
	}
}
