package ps4;

import ps3.graph.Path;
import ps2.GeoSegment;
import ps2.Route;
import java.util.*;


/**
 * <p>
 * A RoutePath is a sequence of GeoSegments that represent a continuous 
 * navigational path across the earth's surface.  It's cost is defined
 * as the total distance traversed by the path's GeoSegments so far, plus
 * the geographic distance to the nearest goal segment. 
 * 
 * <p>
 * A Route Path is immutable. A new RoutePath is returned through
 * the extend path operation.
 * 
 * <p>
 * The elements of this path are GeoSegments.  For any single segment of the path,
 * the segment's start point must be equal to the end point of the previous 
 * segment, and it's end point must be equal to the start point of the next segment
 * in the path.
 *
 *
 * Specfields inherited from Path:
 * @specfield  cost :     double       // cost of traversing this path.
 * @specfield  elements : sequence     // sequence of nodes in this path.
 * 
 * 
 * Local specfields
 * @specfield  node : GeoSegment		// the end node of this path
 * @specfield  goals : Set<GeoSegment> 	// collection of goal nodes for this
 * 										// path instance 
 * @specfield  length : double			// geographic length of this path
 */

public class RoutePath implements Path<GeoSegment, RoutePath> {
	
	/*
	 * Abstraction Function
	 * AF(c) = < cost, elements >  where
	 * 		cost     = c.cost
	 * 		elements = 	[c.node]					if c.path == null
	 * 				 ||	[c.node] + c.path.elements		if c.path != null
	 * 
	 * 
	 * 
	 * Representation Invariant 
	 * 		(this.node != null && this.goals != null && !this.goals.isEmpty() ) &&
	 * 		(this.path == null) ==> (this.cost == this.node.cost) &&
	 * 		(this.path != null) ==> (this.cost == this.node.cost + this.path.cost)
	 * 
	 * 		In a path of n elements, for element e, {1 < e < n-1} :
	 * 			e.getP1().equals( [e-1].getP2() ) && e.getP2().equals( [e+1].getP1() )
	 * 			that is for any element, it's start point must be equal to the end point
	 * 			of the previous element, and it's end point must be equal to the start
	 * 			point of the next element
	 */
	
	/** Switch to turn on checks of the representation invariant */
	private static final boolean CHECK_REP = false;
	
    /** Factor for modified A* of cost = path length + (distance to nearest goal) * FACTOR 
     *  Analyzed to improve operating time by a x2 factor with no loss in accuracy. 
     */
	private static final double DISTANCE_TO_GOAL_COST_FACTOR = 1.00;
	
	
    /** The GeoSegment at the end of the path. */
	private final GeoSegment node;
    /** A RoutePath which, when extended with 'node' at the end,
     *  is equal to this.  May be null iff this path has only 1 element. */
    private final RoutePath path;
    /** The length of this RoutePath. */
    private final double length;
    /** The cost of this RoutePath. */
    private final double cost;
    /** The hashcode of this RoutePath */
    private final int hash;
    
    /** Set of goal GeoSegments of this Path, to compute  */
	private final Set<GeoSegment> goals; // for computing cost

	
	
	/**
	 * A new RoutePath with the specified set of GeoSegments acting as the
	 * end goals for this RoutePath instance.
	 * @requires node != null && goals != null && !goals.isEmpty()
	 * @effects Creates a new single-element RoutePath with the specified 
	 * node as that element and no additional elements 
	 */
    public RoutePath(GeoSegment node, Set<GeoSegment> goals) {
        this(node, null, goals);
    }
	
