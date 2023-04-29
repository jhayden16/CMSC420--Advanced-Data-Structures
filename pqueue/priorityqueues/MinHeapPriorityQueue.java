package pqueue.priorityqueues; // ******* <---  DO NOT ERASE THIS LINE!!!! *******


/* *****************************************************************************************
 * THE FOLLOWING IMPORTS WILL BE NEEDED BY YOUR CODE, BECAUSE WE REQUIRE THAT YOU USE
 * ANY ONE OF YOUR EXISTING MINHEAP IMPLEMENTATIONS TO IMPLEMENT THIS CLASS. TO ACCESS
 * YOUR MINHEAP'S METHODS YOU NEED THEIR SIGNATURES, WHICH ARE DECLARED IN THE MINHEAP
 * INTERFACE. ALSO, SINCE THE PRIORITYQUEUE INTERFACE THAT YOU EXTEND IS ITERABLE, THE IMPORT OF ITERATOR
 * IS NEEDED IN ORDER TO MAKE YOUR CODE COMPILABLE. THE IMPLEMENTATIONS OF CHECKED EXCEPTIONS
 * ARE ALSO MADE VISIBLE BY VIRTUE OF THESE IMPORTS.
 ** ********************************************************************************* */

import pqueue.exceptions.*;
import pqueue.heaps.ArrayMinHeap;
import pqueue.heaps.EmptyHeapException;
import pqueue.heaps.MinHeap;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
/**
 * <p>{@link MinHeapPriorityQueue} is a {@link PriorityQueue} implemented using a {@link MinHeap}.</p>
 *
 * <p>You  <b>must</b> implement the methods of this class! To receive <b>any credit</b> for the unit tests
 * related to this class, your implementation <b>must</b> use <b>whichever</b> {@link MinHeap} implementation
 * among the two that you should have implemented you choose!</p>
 *
 * @author  Jonathan Hayden
 *
 * @param <T> The Type held by the container.
 *
 * @see LinearPriorityQueue
 * @see MinHeap
 * @see PriorityQueue
 */
public class MinHeapPriorityQueue<T> implements PriorityQueue<T>{

	/* ***********************************************************************************
	 * Write any private data elements or private methods for MinHeapPriorityQueue here...*
	 * ***********************************************************************************/
	private ArrayMinHeap<PQElement> pqueue;
	private PQElement root;
	private int size;
	private int mods;
	
	private class PQElement implements Comparable<PQElement>{
		private T data;
		private int priority;
		
		private PQElement(T data, int priority) {
			this.data = data;
			this.priority = priority;
		}
		
		@Override
		public int compareTo(PQElement element) {
			return this.priority - element.priority;
		}
	
	}




	/* *********************************************************************************************************
	 * Implement the following public methods. You should erase the throwings of UnimplementedMethodExceptions.*
	 ***********************************************************************************************************/
		/**
	 * Simple default constructor.
	 */
	public MinHeapPriorityQueue(){
		pqueue = new ArrayMinHeap<PQElement>();
		root = null;
		size = 0;
		mods = 0;
	}

	@Override
	public void enqueue(T element, int priority) throws InvalidPriorityException {	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (priority <= 0) {
			throw new InvalidPriorityException("Invalid priority.");
		}
		
		PQElement newElement = new PQElement(element, priority);
		
		pqueue.insert(newElement);
		size++;
		mods++;
	}

	@Override
	public T dequeue() throws EmptyPriorityQueueException {		// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (size == 0) {
			throw new EmptyPriorityQueueException("Priority queue is empty.");
		}
		
		T min = null;
		
		try {
			min = pqueue.deleteMin().data;
		} catch (EmptyHeapException e) {
			System.out.println("Heap is empty.");
		}
		
		size--;
		mods++;
		
		return min;
	}

	@Override
	public T getFirst() throws EmptyPriorityQueueException {	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (size == 0) {
			throw new EmptyPriorityQueueException("Priority queue is empty.");
		}
		
		T first = null;
		
		try {
			first = pqueue.getMin().data;
		} catch (EmptyHeapException e) {
			System.out.println("Heap is empty.");
		}
		
		return first;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return pqueue.isEmpty();
	}


	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private int iterMods = mods;
			
			public boolean hasNext() {
				if (iterMods != mods) {
					throw new ConcurrentModificationException();
				} else {
					return pqueue.iterator().hasNext();
				}
			}

			public T next() {
				if (iterMods != mods) {
					throw new ConcurrentModificationException();
				} else {
					return pqueue.iterator().next().data;
				}
			}
		};
	}

}
