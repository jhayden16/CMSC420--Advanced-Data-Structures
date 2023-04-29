package avlg;

import avlg.exceptions.UnimplementedMethodException;
import avlg.exceptions.EmptyTreeException;
import avlg.exceptions.InvalidBalanceException;
import java.lang.Math;

/** <p>{@link AVLGTree}  is a class representing an <a href="https://en.wikipedia.org/wiki/AVL_tree">AVL Tree</a> with
 * a relaxed balance condition. Its constructor receives a strictly  positive parameter which controls the <b>maximum</b>
 * imbalance allowed on any subtree of the tree which it creates. So, for example:</p>
 *  <ul>
 *      <li>An AVL-1 tree is a classic AVL tree, which only allows for perfectly balanced binary
 *      subtrees (imbalance of 0 everywhere), or subtrees with a maximum imbalance of 1 (somewhere). </li>
 *      <li>An AVL-2 tree relaxes the criteria of AVL-1 trees, by also allowing for subtrees
 *      that have an imbalance of 2.</li>
 *      <li>AVL-3 trees allow an imbalance of 3.</li>
 *      <li>...</li>
 *  </ul>
 *
 *  <p>The idea behind AVL-G trees is that rotations cost time, so maybe we would be willing to
 *  accept bad search performance now and then if it would mean less rotations. On the other hand, increasing
 *  the balance parameter also means that we will be making <b>insertions</b> faster.</p>
 *
 * @author YOUR NAME HERE!
 *
 * @see EmptyTreeException
 * @see InvalidBalanceException
 * @see StudentTests
 */
public class AVLGTree<T extends Comparable<T>> {

    /* ********************************************************* *
     * Write any private data elements or private methods here...*
     * ********************************************************* */
	private class AVLGNode {
		private AVLGNode left;
		private AVLGNode right;
		private T key;
		private int height;
		
		private AVLGNode(T key) {
			this.key = key;
			if (key == null) {
				height = -1;
			} else {
				height = 0;
			}
		}
	}
	private AVLGNode root;
	private int maxImbalance;
	private int count;
	
	private AVLGNode rotateLeft(AVLGNode target) {
		AVLGNode temp;
		temp = target.right;
		target.right = temp.left;
		temp.left = target;
		
		target.height = height(target);
		
		return temp;
	}
	
	private AVLGNode rotateRight(AVLGNode target) {
		AVLGNode temp;
		temp = target.left;
		target.left = temp.right;
		temp.right = target;
		
		target.height = height(target);
		
		return temp;
	}
	
	private AVLGNode rotateLeftRight(AVLGNode target) {
		target.left = rotateLeft(target.left);
		target = rotateRight(target);
		
		target.height = height(target);
		
		return target;
	}
	
	private AVLGNode rotateRightLeft(AVLGNode target) {
		target.right = rotateRight(target.right);
		target = rotateLeft(target);
		
		target.height = height(target);
		
		return target;
	}
	
	private AVLGNode rotation(AVLGNode node, int balance) {
		if(Math.abs(balance) > maxImbalance) {
			if (balance < 0) {
				if (balance(node.right) > 0) {				
					node = rotateRightLeft(node);
					node.height = height(node);
				} else {				
					node = rotateLeft(node);
					node.height = height(node);
				}
			} else {
				if (balance(node.left) < 0) {				
					node = rotateLeftRight(node);
					node.height = height(node);
				} else {				
					node = rotateRight(node);
					node.height = height(node);
				}
			}
		}
		
		return node;
	}
	
	private AVLGNode recursiveInsert(AVLGNode node, AVLGNode newNode) {
		if (node != null) {
			if (newNode.key.compareTo(node.key) < 0) {
				node.left = recursiveInsert(node.left, newNode);
				node.height = height(node);
				if (Math.abs(balance(node)) > maxImbalance) {
					if (newNode.key.compareTo(node.left.key) < 0) {
						node = rotateRight(node);
					} else {
						node = rotateLeftRight(node);
					}
				}
			} else {
				node.right = recursiveInsert(node.right, newNode);
				node.height = height(node);
				if (Math.abs(balance(node)) > maxImbalance) {
					if (newNode.key.compareTo(node.right.key) > 0) {
						node = rotateLeft(node);
					} else {
						node = rotateRightLeft(node);
					}
				}
			}
			
			return node;
		} else {
			return newNode;
		}
	}
	
