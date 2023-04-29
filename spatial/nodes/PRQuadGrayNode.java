package spatial.nodes;

import spatial.exceptions.UnimplementedMethodException;
import spatial.kdpoint.KDPoint;
import spatial.knnutils.BoundedPriorityQueue;
import spatial.knnutils.NNData;
import spatial.trees.CentroidAccuracyException;
import spatial.trees.PRQuadTree;

import java.util.ArrayList;
import java.util.Collection;

/** <p>A {@link PRQuadGrayNode} is a gray (&quot;mixed&quot;) {@link PRQuadNode}. It
 * maintains the following invariants: </p>
 * <ul>
 *      <li>Its children pointer buffer is non-null and has a length of 4.</li>
 *      <li>If there is at least one black node child, the total number of {@link KDPoint}s stored
 *      by <b>all</b> of the children is greater than the bucketing parameter (because if it is equal to it
 *      or smaller, we can prune the node.</li>
 * </ul>
 *
 * <p><b>YOU ***** MUST ***** IMPLEMENT THIS CLASS!</b></p>
 *
 *  @author --- YOUR NAME HERE! ---
 */
public class PRQuadGrayNode extends PRQuadNode{


    /* ******************************************************************** */
    /* *************  PLACE ANY  PRIVATE FIELDS AND METHODS HERE: ************ */
    /* ********************************************************************** */
	private PRQuadNode NW, NE, SW, SE;
    /* *********************************************************************** */
    /* ***************  IMPLEMENT THE FOLLOWING PUBLIC METHODS:  ************ */
    /* *********************************************************************** */

    /**
     * Creates a {@link PRQuadGrayNode}  with the provided {@link KDPoint} as a centroid;
     * @param centroid A {@link KDPoint} that will act as the centroid of the space spanned by the current
     *                 node.
     * @param k The See {@link PRQuadTree#PRQuadTree(int, int)} for more information on how this parameter works.
     * @param bucketingParam The bucketing parameter fed to this by {@link PRQuadTree}.
     * @see PRQuadTree#PRQuadTree(int, int)
     */
    public PRQuadGrayNode(KDPoint centroid, int k, int bucketingParam){
        super(centroid, k, bucketingParam); // Call to the super class' protected constructor to properly initialize the object!
    }


    /**
     * <p>Insertion into a {@link PRQuadGrayNode} consists of navigating to the appropriate child
     * and recursively inserting elements into it. If the child is a white node, memory should be allocated for a
     * {@link PRQuadBlackNode} which will contain the provided {@link KDPoint} If it's a {@link PRQuadBlackNode},
     * refer to {@link PRQuadBlackNode#insert(KDPoint, int)} for details on how the insertion is performed. If it's a {@link PRQuadGrayNode},
     * the current method would be called recursively. Polymorphism will allow for the appropriate insert to be called
     * based on the child object's runtime object.</p>
     * @param p A {@link KDPoint} to insert into the subtree rooted at the current {@link PRQuadGrayNode}.
     * @param k The side length of the quadrant spanned by the <b>current</b> {@link PRQuadGrayNode}. It will need to be updated
     *          per recursive call to help guide the input {@link KDPoint}  to the appropriate subtree.
     * @return The subtree rooted at the current node, potentially adjusted after insertion.
     * @see PRQuadBlackNode#insert(KDPoint, int)
     */
    @Override
    public PRQuadNode insert(KDPoint p, int k) {
    	if (k < -5) throw new CentroidAccuracyException("Centroid accuracy exception");
    	if (p.coords[0] < centroid.coords[0]) {
    		if (p.coords[1] < centroid.coords[1]) {
    			if (SW == null) {
    				KDPoint cent = new KDPoint();
    				cent.coords[0] = (int) (centroid.coords[0] - Math.pow(2, k-2));
    				cent.coords[1] = (int) (centroid.coords[1] - Math.pow(2, k-2));
    				SW = new PRQuadBlackNode(cent, k - 1, bucketingParam, p);
    			} else {
    				SW = SW.insert(p, k - 1);
    			}
    		} else {
    			if (NW == null) {
    				KDPoint cent = new KDPoint();
    				cent.coords[0] = (int) (centroid.coords[0] - Math.pow(2, k-2));
    				cent.coords[1] = (int) (centroid.coords[1] + Math.pow(2, k-2));
    				NW = new PRQuadBlackNode(cent, k - 1, bucketingParam, p);
    			} else {
    				NW = NW.insert(p, k - 1);
    			}
    		}
    	} else {
    		if (p.coords[1] < centroid.coords[1]) {
    			if (SE == null) {
    				KDPoint cent = new KDPoint();
    				cent.coords[0] = (int) (centroid.coords[0] + Math.pow(2, k-2));
    				cent.coords[1] = (int) (centroid.coords[1] - Math.pow(2, k-2));
    				SE = new PRQuadBlackNode(cent, k - 1, bucketingParam, p);
    			} else {
    				SE = SE.insert(p, k - 1);
    			}
    		} else {
    			if (NE == null) {
    				KDPoint cent = new KDPoint();
    				cent.coords[0] = (int) (centroid.coords[0] + Math.pow(2, k-2));
    				cent.coords[1] = (int) (centroid.coords[1] + Math.pow(2, k-2));
    				NE = new PRQuadBlackNode(cent, k - 1, bucketingParam, p);
    			} else {
    				NE = NE.insert(p, k - 1);
    			}
    		}
    	}
    	
    	return this;
    }

