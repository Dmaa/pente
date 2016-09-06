package pente;
import java.awt.image.BufferedImage;

/**
 * A GridObject is a generalization of any object meant to be displayed as a BufferedImage in any
 * kind of grid or 2D array.
 * @author eric_ferrante
 */
public class GridObject {

	private int row;
	private int col;
	private BufferedImage image;
	private int id;

	/**
	 * Creates a new GridObject with the given parameters.
	 * @param r The row this GridObject is in.
	 * @param c The column this GridObject is in.
	 * @param id The unique ID associated with this GridObject.
	 * @param img The BufferedImage used by this GridObject.
	 */
	public GridObject(int r, int c, int id, BufferedImage img) {
		row = r;
		col = c;
		this.id = id;
		image = img;
	}

	/**
	 * Returns the unique ID of this GridObject.
	 * @return The unique ID of this GridObject.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Returns the row this GridObject is located at.
	 * @return The row this GridObject is located at.
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Sets the row this GridObject is located at.
	 * @param row The new row this GridObject is located at.
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * Returns the column this GridObject is located at.
	 * @return The column this GridObject is located at.
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Sets the column this GridObject is located at.
	 * @param col The new column this GridObject is located at.
	 */
	public void setCol(int col) {
		this.col = col;
	}

	/**
	 * Finds out whether this GridObject is equal to another GridObject by
	 * comparing the row and column position.  Two GridObjects are defined
	 * as equal if and only if they have equivalent row, col positions.
	 */
	public boolean equals(Object other) {
		return ((GridObject)other).hashCode() == this.hashCode();
	}

	/**
	 * Returns the hash code for this GridObject, which is 1000*row + col.
	 * Importantly, this means that the .equals() method for GridObjects is
	 * reliable only for grid sizes up to 999 columns.  The number of rows
	 * doesn't matter.
	 */
	public int hashCode() {
		return 1000*row + col;
	}

	/**
	 * Returns the BufferedImage used by this GridObject.
	 * @return The BufferedImage used by this GridObject.
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Sets the BufferedImage used by this GridObject
	 * @param image The BufferedImage to be used by this GridObject
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}
}
