package stringpredecessor;

import java.util.ArrayList;

public class ArrayTrie {
	private ArrayTrie parent;
	private ArrayTrie[] children;
	private int count;
	private char character;
	
	public ArrayTrie(ArrayTrie pnode, char newCharacter) {
		children = new ArrayTrie[28];
		parent = pnode;
		count = 0;
		character = newCharacter;
	}
	
	public ArrayTrie() {
		children = new ArrayTrie[28];
		count = 0;
	}
	
	void insert(String newString) {
		if(newString.length() == 0) {
			count++;
			children[26] = predecessor();
			children[27] = successor();
			children[26].setSuccessor(this);
			children[27].setPredecessor(this);
		}
		else {
			int ind = charToInt(newString.charAt(0));
			if(children[ind] == null) {
				children[ind] = new ArrayTrie(this, newString.charAt(0));
				
			}
			children[ind].insert(newString.substring(1));
			
			//set predecessors and successors

		}
		
	}
	
	public ArrayTrie predecessor() {
		if(children[26] == null) {
			return recurseUp();
		}
		else {
			return children[26];
		}
	}
	
	private ArrayTrie recurseUp() {
		if(parent == null) {
			return parent;
		}
		ArrayTrie result;
		for(int i = charToInt(character)-1; i >=0; i--) {
			if(parent.children[i] != null) {
				if(parent.children[i].count > 0) {
					return parent.children[i];
				}
				else {
					result = parent.children[i].recurseDown();
					if(result != null) {
						return result;
					}
				}
			}
		}
		if(count > 0) {
			return this;
		}
		return parent.recurseUp();
	}
	
	private ArrayTrie recurseDown() {
		ArrayTrie t;
		for(int i = 25; i >=0; i--) {
			if(children[i] != null) {
				t = children[i].recurseDown();
				if(t != null) {
					return t;
				}
			}
		}
		if(count > 0) {
			return this;
		}
		return null;
	}
	
	private ArrayTrie recurseUpSuc() {
		if(parent == null) {
			return parent;
		}
		ArrayTrie result;
		for(int i = charToInt(character)+1; i < 26; i++) {
			if(parent.children[i] != null) {
				if(parent.children[i].count > 0) {
					return parent.children[i];
				}
				else {
					result = parent.children[i].recurseDownSuc();
					if(result != null) {
						return result;
					}
				}
			}
		}
		if(count > 0) {
			return this;
		}
		return parent.recurseUpSuc();
	}
	
	private ArrayTrie recurseDownSuc() {
		ArrayTrie t;
		for(int i = 0; i < 26; i++) {
			if(children[i] != null) {
				t = children[i].recurseDownSuc();
				if(t != null) {
					return t;
				}
			}
		}
		if(count > 0) {
			return this;
		}
		return null;
	}
	
	public ArrayTrie successor() {
		if(children[27] == null) {
			return recurseUpSuc();
		}
		else {
			return children[27];
		}
	}
	
	public void setSuccessor(ArrayTrie newSuc) {
		children[27] = newSuc;
	}
	
	public void setPredecessor(ArrayTrie newPred) {
		children[26] = newPred;
	}
	
	public String getWord() {
		if(parent == null) {
			return "";
		}
		else {
			return parent.getWord() + character;
		}
	}

	public int charToInt(char c) {
		return Character.toLowerCase(c) - 97;
	}
	
	public char intToChar(int c) {
		return (char) (c + 97);
	}
	
	public ArrayList<String> sorted(String curWord) {
		ArrayList<String> tempStrings = new ArrayList();
		if(count > 0) {
			tempStrings.add(curWord + character);
		}
		for(int i = 0; i < 26; i++) {
			if(children[i] != null) {
				tempStrings.addAll(children[i].sorted(curWord + character));
			}
		}
		return tempStrings;
	}

	
	public static String[] sortStrings(String[] toSort) {
		ArrayTrie t = new ArrayTrie();
		for(String str : toSort) {
			t.insert(str);
		}
		ArrayList<String> sortedArray = t.sorted("");
		String[] temp = new String[sortedArray.size()];
		temp = sortedArray.toArray(temp);
		return temp;
		
	}
	public static void main(String args[]) {
		
		String[] testStrings = {"hello", "hell", "aaaa", "adfslj", "asdfajsldkjfa"};
		String[] testResult = sortStrings(testStrings);
		
		for(String i : testResult) {
			System.out.println(i);
		}
		
		
		ArrayTrie t = new ArrayTrie();
		for(String str : testStrings) {
			t.insert(str);
		}
		System.out.println(t.children[0].children[3].successor().getWord());
		
	}
}
