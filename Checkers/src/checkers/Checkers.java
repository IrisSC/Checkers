package checkers;
/* Plays a match between two Checkers Players
 * 
 * @author Iris Shaker-check
 * basted off of code from Micheal Skalak Mancala
 * @version Dec. 2 2020
 */

public class Checkers {
	
	CheckersPlayer p1, p2, randomPlayer;
	
	int time;
	
	public static void main(String[] args) {
		RandomPlayer rp1 = new RandomPlayer();
		RandomPlayer rp2 = new RandomPlayer();
		MinMaxPlayer mm1 = new MinMaxPlayer(2);
		MinMaxPlayer mm2 = new MinMaxPlayer(2);
		AlphaBetaPlayer ab = new AlphaBetaPlayer(1);
		NullMovePruning nmp = new NullMovePruning(1);
		Checkers c = new Checkers(nmp, ab, 1200);
		c.playGame();
	}
	
	public Checkers(CheckersPlayer p1, CheckersPlayer p2, int time) {
		this.p1 = p1;
		this.p2 = p2;
		this.time = time;
	}
	
	public long playGame() {
		return playGame(true);
	}
	/* 
	 * This method was taken from Mancala by Micheal Skalak
	 */
	private long playGame(boolean p1IsBottom) {
		int moveCount = 0;
		CheckersPlayer bottom, top;
		if(p1IsBottom) {
			bottom = p1;
			top = p2;
		}else {
			bottom = p2;
			top = p1;
		}
		GameState g = new GameState();
		g.printBoard();
		while(!(g.isGameOver()) && !(p1.getLMoves(g).size() == 0) && 
				!(p2.getLMoves(g).size() ==0)) {
			moveCount++;
			GameThread t;
			if (g.isBottomTurn) {
				t = new GameThread(bottom,g,time);
				System.out.println("is bottoms turn");
			} else {

				t = new GameThread(top,g,time);
				System.out.println("is tops turn");
			}
		//	System.out.println("trying ");
			t.run();
			
			int segments =0;
			while(t.m==null && segments <8) {
				try {
					Thread.sleep(time/8);
					segments++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if(t.m == null){
				System.out.println("Player took too long! " + t.p);
				System.out.println("Making random move...");
				t.m=randomPlayer.getMove(g,time);
			}
			g = g.makeMove(t.m);
			g.printBoard();
			System.out.println("Top is " + top);
			System.out.println("Bottom is " + bottom);
		}
		double finalScore = g.currentScore();
		System.out.println( "Game over!");
		System.out.println("Final net score is " + finalScore );
		
		if(finalScore > 0) {
			System.out.println(bottom + " wins!");
		}else if(finalScore <0) {
			System.out.println(top + " wins!");
		}else {
			System.out.println("It's a tie!");
		}
		return (long)g.currentScore();
	}
	/*
	 * this class was taken from Mancala by Micheal Skalak
	 */
	private class GameThread implements Runnable{
		
		CheckersPlayer p;
		GameState g;
		int time;
		Move m=null;
		
		public GameThread(CheckersPlayer p, GameState g, int time) {
			this.p = p;
			this.g = g;
			this.time = time;
		}
		
		public void run() {
			//System.out.println("getting move");
			m = p.getMove(g, System.currentTimeMillis() + time);
		}
	}
}
