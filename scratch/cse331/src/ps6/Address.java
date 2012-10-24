package ps6;

/** An Address is an immutable record type defining a location.
 *   @specfield num     : integer // number of the building along the street
 *   @specfield name    : String  // name of the street
 *   @specfield zipcode : String  // US ZIP code
 */
public class Address {

    // AF(x):
    //    num = x.streetNum
    //    name = x.streetName
    //    zipcode = x.zipcode
    // RI:
    //    num >= 0
    //    streetName != null &&
    //    zipcode != null

    private final int streetNum;
    private final String streetName;
    private final String zipcode;

    /**
     * Address Constructor
     *
     * @requires num > 0 && name != null && zipcode != null
     * @effects returns an Address with the given field values
     * */
    public Address (int num, String name, String zipcode) {
        if (num < 0) {
            throw new IllegalArgumentException("Negative street number");
        } else if (name == null) {
            throw new NullPointerException("Street name is null");
        } else if (zipcode == null) {
            throw new NullPointerException("Zip code is null");
        }
        this.streetNum = num;
        this.streetName = name.intern();
        this.zipcode = zipcode.intern();
    }

    /**
     * Get the street number
     * @returns this.num
     */
    public int getNum () {
        return streetNum;
    }

    /**
     * Get the name of the street
     * @returns this.name
     */
    public String getName () {
        return streetName;
    }

    /**
     * Get the zipcode for the street
     * @returns this.zipcode
     */
    public String getZipcode () {
        return zipcode;
    }

    public boolean equals (Object other) {
        return (other instanceof Address) && equals((Address) other);
    }

    public boolean equals (Address other) {
        //can use referential equality because strings are interned
        return (other != null) &&
            zipcode == other.zipcode &&
            streetName == other.streetName &&
            (streetNum == other.streetNum);
    }

    public int hashCode () {
        return streetName.hashCode() + zipcode.hashCode() * 7 + streetNum * 17;
    }

    /**
     * @return a String representation of this address in the format:
     * "num name zipcode"
     **/
    public String toString () {
        return new String (streetNum + " " + streetName + " " + zipcode);
    }
}
