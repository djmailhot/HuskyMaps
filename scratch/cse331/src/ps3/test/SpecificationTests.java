package ps3.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/*
 * SpecificationTests is a test suite containing your black
 * box (specification-based) tests.  Only tests which test
 * specifications provided by the course staff (that is, tests
 * which all students' implementations should pass) belong here.
 */
public final class SpecificationTests extends TestSuite
{
    public static Test suite() { return new SpecificationTests(); }
    public SpecificationTests() { this("Problem Set 3 SpecificationTests"); }
    public SpecificationTests(String s)
    {
        super(s);
        addTest(ScriptFileTests.suite());   //<<<<<<<<runs all tests of ScriptFileTests.suite()
        									//<<<<<<<<in effect, runs all *.test files
    }

    ///////////////////////////////////////////////////////////
    // ALL RELEVENT TESTS HANDLED BY ScriptFileTests.suite() //
    ///////////////////////////////////////////////////////////
}
