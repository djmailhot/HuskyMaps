package ps6;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import ps6.Address;
import ps7.EnvironmentVariables;
import ps2.*;
/**
 * This class implements an interactive text-based interface for Husky Maps.
 * Valid command line arguments can displayed with the single flag '-h'. 
 **/

public class TextUI {
	
	/** switch to turn on status reporting for the class during runtime, printed to the console */
	public static boolean VERBOSE = false;

	
    /**
	* @param args, for each element of args, the following flags are valid:</br>
	* -h for help text
	* -F to turn on fractional distance, -f to turn off
	* -I to turn on intersection traversal, -i to turn off
	* -C to turn on street classification, -c to turn off
	* after any flags, the first argument is the database path, while all
	* arguments after that are zipcodes to filter over, if any.
	*/
    public static void main(String args[]) {
        try {
            if (args.length == 0) {
                printUsage();
                return;
            }

            TextUI textui;

            int index = 0;
            String next = args[index];
            while(next.startsWith("-")){
                if(args[index].equals("-h")){
        			System.err.println("Set HuskyMaps environment variables!\n" +
        					"USAGE:\n" +
        					"-F to turn on fractional distance, -f to turn off\n" +
        					"-I to turn on intersection traversal, -i to turn off\n" +
        					"-C to turn on street classification, -c to turn off");
        			return;
        		}else{
    				EnvironmentVariables.setFlag(args[index]);
        		}
                next = args[index];
                index++;
            }
            String databaseName = next;
            List<String> zipcodes = new ArrayList<String>();
            for (int i = 1; i<args.length; i++) {
                zipcodes.add(args[i]);
            }

            textui = new TextUI(databaseName, zipcodes);


            BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
            textui.interactiveLoop(r, System.out);

        } catch (IOException e) {
            System.out.flush();
            System.err.println(e.toString());
            e.printStackTrace(System.err);

        }
    }

    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println("java ps6.TextUI [flags] directory-name  zipcode1 zipcode2 ...  zipcodeN\n" +
        		"Type ps6.TextUI -h for valid flags");
    }

    private final String databaseName;
    private final List<String> zipcodes;

    /**
     * @requires databaseName != null && zipcodes != null && 
     * !zipcodes.contains(invalid zipcode format)
     * A valid zipcode is defined as a String that contains exactly 5 numerals, from 0 to 9. 
     *
     * @effects Creates a new TextUI which loads from databaseName
     * and limits its search to the zipcodes given, unless the list
     * is empty or null, in which case it searches over all zipcodes.
     * @throws IllegalArgumentException if a passed zipcode is invalid
     **/
    public TextUI(String databaseName, List<String> zipcodes) {
        this.databaseName = databaseName;
        this.zipcodes = new ArrayList<String>(zipcodes);
        
        for(String s : zipcodes){
        	if(s.length() != 5){
        		throw new IllegalArgumentException("zipcode must be a 5 digit number");
        	}
        	try{
        		Integer.parseInt(s);
        	}catch(NumberFormatException ex){
        		throw new IllegalArgumentException("zipcode must be a 5 digit number");
        	}
        }
    }

    /**
     * Generates text-based user interaction prompts to ask the user for and starting 
     * point and desired destination.  It prints out the shortest-distance directions 
     * from the start point to the destination as a series of navigation instructions.
     *  An example of the instructions format: 
     * 
     * 		"Turn sharp left onto MacGregor Street and [go xxx distance]."
     * 
     * Where the format of '[go xxx distance]' depends on the specified mode of 
     * transportation.
     * 
     * To terminate the interaction loop, specify the "starting number" (at the
     * beginning of the prompt) as '-1'.
     * 
     * @throws InvalidDatabaseException if specified source database is invalid
     **/
    public void interactiveLoop(BufferedReader input, PrintStream output)
        throws IOException
    {
    	
    	if(VERBOSE){ System.out.println("--> Constructing the DirectionsFinder ..."); }
    	
    	DirectionsFinder virtualGPS = null;
    	try{
    		virtualGPS = DirectionsFinder.getDirectionsFinder(databaseName, zipcodes);
    		virtualGPS.VERBOSE = VERBOSE;
    		//throws InvalidDatabaseException if specified source database is invalid
    		
    	}catch(Exception ex){
    		output.println("Database error");
//            System.err.println(ex.toString());
//            ex.printStackTrace(System.err);
            
    	}finally{
    		    		
    		// UI loop:
    		do{
	            //   read addresses from input
	    		Address start = getAddress(input, output, true);
	    		if(start == null){  // if the starting address was specified as the exit command
	    			break;
	    		}
	    		Address end = getAddress(input, output, false);
	    		if(end == null){
	    			throw new RuntimeException("end address should never be set to null");
	    		}

	    		// get travel format
	        	output.print("walking or driving [w/d]? ");
	        	String travelMode = input.readLine().trim();
		    	RouteFormatter formatter = setFormat(travelMode);
		    	
		    	//////////// prompting complete ////////////
		    	
		    	if(VERBOSE){ System.out.println("--> Checking the addresses are valid ..."); }
		    	
		    	// address and directions format checks
		    	int startValid = virtualGPS.checkAddress(start);
		    	int endValid = virtualGPS.checkAddress(end);
		    	
		    	if(startValid != DirectionsFinder.ADDRESS_VALID){       // if start address is bad
		    		output.println(addressError(start, startValid));
		    		break;
		    	}else if(endValid != DirectionsFinder.ADDRESS_VALID){       // if end address is bad
		    		output.println(addressError(end, endValid));
		    		break;
		    	}else if(formatter == null){     // if directions format is invalid
		    		output.println("Invalid direction type: "+travelMode);
		    		break;
		    	}else{

		    		///////////// valid input provided ////////////
		    		
		        	if(VERBOSE){ System.out.println("--> Calculating directions ..."); }
		    		
		    		// attempt to find a path
		        	Directions navigate = null;
		        	try{
		        		navigate = virtualGPS.getDirections(start, end, formatter);
		        		
		        	}catch(InvalidAddressException ex){     // handle errors of bad input
		        		// this error should be caught earlier
		        		output.println("DirectionsFinder address indexing inconsistant");
		            //    System.err.println(ex.toString());
		            //    ex.printStackTrace(System.err);
		        		
		        	}catch(NoPathException ex){     // no path found
		        		output.println("No path from "+start+" to "+end);
		        		
		        	}finally{
		        	
		        		/////////////// valid directions found ////////////
		    		
		            	if(VERBOSE){ System.out.println("--> Displaying the results ..."); }
		        		
		        		displayDirections(navigate, output, formatter);
		        	}
		    	}
		    	
    		}while(true);
	    	
    	}
    }
    
    
    /** handles and outputs Directions as calculated by DirectionsFinder */
    private void displayDirections(Directions navigate, PrintStream output, RouteFormatter formatter)
    {
    	if(navigate == null){
    		throw new IllegalArgumentException("directions object should not be null");
    	}
    	
		//   write to output
		output.println("Start at "+navigate.getStart());
		
		Iterator<String> iter = navigate.iterator();
		while(iter.hasNext()){
			output.println(iter.next());
		}
		
		//distinguish between miles and minutes
		if(formatter instanceof WalkingRouteFormatter){
			//output.println("Trip time: " + (Math.round(navigate.getLength()) * 20) + " minutes");
			
			//output.print("Trip time: "+Math.round(navigate.getLength()*20)+" minutes\n");
			output.printf("Trip time: %.0f minutes\n", navigate.getLength() * 20);
		}else if(formatter instanceof DrivingRouteFormatter){
			//DecimalFormat printer = new DecimalFormat("#.#"); // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			
			//output.print("Trip length: "+(Math.round(navigate.getLength()*10))/10.0+" miles\n");
			output.printf("Trip length: %.1f miles\n", navigate.getLength());
		}else{
			throw new IllegalArgumentException("Directions implementor class not recognized");
		}
    }
    	
    
    
    /** helper to prompt user for information about an address and return an Address object 
     * @return an Address, or null if the user specifies "-1" as an exit command */
    private Address getAddress(BufferedReader input, PrintStream output, boolean starting)
    		throws IOException
    {
    	String where = (starting ? "starting" : "destination");
    	
    	output.print( where +" number? ");
    	String address = input.readLine().trim();
    	if(starting && address.equals("-1")){  // if the starting number is the exit command
    		return null;
    	}
    	
    	output.print( where +" street? ");
    	String street = input.readLine().trim();
    	output.print( where +" zipcode? ");
    	String zip = input.readLine().trim();

    	return new Address(Integer.parseInt(address), street, zip);
    }
    
    /** helper to prompt user for travel method and return a RouteFormatter for that method 
     * @return a RouteFormatter, or null if an invalid format is specified*/
    private RouteFormatter setFormat(String mode)
    {
    	if(mode.equals("w")){
    		return new WalkingRouteFormatter();
    	}else if(mode.equals("d")){
    		return new DrivingRouteFormatter();
    	}else{    // invalid argument
    		return null;
    	}
    }
    
    /** helper to handle error-reporting 
     * @return a String of the error report */
    private String addressError(Address addr, int cause){

    	if(cause == DirectionsFinder.ADDRESS_VALID){
    		return "Should not have thrown error for "+addr;
    	}else if(cause == DirectionsFinder.ADDRESS_ZIPCODE_INVALID){
			return "No such zipcode: "+addr;
		}else if(cause == DirectionsFinder.ADDRESS_STREET_INVALID){
			return "No such street: "+addr;
		}else if(cause == DirectionsFinder.ADDRESS_NUM_INVALID){
			return "No such number: "+addr;
		}else{  // address is null
			return "Invalid address argument";
		}
    }
    
	
	
}
