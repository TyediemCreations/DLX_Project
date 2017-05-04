import java.util.*;

public class DLX{
	private DancingLink h;
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

		updateCount();	//set initial 'size' for each Column Header		
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
	public void updateCount(){
		for (DancingLink link=h.getRight();link != null;link=link.getRight()){
			link.setSize(link.countSize());
		}
	}
	public DancingLink chooseColumn(){
		DancingLink ans = h.getRight();
		int min = ans.getSize();

		for (DancingLink nextColumn=ans.getRight(); nextColumn != null; nextColumn = nextColumn.getRight()){
			if (nextColumn.getSize() < min){
				min = nextColumn.getSize();
				ans = nextColumn;
			}
		}

		return ans;
	}

	public void printLinks(){
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
	public String strFormatLink(DancingLink theLink){
		return theLink.strFormatLink();
	}
	public void printLinksV2(){	/*
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
				if (theLink.getRow() == -1)
					str = theLink.getSize() +", "+str;
				System.out.println("Node: "+strFormatLink(theLink)+"\t"+str);
			}
			System.out.println("");
		}
	}
	
	public void removeColumn(DancingLink c){	//such that 'c' is a columnNode
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

				leftNode.getColumnHeader().decreaseSize();
			}
			//--would not be necessary if DancingLinks looped back on each other
			for (DancingLink rightNode = row.getRight(); rightNode != null; rightNode = rightNode.getRight()){
				if (rightNode.getUp() != null)
					rightNode.getUp().setDown(rightNode.getDown());
				if (rightNode.getDown() != null)				
					rightNode.getDown().setUp(rightNode.getUp());

				rightNode.getColumnHeader().decreaseSize();
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

				leftNode.getColumnHeader().increaseSize();
			}
			//
			for (DancingLink rightNode = row.getRight(); rightNode != null; rightNode = rightNode.getRight()){
				if (rightNode.getUp() != null)
					rightNode.getUp().setDown(rightNode);
				if (rightNode.getDown() != null)
					rightNode.getDown().setUp(rightNode);

				rightNode.getColumnHeader().increaseSize();
			}
			//
		}
	}
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
	public Vector <Integer> solutionCopy(Vector<Integer> pS){
		Vector <Integer> copy = new Vector<Integer>();

		for (int i=0;i<pS.size();i++){
			copy.add(pS.get(i));
		}
		return copy;
	}
	public void algorithmX(){
		Vector <Integer> pS = new Vector <Integer>();
		algorithmX(pS);
	}
	public void algorithmX(Vector <Integer> pS){		
		Vector <Integer> partialSolution = solutionCopy(pS);

		if (h.getRight() == null){
			solutions.add(partialSolution);
			return;	//Current partial solution is valid; terminate successfully
		}
		DancingLink c = chooseColumn();	//choose column c, such that c is the first column with the fewest 1's
		
		if (c.getSize() < 1){
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
			algorithmX(partialSolution);
			partialSolution = solutionCopy(pS);

			//return removed columns
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



/*
To do:
	-(eventually) optimize 'countSize' (update automatically following the deletion/insertion of a Link to a column)

	-(completed?) verify 'return' works as it says on the tin
	-(completed?) verify 'size' is being updated accurately and at the correct time
*/

/*
If running into issues implementing algX, double-check remove/returnColumn; make sure the
	order removed/returned doesn't affect the outcome.
*/