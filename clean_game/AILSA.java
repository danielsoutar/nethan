package clean_game;

import java.util.ArrayList;
import java.util.List;

public class AILSA {

	private Board board;
	private boolean isNought;
	
	public String json_output = "";

	public AILSA(Board board, boolean isNought) {
		this.board = board;
		this.isNought = isNought;
	}

	public void makeRandomMove() {
		while(true) {
			int x = (int) Math.floor((Math.random() * 3));
			int y = (int) Math.floor((Math.random() * 3));
			if(board.isEmptySquareAt(x, y)) {
				board.update(x, y, isNought);
				break;
			}
		}
	}

	public void makeIntelligentMove() {
		boolean isInput = true;
		json_output += "{ \"input\": ";
		json_output += board.toNethanFormat(isInput) + ", ";
		int[] optimumMove = miniMax(2, true);
		int xCoordinate = optimumMove[1];
		int yCoordinate = optimumMove[2];
		board.update(xCoordinate, yCoordinate, isNought);
		json_output += "\"output\": ";
		json_output += board.toNethanFormat(!isInput) + " },\n";
	}

	private int[] miniMax(int depth, boolean isAILSA) {
		List<int[]> possibleMoves = generatePossibleMoves();

		int bestScore = (isAILSA) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		int currentScore;
		int bestRow = -1;
		int bestCol = -1;

		if(possibleMoves.isEmpty() || depth == 0)
			bestScore = evaluate();
		else {
			for(int[] move : possibleMoves) {
				board.update(move[0], move[1], !isAILSA);
				currentScore = miniMax(depth - 1, !isAILSA)[0];
				if(isAILSA) {
					if (currentScore > bestScore) {
						bestScore = currentScore;
						bestRow = move[0];
						bestCol = move[1];
					}
				}
				else {
					if (currentScore < bestScore) {
						bestScore = currentScore;
						bestRow = move[0];
						bestCol = move[1];
					}
				}
				board.undo(move[0], move[1]);
			}
		}
		return new int[] {bestScore, bestRow, bestCol};
	}

	private List<int[]> generatePossibleMoves() {
		List<int[]> possibleMoves = new ArrayList<int[]>();
		for (int row = 0; row < board.MAX_SIZE; row++) {
			for (int col = 0; col < board.MAX_SIZE; col++) {
				if (board.matrix[row][col] == null) {
					possibleMoves.add(new int[] {row, col});
				}
			}
		}
		return possibleMoves;
	}

	private int evaluate() {
		int score = 0;
		// Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
		score += evaluateLine(0, 0, 0, 1, 0, 2);  // row 0
		score += evaluateLine(1, 0, 1, 1, 1, 2);  // row 1
		score += evaluateLine(2, 0, 2, 1, 2, 2);  // row 2
		score += evaluateLine(0, 0, 1, 0, 2, 0);  // col 0
		score += evaluateLine(0, 1, 1, 1, 2, 1);  // col 1
		score += evaluateLine(0, 2, 1, 2, 2, 2);  // col 2
		score += evaluateLine(0, 0, 1, 1, 2, 2);  // diagonal
		score += evaluateLine(0, 2, 1, 1, 2, 0);  // alternate diagonal
		return score;
	}

	private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
		int score = 0;

		// First cell
		if(board.matrix[row1][col1] != null) {
			if (board.matrix[row1][col1].isNought == isNought) {
				score = 1;
			} else if (board.matrix[row1][col1].isNought == !isNought) {
				score = -1;
			}
		}

		// Second cell
		if(board.matrix[row2][col2] != null) {
			if (board.matrix[row2][col2].isNought == isNought) {
				if (score == 1) {   // cell1 is mySeed
					score = 10;
				} else if (score == -1) {  // cell1 is oppSeed
					return 0;
				} else {  // cell1 is empty
					score = 1;
				}
			} else if (board.matrix[row2][col2].isNought == !isNought) {
				if (score == -1) { // cell1 is oppSeed
					score = -10;
				} else if (score == 1) { // cell1 is mySeed
					return 0;
				} else {  // cell1 is empty
					score = -1;
				}
			}
		}

		// Third cell
		if(board.matrix[row3][col3] != null) {
			if (board.matrix[row3][col3].isNought == isNought) {
				if (score > 0) {  // cell1 and/or cell2 is mySeed
					score *= 10;
				} else if (score < 0) {  // cell1 and/or cell2 is oppSeed
					return 0;
				} else {  // cell1 and cell2 are empty
					score = 1;
				}
			} else if (board.matrix[row3][col3].isNought != isNought) {
				if (score < 0) {  // cell1 and/or cell2 is oppSeed
					score *= 10;
				} else if (score > 1) {  // cell1 and/or cell2 is mySeed
					return 0;
				} else {  // cell1 and cell2 are empty
					score = -1;
				}
			}
		}
		return score;
	}



}
