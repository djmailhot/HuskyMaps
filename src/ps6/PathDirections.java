package ps6;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ps2.Route;

/**
 *   A PathDirections is a description of how to travel from one Address to
 *   another.
 *   @specfield start      : Address // starting point
 *   @specfield end        : Address // destination
 *   @specfield length     : double  // traveling distance, in miles (not rounded)
 *   @specfield route      : Route  // this sequence of segments the directions
 *   								// travel over
 *   @specfield directions : sequence[String] //
 *   directions for traveling from start to end.  Each
 *   String in the sequence must be one line of directions, in the format
 *   specified in <a href="../../psets/ps6/ps6.html#dirs-format">PS6
 *   text based interface</a>, <b>excluding</b> the first line which
 *   states where you start <b>and</b> the final line which gives the length/time
 *   of the trip.  The strings <b>must not</b> include newlines at their
 *   ends.  The order of the Strings in the sequence must correspond to the order
 *   of the lines in the directions from start to end.
 */

public class PathDirections implements Directions{

	/** The start address of these directions */
	private final Address start;
	/** The end address of these directions */
	private final Address end;
	/** The route geographically representing these directions */
	private final Route route;
	/** Sequential list of navigational instructions */
	private final List<String> directions;
	
	/**
	 * Creates a new PathDirections containing the specified sequence of directions 
	 * @requires directions != null && route != null && start != null && end != null
	 * @effects Create a new PathDirections object with the specified list of 
	 * strings as its sequence of directions.
	 */
	public PathDirections(List<String> directions, Route route, Address start, Address end){
		if(directions == null || route == null || start == null || end == null){
			throw new IllegalArgumentException("Specified arguments must not be null");
		}
		
		this.start = start;
		this.end = end;
		this.route = route;
		this.directions = Collections.unmodifiableList(directions);
	}
	
	
    /** 
     * Get the length of these directions, in miles
     * @return this.length. 
     */
    public double getLength (){
    	return route.getLength();
    }

    /** 
     * Get an iterator over this sequence of directions
     * @return an Iterator of Strings over this.directions 
     */
    public Iterator<String> iterator(){
    	return directions.iterator();
    }

    /**
     * Get the start address 
     * @return this.start 
     */
    public Address getStart (){
    	return start;
    }

    /**
     * Get the end address 
     * @return this.end 
     */
    public Address getEnd (){
    	return end;
    }
    
    /**
     * Get the route geographically representing these directions
     * @return this.route 
     */
    public Route getRoute(){
    	return route;
    }
    
    /**
     * @return these directions in a single new-line-separated string 
     */
    public String toString(){
    	String result = "Start at "+start+"\n";
		
		Iterator<String> iter = iterator();
		while(iter.hasNext()){
			result += iter.next()+"\n";
		}
		
		return result + "Trip length: "+route.getLength();
    }
	
}
