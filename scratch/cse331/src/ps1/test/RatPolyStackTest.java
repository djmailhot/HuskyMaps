package ps1.test;

import ps1.*;

import junit.framework.*;

/**
 * This class contains a set of test cases that can be used to test the
 * implementation of the RatPolyStack class.
 * <p>
 */
public final class RatPolyStackTest extends TestCase {
    // create a new poly that is a constant (doesn't depend on x)
    private RatPoly constantPoly(int constant) {
        return new RatPoly(constant, 0);
    }

    // create a new poly that is a constant (doesn't depend on x)
    // taking a char allows us to represent stacks as strings
    private RatPoly constantPoly(char constant) {
        return constantPoly(Integer.valueOf("" + constant).intValue());
    }

    // * @return a new RatPolyStack instance
    private RatPolyStack stack() {
        return new RatPolyStack();
    }

    // create stack of single-digit constant polys
    private RatPolyStack stack(String desc) {
        RatPolyStack s = new RatPolyStack();

        // go backwards to leave first element in desc on _top_ of stack
        for (int i = desc.length() - 1; i >= 0; i--) {
            char c = desc.charAt(i);
            s.push(constantPoly(c));
        }
        return s;
    }

    // RatPoly equality check
    // (getting around non-definition of RatPoly.equals)
    private boolean eqv(RatPoly p1, RatPoly p2) {
        return p1.toString().equals(p2.toString());
    }

    // compares 's' to a string describing its values
    // thus stack123 = "123". desc MUST be a sequence of
    // decimal number chars
    //
    // NOTE: THIS CAN FAIL WITH A WORKING STACK IF RatPoly.toString IS BROKEN!
    private void assertStackIs(RatPolyStack s, String desc) {
        assertTrue(s.size() == desc.length());

        for (int i = 0; i < desc.length(); i++) {
            RatPoly p = s.getNthFromTop(i);
            char c = desc.charAt(i);
            String asstr = "Elem(" + i + "): " + p.toString() + ", Expected "
                    + c + ", (Expected Stack:" + desc + ")";

            assertTrue(asstr, eqv(p, constantPoly(c)));
        }
    }

    public RatPolyStackTest(String name) {
        super(name);
    }

    public void testCtor() {
        RatPolyStack stk1 = stack();
        assertTrue(stk1.size() == 0);
    }

    public void testPush() {
        RatPolyStack stk1 = stack();
        stk1.push(constantPoly(0));

        assertStackIs(stk1, "0");

        stk1.push(constantPoly(0));
        assertStackIs(stk1, "00");

        stk1.push(constantPoly(1));
        assertStackIs(stk1, "100");

        stk1 = stack("3");
        assertStackIs(stk1, "3");

        stk1 = stack("23");
        assertStackIs(stk1, "23");

        stk1 = stack("123");
        assertStackIs(stk1, "123");
    }

    public void testPushCheckForSharingTwixtStacks() {
        RatPolyStack stk1 = stack();
        RatPolyStack stk2 = stack("123");
        assertStackIs(stk1, "");
        assertStackIs(stk2, "123");

        stk1.push(constantPoly(0));
        assertStackIs(stk1, "0");
        assertStackIs(stk2, "123");

        stk1.push(constantPoly(0));
        assertStackIs(stk1, "00");
        assertStackIs(stk2, "123");

        stk1.push(constantPoly(1));
        assertStackIs(stk1, "100");
        assertStackIs(stk2, "123");

        stk2.push(constantPoly(8));
        assertStackIs(stk1, "100");
        assertStackIs(stk2, "8123");
    }

    public void testPop() {
        RatPolyStack stk1 = stack("123");

        RatPoly poly = stk1.pop();
        assertTrue(eqv(poly, constantPoly(1)));
        assertStackIs(stk1, "23");

        poly = stk1.pop();
        assertTrue(eqv(poly, constantPoly(2)));
        assertStackIs(stk1, "3");

        poly = stk1.pop();
        assertStackIs(stk1, "");
    }

    public void testDup() {
        RatPolyStack stk1 = stack("3");
        stk1.dup();
        assertStackIs(stk1, "33");

        stk1 = stack("23");
        stk1.dup();
        assertStackIs(stk1, "223");
        assertTrue(stk1.size() == 3);
        assertTrue(eqv(stk1.getNthFromTop(0), constantPoly(2)));
        assertTrue(eqv(stk1.getNthFromTop(1), constantPoly(2)));
        assertTrue(eqv(stk1.getNthFromTop(2), constantPoly(3)));

        stk1 = stack("123");
        stk1.dup();
        assertStackIs(stk1, "1123");

    }

    public void testSwap() {
        RatPolyStack stk1 = stack("23");
        stk1.swap();
        assertStackIs(stk1, "32");

        stk1 = stack("123");
        stk1.swap();
        assertStackIs(stk1, "213");

        stk1 = stack("112");
        stk1.swap();
        assertStackIs(stk1, "112");
    }

