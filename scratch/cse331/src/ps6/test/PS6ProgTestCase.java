package ps6.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;
import ps6.Directions;
import ps6.DirectionsFinder;
import ps6.InvalidAddressException;
import ps6.InvalidDatabaseException;
import ps6.NoPathException;

public class PS6ProgTestCase extends TestCase {
    /** Threshold for comparing double values */
    public static final double THRESHOLD = 0.001;

    // Store the DirectionsDFinder in a static field, so that we only
    // make one DirectionsFinder per database no matter how many tests we
    // instantiate.
    private static final Map<TestRecord.TestDB,DirectionsFinder> dfs = new HashMap<TestRecord.TestDB,DirectionsFinder>();
    private static final Set<TestRecord.TestDB> dfsLoadFailed = new HashSet<TestRecord.TestDB>();

    private final TestRecord test;
    private ps2.WalkingRouteFormatter walkingFormatter;
    private ps2.DrivingRouteFormatter drivingFormatter;

    /**
     * @effects creates a new test which runs the given test in the given mode
     **/
    public PS6ProgTestCase(ps6.test.TestRecord test) {
        super(test.getTestName());
        this.test = test;
        this.walkingFormatter = new ps2.WalkingRouteFormatter();
        this.drivingFormatter = new ps2.DrivingRouteFormatter();
    }

    private static void loadDatabase(TestRecord.TestDB db) {
        // might be done already...
        if (dfs.containsKey(db) && dfs.get(db) != null) {
            return;
        }

        // might have failed already ...
        if (dfsLoadFailed.contains(db)) {
            fail("A previous attempt at loading the database has already failed");
        }

        // otherwise, give it a shot...
        try {
            dfs.put(db, DirectionsFinder.getDirectionsFinder(db.dbPath(), null));

        } catch (InvalidDatabaseException e) {
        dfsLoadFailed.add(db);
            fail("Load of tiny database failed with an exception: " + e);
        }
    }

    /**
     * Runs the test against getDirections(Address x 2, RouteDirections)
     **/
    @Override
    public void runTest() {
        loadDatabase(test.getDb());

        String usefulName = "query from '" + test.getStart() + "' to '" + test.getEnd() + "'";
        switch(test.getType()) {
        case DRIVING:
            runProgADTDirectionsDriving(usefulName);
            break;
        case WALKING:
            runProgADTDirectionsWalking(usefulName);
            break;
        case INVALID_ADDRESS:
            runProgADTBadAddress(usefulName);
            break;
        case NO_PATH:
            runProgADTNoPath(usefulName);
            break;
        case INVALID_DIR_TYPE:
            //Ignore for Programmatic Tests
            return;
        default:
            throw new IllegalStateException("Unknown desired result");
        }
    }

    private void runProgADTBadAddress(String usefulName) {
        try {
            @SuppressWarnings("unused")
            Directions result = dfs.get(test.getDb()).getDirections(test.getStart(), test.getEnd(), drivingFormatter);
            fail("Expected InvalidAddressException on " + usefulName);
        } catch (InvalidAddressException e) {
            // this is what we want
            return;
        } catch (NoPathException e) {
            fail("Unexpected exception on " + usefulName + ": " + e.toString());
        }
    }

    private void runProgADTNoPath(String usefulName) {
        try {
            @SuppressWarnings("unused")
            Directions result = dfs.get(test.getDb()).getDirections(test.getStart(), test.getEnd(), drivingFormatter);
            fail("Expected NoPathException on " + usefulName);
        } catch (InvalidAddressException e) {
            fail("Unexpected exception on " + usefulName + ": " + e.toString());
        } catch (NoPathException e) {
            // this is what we want
            return;
        }
    }

    private void runProgADTDirectionsDriving(String usefulName) {
        try {
            Directions result = dfs.get(test.getDb()).getDirections(test.getStart(), test.getEnd(), drivingFormatter);

            assertEquals("The start given by getStart() should match the starting point given in the query (" + usefulName + ")",
                         test.getStart(),
                         result.getStart());

            assertEquals("The end given by getEnd() should match the ending point given in the query (" + usefulName + ")",
                         test.getEnd(),
                         result.getEnd());

            assertEquals("The length given by getLength() should match the expected length for " + usefulName,
                         test.getLength(),
                         result.getLength(),
                         THRESHOLD);

            Iterator<String> queryDirs = Arrays.asList(test.getDirections()).iterator();
            Iterator<String> resultDirs = result.iterator();
            assertEquals("getDirections(Address x 2, RouteDirections) " + usefulName,
                         queryDirs, resultDirs);

        } catch (InvalidAddressException e) {
            fail("Unexpected exception on " + usefulName + ": " + e.toString());
        } catch (NoPathException e) {
            fail("Unexpected exception on " + usefulName + ": " + e.toString());
        }
    }

    private void runProgADTDirectionsWalking(String usefulName) {
        try {
            Directions result = dfs.get(test.getDb()).getDirections(test.getStart(), test.getEnd(), walkingFormatter);

            assertEquals("The start given by getStart() should match the starting point given in the query (" + usefulName + ")",
                         test.getStart(),
                         result.getStart());

            assertEquals("The end given by getEnd() should match the ending point given in the query (" + usefulName + ")",
                         test.getEnd(),
                         result.getEnd());

            assertEquals("The length given by getLength() should match the expected length for " + usefulName,
                         test.getLength(),
                         result.getLength(),
                         THRESHOLD);

            Iterator<String> queryDirs = Arrays.asList(test.getDirections()).iterator();
            Iterator<String> resultDirs = result.iterator();
            assertEquals("getDirections(Address x 2, RouteDirections) " + usefulName,
                         queryDirs, resultDirs);

        } catch (InvalidAddressException e) {
            fail("Unexpected exception on " + usefulName + ": " + e.toString());
        } catch (NoPathException e) {
            fail("Unexpected exception on " + usefulName + ": " + e.toString());
        }
    }

    // (destructively) checks that two iterators return equal elements
    protected static void assertEquals(String message, Iterator<?> expected, Iterator<?> actual) {
        boolean expectedHasNext = expected.hasNext();
        boolean actualHasNext = actual.hasNext();

        // if both iterators are exhausted, we're fine
        if (!expectedHasNext && !actualHasNext) {
            return;
        }

        // if just one is exhausted, we fail
        if (expectedHasNext && !actualHasNext) {
            Object expectedNext = expected.next();
            fail(message + ": expected had '" + expectedNext + "' but actual had no more elements");
        }

        // if just one is exhausted, we fail
        if (!expectedHasNext && actualHasNext) {
            Object actualNext = actual.next();
            fail(message + ": actual had '" + actualNext + "' but expected had no more elements");
        }

        // assert that the current element pairing is equal
        Object expectedNext = expected.next();
        Object actualNext = actual.next();
        assertEquals(message,
                     expectedNext, actualNext);

        // check the rest recursively
        assertEquals(message, expected, actual);
    }
}
