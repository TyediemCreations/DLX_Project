
public class Sudoku{
	private int[][] complete;
	private int[][] incomplete;
	private int[] difficulty;

	public Sudoku(int[][] complete, int[][] incomplete, int[] difficulty){
		this.complete = complete;
		this.incomplete = incomplete;
		this.difficulty = difficulty;
	}
	public int[][] getComplete(){return complete;}
	public int[][] getIncomplete(){return incomplete;}
	public int[] getDifficulty(){return difficulty;}
}
