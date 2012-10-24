package ps6.test;

import ps6.Address;
import ps6.test.TestRecord.TestDB;

/**
 * ValidateQueries contains static fields which hold candidate test queries
 */
public final class ValidateQueries {

	
	/////////////////////// TEST SAME STREET /////////////////////
	
    // test directions when start and end are on the same segment
	//SE Pioneer Way	(48.288302,-122.648447)	(48.288728,-122.646972)	0.1	98277	98277	750-878	751-879
    public static final TestRecord testSameSegment1 = new TestRecord(
            "testSameSegment1", TestDB.TINY,
            TestRecord.TestType.DRIVING,
            new Address(800, "SE Pioneer Way", "98277"),
            new Address(841,"SE Pioneer Way", "98277"),
            new String[] {
                    "Turn left onto SE Pioneer Way and go 0.1 miles.",
                    "841 SE Pioneer Way 98277 is on your right" },
                    "Trip length: 0.1 miles",
                    0.07472776589129437, null);
    
	//SE Pioneer Way	(48.288302,-122.648447)	(48.288728,-122.646972)	0.1	98277	98277	750-878	751-879
    public static final TestRecord testSameSegment2 = new TestRecord(
            "testSameSegment2", TestDB.TINY,
            TestRecord.TestType.DRIVING,
            new Address(841, "SE Pioneer Way", "98277"),
            new Address(800,"SE Pioneer Way", "98277"),
            new String[] {
                    "Turn left onto SE Pioneer Way and go 0.1 miles.",
                    "800 SE Pioneer Way 98277 is on your right" },
                    "Trip length: 0.1 miles",
                    0.07472776589129437, null);
    
	//SE Pioneer Way	(48.288302,-122.648447)	(48.288728,-122.646972)	0.1	98277	98277	750-878	751-879
    public static final TestRecord testSameSegment3 = new TestRecord(
            "testSameSegment3", TestDB.TINY,
            TestRecord.TestType.DRIVING,
            new Address(800, "SE Pioneer Way", "98277"),
            new Address(761,"SE Pioneer Way", "98277"),
            new String[] {
                    "Turn right onto SE Pioneer Way and go 0.1 miles.",
                    "761 SE Pioneer Way 98277 is on your left" },
                    "Trip length: 0.1 miles",
                    0.07472776589129437, null);
    
	//SE Pioneer Way	(48.288302,-122.648447)	(48.288728,-122.646972)	0.1	98277	98277	750-878	751-879
    public static final TestRecord testSameSegment4 = new TestRecord(
            "testSameSegment4", TestDB.TINY,
            TestRecord.TestType.DRIVING,
            new Address(761, "SE Pioneer Way", "98277"),
            new Address(800,"SE Pioneer Way", "98277"),
            new String[] {
                    "Turn right onto SE Pioneer Way and go 0.1 miles.",
                    "800 SE Pioneer Way 98277 is on your left" },
                    "Trip length: 0.1 miles",
                    0.07472776589129437, null);

    // test directions when start and end are on the same street
    public static final TestRecord testSameStreetWalking = new TestRecord(
            "testSameStreetWalking", TestDB.TINY,
            TestRecord.TestType.WALKING,
            new Address(630, "SE Pioneer Way", "98277"),
            new Address(940, "SE Pioneer Way", "98277"),
            new String[] {
                    "Turn left onto SE Pioneer Way and walk for 5 minutes.",
                    "940 SE Pioneer Way 98277 is on your left", },
                    "Trip time: 5 minutes",
                    0.232764928811733, null);

    // test directions when start and end are on the same street
    public static final TestRecord testSameStreetDriving = new TestRecord(
            "testSameStreetDriving", TestDB.TINY,
            TestRecord.TestType.DRIVING,
            new Address(630, "SE Pioneer Way", "98277"),
            new Address(940, "SE Pioneer Way", "98277"),
            new String[] {
                    "Turn left onto SE Pioneer Way and go 0.2 miles.",
                    "940 SE Pioneer Way 98277 is on your left", },
                    "Trip length: 0.2 miles",
                    0.232764928811733, null);

    // test directions when start and end are on the same zip
    public static final TestRecord testSameZip = new TestRecord(
            "testSameZip", TestDB.TINY,
            TestRecord.TestType.DRIVING,
            new Address(841, "SE Pioneer Way", "98277"),
            new Address(1403, "Monroe Landing Rd", "98277"),
            new String[] {
                    "Turn left onto SE Pioneer Way and go 0.5 miles.",
                    "Continue onto W Pioneer Way and go 0.6 miles.",
                    "Turn slight left onto State Route 20 and go 1.6 miles.",
                    "Turn left onto Monroe Landing Rd and go 0.2 miles.",
                    "1403 Monroe Landing Rd 98277 is on your right",},
                    "Trip length: 2.9 miles",
                    2.9145611963146507, null);

    
    ////////////////// TEST EXAMPLE PATHFINDING ///////////////////////
    
    // test the first example from the assignment handout
    public static final TestRecord testExamplePath1 = new TestRecord(
            "testExamplePath1", TestDB.TINY,
            TestRecord.TestType.WALKING,
            new Address(950, "NW 2nd Ave", "98277"),
            new Address(473, "SW Fairhaven Dr","98277"),
            new String[] {
                    "Turn left onto NW 2nd Ave and walk for 4 minutes.",
                    "Turn right onto NW Fairhaven Dr and walk for 2 minutes.",
                    "Turn slight right onto SW Fairhaven Dr and walk for 7 minutes.",
                    "473 SW Fairhaven Dr 98277 is on your right",},
                    "Trip time: 13 minutes",
                    0.6395505841145289, null);

