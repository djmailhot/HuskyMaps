package ps6.test;

import ps6.Address;
import ps6.test.TestRecord.TestDB;

public class PathFindingQueries {

	/**
	 * Test collections:
	 * "allQueries"       ( doesn't contain tests of pathFindingStressQueries below)
	 * "sameStreetQueries"
	 * "medDBQueries"
	 * "pathFindingStressQueries"     ( Note, may take an impractical amount of time for 
	 * 				some implementations, especially relying on the optimization of ps3.graph.PathFinder )
	 */
	
	
	//////////////////////////// TEST ON SMALL DATABASE ////////////////////

	
	public static final TestRecord testSmallDB1 = new TestRecord(
            "testSmallDB1", TestDB.SMALL,
            TestRecord.TestType.DRIVING,
            new Address (23147, "N Lake Cochran Rd", "98272"),
            new Address (10900, "Wagner Rd", "98290"),
            new String[] {
                    "Turn left onto N Lake Cochran Rd and go 0.6 miles.",
                    "Turn slight right onto E Lake Cochran Rd and go 0.6 miles.",
                    "Turn right onto Woods Creek Rd and go 3.6 miles.",
                    "Turn right onto Wagner Rd and go 1.9 miles.",
                    "10900 Wagner Rd 98290 is on your left"
            },
            "Trip length: 6.7 miles",
            6.654705056526034, null);

	public static final TestRecord testSmallDB2 = new TestRecord(
            "testSmallDB2", TestDB.SMALL,
            TestRecord.TestType.WALKING,
            new Address (11792, "Bollenbaugh Hill Rd", "98272"),
            new Address (13369, "Robinhood Ln", "98290"),
            new String[] {
                    "Turn right onto Bollenbaugh Hill Rd and walk for 25 minutes.",
                    "Turn left onto Woods Creek Rd and walk for 60 minutes.",
                    "Turn right onto United States Highway 2 and walk for 26 minutes.",
                    "Turn slight right onto 179th Ave SE and walk for 13 minutes.",
                    "Continue onto Robinhood Ln and walk for 2 minutes.",
                    "13369 Robinhood Ln 98290 is on your right"
            },
            "Trip time: 126 minutes",
            6.315092129078746, null);
	
	
	///////////////////// TEST DIFFERENT ZIP CODES ////////////////////
    
    
    // test that if a street has at least one side with a valid zipcode, then it
    // is a valid segment
	
	//E Fairhaven Dr	(48.030969,-122.599991)	(48.030969,-122.600544)	0.0	98249	98249	280-288	267-289
	//Smugglers Cove Rd	(48.020097,-122.590218)	(48.025973,-122.595145)	0.5	98249	98253	300-438,5100-5298	5101-5299
    public static final TestRecord testDestinationDiffZips1 = new TestRecord(
            "testDestinationZipDiffZips1", TestDB.TINY,
            TestRecord.TestType.DRIVING,
            new Address(284, "E Fairhaven Dr", "98249"),
            new Address(5102, "Smugglers Cove Rd", "98249"),
            new String[] {
                "Turn right onto E Fairhaven Dr and go 0.1 miles.",
                "Turn right onto Scurlock Rd and go 0.4 miles.",
                "Turn slight right onto Smugglers Cove Rd and go 0.5 miles.",
                "5102 Smugglers Cove Rd 98249 is on your right", },
                "Trip length: 0.9 miles",
                0.9358807910281886, null);
	
    
	//E Fairhaven Dr	(48.030969,-122.599991)	(48.030969,-122.600544)	0.0	98249	98249	280-288	267-289
	//Smugglers Cove Rd	(48.020097,-122.590218)	(48.025973,-122.595145)	0.5	98249	98253	300-438,5100-5298	5101-5299
    public static final TestRecord testDestinationDiffZips2 = new TestRecord(
            "testDestinationZipDiffZips2", TestDB.TINY,
            TestRecord.TestType.WALKING,
            new Address(285, "E Fairhaven Dr", "98249"),
            new Address(5102, "Smugglers Cove Rd", "98249"),
            new String[] {
                "Turn left onto E Fairhaven Dr and walk for 1 minutes.",
                "Turn right onto Scurlock Rd and walk for 8 minutes.",
                "Turn slight right onto Smugglers Cove Rd and walk for 9 minutes.",
                "5102 Smugglers Cove Rd 98249 is on your right", },
                "Trip time: 19 minutes",
                0.9358807910281886, null);
    
    
    // test when a destination has one side with the address number && wrong zip
    // and the other side has right zip && no address, then no ADDRESS NUMBER is found
    
