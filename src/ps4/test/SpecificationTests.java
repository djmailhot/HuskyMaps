package ps4.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import ps4.*;

/*
 * SpecificationTests is a test suite containing your black
 * box (specification-based) tests.
 */
public final class SpecificationTests extends TestSuite
{
	
    public static Test suite() { return new SpecificationTests(); }
    public SpecificationTests() { this("Problem Set 4 SpecificationTests"); }
    public SpecificationTests(String s)
    {
        super(s);
        addTest(ScriptFileTests.suite());

        // add JUnit tests here for StreetNumberSet and StreetSegment
        addTestSuite(StreetNumberSetTest.class);
        addTestSuite(StreetSegmentTest.class);

    }
    
    
}
