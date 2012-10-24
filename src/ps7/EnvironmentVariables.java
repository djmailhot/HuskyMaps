package ps7;

/**
 * Class of static variables and switches that control the behavior of extra
 * features of HuskyMaps.  Accessed by TextUI and GraphicUI with command line arguments.
 */

public class EnvironmentVariables {

	/** Switch to turn on fractional segments */
	public static boolean FRACTIONAL_SEGMENTS_ON =  false;
	
	/** Switch to turn on Pathfinding algorithm for performance.  Doubles the cost
	 * of A*'s hypothetical distance-to-goal pathfinding optimization. */
	public static boolean OPTIMIZE_FOR_PERFORMANCE_ON = false;

	/** Switch to turn on intersection traversal cost */
	public static boolean INTERSECTION_TRAVERSAL_ON = false;
	/** Standard measure of the cost of traversing an intersection, in miles. 
	 * Based on an average wait time of a FIFTEEN SECONDS at each intersection.
	 * If street classification turned on, based on travel time for the street class.
	 * Otherwise, FIFTEEN SECONDS wait time calculated for an average speed of 2 minutes-per-mile. 
	 * ( 30 MPH ) therefore, .25 miles-per-minute intersection cost */
	public static final double INTERSECTION_TRAVERSAL_COST = .25;
	/** Standard number of intersecting streets required to be considered an intersection. */
	public static final int STREETS_PER_INTERSECTION = 3;
	
	/** Switch to turn on street classification cost */
	public static boolean STREET_CLASSIFICATION_ON = false;
	/** Modifier of based on Street Classification, representing standard 
	 * minutes-per-mile averages for primary highway road type. */
	public static final double PRIM_HWY_COST = 60.0/60.0;
	/** Modifier of based on Street Classification, representing standard 
	 * minutes-per-mile averages for secondary highway road type. */
	public static final double SEC_HWY_COST = 60.0/35.0;
	/** Modifier of based on Street Classification, representing standard 
	 * minutes-per-mile averages for local road type. */
	public static final double LOCAL_ROAD_COST = 60.0/25.0;
	/** Modifier of based on Street Classification, representing standard 
	 * minutes-per-mile averages for an unknown road type. */
	public static final double UNKNOWN_ROAD_COST = 60.0/15.0;
	
    /** Factor for modified A* of cost = path length + (distance to nearest goal) * FACTOR 
     *  Increasing factor improves PathFinding operating time with a corresponding loss in accuracy.
     *  Meant to be used in conjunction with the MAXIMUM_BUFFER_DISTANCE.  This PathFinding
     *  optimization will be active if intersection traversal is turned on. */
	public static final double DISTANCE_TO_GOAL_COST_FACTOR = 3.00;
	/** Maximum buffer distance in miles surrounding the leading edge of the most probable path.
	 * Within this buffer distance, path distance-to-goal cost ( of the A* path finding algorithm )
	 * is determined normally.  Outside this distance, path distance-to-goal cost should be
	 * exponentially increased.  This PathFinding optimization will be active if  
	 * intersection traversal is turned on. */
	public static double MAXIMUM_BUFFER_DISTANCE = 0.5;
		
	
	/** Processes the specified command line flag and sets the corresponding switch */
	public static void setFlag(String flag){

		if(flag.equals("-F")){
			FRACTIONAL_SEGMENTS_ON =  true;
		}else if(flag.equals("-f")){
			FRACTIONAL_SEGMENTS_ON =  false;
		}else if(flag.equals("-I")){
			INTERSECTION_TRAVERSAL_ON = true;
		}else if(flag.equals("-i")){
			INTERSECTION_TRAVERSAL_ON = false;
		}else if(flag.equals("-C")){
			STREET_CLASSIFICATION_ON = true;
		}else if(flag.equals("-c")){
			STREET_CLASSIFICATION_ON = false;
		}
		
	}
	
}
