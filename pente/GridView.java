package pente;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

/**
 * A GridView is a custom JPanel that is displayed as a regular grid
 * of columns and rows, each of which may contain a GridObject.
 * @author eric_ferrante
 */
public class GridView extends JPanel implements Observer {

	private static final long serialVersionUID = -4358507809242582233L;
	private int cellSize;
	private int numRows;
	private int numCols;
	private int gridLineThickness;
	private boolean showGridLines;
	private BufferedImage bgImage;
	private ArrayList<GridObject> gridObjects = new ArrayList<GridObject>();

	/**
	 * Creates a new GridView with the given number of rows, columns. Grid lines
	 * are set to visible with a thickness of 1. The Dimension of this GridView
	 * is calculated and set to (cols*cellSize) x (rows*cellSize) plus the space
	 * the grid lines take up.
	 * 
	 * @param rows The number of rows in this GridView
	 * @param cols The number of columns in this GridView
	 * @param cellSize The size in pixels of each cell
	 * @param observeObj An Observable object that updates this view
	 */
	public GridView(int rows, int cols, int cellSize, Observable observeObj) {
		this.cellSize = cellSize;
		this.numRows = rows;
		this.numCols = cols;
		this.setGridLineThickness(1);
		this.showGridLines = false;
		setPreferredSize(new Dimension(cellSize * cols + gridLineThickness *
				(cols + 1), cellSize * rows + gridLineThickness * (rows + 1)));
		setMinimumSize(new Dimension(cellSize * cols + gridLineThickness *
				(cols + 1), cellSize * rows + gridLineThickness * (rows + 1)));
		if (observeObj != null) {
			observeObj.addObserver(this);
		}
	}

	/**
	 * Creates a new GridView with the given number of rows, columns and grid
	 * lines turned on with the given thickness. The Dimension of this GridView
	 * is calculated and set to (cols*cellSize) x (rows*cellSize) plus the space
	 * the grid lines take up.
	 * 
	 * @param rows The number of rows in this GridView
	 * @param cols The number of columns in this GridView
	 * @param cellSize The size in pixels of each cell
	 * @param gridLineThickness The thickness in pixels of the grid lines
	 * @param observeObj An Observable object that updates this view
	 */
	public GridView(int rows, int cols, int cellSize, int gridLineThickness, Observable observeObj) {
		this.cellSize = cellSize;
		this.numRows = rows;
		this.numCols = cols;
		this.setGridLineThickness(gridLineThickness);
		this.showGridLines = true;
		setPreferredSize(new Dimension(cellSize * cols + gridLineThickness
				* (cols + 1), cellSize * rows + gridLineThickness * (rows + 1)));
		if (observeObj != null) {
			observeObj.addObserver(this);
		}
	}

	/**
	 * Sets the background of this GridView to the specified image.
	 * The image automatically scales to the size of the GridView.
	 * @param img The new background of this GridView.
	 */
	public void setBackground(BufferedImage img) {
		bgImage = img;
	}
	
	/**
	 * Returns the width of each cell in pixels
	 * @return Width of each cell in pixels
	 */
	public int getCellSize() {
		return cellSize;
	}

