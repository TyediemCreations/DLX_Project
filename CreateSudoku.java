import java.util.*;
import java.io.*;

public class CreateSudoku{
	public static void main(String[] args){
		//int size = 9;

		/* 
		if (args.length > 0){
			for (int i=0;i<args.length;i++){
				String arg = args[i];
				try{
					int size = Integer.parseInt(arg);
				}
				catch(NumberFormatException e){
					
				}
			}	
		}*/
		int dim,num;

		dim = 4;
		num = 10;
		Vector<Sudoku> fourPuzzles = makePuzzles(dim,num);

		dim = 9;
		num = 50;
		Vector<Sudoku> ninePuzzles = makePuzzles(dim,num);

		dim = 16;
		num = 4;
		Vector<Sudoku> sixteenPuzzles = makePuzzles(dim,num);

		printIncomplete(fourPuzzles);
		printIncomplete(ninePuzzles);
		printIncomplete(sixteenPuzzles);

		System.out.println("------------------------------------");
		System.out.println("------------------------------------");

		printComplete(fourPuzzles);
		printComplete(ninePuzzles);
		printComplete(sixteenPuzzles);
	}

	public static Vector<Sudoku> makePuzzles(int dim, int num){
		int[][] intMatrix = new int[dim][dim];
		Puzzle myPuzzle = new Puzzle(intMatrix);

		Vector<Sudoku> puzzles = new Vector<Sudoku>();
		
		for (int i=(num-1);i>=0;i--){
			int[][] newPuzzle = myPuzzle.newCompletedPuzzle();

			double ignoreNum = ((double) i/ (double) num) * 0.75;
			int[][] newIncomplete = myPuzzle.createPuzzlePlus(newPuzzle,ignoreNum);
			int[] dif = myPuzzle.getDifficulty(newIncomplete);
			
			Sudoku sudo = new Sudoku(newPuzzle,newIncomplete,dif);
			puzzles.add(sudo);
		}
		return puzzles;
	}

	public static void printIncomplete(Vector<Sudoku> puzzles){
		Puzzle p = new Puzzle(new int[1][1]);
		for (int i=0;i<puzzles.size();i++){
			Sudoku puzzle = puzzles.get(i);

			p.printSudoku(puzzle.getIncomplete());
			System.out.println("\n-----");
		}
	}
	public static void printComplete(Vector<Sudoku> puzzles){
		Puzzle p = new Puzzle(new int[1][1]);
		for (int i=0;i<puzzles.size();i++){
			Sudoku puzzle = puzzles.get(i);

			p.printSudoku(puzzle.getComplete());
			System.out.println("\n-----");
		}
	}
}
