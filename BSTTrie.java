package stringpredecessor;

import java.util.ArrayList;

public class BSTTrie {
	private BSTTrie parent;
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
	
	public BSTTrie() {
		children = new SelfBalancingBinarySearchTree();
		count = 0;
	}
	
	public BSTTrie getNode(String nodeName) {
		if(nodeName.length() == 0) {
			return this;
		}
		
		return getChild(charToInt(nodeName.charAt(0))).getNode(nodeName.substring(1));
	}
	
	void insert(String newString) {
		if(newString.length() == 0) {
			count++;
			//set predecessors and successors
			pred = predecessor();

			suc = successor();
			
			if(pred != null) {
				pred.setSuccessor(this);
			}
			if(suc != null) {
				suc.setPredecessor(this);
			}
		}
		else {
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
	
	public BSTTrie predecessor() {
		if(pred == null) {
			return recurseUp();
		}
		else {
			return pred;
		}
	}
	
	private BSTTrie recurseUp() {
		if(parent == null) {
			return null;
		}
		BSTTrie result;

		for(int i = charToInt(character)-1; i >=0; i--) {
			BSTTrie chil = parent.getChild(i);
			if(chil != null) {
				if(chil.count > 0) {
					return chil;
				}
				else {
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
	
	public void setSuccessor(BSTTrie newSuc) {
		suc = newSuc;
	}
	
	public void setPredecessor(BSTTrie newPred) {
		pred = newPred;
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
			BSTTrie chil = getChild(i);
			if(chil != null) {
				tempStrings.addAll(chil.sorted(curWord + character));
			}
		}
		return tempStrings;
	}

	public BSTTrie getChild(int i) {
		SBBSTNode find = children.searchAndReturn(i);
		BSTTrie chil = null;
		if(find != null) {
			chil = find.subTree;
		}
		return chil;
	}
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