	//E Fairhaven Dr	(48.030969,-122.599991)	(48.030969,-122.600544)	0.0	98249	98249	280-288	267-289
	//Smugglers Cove Rd	(48.020097,-122.590218)	(48.025973,-122.595145)	0.5	98249	98253	300-438,5100-5298	5101-5299
    public static final TestRecord testAddressAndZipDiffSide1 = new TestRecord(
            "testAddressAndZipDiffSide1", TestDB.TINY,
            TestRecord.TestType.INVALID_ADDRESS,
            new Address(284, "E Fairhaven Dr", "98249"),
            new Address(5297, "Smugglers Cove Rd", "98249"),
            null, null, Double.NaN,
            "No such number: 5297 Smugglers Cove Rd 98249");
    
    //Harbor Hills Dr	(48.030749,-122.53923)	(48.031513,-122.539637)	0.1	98249	98249	4982-4996	4955-4995
    //Seven Eagles Way	(48.041472,-122.539835)	(48.042119,-122.542421)	0.1	98249	98253	1301-1399	1300-1398
    public static final TestRecord testAddressAndZipDiffSide2 = new TestRecord(
            "testAddressAndZipDiffSide2", TestDB.TINY,
            TestRecord.TestType.INVALID_ADDRESS,
            new Address(4982, "Harbor Hills Dr", "98249"),
            new Address(1303, "Seven Eagles Way", "98253"),
            null, null, Double.NaN,
            "No such number: 1303 Seven Eagles Way 98253");
    
    
    
    /////////////////////// TEST SAME STREET SEGMENTS //////////////////////////
    
    
	//Smugglers Cove Rd	(48.020097,-122.590218)	(48.025973,-122.595145)	0.5	98249	98253	300-438,5100-5298	5101-5299
    public static final TestRecord testSameStreetStartBeforeEnd1 = new TestRecord(
            "testSameStreetStartAfterEnd1", TestDB.TINY,
            TestRecord.TestType.DRIVING,
            new Address(5289, "Smugglers Cove Rd", "98253"),	// start address is before end address from p1 to p2
            new Address(5110, "Smugglers Cove Rd", "98249"),	// addresses different sides
            new String[] {
            "Turn right onto Smugglers Cove Rd and go 0.5 miles.",
            "5110 Smugglers Cove Rd 98249 is on your left", },
            "Trip length: 0.5 miles",
            .18141959112645467, null);
    
	//Smugglers Cove Rd	(48.020097,-122.590218)	(48.025973,-122.595145)	0.5	98249	98253	300-438,5100-5298	5101-5299
    public static final TestRecord testSameStreetStartBeforeEnd2 = new TestRecord(
            "testSameStreetStartAfterEnd2", TestDB.TINY,
            TestRecord.TestType.DRIVING,
            new Address(438, "Smugglers Cove Rd", "98249"),	// start address is before end address from p1 to p2
            new Address(5101, "Smugglers Cove Rd", "98253"),	// addresses different sides
            new String[] {
            "Turn left onto Smugglers Cove Rd and go 0.5 miles.",
            "5101 Smugglers Cove Rd 98253 is on your right", },
            "Trip length: 0.5 miles",
            .18141959112645467, null);
    
    
    //Seven Eagles Way	(48.041472,-122.539835)	(48.042119,-122.542421)	0.1	98249	98253	1301-1399	1300-1398
    public static final TestRecord testSameStreetStartBeforeEnd3 = new TestRecord(
            "testSameStreetStartAfterEnd3", TestDB.TINY,
            TestRecord.TestType.DRIVING,
            new Address(1397, "Seven Eagles Way", "98249"),	// start address is before end address from p1 to p2
            new Address(1302, "Seven Eagles Way", "98253"),	// addresses different sides
            new String[] {
            "Turn left onto Seven Eagles Way and go 0.1 miles.",
            "1302 Seven Eagles Way 98253 is on your right", },
            "Trip length: 0.1 miles",
            .18141959112645467, null);
    
