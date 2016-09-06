/*Barbara He
 * Period 4
 * 5/21/15
 * 
 * So far I've spent about 4 hrs on this lab.
 * 
 * This lab is pretty hard to code because while even though I've thought out
 * the general strategies and concepts, checking for certain indications is
 * tedious. For instance, one of my human strategies is to form 4 piece rows, 
 * but then I have to first have a method that finds a 3 piece row and another
 * method that checks to see if there are open spaces to place a piece. I commented 
 * out some parts of my code that doesn't work yet to avoid errors. So far, my 
 * makeMove method tries to make a 3 piece row, but if not possible, it will first
 * try making a 2 piece row or putting a piece randomly near the middle.
 */

package aiplayer;
import java.util.ArrayList;
import pente.AIPlayer;
import pente.Location;

public class AIPlayerBHePeriod4 extends AIPlayer {

	private static String name = "furry friend";
	private static String iconFile = "pink.png";

	public AIPlayerBHePeriod4(int id) {
		super(iconFile, name, id);
	}

	@Override
	public Location makeMove(int[][] idArray, int moveCount) {
		System.out.println(findMyLongestSequence(idArray).size());
		
		if (findMyLongestSequence(idArray).size() > 0){
			if (open(idArray, findMyLongestSequence(idArray)) != null){
				return open(idArray, findMyLongestSequence(idArray));
			}else{
				int tempSize = findMyLongestSequence(idArray).size();
				while (tempSize >= 1 && findSpecificSequence(idArray, tempSize) == null){
					tempSize--;
					if (findSpecificSequence(idArray, tempSize) != null && open(idArray, findSpecificSequence(idArray, tempSize)) != null){
						return open(idArray, findSpecificSequence(idArray, tempSize));
					}
				}
			}
		}
		
		
		//if i have pieces on the board
		ArrayList<Location> mine = myPieces(idArray);
		if (mine.size() > 0){
			//if i have 2 pieces in a row, make a 3 in a row sequence if possible
			if (findSpecificSequence(idArray, 4) != null && open(idArray, findSpecificSequence(idArray, 4)) != null){
				System.out.println(findSpecificSequence(idArray, 4));
				return open(idArray, findSpecificSequence(idArray, 4));
			}
			if (findSpecificSequence(idArray, 3) != null && open(idArray, findSpecificSequence(idArray, 3)) != null){
				System.out.println(findSpecificSequence(idArray, 3));
				return open(idArray, findSpecificSequence(idArray, 3));
			}
			if (findSpecificSequence(idArray, 2) != null && open(idArray, twoInRow(idArray)) != null){
				System.out.println(findSpecificSequence(idArray, 2));
				return open(idArray, twoInRow(idArray));
			}
			// otherwise put a piece next to one of my pieces
			for (int i = 0; i < mine.size(); i++){
				int r = mine.get(i).getRow();
				int c = mine.get(i).getCol();
				ArrayList<Location> l = emptyCell(idArray, allNeighbors(idArray, r, c));
				if (l.size() > 0){
					return new Location(l.get(0).getRow(), l.get(0).getCol());
				}
			}
		}
		//if i dont have any pieces on board, play randomly near the middle
		boolean done = false;
		while (!done) {
			int row = (int)(Math.random()*idArray.length/2 + idArray.length/4);
			int col = (int)(Math.random()*idArray[0].length/2 + idArray.length/4);
			if (idArray[row][col] == 0) {
				//System.out.println(getName() + " placing piece at (" + row + ", " + col + ")");
				return new Location(row, col);
			}

		}
		/*
		if (myCaptures() == 8){
			open(idArray, twoInRow(idArray));
		}
		

		//if opponent has 4 in row, block open side
		if (fourInRow(idArray, false).size() > 0){
			return open(idArray,fourInRow(idArray, false));
		}

		if (opponentCaptures() >= 6){
			//save all 2 piece rows
		}

		//if I have 3 or 2 pieces in a row, add one to open side if possible
		if (threeInRow(idArray, true).size() != 0){
			return open(idArray, twoInRow(idArray));
		}else if (//2 in row + space + 1 piece){
			//return middle location 	
		}else if(twoInRow(idArray).size() != 0){
			return open(idArray, twoInRow(idArray));
		}
		*/
		return null;
	}
	

	private ArrayList<Location> opponentPieces(int[][] idArray){
		ArrayList<Location> list = new ArrayList<Location>();

		for (int r = 0; r < idArray.length; r++){
			for (int c = 0; c < idArray[0].length; c++){
				if (idArray[r][c] > 0 && idArray[r][c] != getID()){
					list.add(new Location(r,c));
				}
			}
		}

		return list;
	}

	private ArrayList<Location> myPieces(int[][] idArray){
		ArrayList<Location> list = new ArrayList<Location>();

		for (int r = 0; r < idArray.length; r++){
			for (int c = 0; c < idArray[0].length; c++){
				if (idArray[r][c] > 0 && idArray[r][c] == getID()){
					list.add(new Location(r,c));
				}
			}
		}

		return list;
	}
	
