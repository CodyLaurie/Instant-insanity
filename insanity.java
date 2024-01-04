package main;

import java.util.*;



public class insanity{
	static LinkedList<Integer>[] puzzle;
	static Deque<LinkedList<Integer>> holding = new LinkedList<>();
	static LinkedList<LinkedList<Integer>> spuzzle = new LinkedList<>();
	static double longest = 0;
	static int Lindex = 0;
	static double shortest = 0;
	static int sindex = 0;
	static LinkedList<Integer> inspecting; 
	public static void main(String[] args) {
		//puzzlesolve(puzzlegen(5));
		for(int i = 59; i<60;i++){
			holding = new LinkedList<>();
			spuzzle = new LinkedList<>();
			System.out.println("-------------------");
			System.out.println("Running simulation: " + i);
			long ST = System.nanoTime();
            spuzzlegen(i);
            scramble(puzzle);
            puzzlesolve();
            long ET = System.nanoTime();
            double total= (ET-ST)/1000000000.0;
            if(total>longest) {
            	longest = total;
            	Lindex = i;
            }
            if(total<shortest||shortest==0) {
            	shortest = total;
            	sindex = i;
            }
            System.out.println("-------------------");
            System.out.println("Simutlation " + i +" took " + total + " seconds");
            System.out.println("END OF SIMULATION " + i);
            System.out.println("-------------------");
		}
		 System.out.println("Worst case took " + longest + " seconds at size " + Lindex);
		 System.out.println("Best case took " + shortest + " seconds at size " + sindex);
		
	}
	
	public static void vizualize(LinkedList<Integer>[] puzzle) {
		System.out.println("-------------------");
		for(int i = 0; i <puzzle.length; i++) {
			System.out.println(puzzle[i]);
		}
	}
	public static void vizualize(LinkedList<LinkedList<Integer>> puzzle) {
		System.out.println("-------------------");
		for(int i = 0; i <puzzle.size(); i++) {
			System.out.println(puzzle.get(i));
		}
	}

	public static LinkedList<Integer>[] puzzlegen(int size){
		puzzle = new LinkedList[size];
		int counter = 0;
		int test;
		for(int i = 0; i < size; i++) {
			for(int c  = 0 ; c < 3; c++) {
				counter++;
				test = (int) (1 + Math.floor(counter*17*Math.E)%size);
				try {
					puzzle[i].add(test);
				}catch(Exception e) {
					puzzle[i] = new LinkedList<Integer>();
					puzzle[i].add(test);
				} 
			}
		}
		vizualize(puzzle);
		return puzzle;
	}
	
	public static LinkedList<Integer>[] spuzzlegen(int size){
		puzzle = new LinkedList[size];
		boolean[][] tracker = new boolean[size][3];
		int counter = 0;
		int test;
		for(int i = 0; i < size; i++) {
			for(int c  = 0 ; c < 3; c++) {
				counter++;
				test = (int) (1 + Math.floor(counter*17*Math.E)%size);
				while(tracker[test-1][c] == true) {
					counter++;
					test = (int) (1 + Math.floor(counter*17*Math.E)%size);	
				}
				try {
					puzzle[i].add(test);
				}catch(Exception e) {
					puzzle[i] = new LinkedList<Integer>();
					puzzle[i].add(test);
				}
				tracker[test-1][c] = true; 
			}
		}
		vizualize(puzzle);
		return puzzle;
	}
	
	public static LinkedList<Integer>[] cpuzzlegen(int size){
		puzzle = new LinkedList[size];
		int[] tracker = new int[size];
		for (int i = 0; i<size;i++) {
			tracker[i] = 3;
		}
		int counter = 0;
		int test;
		for(int i = 0; i < size; i++) {
			for(int c  = 0 ; c < 3; c++) {
				counter++;
				test = (int) (1 + Math.floor(counter*17*Math.E)%size);
				while(tracker[test-1] == 0) {
					counter++;
					test = (int) (1 + Math.floor(counter*17*Math.E)%size);	
				}
				try {
					puzzle[i].add(test);
				}catch(Exception e) {
					puzzle[i] = new LinkedList<Integer>();
					puzzle[i].add(test);
				}
				tracker[test-1]--; 
			}
		}
		vizualize(puzzle);
		return puzzle;
	}
	public static LinkedList<Integer> rotate(LinkedList<Integer> piece){
		piece.add(piece.getFirst());
		piece.removeFirst();
		return piece;
	}
	
	public static LinkedList<Integer>[] rotate(LinkedList<Integer>[] puzzle,int index){
		puzzle[index].add(puzzle[index].getFirst());
		puzzle[index].removeFirst();
		return puzzle;
	}
	
	public static LinkedList<Integer> drotate(LinkedList<Integer> piece){
		piece.push(piece.getLast());
		piece.removeLast();
		return piece;
	}
	
	public static LinkedList<Integer>[] drotate(LinkedList<Integer>[] puzzle,int index){
		puzzle[index].push(puzzle[index].getLast());
		puzzle[index].removeLast();
		return puzzle;
	}
	
	public static LinkedList<Integer>[] scramble(LinkedList<Integer>[] puzzle) {
		System.out.println("-------------------");
		System.out.println("Scrambling");
		int counter = -1;
		for (int i = 0 ; i <puzzle.length/2;i++) {
			counter++;
			rotate(puzzle, (int) (Math.floor(counter*17*Math.E)%puzzle.length));
			if(i%2==0) {
				rotate(puzzle, (int) (Math.floor(counter*17*Math.E)%puzzle.length));
			}
		}
		vizualize(puzzle);
		return(puzzle);
	}
	
	public static void puzzlesolve(){
		int tot = 0;
		int counter = 1;
		for(int i = 0; i < puzzle.length;i++) {
			holding.add(puzzle[i]);
		}
		inspecting = holding.remove();
		while (inspecting!=null) {
			if (solcheck(counter)) {
				spuzzle.add(inspecting);
				//System.out.println("!!!!" + inspecting);
				try {
					inspecting = holding.remove();
				}catch(Exception e) {
					inspecting = null;
				}
				counter=0;
			}else {
				//holding.add(rotate(holding.remove()));
				//((LinkedList<LinkedList<Integer>>) holding).push(holding.remove());
				inspecting = rotate(inspecting);
			}
			//if (tot==(Integer.MAX_VALUE)) {
			//if (tot==(400)) {
			//	System.out.println("FAILED");
			//	System.exit(0);
			//}
			tot++;
			counter++;
		}
		System.out.println("-------------------");
		System.out.println("Solution");
		vizualize(spuzzle);
	}
	
	public static boolean solcheck(int counter) {
		try {
			for(int i = 0; i < spuzzle.size();i++) {
				for (int c = 0; c < 3;c++) {
					if(spuzzle.get(i).get(c) == inspecting.get(c)){
						if(counter==5) {
							//vizualize(spuzzle);
							//System.out.println(spuzzle.size());
							//((LinkedList<LinkedList<Integer>>) holding).push(spuzzle.get(i));
							holding.add((spuzzle.get(i)));
							//System.out.println(spuzzle.get(i));
							spuzzle.remove(i);
						}else {
							//System.out.println("!"+inspecting+ " " + counter);
							return false;
						}
					}
				}
			}
		}catch (Exception e) {
		
			return true;
		}
		return true;
	}
}