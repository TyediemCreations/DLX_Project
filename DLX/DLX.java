
/*
  DLX; given the binary matrix representation of a sudoku puzzle, DLX will convert
  into DancingLinks. DLX can then be used to find all solutions to that puzzle, in the form
  of a list of all lists of row indices on the binary matrix that represent an exact cover
*/

import java.util.*;

public class DLX{
	private DancingLink h;	//header node; points to start of column headers
	private Vector< Vector <DancingLink> > links;
	private int matrixRows,matrixCols;
	private Vector< Vector <Integer> > solutions;

	public DLX(boolean[][] matrix){
		links = new Vector< Vector <DancingLink> >();
		solutions = new Vector< Vector <Integer> >();

		h = new DancingLink(-1);

		int rows = matrix.length;
		int cols = matrix[0].length;
		matrixRows = rows;
		matrixCols = cols;
		
		Vector <DancingLink> columnHeaders = new Vector<DancingLink>();
		for (int col=0;col<cols;col++){
			DancingLink newColumnHeader = new DancingLink(col);
			columnHeaders.add(newColumnHeader);
		}
		links.add(columnHeaders);

		for (int row=0;row<rows;row++){
			Vector <DancingLink> newRow = new Vector<DancingLink>();
			for (int col=0;col<cols;col++){
				if (matrix[row][col]){
					DancingLink newLink = new DancingLink(row,col);
					newRow.add(newLink);
				}
			}
			links.add(newRow);
		}

		h.setRight(links.get(0).get(0));
		links.get(0).get(0).setLeft(h);

		for (int row=0;row<links.size();row++){
			Vector <DancingLink> linkRow = links.get(row);
			int numCols = linkRow.size();

			for (int col=0;col<numCols;col++){
				DancingLink theLink = linkRow.get(col);
				if ((col-1) >= 0)
					theLink.setLeft(linkRow.get(col-1));
				if ((col+1) < numCols)
					theLink.setRight(linkRow.get(col+1));
				setLower(theLink,row);
			}
		}
		setColumnHeaders();		
	}
	private void setColumnHeaders(){
		for (DancingLink ch=h.getRight();ch != null; ch=ch.getRight()){
			for (DancingLink rowLink=ch.getDown();rowLink != null;rowLink=rowLink.getDown()){
				rowLink.setColumnHeader(ch);
			}
		}
	}
	public void setLower(DancingLink theLink, int startRow){
		/*
			connects link 'theLink' and the nearest lower link in the same column
			(relative to the matrix, not within 'links' itself)
			if no such link exists, this function does nothing.
		*/
		int linkRow = theLink.getRow();
		int linkColumn = theLink.getColumn();

		for (int row=startRow+1;row<links.size();row++){
			for (int col=0;col<links.get(row).size();col++){
				DancingLink nextLink = links.get(row).get(col);
				int nextColumn = nextLink.getColumn();
	
				if (linkColumn == nextColumn){
					theLink.setDown(nextLink);
					nextLink.setUp(theLink);
					return;
				}
				else if (linkColumn < nextColumn)
					break;
			}
		}
		return;
	}
	public DancingLink chooseColumn(){	//choose column with fewest '1's, i.e., smallest size
		DancingLink ans = h.getRight();
		int min = ans.countSize();

		for (DancingLink nextColumn=ans.getRight(); nextColumn != null; nextColumn = nextColumn.getRight()){
			int nextSize = nextColumn.countSize();
			if (nextSize < min){
				min = nextSize;
				ans = nextColumn;
			}
		}

		return ans;
	}

