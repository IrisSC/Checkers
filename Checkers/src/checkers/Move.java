package checkers;
/*
 * A simple class to hold a move
 * @author Iris Shaker-check 
 * based of version by Micheal Skalak in Mancala game
 * @version Dec. 2 2020
 */
public class Move {
	//holds movement
	int[][] movement = new int[2][];
	
	public Move(int[][] move) {
		this.movement = move;
	}
	public String toString() {
		String print = " ";
		for (int i = 0; i < movement.length; i++) {
			print = print + "(" + movement[0][i] + ", " + movement[1][i] + ") ";
		}
		return print;
	}
	public boolean equals(Object other) {
		return movement == ((Move)other).movement;
	}
}
