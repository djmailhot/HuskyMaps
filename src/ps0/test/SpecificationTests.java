package ps0.test;

import junit.framework.*;

/**
 * SpecificationTests is a simple test suite to test your problem set
 * against the provided specifications.  You do not need to modify this file for problem
 * set 0.
 */
public final class SpecificationTests extends TestSuite
{
    public static Test suite() { return new SpecificationTests(); }
    public SpecificationTests() { this("Problem Set 0 SpecificationTests"); }
    public SpecificationTests(String s)
    {
        super(s);
        addTestSuite(FibonacciTest.class);
        addTestSuite(HolaWorldTest.class);
        addTestSuite(RandomHelloTest.class);
        addTestSuite(BallTest.class);
        addTestSuite(BoxTest.class);
        addTestSuite(BallContainerTest.class);

    }
}
