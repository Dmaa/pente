package pente;
import java.util.ArrayList;

/**
 * This class contains helper methods to aid in the coding of Pente logic and in the
 * tracking of winning pieces and captured pieces.
 * @author eric_ferrante
 */
public class PenteLogic {

	private Board board;

	/**
	 * Constructs a new PenteLogic object using the official Board that Pente is being played on.
	 * @param b The official Board that Pente is being played on.
	 */
	public PenteLogic(Board b) {
		board = b;
	}
	
	/**
	 * Returns whether or not a position on the board is empty.
	 * @param row The row to check.
	 * @param col The column to check.
	 * @return Whether or not a position on the board is empty.
	 */
	public boolean isEmpty(int row, int col) {
		return board.read(row, col) == null || ((GridObject)board.read(row, col)).getID() == 0;
	}

	/**
	 * Returns whether or not a position on the board is in bounds.
	 * @param row The row to check.
	 * @param col The column to check.
	 * @return Whether or not a position on the board is in bounds.
	 */
	public boolean isInBounds(int row, int col) {
		return row >= 0 && row < board.numRows() && col >= 0 && col < board.numCols();
	}

	/**
	 * Returns whether or not a position on the board is occupied by a player.
	 * @param row The row to check.
	 * @param col The column to check.
	 * @return Whether or not a position on the board is occupied by a player.
	 */
	public boolean isOccupied(int row, int col) {
		return board.read(row, col) != null && ((GridObject)board.read(row, col)).getID() != 0;
	}

	/**
	 * Returns whether or not a position is at the center of the board.
	 * @param row The row to check.
	 * @param col The column to check.
	 * @return Whether or not a position is at the center of the board.
	 */
	public boolean isAtBoardCenter(int row, int col) {
		return row == board.numRows()/2 && col == board.numCols()/2;
	}

	/**
	 * Returns whether or not a position is at the center of the board.
	 * @param row The row to check.
	 * @param col The column to check.
	 * @return Whether or not a position is at the center of the board.
	 */
	public boolean isOutsideStartBox(int row, int col) {
		return Math.abs(row - board.numRows()/2) > 2 || Math.abs(col - board.numCols()/2) > 2;
	}
	
	/**
	 * Returns the GridObject at the given row, column.  If no GridObject is at the specified location,
	 * either null or a GridObject with an ID of zero will be returned.
	 * @param row The row to check.
	 * @param col The column to check.
	 * @return The GridObject at the given row, column.
	 */
	public GridObject getGridObjectAt(int row, int col) {
		return (GridObject)board.read(row, col);
	}

	/**
	 * Given a playerID piece played at (row, col), this method returns an ArrayList of Locations
	 * where opponent pieces will be captured, if any. The list is used by the PenteController to
	 * highlight the captured locations on the HighLightPanel.
	 * @param row The row to check.
	 * @param col The column to check.
	 * @param playerID The playerID to check for captures.
	 * @return An ArrayList of Locations representing where on the board pieces were captured.
	 */
	public ArrayList<Location> capture(int row, int col, int playerID) {
		ArrayList<Location> captureList = new ArrayList<Location>();

		// Check for captures east/west
		for (int i = -1; i <= 1; i += 2) {
			if (isInBounds(row, col - 3*i) && isOccupied(row, col - 1*i) && isOccupied(row, col - 2*i) && isOccupied(row, col - 3*i)) {
				if (((GridObject)board.read(row, col - 1*i)).getID() != playerID && ((GridObject)board.read(row, col - 2*i)).getID() != playerID && ((GridObject)board.read(row, col - 3*i)).getID() == playerID) {
					captureList.add(new Location(row, col - 1*i));
					captureList.add(new Location(row, col - 2*i));
					for (Location loc : captureList) {
						board.write(loc.getRow(), loc.getCol(), new GridObject(loc.getRow(), loc.getCol(), 0, null));
					}
				}
			}
		}

		// Check for captures north/south
		for (int i = -1; i <= 1; i += 2) {
			if (isInBounds(row - 3*i, col) && isOccupied(row - 1*i, col) && isOccupied(row - 2*i, col) && isOccupied(row - 3*i, col)) {
				if (((GridObject)board.read(row - 1*i, col)).getID() != playerID && ((GridObject)board.read(row - 2*i, col)).getID() != playerID && ((GridObject)board.read(row - 3*i, col)).getID() == playerID) {
					captureList.add(new Location(row - 1*i, col));
					captureList.add(new Location(row - 2*i, col));
					for (Location loc : captureList) {
						board.write(loc.getRow(), loc.getCol(), new GridObject(loc.getRow(), loc.getCol(), 0, null));
					}
				}
			}
		}

		// Check for diagonal captures
		for (int j = -1; j <= 1; j += 2) {
			for (int i = -1; i <= 1; i += 2) {
				if (isInBounds(row - 3*i, col - 3*i*j) && isOccupied(row - 1*i, col - 1*i*j) && isOccupied(row - 2*i, col - 2*i*j) && isOccupied(row - 3*i, col - 3*i*j)) {
					if (((GridObject)board.read(row - 1*i, col - 1*i*j)).getID() != playerID && ((GridObject)board.read(row - 2*i, col - 2*i*j)).getID() != playerID && ((GridObject)board.read(row - 3*i, col - 3*i*j)).getID() == playerID) {
						captureList.add(new Location(row - 1*i, col - 1*i*j));
						captureList.add(new Location(row - 2*i, col - 2*i*j));
						for (Location loc : captureList) {
							board.write(loc.getRow(), loc.getCol(), new GridObject(loc.getRow(), loc.getCol(), 0, null));
						}
					}
				}
			}
		}
		
		return captureList;
	}
	