	private int height(AVLGNode node) {
		if (node == null) {
			return -1;
		} else if (node.left == null && node.right == null) {
			return 0;
		}
		
		int leftHeight = height(node.left);	
		int rightHeight = height(node.right);
		
		return 1 + Math.max(leftHeight, rightHeight);
	}
	
	private int balance(AVLGNode node) {
		if (node.left != null && node.right != null) {
			return node.left.height - node.right.height;
		} else if (node.left != null && node.right == null) {
			return node.left.height - (-1);
		} else if (node.left == null && node.right != null) {
			return (-1) - node.right.height;
		} else {
			return 0;
		}
	}
	
	private boolean recursiveBST(AVLGNode node) {
		//System.out.println("BST " + node.key);
		if (node.left != null && node.left.key.compareTo(node.key) < 0) {
			if (node.right != null && node.right.key.compareTo(node.key) > 0) {
				return recursiveBST(node.right);
			}
			
			return recursiveBST(node.left);
		} else if (node.right != null && node.right.key.compareTo(node.key) > 0) {
			return recursiveBST(node.right);
		} else if (node.left == null && node.right == null){
			return true;
		}
			
		return false;
		
	}
	
	private boolean recursiveAVLGBalanced(AVLGNode node) {
		if (node == null) {
			return true;
		}
		
		//System.out.println("AVLGBalanced " + node.key);
		
		if (balance(node) <= maxImbalance) {
			return recursiveAVLGBalanced(node.left) && recursiveAVLGBalanced(node.right);
		} else {
			return false;
		}
	}
	
	private AVLGNode inOrderSuccessor(AVLGNode node) {
		//AVLGNode target = node;
		if (node.right == null) {
			return null;
		}
		
		AVLGNode target = node.right;
		
		while (target.left != null ) {
			target = target.left;
		}
		
		return target;
		
		/*while (target.left != null || target.right != null) {
			if (target.right != null) {
				target = target.right;
			} else {
				target = target.left;
			}
		}
		
		return target;*/
	}
	
	private AVLGNode recursiveDeleteMain(AVLGNode node, T key) {
		AVLGNode successor;
        T target = null;
        //AVLGNode targetNode = new AVLGNode(target);
        
        if (key.compareTo(node.key) < 0) {
        	if (node.left != null && key.compareTo(node.left.key) == 0) {
        		// delete left child
        		target = node.left.key;        		
        		if (node.left.left == null) {
        			node.left = node.left.right;
        			count--;
        		} else if (node.left.right == null) {
        			node.left = node.left.left;
        			count--;
        		} else {
        			// in order successor
        			successor = inOrderSuccessor(node.left);
        			target = node.left.key;
        			node.left.key = recursiveDeleteAux(node.left, successor.key);
        			node.left = rotation(node.left, balance(node.left));
        		}
        	} else {
        		// call delete main on left child
        		node.left = recursiveDeleteMain(node.left, key);
        	}
        } else if (key.compareTo(node.key) > 0) {
        	if (node.right != null && key.compareTo(node.right.key) == 0) {
        		// delete right child
        		target = node.right.key;
        		if (node.right.left == null) {
        			node.right = node.right.right;
        			count--;
        		} else if (node.right.right == null) {
        			node.right = node.right.left;
        			count--;
        		} else {
        			// in order successor
        			successor = inOrderSuccessor(node.right);
        			target = node.right.key;
        			node.right.key = recursiveDeleteAux(node.right, successor.key);
        			node.right = rotation(node.right, balance(node.right));
        		}
        	} else {
        		// call delete main on right child
        		node.right = recursiveDeleteMain(node.right, key);
        	}
        } else {
        	target = node.key;
        	if (node.right == null) {
        		node = node.left;
        		count--;
        	} else if (node.left == null) {
        		node = node.right;
        		count--;
        	} else {
        		successor = inOrderSuccessor(node);
        		target = node.key;
        		node.key = recursiveDeleteAux(node, successor.key);
        	}
        	
        	root = node;
        }
		
		node.height = height(node);
		
		int balance = balance(node);
		
		if (root.key.compareTo(node.key) == 0) {
			root = rotation(node, balance);
		} else {
			node = rotation(node, balance);
		}
		
		/*if(Math.abs(balance) > maxImbalance) {
			if (balance < 0) {
				if (balance(node.right) > 0) {
					if (root.key.compareTo(node.key) == 0) {
						root = rotateRightLeft(node);
						root.height = height(root);
					} else {				
						node = rotateRightLeft(node);
						node.height = height(node);
					}
				} else {
					if (root.key.compareTo(node.key) == 0) {
						root = rotateLeft(node);
						root.height = height(root);
					} else {				
						node = rotateLeft(node);
						node.height = height(node);
					}
				}
			} else {
				if (balance(node.left) < 0) {
					if (root.key.compareTo(node.key) == 0) {
						root = rotateLeftRight(node);
						root.height = height(root);
					} else {				
						node = rotateLeftRight(node);
						node.height = height(node);
					}
				} else {
					if (root.key.compareTo(node.key) == 0) {
						root = rotateRight(node);
						root.height = height(root);
					} else {				
						node = rotateRight(node);
						node.height = height(node);
					}
				}
			}
		}*/
		
		return node;
	}
	
