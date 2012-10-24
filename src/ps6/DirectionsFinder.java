package ps6;

import java.util.*;

import ps2.DrivingRouteFormatter;
import ps2.GeoPoint;
import ps2.GeoSegment;
import ps2.Route;
import ps2.WalkingRouteFormatter;
import ps3.graph.Graph;
import ps3.graph.Path;
import ps3.graph.PathFinder;
import ps4.StreetSegment;
import ps7.StreetEntry;
import ps7.StreetPath;


/**
 *   A DirectionsFinder produces directions for traveling from one
 *   address to another in a particular geographical area.  Geographical information
 *   is obtained from a valid Tiger Database Directory, a directory of geographical 
 *   street and address information.
 *   @derivedfield segments : set[StreetSegment]  // the set of StreetSegments
 *   which represents the geographical area for which this DirectionsFinder
 *   produces directions
 *   @specfield geographicMap : graph[StreetSegment]  // the graph representing the
 *   geographic area to be operated over by DirectionsFinder.
 *   @specfield streetIndex : Map[String, Set[StreetSegment] ]  // an index of all 
 *   street names within this geographic area, with each name mapped to all street 
 *   segments with that name.
 *   @specfield fractionalEntryIndex : Map[StreetSegment, StreetSegment]
 *    Map of fractional StreetSegments to corresponding full StreetSegments that 
 *    each was fractioned over, where full segments are present on the graph.
 *   @specfield zipcodeFilter : Collection[String]  // collection of zipcodes that 
 *   this geographic area is constrained to, or if nothing contained, then there 
 *   is no zipcode limit to the area's coverage.
 *   
 */
public class DirectionsFinder
{
	/*
	 * Representation Invariant
	 * 	geographicMap:
	 * 		must not contain duplicate StreetSegments
	 * 	streetIndex:
	 * 		for any Set<StreetSegment> s in streetIndex.values():
	 * 			s must not contain a StreetSegment with name "(unnamed street)"
	 * 			&&  for at least one zipcode z of zipcodeSet, s.checkZip(z) == true 
	 * 
	 * 
	 * Abstraction Function
	 *  set[StreetSegment] segments:
	 *  	Collection segments[GeoSegment, StreetSegment].values() contains all 
	 *  	StreetSegments this DirectionsFinder produces directions over
	 * 	Streets:
	 * 		each element of geographicMap.nodeSet() is a unique street in this geographic
	 * 		area.
	 * 	Intersections:
	 * 		let Set<StreetSegment> s = geographicMap.edgeSet(StreetSegment p)
	 * 		for all segments p contained in the graph
	 * 			then street p shares an intersection with each street c of s 
	 * 			&&  it is a valid driving/walking maneuver to traverse from street p
	 * 			to street c at that intersection.
	 * 
	 */
	
	/** switch to turn on status reporting for the class during runtime, printed to the console */
	public static boolean VERBOSE = false;
	/** switch to turn on debugging reporting for the class during runtime, printed to the console */
	private static boolean DEBUG = false;
	/** switch to turn on additional debugging reporting for the class during runtime, printed to the console */
	private static boolean DEBUG_FRACTIONAL = false;
	
	/** constant to aid in error-check-identification of a valid address */
	public static final int ADDRESS_VALID = 0;
	/** constant to aid in error-check-identification of an invalid zipcode */
	public static final int ADDRESS_ZIPCODE_INVALID = -1;
	/** constant to aid in error-check-identification of an invalid street */
	public static final int ADDRESS_STREET_INVALID = -2;
	/** constant to aid in error-check-identification of an invalid address number */
	public static final int ADDRESS_NUM_INVALID = -3;
	/** constant to aid in error-check-identification of a null address */
	public static final int ADDRESS_NULL = -4;

	
	
