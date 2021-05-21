package stringpredecessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SuffixTrie {
	private String edgeTo;
	private ArrayList<SuffixTrie> children;
	
	public SuffixTrie(String edge) {
		edgeTo = edge;
		children = new ArrayList<SuffixTrie>();
	}
	
	public void insert(String newSuffix) {
		return;
	}
	
	public static SuffixTrie construct(String inp) {
		return new SuffixTrie("fuck you");
	}
	
	
	public static ArrayList<String> suffArray(String inp){
		 HashMap<Character, ArrayList<Integer>> indMap = new HashMap<Character, ArrayList<Integer>>();
		 for(int i = 0; i < inp.length(); i++) {
			 char curChar = inp.charAt(i);
			 if(indMap.containsKey(curChar)) {
				 indMap.get(curChar).add(i);
			 }
			 else {
				 ArrayList<Integer> newL = new ArrayList<Integer>();
				 newL.add(i);
				 indMap.put(curChar, newL);
			 }
		 }
		 
		 char tempArray[] = inp.toCharArray();
	     Arrays.sort(tempArray);
	     Integer[] sortedRank = new Integer[tempArray.length];
	     int count = 0;
	     char last = ' ';
	     for(int i = 0; i < tempArray.length; i++) {
	    	 if(tempArray[i] == last) {
	    		 count++;
	    	 }
	    	 else {
	    		 count = 0;
	    	 }
	    	 sortedRank[i] = indMap.get(tempArray[i]).get(count);
	    	 System.out.println(sortedRank[i]);
	    	 last = tempArray[i];
	     }
	     
	     //ArrayList<>
	     
	     return new ArrayList<String>();
		
	}
	
	
	public static void main(String[] args) {
		System.out.println(suffArray("banana"));
    	SelfBalancingBinarySearchTree t = new SelfBalancingBinarySearchTree();
    	t.insert(20);
    	t.insert(5);
    	t.insert(16);
    	t.insert(32);
    	t.insert(3);
    	
    	t.inorder();
    	System.out.println(t.ordered);
	}
}
