package ps6.tigerdb;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Feature.java
 *
 * @author Felix S. Klock II
 */

public class Feature implements Serializable {
    // public static Set fullNames = new TreeSet();
    public static final long serialVersionUID = 4534;
    private final String prefixDir;
    private final String name;
    private final String type;
    private final String suffixDir;

    public Feature(String prefixDir, String name,
                   String type, String suffixDir) {
        this.prefixDir = prefixDir.trim().intern();
        this.name = name.trim().intern();
        this.type = type.trim().intern();
        this.suffixDir = suffixDir.trim().intern();

        // fullNames.add( fullName().intern() );
    }

    public Feature(String s) {
        if (s.length() != 38)
            throw new RuntimeException
                ("feature must have length of 38, not "+s.length());
        prefixDir = s.substring( 0,  2).trim().intern();
        name      = s.substring( 2, 32).trim().intern();
        type      = s.substring(32, 36).trim().intern();
        suffixDir = s.substring(36, 38).trim().intern();

        // fullNames.add( fullName().intern() );
    }

    public boolean equals(/*@Nullable*/ Object o) {
        if (! (o instanceof Feature) ) return false;
        return ((Feature)o).fullName().equals(this.fullName());
    }
    public int hashCode() {
        return fullName().hashCode();
    }
    public String fullName() {
        StringBuilder sb = new StringBuilder();
        boolean pre = false;
        if (prefixDir.length() > 0) {
            sb.append(prefixDir); pre = true;
        }
        if (name.length() > 0) {
            sb.append((pre)?" ":"");
            sb.append(name);
            pre = true;
        }
        if (type.length() > 0) {
            sb.append((pre)?" ":"");
            sb.append(type);
            pre = true;
        }
        if (suffixDir.length() > 0) {
            sb.append((pre)?" ":"");
            sb.append(suffixDir);
        }

        if (sb.length() == 0) {
            sb.append("(unnamed street)");
        }
        return sb.toString();
    }

    private static HashMap<Feature, Feature> internMap = new HashMap<Feature, Feature>();
    public Feature intern() {
        if (internMap.containsKey(this)) {
            return internMap.get(this);
        } else {
            internMap.put(this, this);
            return this;
        }
    }

    /** Returns an Iterator[String] of names for this feature. */
    Iterator<String> names() {
        ArrayList<String> l = new ArrayList<String>();
        l.add(fullName().trim());
        l.add(name.trim());
        l.add( (prefixDir+" "+name).trim() );
        l.add( (prefixDir+" "+name+" "+type).trim() );
        l.add( (name+" "+type).trim() );
        l.add( (name+" "+type+" "+suffixDir).trim() );
        return ImmIterator.wrap(l.iterator());
    }

    public String toString() {
        return "Feature:"+fullName();
    }

    /**
     * @return the prefix directory of the feature.
     */
    public String getPrefixDir() {
        return prefixDir;
    }

    /**
     * @return the name of the feature.
     */
    public String getName() {
        return name;
    }
    /**
     * @return the type of the feature.
     */
    public String getType() {
        return type;
    }
    /**
     * @return the suffix directory of the feature.
     */
    public String getSuffixDir() {
        return suffixDir;
    }

} // Feature
