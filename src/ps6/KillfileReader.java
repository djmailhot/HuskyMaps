package ps6;

import ps4.*;
import java.io.*;
import java.util.*;

import ps2.GeoPoint;

/**
 * Abstraction to read and return filters based on kill files.
 **/
public class KillfileReader
{
    private static boolean debug = false;
    private static void debugln(String s) { if (debug) { System.err.println("KFR: " + s); } }

    /**
     * @return a filter which accepts segments not listed in the given kill file
     **/
    public static StreetSegmentFilter fromFile(File killfile) {
        debugln("Reading " + killfile);
        return new KillfileFilter(killfile);
    }

    /**
     * @return a filter which accepts segments not listed in $(dbdir)/*killfile.txt
     **/
    public static StreetSegmentFilter fromDir(File dbdir) {
        if (!dbdir.isDirectory()) {
            throw new KillfileException("Not a directory: " + dbdir);
        }

        File[] files = dbdir.listFiles(killfile_filter);
        if (files == null) {
            throw new KillfileException("IO error while reading directory " + dbdir);
        }

        if (files.length == 0) {
            return new AllPassStreetSegmentFilter();
        }

        StreetSegmentFilter result = new KillfileFilter(files[0]);
        for (File f : files) {
            StreetSegmentFilter another = new KillfileFilter(f);
            result = new CompositeStreetSegmentFilter(result, another);
        }

        return result;
    }

    /** filter that yields *killfile.txt files */
    private static final FilenameFilter killfile_filter = new FNFilter();
    private static class FNFilter
        implements FilenameFilter {
        /**
         * @param d an arbitrary File
         * @param name the string to apply the filter to
         * @return true iff name ends with "killfile.txt"
         */
        public boolean accept(File d, String name) {
            return (name.toLowerCase().endsWith("killfile.txt"));
        }
    }

    /**
     * Indicates that a killfile was non-existant, malformed, etc.
     **/
    public static class KillfileException
        extends RuntimeException {
        public static final long serialVersionUID = 4534;
        public KillfileException(String s) { super(s); }
    }

    /**
     * Filter that accepts all segments
     **/
    private static class AllPassStreetSegmentFilter
        implements StreetSegmentFilter
    {
        public boolean apply(StreetSegment seg)
        {
            return true;
        }
    }

    /**
     * Filter that composes two other filters, accepting only segments
     * accepted by both of the two filters.
     **/
    private static class CompositeStreetSegmentFilter
        implements StreetSegmentFilter {

        private final StreetSegmentFilter filter1;
        private final StreetSegmentFilter filter2;

        public CompositeStreetSegmentFilter(StreetSegmentFilter filter1, StreetSegmentFilter filter2) {
            this.filter1 = filter1;
            this.filter2 = filter2;
        }

        public boolean apply(StreetSegment seg) {
            return filter1.apply(seg) && filter2.apply(seg);
        }
    }

    /**
     * Filter that filters based on some killfile
     **/
    private static class KillfileFilter
        implements StreetSegmentFilter {

        public boolean apply(StreetSegment seg)
        {
            if (kill.contains(seg)) {
                KillfileReader.debugln("Killed " + seg);
                return false;
            }

            if (limit_one.contains(seg)) {
                KillfileReader.debugln("First hit on " + seg);
                limit_one.remove(seg);
                kill.add(seg);
            }

            return true;
        }

        private final Set<StreetSegment> kill = new HashSet<StreetSegment>();
        private final Set<StreetSegment> limit_one = new HashSet<StreetSegment>();


        // not a real field, but want scope in c-tor and helper
        private String line;
        private String orig_line;

        private KillfileFilter(File killfile) {
            try {

                BufferedReader lines = new BufferedReader(new InputStreamReader(new FileInputStream(killfile)));
                while ((orig_line = line = lines.readLine()) != null) {
                    // e.g. "KILL$(unnamed street)$41271599$-70181563$41273016$-70172648$1$$$     $     $UNKNOWN"

                    String command = nextToken();
                    String name = nextToken();
                    int p1_lat = parseInt(nextToken());
                    int p1_long = parseInt(nextToken());
                    int p2_lat = parseInt(nextToken());
                    int p2_long = parseInt(nextToken());
                    boolean inc = (parseInt(nextToken()) != 0);
                    StreetNumberSet leftNum = makeSNS(nextToken());
                    StreetNumberSet rightNum = makeSNS(nextToken());
                    String leftZip = nextToken();
                    String rightZip = nextToken();
                    StreetClassification streetClass = StreetClassification.parse(nextToken());

                    StreetSegment seg =
                        new StreetSegment(new GeoPoint(p1_lat, p1_long),
                                          new GeoPoint(p2_lat, p2_long),
                                          name,
                                          leftNum,
                                          rightNum,
                                          leftZip,
                                          rightZip,
                                          streetClass,
                                          inc);

                    if ("WARNING".equals(command)) {
                        // for now, just ignore these
                    } else if ("KILL".equals(command)) {
                        kill.add(seg);
                    } else if ("LIMIT_ONE".equals(command)) {
                        limit_one.add(seg);
                    } else {
                        throw new KillfileReader.KillfileException("Unknown command: " + command);
                    }
                }

            } catch (IOException e) {
                throw new KillfileReader.KillfileException(e.getClass() + e.getMessage());
            }
        }

        /**
         * Wraps Integer.parseInt with class-specific exception handling
         * @param num number to parse to an integer
         * @return a string representation of the number
         */
        private int parseInt(String num) {
            try {
                return Integer.parseInt(num);
            } catch (NumberFormatException e) {
                throw new KillfileReader.KillfileException(e.getClass() + e.getMessage() + "'" + orig_line + "'");
            }
        }

        /**
         * Convenience function for creating street number sets
         * @param sns string representation of street number set
         * @return street number set corresponding to sns
         */
        private StreetNumberSet makeSNS(String sns) {
            return new StreetNumberSet(sns);
        }

        private String nextToken() {
            if (line == null) {
                throw new KillfileException("Ran out of tokens");
            }

            String result;
            int n = line.indexOf('$');
            if (n >= 0) {
                result = line.substring(0, n);
                line = line.substring(n+1);
            } else {
                result = line;
                line = null;
            }

            return result;
        }

    }

}
