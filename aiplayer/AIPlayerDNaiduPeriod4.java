package aiplayer;
import pente.*;

import java.util.*;
/*****************************************************************************
 * 
 * @author Dharma Naidu  Time : 8 ish hours total
 * This revision was spent mostly implementing recursion.  I spent a bit of 
 * time learning about it, and a lot of time trying to find out a better way
 * to rank boards.  I coded a ranking system made of a dummy board that held
 * values for each row/possible capture.  If I could improve on one thing, 
 * it would definitely be the ranking system / the way to filter possible 
 * "locations".  Overall, this was a very fun lab.
 ****************************************************************************/
public class AIPlayerDNaiduPeriod4 extends AIPlayer {

	// Change these to match your AIPlayer info
	private static String name = "Dank Memes";
	private static String iconFile = "breachblue.png";
	Random rando = new Random();
	int oppID;
	int attempt = 0;
	int count;
	
	public AIPlayerDNaiduPeriod4(int id) {
		super(iconFile, name, id);
	}

	/****
	 * WEIGHTING SYSTEM OF COPY ARRAY
	 * If win possible : 100000
	 * if stop win possible : 90000
	 * if we got nothing : spots ranked by closest to center
	 */
	@Override
	public Location makeMove(int[][] idArray, int moveCount) {
		count = moveCount;
		if(moveCount == 2 || moveCount == 3) {
			oppID = findID(idArray);
		}
		if(moveCount == 1) {
			return new Location(idArray.length / 2, idArray[0].length / 2);
		}
		if(moveCount == 2) {
			return new Location(idArray.length / 2, idArray[0].length / 2 - 1);
		}
		if(moveCount == 3) {
			return new Location(12, 12);
		}
		else {
			for(int row = 0; row < idArray.length; row++) {
				for(int col = 0; col < idArray[0].length; col++) {
					if(idArray[row][col] == 0) {
						if(longestRow(true, idArray, row, col) >= 4) { // WIN CONDITION
							return new Location(row, col);
						}
					}
				}
			}
			int[] move = minimax(2, idArray, getID());
			return new Location(move[1], move[2]);
		}

		
	}
	private int[] minimax(int depth, int[][] idArray,int id) {
		// Generate possible next moves in a List of int[2] of {row, col}.
		List<Location> nextMoves = generateMoves(idArray);
		// mySeed is maximizing; while oppSeed is minimizing
		int bestScore;
		if(id == getID())
			 bestScore = Integer.MIN_VALUE + 1;
		else
			bestScore = Integer.MAX_VALUE - 1;
        int currentScore;
        int bestRow = -1;
        int bestCol = -1;
 
        if(nextMoves.isEmpty() || depth == 0) {
        	// Gameover or depth reached, evaluate score
        	bestScore = evaluate(idArray);
        }
        else {
        	for (Location move : nextMoves) {
        		// Try this move for the current "player"
        		idArray[move.getRow()][move.getCol()] = id;
        		if (id == getID()) {
        			// mySeed (computer) is maximizing player
        			currentScore = minimax(depth - 1, idArray, oppID)[0];
        			if(currentScore > bestScore) {
        				bestScore = currentScore;
	                    bestRow = move.getRow();
	                    bestCol = move.getCol();
        			}
        		}
        		else {  // oppSeed is minimizing player
        			currentScore = minimax(depth - 1, idArray, id)[0];
                    if(currentScore < bestScore) {
	                    bestScore = currentScore;
	                    bestRow = move.getRow();
	                    bestCol = move.getCol();
                    }
        		}
        		// Undo move
        		idArray[move.getRow()][move.getCol()] = 0;
        	}
        }
        return new int[] {bestScore, bestRow, bestCol};
   }
   private ArrayList<Location> generateMoves(int[][] idArray) {
	   ArrayList<Location> list = new ArrayList<Location>();
	   int[][] copy = new int[idArray.length][idArray[0].length];	
		copy = defaultCopy(idArray, copy);
		for(int row = 0; row < idArray.length; row++) {
			for(int col = 0; col < idArray[0].length; col++) {
				if(idArray[row][col] == 0) {
					if(longestRow(true, idArray, row, col) >= 4) { // WIN CONDITION
						list.add(new Location(row, col));
					}
					else if(longestRow(false, idArray, row, col) >= 4) { // LOSE CONDITION
						copy[row][col] += 2000;
					}
					else if(longestRow(true, idArray,row, col) == 3) {
						copy[row][col] += 900;
					}
					else if(longestRow(false, idArray,row, col) == 3) {
						copy[row][col] += 700;
					}
					else {
						if(longestRow(true, idArray, row, col) != 0)
							copy[row][col] += longestRow(true, idArray, row, col) * 50;
					}	
					
					if(capturePossible(true, idArray, row, col)) { // WIN CONDITION
						if(myCaptures() == 8)
							list.add(new Location(row, col));
						copy[row][col] = copy[row][col] + 800;
					}
					if(capturePossible(false, idArray, row, col)) { // LOSE CONDITION
						if(opponentCaptures() == 8)
							copy[row][col] = copy[row][col] + 3000;
						copy[row][col] = copy[row][col] + 800;
					}
						
				}
			}
		}
		for(int row = 0; row < idArray.length; row++) {
			for(int col = 0; col < idArray[0].length; col++) {
				if(copy[row][col] >= 500 && idArray[row][col] == 0)
					list.add(new Location(row, col));
			}
		}
		if(list.size() < 5) {
			for(int row = 0; row < idArray.length; row++) {
				for(int col = 0; col < idArray[0].length; col++) {
					if(copy[row][col] >= 60 && idArray[row][col] == 0)
						list.add(new Location(row, col));
				}
			}
		}
	   return list;
   }
   
   
   