    public void testClear() {
        RatPolyStack stk1 = stack("123");
        stk1.clear();
        assertStackIs(stk1, "");
        RatPolyStack stk2 = stack("112");
        stk2.clear();
        assertStackIs(stk2, "");
    }

    public void testAdd() {
        RatPolyStack stk1 = stack("123");
        stk1.add();
        assertStackIs(stk1, "33");
        stk1.add();
        assertStackIs(stk1, "6");

        stk1 = stack("112");
        stk1.add();
        assertStackIs(stk1, "22");
        stk1.add();
        assertStackIs(stk1, "4");
        stk1.push(constantPoly(5));
        assertStackIs(stk1, "54");
        stk1.add();
        assertStackIs(stk1, "9");

    }

    public void testSub() {
        RatPolyStack stk1 = stack("123");
        stk1.sub();
        assertStackIs(stk1, "13");
        stk1.sub();
        assertStackIs(stk1, "2");

        stk1 = stack("5723");
        stk1.sub();
        assertStackIs(stk1, "223");
        stk1.sub();
        assertStackIs(stk1, "03");
        stk1.sub();
        assertStackIs(stk1, "3");
    }

    public void testMul() {
        RatPolyStack stk1 = stack("123");
        stk1.mul();
        assertStackIs(stk1, "23");
        stk1.mul();
        assertStackIs(stk1, "6");

        stk1 = stack("112");
        stk1.mul();
        assertStackIs(stk1, "12");
        stk1.mul();
        assertStackIs(stk1, "2");
        stk1.push(constantPoly(4));
        assertStackIs(stk1, "42");
        stk1.mul();
        assertStackIs(stk1, "8");
    }

    public void testDiv() {
        RatPolyStack stk1 = stack("123");
        stk1.div();
        assertStackIs(stk1, "23");
    }

    public void testDifferentiate() {
        RatPolyStack stk1 = stack("123");
        stk1.differentiate();
        stk1.differentiate();
        stk1.differentiate();
        stk1.differentiate();
        assertTrue("Test if stack size changes", stk1.size() == 3);
        assertStackIs(stk1, "023");

        RatPoly rp1 = new RatPoly(3, 5);
        RatPoly rp2 = new RatPoly(7, 0);
        RatPoly rp3 = new RatPoly(4, 1);
        stk1.push(rp1);
        stk1.push(rp2);
        stk1.push(rp3);

        stk1.differentiate();
        assertTrue("Test simple differentiate1", stk1.pop().toString().equals(
                "4"));
        stk1.differentiate();
        assertTrue("Test simple differentiate2", stk1.pop().toString().equals(
                "0"));
        stk1.differentiate();
        assertTrue("Test simple differentiate3", stk1.pop().toString().equals(
                "15*x^4"));
    }

    public void testIntegrate() {
        RatPolyStack stk1 = stack("123");
        stk1.integrate();
        stk1.integrate();
        stk1.integrate();
        stk1.integrate();
        assertTrue("Test if stack size changes", stk1.size() == 3);
        assertTrue("Test simple integrate1", stk1.pop().toString().equals(
                "1/24*x^4"));
        RatPoly rp1 = new RatPoly(15, 4);
        RatPoly rp2 = new RatPoly(7, 0);
        RatPoly rp3 = new RatPoly(4, 0);
        stk1.push(rp1);
        stk1.push(rp2);
        stk1.push(rp3);

        stk1.integrate();
        assertTrue("Test simple integrate1", stk1.pop().toString()
                .equals("4*x"));
        stk1.integrate();
        assertTrue("Test simple integrate2", stk1.pop().toString()
                .equals("7*x"));
        stk1.integrate();
        assertTrue("Test simple integrate3", stk1.pop().toString().equals(
                "3*x^5"));
    }

    // Tell JUnit what order to run the tests in
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new RatPolyStackTest("testCtor"));
        suite.addTest(new RatPolyStackTest("testPush"));
        suite
                .addTest(new RatPolyStackTest(
                        "testPushCheckForSharingTwixtStacks"));
        suite.addTest(new RatPolyStackTest("testPop"));
        suite.addTest(new RatPolyStackTest("testDup"));
        suite.addTest(new RatPolyStackTest("testSwap"));
        suite.addTest(new RatPolyStackTest("testClear"));
        suite.addTest(new RatPolyStackTest("testAdd"));
        suite.addTest(new RatPolyStackTest("testSub"));
        suite.addTest(new RatPolyStackTest("testMul"));
        suite.addTest(new RatPolyStackTest("testDiv"));
        suite.addTest(new RatPolyStackTest("testDifferentiate"));
        suite.addTest(new RatPolyStackTest("testIntegrate"));
        return suite;
    }
}
