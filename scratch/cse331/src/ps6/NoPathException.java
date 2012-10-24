package ps6;

/**
 * Checked exception to indicate that a path was not found.
 **/
public class NoPathException extends Exception {
    public static final long serialVersionUID = 4534;
    public NoPathException () {
        super ();
    }
    public NoPathException(String s) {
        super(s);
    }
}
