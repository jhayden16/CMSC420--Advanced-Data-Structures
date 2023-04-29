package bpt;

import bpt.UnimplementedMethodException;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p>{@code BinaryPatriciaTrie} is a Patricia Trie over the binary alphabet &#123;	 0, 1 &#125;. By restricting themselves
 * to this small but terrifically useful alphabet, Binary Patricia Tries combine all the positive
 * aspects of Patricia Tries while shedding the storage cost typically associated with tries that
 * deal with huge alphabets.</p>
 *
 * @author YOUR NAME HERE!
 */
public class BinaryPatriciaTrie {

    /* We are giving you this class as an example of what your inner node might look like.
     * If you would prefer to use a size-2 array or hold other things in your nodes, please feel free
     * to do so. We can *guarantee* that a *correct* implementation exists with *exactly* this data
     * stored in the nodes.
     */
    private static class TrieNode {
        private TrieNode left, right;
        private String str;
        private boolean isKey;

        // Default constructor for your inner nodes.
        TrieNode() {
            this("", false);
        }

        // Non-default constructor.
        TrieNode(String str, boolean isKey) {
            left = right = null;
            this.str = str;
            this.isKey = isKey;
        }
    }

    private TrieNode root;
    private int keyCount;

    /**
     * Simple constructor that will initialize the internals of {@code this}.
     */
    public BinaryPatriciaTrie() {
        root = new TrieNode();
        keyCount = 0;
    }

    /**
     * Searches the trie for a given key.
     *
     * @param key The input {@link String} key.
     * @return {@code true} if and only if key is in the trie, {@code false} otherwise.
     */
    public boolean search(String key) {
    	return search(key, root);
    }

    /**
     * Inserts key into the trie.
     *
     * @param key The input {@link String}  key.
     * @return {@code true} if and only if the key was not already in the trie, {@code false} otherwise.
     */
    public boolean insert(String key) {
    	System.out.println("Insert(" + key + ")");
        return insert(key, root, null);
    }


    /**
     * Deletes key from the trie.
     *
     * @param key The {@link String}  key to be deleted.
     * @return {@code true} if and only if key was contained by the trie before we attempted deletion, {@code false} otherwise.
     */
    public boolean delete(String key) {
    	if (root.left == null && root.right == null) {
			return false;
		} else if (search(key) == false) {
			return false;
		}
    	
    	System.out.println("Delete(" + key + ")");
    	return delete(key, root, null);
    }

    /**
     * Queries the trie for emptiness.
     *
     * @return {@code true} if and only if {@link #getSize()} == 0, {@code false} otherwise.
     */
    public boolean isEmpty() {
        if (root.left == null && root.right == null) {
        	return true;
        }
        
        return false;
    }

    /**
     * Returns the number of keys in the tree.
     *
     * @return The number of keys in the tree.
     */
    public int getSize() {
    	return keyCount;
    }

    /**
     * <p>Performs an <i>inorder (symmetric) traversal</i> of the Binary Patricia Trie. Remember from lecture that inorder
     * traversal in tries is NOT sorted traversal, unless all the stored keys have the same length. This
     * is of course not required by your implementation, so you should make sure that in your tests you
     * are not expecting this method to return keys in lexicographic order. We put this method in the
     * interface because it helps us test your submission thoroughly and it helps you debug your code! </p>
     *
     * <p>We <b>neither require nor test </b> whether the {@link Iterator} returned by this method is fail-safe or fail-fast.
     * This means that you  do <b>not</b> need to test for thrown {@link java.util.ConcurrentModificationException}s and we do
     * <b>not</b> test your code for the possible occurrence of concurrent modifications.</p>
     *
     * <p>We also assume that the {@link Iterator} is <em>immutable</em>, i,e we do <b>not</b> test for the behavior
     * of {@link Iterator#remove()}. You can handle it any way you want for your own application, yet <b>we</b> will
     * <b>not</b> test for it.</p>
     *
     * @return An {@link Iterator} over the {@link String} keys stored in the trie, exposing the elements in <i>symmetric
     * order</i>.
     */
    public Iterator<String> inorderTraversal() {
    	System.out.println("**IOT**");
    	return new BPTIterator();

    }

