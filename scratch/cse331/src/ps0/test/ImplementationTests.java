package ps0.test;

import junit.framework.*;


/**
 * ImplementationTest is a simple test suite to test the implementation
 * of each problem set.  You do not need to modify this file for problem
 * set 0.
*/
public final class ImplementationTests extends TestSuite
{
    public static Test suite() { return new ImplementationTests(); }
    public ImplementationTests() { this("Problem Set 0 ImplementationTests"); }
    public ImplementationTests(String s)
    {
        super(s);
    }
}