	/**
	 * Given a playerID piece played at (row, col), this method returns an ArrayList of Locations
	 * where the player will win with 5-or-more-in-a-row, column, or diagonal, if any. The list is
	 * used by the PenteController to highlight where on the board a win has occurred.
	 * @param row Row to check win at
	 * @param col Column to check win at
	 * @param playerID PlayerID to check win for
	 * @return an ArrayList of Locations of the winning pieces or null if no win found
	 */
	public ArrayList<Location> fiveInARow(int row, int col, int playerID) {
		
		int startRow = row;
		int startCol = col;
		int endRow = row;
		int endCol = col;
		ArrayList<Location> winList = new ArrayList<Location>();

		// Check for 5 or more in a horizontal row
		while (isInBounds(startRow, startCol-1) && isOccupied(startRow, startCol-1) && ((GridObject)board.read(startRow, startCol-1)).getID() == playerID) { startCol--; }
		while (isInBounds(endRow, endCol+1) && isOccupied(endRow, endCol+1) && ((GridObject)board.read(endRow, endCol+1)).getID() == playerID) { endCol++; }
		if (endCol - startCol + 1 >= 5) {
			for (int i = startCol; i <= endCol; i++)
				winList.add(new Location(startRow, i));
		}

		startCol = col;
		endCol = col;

		// Check for 5 or more in a vertical column
		while (isInBounds(startRow - 1, startCol) && isOccupied(startRow - 1, startCol) && ((GridObject)board.read(startRow - 1, startCol)).getID() == playerID) { startRow--; }
		while (isInBounds(endRow + 1, endCol) && isOccupied(endRow + 1, endCol) && ((GridObject)board.read(endRow + 1, endCol)).getID() == playerID) { endRow++; }
		if (endRow - startRow + 1 >= 5) {
			for (int i = startRow; i <= endRow; i++)
				winList.add(new Location(i, startCol));
		}

		startRow = row;
		endRow = row;

		// Check for 5 or more in a NE-SW diagonal
		while (isInBounds(startRow - 1, startCol + 1) && isOccupied(startRow - 1, startCol + 1) && ((GridObject)board.read(startRow - 1, startCol + 1)).getID() == playerID) { startRow--; startCol++; }
		while (isInBounds(endRow + 1, endCol - 1) && isOccupied(endRow + 1, endCol - 1) && ((GridObject)board.read(endRow + 1, endCol - 1)).getID() == playerID) { endRow++; endCol--; }
		if (endRow - startRow + 1 >= 5) {
			for (int i = 0; i < endRow - startRow + 1; i++)
				winList.add(new Location(startRow + i, startCol - i));
 		}

		startRow = row;
		startCol = col;
		endRow = row;
		endCol = col;
		
		// Check for 5 or more in a NW-SE diagonal
		while (isInBounds(startRow - 1, startCol - 1) && isOccupied(startRow - 1, startCol - 1) && ((GridObject)board.read(startRow - 1, startCol - 1)).getID() == playerID) { startRow--; startCol--; }
		while (isInBounds(endRow + 1, endCol + 1) && isOccupied(endRow + 1, endCol + 1) && ((GridObject)board.read(endRow + 1, endCol + 1)).getID() == playerID) { endRow++; endCol++; }
		if (endRow - startRow + 1 >= 5) {
			for (int i = 0; i < endRow - startRow + 1; i++)
				winList.add(new Location(startRow + i, startCol + i));
 		}
		
		return winList;
	}
}
