package pqueue.heaps; // ******* <---  DO NOT ERASE THIS LINE!!!! *******

/* *****************************************************************************************
 * THE FOLLOWING IMPORT IS NECESSARY FOR THE ITERATOR() METHOD'S SIGNATURE. FOR THIS
 * REASON, YOU SHOULD NOT ERASE IT! YOUR CODE WILL BE UNCOMPILABLE IF YOU DO!
 * ********************************************************************************** */

import java.util.Iterator;

import pqueue.exceptions.UnimplementedMethodException;

/**
 * <p>A {@link LinkedMinHeap} is a tree (specifically, a <b>complete</b> binary tree) where every node is
 * smaller than or equal to its descendants (as defined by the {@link Comparable#compareTo(Object)} overridings of the type T).
 * Percolation is employed when the root is deleted, and insertions guarantee maintenance of the heap property in logarithmic time. </p>
 *
 * <p>You <b>must</b> edit this class! To receive <b>any</b> credit for the unit tests related to this class,
 * your implementation <b>must</b> be a &quot;linked&quot;, <b>non-contiguous storage</b> implementation based on a
 * binary tree of nodes and references. Use the skeleton code we have provided to your advantage, but always remember
 * that the only functionality our tests can test is {@code public} functionality.</p>
 * 
 * @author Jonathan Hayden
 *
 * @param <T> The {@link Comparable} type of object held by {@code this}.
 *
 * @see MinHeap
 * @see ArrayMinHeap
 */
public class LinkedMinHeap<T extends Comparable<T>> implements MinHeap<T> {

	/* ***********************************************************************
	 * An inner class representing a minheap's node. YOU *SHOULD* BUILD YOUR *
	 * IMPLEMENTATION ON TOP OF THIS CLASS!                                  *
 	 * ********************************************************************* */
	private class MinHeapNode {
		private T data;
		private MinHeapNode lChild, rChild;

        /* *******************************************************************
         * Write any further data elements or methods for MinHeapNode here...*
         ********************************************************************* */
		private int index;
		private MinHeapNode parent;
		
		public MinHeapNode (T data) {
			this.data = data;
		}
	}

	/* *********************************
	  * Root of your tree: DO NOT ERASE!
	  * *********************************
	 */
	private MinHeapNode root;




    /* *********************************************************************************** *
     * Write any further private data elements or private methods for LinkedMinHeap here...*
     * *************************************************************************************/
	private int size;
	private MinHeapNode lastNode;

	private void percUp(MinHeapNode node) {
		T temp = null;
		
		if (node.parent != null) {
			if (node.data.compareTo(node.parent.data) < 0 || node.data.compareTo(node.parent.data) == 0) {
				if (root.data.compareTo(node.parent.data) == 0 && lastNode.data.compareTo(node.data) == 0) {
					T temp2 = root.data;
					root.data = node.data;
					lastNode.data = temp2;
				} else {
					/*if (root.data.compareTo(node.parent.data) == 0) {
						root.data = node.data;
					}
					
					if (lastNode.data.compareTo(node.data) == 0) {
						lastNode.data = node.parent.data;
					}*/
					
					temp = node.data;
					node.data = node.parent.data;
					node.parent.data = temp;
				}
				
				percUp(node.parent);
			}
		}
	}

	private void percDown(MinHeapNode node) {
		T temp = null;
		
		if (node.lChild != null && node.rChild != null) {
			if (node.lChild.data.compareTo(node.rChild.data) > 0) {
				if (node.rChild.data.compareTo(node.data) < 0) {
					if (root.data.compareTo(node.data) == 0) {
						root.data = node.rChild.data;
					}
					
					temp = node.data;
					node.data = node.rChild.data;
					node.rChild.data = temp;
					System.out.println("Root: " + root.data);
					percDown(node.rChild);
				}
			} else {
				if (node.lChild.data.compareTo(node.data) < 0) {
					if (root.data.compareTo(node.data) == 0) {
						root.data = node.lChild.data;
					}
					
					temp = node.data;
					node.data = node.lChild.data;
					node.lChild.data = temp;
					System.out.println("Root: " + root.data);
					percDown(node.lChild);
				}
			}
		} else if (node.lChild.data != null && node.rChild == null) {
			if (node.lChild.data.compareTo(node.data) < 0) {
				if (root.data.compareTo(node.data) == 0) {
					root.data = node.lChild.data;
				}
				
				temp = node.data;
				node.data = node.lChild.data;
				node.lChild.data = temp;
			}
		}
	}

