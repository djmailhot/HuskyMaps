/**
 * This is part of the Problem Set 0: Introduction for CSE 331.
 */
package ps0.test;
import ps0.*;
import java.util.Set;
import java.util.HashSet;

import junit.framework.TestCase;

/**
 * BallContainerTest is a glassbox test of the BallContainer class.
 *
 * Recall that the BallContainer is a container for Balls. However, you can only
 * put a Ball into a BallContainer once. After you put the Ball into the BallContainer,
 * further attempts to do so will fail, since the Ball is already in
 * the BallContainer! Similarly, you cannot expect to remove a Ball from a BallContainer
 * if it is not inside the BallContainer.
 *
 * @see ps0.Ball
 * @see ps0.BallContainer
 */
public class BallContainerTest extends TestCase {

    private BallContainer ballcontainer = null;
    private int NUM_BALLS_TO_TEST = 3;
    private Ball[] b = null;
    private double BALL_UNIT_VOLUME = 20.0;
    private double JUNIT_DOUBLE_DELTA = 0.0001;

    protected void setUp() throws Exception {
        assertEquals("Test case error, you must test at least 1 Ball!!", true, NUM_BALLS_TO_TEST > 0);
        ballcontainer = new BallContainer();

        // Let's create the balls we need.
        b = new Ball[NUM_BALLS_TO_TEST];
        for (int i=0; i<NUM_BALLS_TO_TEST; i++) {
            b[i] = new Ball((i+1)*BALL_UNIT_VOLUME);
        }
    }


    /** Test to check that BallContainer.add(Ball) is implemented correctly */
    public void testAdd() {
        double containerVolume;
        ballcontainer.clear();
        for (int i=0; i<NUM_BALLS_TO_TEST; i++) {
            assertEquals("BallContainer.add(Ball) failed to add a new Ball!", true, ballcontainer.add(b[i]));
            containerVolume = ballcontainer.getVolume();
            assertEquals("BallContainer.add(Ball) seems to allow the same Ball to be added twice!", false, ballcontainer.add(b[i]));
            assertEquals("BallContainer's volume has changed, but its contents have not!",containerVolume,ballcontainer.getVolume());
            assertEquals("BallContainer does not contain a ball after it is supposed to have been added!", true, ballcontainer.contains(b[i]));
        }
    }

    /** Test to check that BallContainer.remove(Ball) is implemented correctly */
    public void testRemove() {
        ballcontainer.clear();
        double containerVolume;
        assertEquals("BallContainer.remove(Ball) should fail because ballcontainer is empty, but it didn't!", false, ballcontainer.remove(b[0]));
        for (int i=0; i<NUM_BALLS_TO_TEST; i++) {
            ballcontainer.clear();
            for (int j=0; j<i; j++) {
                ballcontainer.add(b[j]);
            }
            for (int j=0; j<i; j++) {
                assertEquals("BallContainer.remove(Ball) failed to remove a Ball that is supposed to be inside", true, ballcontainer.remove(b[j]));
                containerVolume = ballcontainer.getVolume();
                assertEquals("BallContainer still contains a ball after it is supposed to have been removed!", false, ballcontainer.contains(b[j]));
                assertEquals("BallContainer's volume has changed, but its contents have not!",containerVolume,ballcontainer.getVolume());
            }
            for (int j=i; j<NUM_BALLS_TO_TEST; j++) {
                assertEquals("BallContainer.remove(Ball) did not fail for a Ball that is not inside", false, ballcontainer.remove(b[j]));
            }
        }
    }

    /**
     * Test to check that BallContainer.iterator() is implemented
     * correctly.
     */
    public void testIterator() {
        Set<Ball> allBalls = new HashSet<Ball>();
        Set<Ball> seenBalls = new HashSet<Ball>();
        ballcontainer.clear();
        assertEquals("BallContainer is not empty after being cleared!", 0, ballcontainer.size());
        for (Ball aBall: b) {
            ballcontainer.add(aBall);
            allBalls.add(aBall);
        }
        int i=0;
        for (Ball aBall: ballcontainer) {
            assertTrue("Iterator returned a ball which isn't in the container",
                       allBalls.contains(aBall));
            assertFalse("Iterator returned the same ball twice",
                        seenBalls.contains(aBall));
            seenBalls.add(aBall);
            i++;
        }
        assertEquals("BallContainer iterator did not return enough items!", i, b.length);
    }

    /**
     * Test that BallContainer.clear() is implemented correctly.
     */
    public void testClear() {
        ballcontainer.clear();
        assertEquals("BallContainer is not empty after being cleared!", 0, ballcontainer.size());
        ballcontainer.add(b[0]);
        ballcontainer.clear();
        assertEquals("BallContainer is not empty after being cleared!", 0, ballcontainer.size());
    }

    /** Test that we can put a Ball into a BallContainer */
    public void testVolume() {
        ballcontainer.clear();
        assertEquals("Volume of empty BallContainer is not zero!", 0, ballcontainer.getVolume(), JUNIT_DOUBLE_DELTA);
        for (int i=0; i<NUM_BALLS_TO_TEST; i++) {
            ballcontainer.add(b[i]);
            assertEquals("Volume of BallContainer with "+(i+1)+" ball(s) is supposed to be "+((i+1)*(i+2)*BALL_UNIT_VOLUME/2)+" but it's "
                         +ballcontainer.getVolume()+" instead", (i+1)*(i+2)*BALL_UNIT_VOLUME/2, ballcontainer.getVolume(), JUNIT_DOUBLE_DELTA);
        }

    }

    /** Test that size() returns the correct number. */
    public void testSize() {
        ballcontainer.clear();
        assertEquals("size() of empty BallContainer is not zero!", 0, ballcontainer.size());
        for (int i=0; i<NUM_BALLS_TO_TEST; i++) {
            ballcontainer.add(b[i]);
            assertEquals("size() of BallContainer with "+(i+1)+" ball(s) is supposed to be "+(i+1)+" but it's "
                         +ballcontainer.size()+" instead", i+1, ballcontainer.size());
        }
    }

    /** Test that size() returns the correct number. */
    public void testContains() {
        ballcontainer.clear();
        for (int i=0; i<NUM_BALLS_TO_TEST; i++) {
            assertEquals("Empty BallContainer seems to contain a ball!", false, ballcontainer.contains(b[i]));
        }
        for (int i=0; i<NUM_BALLS_TO_TEST; i++) {
            ballcontainer.add(b[i]);
            assertEquals("BallContainer does not contain a Ball that is supposed to be inside!", true, ballcontainer.contains(b[i]));
            for (int j=i+1; j<NUM_BALLS_TO_TEST; j++) {
                assertEquals("BallContainer seems to contain a Ball that is not inside!", false, ballcontainer.contains(b[j]));
            }
        }
    }

    /** Test that clear removes all balls. **/
    public void testVolumeAfterClear() {
        ballcontainer.add(b[0]);
        ballcontainer.clear();
        assertEquals("The volume of BallContainer after being cleared is not reset to 0!",
                     0,ballcontainer.getVolume(),JUNIT_DOUBLE_DELTA);
    }

}
