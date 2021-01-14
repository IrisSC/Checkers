package checkers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/*
 * @author Iris Shaker-Check
 * basted off of MancalaCageMatch by Micheal Skalak
 * @version Dec. 2 2020
 */

public class CheckersCageMatch {
	
	Random rm = new Random();
	
	ArrayList<SortablePlayer> thePlayers;
	
	public  static void main(String[] args) {
		RandomPlayer rp = new RandomPlayer();
		MinMaxPlayer mm = new MinMaxPlayer(1);
		AlphaBetaPlayer ab = new AlphaBetaPlayer(1);
		NullMovePruning nmp = new NullMovePruning(1);
		
		CheckersCageMatch ccm = new CheckersCageMatch();
		ccm.addPlayer(mm);
		ccm.addPlayer(rp);
		ccm.addPlayer(ab);
		ccm.addPlayer(nmp);
		
		ccm.runGames();
	}
	public CheckersCageMatch() {
		thePlayers = new ArrayList<>();
	}
	public void addPlayer(CheckersPlayer c) {
		thePlayers.add(new SortablePlayer(c));
	}
	/*
	 * most of this method is taken from MancalaCageMatch by Micheal Skalak
	 */
	public void runGames() {
		int roundCount = 0;
		while(roundCount < 5) {
			roundCount++;
			for (int i = 0; i < thePlayers.size(); ++i) {
				for (int j = i + 1; j < thePlayers.size(); ++j) {

					SortablePlayer sp1 = thePlayers.get(i);
					SortablePlayer sp2 = thePlayers.get(j);
					System.out.println(sp1.myPlayer + " versus " + sp2.myPlayer);
					Checkers m = new Checkers(sp1.myPlayer, sp2.myPlayer, 800);
					long p1FirstScore = m.playGame();
					m = new Checkers(sp2.myPlayer, sp1.myPlayer, 800);
					long p2FirstScore = m.playGame();
					long totalScore = p1FirstScore - p2FirstScore;
					sp1.gamesPlayed++;
					sp2.gamesPlayed++;
					if (totalScore > 0) {
						System.out.println(
								sp1.myPlayer + " beats " + sp2.myPlayer + " " + p1FirstScore + " to " + p2FirstScore);
						sp1.wins++;
					} else if (totalScore == 0) {
						System.out.println(sp1.myPlayer + " and " + sp2.myPlayer + " tie");

						sp1.wins += .5;
						sp2.wins += .5;
					} else {
						System.out.println(
								sp2.myPlayer + " beats " + sp1.myPlayer + " " + p2FirstScore + " to " + p1FirstScore);

						sp2.wins++;
					}
					//Collections.sort(thePlayers);
					System.out.println("w%\twins\tgames\tname");
					for (int k = 0; k < thePlayers.size(); ++k) {
						System.out.println(thePlayers.get(k));
					}
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		Collections.sort(thePlayers);
		System.out.println("w%\twins\tgames\tname");
		for (int k = 0; k < thePlayers.size(); ++k) {
			System.out.println(thePlayers.get(k));
		}
	}
	/*
	 * this class was taken from MancalaCageMatch by Micheal Skalak
	 */
	private class SortablePlayer implements Comparable<SortablePlayer> {
		double gamesPlayed;
		double wins;
		CheckersPlayer myPlayer;

		public SortablePlayer(CheckersPlayer p) {
			myPlayer = p;
			gamesPlayed = 0;
			wins = 0;
		}

		double getWinningPct() {
			if (gamesPlayed == 0) {
				return 0;

			} else {
				return wins / gamesPlayed;
			}
		}

		public String toString() {
			return String.format("%.2f", getWinningPct()) + "\t" + wins + "\t" + gamesPlayed + "\t" + myPlayer;
		}

		@Override
		public int compareTo(SortablePlayer o) {
			double winPct = getWinningPct();
			double oWinPct = o.getWinningPct();
			if (winPct > oWinPct) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}