   private int evaluate(int[][] idArray) {
	   int score = 0;
	   for(int row = 0; row < idArray.length; row++) {
			for(int col = 0; col < idArray[0].length; col++) {
				if(captures(idArray, row, col, true)) { // WIN CONDITION
					if(myCaptures() == 8)
						return 100000;
					score += 4000;
				}
				if(captures(idArray, row, col, false)) { // LOSE CONDITION
					if(opponentCaptures() == 8)
						return  -100000;
					score -= 5000;
				}
				if(capturePossible(true, idArray, row, col)) {
					if(myCaptures() == 8)
						score += 5000;
					score += 300;
				}
				if(capturePossible(false, idArray, row, col)) {
					if(opponentCaptures() == 8)
						score -= 900;
					score -= 200;
				}
				if(idArray[row][col] == 0) {
					if(longestRow(true, idArray, row, col) == 5) { // WIN CONDITION
						return 100000;
					}
					else if(longestRow(true, idArray,row, col) == 4) {
						score += 5000;
					}
					else if(longestRow(true, idArray,row, col) == 3) {
						score += 2000;
					}
					else if(longestRow(true, idArray,row, col) == 2) {
						score += 600;
					}
					
					if(longestRow(false, idArray, row, col) == 5) { // LOSE CONDITION
						return -100000;
					}
					else if(longestRow(false, idArray,row, col) == 4) {
						score -= 6000;
					}
					else if(longestRow(false, idArray,row, col) == 3) {
						score -= 1100;
					}
					else if(longestRow(false, idArray,row, col) == 2) {
						score -= 500;
					}
				}
			}
	   }
	   return score;
   }
 
