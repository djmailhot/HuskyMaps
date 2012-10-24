package ps2.test;

import ps2.*;
import static ps2.test.TestValues.TOLERANCE;

import junit.framework.*;


/**
 * Unit tests for the GeoSegment class.
 **/
public class GeoSegmentTest extends TestCase {

	// GeoPoint equal to location of seattle
	private final GeoPoint SEATTLE = new GeoPoint(47609722, -122333056);
	
	
	// equal to eq2
	private final GeoSegment eq1;
	// equal to eq1
	private final GeoSegment eq2;
	// different than eq1 in point
	private final GeoSegment diffPoint;
	// different than ep1 in name
	private final GeoSegment diffName;
	// zero-distance segment
	private final GeoSegment zero;


	// point north of seattle
	private final GeoPoint north;
	// point south of seattle
	private final GeoPoint south;
	// point east of seattle
	private final GeoPoint east;
	// point west of seattle
	private final GeoPoint west;
	// point northEast of seattle
	private final GeoPoint northEast;
	// point northWest of seattle
	private final GeoPoint northWest;
	// point southEast of seattle
	private final GeoPoint southEast;
	// point southWest of seattle
	private final GeoPoint southWest;
	
	
	// due north segment
	private final GeoSegment gsNorth;
	// heading of 'north'
	private final double nHeading;
	// distance of 'north'
	private final double nDist;

	// due south segment
	private final GeoSegment gsSouth;
	// heading of 'south'
	private final double sHeading;
	// distance of 'south'
	private final double sDist;

	// due east segment
	private final GeoSegment gsEast;
	// heading of 'east'
	private final double eHeading;
	// distance of 'east'
	private final double eDist;

	// due west segment
	private final GeoSegment gsWest;
	// heading of 'west'
	private final double wHeading;
	// distance of 'west'
	private final double wDist;

	
	// north east segment, equal to the reverse of southWest
	private final GeoSegment gsNorthEast;
	// heading of 'northEast'
	private final double neHeading;
	// distance of 'northEast'
	private final double neDist;

	// north west segment, equal to the reverse of southEast
	private final GeoSegment gsNorthWest;
	// heading of 'northWest'
	private final double nwHeading;
	// distance of 'northWest'
	private final double nwDist;

	// south east segment, equal to the reverse of northWest
	private final GeoSegment gsSouthEast;
	// heading of 'southEast'
	private final double seHeading;
	// distance of 'southEast'
	private final double seDist;

	// south west segment, equal to the reverse of northEast
	private final GeoSegment gsSouthWest;
	//heading of 'southWest'
	private final double swHeading;
	// distance of 'southWest'
	private final double swDist;
	
	
	
