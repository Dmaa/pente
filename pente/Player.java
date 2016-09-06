package pente;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This class represents a human Player on the Pente board.  For an AI Player, see
 * the AIPlayer subclass.
 * @author eric_ferrante
 */
public class Player {

	private String name;
	private Color color;
	private int id;
	private BufferedImage img;
	private int myCaptures;
	private int opponentCaptures;

	/** Creates a new human Player with the given icon image file, name, and player ID.
	 * Icon files should be put into the "images" folder of the Pente project.
	 * @param iconFileName The filename for your player icon.  Ex: "black.png"
	 * @param n The name of your player.  Ex: "Boboman"
	 * @param id The unique player ID for your player.
	 */
	public Player(String iconFileName, String n, int id) {
		try {
			img = ImageIO.read(new File("images/" + iconFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.name = n;
		this.id = id;
		myCaptures = 0;
		opponentCaptures = 0;
	}

	/**
	 * Creates a new human Player with the given Color, name and player ID.
	 * Icon files should be put into the "images" folder of the Pente project.
	 * @param c The Color of your player.  Ex:  Color.black
	 * @param n The name of your player.  Ex: "Boboman
	 * @param id The unique player ID for your player.
	 */
	public Player(Color c, String n, int id) {
		color = c;
		name = n;
		this.id = id;
		img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.setColor(color);
		g.fillOval(0, 0, 64, 64);
		myCaptures = 0;
		opponentCaptures = 0;
	}

	/**
	 * Used by AIPlayer subclasses to select a move on the board. To look at the board,
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
	public Location makeMove(int[][] idArray, int moveCount) {
		return null;
	}

	/**
	 * Returns the name of this Player
	 * @return The name of this Player
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the Color of this player if this player was constructed by the constructor
	 * that uses a Color instead of an image file.
	 * @return The Color of this player or null if this player was created using an image.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Returns the unique player ID for this player.
	 * @return The unique player ID for this player.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Returns the BufferedImage for this player.
	 * @return The BufferedImage for this player.
	 */
	public BufferedImage getImage() {
		return img;
	}

	/**
	 * Returns the total number of pieces captured by this player.
	 * @return The total number of pieces captured by this player.
	 */
	public int myCaptures() {
		return myCaptures;
	}

	/**
	 * Returns the total number of pieces captured by this player's opponent.
	 * @return The total number of pieces captured by this player's opponent.
	 */
	public int opponentCaptures() {
		return opponentCaptures;
	}

	/**
	 * Sets the number of captures you have made.
	 * <br><br>
	 * Note: The game sets this for you.  Setting it yourself has no effect
	 * on the actual number of captures the game knows you have.
	 * @param myCaptures The number of captures you have made.
	 */
	public void setMyCaptures(int myCaptures) {
		this.myCaptures = myCaptures;
	}

	/**
	 * Sets the number of captures your opponent has made.
	 * <br><br>
	 * Note: The game sets this for you.  Setting it yourself has no effect
	 * on the actual number of captures the game knows your opponent has made.
	 * @param opponentCaptures The number of captures your opponent has made.
	 */
	public void setOpponentCaptures(int opponentCaptures) {
		this.opponentCaptures = opponentCaptures;
	}
}