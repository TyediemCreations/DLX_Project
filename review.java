import java.util.*;
import java.io.*;

public class review{
	

	public static void main(String[] args){
		/*
		LinkedList ll = new LinkedList(10);
		ll.addFirst(20);
		ll.addLast(30);
		ll.removeFirst();
		//ll.printList();
		//System.out.println("Hello, World");

		BST tree = new BST(8,"dummy");
		tree.insert(10);
		tree.insert(3);
		tree.insert(1);
		tree.insert(14);
		tree.insert(13);
		tree.insert(6);
		tree.insert(7);
		tree.insert(4);

		tree.delete(8);

		//tree.printTree();
		Stack myStack = new Stack();
		myStack.push(10);
		myStack.push(20);
		myStack.push(30);
		myStack.pop();
		//myStack.printStack();

		Queue myQueue = new Queue();
		myQueue.enqueue(10);
		myQueue.enqueue(20);
		myQueue.enqueue(30);
		myQueue.dequeue();
		//myQueue.printQueue();

		Graph myGraph = new Graph(1);
		myGraph.addEdge(1,2);
		myGraph.addEdge(1,3);
		myGraph.addEdge(1,4);
		myGraph.addEdge(2,5);
		myGraph.addEdge(2,6);
		myGraph.addEdge(4,7);
		myGraph.addEdge(4,8);
		myGraph.addEdge(5,9);
		myGraph.addEdge(5,10);
		myGraph.addEdge(7,11);
		myGraph.addEdge(7,12);
		myGraph.addEdge(13,14);
		myGraph.addEdge(13,1);
		myGraph.BFS();
		myGraph.printGraph();
		*/

		/*
		Matrix matrix = new Matrix(6,7);
		matrix.set(0,0);
		matrix.set(0,3);
		matrix.set(0,6);
		matrix.set(1,0);
		matrix.set(1,3);
		matrix.set(2,3);
		matrix.set(2,4);
		matrix.set(2,6);
		matrix.set(3,2);
		matrix.set(3,4);
		matrix.set(3,5);
		matrix.set(4,1);
		matrix.set(4,2);
		matrix.set(4,5);
		matrix.set(4,6);
		matrix.set(5,1);
		matrix.set(5,6);
		*/

		/*
		Matrix matrix = new Matrix(11,7);
		matrix.set(0,0);
		matrix.set(0,3);
		matrix.set(0,6);
		matrix.set(1,0);
		matrix.set(1,3);
		matrix.set(2,3);
		matrix.set(2,4);
		matrix.set(2,6);
		matrix.set(3,2);
		matrix.set(3,4);
		matrix.set(3,5);
		matrix.set(4,1);
		matrix.set(4,2);
		matrix.set(4,5);
		matrix.set(4,6);
		matrix.set(5,1);
		matrix.set(5,6);
		matrix.set(6,0);
		matrix.set(6,1);
		matrix.set(6,2);
		matrix.set(6,3);
		matrix.set(6,4);
		matrix.set(6,5);
		matrix.set(6,6);
		matrix.set(7,1);
		matrix.set(7,6);
		matrix.set(8,0);
		matrix.set(8,3);
		matrix.set(9,1);
		matrix.set(10,0);
		matrix.set(10,2);
		matrix.set(10,5);
		//matrix.printMatrix();
		//System.out.println();

		//Vector <Integer> rows = new Vector<Integer>(),cols = new Vector<Integer>();
		
		//matrix.deleteAll(rows,cols);
		//Matrix copy = matrix.matrixCopy();
		//copy.deleteRow(1);
		//copy.deleteColumn(1);

		

		//copy.printMatrix();
		
		//matrix.algorithmX(matrix);
		//matrix.printSolutions();
		*/
		/*
		ColumnHeader header1 = new ColumnHeader(1);
		ColumnHeader header2 = new ColumnHeader(2);
		ColumnHeader header3 = new ColumnHeader(3);

		header1.right = header2;
		header2.left = header1;
		header2.right = header3;
		header3.left = header2;

		header2.removeHeader();
		header2.restoreHeader();
		header3.removeHeader();
		header3.restoreHeader();
		header1.printHeaders();
		*/
		
		boolean [][] matrix = new boolean[6][7];
		/*
		boolean [][] matrix = new boolean[3][2];

		matrix[0][0] = false;
		matrix[0][1] = true;
		matrix[1][0] = true;
		matrix[1][1] = false;
		matrix[2][0] = true;
		matrix[2][1] = true;
		
			01
			10
			11
		*/
		for (int row=0;row<matrix.length;row++){
			for (int col=0;col<matrix[row].length;col++){
				matrix[row][col] = false;
			}
		}
		/*
		matrix[0][2] = true;
		matrix[0][4] = true;
		matrix[0][5] = true;

		matrix[1][0] = true;
		matrix[1][3] = true;
		matrix[1][6] = true;

		matrix[2][1] = true;
		matrix[2][2] = true;
		matrix[2][5] = true;

		matrix[3][0] = true;
		matrix[3][3] = true;

		matrix[4][1] = true;
		matrix[4][6] = true;

		matrix[5][3] = true;
		matrix[5][4] = true;
		matrix[5][6] = true;
		/*
		0010110
		1001001
		0110010
		1001000
		0100001
		0001101

		solution as per 'algorithmX':
		(3,0,4)

		0010110
		1001000
		0100001
		
		
		*/
		/**/
		matrix[0][0] = true;
		matrix[0][3] = true;
		matrix[0][6] = true;

		matrix[1][0] = true;
		matrix[1][3] = true;

		matrix[2][3] = true;
		matrix[2][4] = true;
		matrix[2][6] = true;

		matrix[3][2] = true;
		matrix[3][4] = true;
		matrix[3][5] = true;

		matrix[4][1] = true;
		matrix[4][2] = true;
		matrix[4][5] = true;
		matrix[4][6] = true;

		matrix[5][1] = true;
		matrix[5][6] = true;
		/*
		1001001
		1001000
		0001101
		0010110
		0110011
		0100001

		solution as per algorithmX:
		(1,3,5)
		1001000
		0010110
		0100001
		*/

		/*
		matrix[1][1] = true;
		matrix[2][0] = true;
		matrix[3][0] = true;
		matrix[4][0] = true;
		matrix[4][1] = true;
		/*
		00
		01
		10
		10
		11

		solution as per algorithmX: 
		(1,2)
		(1,3)
		(4)

		01
		10

		01
		10

		11
		*/

		System.out.println("\nHello, world\n");
		
		String fileName = "DLX/sudoEx.txt";

		String line = "";

		int row = 9;
		int col = 9;
		int[][] intMatrix = new int[row][col];
		
		for (int i=0;i<row;i++){
			for (int j=0;j<col;j++){
				intMatrix[i][j] = 0;
			}
		}

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
						intMatrix[rowCounter][colCounter] = Character.getNumericValue(c);
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
		}
		catch(IOException ex){
			System.out.println("Error reading file '" + fileName+"'");
		}
		/*
		for (int i=0;i<row;i++){
			String str = "";
			for (int j=0;j<col;j++){
				str += intMatrix[i][j];
			}
			System.out.println(str);			
		}
		*/

		/**/
		DLX links = new DLX(matrix);
		System.out.println("\nBeginning...\n");
		//links.printLinks();
		//links.printNode();
		//links.removeColumn(links.exampleLink());
		//System.out.println("");
		//links.returnColumn(links.exampleLink());
		//links.updateCount();
		//System.out.println("Column chosen: "+links.chooseColumn().strFormatLink());
		
		//links.algorithmX();
		//links.printLinksV2();
		System.out.println("\n\n");
		links.printLinks();

		links.printSolutions();
		/**/
		System.out.println("\nGoodbye, all\n");
		
	}	
}
