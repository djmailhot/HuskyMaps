package ps2.test;

import java.util.List;
import java.util.LinkedList;

import ps2.*;
import static ps2.test.TestValues.TOLERANCE;

import junit.framework.*;


/**
 * Unit tests for the Route class
 **/
public class RouteTest extends TestCase {
    
    /** Minimum value the latitude field can have in this class. **/
    public static final int MIN_LATITUDE  =  -90 * 1000000;

    /** Maximum value the latitude field can have in this class. **/
    public static final int MAX_LATITUDE  =   90 * 1000000;

    /** Minimum value the longitude field can have in this class. **/
    public static final int MIN_LONGITUDE = -180 * 1000000;

    /** Maximum value the longitude field can have in this class. **/
    public static final int MAX_LONGITUDE =  180 * 1000000;
	
	// location of Seattle
	private final GeoPoint SEATTLE = new GeoPoint(47609722, -122333056);
	
	// equal to gsEq2
	private final GeoSegment gsEq1;
	// equal to gsEq1
	private final GeoSegment gsEq2;
	// different than gsEq1 in point
	private final GeoSegment gsDiffPoint;
	// different than gsEq1 in name
	private final GeoSegment gsDiffName;
	// intended to be added to rEq1
	private final GeoSegment gsAdd1;
	// intended to be added to rEq1 after gsAdd1
	private final GeoSegment gsAdd2;
	// intended to be added to rEq1, as well as fail to to add 
	// to rEq1 after add1
	private final GeoSegment gsAdd3;
	// zero-distance segment
	private final GeoSegment gsZero;
	
	
	// segments making up a route from point E to point N
	private final GeoSegment gsEtoFhan;
	private final GeoSegment gsFtoGhan;
	private final GeoSegment gsGtoHvader;
	private final GeoSegment gsHtoHvader;
	private final GeoSegment gsHtoIhan;
	private final GeoSegment gsItoJlando;
	private final GeoSegment gsJtoJlando;
	private final GeoSegment gsJtoKlando;
	private final GeoSegment gsKtoLvader;
	private final GeoSegment gsLtoMvader;
	private final GeoSegment gsMtoNvader;
	// array of all segments
	private final List<GeoSegment> gsList;
	
	
	// features of the E to N point segments separated into continuous lengths
	private final GeoFeature gfEtoGhan;
	private final GeoFeature gfGtoHvader;
	private final GeoFeature gfHtoIhan;
	private final GeoFeature gfItoKlando;
	private final GeoFeature gfKtoNvader;
	// array of all features
	private final List<GeoFeature> gfList;
	
	
	
