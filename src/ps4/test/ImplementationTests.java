package ps4.test;

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
    public ImplementationTests() { this("Problem Set 4 ImplementationTests"); }
    public ImplementationTests(String s)
    {
        super(s);

        // add your JUnit tests here

        addTestSuite(StreetNumberSetImpTest.class);
        addTestSuite(StreetSegmentImpTest.class);

    }
}
