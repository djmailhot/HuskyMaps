package ps4.test;

import ps2.GeoPoint;
import ps4.*;
import junit.framework.*;
import static ps2.test.TestValues.TOLERANCE;

import java.util.*;

public class StreetSegmentTest extends TestCase {

	/* Testing Analysis
	 * 
	 *cases
	 *   v-- number of StreetSegment objects as fields
	 *a) 0x  validation of constructor rep invariant
	 *b) 2x  non-zero-length segment 	VS zero-length segment
	 *c) 2x  non-zero numbersets 		VS zero numbersets
	 *d) 2x  left&right zipcodes same 	VS not same
	 *e) 2x  increasingAddresses 		VS decreasingAddresses
	 *f) 1x  general stress-testing
	 *   
	 *   
	 *tests
	 *a) contains operation
	 *b) reverse
	 *c) double reverse
	 *d) fraction distance
	 *e) (implementation --> hashcode)
	 *
	 */
	
	
	private final StreetNumberSet numSetNorm1;
	private final StreetNumberSet numSetNorm2; 
	private final StreetNumberSet numSetZero; 
	private final StreetNumberSet numSetZip1;
	private final StreetNumberSet numSetZip2;
	private final StreetNumberSet numSetZip3;
	private final StreetNumberSet numSetStress1;
	private final StreetNumberSet numSetStress2;
	
	
	/** arrays of all numbers that should be present*/
	private final int[] arrNorm1 = {15,7,9,11,13,18,5,42,44,57};
	private final int[] arrNorm2 = {2,4,6,8,10,12,16,20,30,32,34,35,36,37};
	private final int[] arrZero = {};
	private final int[] arrZip1 = {4,5,6,8,9,10,16,17,24,26,28,30,32,34,35,36,563};
	private final int[] arrZip2 = {2,7,11,13,15,18,20,21,22,23,25,27,29,562};
	// Zip3 is a combination of Zip1 and Zip2
	private final int[] arrZip3 = {2,4,6,7,8,10,11,13,17,21,22,23,24,26,30,32,34,563}; 
	// Inc/Dec addresses use arrZips
	
	
	

	// constant for size of ultimate stress test
	// used for the size of a random number set of singles between 0 and 2000000.
	private final int STRESS_FACTOR = 10000;
	// storing data for ultimate stress testing
	private final int[] arrStress1 = new int[STRESS_FACTOR];
	private final int[] arrStress2 = new int[STRESS_FACTOR];
	
	
	
	/** StreetSegment objects, systematically covering cases */
	private final StreetSegment stSNormLeng;
	private final StreetSegment stSZeroLeng;
	private final StreetSegment stSNormSet;
	private final StreetSegment stSZeroSet;
	private final StreetSegment stSDiffZip;
	private final StreetSegment stSSameZip;
	private final StreetSegment stSIncAddres;
	private final StreetSegment stSDecAddres;
	private final StreetSegment stSStress;
	
	
	