	/**
	 * Sets the width of each cell in pixels and updates the display for this GridView.
	 * @param cellSize Width of each cell in pixels.
	 */
	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
		this.repaint();
	}

	/**
	 * Returns the number of rows in this GridView
	 * @return Number of rows in this GridView
	 */
	public int getNumRows() {
		return numRows;
	}

	/**
	 * Sets the number of rows for this GridView and updates the display.
	 * @param numRows Number of rows in this GridView
	 */
	public void setNumRows(int numRows) {
		this.numRows = numRows;
		this.repaint();
	}

	/**
	 * Returns the number of columns in this GridView
	 * @return Number of columns in this GridView
	 */
	public int getNumCols() {
		return numCols;
	}

	/**
	 * Sets the number of columns for this GridView and updates the display.
	 * @param numCols Number of columns in this GridView
	 */
	public void setNumCols(int numCols) {
		this.numCols = numCols;
		this.repaint();
	}

	/**
	 * Returns the thickness of the grid lines in pixels.
	 * @return Thickness of the grid lines in pixels.
	 */
	public int getGridLineThickness() {
		return gridLineThickness;
	}

	/**
	 * Sets the thickness of the grid lines in pixels and updates the display.
	 * @param gridLineThickness Thickness of the grid lines in pixels.
	 */
	public void setGridLineThickness(int gridLineThickness) {
		this.gridLineThickness = gridLineThickness;
		this.repaint();
	}

	/**
	 * Makes grid lines visible and updates the display.
	 * @param newState Whether or not grid lines should be visible
	 */
	public void showGridLines(boolean newState) {
		showGridLines = newState;
		this.repaint();
	}

	/**
	 * Clears the objects in this grid
	 */
	public void clear() {
		gridObjects.clear();
	}
	
	/**
	 * Returns the number of objects in this grid.
	 * @return The number of objects in this grid.
	 */
	public int getNumObjects() {
		return gridObjects.size();
	}
	
	/**
	 * Draws a GridObject, which is an object that knows its own image (a BufferedImage) and its own location
	 * @param g Graphics context for the current GridView
	 */
	private void drawCellObjects(Graphics g) {
		for(GridObject go : gridObjects) {
			g.drawImage(go.getImage(), colToXCoord(go.getCol()), rowToYCoord(go.getRow()), cellSize, cellSize, null);
		}
	}

	/**
	 * Converts a column value into an x-coordinate for the upper left corner of this cell
	 * @param col Column to convert into an x-coordinate
	 * @return The x-coordinate where the specified column begins.
	 */
	public int colToXCoord(int col) {
		return gridLineThickness + col * (cellSize + gridLineThickness);
	}

	/**
	 * Converts a row value into a y-coordinate for the upper left corner of this cell
	 * @param row Row to convert into a y-coordinate
	 * @return The y-coordinate where the specified row begins.
	 */
	public int rowToYCoord(int row) {
		return gridLineThickness + row * (cellSize + gridLineThickness);
	}

	/**
	 * Converts an x-coordinate into a column.
	 * @param x An x-coordinate to convert into a column value.
	 * @return The column that contains the given x-coordinate.
	 */
	public int xCoordToCol(int x) {
		return x / (cellSize + gridLineThickness);
	}

	/**
	 * Converts a y-coordinate into a row.
	 * @param y A y-coordinate to convert into a row value.
	 * @return The row that contains the given y-coordinate.
	 */
	public int yCoordToRow(int y) {
		return y / (cellSize + gridLineThickness);
	}

	/**
	 * Fills the GridView one by one in row major order
	 * @param go A GridObject to fill the next available cell in this GridView.
	 */
	public void fillNextSpot(GridObject go) {
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				GridObject temp = new GridObject(r, c, go.getID(), go.getImage());
				if (!gridObjects.contains(temp)) {
					gridObjects.add(temp);
					repaint();
					return;
				}
			}
		}
	}

	/**
	 * Notification method called automatically by any objects this GridView is observing.
	 * When an observed object is modified, it lets us know and calls this method.
	 * @param o An object we are observing
	 * @param arg An optional parameter given to use by the observed object
	 */
	public void update(Observable o, Object arg) {

		// Clear all items
		if (arg == null) {
			gridObjects.clear();
		}
		// Clear the item at this coordinate
		else if (((GridObject)arg).getID() == 0) {
			gridObjects.remove(((GridObject)arg));
		}
		// Add an item
		else {
			gridObjects.add((GridObject) arg);
		}
		repaint();
	}

	/**
	 * Allows us to have our Mouse Listeners outside of this class.
	 * @param listener MouseListener that will listen to this GridView
	 * @param listener2 MouseMotionListener that will listen to this GridView
	 */
	public void addBoardListeners(MouseListener listener, MouseMotionListener listener2) {
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener2);
	}
	
	
	/**
	 * Called automatically by the system when repainting this component is needed.
	 * If you need to update the display, call repaint() instead of this.
	 * @param g Graphics context for this component.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (bgImage != null) {
			g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null, null);
		}
		
		if (showGridLines) {		
			g.setColor(Color.black);
			// Draw horizontal gridlines
			for (int r = 0; r <= numRows; r++) {
				g.fillRect(0, r * (cellSize + gridLineThickness), getWidth(),
						gridLineThickness);
			}
	
			// Draw vertical gridlines
			for (int c = 0; c <= numCols; c++) {
				g.fillRect(c * (cellSize + gridLineThickness), 0,
						gridLineThickness, getHeight());
			}
		}

		// Draw cell objects
		drawCellObjects(g);
	}	
}
