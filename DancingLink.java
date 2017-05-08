
/*
* Dancing Link; represents a position in a binary matrix with a '1'.
  Each DancingLink contains 4 links to nodes representing '1's above, below, left and right
  relative to the binary matrix. Each node also contains a pointer to their column header.

  Created by 'DLX' for use in 'algorithmX'
*/

public class DancingLink{
	private DancingLink left,right,up,down,columnHeader;
	private int row,column;
	
	public DancingLink(int row, int column){
		this.row = row;
		this.column = column;

		left = null;
		right = null;
		up = null;
		down = null;
		columnHeader = null;
	}
	
	//constructor for special 'columnHeader' link
	public DancingLink(int column){
		row = -1;
		this.column = column;

		left = null;
		right = null;
		up = null;
		down = null;
		columnHeader = null;
	}

	public int getRow(){return row;}
	public int getColumn(){return column;}
	public DancingLink getUp(){return up;}
	public DancingLink getDown(){return down;}
	public DancingLink getLeft(){return left;}
	public DancingLink getRight(){return right;}
	public DancingLink getColumnHeader(){return columnHeader;}

	public void setUp(DancingLink up){
		this.up = up;
	}
	public void setDown(DancingLink down){
		this.down = down;
	}
	public void setLeft(DancingLink left){
		this.left = left;
	}
	public void setRight(DancingLink right){
		this.right = right;
	}
	public void setColumnHeader(DancingLink ch){
		columnHeader = ch;
	}

	public String strFormatLink(){	//used for testing purposes,
 					//returns a String representation of a link
		String coord;
		if (row != -1) coord = "("+row+","+column+")";
		else coord = "("+column+")";
		return coord;
	}public String strFormatLink(DancingLink theLink){
		if (theLink == null)
			return "null";

		String coord = theLink.strFormatLink();
		return coord;
	}

	public int countSize(){		//returns number of nodes connected below current node
		if (down == null)
			return 0;
		else return 1+down.countSize();
	}
}
