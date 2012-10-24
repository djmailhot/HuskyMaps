package ps4.test;

import java.io.*;
import java.util.*;

import ps4.RoutePath;
import ps3.graph.*;
import ps2.*;

/**
 * This class implements a testing driver which reads test scripts
 * from files for testing Graph and PathFinder.
 **/

public class PS4TestDriver {

    public static void main(String args[]) {
        try {
            if (args.length > 1) {
                printUsage();
                return;
            }

            PS4TestDriver td;

            if (args.length == 0) {
                td = new PS4TestDriver(new InputStreamReader(System.in),
                                       new OutputStreamWriter(System.out));
            } else {

                String fileName = args[0];
                File tests = new File (fileName);

                if (tests.exists() || tests.canRead()) {
                    td = new PS4TestDriver(new FileReader(tests),
                                           new OutputStreamWriter(System.out));
                } else {
                    System.err.println("Cannot read from " + tests.toString());
                    printUsage();
                    return;
                }
            }

            td.runTests();

        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace(System.err);
        }
    }

    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println("to read from a file: java ps3.GraphTestDriver <name of input script>");
        System.err.println("to read from standard in: java ps3.GraphTestDriver");
    }


    /** String -> Graph: maps the names of graphs to the actual graph **/
    private final Map<String, Graph<GeoSegment> > graphs = new HashMap<String, 
    		Graph<GeoSegment> >();
    /** String -> GeoSegment: maps the names of nodes to the actual node **/
    private final Map<String, GeoSegment> nodes = new HashMap<String, GeoSegment>();
    private final PrintWriter output;
    private final BufferedReader input;

    /**
     * @requires r != null && w != null
     *
     * @effects Creates a new PS4TestDriver which reads command from
     * <tt>r</tt> and writes results to <tt>w</tt>.
     **/
    public PS4TestDriver(Reader r, Writer w) {
        input = new BufferedReader(r);
        output = new PrintWriter(w);
    }

    /**
     * @effects Executes the commands read from the input and writes results to the output
     * @throws IOException if the input or output sources encounter an IOException
     **/
    public void runTests()  //<<<<<<<<<<<<<<<<<<<< called by ScriptFileTests.runScriptFile() 
        throws IOException
    {
        String inputLine;
        while ((inputLine = input.readLine()) != null) {
            if (inputLine.trim().length() == 0 ||
                inputLine.charAt(0) == '#') {
                // echo blank and comment lines
                output.println(inputLine);
            }
            else
            {
                // separate the input line on white space
                StringTokenizer st = new StringTokenizer(inputLine);
                if (st.hasMoreTokens()) {
                    String command = st.nextToken();

                    List<String> arguments = new ArrayList<String>();
                    while (st.hasMoreTokens()) {
                        arguments.add(st.nextToken());
                    }

                    executeCommand(command, arguments);
                }
            }
            output.flush();
        }
    }

    private void executeCommand(String command, List<String> arguments) {
        try {
            if (command.equals("CreateGraph")) {
                createGraph(arguments);
            } else if (command.equals("CreateGeoNode")) {
                createGeoNode(arguments);
            } else if (command.equals("AddNode")) {
                addNode(arguments);
            } else if (command.equals("AddEdge")) {
                addEdge(arguments);
            } else if (command.equals("ListNodes")) {
                listNodes(arguments);
            } else if (command.equals("ListChildren")) {
                listChildren(arguments);
            } else if (command.equals("FindGeoPath")) {
                findGeoPath(arguments);
            } else {
                output.println("Unrecognized command: " + command);
            }
        } catch (Exception e) {
            output.println("Exception: " + e.toString());
        }
    }

    private void createGraph(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to CreateGraph: " + arguments);
        }

        String graphName = arguments.get(0);
        createGraph(graphName);
    }

    private void createGraph(String graphName) {
        // Insert your code here.

        graphs.put(graphName, new Graph<GeoSegment>());
        output.println("created graph "+graphName);
    }

    private void createGeoNode(List<String> arguments) {
        if (arguments.size() < 5) {
            throw new CommandException("Bad arguments to createNode: " + arguments);
        }

        String nodeName = arguments.get(0);
        int lat1 = Integer.parseInt(arguments.get(1));
        int long1 = Integer.parseInt(arguments.get(2));
        int lat2 = Integer.parseInt(arguments.get(3));
        int long2 = Integer.parseInt(arguments.get(4));
        String segName = arguments.get(5);

        createGeoNode(nodeName, lat1, long1, lat2, long2, segName);
    }

    private void createGeoNode(String nodeName, int lat1, int long1, int lat2, int long2,
    		String segName) {
        // Insert your code here.

        nodes.put(nodeName, new GeoSegment(segName, new GeoPoint(lat1, long1), 
        		new GeoPoint(lat2, long2)));
        output.println("created node "+nodeName+": "+lat1+" "+long1+" "+lat2+" "+
        		long2+" "+segName);
    }

    private void addNode(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to addNode: " + arguments);
        }

        String graphName = arguments.get(0);
        String nodeName = arguments.get(1);

        addNode(graphName, nodeName);
    }

	private void addNode(String graphName, String nodeName) {
        // Insert your code here.
    	Graph<GeoSegment> g = graphs.get(graphName);
    	GeoSegment n = nodes.get(nodeName);
    	if(g == null){
    		throw new IllegalArgumentException("graph must already exist");
    	}else if(g.containsNode(n)){
    		throw new IllegalArgumentException("node must not already be in graph");
    	}
    	
    	g.addNode(nodes.get(nodeName));
        graphs.put(graphName, g);
        output.println("added node "+nodeName+" to "+graphName);
    }

    private void addEdge(List<String> arguments) {
        if (arguments.size() != 3) {
            throw new CommandException("Bad arguments to addEdge: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        String childName = arguments.get(2);

        addEdge(graphName, parentName, childName);
    }

    private void addEdge(String graphName, String parentName, String childName) {
        // Insert your code here.
    	Graph<GeoSegment> g = graphs.get(graphName);
        GeoSegment nP = nodes.get(parentName);
        GeoSegment nC = nodes.get(childName);
        if(g == null){
    		throw new IllegalArgumentException("graph must already exist");
        }else if(!g.containsNode(nP) || !g.containsNode(nC)){
    		throw new IllegalArgumentException("nodes must already be in graph");
    	}else if(!g.containsEdge(nP, nC)){
	        g.addEdge(nP, nC);
	        graphs.put(graphName, g);
    	}
        output.println("added edge from "+parentName+" to "+childName+" in "+graphName);
    }


    private void listNodes(List<String> arguments) {
        if (arguments.size() != 1) {
            throw new CommandException("Bad arguments to listNodes: " + arguments);
        }

        String graphName = arguments.get(0);
        listNodes(graphName);
    }

    private void listNodes(String graphName) {
        // Insert your code here.
    	Graph<GeoSegment> g = graphs.get(graphName);
        if(g == null){
    		throw new IllegalArgumentException("graph must already exist");
        }
        
        String result = "";
        Set<String> s = new TreeSet<String>();
        for(GeoSegment n : g.nodeSet()){
        	s.add(n.getName());
        }
        
        if(s.isEmpty()){
        	result = " ";
        }else{
	        Iterator<String> iter = s.iterator();
	        while(iter.hasNext()){
	        	result += " "+iter.next();
	        }
        }
        
        output.println(graphName+" contains:"+result);
    }

    private void listChildren(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to listChildren: " + arguments);
        }

        String graphName = arguments.get(0);
        String parentName = arguments.get(1);
        listChildren(graphName, parentName);
    }

    private void listChildren(String graphName, String parentName) {
        // Insert your code here.
    	Graph<GeoSegment> g = graphs.get(graphName);
        GeoSegment nP = nodes.get(parentName);
        if(g == null){
    		throw new IllegalArgumentException("graph must already exist");
        }else if(!g.containsNode(nP)){
    		throw new IllegalArgumentException("node must already be in graph");
    	}
        
        String result = "";
        Set<String> s = new TreeSet<String>();
        for(GeoSegment n : g.edgeSet(nP)){
        	s.add(n.getName());
        }
        
        if(s.isEmpty()){
        	result = " ";
        }else{
	        Iterator<String> iter = s.iterator();
	        while(iter.hasNext()){
	        	result += " "+iter.next();
	        }
        }
        
        output.println("the children of "+parentName+" in "+graphName+" are:"+result);
    }
    

    private void findGeoPath(List<String> arguments) {
        String graphName;
        List<String> sourceArgs = new ArrayList<String>();
        List<String> destArgs = new ArrayList<String>();

        if (arguments.size() < 1) {
            throw new CommandException("Bad arguments to FindPath: " + arguments);
        }

        Iterator<String> iter = arguments.iterator();

        graphName = iter.next();

        while (iter.hasNext()) {
            String s =  iter.next();
            if (s.equals("->")) {
                break;
            }
            sourceArgs.add(s);
        }
        while (iter.hasNext()) {
            destArgs.add(iter.next());
        }

        if (sourceArgs.size() < 1) {
            throw new CommandException("Too few source args for FindPath");
        }
        if (destArgs.size() < 1) {
            throw new CommandException("Too few dest args for FindPath");
        }

        findGeoPath(graphName, sourceArgs, destArgs);
    }

    private void findGeoPath(String graphName, List<String> sourceArgs, List<String> destArgs) {
        // Insert your code here.

        Graph<GeoSegment> world = graphs.get(graphName);
        Set<Path<GeoSegment, RoutePath> > starts = 
        		new HashSet<Path<GeoSegment, RoutePath> >(); 
        Set<GeoSegment> goals = new HashSet<GeoSegment>();
        
        for(int j = 0; j < destArgs.size(); j++){
        	goals.add(nodes.get(destArgs.get(j)));
        }
        for(int i = 0; i < sourceArgs.size(); i++){
        	starts.add(new RoutePath(nodes.get(sourceArgs.get(i)), goals));
        }
        
        Path<GeoSegment, RoutePath> shortest = 
        		PathFinder.findPath(world, starts, goals); // call static method of PathFinder
        
        output.print("shortest path in "+graphName+":");
        if(shortest == null){
        	output.println(" no path found");
        }else{
	        RouteFormatter voice = new DrivingRouteFormatter();
	        
        	Route rt = null;
	        Iterator<GeoSegment> iter = shortest.iterator();
	        while(iter.hasNext()){
	        	if(rt == null){
	        		rt = new Route(iter.next());
	        	}else{
	        		rt = rt.addSegment(iter.next());
	        	}
	        }
	        
		    output.print("\n" + voice.computeDirections(rt, 0.0));

        }
    }

    /**
     * This exception results when the input file cannot be parsed properly
     **/
    static class CommandException extends RuntimeException {

        public CommandException() {
            super();
        }
        public CommandException(String s) {
            super(s);
        }

        public static final long serialVersionUID = 3495;
    }
}