    /**
     * <p>Deleting a {@link KDPoint} from a {@link PRQuadGrayNode} consists of recursing to the appropriate
     * {@link PRQuadBlackNode} child to find the provided {@link KDPoint}. If no such child exists, the search has
     * <b>necessarily failed</b>; <b>no changes should then be made to the subtree rooted at the current node!</b></p>
     *
     * <p>Polymorphism will allow for the recursive call to be made into the appropriate delete method.
     * Importantly, after the recursive deletion call, it needs to be determined if the current {@link PRQuadGrayNode}
     * needs to be collapsed into a {@link PRQuadBlackNode}. This can only happen if it has no gray children, and one of the
     * following two conditions are satisfied:</p>
     *
     * <ol>
     *     <li>The deletion left it with a single black child. Then, there is no reason to further subdivide the quadrant,
     *     and we can replace this with a {@link PRQuadBlackNode} that contains the {@link KDPoint}s that the single
     *     black child contains.</li>
     *     <li>After the deletion, the <b>total</b> number of {@link KDPoint}s contained by <b>all</b> the black children
     *     is <b>equal to or smaller than</b> the bucketing parameter. We can then similarly replace this with a
     *     {@link PRQuadBlackNode} over the {@link KDPoint}s contained by the black children.</li>
     *  </ol>
     *
     * @param p A {@link KDPoint} to delete from the tree rooted at the current node.
     * @return The subtree rooted at the current node, potentially adjusted after deletion.
     */
    @Override
    public PRQuadNode delete(KDPoint p) {
        for (PRQuadNode child : this.getChildren()) {
        	if (child != null) {
        		if (child.search(p)) child.delete(p);
        	}
        }
        
        if (this.count() <= bucketingParam) {
        	PRQuadBlackNode blackNode = new PRQuadBlackNode(centroid, k, bucketingParam);
        	
        	for (PRQuadNode child : this.getChildren()) {
        		if (child != null && child instanceof PRQuadBlackNode) {
        			for (KDPoint point : ((PRQuadBlackNode) child).getPoints()) {
        				blackNode.insert(point, k);
        			}
        		}
        	}
        	
        	return blackNode;
        }
        
        
        return this;
    }

    @Override
    public boolean search(KDPoint p){
    	if (p.coords[0] < centroid.coords[0]) {
    		if (p.coords[1] < centroid.coords[1]) {
    			if (SW == null) {
    				return false;
    			} else {
    				return SW.search(p);
    			}
    		} else {
    			if (NW == null) {
    				return false;
    			} else {
    				return NW.search(p);
    			}
    		}
    	} else {
    		if (p.coords[1] < centroid.coords[1]) {
    			if (SE == null) {
    				return false;
    			} else {
    				return SE.search(p);
    			}
    		} else {
    			if (NE == null) {
    				return false;
    			} else {
    				return NE.search(p);
    			}
    		}
    	}
    }

