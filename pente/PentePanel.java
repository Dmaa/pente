package pente;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * A PentePanel is a JPanel that contains a GridView where pieces will be placed on grid line intersections
 * rather than within grid lines.  In order to use a PentePanel, one should access and operate on its GridView
 * object which can be obtained by calling getGridView().
 * 
 * Here's how a PentePanel is built:  Imagine two pieces of graph paper placed so their grid lines match up.
 * Now shift the top paper one-half square down and one-half square right.  Then erase the grid lines
 * from the top paper and make the paper transparent so the bottom grid lines show through.  The effect looks
 * like you are playing on grid lines when in fact you are playing on an invisible grid above the grid lines.
 * This is exactly how a PentePanel works.
 * 
 * @author eric_ferrante
 */
public class PentePanel extends JPanel {

	private static final long serialVersionUID = -5930875544560346164L;
	private GridView gridView;

	/**
	 * Constructs a new PentePanel from the given info.
	 * @param rows Number of rows on the Pente board
	 * @param cols Number of columns on the Pente board
	 * @param cellSize Cell size in pixels (which determines the overall JPanel size)
	 * @param gridLineThickness Grid line thickness in pixels
	 * @param observeObj An observable object the GridView will monitor for changes.  In Pente, this is the Board.
	 */
	public PentePanel(int rows, int cols, int cellSize, int gridLineThickness, Observable observeObj) {
		
		gridView = new GridView(rows, cols, cellSize, gridLineThickness, observeObj);
		gridView.setBounds(0, 0, (int) gridView.getPreferredSize().getWidth(), (int) gridView.getPreferredSize().getHeight());
		gridView.setBackground(Color.YELLOW);
		gridView.setOpaque(false);
		gridView.showGridLines(false);
		gridView.setLayout(null);
		
		GridView backgroundGrid = new GridView(18, 18, 30, 1, null);
		backgroundGrid.setBackground(Color.pink);
		backgroundGrid.setOpaque(true);
		backgroundGrid.setBounds(15, 15, (int) gridView.getPreferredSize().getWidth() - 31,	(int) gridView.getPreferredSize().getHeight() - 31);

		try {
			backgroundGrid.setBackground(ImageIO.read(new File("images/parchment.jpg")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.setBackground(new Color(127, 82, 23));
		this.setPreferredSize(new Dimension((int) gridView.getPreferredSize().getWidth(), (int) gridView.getPreferredSize().getHeight()));
		this.setLayout(null);
		
		this.add(gridView);
		this.add(backgroundGrid);
	}

	/**
	 * Getter method to access the GridView used by this PentePanel
	 * @return The GridView used by this PentePanel
	 */
	public GridView getGridView() {
		return gridView;
	}

}