	/** Graph to represent geographic relationships between all streets 
	 * of this geographic area */
	private Graph<StreetSegment> geographicMap;
	/** Index of street names to all StreetSegments with that name */
	private Map<String, Set<StreetSegment> > streetIndex;
	/** Map of GeoSegments to corresponding StreetSegments, to avoid casting, and
	 * all contained within the graph */
	private Map<GeoSegment, StreetSegment> segments;
	/** Map of fractional StreetSegments to corresponding full StreetSegments that each
	 * was fractioned over, where full segments are present on the graph. */
	private Map<StreetSegment, StreetSegment> fractionalEntryIndex;
	/** Set of zipcodes that are contained in this geographic area. */
	private Set<String> zipcodeSet;

	
    // dummy constructor
    private DirectionsFinder ()  {  }

    
    /** Produces a DirectionsFinder for a given database.
     * @param databaseName the database to find directions in
     * @param zf a collection of zipcodes used for zipcode filtering.
     * Only matching zipcodes will be used.
     * If zf is empty or null then there is no zipcode filtering for
     * the database.
     * @return A DirectionsFinder whose set of segments is
     *  { s | s in (new StreetSegReader(databaseName)).streetSegs () &&
     *  (s.leftZip in zf || s.rightZip in zf || (s.leftZip.trim () ==
     *  s.rightZip.trim () == "") || zf == null) }
     * @throws InvalidDatabaseException if databaseName does not refer to a
     *  valid tiger database.
     */
    public static DirectionsFinder getDirectionsFinder(String databaseName, Collection<String> zf)
    		throws InvalidDatabaseException {
        
    	StreetSegReader source;
    	try{
    		source = new StreetSegReader(databaseName);
			//throws InvalidSourceException if specified source database is invalid
    	}catch(Exception ex){
    		throw new InvalidDatabaseException("Specified Tiger Database Directory invalid");
    	}
	    
        // <<<< create a street index >>>>
        // indexing structure of street names to all the street segments with that name
        Map<String, Set<StreetSegment> > streetIndex = new HashMap<String, Set<StreetSegment> >();
        Map<GeoSegment, StreetSegment> segments = new HashMap<GeoSegment, StreetSegment>();
        Set<String> zipcodeSet = new HashSet<String>();
        if(zf != null && !zf.isEmpty()){
        	zipcodeSet.addAll(zf);
        }
        
        if(VERBOSE){ System.out.println("--> Filtering zipcodes of "+zf+" ..."); }
        
        long startNodes = System.currentTimeMillis();
        // for all segments of the database
        Iterator<StreetSegment> iter = source.streetSegments();
        while(iter.hasNext()){
        	StreetSegment stS = iter.next();
        	
        	// if within the specified zipcode boundaries 
        	boolean rightZipIn = withinZip(stS, stS.getRightZip(), zf);
        	boolean leftZipIn = withinZip(stS, stS.getLeftZip(), zf);
        	if(rightZipIn || leftZipIn){
        		
        		// add to the total collection
        		segments.put(stS, stS);
        		StreetSegment rev = stS.reverse();
        		segments.put(rev, rev);
        		if(zf == null || zf.isEmpty()){
        			zipcodeSet.add(stS.getLeftZip());
        			zipcodeSet.add(stS.getRightZip()); 
        		}
        		
        		// for the segment index
				String name = stS.getName();
				
				// don't include segments of "(unnamed street)" in the index, so they can't be found
				if(!name.equals("(unnamed street)")){
					
					// expand the mapping
			    	Set<StreetSegment> t = !streetIndex.containsKey(name) ? 
			    			new HashSet<StreetSegment>() : streetIndex.get(name);
			    	t.add(stS);
			    	streetIndex.put(name, t);
				}
        	}
		}
        long endNodes = System.currentTimeMillis();
        if(VERBOSE) System.out.println("Time for populating: " + (endNodes-startNodes)/1000.0);
    	
        DirectionsFinder dirFind = new DirectionsFinder();
        
        if(VERBOSE){ System.out.println("--> Constructing the Graph ..."); }
        
    	// use a factory class to produce a graph of the geographic area
    	dirFind.geographicMap = GraphGenerator.produceGraph(segments.values());
    	
    	dirFind.streetIndex = Collections.unmodifiableMap(streetIndex);
		dirFind.segments = Collections.unmodifiableMap(segments);
		dirFind.fractionalEntryIndex = new HashMap<StreetSegment, StreetSegment>();
		dirFind.zipcodeSet = Collections.unmodifiableSet(zipcodeSet);

		return dirFind;
    }
    
    
    /** helper to check segments against the zip code filter*/
    private static boolean withinZip(StreetSegment gs, String zip, Collection<String> zf){
    	return  (zf == null || zf.isEmpty() || 
    			zf.contains(zip) || 
    			(gs.getRightZip().equals("") && gs.getLeftZip().equals("")) );
    }
    
    
    /** Gets directions from start to end.
     * @requires routeFormat != null
     * @throws InvalidAddressException if start or end is not in the
     *  geographical area covered by this DirectionsFinder.
     * @throws NoPathException if no path could be found from start to end.
     * @throws IllegalArgumentException if the specified RouteFormatter is null
     * @return A Directions providing directions of travel from start to end.
     */
    public Directions getDirections(Address start, Address end, ps2.RouteFormatter routeFormat)
        	throws InvalidAddressException, NoPathException {
        
        if(routeFormat == null){
        	throw new IllegalArgumentException("RouteFormatter must not be null");
        }else{
        	int first = checkAddress(start);
        	int second = checkAddress(end);
        	if(first != ADDRESS_VALID || second != ADDRESS_VALID){
        		throw new InvalidAddressException("Address does not exist (should have caught this in TextUI): "+
        					"start = "+first+", end = "+second);
        	}
        }
        
    	if(VERBOSE){ System.out.println("--> Calculating the shortest path ..."); }
        
    	StreetEntry startEntry = new StreetEntry(findAddress(start), start, false); 
    	StreetEntry endEntry = new StreetEntry(findAddress(end), end, true);
    	
    	
        // calculate the shortest path
		Path<StreetSegment, StreetPath> path = calculateShortestPath(startEntry, endEntry);
		
        // Check if no path was found
        if(path == null){
        	throw new NoPathException("No path found");
        }
        
    	if(VERBOSE){ System.out.println("--> Processing the found path ..."); }
        
        // make new Route using path.iterator(), catalog first and last streets
        Iterator<StreetSegment> iter = path.iterator();
        StreetSegment next = iter.next();
        startEntry = new StreetEntry(next, start, false);
        Route route = new Route(next);
        while(iter.hasNext()){
        	next = iter.next();
	       	route = route.addSegment(next);
        }
        endEntry = new StreetEntry(next, end, true);
        
    	if(DEBUG){
    		System.out.println("found route contains the following:");
    		for(GeoSegment gs : route.getGeoSegments()){
    			System.out.println(gs);
    		}
    		System.out.println("route done");
    	}
        
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> CONSISTENCY CHECK FOR PATHFINDER ON SAME-STREET ADDRESSES
        if(startEntry.getStreet().equals(endEntry.getStreet().reverse())){
        	throw new RuntimeException("INCONSISTENCY for same-street addresses PathFinder " +
        			"should return single-street path");
        }
        
        
    	List<String> directions = processDirections(route, startEntry, endEntry, routeFormat); //new LinkedList<String>();

        return new PathDirections(directions, route, start, end);
        
    }
    
