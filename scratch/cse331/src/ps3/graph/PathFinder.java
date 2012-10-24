package ps3.graph;

import java.util.*;

/**
 * The first generic argument (<tt>N</tt>) is the type of nodes in the path.
 * The second generic argument (<tt>P</tt>) should be the name of the
 * implementing class itself (see WeightedNodePath for an example).
 */
// N is the type of node, P is the specific type of path that implements the Path interface
public class PathFinder<N, P extends Path<N, P> >  {
	
	/** switch to turn on status reporting for the class during runtime, printed to the console */
	public static boolean VERBOSE = false;
	
	/**
	 * static algorithm that accepts a graph to work over, a set of start PATHS (end
	 * nodes of these PATHS representing starting nodes), and a set of goal nodes. 
	 * @requires all elements of specified *starts and *goals sets must be members of
	 * Graph *world, and said collections must not be empty.
	 * @return a Path of nodes representing the shortest and least-cost path
	 * from one of the start nodes to one of the end nodes.  if no path found,
	 * returns null.
	 */
    // Return a shortest path from any element of starts to any
    // element of goals in a node-weighted graph.
	public static<N, P extends Path<N, P>>  
			Path<N, P>  findPath(Graph<N> world, Set<Path<N, P>> starts, Set<N> goals){
		
		if(world == null || starts == null || goals == null){
			throw new IllegalArgumentException("Specified graph and sets " +
					"must not be null.");
		}else if(world.isEmpty() || starts.isEmpty() || goals.isEmpty()){
			throw new IllegalArgumentException("Specified graph must not be empty." +
					" Specified sets of start and end points must not be empty.");
		}
	      
	    // The priority queue contains PATHS with priority equal to the cost
	    // of traversing that PATH.  Initially it contains single node PATHs 
	    // constructed using the nodes of *starts.
	    Queue< Path<N, P> > active = new PriorityQueue< Path<N, P> >();
	    // A hash set of *active elements to optimize searching in the PriorityQueue
	    Set<Path<N, P>> elements = new HashSet<Path<N, P>>();
	    
	    for(Path<N, P> path : starts){ 	    // add every start to the map with itself as the first node of it's queue
	    	active.add(path);
	    	elements.add(path);
	    }
	    
	    
	    // The set of *finished nodes are those whose PATHS we know are the shortest
	    // to that node from at least one of the start nodes.  All nodes between 
	    // the start and end points of each node's PATH have had their children
	    // fully examined.
	    Set<N> finished = new HashSet<N>();
	    

	    // while active still has PATHS to examine
	    while(!active.isEmpty()){
	    	// queueMin is the PATH of active with the least cost
	        Path<N, P> queueMin = active.remove();
	        elements.remove(queueMin);
	        
	        // if the endpoint of the choosen PATH is a goal
	        // ---------------------------, SPECIAL, what if a goal node is part of a start node (start PATH)?
	        if (goals.contains(queueMin.end())) {
	        	if(VERBOSE){ System.out.println("... Path reached goal with "+queueMin.end().toString()+" ..."); }
	            return queueMin;
	        }
	        
	        // iterate over all edges of this PATH's end node (world.edgeSet(queueMin.end()))
	        for(N c : world.edgeSet(queueMin.end())) {
	        	Path<N, P> cpath = queueMin.extend(c);
	        	
	        	// if this PATH is not already part of *active and the end node 
	        	// is not already part of *finished
		        if (!finished.contains(c) && !elements.contains(cpath)) {
		            //paths.put(c, cpath); // put this new path into the paths map
		            
		            active.add(cpath); // add this new path to the *active queue
		            elements.add(cpath);
		            
		            //if(VERBOSE){ System.out.println("... Added path of cost "+cpath.cost()+" at "+queueMin.end().toString()+" ..."); }
		        }
		    }
	    
	    	// after examining all children of this node, and adding
	    	// new possible PATHs to *active, put this node into *finished
		    finished.add(queueMin.end());
		    
	    	if(VERBOSE){ System.out.println("... Extend path to "+queueMin.end().toString()+" ..."); }
	    }
	    // execution reaches this point only if active becomes empty
		return null; 
    }
	

}
