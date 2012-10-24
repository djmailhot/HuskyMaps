package ps2;

import static ps2.test.TestValues.TOLERANCE;

import java.util.*;


/**
 * <p>
 * A GeoFeature represents a path from one location to another along
 * a single geographic feature.  GeoFeatures are immutable.
 * </p>
 *
 * <p>
 * GeoFeature abstracts over a sequence of GeoSegments, all of which
 * have the same name, thus providing a representation for nonlinear
 * or nonatomic geographic features.  As an example, a GeoFeature
 * might represent the course of a winding river, or travel along a
 * road through intersections but remaining on the same road.
 * </p>
 *
 * <p>
 * GeoFeatures are immutable.  New GeoFeatures can be constructed by
 * adding a segment to the end of a GeoFeature.  An added segment must
 * be properly oriented; that is, its p1 field must correspond to the
 * end of the original GeoFeature, and its p2 field corresponds to the
 * end of the new GeoFeature, and the name of the GeoSegment being
 * added must match the name of the existing GeoFeature.
 * </p>
 *
 * <p>
 * Because a GeoFeature is not necessarily straight, its length -- the
 * distance traveled by following the path from start to end -- is not
 * necessarily the same as the distance along a straight line between
 * its endpoints.
 * </p>
 * 
 * 
 *
 * @specfield start : GeoPoint         // location of the start of the geo feature
 * @specfield end : GeoPoint           // location of the end of the geo feature
 * @specfield startHeading : angle     // direction of travel at the start of the geo feature, in degrees
 * @specfield endHeading : angle       // direction of travel at the end of the geo feature, in degrees
 * @specfield geoSegments : sequence   // a sequence of segments that make up this geographic feature
 * @specfield name : String            // name of geographical feature
 * @derivedfield length : real         // total length of the geo feature, in miles
 *
 **/
public class GeoFeature {

	 /*
	 * ABSTRACTION FUNCTION
	 *  start  >>  the location of the start of the feature
	 *  end  >>  the location of the end of the feature
	 *  startHeading  >>  the direction of travel in compass degrees at the start of the feature
	 *  endHeading  >>  the direction of travel in compass degrees at the end of the feature
	 *  geoSegments  >>  a sequence of segments that make up this feature
	 *  name  >>  the name of this geographic feature
	 *  length  >>  the total length of the feature, in miles
	 * 
	 * REPRESENTATION INVARIANT
	 *   this.start           = geoSegments.get(0).p1
	 *   && this.startHeading = geoSegments.get(0).heading
	 *   && this.end          = geoSegments.get(a.size - 1).p2
	 *   && this.endHeading   = geoSegments.get(a.size - 1).heading
	 *   && this.length       =  sum (0 &le; i &lt; a.size) . geoSegments.get(i).length
	 *   && for all segments, getName() is equal to any other segment
	 */
	
    //FIELDS
	
	/** The start point of this feature*/
	private final GeoPoint start;
	/** The end point of this feature*/
	private final GeoPoint end;
	/** The direction at the beginning of the feature*/
	private final double startHeading;
	/** The direction at the end of the feature */
	private final double endHeading;
	/** A sequential collection of segments that make up this feature */
	private final List<GeoSegment> geoSegments;
	/** The name of this feature */
	private final String name;
	/** the length of this feature */
	private final double length;

	
    // Constructors

    /**
     * Constructs a new GeoFeature.
     * @requires gs != null
     * @effects Constructs a new GeoFeature, r, such that
     *          r.name = gs.name &&
     *          r.startHeading = gs.heading &&
     *          r.endHeading = gs.heading &&
     *          r.start = gs.p1 &&
     *          r.end = gs.p2
     **/
    public GeoFeature(GeoSegment gs) {
        if(gs == null){
        	throw new IllegalArgumentException("specified geosegment " +
        			"cannot be null");
        }
        start = gs.getP1();
        end = gs.getP2();
        startHeading = gs.getHeading();
        endHeading = startHeading;
        geoSegments = new LinkedList<GeoSegment>();
        geoSegments.add(gs);
        name = gs.getName();
        length = gs.getLength();
    }
    
    /**
     * private constructor to construct a new GeoFeature from a list of GeoSegments
     */
    private GeoFeature(List<GeoSegment> list, double length){
        start = list.get(0).getP1();
        end = list.get(list.size() - 1).getP2();

        /*startHeading = list.get(0).getHeading();
        endHeading = list.get(list.size() - 1).getHeading();*/
        startHeading = checkNaN(list, 0, 1, list.size());
        endHeading = checkNaN(list, list.size() - 1, -1, -1);
        
        geoSegments = list;
        name = list.get(0).getName();
        this.length = length;
        
        //checkRep();
    }

    
    /**
     * helper to determine the nearest non-zero-length segment and return its valid heading
     */
    private double checkNaN(List<GeoSegment> list, int start, int step, int stop){
	    if( start == stop){
	    	return Double.NaN;
	    }else{
	    	double head = list.get(start).getHeading();
	    	if(Double.isNaN(head)){
		    	return checkNaN(list, start+step, step, stop);
		    }else{
		    	return head;
		    }
	    }
    }
    

