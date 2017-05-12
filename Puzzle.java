
/*
  Puzzle; given an integer matrix representation of a sudoku puzzle, Puzzle will convert this
  into a binary matrix usable by 'DLX'. 
  Puzzle can then be used to solve the sudoku, create a new puzzle based on the sudoku, 
  or come up with its own puzzles + solutions independently
*/

import java.util.*;
import java.io.*;

public class Puzzle{
	private int[][] sudoku;	
	private int size;
	private boolean[][] sudoBinary;
	private DLX links;
	private Vector<int[]> coords;

	public Puzzle(int[][] sudoku){
		this.sudoku = sudoku;
		size = sudoku.length;
		int binRows = (size*size*size);	//324 columns; one for each constraint
		int binCols = (size*size)*4;	//729 rows; one for every possible position for every number
		coords = new Vector<int[]>();
		for (int row=0;row<size;row++){
			for (int col=0;col<size;col++){
				int[] coord = new int[2];
				coord[0] = row;
				coord[1] = col;
				coords.add(coord);
			}
		}

		sudoBinary = new boolean[binRows][binCols];
		fillBinary();

		links = new DLX(sudoBinary);
	}
	private void fillBinary(){
		int c1_start=0;
		int c2_start=c1_start+size*size; 
		int c3_start=c2_start+size*size;
		int c4_start=c3_start+size*size;

		for (int row=0;row<sudoBinary.length;row++){
			int groupNo = row/size;
			int number = row%size;	//add a 1 if displaying this number
			int rowNo = groupNo/size;
			int colNo = groupNo%size;
			int regNo = getRegNo(rowNo,colNo);

			sudoBinary[row][c1_start+rowNo*size+colNo] = true;
			sudoBinary[row][c2_start+rowNo*size+number] = true;
			sudoBinary[row][c3_start+colNo*size+number] = true;
			sudoBinary[row][c4_start+regNo*size+number] = true;
		}
	}
	public int getRegNo(int rowNo, int colNo){
		int regLength = (int) Math.sqrt(size);
		int regRow = rowNo/regLength;
		int regCol = colNo/regLength;
		int regNo = regRow*regLength + regCol;

		return regNo;
	}
	public boolean[][] getBinary(){return sudoBinary;}