    // test the second example from the assignment handout
    public static final TestRecord testExamplePath2 = new TestRecord(
            "testExamplePath2", TestDB.TINY,
            TestRecord.TestType.DRIVING,
            new Address(841, "SE Pioneer Way", "98277"),
            new Address(1403, "Monroe Landing Rd","98277"),
            new String[] {
                    "Turn left onto SE Pioneer Way and go 0.5 miles.",
                    "Continue onto W Pioneer Way and go 0.6 miles.",
                    "Turn slight left onto State Route 20 and go 1.6 miles.",
                    "Turn left onto Monroe Landing Rd and go 0.2 miles.",
                    "1403 Monroe Landing Rd 98277 is on your right",},
                    "Trip length: 2.9 miles",
                    2.9145611963146507, null);

    // test the second example from the assignment handout
    public static final TestRecord testExamplePath3 = new TestRecord(
            "testExamplePath3", TestDB.TINY,
            TestRecord.TestType.DRIVING,
            new Address(2430, "Fairway Ln", "98277"),
            new Address(1614, "Swantown Rd","98277"),
            new String[] {
                    "Turn left onto Fairway Ln and go 0.2 miles.",
                    "Turn sharp left onto Swantown Rd and go 1.1 miles.",
                    "1614 Swantown Rd 98277 is on your right",},
                    "Trip length: 1.3 miles",
                   1.3038780471564961, null);


    ///////////////////// TEST BAD QUERIES /////////////////////////
    
    // test for an invalid zip code
    public static final TestRecord testBadZip = new TestRecord(
            "testBadZip", TestDB.TINY,
            TestRecord.TestType.INVALID_ADDRESS,
            new Address(123, "Bad Zip Code Rd", "12345"),
            new Address(123, "Bad Zip Code Rd", "54321"),
            null,null,Double.NaN,
            "No such zipcode: 123 Bad Zip Code Rd 12345");

    // test for an invalid street
    public static final TestRecord testBadStreet = new TestRecord(
            "testBadStreet", TestDB.TINY,
            TestRecord.TestType.INVALID_ADDRESS,
            new Address(123, "Fake St", "98277"),
            new Address(123, "Fake Ave", "98277"),
            null,null,Double.NaN,
            "No such street: 123 Fake St 98277");

    // test for an invalid number
    public static final TestRecord testBadNum = new TestRecord(
            "testBadNum", TestDB.TINY,
            TestRecord.TestType.INVALID_ADDRESS,
            new Address(2000, "Starflower Rd", "98277"),
            new Address(2002,"Starflower Rd", "98277"),
            null,null,Double.NaN,
            "No such number: 2000 Starflower Rd 98277");

    // test for invalid direction type
    public static final TestRecord testBadDirType = new TestRecord(
            "testBadDirType", TestDB.TINY,
            TestRecord.TestType.INVALID_DIR_TYPE,
            new Address(841, "SE Pioneer Way", "98277"),
            new Address(841, "SE Pioneer Way", "98277"),
            null,null,Double.NaN,
            "Invalid direction type: q");
    
    
    
    ///////////////////////////// MY TESTSSS ////////////////////////
    
    
    // test for an invalid destination zip code
    public static final TestRecord testBadZipDest = new TestRecord(
            "testBadZipDest", TestDB.TINY,
            TestRecord.TestType.INVALID_ADDRESS,
            new Address(2604, "S Harbor Estates Rd", "98253"),
            new Address(2676, "S Harbor Estates Rd", "98250"),
            null,null,Double.NaN,
            "No such zipcode: 2676 S Harbor Estates Rd 98250");
    
    // test for an invalid destination street
    public static final TestRecord testBadStreetDest = new TestRecord(
            "testBadStreetDest", TestDB.TINY,
            TestRecord.TestType.INVALID_ADDRESS,
            new Address(2604, "S Harbor Estates Rd", "98253"),
            new Address(2676, "S Harbor Estates Ave", "98253"),
            null,null,Double.NaN,
            "No such street: 2676 S Harbor Estates Ave 98253");
    
    
    // test for an invalid destination address
    public static final TestRecord testBadNumDest = new TestRecord(
            "testBadNumDest", TestDB.TINY,
            TestRecord.TestType.INVALID_ADDRESS,
            new Address(2604, "S Harbor Estates Rd", "98253"),
            new Address(2576, "S Harbor Estates Rd", "98253"),
            null,null,Double.NaN,
            "No such number: 2576 S Harbor Estates Rd 98253");

    

    /** All validation queries **/
    public static final TestRecord[] allQueries = {
        testBadZip,
        testBadZipDest,
        testBadStreet,
        testBadStreetDest,
        testBadNum,
        testBadNumDest,
        testBadDirType,
        testSameSegment1,
        testSameSegment2,
        testSameSegment3,
        testSameSegment4,
        testSameZip,
        testSameStreetWalking,
        testSameStreetDriving,
        testExamplePath1,
        testExamplePath2,
        testExamplePath3,
    };

    
    public static final TestRecord[] sameQueries = {
        testSameSegment1,
        testSameSegment2,
        testSameSegment3,
        testSameSegment4,
        testSameZip,
        testSameStreetWalking,
        testSameStreetDriving,
    };
    
    /** Invalid queries */
    public static final TestRecord[] badQueries = {
        testBadZip,
        testBadZipDest,
        testBadStreet,
        testBadStreetDest,
        testBadNum,
        testBadNumDest,
        testBadDirType,
    };
    
    
}
