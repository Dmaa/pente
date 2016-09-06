package pente;

/**
 * This class represents a Location in a grid in terms of a (row, col) pair.
 * @author eric_ferrante
 */
public class Location {

	private int row;
	private int col;

	/**
	 * Creates a new Location with the given row and column.
	 * @param r The row of this Location object.
	 * @param c The column of this Location object.
	 */
	public Location(int r, int c) {
		row = r;
		col = c;
	}

	/**
	 * Returns the row of this Location object.
	 * @return The row of this Location object.
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Returns the column of this Location object.
	 * @return The column of this Location object.
	 */
 	public int getCol() {
		return col;
	}

 	/**
 	 * Returns a String representation of this object.
 	 * @return A String representation of this object.
 	 */
 	public String toString() {
 		return "(" + row + ", " + col + ")";
 	}
}