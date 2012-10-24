/**
 * This is part of CSE 331 Problem Set 0.
 */
package ps0.test;
import ps0.*;

import junit.framework.TestCase;

/**
 * BallTest is a simple glassbox test of the Ball class.
 *
 * @see ps0.Ball
 */
public class BallTest extends TestCase {

    private Ball b = null;
    private double BALL_VOLUME = 20.0;
    private double JUNIT_DOUBLE_DELTA = 0.0001;

    protected void setUp() throws Exception {
        b = new Ball(BALL_VOLUME);
    }

    /** Test to see that Ball returns the correct volume when queried. */
    public void testVolume() {
        assertEquals("b.getVolume should "+BALL_VOLUME+" but it's "+b.getVolume()+" instead!",
                BALL_VOLUME, b.getVolume(), JUNIT_DOUBLE_DELTA);
    }

}
