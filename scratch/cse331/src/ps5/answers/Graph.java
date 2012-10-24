package ps5.answers;

import java.util.*;

/**
 * A Graph is an mutable generic collection.
 * It that stores objects as 'nodes' and records one-way pointers
 * between objects as 'edges'.  
 * 
 * An edge points only one way:
 * It extends from a parent node and points to a child node.  No parent
 * node may have two edges pointing to the same child.  However, nodes
 * can have self-pointing edges.  
 * 
 * A graph cannot contain two identical nodes.  However, nodes can exist in 
 * the graph without any edges associated with them.
 * 
 * 
 * @specfield nodeMap : Map<node, Set<edge> >   //map of nodes, each with a 
 * 												//corresponding set of edges
 * 
 * Abstract Invariant
 * A graph must not contain two copies of the same node or two copies
 * of the same edge
 * 
 */

public class Graph<E>{
	
	/**
	 * Abstraction Function
	 * AF(r) = a graph g, such that
	 * g.nodes = r.nodeMap.keySet()
	 * g.edges(node n) = r.nodeMap.get(n)
	 * 
	 * 
	 * Representation Invariant
	 * Set<E> nodeMap.keySet() must not have duplicates
	 * Set<E> nodeMap.get(n) for any node n must not have duplicates
	 * 
	 * 
	 * Graph is implemented as a mutable object because of it's collection
	 * behavior (It stores 'nodes' and unique relationships between 'nodes' as data).
	 * No collection in the Java libraries is hard-coded to be immutable to my knowledge.
	 * With the constant database updates that a collection undergoes in everyday use,
	 * a mutable implementation is much more performance-optimized. 
	 */
	
	private Map<E, Set<E>> nodeMap;
	
	
	/**
	 * 	Instantiates a new instance of an empty graph
	 * 	@returns an empty graph object
	 */
	public Graph(){
		nodeMap = new HashMap<E, Set<E>>();
	}
	
	
	/**
	 * helper to guarantee representation invariant.  
	 * ATTENTION:  Not used in implemented code due to O(n^3) complexity rating. 
	 * REASONING:  The implemented collections that store nodes (maps and sets) 
	 * automatically maintain the representation invariant.  Graph implementation 
	 * protects against overwriting keys in java collections Map (see Graph.addNode()).  
	 * Specification of java collections Set guarantees no duplicates.
	 */
	private void checkRep(){
		List<E> nodes = new LinkedList<E>();
		List<E> edges = new LinkedList<E>();
		for(E n : nodeMap.keySet()){
			nodes.add(n);
		}
		
		int i = 0;
		int j;
		while(i < nodes.size()){
			E n = nodes.get(i);
			j = i + 1;
			while(j < nodes.size()){
				if(n.equals(nodes.get(j))){
					throw new IllegalStateException("no duplicate nodes");
				}
				j++;
			}
			
			for(E e : nodeMap.get(n)){
				edges.add(e);
			}
			int k = 0;
			int l;
			while(i < edges.size()){
				E e = edges.get(i);
				l = k + 1;
				while(l < edges.size()){
					if(e.equals(edges.get(l))){
						throw new IllegalStateException("no duplicate edges");
					}
					l++;
				}
				k++;
			}
			
			i++;
		}
	}
	
	
	
	//Observers
	
	/**
	 * @return true if graph has no nodes, false if not
	 */
	public boolean isEmpty(){
		return nodeMap.isEmpty();
	}
	
	
	/**
	 *  @requires n must not be null
	 *  @throws IllegalArgumentException if n is null
	 * 	@return true if specified node is in the graph, or false if not.
	 */
	public boolean containsNode(E n){
		if(n == null){
			throw new IllegalArgumentException("the specified node must not be null");
		}
		return nodeMap.containsKey(n);
	}
	
	
	/**
	 *  @requires nP and nC must not be null and nP must be in the graph
	 *  @throws IllegalArgumentException if nP or nC are null or nP is not in the graph
	 *  @return true if specified edge from nP to nC is in the graph, false if not.
	 */
	public boolean containsEdge(E nP, E nC){
		if(nP == null || nC == null){
			throw new IllegalArgumentException("the specified nodes must not be null");
		}else if(!containsNode(nP)){
			throw new IllegalArgumentException("the specified node must be " +
					"part of the graph");
		}
		return nodeMap.get(nP).contains(nC);
	}

	// Operations
	
	/**
	 * @return an unmodifiable set of all nodes in this graph
	 */
	public Set<E> nodeSet(){
		return Collections.unmodifiableSet(nodeMap.keySet());
	}

	/**
	 * @requires n must not be null and be a member of the graph
	 * @throws IllegalArgumentException if n is null or not in the graph
	 * @return an unmodifiable set of all children nodes
	 * of the specified node n
	 */
	public Set<E> edgeSet(E n){
		if(n == null || !containsNode(n)){
			throw new IllegalArgumentException("the specified node must be " +
					"part of the graph");
		}
		return Collections.unmodifiableSet(nodeMap.get(n));
	}
	
	// Modifiers
	
	/**
	 *  adds the specified node to the graph
	 * 	@requires n to be non null && Graph !contains(node n)
	 *  @throws IllegalArgumentException if the specified node is null
	 *  @modifies nodeMap to contain an additional key mapping
	 *  @effects this Graph will contain node n
	 * 	@return true if node successfully added, false if it already exists
	 */
	public boolean addNode(E n){
		if(n == null){
			throw new IllegalArgumentException("the specified node must not be null");
		}
		
		if(containsNode(n)){  // if map already contains node, don't overwrite the key
			return false;
		}
		nodeMap.put(n, new HashSet<E>());
		// checkRep() // guaranteeing the RepInvariant is an extremely expensive 
		// operation.  See checkRep() method for more information.
		return true;
	}

	
	/**
	 *  adds the specified edge to the graph 
	 * 	@requires nP and nC to be in the graph && no existing edge 
	 *  from nP to nC
	 *  @throws IllegalArgumentException if one of the specified 
	 *  nodes does not exist.
	 *  @modifies nodeMap to contain an additional edge mapping
	 *  @effects this Graph will contain an edge from nP to nC
	 * 	@return true if edge was successfully added, false if it already exists 
	 */
	public boolean addEdge(E nP, E nC){
		if(nP == null || nC == null){
			throw new IllegalArgumentException("the specified nodes must not be null");
		}
		if(!containsNode(nP) || !containsNode(nC)){
			throw new IllegalArgumentException("the specified nodes are not " +
					"contained in the graph");
		}
		
		if(containsEdge(nP, nC)){ // if edge already exists, don't do anything
			return false;
		}
		Set<E> s = nodeMap.get(nP);
		s.add(nC);
		nodeMap.put(nP, s);
		// checkRep() // guaranteeing the RepInvariant is an extremely expensive 
		// operation.  See checkRep() method for more information.
		return true;
	}


}
