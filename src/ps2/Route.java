package ps2;

import java.util.*;


/**
 * <p>
 * A Route is a path that traverses arbitrary GeoSegments, regardless
 * of their names.
 * </p>
 *
 * <p>
 * Routes are immutable.  New Routes can be constructed by adding a
 * segment to the end of a Route.  An added segment must be properly
 * oriented; that is, its p1 field must correspond to the end of the
 * original Route, and its p2 field corresponds to the end of the new
 * Route.
 * </p>
 *
 * <p>
 * Because a Route is not necessarily straight, its length -- the
 * distance traveled by following the path from start to end -- is not
 * necessarily the same as the distance along a straight line between
 * its endpoints.
 * </p>
 *
 * <p>
 * Lastly, a Route may be viewed as a sequence of geographical
 * features, using the <tt>getGeoFeatures()</tt> method which returns
 * a List<GeoFeature>.
 * </p>
 * 
 * 
 *
 * @specfield start : GeoPoint         // location of the start of the route
 * @specfield end : GeoPoint           // location of the end of the route
 * @specfield startHeading : angle     // direction of travel at the start of the route, in degrees
 * @specfield endHeading : angle       // direction of travel at the end of the route, in degrees
 * @specfield geoFeatures : sequence   // a sequence of geographic features that make up this Route
 * @specfield geoSegments : sequence   // a sequence of segments that make up this Route
 * @derivedfield length : real         // total length of the route, in miles
 * @derivedfield endingGeoSegment : GeoSegment   // last GeoSegment of the route
 **/
public class Route {
    
	 /*
	 * ABSTRACTION FUNCTION
	 *  start  >>  the location of the start of this route
	 *  end  >>  the location of the end of this route
	 *  startHeading  >>  the direction of travel in compass degrees at the start of the route
	 *  endHeading  >>  direction of travel in compass degrees at the end of the route
	 *  geoFeatures  >>  a sequence of geographic features that make up this Route
	 *  geoSegments  >>  a sequence of feature-segments that make up this Route
	 *  length  >>  the total length of the route, in miles
	 *  endingGeoSegment  >>  the last feature-segment of the route
	 * 
	 * 
	 * REPRESENTATION INVARIANT
	 *   this.start           = geoSegments.get(0).p1
	 *   && this.startHeading = geoSegments.get(0).heading
	 *   && this.end          = geoSegments.get(a.size - 1).p2
	 *   && this.endHeading   = geoSegments.get(a.size - 1).heading
	 *   && this.length       =  sum (0 &le; i &lt; a.size) . geoSegments.get(i).length
	 */
	
	
	//FIELDS
	
	/** The start point of this route*/
	private final GeoPoint start;
	/** The end point of this route*/
	private final GeoPoint end;
	/** The direction at the beginning of the route*/
	private final double startHeading;
	/** The direction at the end of the  */
	private final double endHeading;
	/** A sequential collection of features that make up this route*/
	private final List<GeoFeature> geoFeatures;
	/** A sequential collection of segments that make up this route */
	private final List<GeoSegment> geoSegments;
	/** the length of this route */
	private final double length;
	/** the last segment of this route*/
	private final GeoSegment endingGeoSegment;
	
    // Constructors

    /**
     * @requires gs != null
     * @effects Constructs a new Route containing the specified geosegment.
     **/
    public Route(GeoSegment gs) {
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
        geoFeatures = new LinkedList<GeoFeature>();
        geoFeatures.add(new GeoFeature(gs));
        length = gs.getLength();
        endingGeoSegment = gs;
        
        //checkRep();
    }

    /**
     * method to construct a new GeoFeature from a list of GeoSegments
     */
    private Route(List<GeoSegment> segments, List<GeoFeature> features, double length){
        start = segments.get(0).getP1();
        end = segments.get(segments.size() - 1).getP2();
        
        /*startHeading = segments.get(0).getHeading();
        endHeading = segments.get(segments.size() - 1).getHeading();*/
        startHeading = checkNaN(segments, 0, 1, segments.size());
        endHeading = checkNaN(segments, segments.size() - 1, -1, -1);
        
        geoSegments = segments;
        geoFeatures = features;
        this.length = length;
        endingGeoSegment = segments.get(segments.size() - 1);
        
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
    	
    	int i = 0;
    	String name = geoFeatures.get(i).getName();
    	while(i < geoFeatures.size() - 1){
    		i++;
    		if(name.equals(geoFeatures.get(i))){
    			throw new RuntimeException("Consecutive geoFeatures cannot have" +
    					" the same name");
    		}
    	}
    }


    // Observers

    /**
     * @return location of the start of the route
     **/
    public GeoPoint getStart() {
        return start;
    }


    /**
     * @return location of the end of the route
     **/
    public GeoPoint getEnd() {
        return end;
    }

