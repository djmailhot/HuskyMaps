package ps4;

import ps2.GeoPoint;
import ps2.GeoSegment;

/**
 * <p>
 * A StreetSegment represents a straight-line geographic street on the Earth.
 * It contains information about the building addresses located on the street.
 * It is immutable and extends GeoSegment.
 * </p>
 * 
 * <p>
 * A geographic street has a start point and and an end point. 
 * 'Left' and 'right' terminology refer to each side of the street when
 * traveling from the start point to the end point of the street segment.
 * </p>
 * 
 * <p>
 * A StreetSegment stores two collections of building addresses in a
 * StreetNumberSet object, one for each the right and left sides.
 * Common practice is to arrange all odd numbered addresses on one
 * side of a street and even on the other.  However, discrepancies are
 * allowed.  The segment also records the zip code for the addresses of 
 * each side. If the zip codes are different between the sides, then the same
 * building address may show up on both sides of the street.
 * </p>
 * 
 * <p>
 * A StreetSegment will be given a 'street classification' based on
 * the type of street it is.  This is to help differentiate a street
 * that is, for example, a main thorough fair as opposed to a country lane.  
 * </p>
 * 
 * @specfield leftNumbers : StreetNumberSet 	//left side address numbers 
 * @specfield rightNumbers : StreetNumberSet	//right side address numbers
 * @specfield leftZip : String					//left side zip code
 * @specfield rightZip : String					//right side zip code
 * @specfield streetClass : StreetClassification//the type of street
 * @specfield increasingAddresses : boolean 	//indicates whether addresses increase from p1 to p2
 **/