	// equal to rEq2
	private final Route rEq1;
	// equal to rEq1
	private final Route rEq2;
	// different than rEq1 in point
	private final Route rDiffPoint;
	// different than rEq1 in name
	private final Route rDiffName;
	// zero-distance segment
	private final Route rZero;		
	
	
	public RouteTest(String name) {
        super(name);
        
        
        // initialize GeoSegments with value significant in relativity,
        // descriptions are above field declarations
        gsEq1 = new GeoSegment("Same St",offset(SEATTLE, 570000, -140000), offset
        		(SEATTLE, -200000, -1140000));
        gsEq2 = new GeoSegment("Same St",offset(SEATTLE, 570000, -140000), offset
        		(SEATTLE, -200000, -1140000));
        gsDiffPoint = new GeoSegment("Same St",offset(SEATTLE, 240000,-30000), 
        		offset(SEATTLE, -460000, -530000));
        gsDiffName = new GeoSegment("Diff Dr",offset(SEATTLE, 570000, -140000), 
        		offset(SEATTLE, -200000, -1140000));
        
        gsAdd1 = new GeoSegment("Same St", offset(SEATTLE, -200000, -1140000), 
        		offset(SEATTLE, 430000, -360000));
        gsAdd2 = new GeoSegment("Same St", offset(SEATTLE, 430000, -360000),
        		 offset(SEATTLE, 1200000, 130000));
        gsAdd3 = new GeoSegment("Same St", offset(SEATTLE, -200000, -1140000),
        		offset(SEATTLE, -60000, 420000));
        gsZero = new GeoSegment("Zero zone",offset(SEATTLE, -200000, -1140000), 
        		offset(SEATTLE, -200000, -1140000));

        
        // initialize Routes
        rEq1 = new Route(gsEq1);
        rEq2 = new Route(gsEq2);
        rDiffPoint = new Route(gsDiffPoint);
        rDiffName = new Route(gsDiffName);
        rZero = new Route(gsZero);
        
        
        
        // initialize point A to point N segments
        gsEtoFhan = new GeoSegment("han", new GeoPoint( -700000, -700000), 
        		new GeoPoint( 100000, -500000));
        gsFtoGhan = new GeoSegment("han",new GeoPoint( 100000, -500000), 
        		new GeoPoint( 500000, 100000));
        gsGtoHvader = new GeoSegment("vader",new GeoPoint( 500000, 100000), 
        		new GeoPoint( 200000, 900000));
        gsHtoHvader = new GeoSegment("vader", new GeoPoint( 200000, 900000), 
        		new GeoPoint( 200000, 900000));
        gsHtoIhan = new GeoSegment("han",new GeoPoint( 200000, 900000), 
        		new GeoPoint( -400000, 700000));
        gsItoJlando = new GeoSegment("lando",new GeoPoint( -400000, 700000), offset
        		(SEATTLE, -800000, 400000));
        gsJtoJlando = new GeoSegment("lando",offset(SEATTLE, -800000, 400000), offset
        		(SEATTLE, -800000, 400000));
        gsJtoKlando = new GeoSegment("lando",offset(SEATTLE, -800000, 400000), offset
        		(SEATTLE, 200000, -200000));
        gsKtoLvader = new GeoSegment("vader", offset(SEATTLE, 200000, -200000), 
        		new GeoPoint(GeoPoint.MIN_LATITUDE, GeoPoint.MIN_LONGITUDE));
        gsLtoMvader = new GeoSegment("vader", new GeoPoint(GeoPoint.MIN_LATITUDE, 
        		GeoPoint.MIN_LONGITUDE), new GeoPoint(GeoPoint.MAX_LATITUDE,
        		GeoPoint.MAX_LONGITUDE));
        gsMtoNvader = new GeoSegment("vader", new GeoPoint(GeoPoint.MAX_LATITUDE,
        		GeoPoint.MAX_LONGITUDE), new GeoPoint(4321432, 3213213));
        
        
        gsList = new LinkedList<GeoSegment>();
        gsList.add(gsEtoFhan);
        gsList.add(gsFtoGhan);
        gsList.add(gsGtoHvader);
        gsList.add(gsHtoHvader);
        gsList.add(gsHtoHvader);
        gsList.add(gsHtoIhan);
        gsList.add(gsItoJlando);
        gsList.add(gsJtoJlando);
        gsList.add(gsJtoKlando);
        gsList.add(gsKtoLvader);
        gsList.add(gsLtoMvader);
        gsList.add(gsMtoNvader);
        
        
        // initialize features of continuous segments
        GeoFeature temp = new GeoFeature(gsEtoFhan);
        gfEtoGhan = temp.addSegment(gsFtoGhan);
        temp = new GeoFeature(gsGtoHvader);
        temp = temp.addSegment(gsHtoHvader);
        gfGtoHvader = temp.addSegment(gsHtoHvader);
        gfHtoIhan = new GeoFeature(gsHtoIhan);
        temp = new GeoFeature(gsItoJlando);
        temp = temp.addSegment(gsJtoJlando);
        gfItoKlando = temp.addSegment(gsJtoKlando);
        temp = new GeoFeature(gsKtoLvader);
        temp = temp.addSegment(gsLtoMvader);
        gfKtoNvader = temp.addSegment(gsMtoNvader);
        
        gfList = new LinkedList<GeoFeature>();
        gfList.add(gfEtoGhan);
        gfList.add(gfGtoHvader);
        gfList.add(gfHtoIhan);
        gfList.add(gfItoKlando);
        gfList.add(gfKtoNvader);
        
    }
	
    
    /**
     * helper to create a new GeoPoint offset from an original
     * @return a new GeoPoint offset by the specified lat and long
     */
    GeoPoint offset(GeoPoint pt, int lat, int lon){
    	return new GeoPoint(pt.getLatitude() + lat, pt.getLongitude() + lon);
    }


    // JUnit calls setUp() before each test__ method is run
    protected void setUp() {

    }
    