    public GeoSegmentTest(String name) {
        super(name);
        
        
        /** direction points are relative GeoPoint value of seattle **/
        north = new GeoPoint(locationToInt(47, 42, 35),
                             locationToInt(-122, 19, 59));
        south = new GeoPoint(locationToInt(47, 30, 35),
                             locationToInt(-122, 19, 59));
        east = new GeoPoint(locationToInt(47, 36, 35),
                            locationToInt(-122, 13, 59));
        west = new GeoPoint(locationToInt(47, 36, 35),
                            locationToInt(-122, 25, 59));
        northEast = new GeoPoint(locationToInt(47, 42, 35),
                                 locationToInt(-122, 13, 59));
        southWest = new GeoPoint(locationToInt(47, 30, 35),
                                 locationToInt(-122, 25, 59));
        northWest = new GeoPoint(locationToInt(47, 42, 35),
                                 locationToInt(-122, 25, 59));
        southEast = new GeoPoint(locationToInt(47, 30, 35),
                                 locationToInt(-122, 13, 59));
        
        
        /** initialization of working segments **/
        GeoPoint pA = new GeoPoint(locationToInt(47, 36, 35), 
        		locationToInt(-122, 19, 59));
        GeoPoint pB = new GeoPoint(locationToInt(47, 30, 35), 
        		locationToInt(-122, 31, 59));
        
        eq1 = new GeoSegment("Gee Wilker's Rd", pA, pB);
        eq2 = new GeoSegment("Gee Wilker's Rd", pA, pB);
        diffName = new GeoSegment("Gee By Golly Gulch", pA, pB);
        diffPoint = new GeoSegment("Gee Wilker's Rd", offset(pA, locationToInt
        		(0, 2, 0), 0), offset(pB, 0, locationToInt(-0, 2, 0)));
        zero = new GeoSegment("Nowhere Lane", SEATTLE, SEATTLE);
        
        
        // all real values derived from hard-coded values of GeoPointTest.java
        /** True directions, with starting point at seattle **/
        gsNorth = new GeoSegment("N rd", SEATTLE, north);
        nHeading = 0;
        nDist = 6.904;
        
        gsSouth = new GeoSegment("S rd", SEATTLE, south);
        sHeading = 180;
        sDist = 6.904;

        gsEast = new GeoSegment("E rd", SEATTLE, east);
        eHeading = 90;
        eDist = 4.6574;
        
        gsWest = new GeoSegment("W rd", SEATTLE, west);
        wHeading = 270;
        wDist = 4.6574;
        
        
        /** diagonal directions from seattle, NE is reverse of SW, NW is reverse of SE **/
        gsNorthEast = new GeoSegment("NE rd", southWest, northEast);
        neHeading = 34.0033834;
		neDist = 8.328060444 * 2;
		
        gsSouthWest = new GeoSegment("SW rd", northEast, southWest);
        swHeading = 214.0033834;
        swDist = 8.328060444 * 2;
        
        gsNorthWest = new GeoSegment("NW rd", southEast, northWest);
        nwHeading = 325.9966166;
		nwDist = 8.328060444 * 2;
		
        gsSouthEast = new GeoSegment("SE rd", northWest, southEast);
        seHeading = 145.9966166;
		seDist = 8.328060444 * 2;

    }
    
    
    /**
     * helper to create a new GeoPoint offset from an original
     * @return a new GeoPoint offset by the specified lat and long
     */
    GeoPoint offset(GeoPoint pt, int lat, int lon){
    	return new GeoPoint(pt.getLatitude() + lat, pt.getLongitude() + lon);
    }
    
    
    /**
     * Convert a degree, minutes, seconds latitude or longitude value
     * to its associated integer value (to millionths of a degree).
     * Allows invalid locations to be specified for testing, but
     * doesn't allow negative minute/second values.
     **/
    static int locationToInt(int deg, int min, int sec) {
        if (min < 0 || sec < 0) {
            throw new RuntimeException("min and sec must be positive!");
        }

        double dmin = (double)min / 60;
        double dsec = (double)sec / (60 * 60);
        if (deg >= 0) {
            return ((int)Math.round(1000000 * (deg + dmin + dsec)));
        } else {
            return ((int)Math.round(1000000 * (deg - dmin - dsec)));
        }
    }
    
    /**
     * Sanity check for our locationToInt() method
     **/
    public void testLocationToIntMethod() {
        int lat = locationToInt(42, 21, 30);
        int lng = locationToInt(-71, 3, 37);
        if (lat != 42358333) {
            fail("42 21 30 latitude must be 42358333, is " + lat);
        } else if (lng != -71060278) {
            fail("-71 3 37 longitude must be -71060278, is " + lng);
        }
    }
    
    
    // JUnit calls setUp() before each test__ method is run
    protected void setUp() {
    }
    
    
    //////////////////////////////////////////////////////
    // TEST LEGAL/ILLEGAL VALUES IN GeoSegment CREATION //
    //////////////////////////////////////////////////////
    
    /**
     * Tests that a GeoSegment will output the constructor-passed properties.
     **/
    public void checkLegalValues() {
    	GeoPoint pt1 = offset(SEATTLE, 2000, -7000);
    	GeoPoint pt2 = offset(SEATTLE, 300, 11200);
    	
    	GeoSegment gs = null;
        String name = "Segment Rd";
        try {
            gs = new GeoSegment(name, pt1, pt2);
        } catch (Exception ex) {
            // Failed
            fail("Didn't allow legal (start P1, end P2) of (" +
            		pt1 + ", " + pt2 + ")");
            return;
        }

        if (!gs.getName().equals(name)){
        	fail("Name should have been set to " + name + ", was set to "+
        			gs.getName());
        }else if (gs.getLength() != pt1.distanceTo(pt2)) {
            fail("Length should have been set to " + pt1.distanceTo(pt2) +
            		", was set to " + gs.getLength() + " instead.");

        }else if (gs.getHeading() != pt1.headingTo(pt2)) {
            fail("Longitude should have been set to " + pt1.headingTo(pt2) +
            		", was set to " + gs.getHeading() + " instead.");
        }
    }    
    
    ///////////////////////////////
    // TEST GeoSegment.reverse() //
    ///////////////////////////////
    
