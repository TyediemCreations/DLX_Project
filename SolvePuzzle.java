import java.util.*;
import java.io.*;

public class SolvePuzzle{
	public static void main(String[] args){
		String fileName = "";

		

		int row = 9;	//originally 9
		int col = 9;	//originally 9

		if (args.length > 0){
			for (int i=0;i<args.length;i++){
				String arg = args[i];
				try{
					int size = Integer.parseInt(arg);
					row = size;
					col = size;
				}
				catch(NumberFormatException e){
					fileName = arg;
				}
			}	
		}
		if (fileName=="") fileName = "sudoku.txt";

		int[][] intMatrix = new int[row][col];
		String line = "";
		try{
			FileReader fileReader = new FileReader(fileName);

			BufferedReader bufferedReader = new BufferedReader(fileReader);

			int rowCounter = 0;
			while((line = bufferedReader.readLine()) != null && rowCounter<row){
				int colCounter = 0;
				//System.out.println(line);
				for (int i=0;i<line.length()&&colCounter<col;i++){
					char c = line.charAt(i);
					if (c >= '0' && c <= '9'){
						int num = Character.getNumericValue(c);
						if (num <= row)
							intMatrix[rowCounter][colCounter] = num;
						colCounter++;
						if (colCounter>=col)
							rowCounter++;
					}else if (c >= 'A' && c<= 'Z'){
						intMatrix[rowCounter][colCounter] = (int) c - 55;
						colCounter++;
						if (colCounter>=col)
							rowCounter++;
					}
				}
				
			}

			bufferedReader.close();
		}
		catch(FileNotFoundException ex){
			System.out.println("Unable to open file '" + fileName +"'");
			return;
		}
		catch(IOException ex){
			System.out.println("Error reading file '" + fileName+"'");
			return;
		}

		Puzzle myPuzzle = new Puzzle(intMatrix);
		myPuzzle.printSudoku(intMatrix);
		myPuzzle.printSolutions();
	}
}