	public StreetSegmentTest(String name){
		super(name);
		// initialize ranges
    	
    	// initialize StreetNumberSets
    	numSetNorm1 = new StreetNumberSet("15,7-13,18,5,42-44,57");
    	numSetNorm2 = new StreetNumberSet("2-8,10-12,20,30-36,35-37,16");
    	numSetZero = new StreetNumberSet("");
    	numSetZip1 = new StreetNumberSet("4-10,16,17,24-26,563,5,9,28-36,35");
    	numSetZip2 = new StreetNumberSet("562,2,7,11-15,21-27,18-22,29");
    	numSetZip3 = new StreetNumberSet("2-8,7,10,563,11-13,21-23,17,22-26,30-34");

    	
        // initialize StreetSegments
    	stSNormLeng = new StreetSegment(new GeoPoint( -700000, -700000), 
        		new GeoPoint( 100000, -500000),"han",
        		numSetNorm1, numSetNorm2, "98177", "98177", 
        		StreetClassification.parse("PRIM_HWY"),true);
    	stSZeroLeng = new StreetSegment(new GeoPoint( -700000, -700000), 
    			new GeoPoint( -700000, -700000),"han",
        		numSetNorm1, numSetNorm2, "98177", "98177", 
        		StreetClassification.parse("PRIM_HWY"),true);
    	stSNormSet = new StreetSegment(new GeoPoint( 500000, 100000), 
        		new GeoPoint( 200000, 900000),"vader",
        		numSetNorm1, numSetNorm2, "98177", "98177", 
        		StreetClassification.parse("PRIM_HWY"),true);
    	stSZeroSet = new StreetSegment(new GeoPoint( -400000, 700000), 
    			new GeoPoint( -700000, -700000),"lando",
        		numSetZero, numSetZero, "", "", 
        		StreetClassification.parse("PRIM_HWY"),true);
    	stSDiffZip = new StreetSegment(new GeoPoint( -700000, -700000), 
        		new GeoPoint( 100000, -500000),"vader",
        		numSetZip1, numSetZip3, "43137", "73672", 
        		StreetClassification.parse("PRIM_HWY"),true);
    	stSSameZip = new StreetSegment(new GeoPoint( 200000, 900000), 
        		new GeoPoint( 200000, 900000),"han",
        		numSetZip1, numSetZip2, "98177", "98177", 
        		StreetClassification.parse("PRIM_HWY"),true);
    	stSIncAddres = new StreetSegment(new GeoPoint( 500000, 100000), 
        		new GeoPoint( 200000, 900000),"vader",
        		numSetZip3, numSetZip1, "95327", "85432",
        		StreetClassification.parse("PRIM_HWY"),true);
    	stSDecAddres = new StreetSegment(new GeoPoint( 500000, 100000), 
        		new GeoPoint( 200000, 900000),"vader",
        		numSetZip3, numSetZip2, "67432", "54329",
        		StreetClassification.parse("PRIM_HWY"),false);
    	
    	
    	// ultimate stress test case for contained street addresses
    	numSetStress1 = new StreetNumberSet(generate(arrStress1));
    	numSetStress2 = new StreetNumberSet(generate(arrStress2));
    	stSStress = new StreetSegment(new GeoPoint(GeoPoint.MIN_LATITUDE, 
        		GeoPoint.MIN_LONGITUDE),new GeoPoint(GeoPoint.MAX_LATITUDE,
                GeoPoint.MAX_LONGITUDE),"STRESS",
        		numSetStress1, numSetStress2,
        		"03473", "23480", StreetClassification.parse("UNKNOWN"),false);
    	
	}
	
	
	/**
	 * helper to populate the specified array with random numbers with domain 
	 * covering the maximum limits of primitive type int (0 to 1000000).
	 * @return string representation of selected numbers to be used in 
	 * StreetNumberSet constructor. 
	 */
	private String generate(int[] arr){
		Random dice = new Random();
		Set<Integer> s = new TreeSet<Integer>();  // use a set for no duplicates
		while(s.size() < STRESS_FACTOR){
			s.add(dice.nextInt(2000000)); // randomize the input
			
		}
		
		StringBuilder deathBed = new StringBuilder();
		int index = 0;
		Iterator<Integer> iter = s.iterator();
		while(iter.hasNext()){
			int next = iter.next();
			
			arr[index] = next;
			deathBed.append(next+",");        // record in string form
			index++;
		}
		// solve the fence post issue
		deathBed.deleteCharAt(deathBed.length()-1);
		return deathBed.toString();
	}
	
	
	
	
    /**
     * test valid/invalid constructor arguments
     */
	/*PRIM_HWY
	SEC_HWY
	LOCAL_ROAD
	UNKNOWN*/
    

	///////// StreetSegment.fractionDist() ////////
    
