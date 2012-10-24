package ps2.test;

import ps2.*;

import static ps2.test.TestValues.TOLERANCE;
import junit.framework.*;


//>>>>>>>>>>>>>>>>>>>> assertEquals not able to be accessed


//>>>>>>>>>>>>>>>>>>>> for PS2, wanted to test my handling of zero-length segments.

/**
 * ImplementationTest is a simple test suite to test the
 * implementation of each problem set.  You do not need to modify this
 * file for problem set 2.
 **/
public final class ImplementationTests extends TestSuite {

	
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
	
    public ImplementationTests() {
        this("Problem Set 2 ImplementationTests");
    }

    public ImplementationTests(String name) {
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
    }

    /**
     * helper to create a new GeoPoint offset from an original
     * @return a new GeoPoint offset by the specified lat and long
     */
    GeoPoint offset(GeoPoint pt, int lat, int lon){
    	return new GeoPoint(pt.getLatitude() + lat, pt.getLongitude() + lon);
    }
    
    
    
    public static Test suite() {
        return new ImplementationTests();
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
    	
    }*/
    
    
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
    }*/
    
}
