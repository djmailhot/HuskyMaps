package ps1.test;

import ps1.*;

import junit.framework.*;

/**
 * This class contains a set of test cases that can be used to test the
 * implementation of the RatPoly class.
 * <p>
 */
public final class RatTermTest extends TestCase {

    // Get a RatNum for in an integer
    private static RatNum num(int i) {
        return new RatNum(i);
    }

    private static RatNum num(int i, int j) {
        return new RatNum(i, j);
    }

    private static final RatNum nanNum = (num(1)).div(num(0));

    private static final RatTerm nanTerm = new RatTerm(nanNum, 3);

    // Convenient way to make a RatTerm equals to coeff*x^expt.
    private static RatTerm term(int coeff, int expt) {
        return new RatTerm(num(coeff), expt);
    }

    // Convenient way to make a RatTerm equals to num/denom*x^expt.
    private static RatTerm term(int numer, int denom, int expt) {
        return new RatTerm(num(numer, denom), expt);
    }

    public RatTermTest(String name) {
        super(name);
    }

    public void testCtor() {
        term(1, 0);
        term(2, 3);
        term(4, 3, 6);
        term(-2, 7, 3);
    }

    public void testCtorZeroCoeff() {
        term(0, 0);
        term(0, 1);
    }

    public void testCtorNaN() {
        term(3, 0, 0);
    }

    public void testGetCoeff() {
        // Simple cases
        assertTrue(term(3, 1).getCoeff().equals(num(3)));
        assertTrue(term(2, 5, 2).getCoeff().equals(num(2, 5)));

        // Check zero
        assertTrue(term(0, 0).getCoeff().equals(num(0)));

        // Check negative coeff
        assertTrue(term(-2, 3, 2).getCoeff().equals(num(-2, 3)));

        // Check NaN
        assertTrue(term(3, 0, 4).getCoeff().equals(nanNum));
    }

    public void testGetExpt() {
        // Simple
        assertTrue(term(2, 4).getExpt() == 4);

        // Zero always have zero expt
        assertTrue(term(0, 0).getExpt() == 0);
        assertTrue(term(0, 1).getExpt() == 0);

    }

    public void testIsNaN() {
        assertTrue(term(5, 0, 0).isNaN());

        // Check that 0/0*x^4 isNaN instead of zero
        assertTrue(term(0, 0, 4).isNaN());

        // Check for false positives
        assertFalse(term(2, 3, 2).isNaN());
    }

    public void testIsZero() {
        assertTrue(term(0, 0).isZero());
        assertTrue(term(0, 1).isZero());
        assertTrue(term(0, 4, 3).isZero());
        assertTrue(term(0, -2, 2).isZero());

        // Check for false positives
        assertFalse(term(1, 3, 0).isZero());

        // Check that 0/0*x^4 is not zero
        assertFalse(term(0, 0, 4).isZero());
    }

    // The forth argument to assertEquals states to what percision the
    // values must to the same. Given the idiosyncrasies of Java's
    // floating point math, it is often impossible to test for
    // identical results. (These cases are all simple enough that
    // there should be no issues, however.)
    public void testEval() {
        assertEquals(0.0, term(0, 0).eval(5.0), 0.0000001);
        assertEquals(0.0, term(0, 5).eval(1.2), 0.0000001);
        assertEquals(2.0, term(2, 0).eval(3.1), 0.0000001);
        assertEquals(1.0, term(1, 0).eval(100.0), 0.0000001);
        assertEquals(35.0, term(5, 1).eval(7.0), 0.0000001);
        assertEquals(12.0, term(3, 2).eval(2.0), 0.0000001);
        assertEquals(-16.0, term(-2, 3).eval(2.0), 0.0000001);
        assertEquals(-3.0, term(3, 3).eval(-1.0), 0.0000001);
        assertEquals(1.0, term(-1, 1).eval(-1.0), 0.0000001);
        assertEquals(2.0, term(1, 2, 2).eval(2.0), 0.0000001);
        assertEquals(.125, term(1, 2, 2).eval(.5), 0.0000001);
        // For some reason it must be written like this. Some thing
        // seem broken with the way Double.NaN's are compared.
        assertTrue((new Double(Double.NaN)).equals(new Double(term(3, 0, 2)
                .eval(1.0))));
    }

