package ps6.test;

import ps6.Address;
import ps6.test.TestRecord.TestDB;

public class CustomDBQueries {

	
	// Ravenscall                    Rd    A41       4398       4300       4399       430100009825318253
    public static final TestRecord testSameStreetMyDB1 = new TestRecord(
            "testSameStreetDiffSideStartBeforeEnd101", TestDB.MYDB,
            TestRecord.TestType.WALKING,
            new Address(4396, "Ravenscall", "98253"),	// start address is before end address from p1 to p2
            new Address(4303, "Ravenscall", "18253"),	// addresses different sides
            new String[] {
            "Turn left onto Ravenscall and go 0.2 miles.",
            "4303 Ravenscall 18253 is on your right", },
            "Trip length: 0.2 miles",
            .18141959112645467, null);
    
    //Bald Eagle                    Way   A41        501        699        500        69800009824998249 
    public static final TestRecord testSameStreetMyDB2 = new TestRecord(
            "testSameStreetDiffSideStartBeforeEnd102", TestDB.MYDB,
            TestRecord.TestType.WALKING,
            new Address(501, "Bald Eagle", "98249"),	// start address is before end address from p1 to p2
            new Address(698, "Bald Eagle", "98249"),	// addresses different sides
            new String[] {
            "Turn left onto Ravenscall and go 0.2 miles.",
            "698 Bald Eagle 98249 is on your right", },
            "Trip length: 0.2 miles",
            .18141959112645467, null);
	
	
	
    /** all pathfinding queries */
    public static final TestRecord[] allQueries = {
    	testSameStreetMyDB1,
    	testSameStreetMyDB2,
    };
	
}