	public Vector< Vector<Integer> > solvePuzzle(){return solvePuzzle(sudoku,true);}
	public Vector< Vector<Integer> > solvePuzzle(int[][] toSolve, boolean fullAnswer){
		Vector <Integer> partialSolution = new Vector<Integer>();

		for (int row=0;row<toSolve.length;row++){
			for (int col=0;col<toSolve[row].length;col++){
				int num = toSolve[row][col];
				if (num != 0){
					int rowNo = getBinaryRow(num, row, col);
					partialSolution.add(rowNo);
				}
			}
		}
	
		links.algorithmXPlus(partialSolution,fullAnswer);
		return links.getSolutions();
	}
	public Vector< Vector<Integer> > solvePuzzle(Vector <Integer> partialSolution, boolean fullAnswer){
		links.algorithmXPlus(partialSolution,fullAnswer);
		return links.getSolutions();
	}
	public int[][] newCompletedPuzzle(){	/*creates new puzzle by randomly placing
						 *6 random numbers, and finding the first
						 *possible solution given that initial setup
						 */
		int[][] newPuzzle = new int[size][size];

		Vector < Vector< Integer >> solutions = new Vector< Vector<Integer> >();
		Random rand = new Random();
		int count=0;
		
		Collections.shuffle(coords);
		for (int coord=0;coord<coords.size() && count<6;coord++){
			int row = coords.get(coord)[0];
			int col = coords.get(coord)[1];
			
			int randnum = rand.nextInt(size)+1;
			newPuzzle[row][col] = randnum;
			solutions = solvePuzzle(newPuzzle,false);
			if (solutions.size()==0){
				newPuzzle[row][col] = 0;
			}else{
				count++;
			}
		}
		newPuzzle = interpretSolution(solutions.get(0));
		

		return newPuzzle;
	}
	public void printDifficulty(int difficulty){
		int base = (size*size)+1;
		int[] level = new int[5];
		for (int i=4;i>=0;i--){
			level[i] = difficulty/((int) Math.pow(base,i));
			difficulty-= level[i]*(Math.pow(base,i));
		}

		System.out.println("Duh: "+level[0]+", Easy: "+level[1]+", Med: "+level[2]+", Challenge: "+level[3]+", Hard: "+level[4]);
	}
	public void printDifficulty(long difficulty){
		int base = (size*size)+1;
		int[] level = new int[5];
		for (int i=4;i>=0;i--){
			level[i] = (int) difficulty/((int) Math.pow(base,i));
			difficulty-= level[i]*(Math.pow(base,i));
		}

		System.out.println("Duh: "+level[0]+", Easy: "+level[1]+", Med: "+level[2]+", Challenge: "+level[3]+", Hard: "+level[4]);
	}
	public void printDifficulty(int[] level){
		System.out.println("Duh: "+level[0]+", Easy: "+level[1]+", Med: "+level[2]+", Challenge: "+level[3]+", Hard: "+level[4]);
	}
	public int[] getDifficulty(int[][] puzzle){
		PosValues[][] possible = new PosValues[size][size];
		for (int i=0;i<size;i++)
			for (int j=0;j<size;j++)
				possible[i][j] = new PosValues(size);
		return getDifficulty(puzzle,possible);
	}
	public int[] getDifficulty(int[][] puzzle,PosValues[][] possible){
		int num;
		int base = (size*size)+1;
		//int difficulty = 0; 
		//level = {'duh','easy','medium','challenge','hard'}
		int[] level = new int[5];
		int[] empty = null;
		boolean changed;

		do{
			changed = false;
			for (int row=0;row<puzzle.length;row++){
				for (int col=0;col<puzzle.length;col++){
					if (puzzle[row][col] == 0){
						int difVal = possible[row][col].setPos(row,col,puzzle);
						int uniqueVal = 0;
						//int uniqueVal = possible[row][col].uniqueVal(row,col,possible);				
						if (difVal==-1){return null;}
						else if (difVal==0) {}
						else if (difVal == 1 || difVal == 2){
							num = possible[row][col].getFirst();
							puzzle[row][col] = num;
							if (difVal==1) level[0]++;
							else level[1]++;
							changed = true;
						}else if (uniqueVal != 0){
							puzzle[row][col] = uniqueVal;
							level[2]++;
							changed = true;
						}else if (difVal == 3){
							num = possible[row][col].getFirst();
							puzzle[row][col] = num;
							level[3]++;
							changed = true;
						}
						
					}
					else{
						possible[row][col].set(puzzle[row][col]);
					}
				}
			}
		}while(changed);
		//for (int i=0;i<level.length;i++){
		//	difficulty+=level[i]*Math.pow(base,i);
		//}

		int minRow=-1,minCol=-1,minLength=10;
		for (int row=0;row<puzzle.length;row++){
			for (int col=0;col<puzzle.length;col++){
				int len = possible[row][col].getLength();
				if (len==0) return null;
				if (puzzle[row][col] == 0 && len < minLength){
					minRow=row;
					minCol=col;
					minLength = len;
				}
			}
		}
		if (minRow==-1) {return level;}
		/**/
		int tryNum = 0;
		int[] pos_diff = new int[5];
		int[][] puzzle_copy = new int[size][size];
		PosValues[][] possible_copy = new PosValues[size][size];
		//Vector<Vector<Integer>> solutions = new Vector<Vector<Integer>>();
		do{
			puzzle_copy = copySudoku(puzzle);
			possible_copy = copyPossible(possible);
			num = possible[minRow][minCol].getNext(tryNum);
			tryNum = num;
			if (num==0) return null;

			puzzle_copy[minRow][minCol] = num;
			//puzzle[minRow][minCol] = num;
			pos_diff = getDifficulty(puzzle_copy,possible_copy);
			//solutions = solvePuzzle(puzzle,false);
		//}while(solutions.size() != 1);
		}while(pos_diff == null);
		//difficulty += pos_diff;

		level[4]++;
		//difficulty += level[4] * Math.pow(base,4);
		level = add(level,pos_diff);

		for (int x=0;x<size;x++)
			for (int y=0;y<size;y++)
				puzzle[x][y] = puzzle_copy[x][y];
		possible = copyPossible(possible_copy);
		
		return level;
	}
	public int[] add(int[] dif1, int[] dif2){
		int[] ans = new int[5];
		for (int i=0;i<ans.length;i++){
			ans[i] = dif1[i] + dif2[i];
		}
		return ans;
	}
	public int[] avg(int[] level, int num){
		int[] ans = new int[5];
		for (int i=0;i<level.length;i++){
			ans[i] = (int) (level[i]/num);
		}
		return ans;
	}
	public PosValues[][] copyPossible(PosValues[][] possible){
		PosValues[][] copy = new PosValues[size][size];
		for (int i=0; i<possible.length;i++){
			for (int j=0;j<possible.length;j++){
				copy[i][j] = new PosValues(size);
				copy[i][j].copy(possible[i][j].copyValues());
			}
		}
		return copy;
	}
	public boolean solved(int[][] puzzle){
		for (int row=0;row<puzzle.length;row++){
			for (int col=0;col<puzzle[row].length;col++){
				if (puzzle[row][col] == 0)
					return false;
			}
		}
		return true;
	}

