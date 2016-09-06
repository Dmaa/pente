/* Name: Geonil Kim
 * Date: 5/26/2015
 * Period: 4
 * How long it took me: 4 hours
 * 
 * This was tiring because I had to restart from my previous
 * code and almost restart a new strategy.  This is unfinished
 * but as I did this coding, there were some problems like debugging
 * when I did not know why some errors were happening.  I always
 * go for the protection first and then winning.  And when I see
 * a chance, I always go for it.  But I need to code the part that
 * forsees a move that may happen after.
 */

package aiplayer;

import java.util.ArrayList;

import pente.*;

public class AIPlayerGKimPeriod4 extends AIPlayer {

	// Change these to match your AIPlayer info
	private static String name = "Geonil's Intermediate AI";
	private static String iconFile = "ruby.png";
	private static Board board;
	private static PenteLogic penteLogic;
	private static int enemyID = -1;

	public AIPlayerGKimPeriod4(int id) {
		super(iconFile, name, id);
	}

	@Override
	public Location makeMove(int[][] idArray, int moveCount) {																// Make Move

		// Basic beginning steps
		if (moveCount == 1) {
			System.out.println("------------------New Game------------------");
			return new Location(idArray.length / 2, idArray[0].length / 2);
		} else if(moveCount == 2) {
			System.out.println("------------------New Game------------------");
			int row = 0, col = 0;
			do {
				row = (int) (Math.random() * idArray.length);
				col = (int) (Math.random() * idArray[0].length);
			} while (row < 5 || idArray.length - row < 5 || col < 5 || idArray[0].length - col < 5 || (row == idArray.length / 2 && col == idArray[0].length / 2));
			return new Location(row, col);
		} else if (moveCount == 3) {
		
			int row = 0, col = 0;
			do {
				row = (int) (Math.random() * idArray.length);
				col = (int) (Math.random() * idArray[0].length);
			} while (Math.abs(row - idArray.length / 2) <= 3
					&& Math.abs(col - idArray[0].length / 2) <= 3 || row < 5 || idArray.length - row < 5 || col < 5 || idArray[0].length - col < 5);
			return new Location(row, col);
		}
		System.out.println("--New Move--");
		if (canWin(idArray) != null) {
			System.out.println("MOVE +: YOU MIGHT WIN");
			return canWin(idArray);
		}

		if (myCaptures() == 8 && theirBlocked(idArray, 2) != null) {
			return theirBlocked(idArray, 2);
		}
		
		if (canLose(idArray) != null) {
			System.out.println("MOVE -: YOU MIGHT LOSE");
			return canLose(idArray);
		}

		if (opponentCaptures() == 8 && myTwo(idArray) != null) {
			return myTwo(idArray);
		}
		
		if(tryWin(idArray) != null) {
			System.out.println("MOVE ?: Try to WIN");
			return tryWin(idArray);
		}
		if(myUnblocked(idArray, 3) != null) {
			System.out.println("Move 3: You have three UNBLOCKED");
			return myUnblocked(idArray, 3);
		}
		
		if (theirUnblocked(idArray, 3) != null) {
			System.out.println("Move 2: They have three UNBLOCKED");
			return theirUnblocked(idArray, 3);
		}

		if (theirTwo(idArray) != null) {
			System.out.println("Move 3: Capture their two");
			return theirTwo(idArray);
		}
		
		if (myTwo(idArray) != null) {
			System.out.println("Move 4: Defend your two");
			return myTwo(idArray);
		}

		if (opponentCaptures() != 8 && theirBlocked(idArray, 3) != null) {
			System.out.println("Move 5: They have 3 in-a-row BLOCKED");
			return theirBlocked(idArray, 3);
		}

		Location randPos = smartMove(idArray);
		System.out.println("Move 6: Make smart decision");
		return randPos;

	}

	public boolean isInBounds(int[][] array, int row, int col) {
		return (row >= 0 && row < array.length && col >= 0 && col < array[0].length);
	}
	
	public boolean isMine(int[][] board, int r, int c) {
		return (isInBounds(board, r, c) && board[r][c] == getID());
	}
	
	public boolean isTheirs(int[][] board, int r, int c) {
		return (isInBounds(board, r, c) && board[r][c] != getID() && board[r][c] > 0);
	}
	
	public boolean isBlank(int[][] board, int r, int c) {
		return (isInBounds(board, r, c) && board[r][c] == 0);
	}

	// *****************************************************************************************************************************************
	public Location myDiagonal(int[][] board, int r, int c, int n) {														// Diagonal Self
		int chain = 0;
		for (int i = 0; i < n; i++) {
			if (isMine(board, r + i, c + i)) {
				chain++;

			}
		}
		if (chain == n && isBlank(board, r + n, c + n) && isTheirs(board, r - 1, c - 1)) {
			return new Location(r + n, c + n);
		} else if (chain == n && isBlank(board, r - 1, c - 1) && isTheirs(board, r + n, c + n)) {
			return new Location(r - 1, c - 1);
		}

		chain = 0;
		for (int i = 0; i < n; i++) {
			if (isMine(board, r - i, c + i)) {
				chain++;
			}
		}
		if (chain == n && isBlank(board, r - n, c + n) && isTheirs(board, r + 1, c - 1)) {
			return new Location(r - n, c + n);
		} else if (chain == n && isBlank(board, r + 1, c - 1) && isTheirs(board, r - n, c + n)) {
			return new Location(r + 1, c - 1);
		}

		return null;
	}
	
