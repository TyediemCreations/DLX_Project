/*
  SolvePuzzle; a Main-level class, can be run using 'make run NAME=CreateSudoku'
  Currently supports command-line arguments representing a text file containing a
  representation of the puzzle to be solved, and a number representing the size of 
  the puzzle. (these arguments can be set using 'ARGS = "<filename> <number>")

  SolvePuzzle prints to stdout all solutions to a puzzle, given by a text file.
  Note: SolvePuzzle won't start printing solutions until every solution is found.
  running on an empty 9x9 / greater puzzle is a bad idea.
*/

import java.util.*;
import java.io.*;

public class SolvePuzzle{
	public static void main(String[] args){
		String fileName = "";

		

		int row = 9;	//standard is 9
		int col = 9;	//standard is 9

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
		if (fileName.equals("")) fileName = "sudoku.txt";

		int[][] intMatrix = new int[row][col];;

		String line = "";
		try{
			FileReader fileReader = new FileReader(fileName);

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			int rowCounter = 0;
			while((line = bufferedReader.readLine()) != null && rowCounter<row){
				int colCounter = 0;
				for (int i=0;i<line.length()&&colCounter<col;i++){
					char c = line.charAt(i);
					if (c >= '0' && c <= '9' || c >= 'A' && c<= 'Z'){
						int num = 0;
						if (c >= '0' && c <= '9')
							num = Character.getNumericValue(c);
						else 
							num = (int) c - 55;
						
						if (num <= row){
							intMatrix[rowCounter][colCounter] = num;
						}
						colCounter++;
					
						if (colCounter >= col) rowCounter++;	
					}
				}
				rowCounter++;
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
		System.out.println("");
		myPuzzle.printSolutions();
	}
}
