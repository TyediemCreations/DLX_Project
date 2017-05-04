
/*
* https://www.ocf.berkeley.edu/~jchu/publicportal/sudoku/sudokku.paper.html
*/

public class DancingLink{
	private DancingLink left,right,up,down,columnHeader;
	private int row,column,size;
	
	public DancingLink(int row, int column){
		left = null;
		right = null;
		up = null;
		down = null;
		columnHeader = null;

		this.row = row;
		this.column = column;
	}
	
	//constructor for special 'columnHeader' link
	
	public DancingLink(int column){
		this.column = column;
		row = -1;

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

		//System.out.println("Connected (down)"+strFormatLink() + " to "+strFormatLink(down));
	}
	public void setLeft(DancingLink left){
		this.left = left;
	}
	public void setRight(DancingLink right){
		this.right = right;

		//System.out.println("Connected (right)"+strFormatLink() + " to "+strFormatLink(right));
	}
	public void setColumnHeader(DancingLink ch){
		columnHeader = ch;
	}

	public String strFormatLink(){
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
	public void increaseSize(){size++;}
	public void decreaseSize(){size--;}

	public void setSize(int size){
		this.size = size;
	}
	public int getSize(){return size;}
}