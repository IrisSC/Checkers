package checkers;
/* This Player used the min/max stradigy to play checkers
 * 
 * @author Iris Shaker-Check
 * @version Dec. 2 2020
 */

import java.util.ArrayList;
import java.util.Random;

public class MinMaxPlayer extends CheckersPlayer implements MinMax{
	
	private int depthLimit;
	private long nodesGenerated = 0;
	private long numStaticEvalutions = 0;
	private double numChildrenExpanded = 0;
	private long numPossibleMoves = 0;
	private long leafNodes = 0;
	
	public MinMaxPlayer() {
		this.depthLimit = 5;
	}
	public MinMaxPlayer(int depthLimit) {
		this.depthLimit = depthLimit;
	}
	public String toString() {
		return "MinMix";
	}
	@Override
	public Move getMove(GameState gs, long deadline) {
		ArrayList<Move> validMoves = gs.getLegalMoves();
		this.numPossibleMoves = this.numPossibleMoves + validMoves.size()-1;
		//add number of new node bring create
		this.nodesGenerated = this.nodesGenerated + 1;
		ArrayList<Move> sameScoreMoves = new ArrayList<Move>();
		ArrayList<Move> sameFirstMoves = new ArrayList<Move>();
		Move bestMove = null;
		Move firstBestMove = null;
		double bestScore=-100000000;
		int depthMoveLimit = this.depthLimit;
		while( deadline-System.currentTimeMillis() > 300) {
			for(Move m : validMoves) {
				GameState next = gs.makeMove(m);
				//add to number of children expanded
				this.numChildrenExpanded = this.numChildrenExpanded +1;
				
				double curScore;
				if (next.isBottomTurn) {
					curScore = maxPlayer(next, depthMoveLimit - 1);
				}else {
					curScore = minPlayer(next, depthMoveLimit - 1);
				}
				if(!gs.isBottomTurn) {
					curScore = -curScore;
				}
				if (curScore > bestScore) {
					bestScore = curScore;
					bestMove = m;
					sameScoreMoves = new ArrayList<Move>();
					sameScoreMoves.add(bestMove);
				}else if(curScore == bestScore) {
					sameScoreMoves.add(m);
				}
			}
			/*if(depthMoveLimit ==1) {
				firstBestMove = bestMove;
				sameFirstMoves = sameScoreMoves;
			}*/
			depthMoveLimit++;
		}
		if(sameScoreMoves.size() <= 1) {
			return bestMove;
		}else {
			double bScore = -100000000;
			for(int i = 0; i < sameScoreMoves.size(); i++){
				GameState next = gs.makeMove(sameScoreMoves.get(i));
				double curScore = next.currentScore();
				if(curScore > bScore) {
					bScore = curScore;
					sameFirstMoves = new ArrayList<Move>();
					sameFirstMoves.add(sameScoreMoves.get(i));
				}else if(curScore == bScore) {
					sameFirstMoves.add(sameScoreMoves.get(i));
				}
				
			}
			Random r = new Random();
			int index = r.nextInt(sameFirstMoves.size());
			Move makeMove = sameFirstMoves.get(index);
			return makeMove;
		}
		
		/*Random r = new Random();
		int index = r.nextInt(sameScoreMoves.size());
		Move makeMove = sameScoreMoves.get(index);
		return makeMove;*/
	}
	/*
	 * @param curState
	 * 		takes in the current state of the game
	 * @param depthLeft
	 * 		how much depth the player can still go
	 * @returns
	 * 		the value of the min score
	 */
	public double minPlayer(GameState curState, int depthLeft) {
		this.nodesGenerated = this.nodesGenerated + 1;
		//get all the moves for the node
		ArrayList<Move> validMoves = curState.getLegalMoves();
		if (curState.isGameOver() || validMoves.size() == 0) {
			this.leafNodes = this.leafNodes + 1;
			return curState.currentScore();//value of the end of the game
		}
		else if (depthLeft == 0) {
			this.leafNodes = this.leafNodes + 1;
			return staticEvaluator(curState);
		}
		else {
			this.numPossibleMoves = this.numPossibleMoves + validMoves.size()-1;
			//set min value for infinity
			double bestScore= 100000000;
			for (Move m : validMoves) {
				GameState next = curState.makeMove(m);
				this.numChildrenExpanded = this.numChildrenExpanded +1;

				bestScore = Math.min(bestScore, maxPlayer(next, depthLeft - 1));

			}
			return bestScore;
		}
	}
	/*
	 * @param curState
	 * 		takes in the current state of the game
	 * @param depthLeft
	 * 		how much depth the player can still go
	 * @returns
	 * 		the value of the max score
	 */
	public double maxPlayer(GameState curState, int depthLeft) {
		this.nodesGenerated = this.nodesGenerated + 1;
		//get all the moves for the node
		ArrayList<Move> validMoves = curState.getLegalMoves();
		if (curState.isGameOver() || validMoves.size() == 0) {
			this.leafNodes = this.leafNodes + 1;
			return curState.currentScore();//value of the end of the game
		}
		else if (depthLeft == 0) {
			this.leafNodes = this.leafNodes + 1;
			return staticEvaluator(curState);
		}
		else {
			this.numPossibleMoves = this.numPossibleMoves + validMoves.size()-1;
			//set max value for negative infinity
			double bestScore= -100000000;
			for (Move m : validMoves) {
				 GameState next = curState.makeMove(m);
				 this.numChildrenExpanded = this.numChildrenExpanded +1;
				 //add to number of children expanded
				 bestScore = Math.max(bestScore, minPlayer(next, depthLeft - 1));
				
			}
			return bestScore;
		}
			
	}
	public double staticEvaluator(GameState state) {
		this.numStaticEvalutions = this.numStaticEvalutions +1;
		//returns bottoms score
		return state.currentScore();
	}
	@Override
	public long getNodesGenerated() {
		return this.nodesGenerated;
	}
	@Override
	public long getStaticEvaluations() {
		return this.numStaticEvalutions;
	}
	@Override
	public double getAveBranchingFactor() {
		//double numNodesExpanded = this.numChildrenExpanded +1;
		double aveBranchingFac = ((double)this.numPossibleMoves / (double)(this.nodesGenerated - this.numStaticEvalutions));
		return aveBranchingFac;
	}
	@Override
	public double getEffectiveBranchingFactor() {
		return (double)this.nodesGenerated/(double)(this.nodesGenerated - this.numStaticEvalutions);
	}
}
