package checkers;
/* This player uses alpha beta and null move pruning stagies
 * @author Iris Shaker-Check
 * @version Dec. 2 2020
 */

import java.util.ArrayList;
import java.util.Random;

public class NullMovePruning extends CheckersPlayer implements MinMax{
	
	private int depthLimit;
	private long nodesGenerated;
	private double numChildrenExpanded;
	private long numPossibleMoves;
	private long numStaticEvalutions;
	
	public NullMovePruning() {
		this.depthLimit = 5;
	}
	public NullMovePruning(int depth) {
		this.depthLimit = depth;
	}
	public String toString() {
		return "NullMovePruning";
	}
	@Override
	public Move getMove(GameState gs, long deadline) {
		ArrayList<Move> validMoves = gs.getLegalMoves();
		ArrayList<Move> sameScoreMoves = new ArrayList<Move>();
		ArrayList<Move> sameFirstMoves = new ArrayList<Move>();
		this.numPossibleMoves = this.numPossibleMoves + validMoves.size()-1;
		this.nodesGenerated = this.nodesGenerated + 1;
		Move bestMove = null;
		Move firstBestMove = null;
		double bestScore=-100000000;
		double alpha= -100000000;
		double beta = 100000000;
		int depthMoveLimit = this.depthLimit;
		while( deadline-System.currentTimeMillis() > 300) {
			for (Move m : validMoves) {
				GameState next = gs.makeMove(m);
				//gets the current value of the node
				double curScore;
				/*if (next.isBottomTurn) {
					curScore = maxPlayer(next, depthMoveLimit, 1, alpha, beta);
				}else {
					curScore = minPlayer(next, depthMoveLimit, 1, alpha, beta);
				}
				*/
				if(next.isBottomTurn) {
					curScore = negaMax(next, depthMoveLimit - 1, 1, alpha, beta);
				}else {
					curScore = -negaMax(next, depthMoveLimit - 1, 1, -beta, -alpha);
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
				if(!next.isBottomTurn) {
					curScore = -curScore;
				}
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
	 * this method is a combination of both min and max
	 */
	public double negaMax(GameState curState, int depthLeft, int currentDepth, double alpha, double beta) {
		this.nodesGenerated = this.nodesGenerated + 1;
		//get all the posible moves for this node
		ArrayList<Move> validMoves = curState.getLegalMoves();
		if (curState.isGameOver() || validMoves.size() == 0) {
			//System.out.println("Game over");
			if(curState.isBottomTurn) {
				return curState.currentScore();//value of the end of the game
			}else {
				return -curState.currentScore();//value of the end of the game
			}
		}
		else if (depthLeft == 0) {
			//System.out.println(curState.currentScore());
			return staticEvaluator(curState);
		}
		else {
			this.numPossibleMoves = this.numPossibleMoves + validMoves.size()-1;
			double bestScore= -100000000;
			if(currentDepth == 3 && validMoves.size() > 2) {
				curState.isBottomTurn = !curState.isBottomTurn;
				bestScore = -negaMax(curState, 0,currentDepth +1, -beta, -beta+1);
				curState.isBottomTurn = !curState.isBottomTurn;
				if (bestScore >= beta) {
					return bestScore;
				}
			}
			//set max value for negative infinity
			//bestScore= -100000000;
			for (Move m : validMoves) {
				GameState next = curState.makeMove(m);
				this.numChildrenExpanded = this.numChildrenExpanded + 1;

				//get the highest possible score
				bestScore = Math.max(bestScore, -negaMax(next, depthLeft - 1, 
						currentDepth + 1, -beta, -alpha));
				if (bestScore >= beta) {
					return bestScore;
				}
				if (bestScore > alpha) {
					alpha = bestScore;
				}
			}
		return alpha;
		}
	}
	
	/*
	 * @param curState
	 * 		is the current state of the game
	 * @param depthLeft
	 * 		how much farther down it is alloud to go
	 * @param alpha, beta
	 * 		alpha is the current highest score that it can garenty while
	 * 		beta is the current lowest score it can garenty
	 * @return
	 * 		the lowest possible score from this move
	 */
	public double minPlayer(GameState curState, int depthLeft, int currentDepth, double alpha, double beta) {
		this.nodesGenerated = this.nodesGenerated +1;
		//get all the moves for the node
		ArrayList<Move> validMoves = curState.getLegalMoves();
		if (curState.isGameOver() || validMoves.size() == 0) {
			return curState.currentScore();//value of the end of the game
		}
		else if (depthLeft == 0) {
			return staticEvaluator(curState);
		}
		else {
			this.numPossibleMoves = this.numPossibleMoves + validMoves.size()-1;
			double bestScore= 100000000;
			if (currentDepth == 5 && validMoves.size() > 2) {
				curState.isBottomTurn = !curState.isBottomTurn;
				bestScore = maxPlayer(curState, 2,currentDepth+1, beta - 1, beta);
				curState.isBottomTurn = !curState.isBottomTurn;
				if (bestScore <= alpha) {
					return bestScore;
				}
			}
			//set min value for infinity
			bestScore= 100000000;
			for (Move m : validMoves) {
				GameState next = curState.makeMove(m);
				this.numChildrenExpanded = this.numChildrenExpanded + 1;
				//try to do null-move heuristic at depth 5
				if (currentDepth == 5 && validMoves.size() > 2) {
					curState.isBottomTurn = !curState.isBottomTurn;
					bestScore = maxPlayer(curState, 2,currentDepth+1, beta - 1, beta);
					curState.isBottomTurn = !curState.isBottomTurn;
					if (bestScore <= alpha) {
						return bestScore;
					}
				}
				//get lowest score
				bestScore = Math.min(bestScore, maxPlayer(next, depthLeft - 1, currentDepth + 1,alpha, beta));
				if (bestScore <= alpha) {
					return bestScore;
				}
				if(bestScore <= beta) {
					beta = bestScore;
				}
			}
			return beta;
		}
	}
	/*
	 * @param curState
	 * 		is the current state of the game
	 * @param depthLeft
	 * 		how much farther down it is alloud to go
	 * @param alpha, beta
	 * 		alpha is the current highest score that it can garenty while
	 * 		beta is the current lowest score it can garenty
	 * @return
	 * 		the highest possible score from this move
	 */
	public double maxPlayer(GameState curState, int depthLeft, int currentDepth, double alpha, double beta) {
		this.nodesGenerated = this.nodesGenerated + 1;
		//get all the posible moves for this node
		ArrayList<Move> validMoves = curState.getLegalMoves();
		if (curState.isGameOver() || validMoves.size() == 0) {
			//System.out.println("Game over");
			if(curState.isBottomTurn) {
				return curState.currentScore();//value of the end of the game
			}else {
				return -curState.currentScore();//value of the end of the game
			}
			
		}
		else if (depthLeft == 0) {
			//System.out.println(curState.currentScore());
			return staticEvaluator(curState);
		}
		else {
			this.numPossibleMoves = this.numPossibleMoves + validMoves.size()-1;
			//set max value for negative infinity
			double bestScore= -100000000;
			for (Move m : validMoves) {
				GameState next = curState.makeMove(m);
				this.numChildrenExpanded = this.numChildrenExpanded + 1;
				if(currentDepth == 5 && validMoves.size() > 2) {
					curState.isBottomTurn = !curState.isBottomTurn;
					bestScore = minPlayer(curState, 2,currentDepth +1, alpha, alpha+1);
					curState.isBottomTurn = !curState.isBottomTurn;
					if (bestScore >= beta) {
						return bestScore;
					}
				}
				//get the highest possible score
				bestScore = Math.max(bestScore, minPlayer(next, depthLeft - 1, currentDepth + 1, alpha, beta));
				if (bestScore >= beta) {
					return bestScore;
				}
				if (bestScore > alpha) {
					alpha = bestScore;
				}
			}
			return alpha;
		}
	}
	public double staticEvaluator(GameState state) {
		//add one to the number of static evulations preformed
		this.numStaticEvalutions = this.numStaticEvalutions +1;
		//returns bottoms score
		if(state.isBottomTurn) {
			return state.currentScore();
		}else {
			return -state.currentScore();
		}
		
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
		double aveBranchingFac = ((double)this.numPossibleMoves / (double)(this.nodesGenerated - this.numStaticEvalutions));
		return aveBranchingFac;
	}
	@Override
	public double getEffectiveBranchingFactor() {
		return (double)this.nodesGenerated/(double)(this.nodesGenerated - this.numStaticEvalutions);
	}
}
