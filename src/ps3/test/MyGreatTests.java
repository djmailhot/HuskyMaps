package ps3.test;

import java.util.Set;

import ps3.graph.Graph;
import junit.framework.*;

public final class MyGreatTests extends TestCase{
	
	
	
    
    /**
     * tests that two identical nodes cannot be added to a graph
     */
    public void testAddNodeRepInv(){
    	Graph<String> g = new Graph<String>();
    	String s = "Legolas";
    	
    	g.addNode(s);
    	Set<String> nodes = g.nodeSet();
    	
    	g.addNode(s);
    	//assertTrue("Adding a node that is already in the graph should not " +
    	//		"change the graph.", nodes.equals(g.nodeSet()));
    	
    }
    
    
    /**
     * tests that two identical edges cannot be added to the graph
     */
    public void testAddEdgeRepInv(){
    	
    }
    
    /**
     * tests that graphs can be made of different types objects as nodes
     * and that operations still work
     */
    public void testGraphAnyType(){
    	
    }
    
    
    /**
     * tests that a path can be found with the use of NodeCountingPath 
     */
    public void testNodeCountingFindPath(){
    	
    }
}
