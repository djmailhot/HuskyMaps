package ps7;

import ps6.Address;
import ps2.GeoPoint;
import ps4.StreetNumberSet;
import ps4.StreetSegment;

/**
 * <p>
 * Immutable Data object class holding an Address and the StreetSegment it is
 * located on.
 * </p>
 * 
 * <p>
 * Includes functionality to produce fractional StreetSegments based on the
 * original street and the location of the address on the street.  Fractional
 * streets are StreetSegments representing travel between an end of the 
 * original street to the exact point of the address on the street.  The result
 * depend on the chosen address being a start address or an end address.
 * If a start address, the fractional segment goes from the address to this
 * street's P2.  If an end address, the segment goes from the original street's P1
 * and ends at the location of the address on the street.  
 * </p>
 * 
 * <p>
 * This class assumes that the locations corresponding to addresses
 * on this street are spaced evenly apart. For instance, if one side of 
 * this street contains the addresses 1, 3, and 99, then those numbers 
 * appear .25, .5, and .75 of the way along the street from the street's 
 * start point.  
 * </p>
 * 
 * @specfield  addr :  Address			// the address of this Entry.
 * @specfield  street : StreetSegment	// street segment of this Entry.
 * @specfield  endAddress : boolean		// whether this is an end-point address
 * @derivedfield  fractional : StreetSegment 	// the fractional segment representing
 * 										// travel on this street to the exact 
 * 										// point of the address on this street
 * @derivedfield  gp : GeoPoint			// the geographic point of this address
 */

public class StreetEntry {
	
	/*
	 * Abstraction Function
	 * 	fractionalSegment:
	 * 		if endAddress is true, then the fractional segment represents the
	 * 		distance to travel to enter this street and arrive at this address
	 * 		if endAddress is false, then the fractional segment represents the
	 * 		distance to travel to leave this address and exit this street at its end 
	 * 
	 * Representation Invariant:
	 * 		street.contains(addr.num, addr.zip)
	 * 
	 */
	
	
	/** switch to turn on debugging reporting for the class during runtime, printed to the console */
	private static final boolean DEBUG = false;
	
	/** the address of this Entry */
	private final Address addr;
	/** street segment of this Entry */
	private final StreetSegment street;
	/** true if this is an end-point address */ 
	private final boolean endAddress;
	/** the fractional segment representing travel between an end of the 
	 * original street to the exact point of the address on this street */
	private final StreetSegment fractional;
	/** the geographic point of this address */
	private final GeoPoint gp;
	
	/**
	 * Produces a StreetEntry holding the street and the corresponding address.
	 * Also generates a representative fractional segment.
	 * @requires street != null && addr != null && 
	 * 		street.contains( addr.getNum(), addr.getZipcode() )
	 * @param street the StreetSegment of this Entry
	 * @param addr the Address of this Entry
	 * @param endAddress true if the specified Address is a destination address 
	 * on the specified street
	 * @effects Creates a new StreetEntry containing the specified information
	 * and using it to generate a representative fractional segment.
	 */
	public StreetEntry(StreetSegment street, Address addr, boolean endAddress)
	{
		if(street == null || addr == null){
			throw new IllegalArgumentException("No parameters can be null");
		}else if(!street.contains( addr.getNum(), addr.getZipcode())){
			throw new IllegalArgumentException("The specified address must appear on this street");
		}
		this.addr = addr;
		this.street = street;
		this.endAddress = endAddress;
		
		double fracDist = street.fractionDist(addr.getNum());
		
		// figure out difference between P1 towards P2.
		int latDiff = street.getP2().getLatitude() - street.getP1().getLatitude();
		int lonDiff = street.getP2().getLongitude() - street.getP1().getLongitude();
		
		// calculate intermediate coordinates from the fraction distance.
		int newLat = (int)(street.getP1().getLatitude() + latDiff * fracDist);
		int newLon = (int)(street.getP1().getLongitude() + lonDiff * fracDist);
		// this point plus the ( difference reduced by the fractional distance factor )
		
		this.gp = new GeoPoint(newLat, newLon);
		
		
		if(endAddress){
			this.fractional = generateFracStreet(street.getP1(), gp, null, true);
		}else{   // start address case
			this.fractional = generateFracStreet(gp, street.getP2(), null, true);
		}
	}
	
