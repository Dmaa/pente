package pente;
import java.util.Observable;

/**
 * A Board is a 2D grid of Objects.  Functionality is provided to read and write to/from the Board.
 * @author Eric Ferrante
 *
 */
public class Board extends Observable {

	private Object[][] board;

	/**
	 * Creates a Board with the given number of rows and columns.
	 * @param rows Number of rows
	 * @param cols Number of columns
	 */
	public Board(int rows, int cols) {
		board = new Object[rows][cols];
	}

	/**
	 * Writes a new value to the board at the specified location and notifies any Observers.
	 * @param row Row where the new data should be written.
	 * @param col Column where the new data should be written. 
	 * @param newObj The new Object to be written at (row, col)
	 */
	public void write(int row, int col, Object newObj) {
		board[row][col] = newObj;
		setChanged();
		notifyObservers(newObj);
	}

	/**
	 * Reads a value from the Board at the specified location.
	 * @param row Row to read data from.
	 * @param col Column to read data from.
	 * @return The Object at the given (row, col) position.
	 */
	public Object read(int row, int col) {
		return board[row][col];
	}
	
	/**
	 * Returns the number of rows in this Board.
	 * @return The number of rows in this Board
	 */
	public int numRows() {
		return board.length;
	}

	/**
	 * Returns the number of columns in this Board.
	 * @return The number of columns in this Board
	 */
	public int numCols() {
		return board[0].length;
	}

	/**
	 * Resets this Board and notifies any Observers.
	 */
	public void reset() {
		for (int row = 0; row < board.length; row++)
			for (int col = 0; col < board[0].length; col++)
				board[row][col] = null;
		setChanged();
		notifyObservers();
	}
}