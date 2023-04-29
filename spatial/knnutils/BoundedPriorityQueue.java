package spatial.knnutils;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import spatial.exceptions.UnimplementedMethodException;


/**
 * <p>{@link BoundedPriorityQueue} is a priority queue whose number of elements
 * is bounded. Insertions are such that if the queue's provided capacity is surpassed,
 * its length is not expanded, but rather the maximum priority element is ejected
 * (which could be the element just attempted to be enqueued).</p>
 *
 * <p><b>YOU ***** MUST ***** IMPLEMENT THIS CLASS!</b></p>
 *
 * @author  <a href = "https://github.com/jasonfillipou/">Jason Filippou</a>
 *
 * @see PriorityQueue
 * @see PriorityQueueNode
 */
public class BoundedPriorityQueue<T> implements PriorityQueue<T>{

	/* *********************************************************************** */
	/* *************  PLACE YOUR PRIVATE FIELDS AND METHODS HERE: ************ */
	/* *********************************************************************** */
	
	private ArrayList<BQElement> pqueue;
	
	private class BQElement {
		private T data;
		private double priority;
		
		private BQElement(T data, double priority) {
			this.data = data;
			this.priority = priority;
		}
	}
	
	private int capacity;
	private int mods;

	/* *********************************************************************** */
	/* ***************  IMPLEMENT THE FOLLOWING PUBLIC METHODS:  ************ */
	/* *********************************************************************** */

	/**
	 * Constructor that specifies the size of our queue.
	 * @param size The static size of the {@link BoundedPriorityQueue}. Has to be a positive integer.
	 * @throws IllegalArgumentException if size is not a strictly positive integer.
	 */
	public BoundedPriorityQueue(int size) throws IllegalArgumentException{
		pqueue = new ArrayList<BQElement>();
		
		if (size <= 0) {
			throw new IllegalArgumentException("Size must be a strictly positive integer.");
		}
		
		capacity = size;
		mods = 0;
	}

	/**
	 * <p>Enqueueing elements for BoundedPriorityQueues works a little bit differently from general case
	 * PriorityQueues. If the queue is not at capacity, the element is inserted at its
	 * appropriate location in the sequence. On the other hand, if the object is at capacity, the element is
	 * inserted in its appropriate spot in the sequence (if such a spot exists, based on its priority) and
	 * the maximum priority element is ejected from the structure.</p>
	 * 
	 * @param element The element to insert in the queue.
	 * @param priority The priority of the element to insert in the queue.
	 */
	@Override
	public void enqueue(T element, double priority) {
		BQElement newElement = new BQElement(element, priority);
		int qSize = pqueue.size();
		for(int i = 0; i < pqueue.size(); i++) {
			if (pqueue.get(i).priority > priority) {
				pqueue.add(i, newElement);
				capacity--;
				mods++;
				break;
			}
		}
		
		if (pqueue.size() == qSize) {
			pqueue.add(newElement);
			capacity--;
			mods++;
		}
		
		if (capacity < 0) {
			pqueue.remove(pqueue.size()-1);
			capacity = 0;
		}
	}

	@Override
	public T dequeue() {
		if (pqueue.size() != 0) {
			T oldData = pqueue.get(0).data;
			pqueue.remove(0);
			capacity++;
			mods++;
			
			return oldData;
		} else {
			return null;
		}
	}

	@Override
	public T first() {
		if (this.isEmpty()) {
			return null;
		} else {
			return pqueue.get(0).data;
		}
	}
	
	/**
	 * Returns the last element in the queue. Useful for cases where we want to 
	 * compare the priorities of a given quantity with the maximum priority of 
	 * our stored quantities. In a minheap-based implementation of any {@link PriorityQueue},
	 * this operation would scan O(n) nodes and O(nlogn) links. In an array-based implementation,
	 * it takes constant time.
	 * @return The maximum priority element in our queue, or null if the queue is empty.
	 */
	public T last() {
		if (this.isEmpty()) {
			return null;
		} else {
			return pqueue.get(pqueue.size()-1).data;
		}
	}

	/**
	 * Inspects whether a given element is in the queue. O(N) complexity.
	 * @param element The element to search for.
	 * @return {@code true} iff {@code element} is in {@code this}, {@code false} otherwise.
	 */
	public boolean contains(T element) {
		for(BQElement currElement : pqueue){
			if (currElement.data.equals(element)){
				return true;
			}
		}
		return false;
	}

	@Override
	public int size() {
		return pqueue.size();
	}

	@Override
	public boolean isEmpty() {
		if (pqueue.size() == 0) {
			return true;
		}
		
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<>(){
			int index = 0;
			int modCount = mods;
			
			@Override
			public boolean hasNext() {
				if (modCount != mods) {
					throw new ConcurrentModificationException();
				} else {
					return (index < pqueue.size());
				}
			}
			@Override
			public T next() {
				if (modCount != mods) {
					throw new ConcurrentModificationException();
				} else {
					index++;
					return pqueue.get(index-1).data;
				}
		}

		};
	}
}
