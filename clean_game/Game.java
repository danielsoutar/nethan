package clean_game;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Game {

	Board theBoard;

	final int MAX_NUM_MOVES = 9;

	public boolean gameIsNotOver = true;
	public boolean noughtToPlay;

	public static void main(String[] args) {
		Game theGame = new Game();
		theGame.play();
	}

	void play() {
		File file = new File("out.txt");
		FileWriter file_writer = null;
		try {
			file_writer = new FileWriter(file);
			for(int i = 0; i < 1000; i++) {
				theBoard = new Board();
				int numberOfMoves = 0;
				Scanner scanner = new Scanner(System.in);
				chooseStartingSymbol(scanner);
				//Assume AILSA always plays crosses.
				AILSA ai = new AILSA(theBoard, false);
				AILSA ai2 = new AILSA(theBoard, true);
				//System.out.println("Empty board: ");
				//theBoard.print();
				while(gameIsNotOver) {
					if(noughtToPlay)
						ai2.makeRandomMove();
					else
						ai.makeIntelligentMove();
					//theBoard.print();
					numberOfMoves++;
					if(theBoard.isWon()) {
						file_writer.write(ai.json_output.substring(0, ai.json_output.length() - 1));
						//				if(noughtToPlay) 
						//					System.out.println("Noughts wins!");
						//				else
						//					System.out.println("Lol u could do with improvement m8");
						break;
					}
					else if(numberOfMoves == MAX_NUM_MOVES) {
						//				System.out.println("You'll never beat me now ;)");
						break;
					}
					noughtToPlay = !noughtToPlay;
				}
				scanner.close();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				file_writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void chooseStartingSymbol(Scanner scanner) {
		int x = (int) Math.floor((Math.random() * 2));

		if(x == 0)
			noughtToPlay = true;
		else
			noughtToPlay = false;
	}

	void makeMove(Scanner scanner) {
		String playerSymbol;
		if(noughtToPlay)
			playerSymbol = "Noughts.";
		else
			playerSymbol = "Crosses.";
		System.out.println("Please enter your coordinates for " + playerSymbol);
		System.out.println("Enter them of the form 'X,Y'.");
		String coordinateString = scanner.nextLine();
		coordinateString = coordinateString.replace(" ", "");
		while (coordinateString.length() != 3 || !coordinateString.substring(0, 1).matches("[123]")
				|| coordinateString.charAt(1) != ',' || !coordinateString.substring(2, 3).matches("[123]")) {
			coordinateString = scanner.nextLine();
		}
		int xCoordinate = retrieveX(coordinateString);
		int yCoordinate = retrieveY(coordinateString);
		if(!insertSymbol(xCoordinate, yCoordinate))
			makeMove(scanner);
	}

	int retrieveX(String coordinateString) {
		int xCoordinate = Integer.parseInt(coordinateString.substring(0, 1));
		return xCoordinate;
	}

	int retrieveY(String coordinateString) {
		int yCoordinate = Integer.parseInt(coordinateString.substring(2, 3));
		return yCoordinate;
	}

	boolean insertSymbol(int xCoordinate, int yCoordinate) {
		System.out.println(xCoordinate + ", " + yCoordinate);
		xCoordinate--;
		yCoordinate--;
		if(theBoard.withinBounds(xCoordinate, yCoordinate) && theBoard.isEmptySquareAt(xCoordinate, yCoordinate)) {
			theBoard.update(xCoordinate, yCoordinate, noughtToPlay);
			return true;
		}
		else
			return false;
	}

}