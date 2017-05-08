public class PosValues{
	boolean[] values;

	public PosValues(int size){
		values = new boolean[size];
		for (int i=0;i<size;i++)
			values[i] = true;
	}

	public void setPos(int row, int col, int[][] puzzle){
		for (int puzRow=0;puzRow<puzzle.length;puzRow++){
			int num=puzzle[puzRow][col];
			if (num != 0){
				values[num-1] = false;
			}
		}
		for (int puzCol=0;puzCol<puzzle.length;puzCol++){
			int num=puzzle[row][puzCol];
			if (num != 0){
				values[num-1] = false;
			}
		}
		
		int regLength = (int) Math.sqrt(values.length);
		int x = (row/regLength)*regLength;
		int y = (col/regLength)*regLength;
		for (int puzRow=x;puzRow<x+regLength;puzRow++){
			for (int puzCol=y;puzCol<y+regLength;puzCol++){
				int num = puzzle[puzRow][puzCol];
				if (num != 0){
					values[num-1] = false;
				}
			}
		}
	}
	public boolean[] get(){return values;}
	public int uniqueVal(int row, int col, PosValues[][] possible){
		boolean[] unique = copyValues();
		for (int posRow=0;posRow<possible.length;posRow++){
			if (posRow == row) continue;
			setUnique(unique,possible[posRow][col].get());
		}
		for (int i=0;i<unique.length;i++){
			if (unique[i]) return i+1;
		}
		unique = copyValues();
		for (int posCol=0;posCol<possible[0].length;posCol++){
			if (posCol == col) continue;
			setUnique(unique,possible[row][posCol].get());
		}
		for (int i=0;i<unique.length;i++){
			if (unique[i]) return i+1;
		}
		unique = copyValues();
		int regLength = (int) Math.sqrt(values.length);
		int x = (row/regLength)*regLength;
		int y = (col/regLength)*regLength;
		for (int posRow=x;posRow<x+regLength;posRow++){
			for (int posCol=y;posCol<y+regLength;posCol++){
				if (posRow == row && posCol == col) continue;
				setUnique(unique,possible[posRow][posCol].get());
			}
		}
		for (int i=0;i<unique.length;i++){
			if (unique[i]) return i+1;
		}
		return 0;
	}
	public boolean[] copyValues(){
		boolean[] copy = new boolean[values.length];
		for (int i=0;i<values.length;i++){
			copy[i] = values[i];
		}
		return copy;
	}
	public void setUnique(boolean[] unique, boolean[] other){
		for (int i=0;i<other.length;i++){
			if (other[i]) unique[i] = false;
		}
	}
	/*
	public boolean[] getUnique(boolean[] other){
		boolean[] unique = new boolean[values.length];
		for (int i=0;i<values.length;i++){
			if (value[i] && !other[i])
				unique[i] = true;
		}
		return unique;
	}*/
	public int getFirst(){
		return getNext(0);
	}
	public int getNext(int num){
		for (int i=num;i<values.length;i++){
			if (values[i]) return i+1;
		}
		return 0;
	}
	public boolean empty(){
		for (int i=0;i<values.length;i++){
			if (values[i]) return false;
		}
		return true;
	}
	public int getLength(){
		int count = 0;
		for (int i=0;i<values.length;i++){
			if (values[i]) count++;
		}
		return count;
	}
}
