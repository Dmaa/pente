package pente;

/**
 * A GameResult stores the win/loss totals of an individual player.  To keep track
 * of the tournament rankings, a List of GameResults is kept and sorted in order
 * from highest total wins to lowest total wins.
 * @author eric_ferrante
 */
public class GameResult implements Comparable<GameResult> {

	private Player player;
	private int fiveInARowWins;
	private int captureWins;

	/**
	 * Creates a new GameResult for the given Player.
	 * @param p The Player to keep statistics for.
	 * @param winByFive The number of wins by five-in-a-row.
	 * @param winByCapture The number of winds by capture.
	 */
	public GameResult(Player p, int winByFive, int winByCapture) {
		player = p;
		fiveInARowWins = winByFive;
		captureWins = winByCapture;
	}

	/**
	 * Returns the Player this GameResult keeps statistics for.
	 * @return The Player this GameResult keeps statistics for.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Returns the number of five-in-a-row wins.
	 * @return The number of five-in-a-row wins.
	 */
	public int getFiveInARowWins() {
		return fiveInARowWins;
	}

	/**
	 * Returns the number of wins by capture.
	 * @return The number of wins by capture.
	 */
	public int getCaptureWins() {
		return captureWins;
	}

	/**
	 * Increments the number of wins by five-in-a-row.
	 */
	public void addFiveInARowWin() {
		fiveInARowWins++;
	}

	/**
	 * Increments the number of wins by capture.
	 */
	public void addCaptureWin() {
		captureWins++;
	}

	/**
	 * Compares this GameResult to 'other' according to the total number of wins, whether
	 * by five-in-a-row or by capture.
	 * @return Returns the difference between this GameResult's total number of wins and
	 * 'other' GameResult's total number of wins.
	 */
	public int compareTo(GameResult other) {
		return (fiveInARowWins + captureWins) - (other.getFiveInARowWins() + other.getCaptureWins());
	}

	/**
	 * Returns a String representation of this GameResult.
	 * @return A String representation of this GameResult.
	 */
	public String toString() {
		return "Name: " + player.getName() + " Wins: " + (fiveInARowWins + captureWins) + " (" + fiveInARowWins + "/" + captureWins + ")";
	}
}