    /**
     * Checks that the representation invariant holds (if any).
     **/
    // Throws a RuntimeException if the rep invariant is violated.
    private void checkRep() throws RuntimeException {
    	if(!getStart().equals(geoSegments.get(0).getP1())){
    		throw new RuntimeException("The start point for the feature " +
    				"should be the first point of the first segment");
    	}else if(!Double.isNaN(getStartHeading() + geoSegments.get(0).getHeading())
    			&& getStartHeading() !=	geoSegments.get(0).getHeading()){
    		throw new RuntimeException("The start heading should be the " +
    				"heading of the first segment");
    	}else if(!getEnd().equals(geoSegments.get(geoSegments.size() - 1).
    			getP2())){
    		throw new RuntimeException("The end point should be the second " +
    				"point of the last segment");
    	}else if(!Double.isNaN(getEndHeading() + geoSegments.get(geoSegments.
    			size() - 1).getHeading()) && getEndHeading() != 
    			geoSegments.get(geoSegments.size() - 1).getHeading()){
    		throw new RuntimeException("The end heading should be the " +
    				"heading of the last segment");
    	}
    	double sum = 0;
    	for(GeoSegment gs : geoSegments){
    		sum += gs.getLength();
    	}
    	if(getLength() != sum){
    		throw new RuntimeException("The sum of the lengths of the " +
    				"segments making up the feature should equal the " +
    				"length of the feature");
    	}
    }


    // Observers

    /**
     * @return name of geographic feature
     **/
    public String getName() {
        return name;
    }


    /**
     * @return location of the start of the feature
     **/
    public GeoPoint getStart() {
        return start;
    }


    /**
     * @return location of the end of of the feature
     **/
    public GeoPoint getEnd() {
        return end;
    }


    /**
     * @return direction (in standard heading) of travel at the start
     * of the feature, in degrees, or NaN iff all segments of this 
     * feature are zero-length segments.  (if the start segment is zero-length,
     * will return the heading of the first non-zero segment iterating forwards)
     **/
    public double getStartHeading() {
        return startHeading;
    }


    /**
     * @return direction of travel at the end of the feature, in
     * degrees, or NaN iff all segments of this feature are zero-length
     * segments. (if the end segment is zero-length, will return the 
     * heading of the first non-zero segment iterating backwards)
     **/
    public double getEndHeading() {
        return endHeading;
    }


    /**
     * @return total length of the geo feature, in miles.  NOTE: this
     * is NOT as-the-crow-flies, but rather the total distance
     * required to traverse the geo feature.  These values are not
     * necessarily equal.
     **/
    public double getLength() {
        return length;
    }


    // Producers

    /**
     * Creates a new GeoFeature that is equal to this GeoFeature with
     * gs appended to its end.
     *
     * @requires gs != null && gs.p1 = this.end && gs.name = this.name
     * @return a new GeoFeature r such that
     *       r.end = gs.p2
     *    && r.endHeading = gs.heading
     *    && r.length = this.length + gs.length
     **/
    public GeoFeature addSegment(GeoSegment gs) {
        if(gs == null){
        	throw new IllegalArgumentException("specified GeoSegment must " +
        			"not be null");
        }else if(!gs.getP1().equals(getEnd()) || !gs.getName().equals(getName())){
        	throw new IllegalArgumentException("specified GeoSegment must " +
        			"have same name and its start point must match this feature's" +
        			" end point");
        }
        
        List<GeoSegment> list = new LinkedList<GeoSegment>();
        for(GeoSegment g : geoSegments){
        	list.add(g);
        }
        list.add(gs);
        return new GeoFeature(list, length+gs.getLength());
    }


    // Observers

    /**
     * Returns a List of GeoSegment objects.  The concatenation
     * of the GeoSegments, in order, is equivalent to this GeoFeature.
     * All the GeoSegments should have the same name.
     * @return a List of GeoSegments such that
     * <pre>
     *      this.start        = a.get(0).p1
     *   && this.startHeading = a.get(0).heading
     *   && this.end          = a.get(a.size - 1).p2
     *   && this.endHeading   = a.get(a.size - 1).heading
     *   && this.length       =  sum (0 &le; i &lt; a.size) . a.get(i).length
     *   && for all integers i .
     *          (0 &le; i &lt; a.size - 1 &rArr; (a.get(i).name = a.get(i+1).name &&
     *                                              a.get(i).p2 = a.get(i+1).p1))
     * </pre>
     * @see ps2.GeoSegment
     */
    public List<GeoSegment> getGeoSegments() {
    	return Collections.unmodifiableList(geoSegments);
    }


    /**
     * Compares the argument with this GeoFeature for equality.
     * @return o != null && (o instanceof GeoFeature)
     *         && (o.geoSegments and this.geoSegments contain
     *             the same elements in the same order).
     **/
    public boolean equals(Object o) {
        if (o == null || !(o instanceof GeoFeature)){
        	return false;
        }
        
        List<GeoSegment> list = ((GeoFeature) o).getGeoSegments();
        if (list.size() != geoSegments.size()){
        	return false;
        }
        
        int i = 0;
    	while ( i < geoSegments.size()){
    		if (!geoSegments.get(i).equals(list.get(i))){
    			return false;
    		}
    		i++;
    	}
    	return true;
    }
    

    /**
     * @return a valid hashcode for this.
     **/
    public int hashCode() {
    	int mod = getName().hashCode() + 1;
    	int hash = getStart().hashCode() + getEnd().hashCode() % (mod + 1) + 1;
    	for(GeoSegment gs : geoSegments){
    		hash = (hash * gs.hashCode()) % (mod + 1) + 1;
    	}
    	return hash;
    }


    /**
     * @return a string representation of this.
     **/
    public String toString() {
        return "<"+getName()+" from "+getStart()+" @ heading "+
        		getStartHeading()+", to "+getEnd()+" @ "+
        		getEndHeading()+">";
    }

} // GeoFeature