	public Location myDiagonalAlone(int[][] board, int r, int c, int n) {														// Diagonal Self
		int chain = 0;
		for (int i = 0; i < n; i++) {
			if (isMine(board, r + i, c + i)) {
				chain++;
			}
		}
		if (chain == n && isBlank(board, r + n, c + n) && 
				isBlank(board, r - 1, c - 1)) {
			return new Location(r - 1, c - 1);
		}

		chain = 0;
		for (int i = 0; i < n; i++) {
			if (isMine(board, r - i, c + i)) {
				chain++;
			}
		}
		if (chain == n && isBlank(board, r - n, c + n) && 
				isBlank(board, r + 1, c - 1)) {
			return new Location(r + 1, c - 1);
		}

		return null;
	}
	
	public Location myDiagonalFour(int[][] board, int r, int c) {														// Diagonal Self
		int chain = 0;
		int empty = 0;
		int emptyR = 0;
		int emptyC = 0;
		for (int i = 0; i < 5; i++) {
			if (isMine(board, r + i, c + i)) {
				chain++;
			} else if(isBlank(board, r + i, c + i)) {
				empty++;
				emptyR = r + i;
				emptyC = c + i;
			}
		}
		if (chain == 4 && empty == 1 && isInBounds(board, emptyR, emptyC)) {
			
			return new Location(emptyR, emptyC);
		} 

		chain = 0;
		empty = 0;
		emptyR = 0;
		emptyC = 0;
		for (int i = 0; i < 5; i++) {
			if (isMine(board, r - i, c + i)) {
				chain++;
			} else if(isBlank(board, r - i, c + i)) {
				empty++;
				emptyR = r - i;
				emptyC = c + i;
			}
		}
		if (chain == 4 && empty == 1 && isInBounds(board, emptyR, emptyC)) {
			return new Location(emptyR, emptyC);
		} 


		return null;
	}
	
	// *****************************************************************************************************************************************
	public Location theirDiagonal(int[][] board, int r, int c, int n) {															// Diagonal Opponent
		int chain = 0;
		for (int i = 0; i < n; i++) {
			if (isTheirs(board, r + i, c + i)) {
				chain++;
			}
		}

		if (chain == n && isMine(board, r - 1, c - 1) &&
				isBlank(board, r + n, c + n)) {
			for (int i = 0; i < n; i++) {
				if (eatTheirsAtPoint(board, r + i, c + i) != null) {
					return eatTheirsAtPoint(board, r + i, c + i);
				}
			}
			return new Location(r + n, c + n);
		} else if (chain == n && isBlank(board, r - 1, c - 1) &&
				isMine(board, r + n, c + n)) {
			for (int i = 0; i < n; i++) {
				if (eatTheirsAtPoint(board, r + i, c + i) != null) {
					return eatTheirsAtPoint(board, r + i, c + i);
				}
			}
			return new Location(r - 1, c - 1);
		}

		chain = 0;
		for (int i = 0; i < n; i++) {
			if (isTheirs(board, r - i, c + i)) {
				chain++;
			}
		}
		if (chain == n && isBlank(board, r - n, c + n)
				&& isMine(board, r + 1, c - 1)) {
			for (int i = 0; i < n; i++) {
				if (eatTheirsAtPoint(board, r - i, c + i) != null) {
					return eatTheirsAtPoint(board, r - i, c + i);
				}
			}
			return new Location(r - n, c + n);
		} else if (chain == n && isBlank(board, r + 1, c - 1)
				&& isMine(board, r - n, c + n)) {
			for (int i = 0; i < n; i++) {
				if (eatTheirsAtPoint(board, r - i, c + i) != null) {
					return eatTheirsAtPoint(board, r - i, c + i);
				}
			}
			return new Location(r + 1, c - 1);
		}

		return null;
	}
	
	public Location theirDiagonalAlone(int[][] board, int r, int c, int n) {															
		int chain = 0;
		for (int i = 0; i < n; i++) {
			if (isTheirs(board, r + i, c + i)) {
				chain++;
			}
		}

		if (chain == n && isBlank(board, r + n, c + n)
				&& isBlank(board, r - 1, c - 1)) {
			for (int i = 0; i < n; i++) {
				if(eatTheirsAtPoint(board, r + i, c + i) != null) {
					System.out.println("They had " + n + " in a diagonal, but instead we are going to capture!");
					return eatTheirsAtPoint(board, r + i, c + i);
				}
			}
			if(checkIfCanBeTakenIfPlacedHere(board, r - 1, c- 1)) {
				if(checkIfCanBeTakenIfPlacedHere(board, r + n, c + n)) {
					System.out.println("Have to put here or will lose game");
					return new Location(r - 1, c - 1);
				} else {
					System.out.println("Choosing different spot for 3-in-a-row");
					return new Location(r + n, c + n);
				}
			}
			System.out.println("None is going to be taken in this 3 in a row!");
			return new Location(r - 1, c - 1);
		}

		chain = 0;
		for (int i = 0; i < n; i++) {
			if (isTheirs(board, r - i, c + i)) {
				chain++;
			}
		}
		if (chain == n && isBlank(board, r - n, c + n) &&
				isBlank(board, r + 1, c - 1)) {
			for (int i = 0; i < n; i++) {
				if(eatTheirsAtPoint(board, r - i, c + i) != null) {
					System.out.println("They had " + n + " in a diagonal, but instead we are going to capture!");
					return eatTheirsAtPoint(board, r - i, c + i);
				}
			}
			if(checkIfCanBeTakenIfPlacedHere(board, r + 1, c - 1)) {
				if(checkIfCanBeTakenIfPlacedHere(board, r - n, c + n)) {
					return null; // return new Location(r + 1, c - 1);
				} else {
					System.out.println("Choosing different spot for 3-in-a-row");
					return new Location(r - n, c + n);
				}
			}
			return new Location(r + 1, c - 1);
		}

		
		return null;
	}
	