    //Seven Eagles Way	(48.041472,-122.539835)	(48.042119,-122.542421)	0.1	98249	98253	1301-1399	1300-1398
    public static final TestRecord testSameStreetStartBeforeEnd4 = new TestRecord(
            "testSameStreetStartAfterEnd4", TestDB.TINY,
            TestRecord.TestType.DRIVING,
            new Address(1397, "Seven Eagles Way", "98249"),	// start address is before end address from p1 to p2
            new Address(1391, "Seven Eagles Way", "98249"),	// addresses different sides
            new String[] {
            "Turn left onto Seven Eagles Way and go 0.1 miles.",
            "1391 Seven Eagles Way 98249 is on your left", },
            "Trip length: 0.1 miles",
            .18141959112645467, null);
    
    
    //////////////////////// TEST SAME STREET ADDRESS NUMBER DISTRIBUTION ///////////////
    
    /* case of different zip codes, same address number.  are addresses distributed independently from side to side?
     * start address = num 1302, zip 11111	
     * end address = num 1301, zip 22222
     *	
     * segment looks like:
     *	left	1300						1398
     *			_______________________________

     *	right	77					  1301	1399
     *
     * ALSO THIS STREET IS READ IN FROM THE DATABASE WITH increasingAddresses == false
     * 
     */
    
    // Graham Dr	(48.159175,-122.4857)	(48.159312,-122.48959)	0.2	98282	98292	1300-1398	77-1399
    public static final TestRecord testSameStreetDiffSideStartBeforeEnd1 = new TestRecord(
            "testSameStreetDiffSideStartBeforeEnd1", TestDB.TINY,
            TestRecord.TestType.DRIVING,
            new Address(1304, "Graham Dr", "98282"),	// start address is before end address from p1 to p2
            new Address(1301, "Graham Dr", "98292"),	// addresses different sides
            new String[] {
            "Turn right onto Graham Dr and go 0.2 miles.",
            "1301 Graham Dr 98292 is on your left", },
            "Trip length: 0.2 miles",
            .18141959112645467, null);
    
	    // Graham Dr	(48.159175,-122.4857)	(48.159312,-122.48959)	0.2	98282	98292	1300-1398	77-1399
	    public static final TestRecord testSameStreetDiffSideStartBeforeEnd2 = new TestRecord(
	            "testSameStreetDiffSideStartBeforeEnd2", TestDB.TINY,
	            TestRecord.TestType.WALKING,
	            new Address(1390, "Graham Dr", "98282"),	// start address is before end address from p1 to p2
	            new Address(1397, "Graham Dr", "98292"),	// addresses different sides
	            new String[] {
	            "Turn right onto Graham Dr and walk for 4 minutes.",
	            "1397 Graham Dr 98292 is on your left", },
	            "Trip time: 4 minutes",
	            .18141959112645467, null);
    
    // Graham Dr	(48.159175,-122.4857)	(48.159312,-122.48959)	0.2	98282	98292	1300-1398	77-1399
    public static final TestRecord testSameStreetDiffSideStartAfterEnd1 = new TestRecord(
            "testSameStreetDiffSideStartAfterEnd1", TestDB.TINY,
            TestRecord.TestType.DRIVING,
            new Address(1301, "Graham Dr", "98292"),	// start address is after end address from p1 to p2
            new Address(1304, "Graham Dr", "98282"),	// addresses different sides
            new String[] {
            "Turn right onto Graham Dr and go 0.2 miles.",
            "1304 Graham Dr 98282 is on your left", },
            "Trip length: 0.2 miles",
            .18141959112645467, null);
    
