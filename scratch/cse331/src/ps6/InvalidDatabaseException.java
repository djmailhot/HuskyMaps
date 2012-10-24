package ps6;

/**
 * Checked exception to indicate that there was an error
 * in loading the Tiger database.
 **/
public class InvalidDatabaseException extends Exception {
    public static final long serialVersionUID = 4534;
    public InvalidDatabaseException() {
        super();
    }
    public InvalidDatabaseException (String dbname) {
        super (dbname);
    }
    public InvalidDatabaseException (String dbname, Throwable cause) {
        super(dbname, cause);
    }
}