    /**
     * @return the last GeoSegment of the route
     **/
    public GeoSegment getEndingGeoSegment() {
        return endingGeoSegment;
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
     * @return total length of the route, in miles.  NOTE: this is NOT
     * as-the-crow-flies, but rather the total distance required to
     * traverse the route.  These values are not necessarily equal.
     */
    public double getLength() {
        return length;
    }


    // Producers

    /**
     * Creates a new route that is equal to this route with gs
     * appended to its end.
     * @requires gs != null && gs.p1 = this.end
     * @return a new Route r such that
     *       r.end = gs.p2
     *    && r.endHeading = gs.heading
     *    && r.length = this.length + gs.length
     **/
    public Route addSegment(GeoSegment gs) {
        if(gs == null){
        	throw new IllegalArgumentException("specified GeoSegment must " +
        			"not be null");
        }else if(!gs.getP1().equals(getEnd())){
        	throw new IllegalArgumentException("specified GeoSegment must " +
        			"have its start point must match this route's" +
        			" end point");
        }
        
        
        List<GeoSegment> segments = new LinkedList<GeoSegment>();
        for(GeoSegment g : geoSegments){
        	segments.add(g);
        }
        segments.add(gs);
        
        
        List<GeoFeature> features = new LinkedList<GeoFeature>();
        int i = 0;
        while(i < geoFeatures.size() - 1){
        	features.add(geoFeatures.get(i));
        	i++;
        }
        
        GeoFeature gf = geoFeatures.get(i);
        if(gf.getName().equals(gs.getName())){
        	features.add(gf.addSegment(gs));
        }else{
        	features.add(gf);
        	features.add(new GeoFeature(gs));
        }
        
        return new Route(segments, features, length+gs.getLength());
    }


    /**
     * Returns a List of GeoFeature objects.  The
     * concatenation of the GeoFeatures, in order, is equivalent to
     * this route.  No two consecutive GeoFeature objects have the
     * same name.<p>
     *
     * @return a List of GeoFeatures such that
     * <pre>
     *      this.start        = a.get(0).start
     *   && this.startHeading = a.get(0).startHeading
     *   && this.end          = a.get(a.length - 1).end
     *   && this.endHeading   = a.get(a.length - 1).endHeading
     *   && this.length       =  sum (0 &le; i &lt; a.size) . a.get(i).length
     *   && for all integers i .
     *          (0 &le; i &lt; a.size - 1 &rArr; (a.get(i).name != a.get(i+1).name &&
     *                                       a.get(i).end   = a.get(i+1).start))
     * </pre>
     * @see ps2.GeoFeature
     **/
    public List<GeoFeature> getGeoFeatures() {
        return Collections.unmodifiableList(geoFeatures);
    }


    /**
     * Returns a List of GeoSegment objects.  The concatentation
     * of the GeoSegments, in order, is equivalent to this route.
     * @return a List of GeoSegments such that
     * <pre>
     *      this.start        = a.get(0).p1
     *   && this.startHeading = a.get(0).heading
     *   && this.end          = a.get( a.size - 1 ).p2
     *   && this.endHeading   = a.get( a.size - 1 ).heading
     *   && this.length       =  sum (0 &le; i &lt; a.size) . a.get(i).length
     *   && for all integers i .
     *          (0 &le; i &lt; a.size-1 &rArr; (a.get(i).p2 = a.get(i+1).p1))
     * </pre>
     * @see ps2.GeoSegment
     */
    public List<GeoSegment> getGeoSegments() {
        return Collections.unmodifiableList(geoSegments);
    }


    /**
     * Compares the specified Object with this Route for equality.
     * @return true iff (o instanceof Route)
     *         && (o.geoFeatures and this.geoFeatures contain
     *             the same elements in the same order).
     **/
    public boolean equals(Object o) {
        if (o == null || !(o instanceof Route)){
        	return false;
        }
        
        List<GeoSegment> segments = ((Route) o).getGeoSegments();
        List<GeoFeature> features = ((Route) o).getGeoFeatures();
        if (segments.size() != geoSegments.size() || features.size() != 
        		geoFeatures.size()){
        	return false;
        }
        
        int i = 0;
    	while ( i < geoSegments.size()){
    		if (!geoSegments.get(i).equals(segments.get(i)) || 
    				(i < geoFeatures.size() && !geoFeatures.get(i).
    				equals(features.get(i)))){
    			return false;
    		}
    		i++;
    	}
    	
    	return true;
        
    }


    /**
     * @return a valid hash code for this.
     **/
    public int hashCode() {
    	int mod = endingGeoSegment.hashCode() + 1;
    	int hash = getStart().hashCode() + getEnd().hashCode() % (mod + 1) + 1;
    	for(GeoFeature gf : geoFeatures){
    		hash = (hash * gf.hashCode()) % (mod + 1) + 1;
    	}
    	return hash;
    }


    /**
     * @return a string representation of this.
     **/
    public String toString() {
        String result = "Route along the following streets: ";
        for(GeoFeature gf : geoFeatures){
        	result += gf.getName()+" -> ";
        }
    	
    	return result;
    }

} //Route
