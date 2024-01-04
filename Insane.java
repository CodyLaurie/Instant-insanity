package main;

import java.util.*;

/* Cody Laurie 12/7/23 Code was trimmed from a more robust solver as to not have extra code. */

public class Insane{
	static LinkedList<Integer>[] puzzle;
	static Deque<LinkedList<Integer>> holding = new LinkedList<>();
	static LinkedList<LinkedList<Integer>> spuzzle = new LinkedList<LinkedList<Integer>>();
	static int location;
	static double longest = 0;
	static int Lindex = 0;
	static double shortest = 0;
	static int sindex = 0;
	public static void main(String[] args) {
			for(int i = 65; i<66;i++){
				holding = new LinkedList<>();
				spuzzle = new LinkedList<>();
				System.out.println("-------------------");
				System.out.println("Running simulation: " + i);
				long ST = System.nanoTime();
	            cpuzzlegen(i);
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
		 System.out.println("Solved in " + longest + " seconds at size " + Lindex);
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
	
	public static void puzzlesolve(){
		int tot = 0;
		int counter = 0;
		for(int i = 0; i < puzzle.length;i++) {
			holding.add(puzzle[i]);
		}
		while (!holding.isEmpty()) {
			if (solcheck(counter)) {
				spuzzle.add(holding.remove());
				if(counter>4) {
					counter=0;
				}
			}else {
				((LinkedList<LinkedList<Integer>>) holding).push(rotate(holding.remove()));
			}
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
					if(spuzzle.get(i).get(c) == holding.peek().get(c)){
						if(counter>4) {
							holding.add(rotate(spuzzle.get(i)));
							spuzzle.remove(i);
							return false;
						}else {
							return false;
						}
					}
				}
			}
		}catch (Exception e) {
			//System.out.println("!");
			return true;
		}
		return true;
	}

}