	public Location theirDiagonalFour(int[][] board, int r, int c) {														// Diagonal Self
		int chain = 0;
		int empty = 0;
		int emptyR = 0;
		int emptyC = 0;
		for (int i = 0; i < 5; i++) {
			if (isTheirs(board, r + i, c + i)) {
				chain++;
			} else if(isBlank(board, r + i, c + i)) {
				empty++;
				emptyR = r + i;
				emptyC = c + i;
			}
		}
		if (chain == 4 && empty == 1 && isInBounds(board, emptyR, emptyC)) {
			for (int i = 0; i < 5; i++) {
				if(eatTheirsAtPoint(board, r + i, c + i) != null) {
					System.out.println("They had 4 stones and 1 empty in a diagonal, but instead we are going to capture! " + i);
					return eatTheirsAtPoint(board, r + i, c + i);
				}
			}
			System.out.println("They may win diagonally");
			return new Location(emptyR, emptyC);
		} 
		
		if(isBlank(board, r, c) && isTheirs(board, r + 1, c + 1) && isTheirs(board, r + 2, c + 2) &&
				isBlank(board, r + 3, c + 3) && isTheirs(board, r + 4, c + 4) && isBlank(board, r + 5, c + 5)) {	
			for(int i = 0; i < 6; i++) {
				if(isTheirs(board, r + i, c + i) && eatTheirsAtPoint(board, r + i, c + i) != null) {
					System.out.println("Ate theirs at diagonal at:" + (r + i) + ", " + (c + i) );
					eatTheirsAtPoint(board, r + i, c + i);
				}
			}
			return new Location(r + 3, c + 3);
		}
		if(isBlank(board, r, c) && isTheirs(board, r + 1, c + 1) && isBlank(board, r + 2, c + 2) &&
				isTheirs(board, r + 3, c + 3) && isTheirs(board, r + 4, c + 4) && isBlank(board, r + 5, c + 5)) {
			for(int i = 0; i < 6; i++) {
				if(isTheirs(board, r + i, c + i) && eatTheirsAtPoint(board, r + i, c + i) != null) {
					System.out.println("Ate theirs at diagonal at:" + (r + i) + ", " + (c + i) );
					eatTheirsAtPoint(board, r + i, c + i);
				}
			}
			return new Location(r + 2, c + 2);
		}
		
		chain = 0;
		empty = 0;
		emptyR = 0;
		emptyC = 0;
		for (int i = 0; i < 5; i++) {
			if (isTheirs(board, r - i, c + i)) {
				chain++;
			} else if(isBlank(board, r - i, c + i)) {
				empty++;
				emptyR = r - i;
				emptyC = c + i;
			} 
		}
		if (chain == 4 && empty == 1 && isInBounds(board, emptyR, emptyC)) {
			for (int i = 0; i < 5; i++) {
				if(eatTheirsAtPoint(board, r - i, c + i) != null) {
					System.out.println("They had 4 stones and 1 empty in a diagonal, but instead we are going to capture!" + i);
					return eatTheirsAtPoint(board, r - i, c + i);
				}
			}
			System.out.println("They may win diagonally");
			return new Location(emptyR, emptyC);
		} 
		
		if(isBlank(board, r, c) && isTheirs(board, r - 1, c + 1) && isTheirs(board, r - 2, c + 2) &&
				isBlank(board, r - 3, c + 3) && isTheirs(board, r - 4, c + 4) && isBlank(board, r - 5, c + 5)) {
			for(int i = 0; i < 6; i++) {
				if(isTheirs(board, r - i, c + i) && eatTheirsAtPoint(board, r - i, c + i) != null) {
					eatTheirsAtPoint(board, r - i, c + i);
				}
			}
			System.out.println("check 3");
			return new Location(r - 3, c + 3);
		}
		if(isBlank(board, r, c) && isTheirs(board, r - 1, c + 1) && isBlank(board, r - 2, c + 2) &&
				isTheirs(board, r - 3, c + 3) && isTheirs(board, r - 4, c + 4) && isBlank(board, r - 5, c + 5)) {
			for(int i = 0; i < 6; i++) {
				if(isTheirs(board, r - i, c + i) && eatTheirsAtPoint(board, r - i, c + i) != null) {
					eatTheirsAtPoint(board, r - i, c + i);
				}
			}
			System.out.println("check 4");
			return new Location(r - 2, c + 2);
		}
		
	
		
		return null;
	}

