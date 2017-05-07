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
	
		//DLX newLinks = new DLX(sudoBinary);
		//newLinks.algorithmXPlus(partialSolution,fullAnswer);
		//return newLinks.getSolutions();

		links.algorithmXPlus(partialSolution,fullAnswer);
		return links.getSolutions();
	}
	public Vector< Vector<Integer> > solvePuzzle(Vector <Integer> partialSolution, boolean fullAnswer){
		links.algorithmXPlus(partialSolution,fullAnswer);
		return links.getSolutions();
	}
	public int[][] newCompletedPuzzle(){
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
			//System.out.println("Trying to place "+randnum+":("+row+","+col+")");
			solutions = solvePuzzle(newPuzzle,false);
			if (solutions.size()==0){
				newPuzzle[row][col] = 0;
				//System.out.println("Failed to place number!");
			}else{
				count++;
				//System.out.println("Successfully placed number!");
			}
		}
		newPuzzle = interpretSolution(solutions.get(0));
		

		return newPuzzle;
	}
	public int getDifficulty(int[][] puzzle){
		int difficulty = 0;

		return difficulty;
	}
	public int[][] createPuzzle(){return createPuzzle(copySudoku());}
	public int[][] createPuzzle(int[][] completed){
		Vector < Vector< Integer >> solutions = new Vector< Vector<Integer> >();

		Collections.shuffle(coords);
		for (int i=0;i<coords.size();i++){
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
	public int[][] copySudoku(){
		int[][] copy = new int[size][size];

		for (int row=0;row<size;row++){
			for (int col=0;col<size;col++){
				copy[row][col] = sudoku[row][col];
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

			/*
			strNum = "Number: "+(number+1);
			strPos = "Row,Col: ("+rowNo+","+colNo+")";
			strReg = "Region: "+regNo;
			System.out.println(strNum+", "+strPos+", "+strReg);
			*/
			sudo[rowNo][colNo] = number+1;
			
		}
		
		//printSudoku(sudo);
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
				printRow += sudo[row][col];
			}
			if (row%regLength == 0)
				System.out.println("");
			System.out.println(printRow);
		}
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
/*
Binary matrix:
    rows:
	row group#0-row#[0-8] represent numbers [1-9] at position (0,0)
	row group#1-row#[9-17] rows represent numbers [1-9] at position (0,1)
	...
	row group#9-row#[81-89] represent numbers [1-9] at position (1,0)
	...
	row group#18-row#[162-170] represent number [1-9] at position (2,0)
	...

	row group#n-row#[(0+9n)-(8+9n)] represent number [1-9] at position (row,col)
		s.t. row = floor(n/9)
		     col = remainder(n/9) = n%9

    cols:
	col collection#1-col#[0-80] (position constraint):
			col#n=>number at position (r,c)
				s.t. r*9+c = n

	col collection#2-col#[81-161] (row constraint):
		row#0: 	81=>row#0 contains number 1
			82=>row#0 contains number 2
			...
		row#1:	90=>row#1 contains number 1
			91=>row#1 contains number 2
			...
		row#2:	99=>row#2 contains number 1
			...
		row#n:	81+9n=>row#n contains number 1
			81+9n+1=> ... 2
			...
			81+9n+8=> ... 9
				s.t. n e.of [0-8]

	col collection#3-col#[162-242] (column constraint):
		col#0:	162=>col#0 contains number 1
			163=>col#0 contains number 2
			...	

		col#n:	162+9n=>col#n contains number 1
			162+9n+1=>col#n contains number 2
			...
			162+9n+8=>col#n contains number 9
		

	col collection#4-col#[243-323] (regional constraint):
		reg#n: 	243+9n=>reg#n contains number 1
			243+9n+1=>reg#n contains number 2
			...
			243+9n+8=>reg#n contains number 9
	
*/

/*
The columns represent the constraints of the puzzle:
	-Position constraint: Only 1 number can occupy a cell
	-Row constraint: Only 1 instance of a number can be in a column
	-Column constraint: Only 1 instance of a number can be in a column
	-A region constraint: Only 1 instance of a number can be in a region

Each number comes with its own set of constraints.
=> SIZE^2 * 4
	s.t. SIZE = 9
=> 324 columns

The rows represent every single possible position for every number.
=> SIZE^3
	s.t. SIZE = 9
=> 729 rows

(Each row would represent only one candidate position. Therefore, only 4 1's will be in the row, representing the constraints of that position.)


*Given the initial positions in the matrix, those rows will be included in the answer and 'covered'. Then the Search algorithm will produce the solutions to the puzzle.
*/