    /////////////////////////////////////////////////
    // TEST LEGAL/ILLEGAL VALUES IN Route CREATION //
    /////////////////////////////////////////////////
    
    
    /**
     * Route constructor @requires gs != null
     */
    public void testLegalValues(){
    	
    	Route r = null;
        try {
            r = new Route(gsEq1);
        } catch (Exception ex) {
            // Failed
            fail("Didn't allow legal (Route) construction from "+gsEq1);
            return;
        }
    	
    	if(!r.getStart().equals(gsEq1.getP1())){
    		fail("Route start should have been set to "+gsEq1.getP1()+
    				", but was set to "+r.getStart()+" instead");
    	}else if(!r.getEnd().equals(gsEq1.getP2())){
    		fail("Route start should have been set to "+gsEq1.getP1()+
    				", but was set to "+r.getEnd()+" instead");
    	}
    }
    
    //////////////////////////////////////
    // TEST Route.getStart/EndHeading() //
    //////////////////////////////////////
    
    /**
     * test start heading is equal to the first point input, end heading
     * equal to the same 
     **/
    public void testHeadingSingle(){
    	testStartHeading(gsEq1, rEq1);
    	testEndHeading(gsEq1, rEq1);
    	
    	testStartHeading(gsDiffPoint, rDiffPoint);
    	testEndHeading(gsDiffPoint, rDiffPoint);
    }
    
    // multiple-segment features tested in addSegment section
    
    
    /**
     * helper to test the start heading of a Route
     */
    void testStartHeading(GeoSegment gs, Route r){
    	double hea = gs.getP1().headingTo(gs.getP2());
    	// assert only if not a zero-length segment
    	if( gs.getLength() != gsZero.getLength()){
	    	assertEquals("Start heading for this Route " +
	    			"should be "+hea+", instead it is "+r.getStartHeading(),
	    			hea, r.getStartHeading(), TOLERANCE);
    	}
    }
    
    /**
     * helper to test the end heading of a Route
     */
    void testEndHeading(GeoSegment gs, Route r){
    	double hea = gs.getP1().headingTo(gs.getP2());
    	// assert only if not a zero-length segment
    	if( gs.getLength() != gsZero.getLength()){
	    	assertEquals("End heading for this Route " +
	    			"should be "+hea+", instead it is "+r.getEndHeading(),
	    			hea, r.getEndHeading(), TOLERANCE);
    	}
    }
    
    ////////////////////////////
    // TEST Route.getLength() //
    ////////////////////////////
    
    /**
     * test single segment
     **/
    public void testLengthSingle(){
    	double len = gsEq1.getP1().distanceTo(gsEq1.getP2());
    	testLength(rEq1, len);
    }
    
    
    // multiple-segment features tested in addSegment section
    
    
    /**
     * test Zero-Route equals 0
     **/
    public void testLengthZeroRoute(){
    	if(rZero.getLength() != 0){
    		fail("Route of a zero-length segment must have a length of 0");
    	}
    }
    
    
    /**
     * helper to test the length of a Route
     */
    void testLength(Route r, double len){
    	assertEquals("Length for this Route " +
    			"should be "+len+", instead it is "+r.getLength(),
    			len, r.getLength(), TOLERANCE);
    }
    
    /////////////////////////////
    // TEST Route.addSegment() //
    /////////////////////////////
    
    /**
     * test add valid segments
     */
    public void testAddValidSegments(){
    	int i = 0;
    	Route r = new Route(gsList.get(i));
    	while(i < gsList.size() - 1){
    		i++;
    		try{
    			r = r.addSegment(gsList.get(i));
    		}catch(Exception ex){
                fail("Didn't allow legal GeoSegments to be added to a Route"+
                		" with Route.addSegment()");
    		}
    	}
    	
    	// determine that invalid segments are not added
    	Route r2 = r;
    	i = 0;
    	while(i < gsList.size()){
    		try{
    			r2 = r2.addSegment(gsList.get(i));
    		}catch(Exception ex){}
    			
    		assertTrue("Adding an invalid segments should not alter the Route",
    				r2.equals(r));
    		i++;
    	}
    }
    
    
    /**
     * test that the feature properties properly change.
     * the start heading should be equal to the first point even 
     * if more segments are added, end heading equal to the last 
     * even if more segments are added.  The length should be the
     * sum of the lengths of all contained segments.
     **/
    public void testAddChangeProperties(){
    	// initialize a 2-segment feature
    	int i = 0;
    	Route r = new Route(gsList.get(i));
    	double len = 0;

    	
    	while(i < gsList.size() - 1){
    		len += gsList.get(i).getLength();
    		
    		testLength(r, len); 	
        	testStartHeading(gsList.get(0), r);
        	testEndHeading(gsList.get(i), r);
        	
        	i++;
        	if(i < gsList.size() - 1){
        		r = r.addSegment(gsList.get(i));
        	}        	
    	}
    
    }
    
    
    
