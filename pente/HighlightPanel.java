package pente;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JComponent;

/**
 * This class handles drawing the overlays on top of the Pente board when the mouse is
 * pressed (for Human Players) and when winning pieces or captured pieces are highlighted.
 * This class is used as the GlassPane of the main JFrame, which is why it always draws
 * on top of everything else.
 * @author eric_ferrante
 */
public class HighlightPanel extends JComponent {

	private static final long serialVersionUID = -5473786350256457600L;
	private Color highlightColor = new Color(255, 255, 255, 100);
	private Location dropLoc;
	private ArrayList<Location> winLocs = null;
	private int cellSize = 30;
	private int xOffset;
	private int yOffset;
	
	/**
	 * Updates the display for this component.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		 // Highlight winning pieces, if any
		if (winLocs != null && winLocs.size() > 0) {
			for (Location loc : winLocs) {
				g.setColor(highlightColor); // Yellow highlight
				g.fillRect(xOffset + loc.getCol() * (cellSize+1)+1, yOffset + loc.getRow() * (cellSize+1)+1, cellSize, cellSize);
			}
		}
		// Draw ghost of Piece about to be dropped, if any.
		// Also, draw a red X if the attempted Location is invalid.
		else if (dropLoc != null) {
				g.setColor(highlightColor);
				g.fillOval(xOffset + dropLoc.getCol() * (cellSize + 1)+1, yOffset + dropLoc.getRow() * (cellSize + 1)+1, cellSize, cellSize);
			}

	}

	/**
	 * Sets the winning locations to be highlighted
	 * @param locs The winning locations to be highlighted
	 */
	public void setWinLocs(ArrayList<Location> locs) {
		winLocs = locs;
	}

	/**
	 * Gets the location a user is currently clicking or dragging on where a piece
	 * might possibly be dropped.
	 * @return the location a user is currently clicking or dragging on where a piece
	 * might possibly be dropped.
	 */
	public Location getDropLoc() {
		return dropLoc;
	}

	/**
	 * The location a user has clicked on where a piece has been dropped.
	 * @param dropLoc The location a user has clicked on where a piece has been dropped.
	 */
	public void setDropLoc(Location dropLoc) {
		this.dropLoc = dropLoc;
	}

	/**
	 * The location a user has clicked on where a piece has been dropped.
	 * Since the HighlightPanel (a GlassPane) may have a different (x,y) origin than the
	 * panel it is meant to highlight, we may need to include an offset to align the
	 * origin with the panel it is meant to highlight.  
	 * @param dropLoc The location a user has clicked on where a piece has been dropped.
	 * @param xOffset The amount of offset in pixels to align the highlight panel's origin
	 * to the origin of the panel it is meant to highlight.
	 * @param yOffset The amount o foffset in pixels to align the highlight panel's origin
	 * to the origin of the panel it is meant to highlight.
	 */
	public void setDropLoc(int xOffset, int yOffset, Location dropLoc) {
		this.dropLoc = dropLoc;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
}