	// *****************************************************************************************************************************************
	public Location myHorizontal(int[][] board, int r, int c, int n) {												// Horizontal Self
		int chain = 0;
		for (int i = 0; i < n; i++) {
			if (isMine(board, r, c + i)) {
				chain++;
			}
		}

		if (chain == n && isBlank(board, r, c + n) && isTheirs(board, r, c - 1)) {
			return new Location(r, c + n);
		} else if (chain == n && isBlank(board, r, c - 1) && isTheirs(board, r, c + n)) {
			return new Location(r, c - 1);
		}
		return null;
	}

	public Location myHorizontalAlone(int[][] board, int r, int c, int n) {
		int chain = 0;
		for (int i = 0; i < n; i++) {
			if (isInBounds(board, r, c + i) && board[r][c + i] == getID()) {
				chain++;
			}
		}

		if (chain == n && isBlank(board, r, c + n) && isBlank(board, r, c - 1)) {
			if(checkIfCanBeTakenIfPlacedHere(board, r, c - 1)) {
				if(checkIfCanBeTakenIfPlacedHere(board, r, c + n)) {
					return new Location(r, c - 1);
				} else {
					return new Location(r, c + n);
				}
			}
			return new Location(r, c - 1);
		}
		return null;
	}
	
	public Location myHorizontalFour(int[][] board, int r, int c) {												
		int chain = 0;
		int empty = 0;
		int emptyR = 0;
		int emptyC = 0;
		for (int i = 0; i < 5; i++) {
			if (isMine(board, r, c + i)) {
				chain++;
			} else if(isBlank(board, r, c + i)) {
				empty++;
				emptyR = r;
				emptyC = c + i;
			}
		}

		if (chain == 4 && empty == 1 && isInBounds(board, emptyR, emptyC)) {
			return new Location(emptyR, emptyC);
		}
		
		return null;
	}

	// *****************************************************************************************************************************************

	public Location theirHorizontal(int[][] board, int r, int c, int n) {													// Horizontal Opponent
		int chain = 0;
		for (int i = 0; i < n; i++) {
			if (isTheirs(board, r, c + i)) {
				chain++;
			}
		}

		if (chain == n && isBlank(board, r, c + n) && isMine(board, r, c- 1)) {
			for (int i = 0; i < n; i++) {
				if (eatTheirsAtPoint(board, r, c + i) != null) {
					return eatTheirsAtPoint(board, r, c + i);
				}
			}
			return new Location(r, c + n);
		} else if (chain == n && isBlank(board, r, c - 1) && isMine(board, r, c + n)) {
			for (int i = 0; i < n; i++) {
				if (eatTheirsAtPoint(board, r, c + i) != null) {
					return eatTheirsAtPoint(board, r, c + i);
				}
			}
			return new Location(r, c - 1);
		}

		return null;
	}
	
	public Location theirHorizontalAlone(int[][] board, int r, int c, int n) {												
		int chain = 0;
		for (int i = 0; i < n; i++) {
			if (isTheirs(board, r, c + i)) {
				chain++;
			}
		}

		if (chain == n && isBlank(board, r, c + n)&& isBlank(board, r, c - 1)) {
			for (int i = 0; i < n; i++) {
				if(eatTheirsAtPoint(board, r, c + i) != null) {
					System.out.println("They had " + n + " in a row, but instead we are going to capture!");
					return eatTheirsAtPoint(board, r, c + i);
				}
			}
			if(checkIfCanBeTakenIfPlacedHere(board, r, c - 1)) {
				if(checkIfCanBeTakenIfPlacedHere(board, r, c + n)) {
					return null;
				} else {
					System.out.println("Choosing different spot for 3-in-a-row");
					return new Location(r, c + n);
				}
			}
			return new Location(r, c - 1);
		}

		return null;
	}
	
	public Location theirHorizontalFour(int[][] board, int r, int c) {												
		int chain = 0;
		int empty = 0;
		int emptyR = 0;
		int emptyC = 0;
		for (int i = 0; i < 5; i++) {
			if (isTheirs(board, r, c + i)) {
				chain++;
			} else if(isBlank(board, r, c + i)) {
				empty++;
				emptyR = r;
				emptyC = c + i;
			}
		}

		if (chain == 4 && empty == 1 && isInBounds(board, emptyR, emptyC)) {
			for (int i = 0; i < 5; i++) {
				if(eatTheirsAtPoint(board, r, c + i) != null) {
					System.out.println("They had 4 stones and 1 empty in a row, but instead we are going to capture!" + i);
					return eatTheirsAtPoint(board, r, c + i);
				}
			}
			System.out.println("They may win horizontally");
			return new Location(emptyR, emptyC);
		}
		
		if(isBlank(board, r, c) && isTheirs(board, r, c + 1) && isTheirs(board, r, c + 2) &&
			isBlank(board, r, c + 3) && isTheirs(board, r, c + 4) && isBlank(board, r, c + 5)) {
			for(int i = 0; i < 6; i++) {
				if(isTheirs(board, r, c + i) && eatTheirsAtPoint(board, r, c + i) != null) {
					System.out.println("Ate theirs at horizontal");
					eatTheirsAtPoint(board, r, c + i);
				}
			}
			if(checkIfCanBeTakenIfPlacedHere(board, r, c + 3)) {
				if(checkIfCanBeTakenIfPlacedHere(board, r, c)) {
					return new Location(r, c + 3);
				}
				return new Location(r, c);
			}
			return new Location(r, c + 3);
		}
		if(isBlank(board, r, c) && isTheirs(board, r, c + 1) && isBlank(board, r, c + 2) &&
			isTheirs(board, r, c + 3) && isTheirs(board, r, c + 4) && isBlank(board, r, c + 5)) {
			for(int i = 0; i < 6; i++) {
				if(isTheirs(board, r, c + i) && eatTheirsAtPoint(board, r, c + i) != null) {
					System.out.println("Ate theirs at horizontal");
					eatTheirsAtPoint(board, r, c + i);
				}
			}
			if(checkIfCanBeTakenIfPlacedHere(board, r, c + 2)) {
				if(checkIfCanBeTakenIfPlacedHere(board, r, c + 5)) {
					return new Location(r, c + 2);
				}
				return new Location(r, c + 5);
			}
			return new Location(r, c + 2);
		}
		return null;
	}

