package ps6.test;

import ps6.Address;

/**
 * A test record is a fairly simple record type to hold a query and its expected
 * results
 *
 * @author tws
 */
public final class TestRecord {

    //TODO: make sure this points to /cse/courses/cse331/tigerdb when you turn-in the problem set
    private static final String tigerBaseDir = "/cse/courses/cse331/tigerdb";

    /**
     * Database information the various testing databases
     * @author tws
     */
    public static enum TestDB{
        TINY ("tiny"),
        SMALL ("small"),
        MEDIUM ("medium"),
        MYDB ("myDB"),
        LARGE ("large");

        private final String dbPath;
        TestDB(String name) {
        	if(name.equals("myDB")){
        		this.dbPath = "/homes/iws/djmail/workspace/cse331/src/ps6/test/" + name;
        	}else{
        		this.dbPath = tigerBaseDir + "/" + name;
        	}
        }
        public String dbPath() {
            return dbPath;
        }
    }

    /** Types of tests, e.g. a walking test or an invalid address test */
    public static enum TestType
    {WALKING,DRIVING,INVALID_DIR_TYPE,INVALID_ADDRESS,NO_PATH};

    private final String testName;
    private final TestDB db;
    private final TestType type;

    private final Address start;
    private final Address end;

    private final String[] directions;
    private final String tripLength;
    private final double length;
    private final String errorMessage;

    /**
     *
     * @param testName name of the test (for use by JUnit)
     * @param db database information for the test
     * @param type type of the test
     * @param start starting address of the query
     * @param end ending address of the query
     * @param directions sequence of directions lines expected from getDirections, or null if we
     * expect not to find any directions. the elements must not contain newlines
     * at the end.
     * @param tripLength the trip length or trip time that appear at the end of the directions
     * @param length expected length of the path, or NaN if we expect not to find any
     * directions
     * @param errorMessage expected error message or null if no error is expected
     */
    public TestRecord(String testName,TestDB db,TestType type,
            Address start, Address end,
            String[] directions, String tripLength, double length,
            String errorMessage) {
        this.db = db;
        this.testName = testName;
        this.type = type;
        this.start = start;
        this.end = end;
        this.directions = directions;
        this.tripLength = tripLength;
        this.length = length;
        this.errorMessage = errorMessage;
    }

    /**
     * Get a string rep. of the direction type for the test (e.g. "w" or "d")
     * @return a string representation of the direction type for the test
     */
    public String getDirectionType() {
        switch (type) {
        case WALKING:
            return "w";
        case INVALID_DIR_TYPE:
            return "q";
        case DRIVING:
        default:
            return "d";
        }
    }

    /**
     * Get the name of the Test
     * @return the name of the test
     */
    public String getTestName() {
        return testName;
    }

    /**
     * Get the starting test's starting address
     * @return the starting address
     */
    public Address getStart() {
        return start;
    }

    /**
     * Get the test's ending address
     * @return the ending address
     */
    public Address getEnd() {
        return end;
    }

    /**
     * Get the type of test
     * @return the type of test
     */
    public TestType getType() {
        return type;
    }

    /**
     * Get the expected direction lines
     * @return the expected direction lines
     */
    public String[] getDirections() {
        return directions;
    }

    /**
     * Get the expected length / travel time line
     * @return the expected length / travel time line
     */
    public String getTripLength() {
        return tripLength;
    }

    /**
     * Get the expected length of the path
     * @return the expected length of path
     */
    public double getLength() {
        return length;
    }

    /**
     * Get the expected error message
     * @return the expected error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Get the database information for the test
     * @return the database for the test
     */
    public TestDB getDb() {
        return db;
    }

}