	public void printLinks(){	/*
					 * prints the coordinates of all links currently 
					 * connected to the header, 'h'.
					 * Used for testing purposes.
					 */
		String[][] iMatrix = new String[matrixRows+1][matrixCols];

		for (int i=0;i<matrixRows+1;i++){
			for (int j=0;j<matrixCols;j++){
				iMatrix[i][j] = "";
			}
		}

		for (DancingLink row=h.getRight();row != null; row = row.getRight()){
			for (DancingLink col=row; col != null; col = col.getDown()){
				iMatrix[col.getRow()+1][col.getColumn()] = col.strFormatLink();
			}
		}

		for (int i=0;i<iMatrix.length;i++){
			String printString = "Row "+(i-1)+"\t";
			for (int j=0;j<iMatrix[i].length;j++){
				printString += iMatrix[i][j] + "\t";
			}
			System.out.println(printString);
		}
		
	}
	public void printLinksAll(){	/*
					 * prints the coordinates of all links to stdout, 
					 * as well as the coordinates of all conencted links
					 * (regardless of whether or not these are connected)
					 * to the header)
					 */
		String left,right,up,down,str;

		for (int row=0;row<links.size();row++){
			for (int col=0;col<links.get(row).size();col++){
				DancingLink theLink = links.get(row).get(col);

				left = strFormatLink(theLink.getLeft());
				right = strFormatLink(theLink.getRight());
				up = strFormatLink(theLink.getUp());
				down = strFormatLink(theLink.getDown());

				str = "Left: "+left +", Right: "+right+", Up: "+up+", Down: "+down;
				//if (theLink.getRow() == -1)
				//	str = theLink.getSize() +", "+str;
				System.out.println("Node: "+strFormatLink(theLink)+"\t"+str);
			}
			System.out.println("");
		}
	}
	public String strFormatLink(DancingLink theLink){return theLink.strFormatLink();}
	public String strSolution(Vector <Integer> solution){
		String ans = "(";
		for (int row=0;row<solution.size();row++){
			if (row != 0) ans += ",";
			ans += solution.get(row);
		}
		ans += ")";
		
		return ans;
	}
	public void printSolutions(){
		for (int solution=0;solution<solutions.size();solution++){
			String printString = "Solution#"+(solution+1);
			printString += strSolution(solutions.get(solution));
			System.out.println(printString);
		}
	}

	public void removeColumn(DancingLink c){	
				/* such that 'c' is a columnNode
			         * relative to binary matrix A, removes column 'c',
				 * as well as all rows 'r' such that A[r][c] == 1
				 */
		if (c.getLeft() != null)
			c.getLeft().setRight(c.getRight());
		if (c.getRight() != null)
			c.getRight().setLeft(c.getLeft());

		for (DancingLink row=c.getDown(); row != null; row = row.getDown()){
			for (DancingLink leftNode = row.getLeft(); leftNode != null; leftNode = leftNode.getLeft()){
				if (leftNode.getUp() != null)
					leftNode.getUp().setDown(leftNode.getDown());
				if (leftNode.getDown() != null)
					leftNode.getDown().setUp(leftNode.getUp());
			}
			//--would not be necessary if DancingLinks looped back on each other
			for (DancingLink rightNode = row.getRight(); rightNode != null; rightNode = rightNode.getRight()){
				if (rightNode.getUp() != null)
					rightNode.getUp().setDown(rightNode.getDown());
				if (rightNode.getDown() != null)				
					rightNode.getDown().setUp(rightNode.getUp());
			}
			//
		}
	}
	public void returnColumn(DancingLink c){	//such that 'c' is a columnNode
		if (c.getLeft() != null)
			c.getLeft().setRight(c);
		if (c.getRight() != null)
			c.getRight().setLeft(c);

		for (DancingLink row = c.getDown(); row != null; row = row.getDown()){
			for (DancingLink leftNode = row.getLeft(); leftNode != null; leftNode = leftNode.getLeft()){
				if (leftNode.getUp() != null)
					leftNode.getUp().setDown(leftNode);
				if (leftNode.getDown() != null)
					leftNode.getDown().setUp(leftNode);
			}
			//
			for (DancingLink rightNode = row.getRight(); rightNode != null; rightNode = rightNode.getRight()){
				if (rightNode.getUp() != null)
					rightNode.getUp().setDown(rightNode);
				if (rightNode.getDown() != null)
					rightNode.getDown().setUp(rightNode);
			}
			//
		}
	}
	
	public Vector <Integer> solutionCopy(Vector<Integer> pS){
		Vector <Integer> copy = new Vector<Integer>();

		for (int i=0;i<pS.size();i++){
			copy.add(pS.get(i));
		}
		return copy;
	}
	public Vector< Vector <Integer> > getSolutions(){
		Vector< Vector<Integer> > solutionsCopy = new Vector< Vector<Integer> >();
		for (int i=0;i<solutions.size();i++){
			Vector<Integer> rowCopy = new Vector<Integer>();
			for (int j=0;j<solutions.get(i).size();j++){
				rowCopy.add(solutions.get(i).get(j));
			}
			solutionsCopy.add(rowCopy);
		}
		return solutionsCopy;
	}
	