	// *****************************************************************************************************************************************
	public Location myVertical(int[][] board, int r, int c, int n) {														// Vertical Self			
		int chain = 0;
		for (int i = 0; i < n; i++) {
			if (isMine(board, r + i, c)) {
				chain++;
			}
		}

		if (chain == n && isBlank(board, r + n, c) && isTheirs(board, r - 1, c)) {
			return new Location(r + n, c);
		} else if (chain == n && isBlank(board, r - 1, c) && isTheirs(board, r + n, c)) {
			return new Location(r - 1, c);
		}

		return null;
	}

	public Location myVerticalAlone(int[][] board, int r, int c, int n) {
		int chain = 0;
		for (int i = 0; i < n; i++) {
			if (isMine(board, r + i, c)) {
				chain++;
			}
		}

		if (chain == n && isBlank(board, r + n, c) && isBlank(board, r - 1, c)) {
			return new Location(r - 1, c);
		}
		return null;
	}
	
	public Location myVerticalFour(int[][] board, int r, int c) {																
		int chain = 0;
		int empty = 0;
		int emptyR = 0;
		int emptyC = 0;
	
		for (int i = 0; i < 5; i++) {
			if (isMine(board, r + i, c)) {
				chain++;
			} else if(isBlank(board, r + i, c)) {
				empty++;
				emptyR = r + i;
				emptyC = c;
			}
		}

		if (chain == 4 && empty == 1 && isInBounds(board, emptyR, emptyC)) {
			return new Location(emptyR, emptyC);
		}
 

		return null;
	}

	// *****************************************************************************************************************************************

	public Location theirVertical(int[][] board, int r, int c, int n) {														// Vertical Opponenet
		int chain = 0;
		for (int i = 0; i < n; i++) {
			if (isTheirs(board, r + i, c)) {
				chain++;
			}
		}

		if (chain == n && isBlank(board, r + n, c) &&
				isMine(board, r - 1, c)) {
			for (int i = 0; i < n; i++) {
				if (eatTheirsAtPoint(board, r + i, c) != null) {
					return eatTheirsAtPoint(board, r + i, c);
				}
			}
			return new Location(r + n, c);
		} else if (chain == n && isBlank(board, r - 1, c) && isMine(board, r + n, c) ) {
			for (int i = 0; i < n; i++) {
				if (eatTheirsAtPoint(board, r + i, c) != null) {
					return eatTheirsAtPoint(board, r + i, c);
				}
			}
			return new Location(r - 1, c);
		}

		return null;
	}
	
	public Location theirVerticalAlone(int[][] board, int r, int c, int n) {														// Vertical Opponenet
		int chain = 0;
		boolean canCapture = false;
		for (int i = 0; i < n; i++) {
			if (isTheirs(board, r + i, c)) {
				chain++;
			}
		}
		
		if (chain == n && isBlank(board, r + n, c) && isBlank(board, r - 1, c)) {
			for (int i = 0; i < n; i++) {
				if(eatTheirsAtPoint(board, r + i, c) != null) {
					System.out.println("They had " + n + " in a column, but instead we are going to capture!" + i);
					return eatTheirsAtPoint(board, r + i, c);
				}
			}
			if(checkIfCanBeTakenIfPlacedHere(board, r - 1, c)) {
				if(checkIfCanBeTakenIfPlacedHere(board, r + n, c)) {
					return null;
				} else {
					System.out.println("Choosing different spot for 3-in-a-row");
					return new Location(r + n, c);
				}
			}
			return new Location(r - 1, c);
		}

		return null;
	}
	