	private T recursiveDeleteAux(AVLGNode node, T key) {	
		if (key.compareTo(node.key) < 0) {
			if (key.compareTo(node.left.key) == 0) {
				if (node.left.left != null)  {
					node.left = node.left.left;
				} else {
					node.left = node.left.right;
				}
				node.height = height(node);
				count--;
			} else {
				recursiveDeleteAux(node.left, key);
			}
		} else {
			if (key.compareTo(node.right.key) == 0) {
				if (node.right.left != null)  {
					node.right = node.right.left;
				} else {
					node.right = node.right.right;
				}
				node.height = height(node);
				count--;
			} else {
				recursiveDeleteAux(node.right, key);
			}
		}
		
		return key;
	}


    /* ******************************************************** *
     * ************************ PUBLIC METHODS **************** *
     * ******************************************************** */

    /**
     * The class constructor provides the tree with the maximum imbalance allowed.
     * @param maxImbalance The maximum imbalance allowed by the AVL-G Tree.
     * @throws InvalidBalanceException if maxImbalance is a value smaller than 1.
     */
    public AVLGTree(int maxImbalance) throws InvalidBalanceException {
        if (maxImbalance < 1) {
        	throw new InvalidBalanceException("Invalid balance.");
        } else {
        	root = new AVLGNode(null);
        	this.maxImbalance = maxImbalance;
        }
    }

    /**
     * Insert key in the tree. You will <b>not</b> be tested on
     * duplicates! This means that in a deletion test, any key that has been
     * inserted and subsequently deleted should <b>not</b> be found in the tree!
     * s
     * @param key The key to insert in the tree.
     */
    public void insert(T key) {
        if (root.key == null) {
        	root.key = key;
        	root.height++;
        	System.out.println("NEW TREE: " + maxImbalance);
        } else {
        	AVLGNode newNode = new AVLGNode(key);
        	root = recursiveInsert(root, newNode);
        }
        
        //System.out.println("tree.insert(" + key + "); " + maxImbalance);
        count++;
    }

