package ps2.test;

import ps2.*;
import static ps2.test.TestValues.TOLERANCE;
import java.util.*;

import junit.framework.*;


/**
 * Unit tests for the GeoFeature class
 **/
public class GeoFeatureTest extends TestCase {
	
	
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
	// intended to be added to gfEq1
	private final GeoSegment gsAdd1;
	// intended to be added to gfEq1 after gsAdd1
	private final GeoSegment gsAdd2;
	// intended to be added to gfEq1, as well as fail to to add 
	// to gfEq1 after add1
	private final GeoSegment gsAdd3;
	// zero-distance segment
	private final GeoSegment gsZero;
	
	
	// equal to gfEq2
	private final GeoFeature gfEq1;
	// equal to gfEq1
	private final GeoFeature gfEq2;
	// different than gfEq1 in point
	private final GeoFeature gfDiffPoint;
	// different than gfEq1 in name
	private final GeoFeature gfDiffName;
	// zero-distance segment
	private final GeoFeature gfZero;		
	
	
	
	// segments making up a route from point E to point N
	private final GeoSegment gsEtoFvader;
	private final GeoSegment gsFtoGvader;
	private final GeoSegment gsGtoHvader;
	private final GeoSegment gsHtoHvader;
	private final GeoSegment gsHtoIvader;
	private final GeoSegment gsItoJvader;
	private final GeoSegment gsJtoJvader;
	private final GeoSegment gsJtoKvader;
	private final GeoSegment gsKtoLvader;
	private final GeoSegment gsLtoMvader;
	private final GeoSegment gsMtoNvader;
	// array of all segments
	private final List<GeoSegment> gsList;
	
	
    public GeoFeatureTest(String name) {
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
        

        
        // initialize GeoFeatures
        gfEq1 = new GeoFeature(gsEq1);
        gfEq2 = new GeoFeature(gsEq2);
        gfDiffPoint = new GeoFeature(gsDiffPoint);
        gfDiffName = new GeoFeature(gsDiffName);
        gfZero = new GeoFeature(gsZero);
        
        
        
        // initialize point A to point M segments
        gsEtoFvader = new GeoSegment("vader", new GeoPoint( -700000, -700000), 
        		new GeoPoint( 100000, -500000));
        gsFtoGvader = new GeoSegment("vader",new GeoPoint( 100000, -500000), 
        		new GeoPoint( 500000, 100000));
        gsGtoHvader = new GeoSegment("vader",new GeoPoint( 500000, 100000), 
        		new GeoPoint( 200000, 900000));
        gsHtoHvader = new GeoSegment("vader", new GeoPoint( 200000, 900000), 
        		new GeoPoint( 200000, 900000));
        gsHtoIvader = new GeoSegment("vader",new GeoPoint( 200000, 900000), 
        		new GeoPoint( -400000, 700000));
        gsItoJvader = new GeoSegment("vader",new GeoPoint( -400000, 700000), offset
        		(SEATTLE, -800000, 400000));
        gsJtoJvader = new GeoSegment("vader",offset(SEATTLE, -800000, 400000), offset
        		(SEATTLE, -800000, 400000));
        gsJtoKvader = new GeoSegment("vader",offset(SEATTLE, -800000, 400000), offset
        		(SEATTLE, 200000, -200000));
        gsKtoLvader = new GeoSegment("vader", offset(SEATTLE, 200000, -200000), 
        		new GeoPoint(GeoPoint.MIN_LATITUDE, GeoPoint.MIN_LONGITUDE));
        gsLtoMvader = new GeoSegment("vader", new GeoPoint(GeoPoint.MIN_LATITUDE, 
        		GeoPoint.MIN_LONGITUDE), new GeoPoint(GeoPoint.MAX_LATITUDE,
        		GeoPoint.MAX_LONGITUDE));
        gsMtoNvader = new GeoSegment("vader", new GeoPoint(GeoPoint.MAX_LATITUDE,
        		GeoPoint.MAX_LONGITUDE), new GeoPoint(0, 0));
       
       
        
        gsList = new LinkedList<GeoSegment>();
        gsList.add(gsEtoFvader);
        gsList.add(gsFtoGvader);
        gsList.add(gsGtoHvader);
        gsList.add(gsHtoHvader);
        gsList.add(gsHtoHvader);
        gsList.add(gsHtoIvader);
        gsList.add(gsItoJvader);
        gsList.add(gsJtoJvader);
        gsList.add(gsJtoKvader);
        gsList.add(gsKtoLvader);
        gsList.add(gsLtoMvader);
        gsList.add(gsMtoNvader);
        
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
    
    //////////////////////////////////////////////////////
    // TEST LEGAL/ILLEGAL VALUES IN GeoFeature CREATION //
    //////////////////////////////////////////////////////
    
    /**
     * GeoFeature constructor @requires gs != null
     */
    public void testLegalValues(){
    	
    	GeoFeature gf = null;
        try {
            gf = new GeoFeature(gsEq1);
        } catch (Exception ex) {
            // Failed
            fail("Didn't allow legal (GeoSegment) construction from "+gsEq1);
            return;
        }
    	
    	if(!gf.getName().equals(gsEq1.getName())){
    		fail("feature name should have been set to "+gsEq1.getName()+
    				", but was set to "+gf.getName()+" instead");
    	}else if(!gf.getStart().equals(gsEq1.getP1())){
    		fail("feature start should have been set to "+gsEq1.getP1()+
    				", but was set to "+gf.getStart()+" instead");
    	}else if(!gf.getEnd().equals(gsEq1.getP2())){
    		fail("feature start should have been set to "+gsEq1.getP1()+
    				", but was set to "+gf.getEnd()+" instead");
    	}
    }
    
    
    ///////////////////////////////////////////
    // TEST GeoFeature.getStart/EndHeading() //
    ///////////////////////////////////////////
    
    /**
     * test start heading is equal to the first point input, end heading
     * equal to the same 
     **/
    public void testHeadingSingle(){
    	testStartHeading(gsEq1, gfEq1);
    	testEndHeading(gsEq1, gfEq1);
    	
    	testStartHeading(gsDiffPoint, gfDiffPoint);
    	testEndHeading(gsDiffPoint, gfDiffPoint);
    }
    
    // multiple-segment features tested in addSegment section
    
    
    /**
     * helper to test the start heading of a GeoFeature
     */
    void testStartHeading(GeoSegment gs, GeoFeature gf){
    	double hea = gs.getP1().headingTo(gs.getP2());
    	// assert only if not a zero-length segment
    	if( gs.getLength() != gsZero.getLength()){
	    	assertEquals("Start heading for "+gf.getName()+" GeoFeature " +
	    			"should be "+hea+", instead it is "+gf.getStartHeading(),
	    			hea, gf.getStartHeading(), TOLERANCE);
    	}
    }
    
    /**
     * helper to test the end heading of a GeoFeature
     */
    void testEndHeading(GeoSegment gs, GeoFeature gf){
    	double hea = gs.getP1().headingTo(gs.getP2());
    	// assert only if not a zero-length segment
    	if( gs.getLength() != gsZero.getLength()){
	    	assertEquals("End heading for "+gf.getName()+" GeoFeature " +
	    			"should be "+hea+", instead it is "+gf.getEndHeading(),
	    			hea, gf.getEndHeading(), TOLERANCE);
    	}
    }
    
    
    /////////////////////////////////
    // TEST GeoFeature.getLength() //
    /////////////////////////////////
    
    /**
     * test single segment
     **/
    public void testLengthSingle(){
    	double len = gsEq1.getP1().distanceTo(gsEq1.getP2());
    	testLength(gfEq1, len);
    }
    
    // multiple-segment features tested in addSegment section
    
    
    /**
     * test zero-feature equals 0
     **/
    public void testLengthZeroFeature(){
    	if(gfZero.getLength() != 0){
    		fail("feature of a zero-length segment must have a length of 0");
    	}
    }
    
    
    /**
     * helper to test the length of a GeoFeature
     */
    void testLength(GeoFeature gf, double len){
    	assertEquals("Length for "+gf.getName()+" GeoFeature " +
    			"should be "+len+", instead it is "+gf.getLength(),
    			len, gf.getLength(), TOLERANCE);
    }
    
    
    //////////////////////////////////
    // TEST GeoFeature.addSegment() //
    //////////////////////////////////
    
    /**
     * null case, unequal names, unequal start points covered by
     * "@requires gs != null && gs.p1 = this.end && gs.name = this.name"
     */
    
    /**
     * test whether addSegment() handles valid input 
     */
    public void testAddValidSegments(){
    	int i = 0;
    	GeoFeature gf = new GeoFeature(gsList.get(i));
    	while(i < gsList.size() - 1){
    		i++;
    		try{
    			gf = gf.addSegment(gsList.get(i));
    		}catch(Exception ex){
                fail("Didn't allow legal GeoSegments to be added to a feature"+
                		" with GeoFeature.addSegment()");
    		}
    	}
    	
    	// determine that invalid segments are not added
    	GeoFeature gf2 = gf;
    	i = 0;
    	while(i < gsList.size()){
    		try{
    			gf2 = gf2.addSegment(gsList.get(i));
    		}catch(Exception ex){}
    			
    		assertTrue("Adding an invalid segment should not alter the feature",
    				gf2.equals(gf));
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
    	GeoFeature gf = new GeoFeature(gsList.get(i));
    	double len = 0;

    	
    	while(i < gsList.size() - 1){
    		len += gsList.get(i).getLength();
    		
    		testLength(gf, len); 	
        	testStartHeading(gsList.get(0), gf);
        	testEndHeading(gsList.get(i), gf);
        	
        	i++;
        	if(i < gsList.size() - 1){
        		gf = gf.addSegment(gsList.get(i));
        	}        	
    	}
    	
    }
    
    /**
     * test that adding to a zero-length feature changes both
     * start and end headings
     */
    /*
    public void testAddToZeroFeature(){
    	// create a zero-length feature gf
    	GeoFeature gf = new GeoFeature(new GeoSegment(gsAdd1.getName(),
    			gsAdd1.getP1(), gsAdd1.getP1()));
    	// add a real segment
    	gf = gf.addSegment(gsAdd1);
    	double start = gsAdd1.getHeading();
    	double end = gsAdd1.getHeading();
    	
    	
    	assertEquals("Adding to a zero-length feature should change the "+
    			"start heading to a valid value. "+start+
    			" expected, instead it was "+gf.getStartHeading(), 
    			start, gf.getStartHeading(), TOLERANCE);
    	assertEquals("Adding to a zero-length feature should change the "+
    			"end heading to a valid value. "+end+
    			" expected, instead it was "+gf.getEndHeading(), 
    			end, gf.getEndHeading(), TOLERANCE);
    	
    	
    	// add another real segment
    	gf = gf.addSegment(gsAdd2);
    	end = gsAdd2.getHeading();
    	
    	assertEquals("Adding twice to a zero-length feature should keep the "+
    			"start heading to a valid value. "+start+
    			" expected, instead it was "+gf.getStartHeading(), 
    			start, gf.getStartHeading(), TOLERANCE);
    	assertEquals("Adding twice to a zero-length feature should change the "+
    			"end heading to a valid value. "+end+
    			" expected, instead it was "+gf.getEndHeading(), 
    			end, gf.getEndHeading(), TOLERANCE);
    	
    }
    */
    
    /**
     * test that adding a zero-length feature does not change
     * start and end headings
     */
    /*
    public void testAddAZeroSegment(){
    	// create a real-length feature of 2 segments
    	GeoFeature gf = new GeoFeature(gsEq1);
    	gf.addSegment(gsAdd1);
    	double start = gsEq1.getHeading();
    	double end = gsAdd1.getHeading();
    	
    	// add a zero-length segment
    	gf = gf.addSegment(new GeoSegment(gsAdd1.getName(),
    			gsAdd1.getP2(), gsAdd1.getP2()));
    	
    	assertEquals("Adding a zero-length segment to a real-length " +
    			"feature should not change the start heading. "+
    			start+" expected, instead it was "+gf.getStartHeading(),
    			start, gf.getStartHeading(), TOLERANCE);
    	assertEquals("Adding a zero-length segment to a real-length " +
    			"feature should not change the end heading. "+
    			end+" expected, instead it was "+gf.getEndHeading(),
    			end, gf.getEndHeading(), TOLERANCE);
    	
    	
    	// add another real-length segment
    	gf = gf.addSegment(gsAdd2);
    	end = gsAdd2.getHeading();
    	
    	assertEquals("Adding a zero-length segment, then adding a " +
    			"real-length segment should not change the start heading. "+
    			start+" expected, instead it was "+gf.getStartHeading(),
    			start, gf.getStartHeading(), TOLERANCE);
    	assertEquals("Adding a zero-length segment, then adding a " +
    			"real-length segment should change the end heading. "+
    			end+" expected, instead it was "+gf.getEndHeading(), 
    			end, gf.getEndHeading(), TOLERANCE);
    }
    */
    
    //////////////////////////////////////
    // TEST GeoFeature.getGeoSegments() //
    //////////////////////////////////////
    
    /**
     * test getGeoSegments 
     */
    public void testGetSegments(){
    	int i = 0;
    	GeoFeature gf = new GeoFeature(gsList.get(i));
    	while(i < gsList.size() - 1){
    		i++;
    		gf = gf.addSegment(gsList.get(i));
    	}
    	
    	List<GeoSegment> a = gf.getGeoSegments();
        // this.start        = a.get(0).p1
    	assertTrue("The start point for the feature should be the first" +
    			" point of the first element of the returned list", 
    			gf.getStart().equals(gsList.get(0).getP1()));
    	// this.startHeading = a.get(0).heading
    	testStartHeading(gsList.get(0), gf);
    	// this.end = a.get(a.size - 1).p2
    	assertTrue("The end point should be the second point of the" +
    			" last element of the list", gf.getEnd().equals
    			(gsList.get(gsList.size() - 1).getP2()));
    	// this.endHeading = a.get(a.size - 1).heading
    	testEndHeading(gsList.get(gsList.size() - 1), gf);
    	// this.length =  sum (0 &le; i &lt; a.size) . a.get(i).length
    	double sum = 0;
    	for(GeoSegment gs : a){
    		sum += gs.getLength();
    	}
    	assertEquals("The total length of the segments contained in the list" +
    			" should equal the length of the GeoFeature", gf.getLength(),
    			sum, TOLERANCE);
    	
    	
   		i = 0;
    	while(i < a.size()){
    		assertEquals("The segments contained should be organized in the order" +
    				" they were added", gsList.get(i), a.get(i));
    		i++;
    	}
    	
    }
    
    
    
    //////////////////////////////
    // TEST GeoFeature.equals() //
    //////////////////////////////
    
    /**
     * test same features are equal, and unequal names or segments equate 
     * to unequal features
     */
    public void testEqualsValid(){
    	assertTrue("GeoFeature "+gfEq1+" should be equal to GeoFeature "+
    			gfEq2, gfEq1.equals(gfEq2));
    	
    	assertTrue("GeoFeature "+gfEq1+" should not be equal to GeoFeature "+
    			gfDiffName+" due to unequal names", !gfEq1.equals(gfDiffName));

    	assertTrue("GeoFeature "+gfEq1+" should not be equal to GeoFeature "+
    			gfDiffPoint+"due to unequal points", !gfEq1.equals(gfDiffPoint));
    }
    
    /**
     * test zero-feature is equal to another zero-feature
     */
    public void testEqualsZeroFeature(){
    	GeoFeature gf = new GeoFeature(new GeoSegment(gsZero.getName(), 
    			gsZero.getP1(), gsZero.getP2())); 
    	
    	assertTrue("GeoFeature "+gfZero+" should be equal to GeoFeature "+
    			gf+", even if both are zero-length features", gfZero.equals(gf));
    	
    }
    
    /**
     * test equals rejects invalid types
     */
    public void testNotEqualInvalidTypes(){
        if (gfEq1.equals("")){
            fail("Equality with objects of different type is incorrect.");
        }else if(gfZero.equals("")){
    		fail("An object of different type cannot be equal");
    	}
    }
    
    /**
     * test that unequal segment order equates to unequal features
     */
    public void testUnequalSegmentOrder(){
    	GeoFeature gf1 = new GeoFeature(gsEq1);
    	GeoFeature gf2 = new GeoFeature(gsEq1);

    	
    	//test segment order
    	gf1 = gf1.addSegment(gsAdd3);
    	gf1 = gf1.addSegment(gsAdd3.reverse());
    	gf1 = gf1.addSegment(gsAdd1);
    	gf1 = gf1.addSegment(gsAdd1.reverse());
    	gf1 = gf1.addSegment(gsAdd1);
    	
    	gf2 = gf2.addSegment(gsAdd1);
    	gf2 = gf2.addSegment(gsAdd1.reverse());
    	gf2 = gf2.addSegment(gsAdd3);
    	gf2 = gf2.addSegment(gsAdd3.reverse());
    	gf2 = gf2.addSegment(gsAdd1);
    	
    	assertTrue("Features of equal properties, but different segment order" +
    			"should not be equal", !gf1.equals(gf2));
    	
    	
    	GeoSegment z = new GeoSegment(gsAdd1.getName(), gsAdd1.getP1(),
    			gsAdd1.getP1());
    	// again with zero-length segments the only difference
    	gf1 = new GeoFeature(gsEq1);
    	gf2 = new GeoFeature(gsEq1);
    	
    	gf1 = gf1.addSegment(z);
    	gf1 = gf1.addSegment(gsAdd1);
    	gf1 = gf1.addSegment(gsAdd1.reverse());
    	gf1 = gf1.addSegment(gsAdd1);
    	
    	gf2 = gf2.addSegment(gsAdd1);
    	gf2 = gf2.addSegment(gsAdd1.reverse());
    	gf2 = gf2.addSegment(z);
    	gf2 = gf2.addSegment(gsAdd1);
    	
    	assertTrue("Features of equal properties, but different segment order" +
    			"should not be equal", !gf1.equals(gf2));
    }
    
    
    ////////////////////////////////
    // TEST GeoFeature.hashcode() //
    ////////////////////////////////
    
    /**
     * Test positive case: same object
     **/
    public void testHashCode1() {
        assertEquals("A GeoFeature's hashCode must remain constant",
                     gfEq1.hashCode(), gfEq1.hashCode());
        assertEquals("Two equal GeoFeatures must have the same hashCodes",
        			gfEq1.equals(gfEq1), gfEq1.hashCode() == gfEq1.hashCode());
    }

    /**
     * Test positive case: equal objects
     **/
    public void testHashCode2() {
        assertEquals("GeoFeatures with same data must have the same hashCode",
                     gfEq1.hashCode(), gfEq2.hashCode());
        assertEquals("Two equal GeoFeatures must have the same hashCodes",
        			gfEq1.equals(gfEq2), gfEq1.hashCode() == gfEq2.hashCode());
    }
    
    /**
     * test negative case: two different points and two different names
     */   
    public void testHashCode3() {
    	if(gfEq1.hashCode() == gfDiffPoint.hashCode() && gfEq1.equals(gfDiffPoint)){
    		fail("Features of equal hash numbers but unequal points"+
    				" must not be equal");
    	}
    	if(gfEq1.hashCode() == gfDiffName.hashCode() && gfEq1.equals(gfDiffName)){
    		fail("Features of equal hash numbers but unequal names"+
			" must not be equal");
    	}
    }
    
}