public strictfp class StreetSegment extends GeoSegment {

	/*
	 * Abstraction Function:
	 * AF(n)  =  n is a street number on this StreetSegment
	 * 		then there exists some k, such that
	 * 		if(leftZip == rightZip)
	 * 			then (leftNumbers.contains(k) || rightNumbers.contains(k)) 
	 * 		else(leftZip != rightZip && rightNumbers.contains(k))
	 * 
	 * 
	 * Representation Invariant:
	 * 		all fields are non-null
	 * 		if(leftZip == rightZip) 
	 * 			 !(rightNumbers.contains(k) && leftNumbers.contains(k)) for some int k
	 * 		leftZip && rightZip are a 5-digit Strings or empty Strings
	 * 		
	 */
	
	/** boolean switch to enable checks of the representation invariant */
	private final boolean CHECK_REP = false;
	
	/** The address numbers on the left side of this street */
	private final StreetNumberSet leftNumbers;
	/** The address numbers on the right side of this street */
	private final StreetNumberSet rightNumbers;
	/** The zipcode for the left side of this street */
	private final String leftZip;
	/** The zupcode for the right side of this street */
	private final String rightZip;
	/** This street's Street Classification */
	private final StreetClassification streetClass;
	/** Whether addresses are increasing from this street's p1 to p2 */
	private final boolean increasingAddresses;
	
    /**
     * Creates a new StreetSegment from the given arguments.
     *
     * @requires leftNumbers does not share any numbers with rightNumbers
     *           unless leftZip != rightZip; all arguments are
     *           non-null; leftZip and rightZip are valid zipcodes,
     *           where a valid zipcode is any 5-digit String or the
     *           empty String.
     *
     * @param p1
     *            one end of the StreetSegment
     * @param p2
     *            the other end of the StreetSegment
     * @param name
     *            the name of the street of which this is a segment
     * @param leftNumbers
     *            street numbers on the left side of the street
     * @param rightNumbers
     *            street numbers on the right side of the street
     * @param leftZip
     *            ZIP code on the left side of the street
     * @param rightZip
     *            ZIP code on the right side of the street
     * @param streetClass
     *            StreetClassification of this StreetSegment
     * @param increasingAddresses
     *            true if addresses increase from p1 to p2
     *            <p>
     *            The left and right sides of the street are as viewed from p1
     *            to p2.
     */
    public StreetSegment(GeoPoint p1, GeoPoint p2, String name,
                 StreetNumberSet leftNumbers, StreetNumberSet rightNumbers,
                 String leftZip, String rightZip, StreetClassification streetClass,
                 boolean increasingAddresses) {
    	
        super(name, p1, p2);
    	this.leftNumbers = leftNumbers;
    	this.rightNumbers = rightNumbers;
    	this.leftZip = leftZip;
    	this.rightZip = rightZip;
    	this.streetClass = streetClass;
    	this.increasingAddresses = increasingAddresses;
    	
    	if(CHECK_REP)
    		checkRep();
    }

    
    /**
     * @throw IllegalStateException if the representation invariant is broken
     */
    //check the representation invariant
    private void checkRep(){
    	if(!(leftZip.length() == 0 || leftZip.length() == 5 || rightZip.length() == 0 ||
    			rightZip.length() == 5)){
    		throw new IllegalStateException("zipcode of length "+leftZip.length()+" (left side)," +
    				" or "+rightZip.length()+" (right side), must be a 5-number string or " +
    				"the emtpy string");
    	}
    	
    	if(leftZip.equals(rightZip) && leftNumbers.size() != 0 && rightNumbers.size() != 0){
	    	int i = -1;
	    	double fraction = 0.0;
	    	int min = Math.min(leftNumbers.size(), rightNumbers.size());
	    	do{
	    		i++;
	    		if(leftNumbers.contains(i) || rightNumbers.contains(i)){
	    			fraction = fractionDist(i);
	    		}
	    		if(leftNumbers.contains(i) && rightNumbers.contains(i)){
    				throw new IllegalStateException("If zipcodes are the same, the same" +
    						" address cannot appear on both sides of the street");	    			
	    		}
	    	}while(fraction < 1.0 - (1 / ((double) min + 1)) );
    	}
    }
    
    
    /**
     * Get right side zipcode
     * @return the right side zipcode
     */
    public String getRightZip(){
    	return rightZip;
    }
    
    /**
     * Get left side zipcode
     * @return the left side zipcode
     */
    public String getLeftZip(){
    	return leftZip;
    }
    
    /**
     * Check if this street falls within the specified zipcode
     * @return true iff zip is one of this street's zipcodes
     */
    public boolean checkZip(String zip){
    	return rightZip.equals(zip) || leftZip.equals(zip);
    }
    
    /**
     * Check if the specified address number and corresponding zipcode exists on
     * this street.
     * @return true iff either the left side or right side of this segment
     * contains the address num && has a zipcode equal to zip
     */
    public boolean contains(int num, String zip){
    	return (leftNumbers.contains(num) && leftZip.equals(zip)) || 
    			(rightNumbers.contains(num) && rightZip.equals(zip));
    }
    
    
    /**
     * Check if the specified address number and corresponding zipcode exists on
     * the right side of this street.
     * @return true iff the right side of this segment contains the address
     * num && has a zipcode equal to zip
     */
    public boolean rightSideContains(int num, String zip){
    	return (rightNumbers.contains(num) && rightZip.equals(zip));
    }
    
    /**
     * Return whether address numbers increase on this street
     * @return true iff the addresses are increasing on this street, false if not
     */
    public boolean getIncreasingAddresses(){
    	return increasingAddresses;
    }
    
    /**
     * Return this street's Street Classification
     * @return this segments StreetClassification
     */
    public StreetClassification getStreetClass(){
    	return streetClass;
    }
    
    
    /**
     * Returns a new StreetSegment like this one, but with its endpoints
     * reversed.  Additionally, the right&left side-dependent data fields
     * must also be reversed.
     * @return a new StreetSegment ss such that
     *      ss.name = this.name
     *   && ss.p1 = this.p2
     *   && ss.p2 = this.p1
     *   && ss.leftNumbers = this.rightNumbers
     *   && ss.rightNumbers = this.leftNumbers
     *   && ss.leftZip = this.rightZip
     *   && ss.rightZip = this.leftZip
     **/
    public StreetSegment reverse(){
    	return new StreetSegment(super.getP2(), super.getP1(), super.getName(),
    			rightNumbers, leftNumbers, rightZip, leftZip, streetClass,
    			!increasingAddresses);
    }
    
    
    /**
     * This method assumes that the locations corresponding to street
     * numbers on this street are spaced evenly apart (regardless of
     * the arithmetic difference between two consecutive street
     * numbers).  It returns the fraction of the distance that the
     * street number sn is from p1.  The return value is a number from
     * 0 to 1, exclusive.
     * <p>
     * For instance, if one side of this street contains the street numbers
     * 1, 3, and 99, and this.increasingAddresses is true, then those
     * numbers appear .25, .5, and .75 of the way along the street. If
     * this.increasingAddresses is false, then the numbers appear .75, .5,
     * and .25 of the way along the street.
     * <p>
     * If both sides of the street have the number sn (which can happen
     * only if the two sides have different Zip codes), then this selects
     * the address on the right-hand side of the street (as viewed from p1).
     * 
     * @requires this instance of StreetSegment must contain the street number sn
     * @return the fraction of the length of this segment that is from p1 to
     *         the address sn
     */
    public double fractionDist(int sn) {
    	if(!rightNumbers.contains(sn) && !leftNumbers.contains(sn)){
    		throw new IllegalArgumentException("specified address must be on the street.");
    	}
    	
    	int order;
    	int size;
    	if(rightNumbers.contains(sn)){
   			order = rightNumbers.orderStatistic(sn);
   			size = rightNumbers.size();
    	}else{
    		order = leftNumbers.orderStatistic(sn); 
			size = leftNumbers.size();
    	}
    	
    	double value = ((double) order + 1) / ((double) size + 1);
    	if(!increasingAddresses){
    		value = 1.0 - value;
    	}
    	return value;
    }
    
    
    /**
     * @return the hash code of this segment
     */
    public int hashCode(){
    	return super.hashCode();
    }

}
