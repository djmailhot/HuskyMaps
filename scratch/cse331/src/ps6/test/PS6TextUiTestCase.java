package ps6.test;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import junit.framework.TestCase;

/**
 * An immutable TextUI test case for PS6. Runs the TextUI main method
 * on a given input and compared the actual output to the expected
 * output
 *
 * @author tws
 */
public class PS6TextUiTestCase extends TestCase {
    private static final int INITIAL_BUFFER_SIZE = 10240; // 10 kilobytes

    private final InputStream testInput;
    private final String expected;
    private final Method mainMethod;
    private final String[] mainParams;

    private static Class<?> convertToNewLoader(Class<?> mainClass) {
        try {
            junit.runner.TestCaseClassLoader loader = new junit.runner.TestCaseClassLoader();
            return loader.loadClass(mainClass.getName());
        } catch (Exception mue) {
            throw new RuntimeException("Can't load class", mue);
        }

    }

    /**
     * @param mainClass
     *            class that defines a main() that reads from System.in and
     *            writes to System.out
     * @param testInput
     *            test input for the filter
     * @param expected
     *            expected test output
     */
    public PS6TextUiTestCase(String description, String mainParams[], Class<?> iMainClass,
                             InputStream testInput, String expected) {
        super(description);
        Class<?> mainClass = convertToNewLoader(iMainClass);
        assertNotNull(mainClass);
        assertNotNull(testInput);
        assertNotNull(expected);
        assertNotNull(mainParams);

        this.mainParams = mainParams;
        this.testInput = testInput;
        this.expected = expected;

        // grab the "main" method from mainClass
        try {
            this.mainMethod = mainClass.getMethod("main",
                                                  new Class[] { String[].class });
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(mainClass.getName()
                                               + " does not define main()");
        }
        if (!Modifier.isPublic(mainMethod.getModifiers())
            || !Modifier.isStatic(mainMethod.getModifiers()))
            throw new IllegalArgumentException(mainClass.getName()
                                               + " main() is not public and static");

    }

    /**
     * Runs the this.mainMethod, piping this.testInput into its
     * standard input. Standard output is captured and compared to this.expected
     *
     */
    @Override
        protected void runTest() {
        // save the old streams
        PrintStream savedSystemOut = System.out;
        InputStream savedSystemIn = System.in;

        // read input from testInput and capture output in buffers
        ByteArrayOutputStream outBuffer = new ByteArrayOutputStream(
                                                                    INITIAL_BUFFER_SIZE);

        PrintStream testOutput = new PrintStream(outBuffer, true); // autoflush

        System.setIn(testInput);
        System.setOut(testOutput);

        try {
            mainMethod.invoke(null, new Object[]{mainParams});
        } catch (IllegalArgumentException e) {
            fail("Internal Invocation Error:" + e.getMessage());
        } catch (IllegalAccessException e) {
            fail("Internal Invocation Error:" + e.getMessage());
        } catch (InvocationTargetException e) {
            fail("Internal Invocation Error:" + e.getMessage());
        }

        // flush streams
        System.out.flush();
        System.err.flush();

        // compare actual output to expected
        assertEquals(expected,outBuffer.toString());

        // restore the old streams
        System.setIn(savedSystemIn);
        System.setOut(savedSystemOut);
    }
}
