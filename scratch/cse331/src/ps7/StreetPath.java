package ps7;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ps4.StreetClassification;
import ps4.StreetSegment;
import ps3.graph.Graph;
import ps3.graph.Path;
import ps7.StreetPath;

/**
 * <p>
 * A StreetPath is a sequence of StreetSegments that represent a continuous 
 * navigational path across the earth's surface.  It's cost is defined
 * as the total distance traversed by the path's StreetSegments so far, plus
 * the geographic distance to the nearest goal segment. 
 * </p>
 * 
 * <p>
 * A StreetPath is immutable. A new StreetPath is returned through
 * the extend path operation.
 * </p>
 * 
 * <p>
 * The elements of this path are StreetSegments.  For any single segment of the path,
 * the segment's start point must be equal to the end point of the previous 
 * segment, and it's end point must be equal to the start point of the next segment
 * in the path.
 * </p>
 * 
 * <p>
 * StreetPath contains most implementation of the advanced features of HuskyMaps,
 * controlled statically by the fields of ps7.EnvironmentVariables.  See documentation
 * for details.
 * </p>
 *
 * Specfields inherited from Path:
 * @specfield  cost :     double       // cost of traversing this path.
 * @specfield  elements : sequence     // sequence of nodes in this path.
 * 
 * 
 * Local specfields
 * @specfield  node : StreetSegment		// the end node of this path
 * @specfield  goals : Set[StreetEntry] 	// collection of goal StreetEntrys
 * 										// for this path instance 
 * @derivedfield  weightedLength : double	// cost-accounting length of this path
 * @derivedfield  hash : int			// the hashcode of this path 
 * @specfield  start :  StreetEntry		// StreetEntry of the starting address
 * 
 * @specfield  geographicMap : Graph[StreetSegment]  // Reference to the context 
 * 												//graph to determine intersections
 */


public class StreetPath implements Path<StreetSegment, StreetPath> {

	
	/*
	 * Abstraction Function
	 * AF(c) = < cost, elements >  where
	 * 		cost     = c.cost
	 * 		elements = 	[c.node]					if c.path == null
	 * 				 ||	[c.node] + c.path.elements		if c.path != null
	 * 
	 * 		Intersections:
	 * 			An element traverses an intersection if 
	 * 			geographicMap.edgeSet( element ).size() > 1
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


	
	/** The absolute closest distance of any path object to its goal */
	public static double CLOSEST_PATH_DISTANCE = Double.MAX_VALUE;
	
	
    /** The StreetSegment at the end of the path. */
	private final StreetSegment node;
    /** A StreetPath which, when extended with 'node' at the end,
     *  is equal to this.  May be null iff this path has only 1 element. */
    private final StreetPath path;
    /** The length of this StreetPath in weighted miles as calculated taking into account 
     * street classification and the number of intersections traversed. Weighted miles
     * are representative of travel time, not true travel distance.*/
    private final double weightedLength;
    /** The cost of this StreetPath taking into account street classification and
     * the number of intersections traversed. */
    private final double cost;
    /** The hashcode of this StreetPath */
    private final int hash;
    
    /** The StreetEntry of the starting address */
    private final StreetEntry start;
    /** Reference to the context graph to determine intersections */
	private final Graph<StreetSegment> geographicMap;
	
    /** Set of goal StreetSegments of this Path, to compute  */
	private final Set<StreetEntry> goals; // for computing cost

	
	
	/**
	 * A new StreetPath with the specified set of StreetSegments acting as the
	 * end goals for this StreetPath instance.
	 * @requires start != null && goals != null && !goals.isEmpty() && geographicMap != null
	 * @param goals a set of StreetEntrys as the goals of this path
	 * @param the Graph to operate over
	 * @effects Creates a new single-element StreetPath with the element contained within
	 * the specified StreetEntry and no additional elements. 
	 */
    public StreetPath(StreetEntry start, Set<StreetEntry> goals,
    		Graph<StreetSegment> geographicMap) {
    	this(start.getStreet(), null, goals, start, geographicMap);
		
		// this path has reached a goal segment, so reset the absolute closest path distance
		CLOSEST_PATH_DISTANCE = Double.MAX_VALUE;
    }
	
