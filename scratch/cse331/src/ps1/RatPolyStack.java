package ps1;

import java.util.Stack;
import java.util.Iterator;

/**
 * <b>RatPolyStack</B> is a mutable finite sequence of RatPoly objects.
 * <p>
 * Each RatPolyStack can be described by [p1, p2, ... ], where [] is an empty
 * stack, [p1] is a one element stack containing the Poly 'p1', and so on.
 * RatPolyStacks can also be described constructively, with the append
 * operation, ':'. such that [p1]:S is the result of putting p1 at the front of
 * the RatPolyStack S.
 * <p>
 * A finite sequence has an associated size, corresponding to the number of
 * elements in the sequence. Thus the size of [] is 0, the size of [p1] is 1,
 * the size of [p1, p1] is 2, and so on.
 * <p>
 */
public final class RatPolyStack implements Iterable<RatPoly> {

    private final Stack<RatPoly> polys;

    // Abstraction Function:
    // Each element of a RatPolyStack, s, is mapped to the
    // corrisponding element of polys.
    //
    // RepInvariant:
    // polys != null &&
    // forall i such that (0 <= i < polys.size(), polys.get(i) != null

    /**
     * @effects Constructs a new RatPolyStack, [].
     */
    public RatPolyStack() {
        polys = new Stack<RatPoly>();
        checkRep();
    }

    /**
     * Returns the number of RayPolys in this RatPolyStack.
     *
     * @return the size of this sequence.
     */
    public int size() {
    	return polys.size();
    }

    /**
     * Pushes a RatPoly onto the top of this.
     *
     * @requires p != null
     * @modifies this
     * @effects this_post = [p]:this
     */
    public void push(RatPoly p) {
    	polys.push(p);
    }

    /**
     * Removes and returns the top RatPoly.
     *
     * @requires this.size() > 0
     * @modifies this
     * @effects If this = [p]:S then this_post = S
     * @return p where this = [p]:S
     */
    public RatPoly pop() {
    	return polys.pop();
    }

    /**
     * Duplicates the top RatPoly on this.
     *
     * @requires this.size() > 0
     * @modifies this
     * @effects If this = [p]:S then this_post = [p, p]:S
     */
    public void dup() {
    	RatPoly rp = pop();
    	push(rp);
    	push(rp);
    }

    /**
     * Swaps the top two elements of this.
     *
     * @requires this.size() >= 2
     * @modifies this
     * @effects If this = [p1, p2]:S then this_post = [p2, p1]:S
     */
    public void swap() {
    	RatPoly rp1 = pop();
    	RatPoly rp2 = pop();
    	push(rp1);
    	push(rp2);
    }

    /**
     * Clears the stack.
     *
     * @modifies this
     * @effects this_post = []
     */
    public void clear() {
    	polys.clear();
    }

    /**
     * Returns the RatPoly that is 'index' elements from the top of the stack.
     *
     * @requires index >= 0 && index < this.size()
     * @return If this = S:[p]:T where S.size() = index, then returns p.
     */
    public RatPoly getNthFromTop(int index) {
    	return polys.get(size()-index-1);
    }
    

    /**
     * Pops two elements off of the stack, adds them, and places the result on
     * top of the stack.
     *
     * @requires this.size() >= 2
     * @modifies this
     * @effects If this = [p1, p2]:S then this_post = [p3]:S where p3 = p1 + p2
     */
    public void add() {
    	RatPoly rp1 = pop();
    	RatPoly rp2 = pop();
    	push(rp2.add(rp1));
    }

    /**
     * Subtracts the top poly from the next from top poly, pops both off the
     * stack, and places the result on top of the stack.
     *
     * @requires this.size() >= 2
     * @modifies this
     * @effects If this = [p1, p2]:S then this_post = [p3]:S where p3 = p2 - p1
     */
    public void sub() {
    	RatPoly rp1 = pop();
    	RatPoly rp2 = pop();
    	push(rp2.sub(rp1));
    }

    /**
     * Pops two elements off of the stack, multiplies them, and places the
     * result on top of the stack.
     *
     * @requires this.size() >= 2
     * @modifies this
     * @effects If this = [p1, p2]:S then this_post = [p3]:S where p3 = p1 * p2
     */
    public void mul() {
    	RatPoly rp1 = pop();
    	RatPoly rp2 = pop();
    	push(rp2.mul(rp1));
    }

    /**
     * Divides the next from top poly by the top poly, pops both off the stack,
     * and places the result on top of the stack.
     *
     * @requires this.size() >= 2
     * @modifies this
     * @effects If this = [p1, p2]:S then this_post = [p3]:S where p3 = p2 / p1
     */
    public void div() {
    	RatPoly rp1 = pop();
    	RatPoly rp2 = pop();
    	push(rp2.div(rp1));
    }

    /**
     * Pops the top element off of the stack, differentiates it, and places the
     * result on top of the stack.
     *
     * @requires this.size() >= 1
     * @modifies this
     * @effects If this = [p1]:S then this_post = [p2]:S where p2 = derivative
     *          of p1
     */
    public void differentiate() {
    	push(pop().differentiate());
    }

    /**
     * Pops the top element off of the stack, integrates it, and places the
     * result on top of the stack.
     *
     * @requires this.size() >= 1
     * @modifies this
     * @effects If this = [p1]:S then this_post = [p2]:S where p2 = indefinite
     *          integral of p1 with integration constant 0
     */
    public void integrate() {
    	push(pop().antiDifferentiate(RatNum.ZERO));
    }

    /**
     * Returns an iterator of the elements contained in the stack.
     *
     * @return an iterator of the elements contained in the stack in order from
     *         the bottom of the stack to the top of the stack.
     */
    public Iterator<RatPoly> iterator() {
        return polys.iterator();
    }

    /**
     * Checks that the representation invariant holds (if any).
     */
    // Throws a RuntimeException if the rep invariant is violated.
    private void checkRep() throws RuntimeException {
        if (polys == null) {
            throw new RuntimeException("polys should never be null.");
        }
        for (RatPoly p : polys) {
            if (p == null) {
                throw new RuntimeException(
                        "polys should never contain a null element.");
            }
        }
    }
}
