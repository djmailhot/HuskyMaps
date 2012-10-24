/**
 * This is part of CSE 331 Problem Set 0.
 */
package ps0.test;
import ps0.*;

import junit.framework.TestCase;

/**
 * HolaWorldTest is a simple test of the HolaWorld class that you
 * are to fix.  This test just makes sure that the program
 * does not crash and that the correct greeting is printed.
 */
public class HolaWorldTest extends TestCase {

    /**
     * Tests that HolaWorld does not crash
     */
    public void testCrash() {
        /* Any exception thrown will be caught by JUnit. */
        HolaWorld.main(new String[0]);
    }

    /**
     * Tests that the HolaWorld greeting is correct.
     */
    public void testGreeting() {
        HolaWorld world = new HolaWorld();
        assertEquals(HolaWorld.spanishGreeting, world.getGreeting());
    }


}