	public Location theirVerticalFour(int[][] board, int r, int c) {																
		int chain = 0;
		int empty = 0;
		int emptyR = 0;
		int emptyC = 0;
		for (int i = 0; i < 5; i++) {
			if (isTheirs(board, r + i, c) ) {
				chain++;
			} else if(isBlank(board, r + i, c)) {
				empty++;
				emptyR = r + i;
				emptyC = c;
			}
		}

		if (chain == 4 && empty == 1 && isInBounds(board, emptyR, emptyC)) {
			for (int i = 0; i < 5; i++) {
				if(eatTheirsAtPoint(board, r + i, c) != null) {
					System.out.println("They had 4 stones and 1 empty in a column, but instead we are going to capture!" + i);
					return eatTheirsAtPoint(board, r + i, c);
				}
			}
			System.out.println("They may win vertically");
			return new Location(emptyR, emptyC);
		}
		
		if(isBlank(board, r, c) && isTheirs(board, r + 1, c) && isTheirs(board, r + 2, c) &&
			isBlank(board, r + 3, c) && isTheirs(board, r + 4, c) && isBlank(board, r + 5, c)) {
			for(int i = 0; i < 6; i++) {
				if(isTheirs(board, r + i, c) && eatTheirsAtPoint(board, r + i, c) != null) {
					System.out.println("Ate theirs at vertical" + i);
					eatTheirsAtPoint(board, r + i, c);
				}
			}
			if(checkIfCanBeTakenIfPlacedHere(board, r + 3, c)) {
				if(checkIfCanBeTakenIfPlacedHere(board, r, c)) {
					return new Location(r + 3, c);
				}
				return new Location(r, c);
			}
			return new Location(r + 3, c);
		} 
		if(isBlank(board, r, c) && isTheirs(board, r + 1, c) && isBlank(board, r + 2, c) &&
				isTheirs(board, r + 3, c) && isTheirs(board, r + 4, c) && isBlank(board, r + 5, c)) {
				for(int i = 0; i < 6; i++) {
					if(isTheirs(board, r + i, c) && eatTheirsAtPoint(board, r + i, c) != null) {
						System.out.println("Ate theirs at vertical" + i);
						eatTheirsAtPoint(board, r + i, c);
					}
				}
				if(checkIfCanBeTakenIfPlacedHere(board, r + 2, c)) {
					if(checkIfCanBeTakenIfPlacedHere(board, r + 5, c)) {
						return new Location(r + 2, c);
					}
					return new Location(r + 5, c);
				}
				return new Location(r + 2, c);
		} 
		return null;
	} 

