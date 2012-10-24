package ps6.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class SpecificationTests
    extends TestSuite
{
    public SpecificationTests() {
        this("Problem Set 6 Public Specification Tests");
    }

    public static Test suite() {
        return new SpecificationTests();
    }

    public SpecificationTests(String s) {
        super(s);
        addTest(new PublicProgADTTest());
        addTest(new PublicTextUITest());
    }
}