    /** 
     * returns an error-code indicating whether the specified address addr is a 
     * valid address, that is streetIndex contains a StreetSegment sts such that 
     * 		sts.name == addr.name &&
     * 		sts.contains(addr.number,  addr.zipcode)
     * @return an int value equal to class constants defined for this class:
     * 		ADDRESS_VALID if the specified address is a valid address
     * 		ADDRESS_ZIPCODE_INVALID if the address zipcode is invalid
     * 		ADDRESS_STREET_INVALID if the address street name is not within the address zipcode
     * 		ADDRESS_NUM_INVALID if the address number is invalid for an existing street
     * 		ADDRESS_NULL if the address object is null
     * */
    public int checkAddress(Address addr){
    	if(addr == null){
    		return ADDRESS_NULL;
    	}
    	
    	// if this address's zip passes the zipcode filter
    	String addrZip = addr.getZipcode();
    	if(!zipcodeSet.contains(addrZip)){
    		return ADDRESS_ZIPCODE_INVALID;
    	}
    	
    	// if this address's street name is a real street
    	String addrName = addr.getName();
    	if(!streetIndex.containsKey(addrName)){
    		return ADDRESS_STREET_INVALID;
    	}
    	
    	boolean zipHasStreet = false;
    	int addrNum = addr.getNum();
    	// for all streets of this name
    	for(StreetSegment stS : streetIndex.get(addrName)){
    		
    		// if this street exists in the specified zipcode
    		if(stS.checkZip(addrZip)){
    			zipHasStreet = true;
	    	
    			// if this address is found on a street with the right zipcode
	    		if(stS.contains(addrNum, addrZip)){
	    			return ADDRESS_VALID;
	    		}
    		}
    	}
    	// if the street was in the zipcode, then only the address was wrong 
    	return zipHasStreet ? ADDRESS_NUM_INVALID : ADDRESS_STREET_INVALID;
    }
    
    
    ////////////////////// PATHFINDING METHODS /////////////////////
    
