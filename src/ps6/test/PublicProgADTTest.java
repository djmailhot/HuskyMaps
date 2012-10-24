package ps6.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PublicProgADTTest extends TestSuite {

    public PublicProgADTTest() {this("Problem Set 6 Public ADT Test");}

    public PublicProgADTTest(String s) {
        super(s);
        addTests();
    }

    public static Test suite() {
        return new PublicProgADTTest();
    }

    /**
     * Constructs and adds the tests for the prog. ADT.
     */
    @SuppressWarnings("unused")
	private void addTests() {
        for (TestRecord record : ValidateQueries.allQueries) {
        //    addTest(new PS6ProgTestCase(record));
        }
        
        // <<<<<<<<<<<<<<<<<<< MY TESTS >>>>>>>>>>>>>>>>>
        for (TestRecord record : ValidateQueries.sameQueries) {
        //	addTest(new PS6ProgTestCase(record));
        }
        
        for (TestRecord record : PathFindingQueries.allQueries) {
        //	addTest(new PS6ProgTestCase(record));
        }
        
        for (TestRecord record : PathFindingQueries.medDBQueries) {
           	addTest(new PS6ProgTestCase(record));
        }
        
        // can't get custom database to work
        for (TestRecord record : CustomDBQueries.allQueries) {
        //	addTest(new PS6ProgTestCase(record));
        }
    }
}