	// *****************************************************************************************************************************************
	public Location canWin(int[][] board) {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (myDiagonal(board, row, col, 4) != null) {
					return myDiagonal(board, row, col, 4);
				}
				
				if (myVertical(board, row, col, 4) != null) {
					return myVertical(board, row, col, 4);
				}
	
				if (myHorizontal(board, row, col, 4) != null) {
					return myHorizontal(board, row, col, 4);
				}
				
		
				if (myDiagonalFour(board, row, col) != null) {
					return myDiagonalFour(board, row, col);
				}
		
				if (myVerticalFour(board, row, col) != null) {
					return myVerticalFour(board, row, col);
				}
			
				if (myHorizontalFour(board, row, col) != null) {
					return myHorizontalFour(board, row, col);
				}
			}
		}
		return null;
	}
	
	public Location tryWin(int[][] board) {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
		
				// Need to do diagonal
		
				if(isBlank(board, row, row) && isMine(board, row + 1, row) && isMine(board, row + 2, row) &&
					isBlank(board, row + 3, row) && isMine(board, row + 4, row) && isBlank(board, row + 5, row)) {
					return new Location(row + 3, row);
				} 
				if(isBlank(board, row, row) && isMine(board, row + 1, row) && isBlank(board, row + 2, row) &&
					isMine(board, row + 3, row) && isMine(board, row + 4, row) && isBlank(board, row + 5, row)) {
					return new Location(row + 2, row);
				}
				if(isBlank(board, row, row) && isMine(board, row, row + 1) && isMine(board, row, row + 2) &&
					isBlank(board, row, row + 3) && isMine(board, row, row + 4) && isBlank(board, row, row + 5)) {
					return new Location(row, row + 3);
				}
				if(isBlank(board, row, row) && isMine(board, row, row + 1) && isBlank(board, row, row + 2) &&
					isMine(board, row, row + 3) && isMine(board, row, row + 4) && isBlank(board, row, row + 5)) {
					return new Location(row, row + 2);
				}
			}
		}
		return null;
	}


	public Location canLose(int[][] board) {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {

				if (theirDiagonal(board, row, col, 4) != null) {
					return theirDiagonal(board, row, col, 4);
				}

				if (theirVertical(board, row, col, 4) != null) {
					return theirVertical(board, row, col, 4);
				}
		
				if (theirHorizontal(board, row, col, 4) != null) {
					return theirHorizontal(board, row, col, 4);
				}
				
			}
		}
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
				if (theirDiagonalFour(board, row, col) != null) {
					return theirDiagonalFour(board, row, col);
				}
			
				if (theirVerticalFour(board, row, col) != null) {
					return theirVerticalFour(board, row, col);
				}
			
				if (theirHorizontalFour(board, row, col) != null) {
					return theirHorizontalFour(board, row, col);
				}
			}
		}
		return null;
	}


	public Location theirBlocked(int[][] board, int n) {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
		
				if (theirDiagonal(board, row, col, n) != null) {
					if(n == 3) {
						Location loc = theirDiagonal(board, row, col, 3);
						if(checkIfCanBeTakenIfPlacedHere(board, loc.getRow(), loc.getCol())) {
							return null;
						}
					}
					return theirDiagonal(board, row, col, n);
				}

				if (theirVertical(board, row, col, n) != null) {
					if(n == 3) {
						Location loc = theirVertical(board, row, col, 3);
						if(checkIfCanBeTakenIfPlacedHere(board, loc.getRow(), loc.getCol())) {
							return null;
						}
					}
					return theirVertical(board, row, col, n);
				}
			
				if (theirHorizontal(board, row, col, n) != null) {
					if(n == 3) {
						Location loc = theirHorizontal(board, row, col, 3);
						if(checkIfCanBeTakenIfPlacedHere(board, loc.getRow(), loc.getCol())) {
							return null;
						}
					}
					return theirHorizontal(board, row, col, n);
				}
			}
		}
		return null;
	}
	
	public Location theirUnblocked(int[][] board, int n) {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
		
				if (theirDiagonalAlone(board, row, col, n) != null) {
					if(n == 2 && checkIfCanBeTakenIfPlacedHere(board, row, col)) {
						return null;
					} 
					return theirDiagonalAlone(board, row, col, n);
				}
				
				if (theirVerticalAlone(board, row, col, n) != null) {
					if(n == 2 && checkIfCanBeTakenIfPlacedHere(board, row, col)) {
						return null;
					}
					return theirVerticalAlone(board, row, col, n);
				}
	
				if (theirHorizontalAlone(board, row, col, n) != null) {
					if(n == 2 && checkIfCanBeTakenIfPlacedHere(board, row, col)) {
						return null;
					}
					return theirHorizontalAlone(board, row, col, n);
				}
			}
		}
		return null;
	}
	
	public Location myUnblocked(int[][] board, int n) {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {
		
				if (myDiagonalAlone(board, row, col, n) != null) {
					return myDiagonalAlone(board, row, col, n);
				}
				
				if (myVerticalAlone(board, row, col, n) != null) {
					return myVerticalAlone(board, row, col, n);
				}
	
				if (myHorizontalAlone(board, row, col, n) != null) {
					return myHorizontalAlone(board, row, col, n);
				}
			}
		}
		return null;
	}


	public Location myTwo(int[][] board) {
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[0].length; col++) {

				if (myDiagonal(board, row, col, 2) != null) {
					
					return myDiagonal(board, row, col, 2);
				}
		
				if (myVertical(board, row, col, 2) != null) {
					return myVertical(board, row, col, 2);
				}
		
				if (myHorizontal(board, row, col, 2) != null) {
					return myHorizontal(board, row, col, 2);
				}
			}
		}
		return null;
	}


	public Location theirTwo(int[][] board) {
		
		if(theirBlocked(board, 2) != null) {
			System.out.println("Their two blocked found!");
			return theirBlocked(board, 2);
		}
				
		if(theirUnblocked(board, 2) != null) {							// Check here if placed here, then it could be taken
			System.out.println("Their two unblocked found!");
			return theirUnblocked(board, 2);
		}
		
		return null;
	}
	

	
	public Location smartMove(int[][] board) {
		boolean done = false;
		int counter = 0;

	
		while (!done) {
			int row = (int) (Math.random() * board.length);
			int col = (int) (Math.random() * board[0].length);

			ArrayList<Location> list = new ArrayList<Location>();

			if (board[row][col] == 0) {
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						if (!(i == 0 && j == 0)
								&& isInBounds(board, row + i, col + j)
								&& board[row + i][col + j] > 0) {
							if (board[row + i][col + j] == getID())
								list.add(new Location(row + i, col + j));
						}
					}
				}
			}

			if (list.size() > 0) {
				Location loc = new Location(row, col);
				System.out.println("Checking for a possible position.....");
				if(!checkIfCanBeTakenIfPlacedHere(board, row, col)) {
					System.out.println("This place is okay!");
					return loc;
				} 
				System.out.println("Nope.  This place will help your opponent eat yours.");
				System.out.println("Checking for another spot...");
			}

			if (counter++ > 400)
				done = true;
		}

		done = false;


		while (!done) {
			int row = (int) (Math.random() * board.length);
			int col = (int) (Math.random() * board[0].length);

			if (board[row][col] == 0 && !checkIfCanBeTakenIfPlacedHere(board, row, col)) {
				System.out.println("Looking for random spot....");
				return new Location(row, col);
			}

		}
		return null;
	}
	
	public boolean checkIfCanBeTakenIfPlacedHere(int[][] board, int r, int c) {  // __+*  but what if it is +_*
		if(isBlank(board, r, c)) {
			// vertical
			if(isMine(board, r + 1, c) && 
					isTheirs(board, r + 2, c) &&
					isBlank(board, r - 1, c) ||									// MAY WANT TO ERASE THIS
					isMine(board, r - 1, c) && 
					isTheirs(board, r - 2, c) &&
					isBlank(board, r + 1, c) ||
					isTheirs(board, r - 1, c)  &&
					isMine(board, r + 1, c)  &&
					isBlank(board, r + 2, c) ||
					isTheirs(board, r + 1, c) &&
					isMine(board, r - 1, c) &&
					isBlank(board, r - 2, c)) {
					return true;
			}
			
			//horizontal
			if(isMine(board, r, c + 1) && 
					isTheirs(board, r, c + 2) &&
					isBlank(board, r, c - 1) ||									// MAY WANT TO ERASE THIS
					isMine(board, r, c - 1)&& 
					isTheirs(board, r, c - 2) &&
					isBlank(board, r, c + 1) ||
					isTheirs(board, r, c + 1) &&
					isMine(board, r, c - 1)&&
					isBlank(board, r, c - 2)  ||
					isTheirs(board, r, c - 1) &&
					isMine(board, r, c + 1)  &&
					isBlank(board, r, c + 2)) {
					return true;
			}
			
			// diagonal
			if(isMine(board, r - 1, c + 1) && 
					isTheirs(board, r - 2, c + 2) &&
					isBlank(board, r + 1, c - 1) ||									// MAY WANT TO ERASE THIS
					isMine(board, r - 1, c - 1) && 
					isTheirs(board, r - 2, c - 2) &&
					isBlank(board, r + 1, c + 1) ||
					isMine(board, r + 1, c - 1) && 
					isTheirs(board, r + 2, c - 2) &&
					isBlank(board, r - 1, c + 1) ||
					isMine(board, r + 1, c + 1) && 
					isTheirs(board, r + 2, c + 2)&&
					isBlank(board, r - 1, c - 1) ||
					
					isTheirs(board, r - 1, c + 1) &&
					isMine(board, r + 1, c - 1) &&
					isBlank(board, r + 2, c - 2) ||
					isTheirs(board, r + 1, c + 1) &&
					isMine(board, r - 1, c - 1) &&
					isBlank(board, r - 2, c - 2) ||
					isTheirs(board, r + 1, c - 1) &&
					isMine(board, r - 1, c + 1) &&
					isBlank(board, r - 2, c + 2) ||
					isTheirs(board, r - 1, c - 1) &&
					isMine(board, r + 1, c + 1) &&
					isBlank(board, r + 2, c + 2)) {
					return true;
			}
			
		}
		return false;
	}

	public Location eatTheirsAtPoint(int[][] board, int r, int c) {
		//Diagonal
		if(isTheirs(board, r, c) && isTheirs(board, r - 1, c - 1)) {
			if(isBlank(board, r + 1, c + 1) &&  isMine(board, r - 2, c - 2)) {
				System.out.println("(1)");
				return new Location(r + 1, c + 1);
			} 
			if(isBlank(board, r - 2, c - 2) && isMine(board, r + 1, c + 1)) {
				System.out.println("(2)");
				return new Location(r - 2, c - 2);
			}
		}
		if(isTheirs(board, r, c) && isTheirs(board, r + 1, c + 1)) {
			if(isBlank(board, r + 2, c + 2) && isMine(board, r - 1, c - 1)) {
				System.out.println("(3)");
				return new Location(r + 2, c + 2);
			}
			if(isMine(board, r + 2, c + 2) && isBlank(board, r - 1, c - 1)) {
				System.out.println("(4)");
				return new Location(r - 1, c - 1);
			}
		}
		if(isTheirs(board, r, c) && isTheirs(board, r - 1, c + 1)) {
			if(isBlank(board, r - 2, c + 2) && isMine(board, r + 1, c - 1)) {
				System.out.println("(5)");
				return new Location(r - 2, c+ 2);
			}
			if(isMine(board, r - 2, c + 2) && isBlank(board, r + 1, c - 1)) {
				System.out.println("(6)");
				return new Location(r + 1, c - 1);
			}
		}
		if(isTheirs(board, r, c) && isTheirs(board, r + 1, c - 1)) {
			if(isBlank(board, r + 2, c - 2) && isMine(board, r - 1, c + 1)) {
				System.out.println("(7)");
				return new Location(r + 2, c - 2);
			}
			if(isMine(board, r + 2, c - 2) && isBlank(board, r - 1, c + 1)) {
				System.out.println("(8)");
				return new Location(r - 1, c + 1);
			}
		}
		
		//Horizontal
		if(isTheirs(board, r, c) && isTheirs(board, r, c + 1)) {
			if(isBlank(board, r, c - 1) && isMine(board, r, c + 2)) {
				System.out.println("(9)");
				return new Location(r, c - 1);
			}
			if(isMine(board, r, c - 1) && isBlank(board, r, c + 2)) {
				System.out.println("(10)");
				return new Location(r, c + 2);
			}
		}
		if(isTheirs(board, r, c) && isTheirs(board, r, c - 1)) {
			if(isBlank(board, r, c + 1) && isMine(board, r, c - 2)) {
				System.out.println("(11)");
				return new Location(r, c + 1);
			}
			if(isMine(board, r, c + 1) && isBlank(board, r, c - 2)) {
				System.out.println("(12)");
				return new Location(r, c - 2);
			}
		}
		
		//Vertical
		if(isTheirs(board, r, c) && isTheirs(board, r + 1, c)) {
			if(isBlank(board, r - 1, c) && isMine(board, r + 2, c)) {
				System.out.println("(13)");
				return new Location(r - 1, c);
			}
			if(isMine(board, r - 1, c) && isBlank(board, r + 2, c)) {
				System.out.println("(14)");
				return new Location(r + 2, c);
			}
		}
		if(isTheirs(board, r, c) && isTheirs(board, r - 1, c)) {
			if(isBlank(board, r + 1, c) && isMine(board, r - 2, c)) {
				System.out.println("(15)");
				return new Location(r + 1, c);
			}
			if(isMine(board, r + 1, c) && isBlank(board, r - 2, c)) {
				System.out.println("(16)");
				return new Location(r - 2, c);
			}
		}
		return null;
	}
}