	private void insertRecursive(MinHeapNode node, T element) {
		if (node.lChild == null) {
			node.lChild = new MinHeapNode(element);
			node.lChild.index = ((2 * node.index) + 1);
			node.lChild.parent = node;
			lastNode = node.lChild;
			size++;
		} else if (node.rChild == null) {
			node.rChild = new MinHeapNode(element);
			node.rChild.index = ((2 * node.index) + 2);
			node.rChild.parent = node;
			lastNode = node.rChild;
			size++;
		} else {
			if (node.lChild.lChild != null && node.lChild.rChild != null) {
				if (node.rChild.lChild != null && node.rChild.rChild != null) {
					insertRecursive(node.lChild, element);
				} else {
					insertRecursive(node.rChild, element);
				}
			} else {
				insertRecursive(node.lChild, element);
			}
		}
	}
	
	private void findLastNode(MinHeapNode node) {
		if (node.lChild != null ) {
			findLastNode(node.lChild);
		}
		
		if (node.index == lastNode.index - 1) {
			lastNode = node;
		}
		
		if (node.rChild != null) {
			findLastNode(node.rChild);
		}
	}




    /* *********************************************************************************************************
     * Implement the following public methods. You should erase the throwings of UnimplementedMethodExceptions.*
     ***********************************************************************************************************/

	/**
	 * Default constructor.
	 */
	public LinkedMinHeap() {
		root = new MinHeapNode(null);
		lastNode = new MinHeapNode(null);
		size = 0;
	}

	/**
	 * Second constructor initializes {@code this} with the provided element.
	 *
	 * @param rootElement the data to create the root with.
	 */
	public LinkedMinHeap(T rootElement) {
		root = new MinHeapNode(rootElement);
		lastNode = root;
		size = 1;
	}

	/**
	 * Copy constructor initializes {@code this} as a carbon
	 * copy of the parameter, which is of the general type {@link MinHeap}!
	 * Since {@link MinHeap} is an {@link Iterable} type, we can access all
	 * of its elements in proper order and insert them into {@code this}.
	 *
	 * @param other The {@link MinHeap} to copy the elements from.
	 */
	public LinkedMinHeap(MinHeap<T> other) {
		throw new UnimplementedMethodException();
	}


    /**
     * Standard {@code equals} method. We provide this for you. DO NOT EDIT!
     * You should notice how the existence of an {@link Iterator} for {@link MinHeap}
     * allows us to access the elements of the argument reference. This should give you ideas
     * for {@link #LinkedMinHeap(MinHeap)}.
     * @return {@code true} If the parameter {@code Object} and the current MinHeap
     * are identical Objects.
     *
     * @see Object#equals(Object)
     * @see #LinkedMinHeap(MinHeap)
     */
	/**
	 * Standard equals() method.
	 *
	 * @return {@code true} If the parameter Object and the current MinHeap
	 * are identical Objects.
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof MinHeap))
			return false;
		Iterator itThis = iterator();
		Iterator itOther = ((MinHeap) other).iterator();
		while (itThis.hasNext())
			if (!itThis.next().equals(itOther.next()))
				return false;
		return !itOther.hasNext();
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public int size() {
		return size;
	}


	@Override
	public void insert(T element) {
		if (size == 0) {
			root = new MinHeapNode(element);
			root.index = 0;
			lastNode = root;
			size++;
		} else {
			
			insertRecursive(root, element);
	
			percUp(lastNode);
		
		}
	}

	@Override
	public T getMin() throws EmptyHeapException {		// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (size == 0) {
			throw new EmptyHeapException("Heap is empty.");
		}
		return root.data;
	}

	@Override
	public T deleteMin() throws EmptyHeapException {    // DO *NOT* ERASE THE "THROWS" DECLARATION!
		
		if (size == 0) {
			throw new EmptyHeapException("Heap is empty.");
		} else if (size == 1) {
			size--;
			T min = root.data;
			root = null;
			lastNode = null;
			
			return min;
		}
		
		T min = root.data;
		root.data = lastNode.data;
		size--;
		System.out.println("Deleteing: " + min);
		System.out.println("New Root: " + root.data);
		
		/*if (lastNode.index % 2 == 0) {
			lastNode.parent.rChild = null;
			lastNode = lastNode.parent.lChild;
		} else {
			if (lastNode.parent.index % 2 == 0) {
				lastNode.parent.lChild = null;
				lastNode = lastNode.parent.parent.lChild.rChild;
			} else {
				lastNode.parent.lChild = null;
				lastNode = lastNode.parent.parent.rChild;
			}
		}*/
		
		findLastNode(root);
		
		percDown(root);
		return min;
	}

	@Override
	public Iterator<T> iterator() {
		/*return new Iterator<T>() {
			private int node = 0;
			private MinHeapNode current = root;

			public boolean hasNext() {
				return node < size;
			}

			public T next() {
				
			}
		};*/
		throw new UnimplementedMethodException();
	}

}
