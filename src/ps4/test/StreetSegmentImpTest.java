package ps4.test;

import ps2.GeoPoint;
import ps4.*;
import junit.framework.*;
import static ps2.test.TestValues.TOLERANCE;

import java.util.*;

public class StreetSegmentImpTest extends TestCase {

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
	private final String codeNorm1 = "98177";
	private final int[] arrNorm2 = {2,4,6,8,10,12,16,20,30,32,34,35,36,37};
	private final String codeNorm2 = "98177";
	private final int[] arrZero = {};
	private final String codeZero = "";
	private final int[] arrZip1 = {4,5,6,8,9,10,16,17,24,26,28,30,32,34,35,36,563};
	private final String codeZip1 = "52389";
	private final int[] arrZip2 = {2,7,11,13,15,18,20,21,22,23,25,27,29,562};
	private final String codeZip2 = "52389";
	// Zip3 is a combination of Zip1 and Zip2
	private final int[] arrZip3 = {2,4,6,7,8,10,11,13,17,21,22,23,24,26,30,32,34,563};
	private final String codeZip3 = "47837";
	// Inc/Dec addresses use arrZips
	
	
	

	// constant for size of ultimate stress test
	// used for the size of a random number set of singles between 0 and 1000000.
	private final int STRESS_FACTOR = 20000;
	// storing data for ultimate stress testing
	private final int[] arrStress1 = new int[STRESS_FACTOR];
	private final String codeStress1 = "44444";
	private final int[] arrStress2 = new int[STRESS_FACTOR];
	private final String codeStress2 = "88888";
	
	
	
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
	
	
	

	public StreetSegmentImpTest(String name){
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
        		numSetNorm1, numSetNorm2, codeNorm1, codeNorm2, 
        		StreetClassification.parse("PRIM_HWY"),true);
    	stSZeroLeng = new StreetSegment(new GeoPoint( -700000, -700000), 
    			new GeoPoint( -700000, -700000),"han",
        		numSetNorm1, numSetNorm2, codeNorm1, codeNorm2, 
        		StreetClassification.parse("PRIM_HWY"),true);
    	stSNormSet = new StreetSegment(new GeoPoint( 500000, 100000), 
        		new GeoPoint( 200000, 900000),"vader",
        		numSetNorm1, numSetNorm2, codeNorm1, codeNorm2, 
        		StreetClassification.parse("PRIM_HWY"),true);
    	stSZeroSet = new StreetSegment(new GeoPoint( -400000, 700000), 
    			new GeoPoint( -700000, -700000),"lando",
        		numSetZero, numSetZero, codeZero, codeZero, 
        		StreetClassification.parse("PRIM_HWY"),true);
    	stSDiffZip = new StreetSegment(new GeoPoint( -700000, -700000), 
        		new GeoPoint( 100000, -500000),"vader",
        		numSetZip1, numSetZip3, codeZip1, codeZip3, 
        		StreetClassification.parse("PRIM_HWY"),true);
    	stSSameZip = new StreetSegment(new GeoPoint( 200000, 900000), 
        		new GeoPoint( 200000, 900000),"han",
        		numSetZip1, numSetZip2, codeZip1, codeZip2, 
        		StreetClassification.parse("PRIM_HWY"),true);
    	stSIncAddres = new StreetSegment(new GeoPoint( 500000, 100000), 
        		new GeoPoint( 200000, 900000),"vader",
        		numSetZip3, numSetZip1, codeZip3, codeZip1,
        		StreetClassification.parse("PRIM_HWY"),true);
    	stSDecAddres = new StreetSegment(new GeoPoint( 500000, 100000), 
        		new GeoPoint( 200000, 900000),"vader",
        		numSetZip3, numSetZip2, codeZip3, codeZip2,
        		StreetClassification.parse("PRIM_HWY"),false);
    	
    	
    	// ultimate stress test case for contained street addresses
    	numSetStress1 = new StreetNumberSet(generate(arrStress1));
    	numSetStress2 = new StreetNumberSet(generate(arrStress2));
    	stSStress = new StreetSegment(new GeoPoint(GeoPoint.MIN_LATITUDE, 
        		GeoPoint.MIN_LONGITUDE),new GeoPoint(GeoPoint.MAX_LATITUDE,
                GeoPoint.MAX_LONGITUDE),"STRESS",
        		numSetStress1, numSetStress2,
        		codeStress1, codeStress2, StreetClassification.parse("UNKNOWN"),false);
    	
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
	
	///////// StreetSegment.contains() ////////
    