    /**
     * test positive reverse: the headings and distances of the four prime directions
     */
    public void testReverseCompassDirections(){
    	
    	// true directions
    	testReverse(gsNorth, nDist, nHeading);
    	testReverse(gsSouth, sDist, sHeading);
    	testReverse(gsEast, eDist, eHeading);
    	testReverse(gsWest, wDist, wHeading);
    	assertTrue("A reversed north is equal to south", gsNorth.reverse().
    			getHeading() == gsSouth.getHeading());
    	assertTrue("A reversed east is equal to west", gsEast.reverse().
    			getHeading() == gsWest.getHeading());
    	
    	// diagonal directions
    	testReverse(gsNorthEast, neDist, neHeading);
    	testReverse(gsNorthWest, nwDist, nwHeading);
    	testReverse(gsSouthEast, seDist, seHeading);
    	testReverse(gsSouthWest, swDist, swHeading);
    	assertTrue("A reversed northEast will point southWest", gsNorthEast.
    			reverse().getHeading() == gsSouthWest.getHeading());
    	assertTrue("A reversed northWest will point soutEast", gsNorthWest.
    			reverse().getHeading() == gsSouthEast.getHeading());
    }
    
    /**
     * test positive reverse: the headings and distances of four secondary directions
     */
    public void testReverseZeroLength(){
    	assertTrue("A reversed zero-length segment should be equal to itself",
    			zero.reverse().equals(zero));
    }

    /**
     * helper to test properties of reversed segments
     */
    void testReverse(GeoSegment gs, double len, double hea){
    	GeoSegment rev = gs.reverse();
    	if(hea - 180 < 0){
    		hea += 180;
    	}else{
    		hea -= 180;
    	}
    	
    	assertEquals("A reversed segment must have the same length", 
    			len, rev.getLength(), TOLERANCE);
    	assertEquals("A reversed segment must have the opposite heading",
    			hea, rev.getHeading(), TOLERANCE);
    	assertTrue("A reversed segment should not be equal to itself",
    			!rev.equals(gs));
    	assertTrue("A double reversed segment should be equal to itself",
    			rev.reverse().equals(gs));
    }
    
    
    /////////////////////////////////
    // TEST GeoSegment.getLength() //
    /////////////////////////////////
    
    /**
     * Tests the length of a due north segment
     */
    public void testLengthDueNorth(){
    	checkLength(gsNorth, nDist);
    }
    
    /**
     * Tests the length of a due south segment
     */
    public void testLengthDueSouth(){
    	checkLength(gsSouth, sDist);
    }
    
    /**
     * Tests the length of a due east segment
     */
    public void testLengthDueEast(){
    	checkLength(gsEast, eDist);
    }
    
    /**
     * Tests the length of a due west segment
     */
    public void testLengthDueWest(){
    	checkLength(gsWest, wDist);
    }
    
    /**
     * Tests the length of a due northEast segment
     */
    public void testLengthDueNorthEast(){
    	checkLength(gsNorthEast, neDist);
    }
    
    /**
     * Tests the length of a due northWest segment
     */
    public void testLengthDueNorthWest(){
    	checkLength(gsNorthWest, nwDist);
    }
    
    /**
     * Tests the length of a due southEast segment
     */
    public void testLengthDueSouthEast(){
    	checkLength(gsSouthEast, seDist);
    }
    
    /**
     * Tests the length of a due southWest segment
     */
    public void testLengthDueSouthWest(){
    	checkLength(gsSouthWest, swDist);
    }
    
    /**
     * Helper to check the length of a specified GeoSegment
     */
    void checkLength(GeoSegment gs, double dis){
    	assertEquals("Length of segment is "+dis+", not "+
    			gs.getLength(), dis, gs.getLength(), TOLERANCE);
    	double man_dist = gs.getP1().distanceTo(gs.getP2());
    	assertEquals("Length of segment is "+dis+", not "+
    			man_dist, dis, man_dist, TOLERANCE);
    	assertEquals("getPx() and getLength() methods do not agree",
    			man_dist, gs.getLength(), TOLERANCE);
    }
    
    
    //////////////////////////////////
    // TEST GeoSegment.getHeading() //
    //////////////////////////////////
    
    /**
     * Tests the heading of a due north segment
     */
    public void testHeadingDueNorth(){
    	checkHeading(gsNorth, nHeading);
    }
    
    /**
     * Tests the heading of a due south segment
     */
    public void testHeadingDueSouth(){
    	checkHeading(gsSouth, sHeading);
    }
    
