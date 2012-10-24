package ps4;

import java.util.*;

/**
 * <p>
 * A StreetNumberSet is an immutable data object containing the range of 
 * building numbers that are present on a street.  Building numbers 
 * are indicative of building addresses located on a geographic street.
 * </p>
 * 
 * <p>
 * Ranges of building addresses always represent a collection of same-parity
 * addresses.  For instance, an address range of 6-12 indicates the presence
 * of all even addresses between 6 and 12, inclusive:  6, 8, 10, and 12.  No
 * single address range will ever include both an even and an odd number. 
 * </p>
 * 
 * 
 * @specfield rangeMins : int[]		//the minimums of address-range pairs representing 
 * 									//building addresses present
 * @specfield rangeMaxs : int[]		//the maximums of address-range pairs representing 
 * 									//building addresses present
 * @specfield size : int		//the number of addresses represented in this set
 */
public class StreetNumberSet {
	/*
	 * 
	 * Abstraction Function
	 * AF(n)  =  n is a street number in this StreetNumberSet
	 * 		then there exists some i, such that
	 * 		(n  >=  rangeMins[i]) &&  	> within a range
	 * 		(n  <=  rangeMaxs[i]) && 
	 * 		(n % 2 == rangeMins[i] % 2)	> and same parity 
	 * 		
	 * 
	 * Representation Invariant
	 * rangeMins[j] > 0	for any valid j
	 * rangeMaxs[j] > 0	for any valid j
	 * rangeMins[j] % 2 == rangeMaxs[j] % 2   for any valid j
	 * that is, the min/max pair of an address range must be the same parity.
	 * 
	 */
	
	/** boolean switch to enable checks of the representation invariant */
	private final boolean CHECK_REP = false;
	
	
	/** the unparsed number string */
	private final String numbers;
	/** whether the number string has been parsed or not */
	private boolean parsed;
	/** the minimums of the address ranges for buildings present.  The min at element 
	 * i corresponds to the max at element i of rangeMins */ 
	private int[] rangeMins;
	/** the maximums of the address ranges for buildings present.  The max at element 
	 * i corresponds to the min at element i of rangeMins */
	private int[] rangeMaxs;
	/** the number of addresses represented by this NumberSet*/
	private int size;
	
	
    /**
     * Creates a StreetNumberSet containing the numbers indicated in the
     * argument.
     *
     * @requires numbers != null && numbers is a space-free comma-delimited list
     *           of zero or more disjoint parity ranges. Two parity ranges
     *           are disjoint if they share no elements.
     *           For example, 10-20 and 19-21 are disjoint:  as
     *           one only contains even elements and the other contains odd.
     *           By contrast, 1-5 and 3-7 overlap, as they both contain 3 and 5.
     *
     * <p>
     * A parity range is either a single nonnegative integer "n" or a
     * hyphen-separated pair of nonnegative integers "m-n", where m and n have
     * the same parity and m is no greater than n. A range m-n represents the
     * set of all numbers from m to n, inclusive, which are the same parity as m
     * and n.
     *
     * <p>
     * For instance, legal arguments to this constructor include
     * "", "5", "22,253", "3-101", and "1914-1918,1939-1945".
     */
    public StreetNumberSet(String numbers) {
    	if(numbers == null){
    		throw new IllegalArgumentException("Input string cannot be null");
    	}
    	this.numbers = numbers;
    	parsed = false;
    }
    
    /** Parses out the actual number range of the string representation held by this
     * number set. */
    private void parseNumberRange(){
    	int size = 0;
    	TreeMap<Integer, Integer> pairMap = new TreeMap<Integer, Integer>();
    	
    	int first = 0;
    	int second = 0;
    	boolean singleRange = false;
    	
    	if(numbers.length() != 0){
	    	// split along commas
	    	String[] ranges = numbers.split(",");
	    	singleRange = ranges.length == 1;
	    	
	    	TreeMap<Integer, Integer> evenMap = new TreeMap<Integer, Integer>();
	    	TreeMap<Integer, Integer> oddMap = new TreeMap<Integer, Integer>();
	    	
	    	// for as many separate ranges that are indicated
	    	for(int i = 0; i < ranges.length; i++){
	    		// split along dashes
	    		String[] pair = ranges[i].split("-");
	    		
	    		// derive int value from string information and insert into rangeMins 
	    		// and rangeMaxs
	    		first = Integer.parseInt(pair[0]);
	    		second = first;  					// handle single case
	    		size++;
	    		
	    		if(pair.length != 1){					// handle pair case
	    			second = Integer.parseInt(pair[1]);
	    			// maintain repInvariant
	    			if(first % 2 != second % 2){
	    				throw new IllegalArgumentException("unequal parity for a " +
	    						"specified range increment");
	    			}

		    		size += (second - first) / 2;
	    		}
	    		
	    		if(!singleRange){
		    		// condense adjacent min/max range pairs
	    			if(first % 2 == 0){
	    				condense(evenMap, first, second);
	    			}else{
	    				condense(oddMap, first, second);
	    			}
	    		}

	    	}
	    	
	    	// join all min/max range pairs
	    	pairMap.putAll(oddMap);
	    	pairMap.putAll(evenMap);
	    	

	    	
    	}
    	this.size = size;

    	
    	if(singleRange){
    		rangeMins = new int[1];
    		rangeMins[0] = first;
    		rangeMaxs = new int[1];
    		rangeMaxs[0] = second;
    		
    	}else{

	    	// store min/max ranges in memory-optimizing arrays
	    	rangeMins = new int[pairMap.size()];
	    	rangeMaxs = new int[pairMap.size()];
	    	
	    	Iterator<Integer> iter = pairMap.navigableKeySet().iterator();
	    	int index = 0;
	    	while(iter.hasNext()){
	    		int key = iter.next();
	    		rangeMins[index] = key;
	    		rangeMaxs[index] = pairMap.get(key);
	    		index++;
	    	}
    	}
    	
    	if(CHECK_REP)
    		checkRep();
    }
    
    
    /**
     * helper to examine the entries around the specified key for consecutiveness with key
     * higher, modifies key entry if consecutive, deletes higher key
     * lower, modifies lower entry if consecutive
     */
    private void condense(TreeMap<Integer,Integer> order, Integer key, Integer value){
    	if(key != null){
	    	// get higher of key
    		Integer hKey = order.higherKey(key);
	    	if(hKey != null){
	    		if(hKey == value + 2){   // if adjacent to key
	    			
	    			// replace value with higher's value
	    			value = order.get(hKey);
	    			//remove higher
	    			order.remove(hKey);
	    		}
	    	}
	    	// get lower of key
	    	Integer lKey = order.lowerKey(key);
	    	if(lKey != null){
	    		if(order.get(lKey) == key - 2){
	    			
	    			// replace key with lower's key
	    			key = lKey;
	    			// lower key overwritten below with value
	    		}
	    	}
	    	
	    	order.put(key, value);
    	}
    }
	
    
    
