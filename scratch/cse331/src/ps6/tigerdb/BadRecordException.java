package ps6.tigerdb;

/**
 * BadRecordException.java
 *
 * @author Felix S. Klock II
 */

public class BadRecordException extends Exception {
    String r, o;
    public BadRecordException(String reason, String origData) {
        r = reason;
        o = origData;
    }

    public String reason() { return r; }
    public String originalData() { return o; }
    public static final long serialVersionUID = 4534;
} // BadRecordException
