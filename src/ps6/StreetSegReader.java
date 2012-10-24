package ps6;

import ps4.StreetSegment;

import java.io.*;
import java.util.*;

/**
 * A StreetSegReader reads StreetSegments from a set of Tiger Databases.
 *
 * <p> Tiger Databases are produced and maintained by the <a
 * href="http://tiger.census.gov/">U.S. Census Bureau</a>.
 *
 * <p> Tiger Databases are distributed in zip-compressed files.  To
 * allow for the loading of an arbitrary number of these databases,
 * one creates each StreetSegReader with a source directory argument,
 * which will then be searched for Tiger Database files when
 * <code>streetSegments()</code> is called.  The subdirectories of the
 * argument directory are not searched recursively for Tiger files as
 * well; only the immediate contents of this.sourceDirectory will be
 * considered as potential Tiger files.
 *
 * <p> StreetSegReader is only specified to operate correctly with
 * directories that contain no <tt>.zip</tt> files besides the Tiger
 * Databases. Files with other extensions (such as <tt>.pdf</tt>)
 * will not affect the operation of StreetSegReader, but no other
 * <tt>.zip</tt> files should be kept in the source directory for a
 * StreetSegReader.
 *
 * @specfield sourceDirectory : String     // name of directory where Tiger files are located
 * @specfield streetSegs      : Collection // contents of the database; each element is a StreetSegment
 *
 */
public class StreetSegReader
{

    private File sourceDirectory;

    /**
     * @effects Constructs a StreetSegReader where sourceDirectory
     *   contains the Tiger Database files.
     *
     * @throws InvalidSourceException if sourceDirectory is not valid
     * database (most directories, even empty ones, count as valid).
     **/
    public StreetSegReader(String sourceDirectory)
        throws InvalidSourceException
    {
        try {

            File source = null;
            if (sourceDirectory != null) {
                source = new File(sourceDirectory);
            }
            reader = new StreetSegReader(source);

        } catch (StreetSegReader.InvalidSourceException e) {
            throw new InvalidSourceException("" + e.getMessage());
        }
    }

    private StreetSegReader (File sourceDirFile)
        throws InvalidSourceException {

        if (sourceDirFile == null) {
            throw new InvalidSourceException("DIRECTORY for StreetSegReader constructor cannot be null");
        }

        if (!sourceDirFile.isDirectory()) {
            throw new InvalidSourceException(sourceDirFile + " is not a valid DIRECTORY for StreetSegReader");
        }

        // java.io.File is immutable
        this.sourceDirectory = sourceDirFile;
    }

    // our adaptee
    private StreetSegReader reader = null;


    private static final FilenameFilter zipFileFilter = new ZipFilter();

    /**
     * Filter class for only getting certain .zip files -- those that
     * <b>match</b> the name.
     */
    private static class ZipFilter
        implements FilenameFilter {
        public boolean accept(File d, String name) {
            return (name.toLowerCase().endsWith("zip"));
        }
    }


    /**
     * Returns an Iterator over this.streetSegs.
     *
     * @throws Error if IO error while reading the directory
     * @returns an iterator that produces the contents of
     *   this.streetSegs.  Each element produced by the Iterator is a
     *   StreetSegment.
     **/
    public Iterator<StreetSegment> iterator() {
        return reader.streetSegmentsInternal();
    }

    /**
     * @throws Error if IO error while reading the directory
     */
    protected StreetSegIterator streetSegmentsInternal() {
        File[] filesToRead = sourceDirectory.listFiles(zipFileFilter);
        if (filesToRead == null) {
            // Some unknown I/O error ocurred while listing the files.
            // This is rare and probably unrecoverable (sourceDirectory was
            // already checked to be a directory).
            // Not IOException so we don't have to declare it.
            throw new Error("IO error while reading directory " + sourceDirectory);
        }
        StreetSegmentFilter killfilter = KillfileReader.fromDir(sourceDirectory);
        return new StreetSegIterator(Arrays.asList(filesToRead).iterator(), killfilter);
    }

    /**
     * Returns an Iterator over this.streetSegs.
     *
     * @modifies: System.err
     *
     * @effects: If (debug) then prints messages to System.err on
     *   progress of database reading.  Else no change to System.err
     *
     * @throws Error if IO error while reading the directory
     *
     * @returns an iterator that produces the contents of
     *    this.streetSegs.  Each element produced by the Iterator is a
     *    StreetSegment.
     **/
    public Iterator<StreetSegment> streetSegments() {
        StreetSegIterator iter = reader.streetSegmentsInternal();
        return iter;
    }

    // Inner classes

    /**
     * Exception indicating that the requested source for a
     * tiger database is invalid
     **/
    public static class InvalidSourceException
        extends Exception {
        public static final long serialVersionUID = 4534;
        public InvalidSourceException(String msg) { super(msg); }
    }

}