    /**
     * Delete the key from the data structure and return it to the caller.
     * @param key The key to delete from the structure.
     * @return The key that was removed, or {@code null} if the key was not found.
     * @throws EmptyTreeException if the tree is empty.
     */
    public T delete(T key) throws EmptyTreeException {
        if (this.isEmpty()) {
        	throw new EmptyTreeException("Tree is empty.");
        } else if (count == 1) {
        	T temp = root.key;
        	
        	System.out.println("tree.delete(" + key + "): " + maxImbalance);
        	
        	root.height = -1;
        	root.left = null;
        	root.right = null;
        	root.key = null;
        	count--;
        	
        	return temp;
        }
        
        System.out.println("tree.delete(" + key + "); " + maxImbalance);
        
        if (recursiveDeleteMain(root, key) == null) {
        	return null;
        }
        
        return key;
        //return recursiveDeleteMain(root, key).key;
        
    }

    /**
     * <p>Search for key in the tree. Return a reference to it if it's in there,
     * or {@code null} otherwise.</p>
     * @param key The key to search for.
     * @return key if key is in the tree, or {@code null} otherwise.
     * @throws EmptyTreeException if the tree is empty.
     */
    public T search(T key) throws EmptyTreeException {
    	//System.out.println("Searching for " + key);
    	if (this.isEmpty()) {
    		throw new EmptyTreeException("Tree is empty.");
    	}
    	
        AVLGNode current = root;
        
        while (current.key != key) {
        	if (key.compareTo(current.key) < 0) {
        		if (current.left != null) {
        			current = current.left;
        		} else {
        			//System.out.println("Found " + null);
        			return null;
        		}
        	} else {
        		if (current.right != null) {
        			current = current.right;
        		} else {
        			//System.out.println("Found " + null);
        			return null;
        		}
       
        	}
        }
        
        //System.out.println("Found " + current.key);
        return current.key;
    }

    /**
     * Retrieves the maximum imbalance parameter.
     * @return The maximum imbalance parameter provided as a constructor parameter.
     */
    public int getMaxImbalance(){
        return maxImbalance;

    }
    /**
     * <p>Return the height of the tree. The height of the tree is defined as the length of the
     * longest path between the root and the leaf level. By definition of path length, a
     * stub tree has a height of 0, and we define an empty tree to have a height of -1.</p>
     * @return The height of the tree. If the tree is empty, returns -1.
     */
    public int getHeight() {
    	if (root.key == null) {
    		return -1;
    	} else {
    		return height(root);
    	}
    }

    /**
     * Query the tree for emptiness. A tree is empty iff it has zero keys stored.
     * @return {@code true} if the tree is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
    	if (root.key == null) {
    		return true;
    	} else {
    		return false;
    	}
    }

    /**
     * Return the key at the tree's root node.
     * @return The key at the tree's root node.
     * @throws  EmptyTreeException if the tree is empty.
     */
    public T getRoot() throws EmptyTreeException{
    	if (root.key == null) {
    		throw new EmptyTreeException("Tree is empty.");
    	} else {
    		return root.key;
    	}
    }


    /**
     * <p>Establishes whether the AVL-G tree <em>globally</em> satisfies the BST condition. This method is
     * <b>terrifically useful for testing!</b></p>
     * @return {@code true} if the tree satisfies the Binary Search Tree property,
     * {@code false} otherwise.
     */
    public boolean isBST() {
        if (root.key == null) {
        	return true;
        } else {
        	return recursiveBST(root);
        }
    }


    /**
     * <p>Establishes whether the AVL-G tree <em>globally</em> satisfies the AVL-G condition. This method is
     * <b>terrifically useful for testing!</b></p>
     * @return {@code true} if the tree satisfies the balance requirements of an AVLG tree, {@code false}
     * otherwise.
     */
    public boolean isAVLGBalanced() {
       if (root.key == null) {
    	   return true;
       } else {
    	   return recursiveAVLGBalanced(root);
       }
    }

    /**
     * <p>Empties the AVL-G Tree of all its elements. After a call to this method, the
     * tree should have <b>0</b> elements.</p>
     */
    public void clear(){
    	System.out.println("Clearing tree.");
    	root.key = null;
    	root.left = null;
    	root.right = null;
    	root.height = -1;
    	count = 0;
    }


    /**
     * <p>Return the number of elements in the tree.</p>
     * @return  The number of elements in the tree.
     */
    public int getCount(){
    	return count;
    }
}