    /////////////////////////////////
    // TEST Route.getGeoFeatures() //
    /////////////////////////////////
    
    
    /**
     * test getGeoFeatures 
     */
    public void testGetFeatures(){
    	int i = 0;
    	Route r = new Route(gsList.get(i));
    	while(i < gsList.size() - 1){
    		i++;
    		r = r.addSegment(gsList.get(i));
    	}
    	
    	List<GeoFeature> a = r.getGeoFeatures();
        // this.start        = a.get(0).p1
    	assertTrue("The start point for the route should be the first" +
    			" point of the first element of the returned list", 
    			r.getStart().equals(gfList.get(0).getStart()));
    	// this.startHeading = a.get(0).heading
    	testStartHeading(gsList.get(0), r); 
    	// this.end = a.get(a.size - 1).p2
    	assertTrue("The end point should be the second point of the" +
    			" last element of the list", r.getEnd().equals
    			(gfList.get(gfList.size() - 1).getEnd()));
    	// this.endHeading = a.get(a.size - 1).heading
    	testEndHeading(gsList.get(gsList.size() - 1), r);
    	// this.length =  sum (0 & le; i &lt; a.size) . a.get(i).length
    	double sum = 0;
    	for(GeoFeature gf : a){
    		sum += gf.getLength();
    	}
    	assertEquals("The total length of the features contained in the list" +
    			" should equal the length of the Route", r.getLength(),
    			sum, TOLERANCE);
    	
    	
    	i = 0;
    	while(i < a.size()){
    		if(!gfList.get(i).equals(a.get(i))){
   				System.out.println(gfList.get(i) +"=========VS========"+(a.get(i)));
    			fail("The segments contained should be organized into features "+
    			"of continuous segments");
    		}
    		i++;
    	}
    }
    
    
    /////////////////////////////////
    // TEST Route.getGeoSegments() //
    /////////////////////////////////
    
    /**
     * test getGeoSegments 
     */
    public void testGetSegments(){
    	int i = 0;
    	Route r = new Route(gsList.get(i));
    	while(i < gsList.size() - 1){
    		i++;
    		r = r.addSegment(gsList.get(i));
    	}
    	
    	List<GeoSegment> a = r.getGeoSegments();
        // this.start        = a.get(0).p1
    	assertTrue("The start point for the route should be the first" +
    			" point of the first element of the returned list", 
    			r.getStart().equals(gsList.get(0).getP1()));
    	// this.startHeading = a.get(0).heading
    	testStartHeading(gsList.get(0), r); 
    	// this.end = a.get(a.size - 1).p2
    	assertTrue("The end point should be the second point of the" +
    			" last element of the list", r.getEnd().equals
    			(gsList.get(gsList.size() - 1).getP2()));
    	// this.endHeading = a.get(a.size - 1).heading
    	testEndHeading(gsList.get(gsList.size() - 1), r);
    	// this.length =  sum (0 & le; i &lt; a.size) . a.get(i).length
    	double sum = 0;
    	for(GeoSegment gs : a){
    		sum += gs.getLength();
    	}
    	assertEquals("The total length of the segments contained in the list" +
    			" should equal the length of the Route", r.getLength(),
    			sum, TOLERANCE);
    	
    	
    	i = 0;
    	while(i < a.size()){
    		assertEquals("The segments contained should be organized in the order" +
    				" they were added", gsList.get(i), a.get(i));
    		i++;
    	}
    }
    
    
    /////////////////////////
    // TEST Route.equals() //
    /////////////////////////
    