	/**
	 * Get this Entry's address
	 * @return this StreetEntry's Address 
	 */
	public Address getAddress(){
		return addr;
	}
	
	/**
	 * Get this Entry's street
	 * @return this StreetEntry's StreetSegment 
	 */
	public StreetSegment getStreet(){
		return street;
	}
	
	/**
	 * Get the fractional street segment of this StreetEntry
	 * @return this StreetEntry's fractional length from street.p1 to the 
	 * address on the street if endAddress, else from this address to
	 * street.p2
	 */
	public double getFracLength(){
		return fractional.getLength();
	}
	
	/**
	 * Produce a reversed StreetEntry
	 * @return new StreetEntry with its StreetSegment reversed
	 */
	public StreetEntry reverse(){
		return new StreetEntry(street.reverse(), addr, endAddress);
	}
	
	/**
	 * Get this Entry's fractional StreetSegment.  Fractional streets are StreetSegments 
	 * starting at the original street's start point and ending at the location 
	 * of the address on the street.  
	 * @return a new StreetSegment with p2 ending at this Entry's address, as well
	 * as no address numbers of it's own (empty StreetNumberSets).  
	 */
	public StreetSegment getFractionalSegment(){
		return fractional;
	}
	
	/**
	 * Get a fractional segment representing the path from this Entry's address
	 * to the specified Entry's address, assuming both Entrys are for the same street
	 * segment.
	 * @requires se must contain the same StreetSegment as this
	 * @return a new StreetSegment with p1 located at this Entry's address, and
	 * p2 located at the specified se's address, or null if this and se are not over
	 * the same StreetSegment
	 */
	public StreetSegment getSameStreetFractional(StreetEntry se){
		StreetSegment other = se.street;
		if(street.equals(other)){
			boolean startBeforeEnd = (street.getP1().distanceTo(gp) < 
					street.getP1().distanceTo(se.gp));
			
			if(DEBUG && !startBeforeEnd) System.out.println("Start after End case for "
					+street+"where addresses are\nStart: "+addr+"\nEnd: "+se.addr);
			
			if(EnvironmentVariables.FRACTIONAL_SEGMENTS_ON){
				return generateFracStreet( (startBeforeEnd ? this.gp : se.gp), 
						(startBeforeEnd ? se.gp : this.gp), se.getAddress(), startBeforeEnd);
			}else{
				GeoPoint p1 = street.getP1();
				GeoPoint p2 = se.street.getP2();
				return generateFracStreet( (startBeforeEnd ? p1 : p2), 
						(startBeforeEnd ? p2 : p1), se.getAddress(), startBeforeEnd);
			}
		}
		return null;
	}
	
	/**
	 * Produce a fractional StreetSegment.  Fractional streets are StreetSegments 
	 * starting at the original street's start point and ending at the location 
	 * of the address on the street.  
	 * @return a new StreetSegment with p1 and p2 located at the specified GeoPoints,
	 * as well as StreetNumberSets containing only the single address number of this
	 * StreetEntry, or two addresses if the specified address other is not null.
	 */
	private StreetSegment generateFracStreet(GeoPoint p1, GeoPoint p2, Address other,
				boolean startBeforeEnd){
		StreetSegment frac = startBeforeEnd ? street : street.reverse();
		
		String zipL = "";
		String zipR = "";
		
		if(frac.rightSideContains(addr.getNum(), addr.getZipcode())){
			zipR += addr.getNum();
		}else{
			zipL += addr.getNum();
		}
		
		if(other != null){
			if(frac.rightSideContains(other.getNum(), other.getZipcode())){
				zipR += (zipR.length() != 0 ? "," : "") + other.getNum();
			}else{
				zipL += (zipL.length() != 0 ? "," : "") + other.getNum();
			}
		}
		
		return new StreetSegment(p1, p2, frac.getName(), 
				new StreetNumberSet(zipL), new StreetNumberSet(zipR), 
				frac.getLeftZip(), frac.getRightZip(), frac.getStreetClass(),
				frac.getIncreasingAddresses());
	}
	
	
	/**
	 * Get if the specified StreetEntry is over the same street as this
	 * @return true if ( this.addr.equals(o.addr) && this.street.equals(o.street) )
	 */
	@Override
	public boolean equals(Object o){
		if(o instanceof StreetEntry){
			return (street.equals(((StreetEntry)o).getStreet()));
		}
		return false;	
		
	}
	
}
