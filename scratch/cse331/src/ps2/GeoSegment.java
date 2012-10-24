package ps2;

/**
 * <p>
 * A GeoSegment models a straight line segment on the earth.
 * GeoSegments are immutable.
 * </p>
 *
 * <p>
 * A compass heading is a nonnegative real number less than 360.  In
 * compass headings, north = 0, east = 90, south = 180, and west =
 * 270.
 * </p>
 *
 * <p>
 * When used in a map, a GeoSegment might represent part of a street,
 * boundary, or other feature.
 * As an example usage, this map
 * <pre>
 *  Penny Lane  a
 *              |
 *              i--j--k  Abbey Road
 *              |
 *              z
 * </pre>
 * could be represented by the following GeoSegments:
 * ("Penny Lane", a, i), ("Penny Lane", z, i),
 * ("Abbey Road", i, j), and ("Abbey Road", j, k).
 * </p>
 *
 * <p>
 * A name is given to all GeoSegment objects so that it is possible to
 * differentiate between two GeoSegment objects with identical
 * GeoPoint endpoints.  Equality between GeoSegment objects requires
 * that the names be equal String objects and the respective start and end
 * points be equal GeoPoint objects.
 * </p>
 * 
 * 
 * 
 *
 * @specfield  name : String       // name of the geographic feature identified
 * @specfield  p1 : GeoPoint       // first endpoint of the segment
 * @specfield  p2 : GeoPoint       // second endpoint of the segment
 * @derivedfield length : real     // straight-line distance between p1 and p2, in miles
 * @derivedfield  heading : angle  // compass heading from p1 to p2, in degrees
 **/
public strictfp class GeoSegment  {

	
	 /*
	 * ABSTRACTION FUNCTION
	 * name  >>  the name of this geographic feature
	 * p1  >>  the location of the first endpoint of this segment
	 * p2  >>  the location of the second endpoint of this segment
	 * length  >>  the straight-line distance between p1 and p2, in miles
	 * heading  >>  the compass heading from p1 to p2, in degrees
	 * 
	 * 
	 * REPRESENTATION INVARIANT
	 * 0 < heading < 360
	 */
	
    // FIELDS

	/**The name of this GeoSegment */
	private final String name;
	/**The starting GeoPoint of this segment */
	private final GeoPoint p1;
	/**The ending GeoPoint of this segment */
	private final GeoPoint p2;
	/**The distance between p1 and p2*/
	private final double length;
	/**The heading from p1 to p2 */
	private final double heading;
	/**The hash code of this segment*/
	private final int hash;
	
	
    // Constructors

    /**
     * @requires name != null && p1 != null && p2 != null
     * @effects constructs a new GeoSegment with the specified
     * name and endpoints
     **/
    public GeoSegment(String name, GeoPoint p1, GeoPoint p2) {
        if(name == null || p1 == null || p2 == null){
        	throw new IllegalArgumentException("Input must not be null");
        }
        this.name = name;
        this.p1 = p1;
        this.p2 = p2;
        length = p1.distanceTo(p2);
        heading = p1.headingTo(p2);
        hash = (p1.hashCode()*73 + p2.hashCode()*137) % 7134653*(name.hashCode() + 1) + 1;
        
        checkRep();
    }


    /**
     * Checks that the representation invariant holds (if any).
     * @throws RuntimeException if the rep invariant is violated.
     **/
    private void checkRep() throws RuntimeException {
        if((heading < 0 || heading > 360 ||	heading < 0 || heading > 360)){
        	throw new RuntimeException("heading between the segment points"+
        			" must be 0 < heading < 360");
        }
    }


    // Producers

    /**
     * Returns a new GeoSegment like this one, but with its endpoints
     * reversed.
     * @return a new GeoSegment gs such that
     *      gs.name = this.name
     *   && gs.p1 = this.p2
     *   && gs.p2 = this.p1
     **/
    public GeoSegment reverse() {
        return new GeoSegment(getName(), getP2(), getP1());
    }


    // Observers

    /**
     * @return the name of this GeoSegment.
     **/
    public String getName() {
        return name;
    }


    /**
     * @return first endpoint of the segment.
     **/
    public GeoPoint getP1() {
        return p1;
    }


    /**
     * @return second endpoint of the segment.
     **/
    public GeoPoint getP2() {
        return p2;
    }


    /**
     * @return the length of the segment, using the flat-surface,
     * near-Seattle approximation.
     **/
    public double getLength() {
        return length;
    }


    /**
     * @requires this.length != 0
     * @return the compass heading from p1 to p2, in degrees, using
     * the flat-surface, near-Seattle approximation.  
     **/
    // if length == 0, returns Double.NaN
    public double getHeading() {
    	if(getLength() == 0){
    		return Double.NaN;
    	}
        return heading;
    }


    /**
     * Compares the specified Object with this GeoSegment for
     * equality.
     * @return    gs != null && (gs instanceof GeoSegment)
     *         && gs.name = this.name && gs.p1 = this.p1 && gs.p2 = this.p2
     **/
    public boolean equals(Object gs) {
        if(gs == null || !(gs instanceof GeoSegment)){
        	return false;
        }
        
        return (((GeoSegment) gs).getName().equals(name) && ((GeoSegment) gs).
        		getP1().equals(p1) && ((GeoSegment) gs).getP2().equals(p2)); 
    }


    /**
     * @return a valid hashcode for this.
     **/
    public int hashCode() {
        return hash;
    }


    /**
     * @return a string representation of this.
     **/
    public String toString() {
        return "["+name+", from "+p1+" to "+p2+"]";
    }

} // GeoSegment
