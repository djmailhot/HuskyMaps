package ps6.test;

import junit.framework.*;

public class ImplementationTests
    extends TestSuite
{
    public ImplementationTests() {
        this("Problem Set 6 Implementation Tests");
    }

    public static Test suite() {
        return new ImplementationTests();
    }

    public ImplementationTests(String s) {
        super(s);
    }
}