    /**
     * helper to search the StreetSegment index for the segment that the
     * specified address is located on
     * @requires specified address must be valid
     * @return the StreetSegment that the specified address is located on.
     * @throws IllegalArgumentException if address not found
     */
    private StreetSegment findAddress(Address addr){
    	for(StreetSegment stS : streetIndex.get(addr.getName())){
    		if(stS.contains(addr.getNum(), addr.getZipcode())){
    			return stS;
    		}
    	}
    	throw new IllegalArgumentException("Address-not-found inconsistancy");
    }
    
    /**
     * Searches for the StreetSegment companion to the specified GeoSegment using
     * the segment indexes built within this instance of DirectionsFinder.  Designed
     * to find full-length StreetSegments for fractional segments as well (as fractional
     * segments are never contained within the underlying Graph collection) . 
     * @requires specified GeoSegment must correspond to a street within the graph
     * whether a whole or a fractional segment
     * @return the StreetSegment that the specified GeoSegment refers to, even if that
     * GeoSegment is fractional
     * @throws IllegalArgumentException if street not found
     */
    public StreetSegment findFullSegment(GeoSegment gs){
    	StreetSegment street = segments.get(gs);
    	// fractional case
		street = (street == null ? fractionalEntryIndex.get(gs) : street );
		// runtime error case
		if(street == null) throw new IllegalArgumentException("Passed in segment "+gs+
				" which was not an element of this instance of DirectionsFinder"); 
    	return street;
    }
    
    
    /** Sets up parameters for StreetPath and calls PathFinder to calculate 
     * the travel path */
    private Path<StreetSegment, StreetPath> calculateShortestPath(StreetEntry depart,
    		StreetEntry destiny){
    	
    	StreetEntry departRev = depart.reverse();
    	StreetEntry destinyRev = destiny.reverse();
    	catalogEntrys(depart, destiny);
    	catalogEntrys(departRev, destinyRev);
    	if(DEBUG_FRACTIONAL){
    		for(StreetSegment stS : fractionalEntryIndex.keySet()){
    			System.out.println("Recorded fractional segment "+stS+" on street "+
    					fractionalEntryIndex.get(stS));
    		}
    	}    	
    	
        // goals = make a set of 2 (norm + rev) end segments based on end address
        Set<StreetEntry> goals = new HashSet<StreetEntry>();
        goals.add(destiny);
        goals.add(destinyRev);
        
        // starts = make a set of 2 (norm + rev) RoutePaths based on start address
        Set<Path<StreetSegment, StreetPath> > starts = new HashSet<Path<StreetSegment, StreetPath>>();
        starts.add(new StreetPath(depart, goals, geographicMap));
        starts.add(new StreetPath(departRev, goals, geographicMap));
        
        Set<StreetSegment> streetGoals = new HashSet<StreetSegment>();
        for(StreetEntry se : goals){
        	streetGoals.add(se.getStreet());
        }
        
        return PathFinder.findPath(geographicMap, starts, streetGoals);
    }

    
    /** helper to catalog fractional StreetSegments generated by StreetEntry objects */
    private void catalogEntrys(StreetEntry start, StreetEntry end){
    	StreetSegment first = start.getStreet();
    	StreetSegment second = end.getStreet();
    	if(first.equals(second) || first.equals(second.reverse())){
        	// catalog same street case
        	fractionalEntryIndex.put(start.getSameStreetFractional(end), start.getStreet());
    	}else{
	    	// catalog the fractional segments of the start address 
	    	fractionalEntryIndex.put(start.getFractionalSegment(), start.getStreet());
	    	fractionalEntryIndex.put(start.getFractionalSegment().reverse(), start.getStreet().reverse());
	    	
	    	// catalog the fractional segments of the end address
	    	fractionalEntryIndex.put(end.getFractionalSegment(), end.getStreet());
	    	fractionalEntryIndex.put(end.getFractionalSegment().reverse(), end.getStreet().reverse());
    	}

    }
    
    
    ///////////////////////// DIRECTIONS METHODS /////////////////////////
    
    
	/** helper to determine the start heading of travel when beginning from an 
	 * address on a street */
    private double addressHeading(StreetSegment street, Address addr){
    	double heading = street.getHeading();
    	// rotate based on the street side the address is on
    	heading += (street.rightSideContains(addr.getNum(), addr.getZipcode())) ? -90 : 90;
    	heading += heading > 360 ? -360 : 0;  // make sure rep invariant is held
    	heading += heading < 0 ? 360 : 0;
    	return heading;
    }
    
   
    /* Generates directions for the same-segment directions case. Directions are hard-coded,
     * and do not rely on a RouteFormatter */
    /*private String processSameStreet(StreetSegment street, Address start, Address end, 
    		ps2.RouteFormatter format){

    	// if start address farther down the street
       	boolean endBeforeStart = (((getFractDistance(street, start) > getFractDistance(street, end))));

       	
       	// figure out start turn
       	String startTurn;
       	if((street.rightSideContains(start.getNum(), start.getZipcode()) && endBeforeStart) ||
       			(!street.rightSideContains(start.getNum(), start.getZipcode()) && !endBeforeStart)){
       		startTurn = "Turn left ";
       	}else{
      		startTurn = "Turn right ";
       	}

       	// figure out end turn
       	String endTurn;
       	if((street.rightSideContains(end.getNum(), end.getZipcode()) && endBeforeStart) ||
       			(!street.rightSideContains(end.getNum(), end.getZipcode()) && !endBeforeStart)){
       		endTurn = "left";
       	}else{
      		endTurn = "right";
       	}
       	
       	double length = street.getLength();
       	String travel = (format instanceof WalkingRouteFormatter ? "walk for "+Math.round(length*20) +" minutes." : 
       			"go "+Math.round(length*10)/10.0+" miles." );
   	 	
   	 	return startTurn+"onto "+street.getName()+" and "+travel+"\n"+end+" is on your " + endTurn;
       	
    }*/
    
