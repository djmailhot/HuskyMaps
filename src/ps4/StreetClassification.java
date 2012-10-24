package ps4;

/**
 * A StreetClassification describes a street category. It is an
 * <i>enumeration</i> type, which you can tell by the class declaration
 * <tt>public enum StreetClassification</tt>.  There are a handful of
 * values the type may hold, and the set of options is fixed at compile
 * time.
 *
 * <p>
 * Example uses:
 *
 * <pre>
 * StreetClassification myType = StreetClassification.LOCAL_ROAD;
 * // ...
 * if (myType == StreetClassification.UNKNOWN) {
 *      // ...
 * }
 * </pre>
 *
 * <p>
 * Notice that you may reference the constant values as you would with any other
 * static variable, e.g. ClassName.STATIC_FIELD_NAME. Also, you may use the
 * <code>==</code> operator to check for equality, since there is only one copy
 * of each object.
 *
 * <p>
 * The ordering given by the <code>compareTo</code> method of this class is
 * consistent with equals, and gives the following ordering: PRIM_HWY, SEC_HWY,
 * LOCAL_ROAD, UNKNOWN.
 */

public enum StreetClassification {

    /**
     * Classification indicating a primary highway. Primary highways include
     * interstate highways and some toll highways; these highways are accesed by
     * way of ramps and have multiple lanes of traffic.
     */
    PRIM_HWY("PRIM_HWY", "Primary Highway"),

    /**
     * Classification indicating a secondary highway. Secondary highways include
     * state highways and some county highways.
     */
    SEC_HWY("SEC_HWY", "Secondary Highway"),

    /**
     * Classification indicating a local road. Local roads are for local
     * traffic. Scenic park roads and unpaved roads are also included in this
     * category.
     */
    LOCAL_ROAD("LOCAL_ROAD", "Local Road"),

    /**
     * Classification indicating an unknown type of street. This classificiation
     * is given to streets that do not fall within one of the other three
     * categories or to streets for which not enough information is known to
     * classify them.
     */
    UNKNOWN("UNKNOWN", "Unknown");


    //FIELDS

    /** Name of enum constant */
    private final String repr;
    /** Human-friendly name */
    private final String name;

    //CONSTRUCTOR

    StreetClassification(String repr, String name) {
        this.repr = repr;
        this.name = name;
    }

    //OBSERVERS

    /**
     * @returns a String representation of this
     */
    public String toString() {
        return "StreetClassification[" + name + "]";
    }

    //FACTORY METHOD

    /**
     * @param in the enum constant name or human-friendly name
     * @returns a StreetClassification that matches the string passed in
     */
    public static StreetClassification parse(String in) {
        for (StreetClassification typ : StreetClassification.values()) {
            if (typ.name.equals(in) || typ.repr.equals(in)) {
                return typ;
            }
        }
        return UNKNOWN;
    }

    /** Returns a concise, parseable string representation. */
    public String unparse() {
        return repr;
    }

} //StreetClassification
