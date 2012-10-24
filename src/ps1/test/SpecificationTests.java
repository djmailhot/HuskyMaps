package ps1.test;

import junit.framework.*;

/**
 * SpecificationTests is a simple TestSuite that includes and runs all the tests
 * in {@link RatNumTest}, {@link RatPolyTest}, and {@link RatPolyStackTest}.
 */
public final class SpecificationTests extends TestSuite {
    public static Test suite() {
        return new SpecificationTests();
    }

    public SpecificationTests() {
        this("Problem Set 1 Specification Tests");
    }

    public SpecificationTests(String s) {
        super(s);
        addTestSuite(RatNumTest.class);
        addTestSuite(RatTermTest.class);
        addTestSuite(RatPolyTest.class);
        addTestSuite(RatPolyStackTest.class);
    }

}