    /** Generates a list of directions from a complete route, ready to be passed to a 
     * Directions container object.  Also handles same segment case as well. */
    private List<String> processDirections(Route route, StreetEntry startEntry, StreetEntry endEntry, 
    		ps2.RouteFormatter routeFormat){
    	
    	// catch the same-segment case
    	StreetSegment sameStreet = null;
    	if(startEntry.getStreet().equals(endEntry.getStreet())) 
    	{
    		// get the fractional street with start address before end address
    		sameStreet = startEntry.getSameStreetFractional(endEntry);
    		route = new Route(sameStreet);
    	}
    	
    	// get directions over the route
	    String directLine = routeFormat.computeDirections(route, addressHeading( (sameStreet != null ? 
	    		sameStreet : startEntry.getStreet() ), startEntry.getAddress()));
	    
	    // add the last arrival line, taking into account same-segment possibility
	    Address endAddress = endEntry.getAddress();
        StreetSegment endStreet = (sameStreet != null ? sameStreet : endEntry.getStreet() );
	    directLine += endAddress+" is on your " +
        		(endStreet.rightSideContains(endAddress.getNum(), endAddress.getZipcode() ) ? 
        				"right" : "left");
	    
		List<String> directions = new LinkedList<String>();
		String[] lines = directLine.split("\n");
		
		// add the  directions
		// for every line, that is an element
		for(int i = 0; i < lines.length; i++){
			directions.add(lines[i]);
		}
       	
		return directions;
    }

    
    /* helper to correctly determine fractional distance, even in case of
     *  same address number on both sides */
    /*private double getFractDistance(StreetSegment stS, Address addr){
    	int num = addr.getNum();
    	// because fractionDist is only guaranteed to grab the right-side distance 
    	// in case of same number on both sides
    	if(stS.rightSideContains(num, addr.getZipcode())){
    		return stS.fractionDist(num);
    	}else{  // reverse to treat segment's left-side as a fractionDist right-side  
    		return 1.0 - stS.reverse().fractionDist(num);
    	}
    }*/
    
    
    ////////////////////////// GRAPH MAP CONTEXT METHODS /////////////////////
    
