package ps6.test;

import java.io.ByteArrayInputStream;
import ps6.*;
import junit.framework.*;

/**
 * Public (Specification) TextUI Test Suite
 * @author tws
 */
public class PublicTextUITest extends TestSuite{

    public static final String INPUT_PROMPTS =
        "starting number? starting street? starting zipcode? " +
        "destination number? destination street? destination zipcode? walking or driving [w/d]? ";

    public static final String END_PROMPT = "starting number? ";

    public PublicTextUITest() {
        this("Problem Set 6 TextUI Tests");
    }

    public static String EOL = System.getProperty("line.separator");

    public PublicTextUITest(String name) {
        super(name);
        addTests();
    }

    /**
     * Constructs and adds the tests for the textui.
     */
    private void addTests() {
        // You could test groups of queries separately
        // e.g. loop over ValidateQueries.badQueries
    	
        for (TestRecord record : ValidateQueries.allQueries) {
            addTest(makeUiTest(record));
        }
        
        
        // <<<<<<<<<<<<<<< MY TESTS >>>>>>>>>>>>>>>>>
        for (TestRecord record : ValidateQueries.sameQueries) {
        //    addTest(makeUiTest(record));
        }
        
        for (TestRecord record : PathFindingQueries.allQueries) {
        	addTest(makeUiTest(record));
        }
        
        for (TestRecord record : PathFindingQueries.sameStreetQueries) {
        //	addTest(makeUiTest(record));
        }
        
        for (TestRecord record : PathFindingQueries.medDBQueries) {
        	addTest(makeUiTest(record));
        }
        
        // can't get custom database to work
        for (TestRecord record : CustomDBQueries.allQueries) {
        //	addTest(makeUiTest(record));
        }
        
    }

    public PS6TextUiTestCase makeUiTest(TestRecord record) {
        return new PS6TextUiTestCase(record.getTestName(),
                new String[] {record.getDb().dbPath()},
                TextUI.class,
                new ByteArrayInputStream((genTextUiStdIn(record) + "-1" + EOL).getBytes()),
                INPUT_PROMPTS + genTextUiStdOut(record));
    }

    public static Test suite() {
        return new PublicTextUITest();
    }

    /**
     * Generate the expected TextUI output for a test record
     * @param record a PS6 test
     * @return the expected TestUI output for a test
     */
    public static String genTextUiStdOut(TestRecord record) {
        StringBuilder b = new StringBuilder();

        switch (record.getType()) {
        case INVALID_DIR_TYPE:
        case INVALID_ADDRESS:
        case NO_PATH:
            b.append(record.getErrorMessage());
            b.append(EOL);
            break;
        case WALKING:
        case DRIVING:
            b.append("Start at "
                    + record.getStart().getNum() + " "
                    + record.getStart().getName()
                    + " " + record.getStart().getZipcode());

            b.append(EOL);
            for (String line : record.getDirections()) {
                b.append(line);
                b.append(EOL);
            }
            b.append(record.getTripLength());
            b.append(EOL);
            b.append(END_PROMPT);
            break;
        }

        return b.toString();
    }

    /**
     * Generate the TextUI input for a test record
     * @param record a PS6 test
     * @return the TextUI input for the test
     */
    public static String genTextUiStdIn(TestRecord record) {
        StringBuilder b = new StringBuilder();
        b.append(record.getStart().getNum());
        b.append(EOL);
        b.append(record.getStart().getName());
        b.append(EOL);
        b.append(record.getStart().getZipcode());
        b.append(EOL);
        b.append(record.getEnd().getNum());
        b.append(EOL);
        b.append(record.getEnd().getName());
        b.append(EOL);
        b.append(record.getEnd().getZipcode());
        b.append(EOL);
        b.append(record.getDirectionType());
        b.append(EOL);
        return b.toString();
    }
}
