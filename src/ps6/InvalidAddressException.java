package ps6;

/**
 * Checked exception to indicate that an Address has
 * an invalid number, street, or zipcode.
 **/
public class InvalidAddressException extends Exception {
    public static final long serialVersionUID = 4534;
    public InvalidAddressException () {
        super ();
    }
    public InvalidAddressException(String s) {
        super(s);
    }
}
