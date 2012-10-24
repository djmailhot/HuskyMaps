/**
 * This is part of CSE 331 Problem Set 0.
 */
package ps0.test;
import ps0.*;

import junit.framework.TestCase;

/**
 * FibonacciTest is a glassbox test of the Fibonacci class.
 *
 * Recall that the Fibonacci sequence is a recursive
 * sequence where the first two terms of the sequence are 1 and all subsequent
 * terms are the sum of the previous two terms.
 *
 * Thus, the Fibonacci sequence starts out as 1, 1, 2, 3, 5, 8, 13...
 * The first 1 in the sequence is considered the "0th" term,
 * so the indices that <code>ps0.Fibonacci</code> uses are 0-based.
 *
 * @see ps0.Fibonacci
 *
 * @author mbolin
 */
public class FibonacciTest extends TestCase {

    private Fibonacci fib = null;

    protected void setUp() throws Exception {
        fib = new Fibonacci();
    }

    /**
     * Tests to see that Fibonacci throws an IllegalArgumentException
     * for a negative number but not for zero or for a positive number.
     */
    public void testThrowsIllegalArgumentException() {
        boolean throwsIllegalArgumentException;

        // test -1
        throwsIllegalArgumentException = false;
        try {
            fib.getFibTerm(-1);
        } catch (IllegalArgumentException ex) {
            throwsIllegalArgumentException = true;
        } catch (Exception ex) {
            fail("Threw exception other than IllegalArgumentException for a negative number: "
                    + ex);
        }
        assertTrue(
                "Did not throw IllegalArgumentException for a negative number.",
                throwsIllegalArgumentException);

        // test 0
        try {
            fib.getFibTerm(0);
        } catch (IllegalArgumentException ex) {
            fail("Threw IllegalArgumentException for 0 but 0 is nonnegative: "
                    + ex);
        }

        // test 1
        try {
            fib.getFibTerm(1);
        } catch (IllegalArgumentException ex) {
            fail("Threw IllegalArgumentException for 1 but 1 is nonnegative: "
                    + ex);
        }
    }

    /** Tests to see that Fibonacci returns the correct value for the base cases, n=0 and n=1 */
    public void testBaseCase() {
        assertEquals("getFibTerm(0) should return 1", 1, fib.getFibTerm(0));
        assertEquals("getFibTerm(1) should return 1", 1, fib.getFibTerm(1));
    }

    /** Tests inductive cases of the Fibonacci sequence */
    public void testInductiveCase() {
        int[][] cases = new int[][] {
                { 2, 2 },
                { 3, 3 },
                { 4, 5 },
                { 5, 8 },
                { 6, 13 },
                { 7, 21 }
            };
        for (int i = 0; i < cases.length; i++) {
            assertEquals("getFibTerm(" + cases[i][0] + ") should return "
                    + cases[i][1], cases[i][1], fib.getFibTerm(cases[i][0]));
        }
    }

}