    /**
     * Constructs a new StreetPath with the specified arguments
	 * @requires node != null && goals != null && !goals.isEmpty() && geographicMap != null
	 * @param node the end segment of this path
	 * @param path the rest of the path
	 * @param goals the set of goals for this path
	 * @param start the start Entry for this path
	 * @param the Graph to operate over
	 * @effects Creates a new StreetPath which is the specified path
	 * plus the specified node added as it's new end element.
	 */
    @SuppressWarnings("unused")
	private StreetPath(StreetSegment node, StreetPath path, Set<StreetEntry> goals,
    		StreetEntry start, Graph<StreetSegment> geographicMap){
        if (node == null || goals == null || start == null || geographicMap == null) {
            throw new IllegalArgumentException("no specified arguments can be null");
        }else if(goals.isEmpty()){
            throw new IllegalArgumentException("must have at least one goal segment");	
        }
        this.node = node;
        this.path = path;
        this.goals = goals;
        this.start = start;
		this.geographicMap = geographicMap;

        // get length in weighted miles of the path so far
		double length = (path != null) ? path.weightedLength : 0;
		if(EnvironmentVariables.INTERSECTION_TRAVERSAL_ON  &&
				geographicMap.edgeSet(node).size() >= 
					EnvironmentVariables.STREETS_PER_INTERSECTION)
				// take into account the cost of traversing an intersection if this 
				// node is considered an intersection on the graph.
		{
			// Having the intersection increase cost slows down pathfinding performance 
			length += EnvironmentVariables.INTERSECTION_TRAVERSAL_COST;
		}
		length += (EnvironmentVariables.STREET_CLASSIFICATION_ON ? 
				parseStreetClass(node) : 1) * 
				((path == null && EnvironmentVariables.FRACTIONAL_SEGMENTS_ON) ? 
				start.getFracLength() : node.getLength());
		this.weightedLength = length;
		
		
		double distance = Double.MAX_VALUE;
		for(StreetEntry se : goals){
			StreetSegment gs = se.getStreet();
			if(node.equals(gs)){	// if this path has reached a goal segment, then 
												// cost can double as the final length 
				distance = 0;
				break;
			}
			distance = Math.min(distance, node.getP2().distanceTo(gs.getP1()) +
					(EnvironmentVariables.FRACTIONAL_SEGMENTS_ON ? se.getFracLength() : 0));
					// factor in the distance to the address entering the goal segment  
		}
		
		this.cost = this.weightedLength + calculateDistanceCostFactor(distance);
				// multiply this segment by its 
;
		
		this.hash = node.hashCode() + (this.path==null ? 1 : path.hashCode());
		
		if(CHECK_REP) checkRep();
    }

    /** helper to assign an extra factor of cost to a street segment based on its 
     * Street Classification */
    private double parseStreetClass(StreetSegment street){
    	StreetClassification sc = street.getStreetClass();
    	if(sc == StreetClassification.PRIM_HWY){
    		return EnvironmentVariables.PRIM_HWY_COST;
    	}else if(sc == StreetClassification.SEC_HWY){
    		return EnvironmentVariables.SEC_HWY_COST;
    	}else if(sc == StreetClassification.LOCAL_ROAD){
    		return EnvironmentVariables.LOCAL_ROAD_COST;
    	}else{
    		return EnvironmentVariables.UNKNOWN_ROAD_COST;
    	}
    }
    
    /**
     * Calculates the distance-to-goal cost factor for this StreetPath based on
     * this path's distance to a goal and the absolute shortest distance out of
     * all StreetPath objects.  Also relies on system environment variables.
     * See ps7.EnvironmentVariables
     * @param distance this path's shortest distance to a goal
     * @return the distance-to-goal cost factor to be used in determining this path's cost. 
     */
    @SuppressWarnings("unused")
	private double calculateDistanceCostFactor(double distance){
    	if(!EnvironmentVariables.STREET_CLASSIFICATION_ON &&
    		!EnvironmentVariables.INTERSECTION_TRAVERSAL_ON ){
    			return distance * (EnvironmentVariables.OPTIMIZE_FOR_PERFORMANCE_ON ? 2 : 1);
    		}
    	
    	double distanceFactor = EnvironmentVariables.DISTANCE_TO_GOAL_COST_FACTOR *
    			(EnvironmentVariables.OPTIMIZE_FOR_PERFORMANCE_ON ? 2 : 1);
    	
    	// determine cost optimization for this path 
    	double powerFactor = 1.0;
    	if(distance < CLOSEST_PATH_DISTANCE)
    	{ // if this path happens to be the new absolute shortest path
    		CLOSEST_PATH_DISTANCE = distance;
    		
    	// if the distance falls outside of the 'buffer zone' of the absolute closest path
    	// (i.e. the most probable path)
    	}else if( distance - CLOSEST_PATH_DISTANCE > 
    			EnvironmentVariables.MAXIMUM_BUFFER_DISTANCE)
    	{
    		// exponentially increase the cost of a StreetPath based on how much farther
        	// from a goal segment it is than the closest path
    		powerFactor = distance / CLOSEST_PATH_DISTANCE;
    	}
    	
    	// make sure the distance factor remains the baseline.
    	return distance * Math.max(distanceFactor, 
    			Math.pow(distanceFactor, powerFactor));
    }
    