    // Graham Dr	(48.159175,-122.4857)	(48.159312,-122.48959)	0.2	98282	98292	1300-1398	77-1399
    public static final TestRecord testSameStreetDiffSideStartAfterEnd2 = new TestRecord(
            "testSameStreetDiffSideStartAfterEnd2", TestDB.TINY,
            TestRecord.TestType.WALKING,
            new Address(1397, "Graham Dr", "98292"),	// start address is after end address from p1 to p2
            new Address(1390, "Graham Dr", "98282"),	// addresses different sides
            new String[] {
            "Turn right onto Graham Dr and walk for 4 minutes.",
            "1390 Graham Dr 98282 is on your left", },
            "Trip time: 4 minutes",
            .18141959112645467, null);
    
    // Graham Dr	(48.159175,-122.4857)	(48.159312,-122.48959)	0.2	98282	98292	1300-1398	77-1399
    public static final TestRecord testSameStreetSameSideStartAfterEnd1 = new TestRecord(
            "testSameStreetSameSideStartAfterEnd1", TestDB.TINY,
            TestRecord.TestType.DRIVING,
            new Address(1301, "Graham Dr", "98292"),	// start address is after end address from p1 to p2
            new Address(101, "Graham Dr", "98292"),	// addresses same side
            new String[] {
            "Turn right onto Graham Dr and go 0.2 miles.",
            "101 Graham Dr 98292 is on your right", },
            "Trip length: 0.2 miles",
            .18141959112645467, null);
    
    // Graham Dr	(48.159175,-122.4857)	(48.159312,-122.48959)	0.2	98282	98292	1300-1398	77-1399
    public static final TestRecord testSameStreetSameSideStartAfterEnd2 = new TestRecord(
            "testSameStreetSameSideStartAfterEnd2", TestDB.TINY,
            TestRecord.TestType.WALKING,
            new Address(1396, "Graham Dr", "98282"),	// start address is after end address from p1 to p2
            new Address(1304, "Graham Dr", "98282"),	// addresses same side
            new String[] {
            "Turn left onto Graham Dr and walk for 4 minutes.",
            "1304 Graham Dr 98282 is on your left", },
            "Trip time: 4 minutes",
            .18141959112645467, null);
    
    
    
    ///////////////////////////// TEST SUBSTANTIAL QUERIES ON MED DATABASE /////////////////////////
    
    //Shultzy's Sausage         : 4114        University Way NE       98105
    //Sketchy Safeway           : 4732        Brooklyn Ave NE         98105
    public static final TestRecord testShultzyToSketchy = new TestRecord(
            "testShultzyToSketchy", TestDB.MEDIUM,
            TestRecord.TestType.WALKING,
            new Address(4114, "University Way NE", "98105"),	
            new Address(4732, "Brooklyn Ave NE", "98105"),	
            new String[] {
        	"Turn right onto University Way NE and walk for 2 minutes.",
        	"Turn left onto NE 42nd St and walk for 1 minutes.",
        	"Turn right onto Brooklyn Ave NE and walk for 9 minutes.",
        	"4732 Brooklyn Ave NE 98105 is on your right", },
            "Trip time: 12 minutes",
            .5946036150455003, null);
    
