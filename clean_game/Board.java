package clean_game;

public class Board {

	public final int MAX_SIZE = 3;
	public Piece[][] matrix;

	public Board() {
		matrix = new Piece[MAX_SIZE][MAX_SIZE];
		for (int i = 0; i < MAX_SIZE; i++) {
			for (int j = 0; j < MAX_SIZE; j++) {
				matrix[i][j] = null;
			}
		}
	}

	public void print() {
		printMatrix();
		System.out.println();
	}

	public void printMatrix() {
		for (int i = 0; i < MAX_SIZE; i++) {
			for (int j = 0; j < MAX_SIZE; j++)
				printSquare(matrix[i][j]);
			System.out.println();
		}
	}

	void printSquare(Piece piece) {
		if(piece != null)
			System.out.print(piece.symbol);
		else
			System.out.print("[ ] ");
	}

	public boolean withinBounds(int xCoordinate, int yCoordinate) {
		if(xCoordinate > MAX_SIZE || yCoordinate > MAX_SIZE)
			return false;
		else
			return true;
	}

	public boolean isEmptySquareAt(int xCoordinate, int yCoordinate) {
		if(matrix[xCoordinate][yCoordinate] != null)
			return false;
		else
			return true;
	}

	public void update(int xCoordinate, int yCoordinate, boolean noughtToPlay) {
		if(noughtToPlay) {
			Nought nought = new Nought(xCoordinate, yCoordinate);
			matrix[xCoordinate][yCoordinate] = nought;
		}
		else {
			Cross cross = new Cross(xCoordinate, yCoordinate);
			matrix[xCoordinate][yCoordinate] = cross;
		}
	}

	public void undo(int xCoordinate, int yCoordinate) {
		matrix[xCoordinate][yCoordinate] = null;
	}

	public boolean isWon() {
		return checkRows() || checkColumns() || checkDiagonals();
	}

	private boolean checkRows() {
		int numNoughts = 0, numCrosses = 0;
		for (int row = 0; row < MAX_SIZE; row++) {
			for (int col = 0; col < MAX_SIZE; col++) {
				if(matrix[row][col] != null) {
					if(matrix[row][col].isNought)
						numNoughts++;
					else
						numCrosses++;
				}
				else
					break;
			}
			if(numNoughts == 3 || numCrosses == 3)
				return true;
			else
				numNoughts = numCrosses = 0;
		}
		return false;
	}

	private boolean checkColumns() {
		int numNoughts = 0, numCrosses = 0;
		for (int col = 0; col < MAX_SIZE; col++) {
			for (int row = 0; row < MAX_SIZE; row++) {
				if(matrix[row][col] != null) {
					if(matrix[row][col].isNought)
						numNoughts++;
					else
						numCrosses++;
				}
				else
					break;
			}
			if(numNoughts == 3 || numCrosses == 3)
				return true;
			else
				numNoughts = numCrosses = 0;
		}
		return false;
	}

	private boolean checkDiagonals() {
		return checkLeftToRightDiagonal() || checkRightToLeftDiagonal();
	}

	private boolean checkLeftToRightDiagonal() {
		int numNoughts = 0, numCrosses = 0;
		for (int rowAndCol = 0; rowAndCol < MAX_SIZE; rowAndCol++) {
			if(matrix[rowAndCol][rowAndCol] != null) {
				if(matrix[rowAndCol][rowAndCol].isNought)
					numNoughts++;
				else
					numCrosses++;
			}
			else
				break;
		}
		if(numNoughts == 3 || numCrosses == 3)
			return true;
		else
			numNoughts = numCrosses = 0;
		return false;
	}

	private boolean checkRightToLeftDiagonal() {
		int numNoughts = 0, numCrosses = 0;
		for (int rowAndCol = 0; rowAndCol < MAX_SIZE; rowAndCol++) {
			if(matrix[MAX_SIZE - rowAndCol - 1][rowAndCol] != null) {
				if(matrix[MAX_SIZE - rowAndCol - 1][rowAndCol].isNought)
					numNoughts++;
				else
					numCrosses++;
			}
			else
				break;
		}
		if(numNoughts == 3 || numCrosses == 3)
			return true;
		else
			numNoughts = numCrosses = 0;
		return false;
	}

	public Piece[][] retrieveMatrix() {
		return matrix;
	}

	public String toNethanFormat(boolean isInput) {
		String out = "[";
		for(int i = 0; i < MAX_SIZE; i++)
			for (int j = 0; j < MAX_SIZE; j++) {
				if(isInput) {
					if(matrix[i][j] != null) {
						if(matrix[i][j].isNought)
							out += "1, ";
						else
							out += "-1, ";
					}
					else
						out += "0, ";
				}
				else {
					if(matrix[i][j] != null) {
						if(matrix[i][j].isNought)
							out += "1, ";
						else
							out += "0, ";
					}
					else
						out += "0.5, ";
				}
			}
		out = out.substring(0, out.length() - 2) + "]";
		return out;
	}

}