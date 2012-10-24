package ps6;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ps2.GeoPoint;
import ps4.StreetSegment;
import ps3.graph.Graph;

/**
 * A GraphGenerator is a factory class to produce a Graph of StreetSegments of a geographic area.
 * Geographic information is obtained from a valid Tiger Database Directory, a directory of geographical 
 * street and address information.
 * 
 */
public class GraphGenerator {

	private static boolean DEBUG = false;
	
	/**
	 * Static factory method to produce a graph representing the geographic area 
	 * data accessible by the specified database reader.
	 * @requires segments != null
     * @param Collection segments of StreetSegments containing all segments contained in this graph 
	 * @return a Graph of StreetSegments representing the geographic area.
	 */
	public static Graph<StreetSegment> produceGraph(Collection<StreetSegment> segments){
    	
		// our graph
    	Graph<StreetSegment> geoGraph = new Graph<StreetSegment>();
    	
    	// storage maps
        Map<GeoPoint, Set<StreetSegment> > startPoints = new HashMap<GeoPoint, Set<StreetSegment> >();
        Map<GeoPoint, Set<StreetSegment> > endPoints = new HashMap<GeoPoint, Set<StreetSegment> >();

        long startNodes = System.currentTimeMillis();
        // <<<< add segments to graph and storage maps >>>>
        // for all segments
        for(StreetSegment gs : segments){
        	
    		// add segment to graph
    		geoGraph.addNode(gs);
    		// add segment to storage maps
        	createMapping(startPoints, gs, gs.getP1());
        	createMapping(endPoints, gs, gs.getP2());
        	
        	
        	// add reverse of segment to graph
        	gs = gs.reverse();
        	geoGraph.addNode(gs);
        	// add to storage maps
        	createMapping(startPoints, gs, gs.getP1());
        	createMapping(endPoints, gs, gs.getP2());
        }
        long endNodes = System.currentTimeMillis();
        if(DEBUG) System.out.println("Time for nodes: " + (endNodes-startNodes)/1000.0);
        
        startNodes = System.currentTimeMillis();
        // <<<< connect edges >>>>
        // iterate over end points to add edges to start points
		for(GeoPoint gp : endPoints.keySet()){
		   
			// for all possible children segments of this end point
			Set<StreetSegment> children = startPoints.get(gp);
				
			// for each parent segment with this end point
			for(StreetSegment sP : endPoints.get(gp)){
				
				if(children != null){     // if there exists children
					// get all child segments that start at this parent segment's end point
					for(StreetSegment sC : children){
						// add the edge
						geoGraph.addEdge(sP, sC);
					}
				}
				
			}
        }
		endNodes = System.currentTimeMillis();
        if(DEBUG) System.out.println("Time for edges: " + (endNodes-startNodes)/1000.0);
		return geoGraph;
	}
	

    
    
    /** helper to populate storage maps of points indexed to sets of segments */
    private static void createMapping(Map<GeoPoint, Set<StreetSegment> > m,
    			StreetSegment gs, GeoPoint p){
    	Set<StreetSegment> t = !m.containsKey(p) ? new HashSet<StreetSegment>() : m.get(p);
    	t.add(gs);
    	m.put(p, t);
    }
	
}
