package ps6.tigerdb;


/**
 * TigerRT6.java
 *
 * @author Felix S. Klock II
 */
public class TigerRT6 extends TigerRwTLID {

    public static final long serialVersionUID = 90423;
    @SuppressWarnings("unused")
    private int rtsq;
    private DirectedStreetNumberRange lftRange;
    private DirectedStreetNumberRange rgtRange;

    public TigerRT6(String s) throws BadRecordException {
        super(s, 76);
        try { rtsq = toInt(s.substring(15,18));
        } catch (NoInt e) { die(""); }

        lftRange = parseAddrRange(s.substring(18,29), s.substring(29,40));
        rgtRange = parseAddrRange(s.substring(40,51), s.substring(51,62));
    }

    /**
     * @return the left street number range associated with this RT6
     */
    public DirectedStreetNumberRange getLeftRange() {
        return lftRange;
    }


    /**
     * @return the right street number range associated with this RT6
     */
    public DirectedStreetNumberRange getRightRange() {
        return rgtRange;
    }

} // TigerRT6
