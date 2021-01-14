package checkers;

import java.util.ArrayList;
import java.lang.Math;

/*
 * Contains the logic for how checkers works
 * @author Iris Shaker-Check
 * based of Gamestate for mancal game by Micheal Skalak
 * @version Dec. 2 2020
 */

public class GameState {
	
	int[][] board;
	
	//num of pieces remaining for top
	int TopScore;
	
	//num of pieces remaining for bottom
	int BottomScore;
	
	//the number of moves the top player has
	int numTopMoves;
	
	//the number of moves the bottom play has
	int numBottomMoves;
	
	boolean isBottomTurn;
	
	//create a defualt game
	public GameState() {
		board = new int[8][8];
		for(int j = 0; j < 8; j++) {
			if(j%2 == 0) {
				board[0][j] = -1;
				board[1][j] = 0;
				board[2][j] = -1;
				board[3][j] = 0;
				board[4][j] = 0;
				board[5][j] = 0;
				board[6][j] = 1;
				board[7][j] = 0;
			}else {
				board[0][j] = 0;
				board[1][j] = -1;
				board[2][j] = 0;
				board[3][j] = 0;
				board[4][j] = 0;
				board[5][j] = 1;
				board[6][j] = 0;
				board[7][j] = 1;
			}
		}
		this.TopScore = 12;
		this.BottomScore = 12;
		this.numTopMoves = 4;
		this.numBottomMoves = 4;
		this.isBottomTurn = true;
	}
	//dupplicate GameState
	public GameState(GameState d) {
		board = new int[8][8];
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j ++) {
				board[i][j] = d.board[i][j];
			}
		}
				//d.board.clone();
		BottomScore = d.BottomScore;
		TopScore = d.TopScore;
		isBottomTurn = d.isBottomTurn;
	}
	//returns whos turn it is
	public boolean getTurn() {
		return this.isBottomTurn;
	}
	
	//print a board that could be read by a human, because I am human :-)
	public void printBoard() {
		for(int i = 0; i < board.length; i ++) {
			for (int j = 0; j < board[0].length; j ++) {
				if(board[i][j] == 1 || board[i][j] == 0 || board[i][j] == 2) {
					System.out.print(" " +  board[i][j] + " ");
				}else {
					System.out.print(board[i][j] + " ");
				}
			}
			System.out.println("");
		}
	}
	//return an index of legal Moves
	public ArrayList<Move> getLegalMoves(){
		if(this.isBottomTurn) {
			ArrayList<Move> moves= new ArrayList<Move>();
			
			for(int i = 0; i < board.length; i++) {
				for(int j = 0; j < board[0].length; j++) {
					if(board[i][j] == 1 || board[i][j] == 2) {
						//check to see if the move to the left is empty
						if(i!=0 && j !=0 && board[i-1][j-1] == 0) {
							int[][] movement = new int[2][2];
							movement[0][0] = i;
							movement[1][0] = j;
							movement[0][1] = i - 1;
							movement[1][1] = j - 1;
							moves.add(new Move(movement));
						}
						//check to see if the move to the right is empty
						if(i!=0 && j !=7 && board[i-1][j+1] == 0) {
							int[][] movement = new int[2][2];
							movement[0][0] = i;
							movement[1][0] = j;
							movement[0][1] = i - 1;
							movement[1][1] = j + 1;
							moves.add(new Move(movement));
						}
						//check if it can move backwords
						if (board[i][j] == 2) {
							//check to see if the move to the left  behind :) is empty
							if(i!=7 && j !=0 && board[i+1][j-1] == 0) {
								int[][] movement = new int[2][2];
								movement[0][0] = i;
								movement[1][0] = j;
								movement[0][1] = i + 1;
								movement[1][1] = j - 1;
								moves.add(new Move(movement));
							}
							//check to see if the move to the right behind is empty
							if(i!=7 && j!=7 && board[i+1][j+1] == 0) {
								int[][] movement = new int[2][2];
								movement[0][0] = i;
								movement[1][0] = j;
								movement[0][1] = i + 1;
								movement[1][1] = j + 1;
								/*GameState n = new GameState(this);
								System.out.println(i + "," + j + " " + n.makeMove(new Move(movement)).currentScore());
								*/
								moves.add(new Move(movement));
							}
						}
						//create jump moves
						if((!(i<=1) && !(j<=1) && (board[i-1][j-1] == -1 || board[i-1][j-1] == -2) && board[i-2][j-2] == 0) || 
								(!(i<=1) && !(j>=6) && (board[i-1][j+1] == -1 || board[i-1][j+1] == -2) && board[i-2][j+2] == 0) ||
								(!(i>=6) && !(j>=6) && (board[i+1][j+1] == -1 || board[i+1][j+1] == -2) && board[i+2][j+2] == 0) ||
								(!(i>=6) && !(j<=1) && (board[i+1][j-1] == -1 || board[i+1][j-1] == -2)&& board[i+2][j-2] == 0)) {
							GameState next = new GameState(this);
							int[][] curMoves = new int[2][1];
							curMoves[0][0] = i;
							curMoves[1][0] = j;
							moves.addAll(jump(curMoves, next));
						}
					}
				}
			}
			this.numBottomMoves = moves.size();
			return moves;
		}else {
			ArrayList<Move> moves= new ArrayList<Move>();
			
			for(int i = 0; i < board.length; i++) {
				for(int j = 0; j < board[0].length; j ++) {
					if(board[i][j] == -1 || board[i][j] == -2) {
						//check to see if the move to the left is empty
						if(i!=7 && j !=0 && board[i+1][j-1] == 0) {
							int[][] movement = new int[2][2];
							movement[0][0] = i;
							movement[1][0] = j;
							movement[0][1] = i + 1;
							movement[1][1] = j - 1;
							moves.add(new Move(movement));
						}
						//check to see if the move to the right is empty
						if(i!=7 && j!=7 && board[i+1][j+1] == 0) {
							int[][] movement = new int[2][2];
							movement[0][0] = i;
							movement[1][0] = j;
							movement[0][1] = i + 1;
							movement[1][1] = j + 1;
							moves.add(new Move(movement));
						}
						//check to see if can go backward
						if(board[i][j] == -2) {
							//check to see if the move to the left behind is empty
							if(i!=0 && j !=0 && board[i-1][j-1] == 0) {
								int[][] movement = new int[2][2];
								movement[0][0] = i;
								movement[1][0] = j;
								movement[0][1] = i - 1;
								movement[1][1] = j - 1;
								moves.add(new Move(movement));
							}
							//check to see if the move to the right behind is empty
							if(i!=0 && j !=7 && board[i-1][j+1] == 0) {
								int[][] movement = new int[2][2];
								movement[0][0] = i;
								movement[1][0] = j;
								movement[0][1] = i - 1;
								movement[1][1] = j + 1;
								moves.add(new Move(movement));
							}
						}
						//create jump moves
						if((!(i<=1) && !(j<=1) && (board[i-1][j-1] == 1 || board[i-1][j-1] == 2) && board[i-2][j-2] == 0) || 
								(!(i<=1) && !(j>=6) && (board[i-1][j+1] == 1 || board[i-1][j+1] == 2) && board[i-2][j+2] == 0) ||
								(!(i>=6) && !(j>=6) && (board[i+1][j+1] == 1 || board[i+1][j+1] == 2) && board[i+2][j+2] == 0) ||
								(!(i>=6) && !(j<=1) && (board[i+1][j-1] == 1 || board[i+1][j-1] == 2) && board[i+2][j-2] == 0)) {
							GameState next = new GameState(this);
							int[][] curMoves = new int[2][1];
							curMoves[0][0] = i;
							curMoves[1][0] = j;
							moves.addAll(jump(curMoves, next));
						}
					}
				}
			}
			this.numTopMoves = moves.size();
			return moves;
		}
		
	}
	//determine if the game is over this does not find a draw.
	//will need to put that into player
	public boolean isGameOver() {
		double bScore = 0;
		double tScore = 0;
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j ++) {
				if(board[i][j] == 1 || board[i][j] == 2) {
					bScore = bScore + board[i][j];
				}else if(board[i][j] == -1 || board[i][j] == -2) {
					tScore = tScore + board[i][j];
				}
			}
		}
		if (bScore == 0 || tScore == 0) {
			//|| this.numTopMoves == 0 || this.numBottomMoves == 0
			return true;
		}else {
			return false;
		}
	}
	//determine if a Move is legal
	//public boolean isMoveLegal(Move m) {
		
	//}
	/*
	 * @param Move m
	 * 		Takes in a move
	 * @return Gamestate
	 * 		returns the GameState after the move has been made
	 */
	public GameState makeMove(Move m) {
		GameState next = new GameState(this);
		int [][] movements = m.movement.clone();
		//get beginning row
		int r = movements[0][0];
		//get beginning colums
		int c = movements[1][0];
		int currentPiece = next.board[r][c];
		next.board[r][c] = 0;
		if(movements.length == 2 && (Math.abs(r-movements[0][1])== 1)) {
			//System.out.println(movements[0][1]);
			//System.out.println(movements[1][1]);
			int row = movements[0][1];
			int col = movements[1][1];
			if(row == 0 && currentPiece == 1) {
				currentPiece = 2;
			}
			if(row == 7 && currentPiece == -1) {
				currentPiece = -2;
			}
			next.board[row][col] = currentPiece;
		}else {
			//System.out.println("Making jump move");
			int curRow = r;
			int curCol = c;
			for (int i = 1; i < movements.length; i ++) {
				int row = movements[0][i];
				int col = movements[1][i];
				int jumpRow = 0;
				int jumpCol;
				if(row > curRow) {
					jumpRow = 1;
				}else {
					jumpRow = -1;
				}
				if(col > curCol) {
					jumpCol = 1;
				}else {
					jumpCol = -1;
				}
				/*System.out.println("row: " + curRow + " " + jumpRow + " " + row + "col:" + 
						curCol + " " + jumpCol + " " + col);*/
				next.board[curRow + jumpRow][curCol + jumpCol] = 0;
				curRow = row;
				curCol = col;
			}
			int rowFinal = movements[0][movements.length-1];
			//System.out.println(rowFinal);
			if(rowFinal == 0 && currentPiece == 1) {
				currentPiece = 2;
			}
			if(rowFinal == 7 && currentPiece == -1) {
				currentPiece = -2;
			}
			int colFinal = movements[1][movements.length-1];
			next.board[rowFinal][colFinal] = currentPiece;
		}
		next.isBottomTurn = !this.isBottomTurn;
		
		return next;
	}
	
	//get the current score of the game
	public double currentScore() {
		double bScore = 0;
		double tScore = 0;
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[0].length; j ++) {
				if(board[i][j] == 1 || board[i][j] == 2) {
					bScore = bScore + board[i][j];
				}else if(board[i][j] == -1 || board[i][j] == -2) {
					tScore = tScore + board[i][j];
				}
			}
		}
		return bScore + tScore;
	}
	//get current player score
	//will return bottom score
	public double playerScore() {
		return (double)this.BottomScore;
	}
	/*
	 * @param curMoves
	 * 		will take in a 2D int array of all the current movements that this
	 * 		move has taken so far
	 * @return moves
	 * 		will return a ArrauList<Moves> of all the possible jump moves
	 */
	public ArrayList<Move> jump(int[][] curMoves, GameState gs){
		//get beginning colunm and row
		int r = curMoves[0][0];
		int c = curMoves[1][0];
		//current row
		int k = curMoves[0][curMoves[0].length - 1];
		//current column
		int l = curMoves[1][curMoves[0].length - 1];
		//arraylist of moves
		ArrayList<Move> moves= new ArrayList<Move>();
		if(this.isBottomTurn) {
			//check if current position is 2 or 1
			//check for jumps to the right
			if((!(k<=1) && !(l<=1) && (gs.board[k-1][l-1] == -1 || gs.board[k-1][l-1] == -2)&& 
					gs.board[k-2][l-2] == 0)) {
				curMoves = makeArrayBigger(curMoves);
				curMoves[0][curMoves[0].length-1] = k-2;
				curMoves[1][curMoves[0].length-1] = l-2;
				moves.add(new Move(curMoves));
				gs.board[k-1][l-1] = 0;
				if((k-2) == 0) {
					gs.board[k-2][l-2] = 2;
				}else {
					gs.board[k-2][l-2] = gs.board[k][l];
				}
				gs.board[k][l] = 0;
				moves.addAll(jump(curMoves, gs));
			}
			/*Move m = new Move(curMoves);
			moves.add(m);
			gs = gs.makeMove(m);*/
			//check for jumps to the left
			if (!(k<=1) && !(l>=6) && (gs.board[k-1][l+1] == -1 || gs.board[k-1][l+1] == -2)&& gs.board[k-2][l+2] == 0) {
				curMoves = makeArrayBigger(curMoves);
				curMoves[0][curMoves[0].length-1] = k-2;
				curMoves[1][curMoves[0].length-1] = l+2;
				//System.out.println("Hello" + k + ", " + l + " " + gs.makeMove(new Move(curMoves)).currentScore());
				moves.add(new Move(curMoves));
				gs.board[k-1][l+1] = 0;
				if((k-2) ==0) {
					gs.board[k-2][l+2] = 2;
				}else {
					gs.board[k-2][l+2] = gs.board[k][l];
				}
				gs.board[k][l] = 0;
				/*Move m = new Move(curMoves);
				moves.add(m);
				gs = gs.makeMove(m);*/
				moves.addAll(jump(curMoves, gs));
			}
			//check if can go backwards
			if(board[r][c] == 2) {
				//check for jumps to the right backwards
				if(!(k>=6) && !(l<=1) && (gs.board[k+1][l-1] == -1 || gs.board[k+1][l-1] == -2) && gs.board[k+2][l-2] == 0) {
					curMoves = makeArrayBigger(curMoves);
					curMoves[0][curMoves[0].length-1] = k+2;
					curMoves[1][curMoves[0].length-1] = l-2;
					moves.add(new Move(curMoves));
					gs.board[k][l] = 0;
					gs.board[k+1][l-1] = 0;
					gs.board[k+2][l-2] = 2;
					/*Move m = new Move(curMoves);
					moves.add(m);
					gs = gs.makeMove(m);*/
					moves.addAll(jump(curMoves, gs));
				}
				//check for jumps to the left backwards
				if (!(k>=6) && !(l>=6) && (gs.board[k+1][l+1] == -1 || gs.board[k+1][l+1] == -2) && gs.board[k+2][l+2] == 0) {
					curMoves = makeArrayBigger(curMoves);
					curMoves[0][curMoves[0].length-1] = k+2;
					curMoves[1][curMoves[0].length-1] = l+2;
					moves.add(new Move(curMoves));
					gs.board[k+1][l+1] = 0;
					gs.board[k][l] = 0;
					gs.board[k+2][l+2] = 2;
					/*Move m = new Move(curMoves);
					moves.add(m);
					gs = gs.makeMove(m);*/
					moves.addAll(jump(curMoves, gs));
				}
			}
		}else{
			//check for jumps to the right backwards
			if((!(k>=6) && !(l<=1) && gs.board[k+1][l-1] == 1 && gs.board[k+2][l-2] == 0)) {
				//System.out.println("rb top" + (k+2) + " " + (l-2));
				curMoves = makeArrayBigger(curMoves);
				curMoves[0][curMoves[0].length-1] = k+2;
				curMoves[1][curMoves[0].length-1] = l-2;
				moves.add(new Move(curMoves));
				gs.board[k+1][l-1] = 0;
				if((k+2) == 7) {
					gs.board[k+2][l-2] = -2;
				}else {
					gs.board[k+2][l-2] = gs.board[k][l];
				}
				gs.board[k][l] = 0;
				//Move m = new Move(curMoves);
				//moves.add(m);
				//gs = gs.makeMove(m);
				moves.addAll(jump(curMoves, gs));
			}
			//check for jumps to the left backwards
			if (!(k>=6) && !(l>=6) && (gs.board[k+1][l+1] == 1 || gs.board[k+1][l+1] == 2)&& gs.board[k+2][l+2] == 0) {
				int [][]curMove = makeArrayBigger(curMoves);
				curMove[0][curMove[0].length-1] = k+2;
				curMove[1][curMove[0].length-1] = l+2;
				/*Move m = new Move(curMove);
				moves.add(m);
				gs = gs.makeMove(m);*/
				gs.board[k+1][l+1] = 0;
				if((k+2) == 7) {
					gs.board[k+2][l+2] = -2;
				}else {
					gs.board[k+2][l+2] = gs.board[k][l];
				}
				gs.board[k][l] = 0;
				//gs.printBoard();
				//System.out.println(k + " " + (curMove[0][curMove.length - 1]));
				moves.addAll(jump(curMove, gs));
			}
			//check if can go backwards
			if(board[r][c] == -2) {
				//check for jumps to the right backwards
				if((!(k<=1) && !(l<=1) && (gs.board[k-1][l-1] == 1 || gs.board[k-1][l-1] == 2) && gs.board[k-2][l-2] == 0)) {
					curMoves = makeArrayBigger(curMoves);
					curMoves[0][curMoves[0].length-1] = k-2;
					curMoves[1][curMoves[0].length-1] = l-2;
					moves.add(new Move(curMoves));
					gs.board[k-1][l-1] = 0;
					gs.board[k][l] = 0;
					gs.board[k-2][l-2] = -2;
					/*Move m = new Move(curMoves);
					moves.add(m);
					gs = gs.makeMove(m);*/
					moves.addAll(jump(curMoves, gs));
				}
				//check for jumps to the left backwards
				if (!(k<=1) && !(l>=6) && (gs.board[k-1][l+1] == 1 || gs.board[k-1][l+1] == 2) && gs.board[k-2][l+2] == 0) {
					curMoves = makeArrayBigger(curMoves);
					curMoves[0][curMoves[0].length-1] = k-2;
					curMoves[1][curMoves[0].length-1] = l+2;
					moves.add(new Move(curMoves));
					gs.board[k-1][l+1] = 0;
					gs.board[k][l] = 0;
					gs.board[k-2][l+2] = -2;
					/*Move m = new Move(curMoves);
					moves.add(m);
					gs = gs.makeMove(m);*/
					moves.addAll(jump(curMoves, gs));
				}
			}
		}
		return moves;
	}
	public int [][] makeArrayBigger(int [][] array){
		int [][] bigger = new int[2][array[0].length + 1];
		for(int i = 0; i < 2; i ++) {
			for(int j = 0; j < array[0].length; j++) {
				int num = array[i][j];
				bigger[i][j] = num;
			}
		}
		return bigger;
	}
}
