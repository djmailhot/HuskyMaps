package ps4.test;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.List;
import java.util.LinkedList;

import ps4.*;
import junit.framework.*;

public class StreetNumberSetTest extends TestCase {

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
	

	
	
	
	// constant for size of ultimate stress test
	// used for the size of a random number set of singles between 0 and 2000000.
	private final int STRESS_FACTOR = 500000;
	

	
	
	public StreetNumberSetTest(String name){
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
	 * test valid input string
	 */
	public void testValidInput(){
    	String num = "18-26,15-19";
    	try{
    		StreetNumberSet numSet = new StreetNumberSet(num);
    	}catch(Exception ex){
    		fail("input string of \""+num+"\" is valid");
    	}
	}
	
	
	
    /**
     * test the memory and operations stress of the implementation
     * uses a random number set of singles between 0 and 1000000 of size STRESS_FACTOR
     */
	public void testMemoryAndOperationsStress(){
		/**	variables for stress testing */
		int[] arrStress = new int[STRESS_FACTOR];
		
		System.out.println("\nWORLD EXPLODES");
		StreetNumberSet numSet = new StreetNumberSet(generate(arrStress));
		System.out.println("\nafter the bomb...");
		
    	getContains(numSet, arrStress);
		
    	for(int stat = 0; stat < 1000000; stat = stat * stat + 1){
    		getStats(numSet, arrStress, stat);
    	}
    	
    	numSet.equals(numSet);
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
     * test contains operation
     */
    public void testContains(){
    	
    	// check contains is true
    	getContains(numSetEq1, arrEq1);
    	getContains(numSetEq2, arrEq2);
    	getContains(numSetDiff1, arrDiff1);
    	getContains(numSetDiff2, arrDiff2);
    	getContains(numSetZero, arrZero);
    	getContains(numSetBig, arrBig);
    }
    
    
    /**
     * test contains on negative numbers
     */
    public void testNotContainsNegative(){
    	int[] arr = {-1,-4,-16,-2,-3,-312};
    	
    	for(int i = 0; i < arr.length; i++){
    		try{
    			assertFalse("Street should note contain any negative numbers",
    				numSetBig.contains(arr[i]));
    		}catch(Exception ex){
    			fail(ex+" is an invalid response:  specification does not forbid " +
    					"negative arguments for contains().");
    		}
    	}
    }
    
    
    /**
     * test false cases for contains
     */
    public void testNotContains(){
      	// check that adjacent ints of opposite parity are not present when
    	// they shouldn't be.
    	getNotContains(numSetEq1, arrEq1);
    	getNotContains(numSetEq2, arrEq2);
    	getNotContains(numSetDiff1, arrDiff1);
    	getNotContains(numSetDiff2, arrDiff2);
    	getNotContains(numSetZero, arrZero);
    	getNotContains(numSetBig, arrBig);
    	
    }
    
    
    /**
     * helper to test false cases for contains
     */
    private void getNotContains(StreetNumberSet numSet, int[] arr){
    	List<Integer> in = new LinkedList<Integer>();
    	List<Integer> out = new LinkedList<Integer>();
    	for(int i = 0; i < arr.length; i++){
    		in.add(arr[i]);
    	}
    	
    	int count = 0;
    	int index = 0;
    	while(count < 100){
	    	if(!in.contains(count)){
    			out.add(count);
	    	}
	    	count++;
    	}
    	
    	for(Integer i : out){
    		try{
    			assertFalse("NumberSet should not contain number "+i+", which is outside " +
    					"of this NumberSet's range",
    				numSet.contains(i));
    		}catch(Exception ex){
    			fail(ex+" is an invalid response:  specification does not forbid " +
    					"any bad arguments for contains().");
    		}
    	}
    	
    }
    
    
    /**
     * helper to test whether a segment contains what is should
     */
    private void getContains(StreetNumberSet numSet, int[] arr){
    	for(int i = 0; i < arr.length; i++){
    		assertTrue("Street should contain "+arr[i], 
    				numSet.contains(arr[i]));
    	}
    }
    

    
    /**
     * test orderStatistic operation
     */
    public void testOrderStatistic(){
    	for(int stat = 0; stat < 100; stat += 7){ 
    		getStats(numSetEq1, arrEq1, stat);
   		 	getStats(numSetDiff1, arrDiff1, stat);
   		 	getStats(numSetZero, arrZero, stat);
    		getStats(numSetBig, arrBig, stat);
    	}
    }
    
    
    /**
     * test orderStatistic operation on negative numbers
     */
    public void testOrderStatisticNegative(){
    	for(int stat = -10; stat <= 0; stat++){ 
	    	getStats(numSetEq1, arrEq1, stat);
			getStats(numSetDiff1, arrDiff1, stat);
			getStats(numSetZero, arrZero, stat);
			getStats(numSetBig, arrBig, stat);
    	}
    }
    
    
    /**
     * helper to test statistics
     */
    private void getStats(StreetNumberSet numSet, int[] arr, int stat){
    	int count = 0;
    	int i;
    	for(int j = 0; j < arr.length; j++){
    		i = arr[j];
    		if(i < stat){
    			count++;
    		}
    	}
    	
    	try{
    		assertEquals("statistics count should be "+count+", instead was "+
    				numSet.orderStatistic(stat), count, numSet.orderStatistic(stat));
    	}catch(Exception ex){
    		fail(ex+" is an invalid response:  specification does not forbid " +
    				"arguments outside of address range for orderStatistics().");
    	}
    }
    
    /**
     * test empty set
     */
    public void testEmptySet(){
    	StreetNumberSet zero = new StreetNumberSet("");
		for(int j = 0; j < arrBig.length; j++){
			assertFalse("an empty number set should never contain a number",
					zero.contains(j));
	    	assertTrue("an empty number set should always have 0 elements" +
	    			"under number "+arrBig[j], zero.orderStatistic(arrBig[j]) == 0);
		}
    }
    
    
    /**
     * test segments of different seed number sets, but same range coverage are equal
     */
    public void testEquals(){
    	
    	assertTrue("number sets built from different number strings covering" +
    			"the same number range should be equal",numSetEq1.equals(numSetEq2));
    	assertFalse("number sets built from different number strings covering" +
    			"different number ranges should not be equal",numSetEq1.equals(numSetDiff1));
    	assertFalse("number sets built from different number strings covering" +
    			"different number ranges should not be equal",numSetEq1.equals(numSetDiff2));
    	assertFalse("a non-empty number set should not be equal to a zero-set",
    			numSetEq1.equals(numSetZero));
    }
    
    /**
     * test segments that should not be equal
     */
    public void testNotEqualsBadType(){
    	assertFalse("a number set should not be equal to a string",arrEq1.equals(""));
    	assertFalse("a number set should not be equal to null",arrEq1.equals(null));
    }
    
    
}
