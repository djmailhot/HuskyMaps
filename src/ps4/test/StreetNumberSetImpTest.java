package ps4.test;

import ps4.*;
import junit.framework.*;

public class StreetNumberSetImpTest extends TestCase {

	/** number sets */
	private final StreetNumberSet numSetEq1;
	private final StreetNumberSet numSetEq2;
	private final StreetNumberSet numSetDiff1;
	private final StreetNumberSet numSetDiff2;
	private final StreetNumberSet numSetZero;
	private final StreetNumberSet numSetBig;
	
	/** arrays of all numbers that should be present*/
	private final int[] arrEq1 = {14,16,18,20,22};
	private final int[] arrEq2 = {14,16,18,20,22};
	private final int[] arrDiff1 = {14,15,16,17,18,19,20,22};
	private final int[] arrDiff2 = {14,18,20,22};
	private final int[] arrZero = {};
	private final int[] arrBig = {15,8,10,12,7,9,11,0,13,18,5,42,44,57};
	
	
	public StreetNumberSetImpTest(String name){
		super(name);
		
		// initialize ranges
    	String numEq1 = "14-22";
    	String numEq2 = "14-16,18,20-22";    	
    	String numDiff1 = "14-18,15-19,20-22";
    	String numDiff2 = "14,18,20-22";
    	String numZero = "";
    	// non-ascending order seed string.  No specification about state of
    	// seed string on order of numbers!
    	String numBig = "15,8-12,7-13,0,18,5,42-44,57";   
    	
    	// initialize StreetNumberSets
    	numSetEq1 = new StreetNumberSet(numEq1);
    	numSetEq2 = new StreetNumberSet(numEq2);
    	numSetDiff1 = new StreetNumberSet(numDiff1);
    	numSetDiff2 = new StreetNumberSet(numDiff2);
    	numSetZero = new StreetNumberSet(numZero);
    	numSetBig = new StreetNumberSet(numBig);
    	
	}
	
	
	
    
    /**
     * test size operation
     */
    public void testSize(){
    	
    	assertEquals("size should be "+getSize(arrEq1)+", instead was "+
    			numSetEq1.size(), getSize(arrEq1), numSetEq1.size());
    	assertEquals("size should be "+getSize(arrDiff1)+", instead was "+
    			numSetDiff1.size(), getSize(arrDiff1), numSetDiff1.size());
    	assertEquals("size should be "+getSize(arrBig)+", instead was "+
    			numSetBig.size(), getSize(arrBig), numSetBig.size());
    	assertEquals("size should be "+getSize(arrZero)+", instead was "+
    			numSetZero.size(), getSize(arrZero), numSetZero.size());
    }
    
    
    /**
     * helper to test size
     */
	private int getSize(int[] arr){
	    int count = 0;
		for(int j = 0; j < arr.length; j++){
	    	count++;
	    }
		return count;
	}
    
	
    /**
     * test hashCode
     */
    public void testHashCode(){
    	assertTrue("numbersets covering the same number range, even with different " +
    			"string seeds, should have the same hash code", numSetEq1.hashCode() == 
    			numSetEq2.hashCode() );
    }
	

	
}