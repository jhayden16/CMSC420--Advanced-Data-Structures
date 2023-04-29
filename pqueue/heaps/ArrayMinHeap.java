package pqueue.heaps; // ******* <---  DO NOT ERASE THIS LINE!!!! *******

/* *****************************************************************************************
 * THE FOLLOWING IMPORT IS NECESSARY FOR THE ITERATOR() METHOD'S SIGNATURE. FOR THIS
 * REASON, YOU SHOULD NOT ERASE IT! YOUR CODE WILL BE UNCOMPILABLE IF YOU DO!
 * ********************************************************************************** */

import pqueue.exceptions.UnimplementedMethodException;

import java.util.Iterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;


/**
 * <p>{@link ArrayMinHeap} is a {@link MinHeap} implemented using an internal array. Since heaps are <b>complete</b>
 * binary trees, using contiguous storage to store them is an excellent idea, since with such storage we avoid
 * wasting bytes per {@code null} pointer in a linked implementation.</p>
 *
 * <p>You <b>must</b> edit this class! To receive <b>any</b> credit for the unit tests related to this class,
 * your implementation <b>must</b> be a <b>contiguous storage</b> implementation based on a linear {@link java.util.Collection}
 * like an {@link java.util.ArrayList} or a {@link java.util.Vector} (but *not* a {@link java.util.LinkedList} because it's *not*
 * contiguous storage!). or a raw Java array. We provide an array for you to start with, but if you prefer, you can switch it to a
 * {@link java.util.Collection} as mentioned above. </p>
 *
 * @author Jonathan Hayden
 *
 * @see MinHeap
 * @see LinkedMinHeap
 * @see demos.GenericArrays
 */

public class ArrayMinHeap<T extends Comparable<T>> implements MinHeap<T> {

	/* *****************************************************************************************************************
	 * This array will store your data. You may replace it with a linear Collection if you wish, but
	 * consult this class' 	 * JavaDocs before you do so. We allow you this option because if you aren't
	 * careful, you can end up having ClassCastExceptions thrown at you if you work with a raw array of Objects.
	 * See, the type T that this class contains needs to be Comparable with other types T, but Objects are at the top
	 * of the class hierarchy; they can't be Comparable, Iterable, Clonable, Serializable, etc. See GenericArrays.java
	 * under the package demos* for more information.
	 * *****************************************************************************************************************/
	private ArrayList<T> data;

	/* *********************************************************************************** *
	 * Write any further private data elements or private methods for LinkedMinHeap here...*
	 * *************************************************************************************/
	private T root;
	private int size;
	private int mods;
	
	private void percDown(int index) {
		int minNode = index;
		int left = ((2 * index) + 1);
		int right = ((2 * index) + 2);
		
		if (left < size && data.get(left).compareTo(data.get(index)) < 0) {
			minNode = left;
		}
		
		if (right < size && data.get(right).compareTo(data.get(minNode)) < 0) {
			minNode = right;
		}
		
		if (minNode != index) {
			Collections.swap(data, index, minNode);
			percDown(minNode);
		}
	}


	/* *********************************************************************************************************
	 * Implement the following public methods. You should erase the throwings of UnimplementedMethodExceptions.*
	 ***********************************************************************************************************/

	/**
	 * Default constructor initializes the data structure with some default
	 * capacity you can choose.
	 */
	public ArrayMinHeap(){
		data = new ArrayList<T>();
		root = null;
		size = 0;
		mods = 0;
	}

	/**
	 *  Second, non-default constructor which provides the element with which to initialize the heap's root.
	 *  @param rootElement the element to create the root with.
	 */
	public ArrayMinHeap(T rootElement){
		data = new ArrayList<T>();
		root = rootElement;
		data.add(root);
		size = 1;
		mods = 1;
	}

	/**
	 * Copy constructor initializes {@code this} as a carbon copy of the {@link MinHeap} parameter.
	 *
	 * @param other The MinHeap object to base construction of the current object on.
	 */
	public ArrayMinHeap(MinHeap<T> other){
		throw new UnimplementedMethodException();
	}

	/**
	 * Standard {@code equals()} method. We provide it for you: DO NOT ERASE! Consider its implementation when implementing
	 * {@link #ArrayMinHeap(MinHeap)}.
	 * @return {@code true} if the current object and the parameter object
	 * are equal, with the code providing the equality contract.
	 * @see #ArrayMinHeap(MinHeap)
	 */
	@Override
	public boolean equals(Object other){
		if(other == null || !(other instanceof MinHeap))
			return false;
		Iterator itThis = iterator();
		Iterator itOther = ((MinHeap) other).iterator();
		while(itThis.hasNext())
			if(!itThis.next().equals(itOther.next()))
				return false;
		return !itOther.hasNext();
	}


	@Override
	public void insert(T element) {
		data.add(element);
		size++;
		mods++;
		
		if (root == null) {
			root = element;
		} else {
			while (element.compareTo(data.get(((data.indexOf(element) - 1) / 2))) < 0) {
				Collections.swap(data, data.indexOf(element), ((data.indexOf(element) - 1) / 2));
			}
			
			if (data.get(0).equals(element)) {
				root = element;
			}
		}
	}

	@Override
	public T deleteMin() throws EmptyHeapException { // DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (size == 0) {
			throw new EmptyHeapException("Heap is empty.");
		} else if (size == 1) {
			size--;
			mods++;
			return data.remove(0);
		}
		
		T min = root;
		data.set(0, data.get(size - 1));
		data.remove(size - 1);
		size--;
		mods++;
		
		percDown(0);
		root = data.get(0);
		
		return min;
	}

		@Override
	public T getMin() throws EmptyHeapException {	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (size == 0) {
			throw new EmptyHeapException("Heap is empty.");
		}
		
		return root;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		if (size == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Standard equals() method.
	 * @return {@code true} if the current object and the parameter object
	 * are equal, with the code providing the equality contract.
	 */


	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private int index = 0;
			private T current = root;
			private int iterMods = mods;
			
			public boolean hasNext() {
				if (iterMods != mods) {
					throw new ConcurrentModificationException();
				} else {
					return index < size;
				}
			}

			public T next() {
				if (iterMods != mods) {
					throw new ConcurrentModificationException();
				} else {
					index++;
					current = data.get(index);
					return current;
				}
			}
		};
	}

}