    public void testEquals() {
        assertEquals(term(3, 5), term(3, 5));
        assertEquals(term(1, 2, 4), term(1, 2, 4));
        assertEquals(term(-2, 4, 2), term(1, -2, 2));
        assertFalse(term(4, 6).equals(term(7, 8)));
    }

    public void testEqualsZeroCoeff() {
        assertEquals(term(0, 0), term(0, 0));
        assertEquals(term(0, 1), term(0, 0));
        assertFalse(term(0, 0).equals(term(3, 5)));
    }

    public void testEqualsNaNCoeff() {
        assertEquals(nanTerm, term(19, 0, 0));
        assertEquals(nanTerm, term(0, 0, 0));
        assertFalse(nanTerm.equals(term(3, 5)));
        assertFalse(term(0, 3).equals(nanTerm));
    }

    // All tests below depend on constructor and equals.

    // ValueOf tests

    private void testValueOf(String actual, RatTerm target) {
        assertEquals(target, RatTerm.valueOf(actual));
    }

    public void testValueOfSimple() {
        testValueOf("x", term(1, 1));
        testValueOf("-x", term(-1, 1));
    }

    public void testValueOfConst() {
        testValueOf("2", term(2, 0));
        testValueOf("3/4", term(3, 4, 0));
        testValueOf("-4", term(-4, 0));
        testValueOf("-7/5", term(-7, 5, 0));
    }

    public void testValueOfLeadingCoeff() {
        testValueOf("2*x", term(2, 1));
        testValueOf("3/7*x", term(3, 7, 1));
        testValueOf("-4/3*x", term(-4, 3, 1));
    }

    public void testValueOfPow() {
        testValueOf("x^3", term(1, 3));
        testValueOf("-x^4", term(-1, 4));
    }

    public void testValueOfFull() {
        testValueOf("4*x^2", term(4, 2));
        testValueOf("2/5*x^6", term(2, 5, 6));
        testValueOf("-3/2*x^2", term(-3, 2, 2));
    }

    public void testValueOfNaN() {
        testValueOf("NaN", term(1, 0, 0));
    }

    public void testValueOfZero() {
        testValueOf("0", term(0, 0));
    }

    // toString tests

    private void testToString(String target, RatTerm actual) {
        assertEquals(target, actual.toString());
    }

    public void testToStringSimple() {
        testToString("x", term(1, 1));
        testToString("-x", term(-1, 1));
    }

    public void testToStringConst() {
        testToString("2", term(2, 0));
        testToString("3/4", term(3, 4, 0));
        testToString("-4", term(-4, 0));
        testToString("-7/5", term(-7, 5, 0));
    }

    public void testToStringLeadingCoeff() {
        testToString("2*x", term(2, 1));
        testToString("3/7*x", term(3, 7, 1));
        testToString("-4/3*x", term(-4, 3, 1));
    }

    public void testToStringPow() {
        testToString("x^3", term(1, 3));
        testToString("-x^4", term(-1, 4));
    }

    public void testToStringFull() {
        testToString("4*x^2", term(4, 2));
        testToString("2/5*x^6", term(2, 5, 6));
        testToString("-3/2*x^2", term(-3, 2, 2));
    }

    public void testToStringNaN() {
        testToString("NaN", term(1, 0, 0));
    }

    public void testToStringZero() {
        testToString("0", term(0, 0));
    }

    // Operation tests
    // Tests involving NaN and zero are given separately at end
    public void testAdd() {
        assertEquals(term(3, 0), term(1, 0).add(term(2, 0)));
        assertEquals(term(4, 2), term(3, 2).add(term(1, 2)));
        assertEquals(term(1, 2, 3), term(1, 6, 3).add(term(1, 3, 3)));
        assertEquals(term(1, 8, 1), term(1, 4, 1).add(term(-1, 8, 1)));
        assertEquals(term(-1, 8, 1), term(-1, 4, 1).add(term(1, 8, 1)));
    }

    public void testSub() {
        assertEquals(term(1, 0), term(2, 0).sub(term(1, 0)));
        assertEquals(term(-1, 0), term(1, 0).sub(term(2, 0)));
        assertEquals(term(2, 2), term(3, 2).sub(term(1, 2)));
        assertEquals(term(-1, 6, 3), term(1, 6, 3).sub(term(1, 3, 3)));
        assertEquals(term(3, 8, 1), term(1, 4, 1).sub(term(-1, 8, 1)));
        assertEquals(term(-3, 8, 1), term(-1, 4, 1).sub(term(1, 8, 1)));
    }