    /**
     * @throw IllegalStateException if the representation invariant is broken 
     */
    private void checkRep(){
    	StreetSegment curr = node;
    	Iterator<StreetSegment> iter = iterator();
    	while(iter.hasNext()){
    		StreetSegment next = iter.next();
    		if(!curr.getP1().equals(next.getP2())){
    			throw new IllegalStateException("Consecutive StreetSegments of this" +
    					" StreetPath must have matching points:  this segment's end point" +
    					" must match the next segment's start point");
    		}
    		curr = next;
    	}
    }
    
    /**
     * Specified by Path interface.  In addition:
     * @requires specified StreetSegment is not null.  Also, the start point of 
     * specified StreetSegment must match end point of this path's end segment.
     * @throws IllegalArgumentException if passed an invalid StreetSegment
     */
	public StreetPath extend(StreetSegment gs){
		if(!node.getP2().equals(gs.getP1())){
			throw new IllegalArgumentException("cannot extend StreetPath with a segment that" +
					" does not start at this StreetPath's end point");
		}
		
		return new StreetPath(gs, this, goals, start, geographicMap);
	}


	/**
	 * Specified by Path interface.  In addition:
	 * StreetPath cost is defined as the total weighted-distance traversed by the path's
	 * StreetSegments so far, plus the geographic distance to the nearest goal segment
	 * (if goal segments were specified).  Additional cost-determination features
	 * available and are controlled by ps6.EnvironmentVariables.java
	 * @returns a double representing the path's cost
	 */
	public double cost(){
		return cost;
	}
	
	
    /**
     * Specified by Path interface (which extends Iterable)
     * Additionally, if fractional segments are activated, the first segment 
     * returned represents the fractional StreetSegment for the start Address 
     * of this path.  If the last segment of the path is a goal segment, then 
     * it too is represented as a fractional StreetSegment for the end address
     * of that goal segment.
     */
	public Iterator<StreetSegment> iterator(){
        // reverse the linked list, so that elements are returned in order
        // from start to end of the path.
        List<StreetSegment> accumulator = new LinkedList<StreetSegment>();
        
        if(EnvironmentVariables.FRACTIONAL_SEGMENTS_ON){
	        accumulator.add(determineFractional(node));
	        
	        if(path != null){
	            for (StreetPath cur = path; cur.path!=null; cur = cur.path) {
	                accumulator.add(0, cur.end());
	            }
	        	accumulator.add(0, start.getFractionalSegment());
	        }
        }else{
            for (StreetPath cur = this; cur!=null; cur = cur.path) {
                accumulator.add(0, cur.end());
            }
        }
        
        return accumulator.iterator();
	}
	
	/** helper to determine if a fractional segment should be used to build this route. 
	 * @return the fractional StreetSegment of the specified street if it is a start
	 * or end segment, else returns the street right back */
	private StreetSegment determineFractional(StreetSegment street){
		StreetEntry end = null;
		for(StreetEntry se : goals){
			if(se.getStreet().equals(street)){
				end = se;
				break;
			}
		}
		if(end != null && start.equals(end)){  // street is part of the end
			return start.getSameStreetFractional(end);
		}else if(end != null){
			return end.getFractionalSegment();
		}else if(start.getStreet().equals(street)){
			return start.getFractionalSegment();
		}
		return street;
	}
	

	/**
	 * Get the end segment of this path
	 * @return the end StreetSegment for this path
	 */
	public StreetSegment end(){
		return node;
	}
	

    /**
     * @return true iff o is a StreetPath and o.elements is the
     * same sequence as this.elements
     **/
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StreetPath))
            return false;
        return this.equals((StreetPath) o);
    }

    /**
     * @return true iff sp.elements is the same sequence as this.elements
     **/
    public boolean equals(StreetPath sp) {
        return (sp != null) &&
            this.node.equals(sp.node) && (this.cost() == sp.cost()) &&
            (this.path == null ? sp.path==null : this.path.equals(sp.path));
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
	public int compareTo(Path<?, ?> sp){
		return Double.compare(this.cost(), sp.cost());
	}
}
