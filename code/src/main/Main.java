/**
 * 
 */
package main;

import board.GameBoard;

/**
 * @author lowenicte
 *
 */
public class Main {

	public static void main(String[] args) {
		GameBoard gameFrame = GameBoard.getGameBoard();
		gameFrame.lunchGame();
		
		gameFrame.dispose();
	}
}