   private int findID(int[][] idArray) {
	   for(int row = 0; row < idArray.length; row++) {
			for(int col = 0; col < idArray[0].length; col++) {
				if(idArray[row][col] != 0 && idArray[row][col] != getID())
					return idArray[row][col];
			}
	   }
	   return 0;
   }
	/**
	 * determine if the 
	 * move makes a capture for other team
	 * if me : looking creates capture in favor for me
	 * @param idArray
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean captures(int[][] idArray, int row, int col, boolean me){
		int row2 = row;
		int col2 = col;
		
		for(int i = 1; i <= 8; i++) {
			row2 = row;
			col2 = col;
			if(me) {
				if(idArray[row][col] == getID()) {
					row2 = changeRow2(i, row2);
					col2 = changeCol2(i, col2);
					if(isInBounds(idArray, row2, col2) && idArray[row2][col2] != getID() && idArray[row2][col2] != 0) {
						row2 = changeRow2(i, row2);
						col2 = changeCol2(i, col2);
						if(isInBounds(idArray, row2, col2) && idArray[row2][col2] != getID() && idArray[row2][col2] != 0) {
							row2 = changeRow2(i, row2);
							col2 = changeCol2(i, col2);
							if(isInBounds(idArray, row2, col2) && idArray[row2][col2] == getID()) {
								return true;
							}
						}
					}	
				}
			}
			else {
				if(idArray[row][col] == oppID) {
					row2 = changeRow2(i, row2);
					col2 = changeCol2(i, col2);
					if(isInBounds(idArray, row2, col2) && idArray[row2][col2] == getID()) {
						row2 = changeRow2(i, row2);
						col2 = changeCol2(i, col2);
						if(isInBounds(idArray, row2, col2) && idArray[row2][col2] == getID()) {
							row2 = changeRow2(i, row2);
							col2 = changeCol2(i, col2);
							if(isInBounds(idArray, row2, col2) && idArray[row2][col2] == oppID) {
								return true;
							}
						}
					}	
				}
			}
		}
		return false;
	}
	private int findLargest(int[][] copy, int[][] idArray) {
		int biggest = copy[0][0];
		for(int row = 0; row < copy.length; row++) {
			for(int col = 0; col < copy[0].length; col++) {
				if(copy[row][col] > biggest && idArray[row][col] == 0)
					biggest = copy[row][col];
			}
		}
		return biggest;
	}
	/**
	 * If "me" is true, then looking for capture.  Otherwise, looking to protect
	**/
	private boolean capturePossible(boolean me, int[][] idArray, int row, int col) {
		int row2 = row;
		int col2 = col;
		
		for(int i = 1; i <=8; i++) {
			boolean capture = true;
			row2 = row;
			col2 = col;
			row2 = changeRow2(i, row2);
			col2 = changeCol2(i, col2);
			if(me == true) {
				for(int c = 0; c < 2; c++) {
					if(isInBounds(idArray, row2, col2) && idArray[row2][col2] != getID() && idArray[row2][col2] != 0) {
						row2 = changeRow2(i, row2);
						col2 = changeCol2(i, col2);
					}
					else {
						capture = false;
					}
				}
				if(capture == true) {
					if(isInBounds(idArray, row2, col2) && idArray[row2][col2] == getID())
						return true;
				}
					
			}
			else {
				for(int c = 0; c < 2; c++) {
					if(isInBounds(idArray, row2, col2) && idArray[row2][col2] == getID()) {
						row2 = changeRow2(i, row2);
						col2 = changeCol2(i, col2);
					}
					else {
						capture = false;
					}
				}
				if(isInBounds(idArray, row2, col2) && capture == true) {
					if(idArray[row2][col2] != getID() && idArray[row2][col2] != 0)
						return true;
				}
			}
			
		}
		return false;
	}
	
	
	private int[][] defaultCopy(int[][] idArray, int[][] copy) {
		int row2;
		int col2;
		for(int row = 0; row < copy.length; row++) {
			for(int col = 0; col < copy[0].length; col++) {
				
				copy[row][col] = 15 - (int)Math.sqrt(Math.pow(row - 9, 2) + Math.pow(col - 9, 2));
				if(idArray[row][col] == getID()){
					for(int i = 1; i <= 8; i++) {
						row2 = changeRow2(i, changeRow2(i, changeRow2(i, row)));
						col2 = changeCol2(i, changeCol2(i, changeCol2(i, col)));
						if(isInBounds(idArray, row2, col2) && idArray[row2][col2] == getID()) {
							if(idArray[changeRow2(i, row)][changeCol2(i, col)] == 0 && 
									idArray[changeRow2(i, changeRow2(i, row))][changeCol2(i, changeCol2(i, col))] == 0)
							copy[changeRow2(i, row)][changeCol2(i, col)] = 85000;
							copy[changeRow2(i, changeRow2(i, row))][changeCol2(i, changeCol2(i, col))] = 85000;
						}
					}
				}
				
			}
		}
		return copy;
	}
	/**
	 * 
	 * @param idArray
	 * @param row
	 * @param col
	 * @return the longest row that already exists (if it can be made into a 5 in a row).  5 is max
	 */
	private int longestRow(boolean me, int[][] idArray, int row, int col) {
		int longestCount = 0;
		int count = 0;
		int row2 = row;
		int col2 = col;
		
		for(int i = 1; i <= 8; i++) {
			count = 0;
			row2 = row;
			col2 = col;
			
			count = longestRow2(idArray, row2, col2, i, me);
		if(me){
			if(count > longestCount && longestPossibility(idArray, row, col, i) >= 8)
				longestCount = count;
		}
		else
			if(count > longestCount)
				longestCount = count;
		}
		return longestCount;
		
	}
	private void printArray(int[][] copy) {
		for(int row = 0; row < copy.length; row++) {
			for(int col = 0; col < copy[0].length; col++) {
				System.out.print(copy[row][col] + " ");
			}
			System.out.println();
		}
	}
	/**
	 * 
	 * @param idArray
	 * @param row
	 * @param col
	 * @param change : which direction to go in. in a grid :
	 * 1 2 3
	 * 8 * 4
	 * 7 6 5
	 * @return the longest row of your pieces that can be made sandwiched between 2 enemy pieces
	 */
	private int longestPossibility(int[][] idArray, int row, int col, int change) {
		int count = 0;
		int row2 = row;
		int col2 = col;
		int inverseChange = change + 4;
		if(inverseChange > 8)
			inverseChange -= 8;

			while(isInBounds(idArray, row2, col2)) {
				if(idArray[row2][col2] == getID() || idArray[row2][col2] == 0) {
					count++;
					row2 = changeRow2(change, row2);
					col2 = changeCol2(change, col2);
				}
				else
					break;
				if(count >= 8)
					break;
			}
			row2 = row;
			col2 = col;
			while(count < 8 && isInBounds(idArray, row2, col2)) {
				if(idArray[row2][col2] == getID() || idArray[row2][col2] == 0) {
					count++;
					row2 = changeRow2(inverseChange, row2);
					col2 = changeCol2(inverseChange, col2);
				}
				else
					break;
				if(count >= 8)
					break;
			}
			return count;
	}
	private int longestRow2(int[][] idArray, int row, int col, int change, boolean me) {
		int count = 0;
		int row2 = changeRow2(change, row);
		int col2 = changeCol2(change, col);
		int inverseChange = change + 4;
		if(inverseChange > 8)
			inverseChange -= 8;

			while(isInBounds(idArray, row2, col2)) {
				if(me) {
					if(idArray[row2][col2] == getID()) {
						count++;
						row2 = changeRow2(change, row2);
						col2 = changeCol2(change, col2);
					}
					else
						break;
				}
				else {
					if(idArray[row2][col2] != getID() && idArray[row2][col2] != 0) {
						count++;
						row2 = changeRow2(change, row2);
						col2 = changeCol2(change, col2);
					}
					else
						break;
				}
				if(count >= 8)
					break;
			}
			row2 = changeRow2(inverseChange, row);
			col2 = changeCol2(inverseChange, col);
			while(count < 8 && isInBounds(idArray, row2, col2)) {
				if(me) {
					if(idArray[row2][col2] == getID()) {
						count++;
						row2 = changeRow2(inverseChange, row2);
						col2 = changeCol2(inverseChange, col2);
					}
					else
						break;
				}
				else {
					if(idArray[row2][col2] != getID() && idArray[row2][col2] != 0) {
						count++;
						row2 = changeRow2(inverseChange, row2);
						col2 = changeCol2(inverseChange, col2);
					}
					else
						break;
				}
				if(count >= 8)
					break;
			}
			return count;
	}	
	
	

	/**
	 * change : which direction to go in. in a grid :
	 * 	 * 1 2 3
		 * 8 * 4
		 * 7 6 5
	 */

	private int changeRow2(int num, int row2) {
		if(num == 1) {
			 return --row2;
		}
		else if(num == 2) {
			 return --row2;
		}
		else if(num == 3) {
			 return --row2;
		}
		else if(num == 4) {
			 return row2;
		}
		else if(num == 5) {
			 return ++row2;
		}
		else if(num == 6) {
			 return ++row2;
		}
		else if(num == 7) {
			 return ++row2;
		}
		else{
			 return row2;
		}
		
	}
	private int changeCol2(int num, int col2) {
		if(num == 1) {
			 return --col2;
		}
		else if(num == 2) {
			 return col2;
		}
		else if(num == 3) {
			 return ++col2;
		}
		else if(num == 4) {
			 return ++col2;
		}
		else if(num == 5) {
			 return ++col2;
		}
		else if(num == 6) {
			 return col2;
		}
		else if(num == 7) {
			 return --col2;
		}
		else{
			 return --col2;
		}
		
	}
	private boolean isInBounds(int[][] array, int row, int col) {
		return row >= 0 && row < array.length && col >= 0 && col < array[0].length;
	}
}