    /**
     * @throw IllegalStateException if the representation invariant is broken
     */
    //check the representation invariant
    private void checkRep(){
    	if( size != 0 ){
	    	List<Integer> list = new ArrayList<Integer>();
	    	for(int i = 0; i < rangeMins.length; i++){
	    		int first = rangeMins[i];
	    		int second = rangeMaxs[i];
	    		if(first < 0 || second < 0){
    				throw new IllegalStateException("No represented address can be negative");
	    		}
	    		
	    		if(first%2 != second%2){
    				throw new IllegalStateException("Cannot have a min/max range pair" +
							" of opposite parity.");
	    		}
	    		
	    		for(int j = first; j <= second; j += 2){
	    			if(list.contains(j)){
	    				throw new IllegalStateException("Cannot have overlapping ranges" +
	    						" of the same parity.");
	    			}
	    			list.add(j);
	    		}
	    	}
    	}
    }
    
    
    /**
     * @return true if other is a StreetNumberSet that contains the same
     * numbers as this.  In particular, this expression evaluates to true:
     * <pre>new StreetNumberSet("1-9").equals(new StreetNumberSet("1-3,5-9"))</pre>
     */
    public boolean equals(Object other) {
    	if(other == null || !(other instanceof StreetNumberSet)){
        	return false;
        }
    	if(!parsed){
    		parseNumberRange();
    	}
    	
    	if(CHECK_REP){
			checkRep();
			((StreetNumberSet) other).checkRep();
    	}
    	
    	if(size() != ((StreetNumberSet) other).size()){
    		return false;
    	}
    	
    	for(int i = 0; i < rangeMins.length; i++){
    		if(rangeMins[i] != ((StreetNumberSet) other).rangeMins[i] ||
    				rangeMaxs[i] != ((StreetNumberSet) other).rangeMaxs[i]){
    			return false;
    		}
    	}

    	return true;
    }

    /**
     * Gets if the number appears in this number set 
     * @return true iff i is in this 
     */
    public boolean contains(int i) {
    	if(!parsed){
    		parseNumberRange();
    	}
    	
    	if(size == 0 || i < 0){
    		return false;
    	}
    	
    	int index = Arrays.binarySearch(rangeMins, i);
    	
    	if( index >= 0){  // i was equal to a minimum range marker
    		return true;
    	}else if(index == -1){  // i is indicated to be less than the first minimum marker
    		return false;
    	}else{  // else prepare for searching lower minimum markers
    		index = -index - 1;
    	}
    	
    	// find the closest lower minimum marker of the same parity
    	int first;
    	do{
    		index--;
    		first = rangeMins[index];
    	}while(index > 0 && i % 2 != first % 2);
    	
    	// check that i is between the minimum marker and the maximum marker, equal parity
		int second = rangeMaxs[index];    		
		if( (i % 2 == first % 2 && i % 2 == second % 2) && i >= first && i <= second){
			return true;
		}
    	
    	return false;
    	
    }
    
    
    /**
     * Get the number of address numbers represented
     * @return the number of addresses represented in this NumberSet
     */
    public int size(){
    	if(!parsed){
    		parseNumberRange();
    	}
    	return size;
    }
    

    /**
     * Get the number of address numbers in this less than the specified number  
     * @return the number of elements less than i in this 
     */
    public int orderStatistic(int i) {
    	if(!parsed){
    		parseNumberRange();
    	}
    	if(size == 0 || i < 0){
    		return 0;
    	}
    	
    	int sum = 0;
    	int index = 0;
    	int first;
    	do{
    		first = rangeMins[index];
    		int second = rangeMaxs[index];
    		if(i > second){					// normal case
    			sum += (second - first) / 2 + 1;
    		}else if(i > first){			// case of first < i < second
    			sum += (i - first + 1) / 2;
    		}
    		index++;
    	}while(index < rangeMins.length && i >= first); 
    	
    	return sum;
    }
    

}
