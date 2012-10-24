package ps6.tigerdb;

/**
 * NonNumericDataException.java
 *
 * @author Felix S. Klock II
 */

public class NonNumericDataException extends BadRecordException {
    
    public NonNumericDataException(String o) {
        super("non-numeric data", o);
    }
    public static final long serialVersionUID = 4534;
} // NonNumericDataException