    //Dick's Drive-in           : 115         Broadway E              98102
    //Trabant Coffee & Chai     : 1309        NE 45th St              98105
    public static final TestRecord testDicksToChai = new TestRecord(
            "testDicksToChai", TestDB.MEDIUM,
            TestRecord.TestType.WALKING,
            new Address(115, "Broadway E", "98102"),	
            new Address(1309, "NE 45th St", "98105"),	
            new String[] {
        	"Turn left onto Broadway E and walk for 4 minutes.",
        	"Turn right onto E Harrison St and walk for 1 minutes.",
        	"Turn left onto 10th Ave E and walk for 29 minutes.",
        	"Turn left onto State Route 520 and walk for 1 minutes.",
        	"Turn slight right onto (unnamed street) and walk for 1 minutes.",
        	"Turn slight right onto Harvard Ave E and walk for 10 minutes.",
        	"Turn slight right onto Eastlake Ave E and walk for 2 minutes.",
        	"Continue onto University Brg and walk for 6 minutes.",
        	"Turn slight right onto Roosevelt Way NE and walk for 1 minutes.",
        	"Turn slight right onto (unnamed street) and walk for 1 minutes.",
        	"Turn slight right onto NE 41st St and walk for 2 minutes.",
        	"Turn left onto 12th Ave NE and walk for 6 minutes.",
        	"Turn right onto NE 45th St and walk for 2 minutes.",
        	"1309 NE 45th St 98105 is on your right", },
            "Trip time: 66 minutes",
            3.29816777891387, null);

    
    //Top Pot Doughnuts         : 6855        35th Ave NE             98115
    //U-District Farmers Market : 4519        University Way NE       98105
    public static final TestRecord testDoughnutsToMarket = new TestRecord(
            "testDoughnutsToMarket", TestDB.MEDIUM,
            TestRecord.TestType.DRIVING,
            new Address(6855, "35th Ave NE", "98115"),	
            new Address(4519, "University Way NE", "98105"),	
            new String[] {
        	"Turn right onto 35th Ave NE and go 0.2 miles.",
        	"Turn right onto NE 65th St and go 0.1 miles.",
        	"Turn left onto 33rd Ave NE and go 0.1 miles.",
        	"Turn right onto NE 62nd St and go 0.2 miles.",
        	"Turn left onto 29th Ave NE and go 0.1 miles.",
        	"Turn right onto NE 60th St and go 0.1 miles.",
        	"Turn left onto 27th Ave NE and go 0.2 miles.",
        	"Turn right onto NE 57th St and go 0.1 miles.",
        	"Turn left onto 25th Ave NE and go 0.1 miles.",
        	"Turn right onto NE 55th St and go 0.1 miles.",
        	"Turn slight right onto NE 54th St and go 0.0 miles.",
        	"Turn slight left onto NE22nd Ave and go 0.1 miles.",
        	"Turn right onto NE 52nd St and go 0.3 miles.",
        	"Turn left onto 16th Ave NE and go 0.1 miles.",
        	"Turn right onto NE 50th St and go 0.0 miles.",
        	"Turn left onto 15th Ave NE and go 0.1 miles.",
        	"Turn right onto NE 47th St and go 0.1 miles.",
        	"Turn left onto University Way NE and go 0.1 miles.",
        	"4519 University Way NE 98105 is on your right", },
            "Trip length: 2.2 miles",
            2.1910879673412227, null);
    

    //Dick's Drive-in           : 115         Broadway E              98102
    //Space Needle              : 203         6th Ave N               98109
    public static final TestRecord testDicksToNeedle = new TestRecord(
            "testDicksToNeedle", TestDB.MEDIUM,
            TestRecord.TestType.DRIVING,
            new Address(115, "Broadway E", "98102"),	
            new Address(203, "6th Ave N", "98109"),	
            new String[] {
        	"Turn right onto Broadway E and go 0.1 miles.",
        	"Turn right onto E Denny Way and go 0.4 miles.",
        	"Continue onto Denny Way and go 0.7 miles.",
        	"Turn right onto (unnamed street) and go 0.1 miles.",
        	"Turn slight right onto Aurora Ave N and go 0.0 miles.",
        	"Turn left onto John St and go 0.1 miles.",
        	"Turn right onto 6th Ave N and go 0.1 miles.",
        	"203 6th Ave N 98109 is on your left", },
            "Trip length: 1.4 miles",
            1.3656747868602623, null);
    
    



    
    ///////////////////////////// TEST SUBSTANTIAL QUERIES ON MED DATABASE /////////////////////////

    
    
