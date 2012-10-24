package ps3.test;

import java.io.*;
import java.util.*;

import ps3.graph.*;


/**
 * This class implements a testing driver which reads test scripts
 * from files for testing Graph and PathFinder.
 **/
public class PS3TestDriver {

    public static void main(String args[]) {
        try {
            if (args.length > 1) {
                printUsage();
                return;
            }

            PS3TestDriver td;

            if (args.length == 0) {
                td = new PS3TestDriver(new InputStreamReader(System.in),
                                       new OutputStreamWriter(System.out));
            } else {

                String fileName = args[0];
                File tests = new File (fileName);

                if (tests.exists() || tests.canRead()) {
                    td = new PS3TestDriver(new FileReader(tests),
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
    private final Map<String, Graph<WeightedNode> > graphs = new HashMap<String, 
    		Graph<WeightedNode> >();
    /** String -> WeightedNode: maps the names of nodes to the actual node **/
    private final Map<String, WeightedNode> nodes = new HashMap<String, WeightedNode>();
    private final PrintWriter output;
    private final BufferedReader input;

    /**
     * @requires r != null && w != null
     *
     * @effects Creates a new PS3TestDriver which reads command from
     * <tt>r</tt> and writes results to <tt>w</tt>.
     **/
    public PS3TestDriver(Reader r, Writer w) {
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
            } else if (command.equals("CreateNode")) {
                createNode(arguments);
            } else if (command.equals("AddNode")) {
                addNode(arguments);
            } else if (command.equals("AddEdge")) {
                addEdge(arguments);
            } else if (command.equals("ListNodes")) {
                listNodes(arguments);
            } else if (command.equals("ListChildren")) {
                listChildren(arguments);
            } else if (command.equals("FindPath")) {
                findPath(arguments);
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

        graphs.put(graphName, new Graph<WeightedNode>());
        output.println("created graph "+graphName);
    }

    private void createNode(List<String> arguments) {
        if (arguments.size() != 2) {
            throw new CommandException("Bad arguments to createNode: " + arguments);
        }

        String nodeName = arguments.get(0);
        String cost = arguments.get(1);

        createNode(nodeName, cost);
    }

    private void createNode(String nodeName, String cost) {
        // Insert your code here.

        nodes.put(nodeName, new WeightedNode(nodeName, Integer.parseInt(cost)));
        output.println("created node "+nodeName+" with cost "+cost);
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
    	Graph<WeightedNode> g = graphs.get(graphName);
    	WeightedNode n = nodes.get(nodeName);
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
    	Graph<WeightedNode> g = graphs.get(graphName);
        WeightedNode nP = nodes.get(parentName);
        WeightedNode nC = nodes.get(childName);
        if(g == null){
    		throw new IllegalArgumentException("graph must already exist");
        }else if(!g.containsNode(nP) || !g.containsNode(nC)){
    		throw new IllegalArgumentException("nodes must already be in graph");
    	}else if(g.containsEdge(nP, nC)){
    		throw new IllegalArgumentException("edge must not already be in graph");
    	}
        
        g.addEdge(nP, nC);
        graphs.put(graphName, g);
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
    	Graph<WeightedNode> g = graphs.get(graphName);
        if(g == null){
    		throw new IllegalArgumentException("graph must already exist");
        }
        
        String result = "";
        Set<String> s = new TreeSet<String>();
        for(WeightedNode n : g.nodeSet()){
        	s.add(n.name());
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
    	Graph<WeightedNode> g = graphs.get(graphName);
        WeightedNode nP = nodes.get(parentName);
        if(g == null){
    		throw new IllegalArgumentException("graph must already exist");
        }else if(!g.containsNode(nP)){
    		throw new IllegalArgumentException("node must already be in graph");
    	}
        
        String result = "";
        Set<String> s = new TreeSet<String>();
        for(WeightedNode n : g.edgeSet(nP)){
        	s.add(n.name());
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
    

    private void findPath(List<String> arguments) {
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

        findPath(graphName, sourceArgs, destArgs);
    }

    private void findPath(String graphName, List<String> sourceArgs, List<String> destArgs) {
        // Insert your code here.

        Graph<WeightedNode> world = graphs.get(graphName);
        Set<Path<WeightedNode, WeightedNodePath> > starts = 
        		new HashSet<Path<WeightedNode, WeightedNodePath> >(); 
        Set<WeightedNode> goals = new HashSet<WeightedNode>();
        
        for(int i = 0; i < sourceArgs.size(); i++){
        	starts.add(new WeightedNodePath(nodes.get(sourceArgs.get(i))));
        }
        for(int j = 0; j < destArgs.size(); j++){
        	goals.add(nodes.get(destArgs.get(j)));
        }
        
        Path<WeightedNode, WeightedNodePath> shortest = 
        		PathFinder.findPath(world, starts, goals); // call static method of PathFinder
        
        if(shortest == null){
        	output.println("no path found in "+graphName);
        }else{
	        String result = "";
	        Iterator<WeightedNode> iter = shortest.iterator();
	        while(iter.hasNext()){
	        	result += " "+iter.next().name();
	        }
	        
	        output.println("shortest path in "+graphName+":" + result);
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
