package pente;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.filechooser.FileSystemView;

/**
 * This class serves as the main controller for Pente games.  This is where Player objects
 * are created, where games are run, and where ActionListeners and MouseListeners perform
 * their actions.  The PenteController serves as a go-between for the GUI and raw game data.
 * @author eric_ferrante
 */
public class PenteController {

	private Board board;
	private PenteGUI gui;
	private GridView view;
	private PenteLogic penteLogic;
	private HighlightPanel highlightPanel;
	private Player player1;
	private Player player2;
	private Player whoseTurn;
	private GridView player1Captures;
	private GridView player2Captures;
	private boolean gameWon;
	private Timer gameTimer;
	private ArrayList<Player> playerList;
	private ArrayList<GameResult> gameResultList;
	private int player1Index = 0;
	private int player2Index = 1;
	private PenteController controller = this;
	private int gameCounter = 1;
	private boolean pauseAfterGame = true;
	private boolean continuousMode = false;
	private boolean illegalMove = false;
	private String illegalMessage = "";
	private int timerSpeed = 0;
	private int turnCounter = 1;

	/**
	 * Creates a new PenteController with the given object references.
	 * @param g A PenteGUI to listen to
	 * @param v A GridView that represents the visual board
	 * @param b A Board that contains the raw game data
	 * @param hp A HighlightPanel that serves as an overlay showing highlighted areas of the board
	 * @param p1 Player 1's GridView where captured pieces are shown
	 * @param p2 Player 2's GridView where captured pieces are shown
	 */
	public PenteController(PenteGUI g, GridView v, Board b, HighlightPanel hp, GridView p1, GridView p2) {
		gui = g;
		board = b;
		view = v;
		highlightPanel = hp;
		player1Captures = p1;
		player2Captures = p2;
		MyMouseListener myMouseListener = new MyMouseListener();
		view.addBoardListeners(myMouseListener, myMouseListener);
		MyGUIListener myGUIListener = new MyGUIListener();
		gui.addGUIListeners(myGUIListener);
		penteLogic = new PenteLogic(board);
		gameWon = false;
		playerList = new ArrayList<Player>();
		gameResultList = new ArrayList<GameResult>();
		int playerID = 1;

		// Adds a human player to the tournament.  Add as many as you like.
		Player humanPlayer1 = new Player("white.png", "Bob", playerID++);
		playerList.add(humanPlayer1);

		// This section adds all AIPlayers in the "aiplayer" subfolder to the tournament.
		// To remove AIPlayers, either set the variable below to false or temporarily
		// remove any AIPlayer class files from the "aiplayer" subfolder.
		boolean includeAIPlayers = true;
		
		if (includeAIPlayers) {
			FileSystemView fsv = FileSystemView.getFileSystemView();
			File [] file = fsv.getFiles(new File("aiplayer"),true);
			String className = "";
			for (int i = 0; i < file.length; i++) {
				if (file[i].getName().endsWith("class") && (file[i].getName().contains("AIPlayerD")) && !file[i].getName().equals("AIPlayer.class")) {
					Class<? extends AIPlayer> theClass = null;
					AIPlayer obj = null;
					className = file[i].getName();
					int spot = className.lastIndexOf(".");
					className = className.substring(0,spot);						
					try {
						theClass = Class.forName("aiplayer." + className).asSubclass(AIPlayer.class);
						Constructor<?> constructor = theClass.getConstructor(int.class);
						obj = (AIPlayer) constructor.newInstance(playerID++);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (obj != null)
						playerList.add(obj);
				}
			}
		}

		if (playerList.size() == 0) {
			System.out.println("No players have been added to the player list.  Quitting...");
			System.exit(0);
		}

		player1 = playerList.get(player1Index);
		player2 = playerList.get(player2Index);

		for (Player p : playerList) {
			gameResultList.add(new GameResult(p, 0, 0));
		}
		
		gui.setPlayer1Name(player1.getName());
		gui.setPlayer2Name(player2.getName());
		gui.setPlayer1Icon(player1.getImage());
		gui.setPlayer2Icon(player2.getImage());
		gui.setVSMessage(player1, player2);
		whoseTurn = player1;

		String gameResult = String.format(" %-6s %-20.20s        %-20.20s", "Game", " Player 1", " Player 2");
		gui.addGameInfo(gameResult);
		String separator = "";
		for (int i = 0; i < 65; i++) {
			switch(i) {
				case 7:
				case 35: separator += "\u252C"; break;
				default: separator += "\u2500";
			}
		}
		gui.addGameInfo(separator);
		
		gameTimer = new Timer(timerSpeed, new MyTimerListener());
	}

	/**
	 * This is the official makeMove function that actually makes a move and modifies the board.
	 * @param loc The Location where the current Player will make a move.
	 */
	public void makeMove(Location loc) {
		GridObject bob = new GridObject(loc.getRow(), loc.getCol(), whoseTurn.getID(), whoseTurn.getImage());

		// Tournament rules:  First move must be in center of the board
		if (turnCounter == 1 && !penteLogic.isAtBoardCenter(loc.getRow(), loc.getCol())) {
			illegalMove = true;
			illegalMessage = "Invalid move by " + whoseTurn.getName() + "!  First move must be in the center.  Forfeiting turn...";
		}
		else if (turnCounter == 3 && !penteLogic.isOutsideStartBox(loc.getRow(), loc.getCol())) {
			illegalMove = true;
			illegalMessage = "Invalid move by " + whoseTurn.getName() + "!  Second move must be at least 3 units away.  Forfeiting turn...";			
		}
		else if (!(penteLogic.isInBounds(loc.getRow(), loc.getCol()) && penteLogic.isEmpty(loc.getRow(), loc.getCol()))) {
			illegalMove = true;
			illegalMessage = "Invalid move by " + whoseTurn.getName() + "!  Forfeiting turn...";			
		}
		else {
			highlightPanel.setDropLoc((int)view.getParent().getLocation().getX(), (int)view.getParent().getLocation().getY(), new Location(loc.getRow(), loc.getCol()));
			highlightPanel.repaint();
			board.write(loc.getRow(), loc.getCol(), bob);

			// Check for a win of 5-in-a-row (or more)
			if (penteLogic.fiveInARow(loc.getRow(), loc.getCol(), whoseTurn.getID()).size() != 0) {
				highlightPanel.setWinLocs(penteLogic.fiveInARow(loc.getRow(), loc.getCol(), whoseTurn.getID()));
				if (pauseAfterGame) {
					JOptionPane.showMessageDialog(null, whoseTurn.getName() + " Wins by 5-in-a-row!");
				}
				//System.out.println(whoseTurn.getName() + " Wins by 5-in-a-row!");
				String gameResult = "";
				if (whoseTurn.getID() == player1.getID())
					gameResult = String.format(" %-5d \u2502 %-20.20s (W5) \u2502 %-20.20s     ", gameCounter, player1.getName(), player2.getName());
				else
					gameResult = String.format(" %-5d \u2502 %-20.20s      \u2502 %-20.20s (W5)", gameCounter, player1.getName(), player2.getName());
				gui.addGameInfo(gameResult);
				updateGameResults(whoseTurn, true, false);
				Collections.sort(gameResultList, Collections.reverseOrder());
				
				String resultString = String.format(" %-5s %-23s %-7s %-7s %-7s\n", "Rank", " Player", "Wins", "5-Row", "Capture");

				for (int i = 0; i < 60; i++) {
					switch(i) {
						case 6: case 29: case 37: case 45: resultString += "\u252C"; break;
						default: resultString += "\u2500";
					}
				}
				resultString += "\n";

				for (int i = 0; i < gameResultList.size(); i++) {
					GameResult g = gameResultList.get(i);
					resultString += String.format(" %-5d\u2502 %-20.20s \u2502 %-6d\u2502 %-6d\u2502 %-6d\n", i+1, g.getPlayer().getName(),
							(g.getFiveInARowWins() + g.getCaptureWins()), g.getFiveInARowWins(), g.getCaptureWins());
				}
				gui.setTournamentInfo(resultString);
				gameWon = true;
			}
			// Check for a win of 10 captured pieces
			else {
				ArrayList<Location> locs = penteLogic.capture(loc.getRow(), loc.getCol(), whoseTurn.getID());
				highlightPanel.setWinLocs(locs);
				highlightPanel.setDropLoc(null);
				if (locs.size() != 0) {
					for (int i = 0; i < locs.size(); i++) {
						if (whoseTurn == player1) {
							player1Captures.fillNextSpot(new GridObject(0, 0, player2.getID(), player2.getImage()));
						}
						else {
							player2Captures.fillNextSpot(new GridObject(0, 0, player1.getID(), player1.getImage()));
						}
					}
					player1.setMyCaptures(player1Captures.getNumObjects());
					player1.setOpponentCaptures(player2Captures.getNumObjects());
					player2.setMyCaptures(player2Captures.getNumObjects());
					player2.setOpponentCaptures(player1Captures.getNumObjects());

					if (player1Captures.getNumObjects() >= 10 || player2Captures.getNumObjects() >= 10) {
						if (pauseAfterGame) {
							JOptionPane.showMessageDialog(null, whoseTurn.getName() + " wins by capture!");
						}
						String gameResult = "";
						if (whoseTurn.getID() == player1.getID())
							gameResult = String.format(" %-5d \u2502 %-20.20s (WC) \u2502 %-20.20s     ", gameCounter, player1.getName(), player2.getName());
						else
							gameResult = String.format(" %-5d \u2502 %-20.20s      \u2502 %-20.20s (WC)", gameCounter, player1.getName(), player2.getName());
						gui.addGameInfo(gameResult);

						updateGameResults(whoseTurn, false, true);
						Collections.sort(gameResultList, Collections.reverseOrder());

						String resultString = String.format(" %-5s %-23s %-7s %-7s %-7s\n", "Rank", " Player", "Wins", "5-Row", "Capture");

						for (int i = 0; i < 60; i++) {
							switch(i) {
								case 6: case 29: case 37: case 45: resultString += "\u252C"; break;
								default: resultString += "\u2500";
							}
						}
						resultString += "\n";

						for (int i = 0; i < gameResultList.size(); i++) {
							GameResult g = gameResultList.get(i);
							resultString += String.format(" %-5d\u2502 %-20.20s \u2502 %-6d\u2502 %-6d\u2502 %-6d\n", i+1, g.getPlayer().getName(),
									(g.getFiveInARowWins() + g.getCaptureWins()), g.getFiveInARowWins(), g.getCaptureWins());
						}
						
						gui.setTournamentInfo(resultString);
						gameWon = true;
					}
				}
			}
		}
		
		if (illegalMove) {
			JOptionPane.showMessageDialog(null, illegalMessage);
		}

		if (gameWon) {
			gameCounter++;
			player2Index++;
			if (player2Index < playerList.size() && playerList.get(player2Index).equals(player1))
				player2Index++;
			if (player2Index >= playerList.size()) {
				player2Index = 0;
				player1Index++;
				if (player1Index  >= playerList.size()) {
					System.out.println("Tournament Over.");
					gui.getPlayButton().setText("Play");
					gameTimer.stop();
					player1Index = 0;
					player2Index = 1;
					highlightPanel.setDropLoc(null);
					highlightPanel.setWinLocs(null);
					highlightPanel.repaint();
					//System.exit(0);
				}
			}

			gameWon = false;
			turnCounter = 1;
			highlightPanel.setDropLoc(null);
			highlightPanel.setWinLocs(null);
			highlightPanel.repaint();
			board.reset();
			gui.repaint();
			if (player1Index < playerList.size() && player2Index < playerList.size()) {
				player1 = playerList.get(player1Index);
				player2 = playerList.get(player2Index);
				player1Captures.clear();
				player2Captures.clear();
				player1.setMyCaptures(0);
				player1.setOpponentCaptures(0);
				player2.setMyCaptures(0);
				player2.setOpponentCaptures(0);
				gui.setPlayer1Name(player1.getName());
				gui.setPlayer2Name(player2.getName());
				gui.setPlayer1Icon(player1.getImage());
				gui.setPlayer2Icon(player2.getImage());
				gui.setVSMessage(player1, player2);
				whoseTurn = player1;
				// System.out.println("New game:  " + player1.getName() + " vs " + player2.getName());
			}
		}
		else {
			whoseTurn = (whoseTurn == player1 ? player2 : player1);
			turnCounter++;
			illegalMove = false;
			highlightPanel.setDropLoc(null);
			highlightPanel.repaint();
		}
	}

	/**
	 * When a player wins, this method is called to either update the Player's game statistics
	 * or to create a new entry for the player.
	 * @param p The Player for which game statistics are being created or updated.
	 * @param fiveInARow Whether or not this player won by getting five-in-a-row
	 * @param capture Whether or not this player won by capturing 10 opponent pieces.
	 */
	public void updateGameResults(Player p, boolean fiveInARow, boolean capture) {
		boolean recordFound = false;

		for (GameResult g : gameResultList) {
			if (g.getPlayer().getID() == whoseTurn.getID()) {
				if (fiveInARow)
					g.addFiveInARowWin();
				else if (capture)
					g.addCaptureWin();
				recordFound = true;
			}
		}
		if (!recordFound) {
			gameResultList.add(new GameResult(whoseTurn, 1, 0));
		}
	}
	
	/**
	 * Returns a 2D array of integers representing player IDs on the board.
	 * If a spot is empty, the player ID is zero.
	 * @return A 2D array of player IDs.
	 */
	public int[][] boardCopy() {
		int[][] boardCopy = new int[board.numRows()][board.numCols()];
		for (int row = 0; row < board.numRows(); row++) {
			for (int col = 0; col < board.numCols(); col++) {
				if (board.read(row,  col) == null) {
					boardCopy[row][col] = 0;
				}
				else {
					boardCopy[row][col] = ((GridObject)(board.read(row, col))).getID();
				}
			}
		}
		return boardCopy;
	}

	/**
	 * Handles the GUI interaction code for buttons, checkboxes, etc
	 * @author eric_ferrante
	 */
	class MyGUIListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Play")) {
				if (continuousMode && !gameTimer.isRunning()) {
					((JButton)(e.getSource())).setText("Stop");
					gameTimer.start();
				}
				else if (continuousMode && gameTimer.isRunning()) {
					((JButton)(e.getSource())).setText("Play");
					gameTimer.stop();
				}
				else if (!continuousMode)
				if (whoseTurn instanceof AIPlayer) {
						Location loc = whoseTurn.makeMove(boardCopy(), turnCounter);
						controller.makeMove(loc);
				}
			}
			else if (e.getActionCommand().equals("Continuous Mode")) {
				continuousMode = ((JCheckBox)(e.getSource())).isSelected();
				if (!continuousMode) {
					gui.getPlayButton().setText("Step");
					if (gameTimer.isRunning()) {
						gameTimer.stop();						
					}
				}
				else if (continuousMode) {
					gui.getPlayButton().setText("Play");
				}
			}
			else if (e.getActionCommand().equals("Pause After Game")) {
				pauseAfterGame = ((JCheckBox)(e.getSource())).isSelected();
			}
			else if (e.getActionCommand().equals("Change Speed")) {
				String result = (String)(((JComboBox<?>)(e.getSource())).getSelectedItem());
				if (result.equals("No Delay")) timerSpeed = 0;
				else if (result.equals("Fast")) timerSpeed = 10;
				else if (result.equals("Medium")) timerSpeed = 800;
				else if (result.equals("Slow")) timerSpeed = 2000;
				gameTimer.setDelay(timerSpeed);
			}
		}
	}

	/**
	 * Handles the game timer so that AIPlayers will automatically make a move when the timer
	 * fires.
	 * @author eric_ferrante
	 */
	class MyTimerListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// If current player is an AI player, get the move from the player and make the move
			if (whoseTurn instanceof AIPlayer) {
				Location loc = whoseTurn.makeMove(boardCopy(), turnCounter);
				controller.makeMove(loc);
			}
		}
	}

	/**
	 * Handles MouseListener events for the GUI.
	 * @author eric_ferrante
	 */
	class MyMouseListener extends MouseAdapter {

		// Highlight piece placement
		@Override
		public void mousePressed(MouseEvent e) {
			if (!(whoseTurn instanceof AIPlayer)) {
				int row = view.yCoordToRow(e.getY());
				int col = view.xCoordToCol(e.getX());
				if (penteLogic.isInBounds(row, col)) {
					highlightPanel.setDropLoc((int)view.getParent().getLocation().getX(), (int)view.getParent().getLocation().getY(), new Location(row, col));
					highlightPanel.repaint();
				}
			}
		}

		// Highlight piece placement
		@Override
		public void mouseDragged(MouseEvent e) {
			if (!(whoseTurn instanceof AIPlayer)) {
				int row = view.yCoordToRow(e.getY());
				int col = view.xCoordToCol(e.getX());
				if (penteLogic.isInBounds(row, col)) {
					highlightPanel.setDropLoc((int)view.getParent().getLocation().getX(), (int)view.getParent().getLocation().getY(), new Location(row, col));
					highlightPanel.repaint();
				}
			}
		}

		// If current player is a human, get move location from mouse coordinates
		@Override
		public void mouseReleased(MouseEvent e) {
			// If current player is a human player, get move location from mouseReleased location
			if (!(whoseTurn instanceof AIPlayer)) {
				int row = view.yCoordToRow(e.getY());
				int col = view.xCoordToCol(e.getX());
				controller.makeMove(new Location(row, col));
			}
		}
	}
}