	public void algorithmXPlus(Vector <Integer> pS,boolean fullAnswer){
			/*
			Runs algorithmX given an initial partial solution that must occur
			in any full solution.
			*/	
		
		solutions = new Vector< Vector<Integer> >();

		boolean[] toRemove = new boolean[matrixRows];
		for (int i=0;i<pS.size();i++){
			toRemove[pS.get(i)] = true;
		}

		//remove rows found in the initial partial solution
		Vector<DancingLink> removedLinks = new Vector<DancingLink>();

		for (DancingLink c=h.getRight();c != null; c=c.getRight()){
			for (DancingLink r=c.getDown();r != null;r = r.getDown()){
				if (!toRemove[r.getRow()])
					continue;

				removeColumn(c);
				removedLinks.add(c);
				for (DancingLink rightNode=r.getRight();rightNode != null;rightNode = rightNode.getRight()){
					removeColumn(rightNode.getColumnHeader());
					removedLinks.add(rightNode.getColumnHeader());
				}
				for (DancingLink leftNode=r.getLeft();leftNode != null;leftNode = leftNode.getLeft()){
					removeColumn(leftNode.getColumnHeader());
					removedLinks.add(leftNode.getColumnHeader());
				}
			}			
		}

		algorithmX(pS,fullAnswer);

		//return rows
		for (int i=0;i<removedLinks.size();i++){
			DancingLink removedLink = removedLinks.get(i);
			returnColumn(removedLink);
		}
	}
	public void algorithmXPlusPlus(boolean[] toRemove,boolean fullAnswer){
			/*
			  Slight variation of 'algorithmXPlus', cuts down on a slight amount
			  of repeated work for puzzle creation
			*/	
		Vector <Integer> pS = new Vector<Integer>();
		solutions = new Vector< Vector<Integer> >();

		//remove rows found in the initial partial solution
		Vector<DancingLink> removedLinks = new Vector<DancingLink>();
		
		for (DancingLink c=h.getRight();c != null; c=c.getRight()){
			for (DancingLink r=c.getDown();r != null;r = r.getDown()){
				if (!toRemove[r.getRow()])
					continue;

				removeColumn(c);
				removedLinks.add(c);
				for (DancingLink rightNode=r.getRight();rightNode != null;rightNode = rightNode.getRight()){
					removeColumn(rightNode.getColumnHeader());
					removedLinks.add(rightNode.getColumnHeader());
				}
				for (DancingLink leftNode=r.getLeft();leftNode != null;leftNode = leftNode.getLeft()){
					removeColumn(leftNode.getColumnHeader());
					removedLinks.add(leftNode.getColumnHeader());
				}
			}			
		}
		/**/

		algorithmX(pS,fullAnswer);

		//return rows
		for (int i=0;i<removedLinks.size();i++){
			DancingLink removedLink = removedLinks.get(i);
			returnColumn(removedLink);
		}
	}
	public void algorithmX(){
		solutions = new Vector< Vector<Integer> >();

		Vector <Integer> pS = new Vector <Integer>();
		algorithmX(pS,true);
	}
	public boolean emptyColumn(){
		for (DancingLink c=h.getRight();c!=null;c=c.getRight()){
			if (c.getDown() == null) return true;
		}
		return false;
	}
	private void algorithmX(Vector <Integer> pS, boolean fullAnswer){

		if (!fullAnswer && solutions.size() > 1){
			//for puzzle creation; indicates puzzle has at least 2 solutions
			return;
		}

		Vector <Integer> partialSolution = solutionCopy(pS);

		if (h.getRight() == null){
			solutions.add(partialSolution);
			return;	//Current partial solution is valid; terminate successfully
		}
		DancingLink c = chooseColumn();	//choose column c, such that c is the first column with the fewest 1's
		
		if (c.getDown() == null){
			return;	//terminate unsuccessfully
		}
		

		//delete all columns s.t. A[r][c] == 1 (r is defined below)
		for (DancingLink r=c.getDown();r != null;r = r.getDown()){
			//choose a row r such that A[r][c] == 1
			//include r in the partial solution
			partialSolution.add(r.getRow());

			removeColumn(c);
			for (DancingLink rightNode=r.getRight();rightNode != null;rightNode = rightNode.getRight()){
				removeColumn(rightNode.getColumnHeader());
			}
			for (DancingLink leftNode=r.getLeft();leftNode != null;leftNode = leftNode.getLeft()){
				removeColumn(leftNode.getColumnHeader());
			}

			//repeat this algorithm recursively on the reduced matrix A
			algorithmX(partialSolution, fullAnswer);
			partialSolution = solutionCopy(pS);

			//return removed columns to previous state
			returnColumn(c);
			for (DancingLink rightNode=r.getRight();rightNode != null;rightNode = rightNode.getRight()){
				returnColumn(rightNode.getColumnHeader());
			}
			for (DancingLink leftNode=r.getLeft();leftNode != null;leftNode = leftNode.getLeft()){
				returnColumn(leftNode.getColumnHeader());
			}
		}
	}
}

