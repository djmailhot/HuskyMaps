package ps6.tigerdb;

import java.io.*;
import java.util.*;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

public class DatabaseReader {

    public static boolean INFO = false;

    // private Map type1 = new HashMap(); // TLID -> TigerRT1
    private Map<Integer, TigerRT1> type1 = new TreeMap<Integer, TigerRT1>(); // TLID -> TigerRT1
    private Map<Integer, List<TigerRT6>> type6 = new HashMap<Integer, List<TigerRT6>>(); // TLID ->* TigerRT6

    // tracks tlids that we've thrown out as useless for our purposes
    // (b/c their TigerRT1's weren't Roads, they were Rails or
    // something...)
    private Set<Integer> trashTLIDS = new HashSet<Integer>();

    public class GeoChain {
        private final TigerRT1 rt1;
        private final Collection<TigerRT6> rt6s; // Set[TigerRT2]

        private /*@LazyNonNull*/ IntSet leftSet = null; //cached street numbers on the left
        private /*@LazyNonNull*/ IntSet rightSet = null; //cached street numbers on the right

        GeoChain(TigerRT1 rt1, Collection<TigerRT6> rt6s) {
            this.rt1  = rt1;
            this.rt6s = rt6s;
        }
        /**
         * @return the overall Route information (the RT1)
         */
        public TigerRT1 getRT1() {
            return rt1;
        }

        /**
         * @return the small pieces of Route information in a list of RT6s
         */
        public Collection<TigerRT6> getRT6s() {
            return Collections.unmodifiableCollection(rt6s);
        }

        /**
         * Get the set of addresses on the left
         */
        public String getLeftAddresses() {
            cacheLeftAddresses();
            assert leftSet != null : "@SuppressWarnings(nullness)"; //assume cacheLeftAddresses() works
            return leftSet.unparse();
        }

        /**
         * Get the set of addresses on the right
         */
        public String getRightAddresses() {
            cacheRightAddresses();
            assert rightSet != null : "@SuppressWarnings(nullness)"; //assume cacheRightAddresses() works
            return rightSet.unparse();
        }

        //store the addresses on the right, if they havn't yet been stored
        private void cacheRightAddresses() {
            if (rightSet == null) {
                rightSet = getRT1().getRightRange().getSet();
                for (TigerRT6 rt6 : getRT6s()) {
                    rightSet = rightSet.union(rt6.getRightRange().getSet());
                }
            }
        }

        //store the addresses on the left, if they havn't yet been stored
        private void cacheLeftAddresses() {
            if (leftSet == null) {
                leftSet = getRT1().getLeftRange().getSet();
                for (TigerRT6 rt6 : getRT6s()) {
                    leftSet = leftSet.union(rt6.getLeftRange().getSet());
                }
            }
        }

        /**
         * @return true iff the street numbers on the sides of this
         * chain contain no common elements.
         */
        public boolean sidesDisjoint() {
            cacheLeftAddresses();
            cacheRightAddresses();
            assert leftSet != null : "@SuppressWarnings(nullness)"; //assume cacheLeftAddresses() works
            assert rightSet != null : "@SuppressWarnings(nullness)"; //assume cacheRightAddresses() works
            return leftSet.isDisjoint(rightSet);
        }
    }

    /**
     * @effects  Returns an Iterator[GeoChain] over the GeoChains currently
     *           stored in this
     * @requires this is not modified while the returned iterator is
     *          in use
     */
    public Iterator<GeoChain> geoChains() {
        return new ImmIterator<GeoChain>() {
                Iterator</*@KeyFor("type1")*/ Integer> tlids = type1.keySet().iterator();
                public boolean hasNext() {
                    return tlids.hasNext();
                }
                public GeoChain next() {
                    Integer tlid = tlids.next();
                    TigerRT1 rt1 = type1.get(tlid);
                    assert rt1 != null : "@SuppressWarnings(nullness)"; //Guaranteed since tlids is the keyset of type1

                    Collection<TigerRT6> rt6s = type6.containsKey(tlid) ? type6.get(tlid) : new HashSet<TigerRT6>();
                    rt6s = Collections.unmodifiableCollection(rt6s);

                    return new GeoChain(rt1, rt6s);
                }
            };
    }

    public Iterator<GeoChain> geoChains(File zf) throws IOException {
        return geoChains(new ZipFile(zf));
    }
    public Iterator<GeoChain> geoChains(String zf) throws IOException {
        return geoChains(new ZipFile(zf));
    }
    private Iterator<GeoChain> geoChains(ZipFile zf) {
        // prep by reading type6 records first
        try {
            Enumeration<? extends ZipEntry> entries = zf.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().toLowerCase().endsWith("rt6")) {
                    this.readRecords(zf.getInputStream(entry));
                    break;
                }
            }