    /**
     * Determines nearby segments of the Graph to the specified Route.
     * @returns collection of all segments of the Graph that fall within the specified 
     * latitude and longitude bounds and connect somehow to the specified Route
     */
    public Collection<StreetSegment> getContext(int[] minMaxLatLon, Route rt){
    	if(DEBUG || DEBUG_FRACTIONAL){
    		System.out.println("found route contains the following:");
    		for(GeoSegment gs : rt.getGeoSegments()){
    			System.out.println(gs);
    		}
    		System.out.println("route done");
    	}
    	
    	Set<StreetSegment> map = new HashSet<StreetSegment>(); 
    	
    	Stack<StreetSegment> explore = new Stack<StreetSegment>();
    	explore.push( findFullSegment(rt.getEndingGeoSegment()) );
    	
		if(DEBUG) System.out.println("explore segment "+rt.getEndingGeoSegment());
    	
    	for(GeoSegment gs : rt.getGeoSegments()){
    		explore.push( findFullSegment(gs) );
    		explore.push( findFullSegment(gs.reverse()) );
			if(DEBUG) System.out.println("explore segment "+gs);
    	}
    	
    	// for all segments needed to explore
    	while(!explore.isEmpty()){
    		StreetSegment gs = explore.pop();
    		// if this is a unique segment and it starts within the bounds
    		if(!map.contains(gs) && !map.contains(gs.reverse()) && withinContext(minMaxLatLon, gs.getP1())){
    			map.add(gs);
    			if(DEBUG) System.out.println("added to map segment "+gs+" with bounds of minMaxLatLon");
    		}
    		
    		// even if it isn't, still need to examine this segment's P2 children
    		if(withinContext(minMaxLatLon, gs.getP2())){   // if it's P2 is within the bounds
	    		for(StreetSegment edge : geographicMap.edgeSet(gs)){
	    			if(!map.contains(edge) && !map.contains(edge.reverse())){
	    				explore.push(edge);     // explore all children
	    				if(DEBUG) System.out.println("explore segment "+edge);
	    			}
	    		}
    		}
    		
    	}
    	
    	return map;
    }
    
    
    /** helper to determine if a GeoPoint is within the specified latitude and
     * longitude bounds */
    private boolean withinContext(int[] bounds, GeoPoint gp){
    	return gp.getLatitude() > bounds[0] && gp.getLatitude() < bounds[1] &&
    	gp.getLongitude() > bounds[2] && gp.getLongitude() < bounds[3];
    }
    
    
}