	private void find(int[][] idArray){
		ArrayList<Location> list = myPieces(idArray);
		for (int i = 0; i < list.size(); i++){
			int r = list.get(i).getRow();
			int c = list.get(i).getCol();
			ArrayList<Location> temp = emptyCell(idArray, allNeighbors(idArray, r, c));
		}
	}
	
	//returns arraylist of all legal neighbors
	private ArrayList<Location> allNeighbors(int[][] idArray, int row, int col){
		int rBound = idArray.length;
		int cBound = idArray[0].length;
		ArrayList<Location> list = new ArrayList<Location>();
		if (row - 1 >= 0){
			list.add(new Location(row-1, col));
			if (col - 1 >= 0){
				list.add(new Location(row-1, col-1));
			}
			if (col + 1 < cBound){
				list.add(new Location(row-1, col+1));
			}
		}
		
		if (col - 1 >= 0){
			list.add(new Location(row, col-1));
		}
		if (col + 1 < cBound){
			list.add(new Location(row, col+1));
		}
		
		if (row + 1 < rBound){
			list.add(new Location(row+1, col));
			if (col - 1 >= 0){
				list.add(new Location(row+1, col-1));
			}
			if (col + 1 < cBound){
				list.add(new Location(row+1, col+1));
			}
		}
		return list;	
	}
	
	//my pieces near 
	private ArrayList<Location> occupiedCell(int[][] idArray, ArrayList<Location> list){
		ArrayList<Location> a = new ArrayList<Location>();
		for (int i = 0; i < list.size(); i++){
			int r = list.get(i).getRow();
			int c = list.get(i).getCol();
			if (idArray[r][c] > 0 && idArray[r][c] == getID()){
				a.add(new Location(r, c));
			}
		}
		return a;
	}
	
	//neighboring opponent pieces
	private ArrayList<Location> occupiedCellOpp(int[][] idArray, ArrayList<Location> list){
		ArrayList<Location> a = new ArrayList<Location>();
		for (int i = 0; i < list.size(); i++){
			int r = list.get(i).getRow();
			int c = list.get(i).getCol();
			if (idArray[r][c] > 0 && idArray[r][c] != getID()){
				a.add(new Location(r, c));
			}
		}
		return a;
	}
	
	
	//neighboring empty cells
	private ArrayList<Location> emptyCell(int[][] idArray, ArrayList<Location> list){
		ArrayList<Location> a = new ArrayList<Location>();
		for (int i = 0; i < list.size(); i++){
			int r = list.get(i).getRow();
			int c = list.get(i).getCol();
			if (idArray[r][c] == 0){
				a.add(new Location(r, c));
			}
		}
		return a;
	}
	
	//returns arraylist of 2 locations in row, returns empty list if none exist
	private ArrayList<Location> twoInRow(int[][] idArray){
		ArrayList<Location> list = new ArrayList<Location>();
		for (int r = 0; r < idArray.length; r++){
			for (int c = 0; c < idArray[0].length; c++){
				//arraylist of neighboring locations that are occupied by me
				ArrayList<Location> temp = occupiedCell(idArray, allNeighbors(idArray, r, c));
				if (temp.size() > 0){
					list.add(new Location (r, c));
					int tempR = temp.get(0).getRow();
					int tempC = temp.get(0).getCol();
					list.add(new Location(tempR, tempC));
				}
			}
		}
		return list;
	}

	//returns arraylist of 2 locations in row, returns empty list if none exist
	private ArrayList<Location> twoInRowOpp(int[][] idArray){
		ArrayList<Location> list = new ArrayList<Location>();
		for (int r = 0; r < idArray.length; r++){
			for (int c = 0; c < idArray[0].length; c++){
				//arraylist of neighboring locations that are occupied by me
				ArrayList<Location> temp = occupiedCellOpp(idArray, allNeighbors(idArray, r, c));
				if (temp.size() > 0){
					list.add(new Location (r, c));
					int tempR = temp.get(0).getRow();
					int tempC = temp.get(0).getCol();
					list.add(new Location(tempR, tempC));
				}
			}
		}
		return list;
	}
	
	private Location capture(int[][] idArray){
		return open(idArray, twoInRowOpp(idArray));
	}
	
	private ArrayList<Location> findMyLongestSequence(int[][] idArray){
		int maxLength = 0;
		ArrayList<Location> maxSeq = new ArrayList<Location>();
		
		ArrayList<Location> myPieces = myPieces(idArray);
		for (int i = 0; i < myPieces.size(); i++){
			int r = myPieces.get(i).getRow();
			int c = myPieces.get(i).getCol();
			if (findSequence(idArray, r, c).size() > maxLength){
				maxLength = findSequence(idArray, r, c).size();
				maxSeq = findSequence(idArray, r, c);
			}
		}
		return maxSeq;
	}
	
