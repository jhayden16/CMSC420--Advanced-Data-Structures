package pqueue.priorityqueues; // ******* <---  DO NOT ERASE THIS LINE!!!! *******

/* *****************************************************************************************
 * THE FOLLOWING IMPORTS ARE HERE ONLY TO MAKE THE JAVADOC AND iterator() METHOD SIGNATURE
 * "SEE" THE RELEVANT CLASSES. SOME OF THOSE IMPORTS MIGHT *NOT* BE NEEDED BY YOUR OWN
 * IMPLEMENTATION, AND IT IS COMPLETELY FINE TO ERASE THEM. THE CHOICE IS YOURS.
 * ********************************************************************************** */

import demos.GenericArrays;
import pqueue.exceptions.*;
import pqueue.fifoqueues.FIFOQueue;
import pqueue.heaps.ArrayMinHeap;

import java.util.*;
/**
 * <p>{@link LinearPriorityQueue} is a {@link PriorityQueue} implemented as a linear {@link java.util.Collection}
 * of common {@link FIFOQueue}s, where the {@link FIFOQueue}s themselves hold objects
 * with the same priority (in the order they were inserted).</p>
 *
 * <p>You  <b>must</b> implement the methods in this file! To receive <b>any credit</b> for the unit tests related to
 * this class, your implementation <b>must</b>  use <b>whichever</b> linear {@link Collection} you want (e.g
 * {@link ArrayList}, {@link LinkedList}, {@link java.util.Queue}), or even the various {@link List} and {@link FIFOQueue}
 * implementations that we provide for you. You can also use <b>raw</b> arrays, but take a look at {@link GenericArrays}
 * if you intend to do so. Note that, unlike {@link ArrayMinHeap}, we do not insist that you use a contiguous storage
 * {@link Collection}, but any one available (including {@link LinkedList}) </p>
 *
 * @param <T> The type held by the container.
 *
 * @author  Jonathan Hayden
 *
 * @see MinHeapPriorityQueue
 * @see PriorityQueue
 * @see GenericArrays
 */
public class LinearPriorityQueue<T> implements PriorityQueue<T> {

	/* ***********************************************************************************
	 * Write any private data elements or private methods for LinearPriorityQueue here...*
	 * ***********************************************************************************/
	private LinkedList<PQElement> pqueue;
	
	private class PQElement {
		private T data;
		private int priority;
		private PQElement next;
		
		private PQElement(T data, int priority) {
			this.data = data;
			this.priority = priority;
		}
	}
	
	private int capacity;
	private int size;
	private int mods;

	


	/* *********************************************************************************************************
	 * Implement the following public methods. You should erase the throwings of UnimplementedMethodExceptions.*
	 ***********************************************************************************************************/

	/**
	 * Default constructor initializes the element structure with
	 * a default capacity. This default capacity will be the default capacity of the
	 * underlying element structure that you will choose to use to implement this class.
	 */
	public LinearPriorityQueue(){
		pqueue = new LinkedList<PQElement>();
		capacity = 999;
		size = 0;
		mods = 0;
	}

	/**
	 * Non-default constructor initializes the element structure with
	 * the provided capacity. This provided capacity will need to be passed to the default capacity
	 * of the underlying element structure that you will choose to use to implement this class.
	 * @see #LinearPriorityQueue()
	 * @param capacity The initial capacity to endow your inner implementation with.
	 * @throws InvalidCapacityException if the capacity provided is less than 1.
	 */
	public LinearPriorityQueue(int capacity) throws InvalidCapacityException{		// DO *NOT* ERASE THE "THROWS" DECLARATION!
		pqueue = new LinkedList<PQElement>();
		if (capacity <= 0) {
			throw new InvalidCapacityException("Invalid capacity.");
		}
		
		this.capacity = capacity;
		size = 0;
		mods = 0;
	}

	@Override
	public void enqueue(T element, int priority) throws InvalidPriorityException{	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (priority <= 0) {
			throw new InvalidPriorityException("Invalid priority.");
		}
		
		PQElement newElement = new PQElement(element, priority);
		
		if (pqueue.isEmpty()) {
			pqueue.add(newElement);
			size++;
			mods++;
		} else {
			PQElement current = pqueue.element();
			
			while (current != null) {
				if (newElement.priority > current.priority && current.next != null) {
					if (newElement.priority < current.next.priority) {
						pqueue.add(pqueue.indexOf(current.next), newElement);
						newElement.next = current.next;
						current.next = newElement;
						size++;
						mods++;
						break;
					} else {
						current = current.next;
					}
				} else {
					if (current.next != null) {
						if (newElement.priority == current.next.priority) {
							current = current.next;
						} else {
							pqueue.add(pqueue.indexOf(current), newElement);
							newElement.next = current.next;
							current.next = newElement;
							size++;
							mods++;
							break;
						}
					} else {
						pqueue.add(newElement);
						current.next = newElement;
						size++;
						mods++;
						break;
					}
				}
			}
			
		}
	}

	@Override
	public T dequeue() throws EmptyPriorityQueueException {		// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (pqueue.isEmpty()) {
			throw new EmptyPriorityQueueException("Priority Queue is empty.");
		}
		
		T top =  (T) pqueue.getFirst().data;
		pqueue.pop();
		size--;
		mods++;
		
		return top;
	}

	@Override
	public T getFirst() throws EmptyPriorityQueueException {	// DO *NOT* ERASE THE "THROWS" DECLARATION!
		if (pqueue.isEmpty()) {
			throw new EmptyPriorityQueueException("Priority Queue is empty.");
		}
		
		return (T) pqueue.getFirst().data;
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
			private int index = 0;
			private PQElement current = pqueue.get(index);
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
					current = pqueue.get(index);
					return current.data;
				}	
			}
		};
	}

}