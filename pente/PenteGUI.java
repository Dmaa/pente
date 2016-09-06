package pente;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 * Creates a new JFrame that contains all the GUI elements for Pente.
 * @author eric_ferrante
 */
public class PenteGUI extends JFrame {

	private static final long serialVersionUID = -3027179269173623528L;

	private final Color BEIGE = new Color(232, 223, 205);
	private final int BOARD_X = 570; 	// x-pos of Pente board GUI
	private final int BOARD_Y = 120; 	// y-pos of Pente board GUI
	private final int BOARD_CELLS = 19; // # cells in x and y direction

	private GridView penteView;
	private JLabel player1Label;
	private JLabel player2Label;
	private JTextArea gameResultsArea;
	private JTextArea tournamentStandingsArea;
	private ScrollPanel messagePanel;
	private JCheckBox continuousModeCheckBox;
	private JCheckBox pauseCheckBox;
	private JComboBox<String> speedComboBox;
	private JButton playButton;
	
	public PenteGUI() {

		// Set up overall GUI Frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(1200, 780);
		this.setTitle("Pente");
		this.setLayout(null);

		// The raw data for Pente is stored here
		Board board = new Board(BOARD_CELLS, BOARD_CELLS);

		// Captured pieces are displayed in these GridViews
		GridView player1Captures = new GridView(2, 5, 30, 1, new Board(2, 5));
		player1Captures.setBackground(BEIGE);
		GridView player2Captures = new GridView(2, 5, 30, 1, new Board(2, 5));
		player2Captures.setBackground(BEIGE);

		// This is the GUI of the Pente board
		PentePanel boardPanel = new PentePanel(BOARD_CELLS, BOARD_CELLS, 30, 1, board);
		penteView = boardPanel.getGridView();
		boardPanel.setBounds(BOARD_X, BOARD_Y, (int) penteView.getPreferredSize().getWidth(),
							(int) penteView.getPreferredSize().getHeight());
		
		// Message area, seen as a scroll on the GUI
		messagePanel = new ScrollPanel();
		messagePanel.setOpaque(false);
		messagePanel.setBounds(BOARD_X, BOARD_Y - 100, (int) penteView.getPreferredSize().getWidth(), 100);

		// Overlay where intended moves and winning pieces are highlighted
		HighlightPanel highlightPanel = new HighlightPanel();
		highlightPanel.setBounds(0, 0, (int) penteView.getPreferredSize().getWidth(),
								(int) penteView.getPreferredSize().getHeight());
		this.setGlassPane(highlightPanel);
		highlightPanel.setVisible(true);

		// Player 1's game info is displayed here
		JPanel player1Panel = new JPanel();
		player1Panel.setOpaque(false);
		player1Panel.setPreferredSize(new Dimension(250, 150));
		TitledBorder player1Border = new TitledBorder(new LineBorder(Color.BLACK, 3, true), "Player 1");
		player1Border.setTitleFont(new Font("Arial", Font.BOLD, 16));
		player1Panel.setBorder(player1Border);
		Image img = new ImageIcon("images/white.png").getImage();
		BufferedImage bi = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		g.drawImage(img, 0, 0, 32, 32, null);
		g.dispose();
		ImageIcon player1Icon = new ImageIcon(bi);
		player1Label = new JLabel("Player 1", player1Icon, SwingConstants.CENTER);
		player1Panel.add(player1Label);
		JPanel player1CaptureFrame = new JPanel();
		player1CaptureFrame.setOpaque(false);
		player1CaptureFrame.setPreferredSize(player1Panel.getPreferredSize());
		player1CaptureFrame.add(player1Captures);
		player1Panel.add(player1CaptureFrame);

		// Player 2's game info is displayed here
		JPanel player2Panel = new JPanel();
		player2Panel.setOpaque(false);
		player2Panel.setPreferredSize(new Dimension(250, 150));
		TitledBorder player2Border = new TitledBorder(new LineBorder(Color.BLACK, 3, true), "Player 2");
		player2Border.setTitleFont(new Font("Arial", Font.BOLD, 16));
		player2Panel.setBorder(player2Border);
		Image img2 = new ImageIcon("images/black.png").getImage();
		BufferedImage bi2 = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics g2 = bi2.createGraphics();
		g2.drawImage(img2, 0, 0, 32, 32, null);
		g2.dispose();
		ImageIcon player2Icon = new ImageIcon(bi2);
		player2Label = new JLabel("Player 2", player2Icon, SwingConstants.CENTER);
		player2Panel.add(player2Label);
		JPanel player2CaptureFrame = new JPanel();
		player2CaptureFrame.setOpaque(false);
		player2CaptureFrame.setPreferredSize(player2Panel.getPreferredSize());
		player2CaptureFrame.add(player2Captures);
		player2Panel.add(player2CaptureFrame);

		// Game Results
		gameResultsArea = new JTextArea(8, 65);
		gameResultsArea.setEditable(false);
		gameResultsArea.setBackground(BEIGE);
		gameResultsArea.setFont(new Font("Courier New", Font.PLAIN, 12));
		JScrollPane gameResultsPane = new JScrollPane(gameResultsArea);
		gameResultsPane.setOpaque(false);
		TitledBorder gameResultsBorder = new TitledBorder(new LineBorder(Color.BLACK, 3, true), "Game Results");
		gameResultsBorder.setTitleFont(new Font("Arial", Font.BOLD, 16));
		gameResultsPane.setBorder(gameResultsBorder);

		// Tournament standings
		tournamentStandingsArea = new JTextArea(8, 60);
		tournamentStandingsArea.setBackground(BEIGE);
		tournamentStandingsArea.setFont(new Font("Courier New", Font.PLAIN, 12));
		JScrollPane tournamentStandingsPane = new JScrollPane(tournamentStandingsArea);
		tournamentStandingsPane.setOpaque(false);
		TitledBorder tournamentStandingsBorder = new TitledBorder(new LineBorder(Color.BLACK, 3, true), "Tournament Standings");
		tournamentStandingsBorder.setTitleFont(new Font("Arial", Font.BOLD, 16));
		tournamentStandingsPane.setBorder(tournamentStandingsBorder);

		// Panel containing the game and tournament results
		JPanel resultsPanel = new JPanel();
		resultsPanel.setBackground(Color.pink);
		resultsPanel.setOpaque(false);
		resultsPanel.setPreferredSize(new Dimension(500, 300));
		resultsPanel.add(gameResultsPane);
		resultsPanel.add(tournamentStandingsPane);

		// Set up left half of GUI w/ game info
		JPanel leftPanel = new JPanel();
		leftPanel.setOpaque(false);
		JLabel title = new JLabel(new ImageIcon("images/pente_title.png"));
		leftPanel.add(title);
		leftPanel.setBounds(20, 20, 550, 690);
		JPanel leftSubPanel = new JPanel();
		leftSubPanel.setOpaque(false);
		leftSubPanel.add(player1Panel);
		leftSubPanel.add(player2Panel);
		leftPanel.add(leftSubPanel);		
		leftPanel.add(resultsPanel);

		// Game controls JPanel
		JPanel controlPanel = new JPanel();
		controlPanel.setBackground(BEIGE);
		controlPanel.setBorder(new LineBorder(Color.black, 3, true));
		controlPanel.setPreferredSize(new Dimension(500, 110));
		JPanel controlPanelSub = new JPanel();
		controlPanelSub.setLayout(new GridLayout(1, 2));
		controlPanelSub.setPreferredSize(new Dimension(480, 60));
		controlPanelSub.setOpaque(false);
		controlPanelSub.setBackground(Color.YELLOW);
		JPanel controlPanelLeft = new JPanel();
		controlPanelLeft.setOpaque(false);
		continuousModeCheckBox = new JCheckBox("Continuous Mode", false);
		controlPanelLeft.add(continuousModeCheckBox);
		pauseCheckBox = new JCheckBox("Pause After Game", true);
		controlPanelLeft.add(pauseCheckBox);
		JPanel controlPanelRight = new JPanel();
		controlPanelRight.setOpaque(false);
		String[] speedItems = {"No Delay", "Fast", "Medium", "Slow"};
		speedComboBox = new JComboBox<String>(speedItems);
		speedComboBox.setActionCommand("Change Speed");
		controlPanelRight.add(speedComboBox);
		controlPanelSub.add(controlPanelLeft);
		controlPanelSub.add(controlPanelRight);		
		controlPanel.add(controlPanelSub);
		playButton = new JButton("Step");
		playButton.setActionCommand("Play");
		controlPanel.add(playButton);
		leftPanel.add(controlPanel);
		
		// Add top level GUI panels
		this.add(leftPanel);
		this.add(boardPanel);
		this.add(messagePanel);

		// Add background Image
		try {
			final BufferedImage backgroundImage;
			backgroundImage = ImageIO.read(new File("images/pente_background.png"));
			class BackgroundPanel extends JPanel {
				private static final long serialVersionUID = -6715864334078002215L;
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(backgroundImage, 0, 0, getParent().getWidth(), getParent().getHeight(), null);
				}				
			}
			// Background
			BackgroundPanel bg = new BackgroundPanel();
			bg.setBounds(0, 0, this.getWidth(), this.getHeight());
			this.add(bg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

		// Create a controller that updates this GUI
		new PenteController(this, penteView, board, highlightPanel, player1Captures, player2Captures);
		
		penteView.grabFocus();
		this.revalidate();
	}

	/**
	 * Appends a line of text to the Game Info text area.
	 * @param line The line of text to add to the Game Info text area.
	 */
	public void addGameInfo(String line) {
		gameResultsArea.append(line + "\n");
	}

	/**
	 * Updates the tournament text area to the String given.
	 * @param line The tournament text are is set to this String.
	 */
	public void setTournamentInfo(String line) {
		tournamentStandingsArea.setText(line);
	}

	/**
	 * Updates the ScrollPanel's "playerX vs playerY" message.
	 * @param p1 Reference to player 1
	 * @param p2 Reference to player 2
	 */
	public void setVSMessage(Player p1, Player p2) {
		messagePanel.setVSMessage(p1, p2);
	}
	
	/**
	 * Updates the label for Player 1's name
	 * @param name Player 1's name label in the GUI
	 */
	public void setPlayer1Name(String name) {
		player1Label.setText(name);
		repaint();
	}

	/**
	 * Updates the label for Player 2's name
	 * @param name Player 2's name label in the GUI
	 */
	public void setPlayer2Name(String name) {
		player2Label.setText(name);
		repaint();
	}

	/**
	 * Updates the Icon displayed for player 1
	 * @param img The Image to be used for player 1
	 */
	public void setPlayer1Icon(BufferedImage img) {
		BufferedImage bi = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		g.drawImage(img, 0, 0, 32, 32, null);
		g.dispose();
		ImageIcon playerIcon = new ImageIcon(bi);
		player1Label.setIcon(playerIcon);
		repaint();
	}

	/**
	 * Updates the Icon displayed for player 2
	 * @param img The Image to be used for player 2
	 */
	public void setPlayer2Icon(BufferedImage img) {
		BufferedImage bi = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		g.drawImage(img, 0, 0, 32, 32, null);
		g.dispose();
		ImageIcon playerIcon = new ImageIcon(bi);
		player2Label.setIcon(playerIcon);
		repaint();
	}

	/**
	 * Returns the "Play" button so that ActionListeners outside this class
	 * can modify the button properties
	 * @return The "Play" button in the GUI.
	 */
	public JButton getPlayButton() {
		return playButton;
	}
	
	/**
	 * This function gives us the ability to add mouse listeners outside the GUI class
	 * @param listener A MouseListener to listen to this GUI.
	 * @param listener2 A MouseMotionListener to listen to this GUI.
	 */
	public void addBoardListeners(MouseListener listener, MouseMotionListener listener2) {
		penteView.addMouseListener(listener);
		penteView.addMouseMotionListener(listener2);
	}
	
	/**
	 * This function gives us the ability to add mouse listeners outside the GUI class
	 * @param listener An ActionListener to listen to GUI events. 
	 */
	public void addGUIListeners(ActionListener listener) {
		continuousModeCheckBox.addActionListener(listener);
		pauseCheckBox.addActionListener(listener);
		speedComboBox.addActionListener(listener);
		playButton.addActionListener(listener);
	}
}