    /**
     * Tests the heading of a due east segment
     */
    public void testHeadingDueEast(){
    	checkHeading(gsEast, eHeading);
    }
    
    /**
     * Tests the heading of a due west segment
     */
    public void testHeadingDueWest(){
    	checkHeading(gsWest, wHeading);
    }
    
    /**
     * Tests the heading of a due northEast segment
     */
    public void testHeadingDueNorthEast(){
    	checkHeading(gsNorthEast, neHeading);
    }
    
    /**
     * Tests the heading of a due northWest segment
     */
    public void testHeadingDueNorthWest(){
    	checkHeading(gsNorthWest, nwHeading);
    }
    
    /**
     * Tests the heading of a due southEast segment
     */
    public void testHeadingDueSouthEast(){
    	checkHeading(gsSouthEast, seHeading);
    }
    
    /**
     * Tests the heading of a due southWest segment
     */
    public void testHeadingDueSouthWest(){
    	checkHeading(gsSouthWest, swHeading);
    }
    
    /**
     * Helper to check the heading of a specified GeoSegment
     */
    void checkHeading(GeoSegment gs, double hea){
    	assertEquals("Heading of segment is "+hea+", not "+
    			gs.getHeading(), hea, gs.getHeading(), TOLERANCE);
    	double man_hea = gs.getP1().headingTo(gs.getP2());
    	assertEquals("Heading of segment is "+hea+", not "+
    			man_hea, hea, man_hea, TOLERANCE);
    	assertEquals("getPx() and getHeading() methods do not agree",
    			man_hea, gs.getHeading(), TOLERANCE);
    }
    
    //////////////////////////////
    // TEST GeoSegment.equals() //
    //////////////////////////////
    
    /**
     * test positive equals same object
     */
    public void testEqualsSame(){
    	assertTrue("The same segment must be equal to itself",
    			eq1.equals(eq1));
    }
    
    /**
     * test positive equals different object
     */
    public void testEqualsDiff(){
    	assertTrue("Two segments of the same name and points" +
    			" must be equal", eq1.equals(eq2));
    }
    
    /**
     * test negative equals of same points
     */
    public void testNotEqualsBadName(){
    	assertTrue("Two segments of different names are not " +
    			"equal", !eq1.equals(diffName));
    }
    
    /**
     * test negative equals of same name
     */
    public void testNotEqualsBadPoints(){
    	assertTrue("Two segments of different points are not " +
    			"equal", !eq1.equals(diffPoint));
    }
    
    /**
     * test null case
     */
    public void testNotEqualsNull(){
    	assertTrue("A null object cannot be equal", !eq1.equals(null));
    	assertTrue("A null object cannot be equal", !zero.equals(null));
    }
    
    /**
     * test different types compared
     */
    public void testNotEqualsInvalidType(){
    	if(eq1.equals("")){
    		fail("An object of different type cannot be equal");
    	}else if(zero.equals("")){
    		fail("An object of different type cannot be equal");
    	}
    }
    
    /**
     * test the reverse of a segment is not equal
     */
    public void testNotEqualsReversed(){
    	assertTrue("A reversed object should not be equal",
    			!eq1.equals(eq1.reverse()));
    }
    
    ////////////////////////////////
    // TEST GeoSegment.HashCode() //
    ////////////////////////////////
    
    /**
     * Test positive case: same object
     **/
    public void testHashCode1() {
        assertEquals("A GeoSegment's hashCode must remain constant",
                     eq1.hashCode(), eq1.hashCode());
        assertEquals("Two equal GeoSegments must have the same hashCodes",
        			eq1.equals(eq1), eq1.hashCode() == eq1.hashCode());
    }

    /**
     * Test positive case: equal objects
     **/
    public void testHashCode2() {
        assertEquals("GeoSegment with same data must have the same hashCode",
                     eq1.hashCode(), eq2.hashCode());
        assertEquals("Two equal GeoSegments must have the same hashCodes",
        			eq1.equals(eq2), eq1.hashCode() == eq2.hashCode());
    }
    
    /**
     * test negative case: two different points and two different names
     */   
    public void testHashCode3() {
    	if(eq1.hashCode() == diffPoint.hashCode() && eq1.equals(diffPoint)){
    		fail("Segments of equal hash numbers but unequal points"+
    				" must not be equal");
    	}
    	if(eq1.hashCode() == diffName.hashCode() && eq1.equals(diffName)){
    		fail("Segments of equal hash numbers but unequal names"+
			" must not be equal");
    	}
    }
    

}