    /**
     * Finds the longest {@link String} stored in the Binary Patricia Trie.
     * @return <p>The longest {@link String} stored in this. If the trie is empty, the empty string &quot;&quot; should be
     * returned. Careful: the empty string &quot;&quot;is <b>not</b> the same string as &quot; &quot;; the latter is a string
     * consisting of a single <b>space character</b>! It is also <b>not the same as the</b> null <b>reference</b>!</p>
     *
     * <p>Ties should be broken in terms of <b>value</b> of the bit string. For example, if our trie contained
     * only the binary strings 01 and 11, <b>11</b> would be the longest string. If our trie contained
     * only 001 and 010, <b>010</b> would be the longest string.</p>
     */
    public String getLongest() {
        return getLongest(root);
    }

    /**
     * Makes sure that your trie doesn't have splitter nodes with a single child. In a Patricia trie, those nodes should
     * be pruned.
     * @return {@code true} iff all nodes in the trie either denote stored strings or split into two subtrees, {@code false} otherwise.
     */
    public boolean isJunkFree(){
        return isEmpty() || (isJunkFree(root.left) && isJunkFree(root.right));
    }

    private boolean isJunkFree(TrieNode n){
        if(n == null){   // Null subtrees trivially junk-free
            return true;
        }
        if(!n.isKey){   // Non-key nodes need to be strict splitter nodes
            return ( (n.left != null) && (n.right != null) && isJunkFree(n.left) && isJunkFree(n.right) );
        } else {
            return ( isJunkFree(n.left) && isJunkFree(n.right) ); // But key-containing nodes need not.
        }
    }
    
    private class BPTIterator implements Iterator<String>  {
		private ArrayList<String> strList;
		private int index;
		
		public BPTIterator() {
			strList = new ArrayList<String>();
			addStrings(root, null, "");
			index = 0;
		}

		public void addStrings(TrieNode node, TrieNode prev, String current) {
			
			addStringsAux(node, current, strList);

		}
		
		@Override
		public boolean hasNext() {
			return index < strList.size();

		}

		@Override
		public String next() {
			System.out.println("Next: " + strList.get(index));
			String s =  strList.get(index);
			index++;
			
			return s;
		}
		
	}
    
    private void addStringsAux(TrieNode node, String current, ArrayList<String> list) {
    	if (node == null) {
    		return;
    	}
    	
    	if (node.left == null && node.right == null) {
    		if (node.isKey) {
    			list.add(current);
    		}
    	} else {
    		if (node.left != null) {
    			addStringsAux(node.left, current + node.left.str, list);
    		}
    		
    		if (node.isKey) {
    			list.add(current);
    		}
    		
    		if (node.right != null) {
    			addStringsAux(node.right, current + node.right.str, list);
    		}
    	}
    }
    