    public void testMul() {
        assertEquals(term(2, 0), term(1, 0).mul(term(2, 0)));
        assertEquals(term(3, 4), term(3, 2).mul(term(1, 2)));
        assertEquals(term(1, 18, 6), term(1, 6, 3).mul(term(1, 3, 3)));
        assertEquals(term(-1, 32, 2), term(1, 4, 1).mul(term(-1, 8, 1)));
        assertEquals(term(2, 1), term(2, 1).mul(term(1, 0)));
    }

    public void testDiv() {
        assertEquals(term(1, 2, 0), term(1, 0).div(term(2, 0)));
        assertEquals(term(3, 0), term(3, 2).div(term(1, 2)));
        assertEquals(term(1, 2, 0), term(1, 6, 3).div(term(1, 3, 3)));
        assertEquals(term(-2, 0), term(1, 4, 1).div(term(-1, 8, 1)));
        assertEquals(term(2, 1), term(2, 1).div(term(1, 0)));
        assertEquals(term(8, 3), term(-16, 5).div(term(-2, 2)));
    }

    public void testOperationsOnNaN() {
        assertEquals(nanTerm, nanTerm.add(term(3, 4)));
        assertEquals(nanTerm, term(3, 4).add(nanTerm));
        assertEquals(nanTerm, nanTerm.sub(term(3, 4)));
        assertEquals(nanTerm, term(3, 4).sub(nanTerm));
        assertEquals(nanTerm, nanTerm.mul(term(3, 4)));
        assertEquals(nanTerm, term(3, 4).mul(nanTerm));
        assertEquals(nanTerm, nanTerm.div(term(3, 4)));
        assertEquals(nanTerm, term(3, 4).div(nanTerm));
    }

    public void testOperationsOnZero() {
        RatTerm t = term(-2, 3);
        RatTerm zero = term(0, 0);
        assertEquals(t, zero.add(t));
        assertEquals(t, t.add(zero));
        assertEquals(term(2, 3), zero.sub(t));
        assertEquals(t, t.sub(zero));
        assertEquals(zero, zero.mul(t));
        assertEquals(zero, t.mul(zero));
        assertEquals(zero, zero.div(t));
        assertEquals(nanTerm, t.div(zero));
        assertEquals(0, t.sub(t).getExpt());
    }

    // add and sub should disallow non-zero arguments that differ in
    // exponent.
    public void testDifferentExptArgs() {
        assertTrue(addDifferentExpts(term(1, 2), term(1, 4)));
        assertTrue(addDifferentExpts(term(3, 2, 0), term(7, 3, 1)));
        assertTrue(subDifferentExpts(term(1, 2), term(1, 4)));
        assertTrue(subDifferentExpts(term(3, 2, 0), term(7, 3, 1)));
    }

    public boolean addDifferentExpts(RatTerm arg1, RatTerm arg2) {
        try {
            arg1.add(arg2);
            // Should not have reached this line:
            // cannot allow adding non-zero RatTerms of different exponents
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    public boolean subDifferentExpts(RatTerm arg1, RatTerm arg2) {
        try {
            arg1.sub(arg2);
            // Should not have reached this line:
            // cannot allow adding non-zero RatTerms of different exponents
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    public void testDifferentiate() {
        assertEquals(term(1, 0), term(1, 1).differentiate());
        assertEquals(term(0, 0), term(99, 0).differentiate());
        assertEquals(term(5, 0), term(5, 1).differentiate());
        assertEquals(term(14, 1), term(7, 2).differentiate());
        assertEquals(term(-2, 3), term(-1, 2, 4).differentiate());
        assertEquals(nanTerm, nanTerm.differentiate());
        assertEquals(nanTerm, (new RatTerm(RatNum.NaN, 0)).differentiate());
    }

    public void testAntiDifferentiate() {
        assertEquals(term(1, 1), term(1, 0).antiDifferentiate());
        assertEquals(term(1, 2), term(2, 1).antiDifferentiate());
        assertEquals(term(4, 3, 3), term(4, 2).antiDifferentiate());
        assertEquals(nanTerm, nanTerm.antiDifferentiate());
    }

}