    @Override
    public int height(){
    	int max = 0;
    	for (PRQuadNode child : this.getChildren()) {
    		if (child != null) {
    			if (child.height() > max) max = child.height();
    		}
    	}
        return 1 + max;
    }

    @Override
    public int count(){
        int count = 1;
        
        if (NW != null) count += NW.count();
        if (NE != null) count += NE.count();
        if (SW != null) count += SW.count();
        if (SE != null) count += SE.count();
        
        return count;
        		
    }

    /**
     * Returns the children of the current node in the form of a Z-ordered 1-D array.
     * @return An array of references to the children of {@code this}. The order is Z (Morton), like so:
     * <ol>
     *     <li>0 is NW</li>
     *     <li>1 is NE</li>
     *     <li>2 is SW</li>
     *     <li>3 is SE</li>
     * </ol>
     */
    public PRQuadNode[] getChildren(){
        return new PRQuadNode[] {NW, NE, SW, SE};
    }

    @Override
    public void range(KDPoint anchor, Collection<KDPoint> results, double range) {
        for (PRQuadNode child : this.getChildren()) {
        	if (child != null && child.doesQuadIntersectAnchorRange(anchor, range)) {
        		child.range(anchor, results, range);
        	}
        }
    }

    @Override
    public NNData<KDPoint> nearestNeighbor(KDPoint anchor, NNData<KDPoint> n)  {
    	PRQuadNode visited = null;
    	if (anchor.coords[0] < centroid.coords[0]) {
    		if (anchor.coords[1] < centroid.coords[1]) {
    			if (SW != null) {
	    			n = SW.nearestNeighbor(anchor, n);
	    			visited = SW;
    			}
    		} else {
    			if (NW != null) {
	    			n = NW.nearestNeighbor(anchor, n);
	    			visited = NW;
    			}
    		}
    	} else {
    		if (anchor.coords[1] < centroid.coords[1]) {
    			if (SE != null) {
	    			n = SE.nearestNeighbor(anchor, n);
	    			visited = SE;
    			}
    		} else {
    			if (NE != null) {
	    			n = NE.nearestNeighbor(anchor, n);
	    			visited = NE;
    			}
    		}
    	}
    	
        for (PRQuadNode child : this.getChildren()) {
        	if (child != null && child != visited) {
        		if (n.getBestDist() < 0 || child.doesQuadIntersectAnchorRange(anchor, n.getBestDist())) {
        			n = child.nearestNeighbor(anchor, n);
        		}
        	}
        }
        
        return n;
    }

    @Override
    public void kNearestNeighbors(int k, KDPoint anchor, BoundedPriorityQueue<KDPoint> queue) {
    	PRQuadNode visited = null;
    	if (anchor.coords[0] < centroid.coords[0]) {
    		if (anchor.coords[1] < centroid.coords[1]) {
    			if (SW != null) {
	    			SW.kNearestNeighbors(k, anchor, queue);
	    			visited = SW;
    			}
    		} else {
    			if (NW != null) {
	    			NW.kNearestNeighbors(k, anchor, queue);
	    			visited = NW;
    			}
    		}
    	} else {
    		if (anchor.coords[1] < centroid.coords[1]) {
    			if (SE != null) {
	    			SE.kNearestNeighbors(k, anchor, queue);
	    			visited = SE;
    			}
    		} else {
    			if (NE != null) {
	    			NE.kNearestNeighbors(k, anchor, queue);
	    			visited = NE;
    			}
    		}
    	}
    	
        for (PRQuadNode child : this.getChildren()) {
        	if (child != null && child != visited) {
        		if (queue.last() == null || child.doesQuadIntersectAnchorRange(anchor, queue.last().euclideanDistance(anchor))) {
        			child.kNearestNeighbors(k, anchor, queue);
        		}
        	}
        }
    }
}