    /**
     * test fractionDist with a manageable street segment
     */
    public void testFractionDistBasic(){
    	StreetNumberSet setRight = new StreetNumberSet("33,2-4");
    	StreetNumberSet setLeft = new StreetNumberSet("14,2,4,13");
    	
    	StreetSegment stS = new StreetSegment(new GeoPoint( -700, -700), 
        		new GeoPoint( 100, -500),"han",
        		setLeft, setRight, "47942", "98177", 
        		StreetClassification.parse("PRIM_HWY"),true);
    	
    	
    	assertEquals("basic fraction distance failure",.75, stS.fractionDist(33),TOLERANCE);
    	assertEquals("basic fraction distance failure",.25, stS.fractionDist(2),TOLERANCE);
    	assertEquals("basic fraction distance failure",.5, stS.fractionDist(4),TOLERANCE);
    	assertEquals("basic fraction distance failure",.6, stS.fractionDist(13),TOLERANCE);
    	assertEquals("basic fraction distance failure",.8, stS.fractionDist(14),TOLERANCE);
    }
    
    
    /**
     * test fractionDist method, focusing on dual-address right-side-first specification 
     */
    public void testFractionDistDiffZip(){
    	//for increasing addresses
    	getFractionDist(stSIncAddres, numSetZip1, arrZip3, arrZip1, true, "increasing");
    	
    	//for decreasing addresses
    	getFractionDist(stSDecAddres, numSetZip2, arrZip3, arrZip2, false, "decreasing");
    }
    
    /**
     * test fractionDist method under stress
     */
    public void testFractionDistStress(){
    	getFractionDist(stSStress, numSetStress2, arrStress1, arrStress2, false, "decreasing");
    }
    
    
	/**
	 * helper to analyze fractionDist functionality
	 */
	private void getFractionDist(StreetSegment stS, StreetNumberSet rightSet,
			int[] leftAddr, int[] rightAddr, boolean increasing, String move){
		
		int index = 0;
		while(index < rightAddr.length || index < leftAddr.length){
			
			if(index < leftAddr.length){		// if in left street
				double leftDis = stS.fractionDist(leftAddr[index]);
				double truthDis = getFraction(leftAddr, leftAddr[index], increasing);
				
				if(rightSet.contains(leftAddr[index])){						
					// if also in right street, right value is correct
					
					truthDis = getFraction(rightAddr, leftAddr[index], increasing);
					
					assertEquals("Fractional distance for address "+leftAddr[index]+
							" found on both sides (different zipcodes) of "+move+
							" addresses should be the right-side address distance",
							truthDis, leftDis, TOLERANCE*STRESS_FACTOR);
							//leftDis is the StreetSegment fractionDist return, which 
							//should return a distance of the right-side address.
				}else{
					// else left value is correct
					
					assertEquals("Fractional distance for "+leftAddr[index]+" on left side" +
							" for "+move+" addresses should be "+truthDis+", instead was "+
							leftDis,truthDis, leftDis, TOLERANCE*STRESS_FACTOR);
				}
			}
			
			if(index < rightAddr.length){
				// if in right street
				
				double rightDis = stS.fractionDist(rightAddr[index]);
				double truthDis = getFraction(rightAddr, rightAddr[index], increasing);
				
				assertEquals("Fractional distance for "+rightAddr[index]+" on right side" +
						" for "+move+" addresses should be "+truthDis+", instead was "+
						rightDis,truthDis, rightDis,TOLERANCE*STRESS_FACTOR);
			}
			
			index++;
			
		}
	}
	
	
	/**
	 * helper for getting fraction of distance out of storage arrays
	 */
	private double getFraction(int[] arr, int address, boolean increasing){
    	int count = 0;
    	int i;
    	for(int j = 0; j < arr.length; j++){
    		i = arr[j];
    		if(i < address){
    			count++;
    		}
    	}
    	
    	double value = ((double) count +1 ) / ((double) arr.length + 1);
    	if(!increasing){
    		value = 1.0 - value;
    	}
    	return value;
	}
	
	
}