    private boolean search(String key, TrieNode node) {
    	if (node == null || this.isEmpty()) {
			return false;
		}
    	
    	if (node == root) {
    		if (key.charAt(0) == '0') {
    			return search(key, node.left);
    		} else {
    			return search(key, node.right);
    		}
    	}
    	
		if (key.equals(node.str)) {
			if (node.isKey) {
				return true;
			} else {
				return false;
			}
		} else {
			if (key.substring(0, node.str.length()).equals(node.str)) {		
				key = key.substring(node.str.length());
				
        		if (key.charAt(0) == '0') {
        			return search(key, node.left);
        		} else {
        			return search(key, node.right);
        		}
        	} else {
        		return false;
        	}
    	}
    }

    	
    private boolean insert(String key, TrieNode node, TrieNode prev) {
    	if (node == null) {
			if (key.charAt(0) == '0') {
				prev.left = new TrieNode(key, true);
				keyCount++;
				
				return true;
			} else {
				prev.right = new TrieNode(key, true);
				keyCount++;
				
				return true;
			}
		}
    	
    	if (node == root) {
    		if (key.charAt(0) == '0') {
    			return insert(key, node.left, node);
    		} else {
    			return insert(key, node.right, node);
    		}
    	}
    	
		if (key.equals(node.str)) {
			if (node.isKey == false) {
				node.isKey = true;
				keyCount++;
				
				return true;
			}
			
			return false;
		} else {		
			if (node.str.length() > key.length()) {
				TrieNode newNode = new TrieNode(key, true);
				node.str = node.str.substring(key.length());
				keyCount++;
				
				if (key.charAt(0) == '0') {
					prev.left = newNode;
				} else {
					prev.right = newNode;
				}
				
				if (node.str.charAt(0) == '0') {
					newNode.left = node;
				} else {
					newNode.right = node;
				}
				
				return true;
			} else {
				if (key.substring(0, node.str.length()).equals(node.str)) {		
					key = key.substring(node.str.length());
					
	        		if (key.charAt(0) == '0') {
	        			return insert(key, node.left, node);
	        		} else {
	        			return insert(key, node.right, node);
	        		}
	        	} else {
	        		int index = 0;
	        		
	        		if (key.length() == node.str.length()) {
	        			for (char c: key.toCharArray()) {
	        				if (c != node.str.charAt(index)) {
	        					break;
	        				}
	        				
	        				index++;
	        			}
	        		} else {
	        			String small, large;
	        			
	        			if (key.length() < node.str.length()) {
	        				small = key;
	        				large = node.str;
	        			} else {
	        				small = node.str;
	        				large = key;
	        			}
	        			
	        			for (char c: large.toCharArray()) {
	        				if (index == small.length() || c != node.str.charAt(index)) {
	        					break;
	        				}
	        				
	        				index++;
	        			}
	        		}
	        		
	        		String oldSuffix = node.str.substring(index);
	        		String newSuffix = key.substring(index);
	        		String prefix = key.substring(0, index);
	        		//node.isKey = false;
	        		
	        		TrieNode newParent = new TrieNode(prefix, false);
	        		if (prefix.charAt(0) == '0' ) {
	        			prev.left = newParent;
	        		} else {
	        			prev.right = newParent;
	        		}
	        		
	        		if (oldSuffix.charAt(0) == '0') {
	        			newParent.left = node;
	        			node.str = oldSuffix;
	        			newParent.right = new TrieNode(newSuffix, true);
	        		} else {
	        			newParent.left = new TrieNode(newSuffix, true);
	        			newParent.right = node;
	        			node.str = oldSuffix;
	        		}
	        		
	        		keyCount++;
	        		
	        		return true;
	        		
	        	}
			}
    	}
    }
    
    private boolean delete(String key, TrieNode node, TrieNode prev) {
    	if (node == null || this.isEmpty()) {
			return false;
		}
    	
    	if (node == root) {
    		if (key.charAt(0) == '0') {
    			return delete(key, node.left, node);
    		} else {
    			return delete(key, node.right, node);
    		}
    	}
    	
		if (key.equals(node.str)) {
			if (node.left != null && node.right != null) {
				node.isKey = false;
				keyCount--;
				
				return true;
			} else if (node.left != null) {
				node.left.str = key + node.left.str;
				
				if (key.charAt(0) == '0') {
					prev.left = node.left;
				} else {
					prev.right = node.left;
				}
				
				keyCount--;
				
				return true;
			} else if (node.right != null){
				node.right.str = key + node.right.str;
				
				if (key.charAt(0) == '0') {
					prev.left = node.right;
				} else {
					prev.right = node.right;
				}
				
				keyCount--;
				
				return true;
			} else {
				if (key.charAt(0) == '0') {
					prev.left = null;
					
					if (prev.isKey == false && prev != root) {
						prev.str = prev.str + prev.right.str;
						prev.isKey = prev.right.isKey;
						
						prev.left = prev.right.left;
						prev.right = prev.right.right;
					}
				} else {
					prev.right = null;
					
					if (prev.isKey == false && prev != root) {
						prev.str = prev.str + prev.left.str;
						prev.isKey = prev.left.isKey;
						
						prev.right = prev.left.right;
						prev.left = prev.left.left;
					}
				}
				
				keyCount--;
				
				return true;
			}
		} else {
			if (key.substring(0, node.str.length()).equals(node.str)) {		
				key = key.substring(node.str.length());
        		if (key.charAt(0) == '0') {
        			return delete(key, node.left, node);
        		} else {
        			return delete(key, node.right, node);
        		}
        	} else {
        		return false;
        	}
		}
    }
    
    private String getLongest(TrieNode node) {
    	BPTIterator iter = new BPTIterator();
    	String longest = "";
    	
    	for (String s : iter.strList) {
    		if (s.length() > longest.length()) {
    			longest = s;
    		}
    		
    		if (s.length() == longest.length()) {
    			if (s.compareTo(longest) > 0) {
    				longest = s;
    			}
    		}
    	}
    	
    	return longest;
    }
}
