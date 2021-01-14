package checkers;
/* Parent class of all the players
 * @author Iris Shaker-Check
 * based off of MancolaPlayer by Michael Skalak Sep. 9 2020
 * @version Dec. 2 2020
 */

import java.util.ArrayList;

public abstract class CheckersPlayer {
	//when test is true, will print information to help debug
	boolean test;
	//name of the player
	String name;
	/*
	 * @param gs
	 * 		the current state of the game
	 * @param deadline
	 * 		how long the player has to find a move
	 * @return Move
	 * 		returns the move that was decided apon
	 */
	public abstract Move getMove(GameState gs, long deadline);
	
	/*
	 * @param test
	 * 			takes in a boolean value and sets field test to that value
	 */
	public void setTest(boolean test) {
		this.test = test;
	}
	/*
	 * @return
	 * 		the name of the player
	 */
	public String getName() {
		return this.name;
	}
	/*
	 * @returns 
	 * 		the string presentation of the player
	 */
	public String toString() {
		return this.name;
	}
	public ArrayList<Move> getLMoves(GameState gs){
		ArrayList<Move> legalMoves =gs.getLegalMoves();
		return legalMoves;
	}
}
