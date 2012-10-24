package ps3.test;

import junit.framework.*;

/**
 * ImplementationTest is a test suite used to encapsulate all
 * tests specific to your implementation of this problem set.
 *
 * For instance, unit tests for your individual methods would
 * go here.
 */
public final class ImplementationTests extends TestSuite
{
		
	
    public static Test suite() { return new ImplementationTests(); }
    public ImplementationTests() { this("Problem Set 3 ImplementationTests"); }
    public ImplementationTests(String s)
    {
        super(s);

        addTestSuite(MyGreatTests.class);
    }
    
}