            entries = zf.entries();
            ZipEntry mainEntry = null;
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().toLowerCase().endsWith("rt1")) {
                    mainEntry = entry;
                    break;
                }
            }
            if (mainEntry == null) {
                return Collections.<GeoChain>emptyList().iterator();
            }
            final LineNumberReader lnr =
                new LineNumberReader
                (new InputStreamReader(zf.getInputStream(mainEntry)));

            // now return the nifty read-and-throw-away GeoChain iterator
            return new ImmIterator<GeoChain>() {
                    /*@Nullable*/ TigerRT1 rt1 = null;
                    private void advance() {
                        while (rt1 == null) {
                            String line;
                            try {
                                line = lnr.readLine();
                            } catch (IOException ioe) {
                                line = null;
                            }
                            if (line == null) {
                                return;
                            }
                            try {
                                rt1 = new TigerRT1(line);
                                assert rt1 != null : "@SuppressWarnings(nullness)";//Guaranteed by constructor

                                if (rt1.getCfc().charAt(0) != 'a' &&
                                    rt1.getCfc().charAt(0) != 'A') {
                                    trashTLIDS.add(new Integer(rt1.getTLID()));
                                    type6.remove(new Integer(rt1.getTLID()));
                                    // System.out.println("1: ("+type1.size()+") Skipping "+rt1);
                                    rt1 = null;
                                }
                            } catch (BadRecordException bre) {
                                // System.out.println("bad record: " + bre.getMessage());
                                rt1 = null;
                            }
                        }
                    }
                    public boolean hasNext() {
                        advance();
                        return (rt1 != null);
                    }

                    public GeoChain next() {
                        if (rt1 == null) {
                            throw new NoSuchElementException();
                        } else {
                            int tlid = rt1.getTLID();

                            GeoChain gc = type6.containsKey(tlid) ? new GeoChain(rt1,type6.get(tlid)) :
                                new GeoChain(rt1,new ArrayList<TigerRT6>());

                            rt1 = null;
                            advance();
                            return gc;
                        }
                    }
                };
        } catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage());
        }
    }

    public DatabaseReader() {

    }

    // checks internal state of this to increase confidence that the
    // data set isn't screwy
    public void checkMappingInv() {
        checkOneMappingInv(type6);
    }

    // checks that every key in typeT has an RT1 in type1
    private void checkOneMappingInv(Map<Integer,?> typeT) {
        for (Integer tlid : typeT.keySet()) {

            assert type1.containsKey(tlid) : "No record found for tlid: " + tlid +
                " with records: +" + typeT.get(tlid);
        }
    }

    public static void main(String[] args) {
        DatabaseReader db = new DatabaseReader();

        // each arg is a ZIP file
        for (int i=0; i<args.length; i++) {
            String zfstr = args[i];
            try {
                db.readZipFile(zfstr);
            } catch (IOException e) {
                System.out.println("IOEXCEPTION?");

            } catch (OutOfMemoryError e) {

                System.out.println("OUT OF MEMORY");

                System.out.println("1 "+db.type1.size());
                System.out.println("6 "+db.type6.size());

                e.printStackTrace();
                System.exit(-1);
            }
        }
        db.checkMappingInv();
    }

    public void readZipFile(File zf) throws IOException {
        readZipFile(new ZipFile(zf));
    }

    public void readZipFile(String zstr) throws IOException {
        readZipFile(new ZipFile(zstr));
        if (INFO)
            System.out.println("finished with "+zstr);
    }

    private void readZipFile(ZipFile zf) throws IOException {
        Enumeration<? extends ZipEntry> entries = zf.entries();
        // build the type6 map for
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            this.readRecords(zf.getInputStream(entry));
        }
        zf.close();
    }

    public void readRecords(InputStream is) throws IOException {
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(is));
        for (String line=lnr.readLine(); line!=null; line=lnr.readLine() ) {
            // System.out.print("*");
            try {
                switch(line.charAt(0)) {
                case '1':
                    TigerRT1 rt1 = new TigerRT1(line);

                    if (rt1.getCfc().charAt(0) != 'a' &&
                        rt1.getCfc().charAt(0) != 'A') {
                        trashTLIDS.add(new Integer(rt1.getTLID()));
                        type6.remove(new Integer(rt1.getTLID()));
                     // Skip record
                    } else {
                        if (type1.containsKey(new Integer(rt1.getTLID())))
                            throw new RuntimeException("1: SOMETHING'S WRONG");
                        //Add record
                        type1.put(new Integer(rt1.getTLID()), rt1);
                    }
                    break;
                case '6':
                    TigerRT6 rt6 = new TigerRT6(line);
                    if (!trashTLIDS.contains(new Integer(rt6.getTLID()))) {
                        //Add record
                        int tlid = rt6.getTLID();
                        if (type6.containsKey(tlid)) {
                            type6.get(tlid).add(rt6);
                        } else {
                            List<TigerRT6> x = new ArrayList<TigerRT6>();
                            x.add(rt6);
                            type6.put(tlid,x);
                        }
                    } else {
                        // Skip record
                    }
                    break;
                default:
                    // Skip record
                }
            } catch (BadRecordException e) {
                // Skip record
            }
        }
    }
}
