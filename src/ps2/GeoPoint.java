package ps2;


/**
 * <p>
 * A GeoPoint models a point on the earth.  GeoPoints are immutable.
 * </p>
 *
 * <p>
 * North latitudes and east longitudes are represented by positive
 * numbers.  South latitudes and west longitudes are represented by
 * negative numbers.
 * </p>
 *
 * <p>
 * The code may assume that the represented points are nearby Seattle.
 * </p>
 *
 * <p>
 * <b>Implementation hint</b>:<br /> Seattle is at approximately 47
 * deg. 36 min. 35 sec. N latitude and 122 deg. 19 min. 59 sec. W
 * longitude.  There are 60 minutes per degree, and 60 seconds per
 * minute.  So, in decimal, these correspond to 47.609722 North
 * latitude and -122.333056 East longitude.  The constructor takes
 * integers in millionths of degrees.  To create a new GeoPoint
 * located in Seattle, use: <tt>GeoPoint seattle = new
 * GeoPoint(47609722, -122333056);</tt>
 * </p>
 *
 * <p>
 * Near Seattle, there are approximately 69.04 miles per degree of
 * latitude and 47.574 miles per degree of longitude.  An
 * implementation should use these values when determining distances
 * and headings.
 * </p>
 * 
 *
 * @specfield  latitude :  real     // measured in degrees latitude
 * @specfield  longitude : real     // measured in degrees longitude
 **/
public strictfp class GeoPoint {

	 /* 
	 * ABSTRACTION FUNCTION
	 * latitude  >>  the latitude in millionths of degrees of this location
	 * longitude  >>  the longitude in millionths of degrees of this location
	 * 
	 * REPRESENTATION INVARIANT
	 * -90000000 < latitude < 90000000
	 * -180000000 < longitude < 180000000
	 */ 
	
	
    /** Minimum value the latitude field can have in this class. **/
    public static final int MIN_LATITUDE  =  -90 * 1000000;
    /** Maximum value the latitude field can have in this class. **/
    public static final int MAX_LATITUDE  =   90 * 1000000;
    /** Minimum value the longitude field can have in this class. **/
    public static final int MIN_LONGITUDE = -180 * 1000000;
    /** Maximum value the longitude field can have in this class. **/
    public static final int MAX_LONGITUDE =  180 * 1000000;


    /**
     * Approximation used to determine distances and headings using a
     * "flat earth" simplification.
     **/
    public static final double MILES_PER_DEGREE_LATITUDE = 69.04;

    /**
     * Approximation used to determine distances and headings using a
     * "flat earth" simplification.
     **/
    public static final double MILES_PER_DEGREE_LONGITUDE = 46.574;


    // FIELDS
    
    /**
     * This GeoPoint's latitude in millionths of degrees
     */
    private final int latitude;
    
    /**
     * This GeoPoint's longitude in millionths of degrees
     */
    private final int longitude;

    /**The hash code for this point*/
    private final int hash;
    
    // Constructors

    /**
     * @requires the (latitude, longitude) point expressed in
     * millionths of a degree is valid, such that
     * MIN_LATITUDE <= latitude <= MAX_LATITUDE and
     * MIN_LONGITUDE <= longitude <= MAX_LONGITUDE
     * 
     * @effects constructs a GeoPoint from a latitude and
     * longitude given in millionths of degrees.
     **/
    public GeoPoint(int latitude, int longitude) {
    	this.latitude = latitude;
    	this.longitude = longitude;
    	hash = Math.abs(latitude - longitude);
    	checkRep();
    }


    /**
     * Checks that the representation invariant holds (if any).
     * @throws RuntimeException if the rep invariant is violated.
	 **/
    private void checkRep() {
		if(MIN_LATITUDE > latitude || latitude > MAX_LATITUDE){
			throw new RuntimeException("invalid latitude");
		}else if(MIN_LONGITUDE > longitude || longitude > MAX_LONGITUDE){
			throw new RuntimeException("invalid longitude");
		}
    }


    // Observers

    /**
     * the latitude of the GeoPoint object, in millionths of degrees.
     **/
    public int getLatitude() {
        return latitude;
    }


    /**
     * the longitude of the GeoPoint object, in millionths of degrees.
     **/
    public int getLongitude() {
        return longitude;
    }


    /**
     * Computes the distance between GeoPoints.
     * @requires gp != null
     * @throws IllegalArgumentException is gp == null
     * @return The distance, in miles, from this to gp, using the
     * flat-surface, near Seattle approximation.
     **/
    public double distanceTo(GeoPoint gp) {
        if(gp == null){
        	throw new IllegalArgumentException("specified GeoPoint gp cannot be null");
        }
        double lat = (gp.getLatitude() - latitude) * MILES_PER_DEGREE_LATITUDE /
        		1000000;
        double lon = (gp.getLongitude() - longitude) * MILES_PER_DEGREE_LONGITUDE /
        		1000000;
        return Math.sqrt(Math.pow(lat, 2) + Math.pow(lon, 2)); 
    }


    /**
     * Computes the compass heading between GeoPoints.
     * @requires gp != null && !this.equals(gp)
     * @throws IllegalArgumentException if gp == null
     * @return The compass heading h from this to
     * gp, in degrees, using the flat-surface, near Seattle
     * approximation, such that 0 &le; h &lt; 360.  In compass
     * headings, north = 0, east = 90, south = 180, and west = 270.
     **/
    // If gp is at the same GeoPoint as this, returns Double.NaN
    public double headingTo(GeoPoint gp) {
        if(gp == null){
        	throw new IllegalArgumentException("specified GeoPoint " +
        			"cannot be null");
        }else if(this.equals(gp)){
        	// if this is the same point as gp, returns Double.NaN
        	return Double.NaN;
        }
        
    	double delLat = (gp.getLatitude() - latitude) * MILES_PER_DEGREE_LATITUDE /
    			1000000;
        double delLon = (gp.getLongitude() - longitude) * MILES_PER_DEGREE_LONGITUDE /
        		1000000;
        double deg = Math.toDegrees(Math.atan2(delLat, delLon));
        
        deg = 90 - deg;
        if(deg < 0){
        	deg = deg + 360;
        }
        return deg;
    }

    /**
     * Compares the specified Object with this GeoPoint for equality.
     * @return gp != null && (gp instanceof GeoPoint)
     *         && gp.latitude = this.latitude && gp.longitude = this.longitude
     **/
    public boolean equals(Object gp) {
    	if(gp == null || hashCode() != gp.hashCode()){
    		return false;
    	}
    	return ((GeoPoint) gp).getLatitude() == latitude && ((GeoPoint) gp).
    			getLongitude() == longitude;
    }


    /**
     * @return a valid hashcode for this GeoPoint.
     **/
    public int hashCode() {
        return Math.abs(latitude * longitude);
    }


    /**
     * @return a string representation of this GeoPoint.
     * representation is of the form: "(latitude, longitude)" .
     **/
    public String toString() {
        return "("+latitude+", "+longitude+")";
    }

    
} // GeoPoint
