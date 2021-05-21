package stringpredecessor;

import java.util.ArrayList;

public class BSTTrie {
	private BSTTrie parent;
	//this time, we have a balanced binary tree as our representation. Changes some implementation details
	private SelfBalancingBinarySearchTree children;
	private BSTTrie pred;
	private BSTTrie suc;
	private int count;
	private char character;
	
	public BSTTrie(BSTTrie pnode, char newCharacter) {
		children = new SelfBalancingBinarySearchTree();
		parent = pnode;
		count = 0;
		character = newCharacter;
	}
	
	/**
	 * only for root
	 */
	public BSTTrie() {
		children = new SelfBalancingBinarySearchTree();
		count = 0;
	}
	
	/**
	 * quick way to get a node from name, recursive search
	 * @param nodeName - name of node you want
	 * @return - node you want
	 */
	public BSTTrie getNode(String nodeName) {
		if(nodeName.length() == 0) {
			return this;
		}
		
		return getChild(charToInt(nodeName.charAt(0))).getNode(nodeName.substring(1));
	}
	
	
	/**
	 * add a new string to the trie
	 * recursively adds new nodes as it goes until it reaches the final letter
	 * @param newString - String to be added
	 */
	void insert(String newString) {
		if(newString.length() == 0) {
			count++;
			//set predecessors and successors once we reached the last letter
			pred = predecessor();

			suc = successor();
			
			//Now, update adjacent nodes, if they exist
			if(pred != null) {
				pred.setSuccessor(this);
			}
			if(suc != null) {
				suc.setPredecessor(this);
			}
		}
		else {
			//recursive steps as we traverse through tree
			int ind = charToInt(newString.charAt(0));
			SBBSTNode childNode = children.searchAndReturn(ind);
			if(childNode == null) {
				children.insert(ind);
				childNode = children.searchAndReturn(ind);
				childNode.subTree = new BSTTrie(this, newString.charAt(0));
			}
			
			childNode.subTree.insert(newString.substring(1));
			
			

		}
		
	}
	
	/**
	 * gets the "predecessor", or the last string in the trie that has a count > 0 / has been inserted
	 * If you sort the strings, it would return the string before the string this node represents
	 * @return - node for the predecessor string
	 */
	public BSTTrie predecessor() {
		if(pred == null) {
			return recurseUp();
		}
		else {
			return pred;
		}
	}
	
	
	/**
	 * recursive function for use in predecessor. Traverses up the trie to find the closest node behind.
	 * Also calls recurseDown, which will explore the trees downwards on the left of this node. 
	 * We need the upwards and downwards recursion to properly explore tree and keep track of relative position to current node
	 * @return - the predecessor, or null if there is no predecessor
	 */
	private BSTTrie recurseUp() {
		if(parent == null) {
			return null;
		}
		BSTTrie result;
		
		for(int i = charToInt(character)-1; i >=0; i--) {
			//child loop
			BSTTrie chil = parent.getChild(i);
			if(chil != null) {
				if(chil.count > 0) {
					//found already
					return chil;
				}
				else {
					//if not found, recurse down to fully traverse left of trie
					result = chil.recurseDown();
					if(result != null) {
						return result;
					}
				}
			}
		}
		if(parent.count > 0) {
			return parent;
		}

		return parent.recurseUp();
	}
	
	
	/**
	 * the downwards recursion - finds any below current node, used from recurseUp
	 * looks from right to left since it is looking for predecessor
	 * @return - predecessor if found, null else
	 */
	private BSTTrie recurseDown() {
		BSTTrie t;
		for(int i = 25; i >=0; i--) {
			BSTTrie chil = getChild(i);
			if(chil != null) {
				t = chil.recurseDown();
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
	private BSTTrie recurseUpSuc() {
		if(parent == null) {
			return null;
		}
		BSTTrie result;
		for(int i = charToInt(character)+1; i < 26; i++) {
			BSTTrie chil = parent.getChild(i);
			if(chil != null) {
				if(chil.count > 0) {
					return chil;
				}
				else {
					result = chil.recurseDownSuc();
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
	private BSTTrie recurseDownSuc() {
		if(count > 0) {
			return this;
		}
		BSTTrie t;
		for(int i = 0; i < 26; i++) {
			BSTTrie chil = getChild(i);
			if(chil != null) {
				t = chil.recurseDownSuc();
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
	private BSTTrie recurseDownSpecial() {
		BSTTrie t;
		for(int i = 0; i < 26; i++) {
			BSTTrie chil = getChild(i);
			if(chil != null) {
				t = chil.recurseDownSuc();
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
	public BSTTrie successor() {
		if(suc == null) {
			BSTTrie trySelf = recurseDownSpecial();
			if(trySelf != null) {
				return trySelf;
			}
			return recurseUpSuc();
		}
		else {
			return suc;
		}
	}
	
	/**
	 * set for successor
	 * @param newSuc
	 */
	public void setSuccessor(BSTTrie newSuc) {
		suc = newSuc;
	}
	
	/**
	 * set for predecessor
	 * @param newPred
	 */
	public void setPredecessor(BSTTrie newPred) {
		pred = newPred;
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
			BSTTrie chil = getChild(i);
			if(chil != null) {
				tempStrings.addAll(chil.sorted(curWord + character));
			}
		}
		return tempStrings;
	}

	/**
	 * find index in the balanced BST
	 * @param i
	 * @return child of that index
	 */
	public BSTTrie getChild(int i) {
		SBBSTNode find = children.searchAndReturn(i);
		BSTTrie chil = null;
		if(find != null) {
			chil = find.subTree;
		}
		return chil;
	}
	
	/**
	 * static method for sorting given an array of strings - uses sorted() 
	 * @param toSort - array of strings to sort
	 * @return - sorted array of strings
	 */
	public static String[] sortStrings(String[] toSort) {
		BSTTrie t = new BSTTrie();
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
		
		
		BSTTrie to = new BSTTrie();
		for(String str : testStrings) {
			to.insert(str);
		}
		
		//to.insert("hello");
		System.out.println(to.getNode("hello").pred.getWord());
		
		
		
	}
}