	/**
	 * @requires node != null && goals != null && !goals.isEmpty()
	 * @effects Creates a new RoutePath which is the specified path
	 * plus the specified node added as it's new end element
	 */
    private RoutePath(GeoSegment node, RoutePath path, Set<GeoSegment> goals) {
        if (node == null || goals == null) {
            throw new IllegalArgumentException("no specified arguments can be null");
        }else if(goals.isEmpty()){
            throw new IllegalArgumentException("must have at least one goal segment");	
        }
        this.node = node;
        this.path = path;
        this.goals = goals;
        
		double length = node.getLength();
		if(path != null){
			length += path.length;
		}
		this.length = length;
        
		double distance = Double.MAX_VALUE;
		for(GeoSegment gs : goals){
			if(node.equals(gs)){	// if this path has reached a goal segment, then 
									// cost can double as the final length 
				distance = 0;
				break;
			}
			distance = Math.min(distance, node.getP2().distanceTo(gs.getP1()));
		}
		this.cost = length + distance * DISTANCE_TO_GOAL_COST_FACTOR;
		
		this.hash = node.hashCode() + (this.path==null ? 1 : path.hashCode());
		
		if(CHECK_REP)
			checkRep();
    }


    /**
     * @throw IllegalStateException if the representation invariant is broken 
     */
    private void checkRep(){
    	GeoSegment curr = node;
    	Iterator<GeoSegment> iter = iterator();
    	while(iter.hasNext()){
    		GeoSegment next = iter.next();
    		if(!curr.getP1().equals(next.getP2())){
    			throw new IllegalStateException("Consecutive GeoSegments of this" +
    					" RoutePath must have matching points:  this segment's end point" +
    					" must match the next segment's start point");
    		}
    		curr = next;
    	}
    }
    
    /**
     * Specified by Path interface.  In addition:
     * @requires specified GeoSegment is not null.  Also, the start point of 
     * specified GeoSegment must match end point of this path's end segment.
     * @throws IllegalArgumentException if passed an invalid GeoSegment
     */
	public RoutePath extend(GeoSegment gs){
		if(!node.getP2().equals(gs.getP1())){
			throw new IllegalArgumentException("cannot extend RoutePath with a segment that" +
					" does not start at this RoutePath's end point");
		}
		
		return new RoutePath(gs, this, goals);
	}


	/**
	 * Specified by Path interface.  In addition:
	 * RoutePath cost is defined as the total distance traversed by the path's
	 * GeoSegments so far, plus the geographic distance to the nearest goal segment
	 * (if goal segments were specified)
	 * @returns a double representing the path's cost
	 */
	public double cost(){
		return cost;
	}
	
	
    // Specified by Path interface (which extends Iterable)
	public Iterator<GeoSegment> iterator(){
        // reverse the linked list, so that elements are returned in order
        // from start to end of the path.
        List<GeoSegment> accumulator = new LinkedList<GeoSegment>();
        for (RoutePath cur = this; cur!=null; cur = cur.path) {
            accumulator.add(0, cur.end());
        }
        return accumulator.iterator();
	}
	

	/**
	 * @return the end GeoSegment for this path
	 */
	public GeoSegment end(){
		return node;
	}


    /**
     * @return true iff o is a RoutePath and o.elements is the
     * same sequence as this.elements
     **/
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RoutePath))
            return false;
        return this.equals((RoutePath) o);
    }

    /**
     * @return true iff rp.elements is the same sequence as this.elements
     **/
    public boolean equals(RoutePath rp) {
        return (rp != null) &&
            this.node.equals(rp.node) && (this.cost() == rp.cost()) &&
            (this.path == null ? rp.path==null : this.path.equals(rp.path));
    }

    /**
     * @return the hash code of this path
     */
    public int hashCode(){
    	return hash;
    }

    /**
     * Compares the cost of this path to the given path.
     * @return the value 0 if the cost of this path is equal to the
     *         cost of the given path; a value less than 0 if this.cost is
     *         less than p.cost; and a value greater than 0 if this.cost
     *         is greater than p.cost.
     * @see java.lang.Comparable#compareTo
     */
	public int compareTo(Path<?, ?> rp){
		return Double.compare(this.cost(), rp.cost());
	}
	

}