	/**
	 * test contains operation
	 */
    public void testContains(){
    	getContains(stSNormLeng, arrNorm1, codeNorm1, arrNorm2, codeNorm2);
    	getContains(stSZeroLeng, arrNorm1, codeNorm1, arrNorm2, codeNorm2);
    	getContains(stSNormSet, arrNorm1, codeNorm1, arrNorm2, codeNorm2);
    	getContains(stSZeroSet, arrZero, codeZero, arrZero, codeZero);
    	getContains(stSDiffZip, arrZip1, codeZip1, arrZip3, codeZip3);
    	getContains(stSSameZip, arrZip1, codeZip1, arrZip2, codeZip2);
    	getContains(stSIncAddres, arrZip3, codeZip3, arrZip1, codeZip1);
    	getContains(stSDecAddres, arrZip3, codeZip3, arrZip2, codeZip2);
    }
    
    /**
     * test contains uder stress
     */
    public void testContainsStress(){
    	getContains(stSStress, arrStress1, codeStress1, arrStress2, codeStress2);
    }
    	
    	
    	
    /**
     * helper to test whether a segment contains what is should
     */
    private void getContains(StreetSegment stS, int[] a, String aZip, int[] b, String bZip){
    	for(int i = 0; i < a.length; i++){
    		assertTrue("Street should contain "+a[i], 
    				stS.contains(a[i], aZip));
    		assertFalse("Street should not contain "+a[i]+" if the street-side zipcode "+
    				"does not match", stS.contains(a[i], "00000"));
    	}
    	for(int i = 0; i < b.length; i++){
    		assertTrue("Street should contain "+b[i], 
    				stS.contains(b[i], bZip));
    		assertFalse("Street should not contain "+b[i]+" if the street-side zipcode "+
    				"does not match", stS.contains(b[i], "00000"));
    	}
    }
	
	///////// StreetSegment.reverse() ////////
    
	/**
	 * test reverse operation, focusing on switch of right/left NumberSets
	 */
    public void testReverse(){
    	getReverse(stSNormLeng, numSetNorm1, numSetNorm2, arrNorm1, arrNorm2, true);
    	getReverse(stSZeroLeng, numSetNorm1, numSetNorm2, arrNorm1, arrNorm2, true);
    	getReverse(stSNormSet, numSetNorm1, numSetNorm2, arrNorm1, arrNorm2, true);
    	getReverse(stSZeroSet, numSetZero, numSetZero, arrZero, arrZero, true);
    	getReverse(stSDiffZip, numSetZip1, numSetZip3, arrZip1, arrZip3, true);
    	getReverse(stSSameZip, numSetZip1, numSetZip2, arrZip1, arrZip2, true);
    	getReverse(stSIncAddres, numSetZip3, numSetZip1, arrZip3, arrZip1, true);
    	getReverse(stSDecAddres, numSetZip3, numSetZip2, arrZip3, arrZip2, false);
    }
    
    /**
     * test reverse under stress
     */
    public void testReverseStress(){
    	getReverse(stSStress, numSetStress1, numSetStress2, arrStress1, arrStress2, false);
    }
    
	
    /**
     * helper to test reverse
     */
    private void getReverse(StreetSegment stS, StreetNumberSet leftSet, StreetNumberSet
    			rightSet, int[] leftAddr, int[] rightAddr, boolean increasing){
    	
    	//Basic GeoSegment definitions
    	double hea = stS.getHeading();
    	double len = stS.getLength();    	
    	
    	StreetSegment rev = stS.reverse();
    	if(hea - 180 < 0){
    		hea += 180;
    	}else{
    		hea -= 180;
    	}
    	
    	assertEquals("A reversed segment must have the same length", 
    			len, rev.getLength(), TOLERANCE);
    	if( stS.getLength() != stSZeroLeng.getLength()){
    		assertEquals("A reversed segment must have the opposite heading",
    				hea, rev.getHeading(), TOLERANCE);
    		assertTrue("A reversed segment should not be equal to itself",
    				!rev.equals(stS));
    	}
    	assertTrue("A double reversed segment should be equal to itself",
    			rev.reverse().equals(stS));
    	
    	
    	
    	// NumberSet switching
    	for(int i = 0; i < rightAddr.length; i++){
    		
    		int address = rightAddr[i];
    		if(rightSet.contains(address) && leftSet.contains(address)){	
    			//if both sides contain this address

    			//NORMAL CASE
				double calcDis = stS.fractionDist(address);
    			//must return original right value for stS.fractionDist()
    			double truthDis = getFraction(rightAddr, address, increasing);
    			
				assertEquals("Fractional distance for address "+address+" found on " +
						"both sides (different zipcodes) should be the right-side " +
						"address distance",truthDis, calcDis, TOLERANCE);
						//calsDis should return a distance of the original right-side address.
    			
    			
				//REVERSE CASE
				calcDis = rev.fractionDist(address);
    			//must return original 'left' value for rev.fractionDist()
    			truthDis = getFraction(leftAddr, address, !increasing);
    			
				assertEquals("Fractional distance for address "+address+" found on " +
						"both sides (different zipcodes) on a REVERSED segment should " +
						"be the right-side address distance",truthDis,	calcDis, TOLERANCE);
						//clacDis should be a distance of the original left-side address
						//for a REVERSED segment.
    		}
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
