package ps6;

import java.io.*;
import java.util.*;

import ps2.GeoPoint;

import ps4.*;
import ps6.tigerdb.*;
import ps6.tigerdb.DatabaseReader.GeoChain;


/**
 * Functions as an iterator over the set of StreetSegments represented
 * by the contents of the .zip files in a directory.
 **/
public class StreetSegIterator
    extends ImmIterator<StreetSegment>
{
    /** When true, zero-length street segments will be removed.
     * Default is true. */
    private boolean filter_zero_length = true;

    /** When true, filtering will be performed as determined by the
     * killfile.  Default is true. */
    private boolean filter_killfile = true;

    /** When true, progress messages are sent to System.err as
     * segments are read.  Default is false. */
    private boolean mention_progress = false;

    /** When true, warning messages are sent to System.err if segments
     * are filtered.  Default is false. */
    private boolean mention_filter = false;

    /** When true, warning messages are sent to System.err if numbers
     * are not disjoint.  Default is false. */
    private boolean mention_non_disjoint = false;

    /**
     * @requires files != null &&
     *           elements of files are of type java.io.File &&
     *           elements of files are .zip files
     *
     * @effects creates a new iterator that produces all segments
     *          from the given files that are accepted by the filter
     **/
    public StreetSegIterator(Iterator<File> files, StreetSegmentFilter filter) {
        this.files = files;
        this.filter = filter;
    }

    private boolean initialized = false;

    private StreetSegmentFilter filter;

    /** .zip files to be read */
    private Iterator<File> files;

    /** chains from the current file */
    private Iterator<GeoChain> chains;

    /** next segment to be returned or null if there are no more */
    private StreetSegment next;

    /** number of segments returned so far */
    private long total = 0;

    public boolean hasNext() {
        if (!initialized) {
            initialized = true;
            next = nextSegment();
        }

        return (next != null);
    }

    public StreetSegment next() {
        // standard iterator behavior
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        // grab the segment to be returned, then advance to the next one
        StreetSegment result = next;
        next = nextSegment();

        // instrument reading process, because it's a bit slow
        total++;
        if (mention_progress && ((total % 10000) == 0)) {
            System.err.println("Returning "+total+"th StreetSegment");
            System.err.flush();
        }

        return result;
    }

    /**
     * @return the next segment from the files (post-filtering), or null if none exist
     */
    private StreetSegment nextSegment() {
        // grab the next chain from the file
        GeoChain chain = nextChain();
        if (chain == null) {
            return null;
        }

        // make a segment from it
        StreetSegment candidate = makeSegment(chain);

        // if segment could not be made, try again
        if (candidate == null) {
            return nextSegment();
        }

        // if segment isn't accepted by the filter, try again
        if (filter_killfile && !filter.apply(candidate)) {
            if (mention_filter) {
                System.err.println("Filtered out: " + candidate);
            }
            return nextSegment();
        }

        // otherwise, it was a good segment
        return candidate;
    }


    /**
     * Retrieve the next GeoChain contained in the file(s)
     *
     * @return the next GeoChain contained in the file(s),
     * or null if there are no more files left
     */
    private GeoChain nextChain() {
        // return a chain if we have one ...
        if (chains != null && chains.hasNext()) {
            return chains.next();
        }

        // else, advance to the next file...
        if (!files.hasNext()) {
            return null;
        }
        File fileToRead = files.next();
        if (mention_progress) {
            System.err.println("Reading from " + fileToRead);
            System.err.flush();
        }

        // ... and open it ...
        try {
            DatabaseReader dr = new DatabaseReader();
            chains = dr.geoChains(fileToRead);
        } catch (IOException ioe) {
            throw new RuntimeException("IOException: " + ioe.getMessage());
        }

        // ... and try again
        return nextChain();
    }

    /**
     * Create a Street Segment from a Geo Chain
     * @param chain
     * @return a segment created from the chain,
     * or null if the segment is not desirable
     */
    private StreetSegment makeSegment(GeoChain chain) {
        GeoPoint p1 = chain.getRT1().getStart();
        GeoPoint p2 = chain.getRT1().getEnd();
        String name = chain.getRT1().getFeature().fullName();

        if (filter_zero_length && p1.equals(p2)) {
            if (mention_filter) {
                System.err.println("Filtered out zero-length segment named " + name);
                System.err.flush();
            }
            return null;
        }

        String lftAddr = chain.getLeftAddresses();
        String rgtAddr = chain.getRightAddresses();
        if (!chain.sidesDisjoint()) {
            if (mention_non_disjoint) {
                System.err.println("Numbers on " + name + " were not disjoint, so were changed to empty sets");
                System.err.flush();
            }
            rgtAddr = lftAddr = "";
        }

        StreetNumberSet leftSns = makeSNS(lftAddr);
        StreetNumberSet rightSns = makeSNS(rgtAddr);

        String leftZip = chain.getRT1().getLeftZip();
        String rightZip = chain.getRT1().getRightZip();

        StreetClassification streetClass = getStreetClass(chain);
        boolean incAddr = areAddressesIncreasing(chain);

        return new StreetSegment(p1, p2, name.intern(), leftSns, rightSns,
                                 leftZip, rightZip, streetClass, incAddr);
    }


    private static final StreetNumberSet EMPTY_SNS = new StreetNumberSet("");
    private static StreetNumberSet makeSNS(String s)
    {
        if (s.length() == 0) return EMPTY_SNS;
        return new StreetNumberSet(s);
    }

    private static StreetClassification getStreetClass(GeoChain gc) {
        String s = gc.getRT1().getCfc().toLowerCase();

        if (s.charAt(0) == 'a' || s.charAt(0) == 'A') {
            switch (s.charAt(1)) {
            case '1':
            case '2':
                return StreetClassification.PRIM_HWY;
            case '3':
                return StreetClassification.SEC_HWY;
            case '4':
                return StreetClassification.LOCAL_ROAD;
            default:
                return StreetClassification.UNKNOWN;
            }
        } else {
            return StreetClassification.UNKNOWN;
        }
    }

    private static boolean areAddressesIncreasing(GeoChain gc) {
        return gc.getRT1().getLeftRange().couldBeLowToHigh();
    }
}
