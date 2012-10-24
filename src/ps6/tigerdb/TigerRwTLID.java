package ps6.tigerdb;

/**
 * TigerRwTLID.java
 *
 * @author Felix S. Klock II
 */

public class TigerRwTLID extends TigerRecord {

    public static final long serialVersionUID = 4534;
    private final int tlid;

    public TigerRwTLID(String s, int size, int tlidStart)
        throws BadRecordException {
        super(s, size);
        try {
            tlid = toInt(s.substring(tlidStart,tlidStart+10));
        } catch (NoInt e) {
            throw new RuntimeException();
        }
    }

    public TigerRwTLID(String s, int size) throws BadRecordException {
        this(s, size, 5);
    }

    /**
     * @return the TLID of this record
     */
    /*@Pure*/
    public int getTLID() {
        return tlid;
    }

} // TigerRwTLID
