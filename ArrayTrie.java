package stringpredecessor;

import java.util.ArrayList;

public class ArrayTrie {
	private ArrayTrie parent;
	//array representation of trie children
	private ArrayTrie[] children;
	
	//how many times this word has been inserted
	//also keeps track of if this node contains an actual word or is just transitional
	private int count;
	private char character;
	
	
	public ArrayTrie(ArrayTrie pnode, char newCharacter) {
		children = new ArrayTrie[28];
		parent = pnode;
		count = 0;
		character = newCharacter;
	}
	
	/**
	 * only for root node
	 */
	public ArrayTrie() {
		children = new ArrayTrie[28];
		count = 0;
	}
	
	/**
	 * add a new string to the trie
	 * recursively adds new nodes as it goes until it reaches the final letter
	 * @param newString - String to be added
	 */
	void insert(String newString) {
		if(newString.length() == 0) {
			count++;
			//set predecessors and successors once added
			children[26] = predecessor();
			children[27] = successor();
			
			//change successor and predecessor of adjacent nodes as well, to keep the whole trie updated
			if(children[26] != null) {
				children[26].setSuccessor(this);
			}
			if(children[27] != null) {
				children[27].setPredecessor(this);
			}
			
		}
		else {
			//recursion
			
			int ind = charToInt(newString.charAt(0));
			if(children[ind] == null) {
				children[ind] = new ArrayTrie(this, newString.charAt(0));
				
			}
			children[ind].insert(newString.substring(1));
			
			

		}
		
	}
	
	/**
	 * quick way to get a node from name, recursive search
	 * @param nodeName - name of node you want
	 * @return - node you want
	 */
	public ArrayTrie getNode(String nodeName) {
		if(nodeName.length() == 0) {
			return this;
		}
		int ind = charToInt(nodeName.charAt(0));
		return children[ind].getNode(nodeName.substring(1));
	}
	
	
	/**
	 * gets the "predecessor", or the last string in the trie that has a count > 0 / has been inserted
	 * If you sort the strings, it would return the string before the string this node represents
	 * @return - node for the predecessor string
	 */
	public ArrayTrie predecessor() {
		if(children[26] == null) {
			return recurseUp();
		}
		else {
			return children[26];
		}
	}
	
	/**
	 * recursive function for use in predecessor. Traverses up the trie to find the closest node behind.
	 * Also calls recurseDown, which will explore the trees downwards on the left of this node. 
	 * We need the upwards and downwards recursion to properly explore tree and keep track of relative position to current node
	 * @return - the predecessor, or null if there is no predecessor
	 */
	private ArrayTrie recurseUp() {
		if(parent == null) {
			return null;
		}
		ArrayTrie result;
		for(int i = charToInt(character)-1; i >=0; i--) {
			//child loop
			if(parent.children[i] != null) {
				if(parent.children[i].count > 0) {
					//already found
					return parent.children[i];
				}
				else {
					//recurse down if valid
					result = parent.children[i].recurseDown();
					if(result != null) {
						return result;
					}
				}
			}
		}
		// use parent then
		if(parent.count > 0) {
			return parent;
		}
		//if not, recurse up again
		return parent.recurseUp();
	}
	
	
	/**
	 * the downwards recursion - finds any below current node, used from recurseUp
	 * looks from right to left since it is looking for predecessor
	 * @return - predecessor if found, null else
	 */
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
	
	/**
	 * Equivalent of recurseUp but for successor
	 * Not very much changed, except ordering of loop
	 * @return - successor if found, null else
	 */
	private ArrayTrie recurseUpSuc() {
		if(parent == null) {
			return null;
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
		if(parent.count > 0) {
			return parent;
		}
		return parent.recurseUpSuc();
	}
	
	
	/**
	 * equivalent recurse down for successor. This one has a loop going the opposite direction
	 * it also checks first if it can be used - for successor, we want the lowest 
	 * So we look in the opposite order
	 * @return - successor if found, null else
	 */
	private ArrayTrie recurseDownSuc() {
		if(count > 0) {
			return this;
		}
		ArrayTrie t;
		for(int i = 0; i < 26; i++) {
			if(children[i] != null) {
				t = children[i].recurseDownSuc();
				if(t != null) {
					return t;
				}
			}
		}
		return null;
	}
	
	/**
	 * special case for successor - we first want to check children of the current node since they will be the next up. 
	 * function is necessary because of how the recursion is structured
	 * @return - successor if found, null else
	 */
	private ArrayTrie recurseDownSpecial() {
		ArrayTrie t;
		for(int i = 0; i < 26; i++) {
			if(children[i] != null) {
				t = children[i].recurseDownSuc();
				if(t != null) {
					return t;
				}
			}
		}
		return null;
	}
	
	/**
	 * finds successor of current node, similar functionality to predecessor
	 * needs to check its children first, though, since they come directly after
	 * @return
	 */
	public ArrayTrie successor() {
		if(children[27] == null) {
			ArrayTrie trySelf = recurseDownSpecial();
			if(trySelf != null) {
				return trySelf;
			}
			return recurseUpSuc();
		}
		else {
			return children[27];
		}
	}
	
	/**
	 * set for successor
	 * @param newSuc
	 */
	public void setSuccessor(ArrayTrie newSuc) {
		children[27] = newSuc;
	}
	
	/**
	 * set for predecessor
	 * @param newPred
	 */
	public void setPredecessor(ArrayTrie newPred) {
		children[26] = newPred;
	}
	
	/**
	 * recursively gets full word name of this node
	 * name is not stored
	 * @return
	 */
	public String getWord() {
		if(parent == null) {
			return "";
		}
		else {
			return parent.getWord() + character;
		}
	}

	
	/**
	 * helper conversion between chars and 0-25 
	 * @param c
	 * @return
	 */
	public int charToInt(char c) {
		return Character.toLowerCase(c) - 97;
	}
	
	/**
	 * converts back from 0-25 to char
	 * @param c
	 * @return
	 */
	public char intToChar(int c) {
		return (char) (c + 97);
	}
	
	/**
	 * With current trie, recursively produces list of words in this trie with 
	 * count > 0, in order
	 * @param curWord - necessary for recursive word tracking, starts at "" and builds up
	 * @return - list of sorted words
	 */
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

	/**
	 * static method for sorting given an array of strings - uses sorted() 
	 * @param toSort - array of strings to sort
	 * @return - sorted array of strings
	 */
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
	
	
	/**
	 * testing
	 * @param args
	 */
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
		System.out.println(t.getNode("hello").predecessor().getWord());
		
	}
}
