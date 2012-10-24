
package ps6.tigerdb;

import java.util.Iterator;

/**
 * ImmIterator.java
 *
 * @author Felix S. Klock
 */

public abstract class ImmIterator<E> implements Iterator<E> {

    public static <F> Iterator<F> wrap(final Iterator<F> i) {
        return new ImmIterator<F>() {
            public boolean hasNext() { return i.hasNext(); }
            public F next() { return i.next(); }
        };
    }
    public void remove() {
        throw new UnsupportedOperationException();
    }

} // ImmIterator
