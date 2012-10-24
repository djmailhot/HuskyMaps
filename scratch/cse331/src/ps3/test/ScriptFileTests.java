package ps3.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URISyntaxException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import ps3.test.PS3TestDriver;

/**
 * This class, along with a complete PS4TestDriver implementation,
 * can be used to test the your implementations of Graph and the
 * path finding algorithm using the script file format described
 * in the problem set.  It is assumed that the files are
 * located in the same directory as this class.
*/
public class ScriptFileTests extends TestCase {
    private final File testDriver;
    /** constant to specify a single specific file to test.  Set to null if
     * you want all files run. */
    private static final String SPECIFIC_TEST_FILE = "p3TestOperations.test";
    
    /**
     * Creates a new ScriptFileTests case, which runs the given test file.
     * @param testDriver
     */
    public ScriptFileTests(File testDriver) {
        super("testWithScriptFile");
        //TWS: if only we could make the test name the name of the script :(
        //super(testDriver.getName());
        this.testDriver = testDriver;
    }

    /**
     * Reads in the contents of a file; changes the line
     * separator to System.getProperty("line.separator")
     * @throws FileNotFoundException, IOException
     * @requires that the specified File exists && File ends with a newline
     * @returns the contents of that file
     */
    private String fileContents(File f) throws IOException {
        if (f == null) {
            throw new IllegalArgumentException("No file specified");
        }

        BufferedReader br = new BufferedReader(new FileReader(f));

        StringBuilder result = new StringBuilder();
        String line = null;

        //read line reads up to *any* newline character
        while ( (line = br.readLine()) != null) {
            result.append(line);
            result.append(System.getProperty("line.separator"));
        }

        br.close();
        return result.toString();
    }

    /**
     * @throws IOException
     * @requires there exists a test file indicated by testDriver
     *
     * @effects runs the test in filename, and output its results to a file in
     * the same directory with name filename+".actual"; if that file already
     * exists, it will be overwritten.
     * @returns the contents of the output file
     */
    private String runScriptFile() throws IOException {
        if (testDriver == null) {
            throw new RuntimeException("No file specified");
        }

        File actual = fileWithSuffix("actual");

        Reader r = new FileReader(testDriver);
        Writer w = new FileWriter(actual);

        PS3TestDriver td = new PS3TestDriver(r, w);
        td.runTests();    // <<<<<<<<<<<<<<<<<<< runs PS3TestDriver.runTests()

        return fileContents(actual);
    }

    /**
     * @param newSuffix
     * @return a File with the same name as testDriver, except that the test
     *         suffix is replaced by the given suffix
     */
    private File fileWithSuffix(String newSuffix) {
        File parent = testDriver.getParentFile();
        String driverName = testDriver.getName();
        String baseName = driverName.substring(0, driverName.length() - "test".length());

        return new File(parent, baseName + newSuffix);
    }

    /**
     * The only test that is run: run a script file and test its output.
     * @throws IOException
     */
    public void testWithScriptFile() throws IOException {
        File expected = fileWithSuffix("expected");

        //TWS: Hack to differentiate tests based on script name
        setName(testDriver.getName().substring(0, testDriver.getName().lastIndexOf('.')) );

        assertEquals(testDriver.getName(), fileContents(expected), runScriptFile());

    }

    /**
     * Build a test suite of all of the script files in the directory.
     * @return the test suite
     * @throws URISyntaxException
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite();

        // Hack to get at the directory where the files are: they are in the
        // same directory as the compiled ScriptFileTests class,
        try {
            // getResource() cannot be null: this file itself is ScriptFileTests
            // getParentFile() cannot be null: ScriptFileTests has a package
            @SuppressWarnings("nullness")
                File myDirectory = new File(ScriptFileTests.class.getResource("ScriptFileTests.class").toURI()).getParentFile();
            for (File f : myDirectory.listFiles()) {
                if (f.getName().endsWith(".test")) {
  //              if (f.getName().equals(SPECIFIC_TEST_FILE)) {
                    suite.addTest(new ScriptFileTests(f));
                }
            }
            return suite;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