    //NE 133rd St	(47.717621,-122.074968)	(47.718308,-122.070238)	0.2	98077	98077	19800-20198	19801-20199
    //Pasadena Pl NE	(47.65762,-122.321637)	(47.65832,-122.321637)	0.0	98105	98105	4201-4299	4200-4298
    public static final TestRecord testKingEastsideToWestside = new TestRecord(
            "testKingEastsideToWestside", TestDB.MEDIUM,
            TestRecord.TestType.DRIVING,
            new Address(20000, "NE 133rd St", "98077"),	// start address is after end address from p1 to p2
            new Address(4204, "Pasadena Pl NE", "98105"),	// addresses same side
            new String[] {
        	"Turn right onto NE 133rd St and go 0.5 miles.",
        	"Turn slight left onto Bear Creek Rd NE and go 0.1 miles.",
        	"Turn slight right onto NE 132nd St and go 0.2 miles.",
        	"Turn left onto Avondale Rd NE and go 0.2 miles.",
        	"Turn right onto NE 130th St and go 0.1 miles.",
        	"Turn slight left onto NE 128th Way and go 0.3 miles.",
        	"Turn slight right onto NE 128th St and go 0.6 miles.",
        	"Turn slight left onto NE 124th Way and go 0.4 miles.",
        	"Turn slight right onto NE 124th St and go 1.6 miles.",
        	"Turn slight right onto NE 124th St NE and go 0.0 miles.",
        	"Continue onto NE 124th St and go 0.5 miles.",
        	"Turn slight left onto Slater Ave and go 0.1 miles.",
        	"Turn slight right onto Slater Ave NE and go 0.6 miles.",
        	"Turn slight right onto NE 116th St and go 0.2 miles.",
        	"Turn left onto (unnamed street) and go 0.2 miles.",
        	"Continue onto I-405 and go 2.2 miles.",
        	"Turn slight right onto NE 72nd Pl and go 0.1 miles.",
        	"Continue onto NE 68th St and go 0.2 miles.",
        	"Turn left onto 111th Ave NE and go 0.1 miles.",
        	"Turn right onto NE 65th St and go 0.0 miles.",
        	"Turn left onto 111th Ave NE and go 0.1 miles.",
        	"Turn right onto NE 62nd St and go 0.2 miles.",
        	"Turn left onto 108th Ave NE and go 0.6 miles.",
        	"Turn right onto NE 52nd St and go 0.4 miles.",
        	"Turn slight left onto Lake Washington Blvd NE and go 0.7 miles.",
        	"Turn right onto NE Points Dr and go 0.6 miles.",
        	"Turn slight left onto Points Dr NE and go 0.2 miles.",
        	"Turn slight left onto 92nd Ave NE and go 0.0 miles.",
        	"Turn right onto State Route 520 and go 1.3 miles.",
        	"Continue onto Evergreen Point Floating Brg and go 2.2 miles.",
        	"Turn slight right onto State Route 520 and go 0.1 miles.",
        	"Continue onto (unnamed street) and go 0.6 miles.",
        	"Turn right onto Montlake Blvd E and go 0.1 miles.",
        	"Continue onto Montlake Brg and go 0.1 miles.",
        	"Continue onto Montlake Blvd NE and go 0.1 miles.",
        	"Turn slight left onto NE Pacific St and go 0.5 miles.",
        	"Turn slight right onto University Way NE and go 0.0 miles.",
        	"Turn slight left onto Burke Gilman Trl and go 0.4 miles.",
        	"Turn slight right onto 7th Ave NE and go 0.1 miles.",
        	"Turn left onto NE 42nd St and go 0.0 miles.",
        	"Turn right onto Pasadena Pl NE and go 0.0 miles.",
        	"4204 Pasadena Pl NE 98105 is on your right", },
            "Trip length: 16.8 miles",
            16.761847120865724, null);
    
    
    //Seneca St	(47.610507,-122.327386)	(47.610987,-122.326233)	0.1	98101	98101	1000-1098	1001-1099
    // near the southern King-county county-line
    //59th Pl S	(47.392488,-122.26138)	(47.393315,-122.261815)	0.1	98032	98032	23301-23359	23300-23358
    public static final TestRecord testKingSeattleToSouthedge = new TestRecord(
            "testKingSeattleToSouthedge", TestDB.MEDIUM,
            TestRecord.TestType.WALKING,
            new Address(1096, "Seneca St", "98101"),	// start address is after end address from p1 to p2
            new Address(23304, "59th Pl S", "98032"),	// addresses same side
            new String[] {
        	"Turn left onto Seneca St and walk for 1 minutes.",
        	"Turn right onto Boren Ave and walk for 15 minutes.",
        	"Continue onto Boren Ave S and walk for 5 minutes.",
        	"Turn slight right onto Rainier Ave S and walk for 36 minutes.",
        	"Turn slight right onto Martin Luther King Jr Way S and walk for 101 minutes.",
        	"Turn slight left onto Martin Luther King Jr Way and walk for 10 minutes.",
        	"Continue onto I-5 and walk for 63 minutes.",
        	"Turn left onto Klickitat Dr and walk for 1 minutes.",
        	"Turn right onto Southcenter Pky and walk for 38 minutes.",
        	"Turn slight left onto Frager Rd S and walk for 26 minutes.",
        	"Turn left onto S 212th St and walk for 1 minutes.",
        	"Turn right onto Russell Rd and walk for 25 minutes.",
        	"Turn left onto S 228th St and walk for 2 minutes.",
        	"Turn right onto 57th Ave S and walk for 1 minutes.",
        	"Continue onto 58th Ave S and walk for 0 minutes.",
        	"Turn slight left onto Lakeside Blvd E and walk for 5 minutes.",
        	"Turn sharp right onto (unnamed street) and walk for 1 minutes.",
        	"Turn right onto 59th Pl S and walk for 2 minutes.",
        	"23304 59th Pl S 98032 is on your right" },
            "Trip time: 333 minutes",
            16.662023932461405, null);
    

    
    
    
    // near the northern King-county county-line
    //Echo Lake Pl N	(47.770519,-122.34604)	(47.772319,-122.34464)	0.1	98133	98133	19501-19799	19500-19798
    // near the southern King-county county-line
    //59th Pl S	(47.392488,-122.26138)	(47.393315,-122.261815)	0.1	98032	98032	23301-23359	23300-23358
    public static final TestRecord testKingNorthedgeToSouthedge = new TestRecord(
            "testKingNorthedgeToSouthedge", TestDB.MEDIUM,
            TestRecord.TestType.WALKING,
            new Address(19696, "Echo Lake Pl N", "98133"),	// start address is after end address from p1 to p2
            new Address(23304, "59th Pl S", "98032"),	// addresses same side
            new String[] {
        	"Turn left onto Echo Lake Pl N and walk for 3 minutes.",
        	"Turn slight left onto Aurora Ave N and walk for 169 minutes.",
        	"Continue onto Aurora Brg and walk for 4 minutes.",
        	"Continue onto Aurora Ave N and walk for 1 minutes.",
        	"Turn slight left onto Westlake Ave N and walk for 39 minutes.",
        	"Continue onto Westlake Ave and walk for 11 minutes.",
        	"Turn slight left onto 4th Ave and walk for 15 minutes.",
        	"Turn slight right onto 4th Ave S and walk for 10 minutes.",
        	"Continue onto I-90 and walk for 3 minutes.",
        	"Continue onto 4th Ave S and walk for 67 minutes.",
        	"Turn slight left onto East Marginal Way S and walk for 77 minutes.",
        	"Turn slight left onto State Route 599 and walk for 26 minutes.",
        	"Turn slight right onto I-5 and walk for 37 minutes.",
        	"Turn left onto Klickitat Dr and walk for 1 minutes.",
        	"Turn right onto Southcenter Pky and walk for 38 minutes.",
        	"Turn slight left onto Frager Rd S and walk for 26 minutes.",
        	"Turn left onto S 212th St and walk for 1 minutes.",
        	"Turn right onto Russell Rd and walk for 25 minutes.",
        	"Turn left onto S 228th St and walk for 2 minutes.",
        	"Turn right onto 57th Ave S and walk for 1 minutes.",
        	"Continue onto 58th Ave S and walk for 0 minutes.",
        	"Turn slight left onto Lakeside Blvd E and walk for 5 minutes.",
        	"Turn sharp right onto (unnamed street) and walk for 1 minutes.",
        	"Turn right onto 59th Pl S and walk for 2 minutes.",
        	"23304 59th Pl S 98032 is on your right", },
            "Trip time: 562 minutes",
            28.11931796922192, null);
    
    
    