	private ArrayList<Location> findOppLongestSequence(int[][] idArray){
		int maxLength = 0;
		ArrayList<Location> maxSeq = new ArrayList<Location>();
		
		ArrayList<Location> opp = opponentPieces(idArray);
		for (int i = 0; i < opp.size(); i++){
			int r = opp.get(i).getRow();
			int c = opp.get(i).getCol();
			if (findSequenceOpp(idArray, r, c).size() > maxLength){
				maxLength = findSequence(idArray, r, c).size();
				maxSeq = findSequence(idArray, r, c);
			}
		}
		return maxSeq;
	}
	
	private ArrayList<Location> findSpecificSequence(int[][] idArray, int seqLength){
		for (int r = 0; r < idArray.length; r++){
			for (int c = 0; c < idArray[0].length; c++){
				if (findSequence(idArray, r, c).size() == seqLength){
					return findSequence(idArray, r, c);
				}
			}
		}
		return null;
	}
	
	//find longest sequence starting from a particular location
	private ArrayList<Location> findSequence(int[][] idArray, int row, int col){
		ArrayList<Location> maxSeq = new ArrayList<Location>();
		maxSeq.add(new Location(row, col));
		int tempID = idArray[row][col];
		ArrayList<Location> occ = occupiedCell(idArray, allNeighbors(idArray, row, col));
		int maxSize = 1;
		
		for (int i = 0; i < occ.size(); i++){
			ArrayList<Location> tempSeq = new ArrayList<Location>();
			tempSeq.add(new Location(row, col));
			
			int tempRow = occ.get(i).getRow();
			int tempCol = occ.get(i).getCol();
			int tempRowDiff = tempRow - row;
			int tempColDiff = tempCol - row;
			boolean continues = true;
	
			while (continues && tempRow >= 0 && tempRow < idArray.length && tempCol >= 0 && tempCol < idArray[0].length){
				if (idArray[tempRow][tempCol] == tempID){
					tempSeq.add(new Location(tempRow, tempCol));
				}else{
					continues = false;
				}
				tempRow += tempRowDiff;
				tempCol += tempColDiff;
				
			}
			if (tempSeq.size() > maxSize){
				maxSize = tempSeq.size();
				maxSeq = tempSeq;
			}
		}
		return maxSeq;
	}
	
	private ArrayList<Location> findSequenceOpp(int[][] idArray, int row, int col){
		ArrayList<Location> maxSeq = new ArrayList<Location>();
		maxSeq.add(new Location(row, col));
		int tempID = idArray[row][col];
		ArrayList<Location> occ = occupiedCellOpp(idArray, allNeighbors(idArray, row, col));
		int maxSize = 1;
		
		for (int i = 0; i < occ.size(); i++){
			ArrayList<Location> tempSeq = new ArrayList<Location>();
			tempSeq.add(new Location(row, col));
			
			int tempRow = occ.get(i).getRow();
			int tempCol = occ.get(i).getCol();
			int tempRowDiff = tempRow - row;
			int tempColDiff = tempCol - row;
			boolean continues = true;
	
			while (continues && tempRow >= 0 && tempRow < idArray.length && tempCol >= 0 && tempCol < idArray[0].length){
				if (idArray[tempRow][tempCol] == tempID){
					tempSeq.add(new Location(tempRow, tempCol));
				}else{
					continues = false;
				}
				tempRow += tempRowDiff;
				tempCol += tempColDiff;
				
			}
			if (tempSeq.size() > maxSize){
				maxSize = tempSeq.size();
				maxSeq = tempSeq;
			}
		}
		return maxSeq;
	}

	//returns empty location to play piece to continue a row, returns null if none, given row that is 2+ pieces
	private Location open(int[][] idArray, ArrayList<Location> sequence){
		
		int r = sequence.get(0).getRow();
		int c = sequence.get(0).getCol();
		int tempR1, tempC1, tempR2, tempC2 = 0;
		int last = sequence.size()-1;
		
		if (sequence.size() == 1){
			return emptyCell(idArray, allNeighbors(idArray, r, c)).get(0);
		}
		
		int rowDiff = sequence.get(1).getRow() - r;
		int colDiff = sequence.get(1).getCol() - c;
		
		if (rowDiff == 0){
			tempR1 = tempR2 = r;
			tempC1 = c;
			tempC2 = sequence.get(last).getCol();
		}else if (colDiff == 0){
			tempC1 = tempC2 = c;
			tempR1 = r - rowDiff;
			tempR2 = sequence.get(last).getRow() + rowDiff;
		}else{
			tempR1 = r;
			tempR2 = sequence.get(last).getRow() + rowDiff;
			tempC1 = c;
			tempC2 = sequence.get(last).getCol() + colDiff;
		}
		
		if (tempR1 >= 0 && tempC1 >= 0 && idArray[tempR1][tempC1] == 0){
			return new Location(tempR1, tempC1);
		}
		
		if (tempR2 < idArray.length && tempC2 < idArray[0].length && idArray[tempR2][tempC2] == 0){
			return new Location(tempR2, tempC2);
		}
		
		return null;
	}         
}