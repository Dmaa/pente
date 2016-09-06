package pente;

/**
 * AIPlayer represents an Artificial Intelligence player and is meant to be
 * subclassed by classes that implement the makeMove method which determines where an
 * AIPlayer will make its next move.
 * @author eric_ferrante
 */
public abstract class AIPlayer extends Player {
	
	/** Creates a new AIPlayer with the given icon image file, name, and player ID.
	 * Icon files should be put into the "images" folder of the Pente project.
	 * @param iconFileName The filename for your player icon.  Ex: "black.png"
	 * @param n The name of your player.  Ex: "Boboman"
	 * @param id The unique player ID for your player.
	 */
	public AIPlayer(String iconFileName, String n, int id) {
		super(iconFileName, n, id);
	}

	/**
	 * Override this method and put your game logic here.  To look at the board,
	 * analyze the idArray to see which players have pieces where.  moveCount tells you
	 * which move of the game this is.
	 * @param idArray Represents the Pente board as an array of player IDs. An ID
	 * of zero means that spot is empty.  Any other number represents the ID of
	 * the player in that spot.  You can compare entries to your own ID by comparing
	 * it to getID() of yourself.  If (idArray[r][c] != 0 AND idArray[r][c] != getID())
	 * then it means your opponent has a piece at [r][c].
	 * @param moveCount Which move of the game this is.  If moveCount is odd, it means you
	 * are the first player and moveCount represents which move of the game this is (1, 3,
	 * 5, 7, ...)  Likewise, if moveCount is even, it means you are the second player.
	 * @return A Location you want to move to on this turn.
	 */
	public abstract Location makeMove(int[][] idArray, int moveCount);

}