	public int[][] createPuzzle(){return createPuzzle(copySudoku(),0);}
	public int[][] createPuzzle(int[][] completed, double ignorePercent){
		Vector < Vector< Integer >> solutions = new Vector< Vector<Integer> >();

		Collections.shuffle(coords);

		int ignoreNum = (int) (coords.size() * ignorePercent);
		int ignorePast = coords.size() - ignoreNum;
		for (int i=0;i<coords.size();i++){
			if (i >= ignorePast) break; 

			int row = coords.get(i)[0];
			int col = coords.get(i)[1];
			int currNum = completed[row][col];

			if (currNum==0) continue;

			completed[row][col] = 0;
			solutions = solvePuzzle(completed,false);

			if (solutions.size() != 1)
				completed[row][col] = currNum;
		}

		return completed;
	}
	public int[][] createPuzzlePlus(int[][] completed, double ignorePercent){
		Vector < Vector< Integer >> solutions = new Vector< Vector<Integer> >();

		Collections.shuffle(coords);

		boolean[] toRemove = new boolean[sudoBinary.length];
		for (int row=0;row<completed.length;row++){
			for (int col=0;col<completed[row].length;col++){
				int num = completed[row][col];
				if (num != 0){
					int rowNo = getBinaryRow(num, row, col);
					toRemove[rowNo] = true;
				}
			}
		} 
		

		int ignoreNum = (int) (coords.size() * ignorePercent);
		int ignorePast = coords.size() - ignoreNum;
		for (int i=0;i<coords.size();i++){
			if (i >= ignorePast) break; 

			int row = coords.get(i)[0];
			int col = coords.get(i)[1];
			int currNum = completed[row][col];

			if (currNum==0) continue;
			int rowNo = getBinaryRow(currNum,row,col);
			
			toRemove[rowNo] = false;
			completed[row][col] = 0;
			//solutions = solvePuzzle(completed,false);
			links.algorithmXPlusPlus(toRemove,false);
			solutions = links.getSolutions();

			if (solutions.size() != 1){
				completed[row][col] = currNum;
				toRemove[rowNo] = true;
			}
		}

		return completed;
	}
	/*
	public Vector< Vector<Integer> > solvePuzzle(int[][] toSolve, boolean fullAnswer){
		Vector <Integer> partialSolution = new Vector<Integer>();

		for (int row=0;row<toSolve.length;row++){
			for (int col=0;col<toSolve[row].length;col++){
				int num = toSolve[row][col];
				if (num != 0){
					int rowNo = getBinaryRow(num, row, col);
					partialSolution.add(rowNo);
				}
			}
		}
	
		links.algorithmXPlus(partialSolution,fullAnswer);
		return links.getSolutions();
	}
	*/
	public int[][] copySudoku(){
		int[][] copy = new int[size][size];

		for (int row=0;row<size;row++){
			for (int col=0;col<size;col++){
				copy[row][col] = sudoku[row][col];
			}
		}
		return copy;
	}
	public int[][] copySudoku(int[][] sud){
		int[][] copy = new int[sud.length][sud.length];

		for (int row=0;row<sud.length;row++){
			for (int col=0;col<sud.length;col++){
				copy[row][col] = sud[row][col];
			}
		}
		return copy;
	}

	public int getBinaryRow(int num, int row, int col){
		int rowNo = row*size*size + col*size + (num-1);
		return rowNo;
	}

	public int[][] interpretSolution(Vector <Integer> solution){
		String strNum,strPos,strReg;
		int[][] sudo = new int[size][size];
		for (int i=0;i<solution.size();i++){
			int row = solution.get(i);

			int groupNo = row/size;
			int number = row%size;	//add a 1 if displaying this number
			int rowNo = groupNo/size;
			int colNo = groupNo%size;
			int regNo = getRegNo(rowNo,colNo); 

			sudo[rowNo][colNo] = number+1;
			
		}
		return sudo;
	}

	public void printSudoku(){printSudoku(sudoku);}
	public void printSudoku(int[][] sudo){
		int regLength = (int) Math.sqrt(size);
		for (int row=0;row<sudo.length;row++){
			String printRow = "";
			for (int col=0;col<sudo[row].length;col++){
				if (col%regLength == 0)
					printRow += " ";
				char num = numberFormat(sudo[row][col]);
				printRow += num;
			}
			if (row%regLength == 0)
				System.out.println("");
			System.out.println(printRow);
		}
	}
	public char numberFormat(int num){
		if (num < 10) return (char) (num+48);
		else return (char) ((num-10)+65);
	}
	public void printBinary(){
		String filename = "test.txt";
		
		BufferedWriter bw = null;
		FileWriter fw = null;
	
		try{
			fw = new FileWriter(filename);
			bw = new BufferedWriter(fw);

			for (int i=0;i<sudoBinary.length;i++){
				String content = "";
				for (int j=0;j<sudoBinary[i].length;j++){
					if (j%(size*size)==0)
						content+= " ";

					if (sudoBinary[i][j])
						content += "1";
					else
						content += "0";
				}
				content += "\n";
				bw.write(content);
			}

			System.out.println("Finished printing to file");
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			try{
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			}catch (IOException ex){
				ex.printStackTrace();
			}
		}	
	}
}