    /*
     * With standard A* heuristic for PathFinder:  
     * Shortest length  =  28.11931796922192
     * Time  =  341.184 s
     * 
     * 
     * With modified A* of [cost = path length + (distance to nearest goal) * 1.01 ] :
     * Shortest length  =  28.11931796922192
     * Time  =  168 s
     * Analysis --> a x2 factor of time less for an equal length
     * 
     * 
     * With modified A* of [cost = path length + (distance to nearest goal) * 1.1 ] :
     * Shortest length  =  28.16138411000092
     * Time  =  7.257
     * Analysis --> a x100 factor of time less for a ~0.1% hit in accuracy
     * 
     * 
     * 
     * 
     */
    
    
    
    
    /** all pathfinding queries */
    public static final TestRecord[] allQueries = {
    	testSmallDB1,
    	testSmallDB2,
    	testDestinationDiffZips1,
    	testDestinationDiffZips2,
        testAddressAndZipDiffSide1,
        testAddressAndZipDiffSide2,
        testSameStreetStartBeforeEnd1,
        testSameStreetStartBeforeEnd2,
        testSameStreetStartBeforeEnd3,
        testSameStreetStartBeforeEnd4,
        testSameStreetDiffSideStartBeforeEnd1,
        testSameStreetDiffSideStartBeforeEnd2,
        testSameStreetDiffSideStartAfterEnd1,
        testSameStreetDiffSideStartAfterEnd2,
        testSameStreetSameSideStartAfterEnd1,
        testSameStreetSameSideStartAfterEnd2,
        testShultzyToSketchy, 
        testDicksToChai,
        testDoughnutsToMarket,
        testDicksToNeedle,
    };
    
    public static final TestRecord[] sameStreetQueries = {
    	testSameStreetStartBeforeEnd1,
    	testSameStreetStartBeforeEnd2,
    	testSameStreetStartBeforeEnd3,
    	testSameStreetStartBeforeEnd4,
        testSameStreetDiffSideStartBeforeEnd1,
        testSameStreetDiffSideStartBeforeEnd2,
        testSameStreetDiffSideStartAfterEnd1,
        testSameStreetDiffSideStartAfterEnd2,
        testSameStreetSameSideStartAfterEnd1,
        testSameStreetSameSideStartAfterEnd2,
    };
    
    public static final TestRecord[] medDBQueries = {
        testShultzyToSketchy, 
        testDicksToChai,
        testDoughnutsToMarket,
        testDicksToNeedle,
    	testKingEastsideToWestside,
    	testKingSeattleToSouthedge,
    	testKingNorthedgeToSouthedge,
    };
    
    public static final TestRecord[] pathFindingStressQueries = {
    	testKingEastsideToWestside,
    	testKingSeattleToSouthedge,
    	testKingNorthedgeToSouthedge,
    };
    
    
}