    /**
     * test routes of the same order of segments are equal
     */
    public void testEquals(){
    	int i = 0;
    	Route r1 = new Route(gsList.get(i));
    	Route r2 = new Route(gsList.get(i));
    	while(i < gsList.size() - 1){
    		i++;
    		r1 = r1.addSegment(gsList.get(i));
    		r2 = r2.addSegment(gsList.get(i));
    	}
    	
    	assertTrue("routes of the same order of segments must be equal",
    			r1.equals(r2));

    }
    
    /**
     * test Zero-Route is equal to another Zero-Route
     */
    public void testEqualsZeroRoute(){
    	Route r = new Route(new GeoSegment(gsZero.getName(), 
    			gsZero.getP1(), gsZero.getP2())); 
    	
    	assertTrue("Route "+rZero+" should be equal to Route "+
    			r+", even if both are zero-length routes", rZero.equals(r));
    	
    }
    
    /**
     * test equals rejects invalid types
     */
    public void testNotEqualInvalidTypes(){
        if (rEq1.equals("")){
            fail("Equality with objects of different type is incorrect.");
        }else if(rZero.equals("")){
    		fail("An object of different type cannot be equal");
    	}
    }
    
    /**
     * test that unequal segment order equates to unequal features
     */
    public void testUnequalSegmentOrder(){
    	Route r1 = new Route(gsEq1);
    	Route r2 = new Route(gsEq1);

    	
    	//test segment order
    	r1 = r1.addSegment(gsAdd3);
    	r1 = r1.addSegment(gsAdd3.reverse());
    	r1 = r1.addSegment(gsAdd1);
    	r1 = r1.addSegment(gsAdd1.reverse());
    	r1 = r1.addSegment(gsAdd1);
    	
    	r2 = r2.addSegment(gsAdd1);
    	r2 = r2.addSegment(gsAdd1.reverse());
    	r2 = r2.addSegment(gsAdd3);
    	r2 = r2.addSegment(gsAdd3.reverse());
    	r2 = r2.addSegment(gsAdd1);
    	
    	assertTrue("Routes of equal properties, but different segment order" +
    			"should not be equal", !r1.equals(r2));
    	
    	
    	// again with zero-length segments the only difference
    	r1 = new Route(gsEq1);
    	r2 = new Route(gsEq1);
    	GeoSegment z = new GeoSegment(gsAdd1.getName(), gsAdd1.getP1(),
    			gsAdd1.getP1());
    	
    	r1 = r1.addSegment(z);
    	r1 = r1.addSegment(gsAdd1);
    	r1 = r1.addSegment(gsAdd1.reverse());
    	r1 = r1.addSegment(gsAdd1);
    	
    	r2 = r2.addSegment(gsAdd1);
    	r2 = r2.addSegment(gsAdd1.reverse());
    	r2 = r2.addSegment(z);
    	r2 = r2.addSegment(gsAdd1);
    	
    	assertTrue("Routes of equal properties, but different segment order" +
    			"should not be equal", !r1.equals(r2));
    }
    
    
    
    ///////////////////////////
    // TEST Route.hashcode() //
    ///////////////////////////
    
    /**
     * Test positive case: same object
     **/
    public void testHashCode1() {
        assertEquals("A Route's hashCode must remain constant",
                     rEq1.hashCode(), rEq1.hashCode());
        assertEquals("Two equal Routes must have the same hashCodes",
        			rEq1.equals(rEq1), rEq1.hashCode() == rEq1.hashCode());
    }

    /**
     * Test positive case: equal objects
     **/
    public void testHashCode2() {
        assertEquals("Routes with same data must have the same hashCode",
                     rEq1.hashCode(), rEq2.hashCode());
        assertEquals("Two equal Routes must have the same hashCodes",
        			rEq1.equals(rEq2), rEq1.hashCode() == rEq2.hashCode());
    }
    
    /**
     * test negative case: two different points and two different names
     */   
    public void testHashCode3() {
    	if(rEq1.hashCode() == rDiffPoint.hashCode() && rEq1.equals(rDiffPoint)){
    		fail("Features of equal hash numbers but unequal points"+
    				" must not be equal");
    	}
    	if(rEq1.hashCode() == rDiffName.hashCode() && rEq1.equals(rDiffName)){
    		fail("Features of equal hash numbers but unequal names"+
			" must not be equal");
    	}
    }
    
}
