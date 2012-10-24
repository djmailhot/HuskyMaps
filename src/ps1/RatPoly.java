package ps1;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;

/**
 * <b>RatPoly</b> represents an immutable single-variate polynomial expression.
 * RatPolys are sums of RatTerms with non-negative exponents.
 * <p>
 *
 * Examples of RatPolys include "0", "x-10", and "x^3-2*x^2+5/3*x+3", and "NaN".
 */
// See RatNum's documentation for a definition of "immutable".
public final class RatPoly {

    /** Holds all the RatTerms in this RatPoly */
    private final List<RatTerm> terms;

    // Definitions:
    // For a RatPoly p, let C(p,i) be "p.terms.get(i).getCoeff()" and
    // E(p,i) be "p.terms.get(i).getExpt()"
    // length(p) be "p.terms.size()"
    // (These are helper functions that will make it easier for us
    // to write the remainder of the specifications. They are not
    // executable code; they just represent complex expressions in a
    // concise manner, so that we can stress the important parts of
    // other expressions in the spec rather than get bogged down in
    // the details of how we extract the coefficient for the 2nd term
    // or the exponent for the 5th term. So when you see C(p,i),
    // think "coefficient for the ith term in p".)
    //
    // Abstraction Function:
    // RatPoly, p, represents the polynomial equal to the sum of the
    // RatTerms contained in 'terms':
    // sum (0 <= i < length(p)): p.terms.get(i)
    // If there are no terms, then the RatPoly represents the zero
    // polynomial.
    //
    // Representation Invariant for every RatPoly p:
    // terms != null &&
    // forall i such that (0 <= i < length(p)), C(p,i) != 0 &&
    // forall i such that (0 <= i < length(p)), E(p,i) >= 0 &&
    // forall i such that (0 <= i < length(p) - 1), E(p,i) > E(p, i+1)
    // In other words:
    // * The terms field always points to some usable object.
    // * No term in a RatPoly has a zero coefficient.
    // * No term in a RatPoly has a negative exponent.
    // * The terms in a RatPoly are sorted in descending exponent order.
    // (It is implied that 'terms' does not contain any null elements by the
    // above
    // invariant.)

    /** A constant holding a Not-a-Number (NaN) value of type RatPoly */
    public static final RatPoly NaN = new RatPoly(RatTerm.NaN);

    /** A constant holding a zero value of type RatNum */
    public static final RatPoly ZERO = new RatPoly();

    /**
     * @effects Constructs a new Poly, "0".
     */
    public RatPoly() {
        terms = new ArrayList<RatTerm>();
        checkRep();
    }

    /**
     * @requires rt.getExpt() >= 0
     * @effects Constructs a new Poly equal to "rt". If rt.getCoeff() is zero,
     *          constructs a "0" polynomial.
     */
    public RatPoly(RatTerm rt) {
    	List<RatTerm> list = new ArrayList<RatTerm>();
    	if(!rt.getCoeff().equals(RatNum.ZERO)){
    		list.add(rt);
    	}
    	terms = list;
    	checkRep();
    }

    /**
     * @requires e >= 0
     * @effects Constructs a new Poly equal to "c*x^e". If c is zero, constructs
     *          a "0" polynomial.
     */
    public RatPoly(int c, int e) {
    	this(new RatTerm(new RatNum(c), e));
    }

    /**
     * @requires 'rt' satisfies clauses given in rep. invariant
     * @effects Constructs a new Poly using 'rt' as part of the representation.
     *          The method does not make a copy of 'rt'.
     */
    private RatPoly(List<RatTerm> rt) {
        terms = rt;
        // The spec tells us that we don't need to make a copy of 'rt'
        checkRep();
    }

    /**
     * Returns the degree of this RatPoly.
     *
     * @requires !this.isNaN()
     * @return the largest exponent with a non-zero coefficient, or 0 if this is
     *         "0".
     */
    public int degree() {
    	if(terms.size() == 0){
    		return 0;
    	}
    	return terms.get(0).getExpt();    	
    }

    /**
     * Gets the RatTerm associated with degree 'deg'
     *
     * @requires !this.isNaN()
     * @return the RatTerm of degree 'deg'. If there is no term of degree 'deg'
     *         in this poly, then returns the zero RatTerm.
     */
    public RatTerm getTerm(int deg) {
    	for(RatTerm rt: terms){
    		if(rt.getExpt() == deg){
    			return rt;
    		}
    	}
    	return RatTerm.ZERO;
    }

    /**
     * Returns true if this RatPoly is not-a-number.
     *
     * @return true if and only if this has some coefficient = "NaN".
     */
    public boolean isNaN() {
    	for(RatTerm rt: terms){
    		if(rt.isNaN()){
    			return true;
    		}
    	}
    	return false;
    }

    /**
     * Scales coefficients within 'lst' by 'scalar' (helper procedure).
     *
     * @requires lst, scalar != null
     * @modifies lst
     * @effects Forall i s.t. 0 <= i < lst.size(), if lst.get(i) = (C . E) then
     *          lst_post.get(i) = (C*scalar . E)
     * @see ps1.RatTerm regarding (C . E) notation
     */
    private static void scaleCoeff(List<RatTerm> lst, RatNum scalar) {
   		for(int i = 0; i < lst.size(); i++){
    		lst.set(i, lst.get(i).mul(new RatTerm(scalar, 0)));
    	}
    }

    /**
     * Increments exponents within 'lst' by 'degree' (helper procedure).
     *
     * @requires lst != null
     * @modifies lst
     * @effects Forall i s.t. 0 <= i < lst.size(), if (C . E) = lst.get(i) then
     *          lst_post.get(i) = (C . E+degree)
     * @see ps1.RatTerm regarding (C . E) notation
     */
    private static void incremExpt(List<RatTerm> lst, int degree) {
    	for(int i = 0; i < lst.size(); i++){
    		lst.set(i, lst.get(i).mul(new RatTerm(new RatNum(1), degree)));
    		if(lst.get(i).getExpt() < 0){
    			lst.remove(i);
    		}
    	}
    }

    /**
     * Helper procedure: Inserts a term into a sorted sequence of terms,
     * preserving the sorted nature of the sequence. If a term with the given
     * degree already exists, adds their coefficients.
     *
     * Definitions: Let a "Sorted List<RatTerm>" be a List<RatTerm> V such
     * that [1] V is sorted in descending exponent order && [2] there are no two
     * RatTerms with the same exponent in V && [3] there is no RatTerm in V with
     * a coefficient equal to zero
     *
     * For a Sorted List<RatTerm> V and integer e, let cofind(V, e) be either
     * the coefficient for a RatTerm rt in V w    	int b = 0;hose exponent is e, or zero if
     * there does not exist any such RatTerm in V. (This is like the coeff
     * function of RatPoly.) We will write sorted(lst) to denote that lst is a
     * Sorted List<RatTerm>, as defined above.
     *
     * @requires lst != null && sorted(lst)
     * @modifies lst
     * @effects sorted(lst_post) && (cofind(lst_post,newTerm.getExpt()) =
     *          cofind(lst,newTerm.getExpt()) + newTerm.getCoeff())
     */
    private static void sortedInsert(List<RatTerm> lst, RatTerm newTerm) {
    	if(!newTerm.getCoeff().equals(RatNum.ZERO)){
	    	if(!lst.isEmpty()){
		    	int i = 0;
		    	
		    	boolean added = false;
		    	while(i < lst.size() && !added){ // iterate over all elements

		    		if(lst.get(i).getExpt() == newTerm.getExpt()){ //if degrees are equal
		    			
		    			if(lst.get(i).add(newTerm).getCoeff().equals(RatNum.ZERO)){ //if sum == 0
		    				lst.remove(i);
		    			}else{ // set to temp
		    				lst.set(i, lst.get(i).add(newTerm));
		    			}
		    			added = true;
		    			
		    		}else if(lst.get(i).getExpt() < newTerm.getExpt()){
		    			lst.add(i, newTerm);	
		    			added = true;
		    		}

		    		i++;
		    	}
		    	if(!added){
		    		lst.add(newTerm);
		    	}
		    	
		    }else{
	    		lst.add(newTerm);
	    	}
    	}
    }

    /**
     * Return the additive inverse of this RatPoly.
     *
     * @return a RatPoly equal to "0 - this"; if this.isNaN(), returns some r
     *         such that r.isNaN()getTerm(i).equals(RatTerm.NaN) || p.getTerm(i).equals(RatTerm.NaN)){
    		getTerm(i)
    		
    	}
     */
    public RatPoly negate() {
        List<RatTerm> list = new ArrayList<RatTerm>();
        for(RatTerm rt: terms){
        	list.add(rt);
        }
    	scaleCoeff(list, new RatNum(-1));
    	return new RatPoly(list);
    }

    /**
     * Addition operation.
     *
     * @requires p != null
     * @return a RatPoly, r, such that r = "this + p"; if this.isNaN() or
     *         p.isNaN(), returns some r such that r.isNaN()
     */
    public RatPoly add(RatPoly p) {
    	if(this.isNaN() || p.isNaN()){
    		return NaN;
    	}
    	
    	List<RatTerm> list = new ArrayList<RatTerm>();
    	int i = 0;
    	while(i <= degree() || i <= p.degree()){
    		sortedInsert(list, getTerm(i));
    		sortedInsert(list, p.getTerm(i));
    		i++;
    	}
    	return new RatPoly(list);
    }

    /**
     * Subtraction operation.
     *
     * @requires p != null
     * @return a RatPoly, r, such that r = "this - p"; if this.isNaN() or
     *         p.isNaN(), returns some r such that r.isNaN()
     */
    public RatPoly sub(RatPoly p) {
    	return add(p.negate());
    }

    /**
     * Multiplication operation.
     *
     * @requires p != null
     * @return a RatPoly, r, such that r = "this * p"; if this.isNaN() or
     *         p.isNaN(), returns some r such that r.isNaN()
     */
    public RatPoly mul(RatPoly p) {
    	if(this.isNaN() || p.isNaN()){
    		return NaN;
    	}
    	
    	List<RatTerm> list = new ArrayList<RatTerm>();
    	int i = 0;
    	int j = 0;
    	while(i <= degree()){ // for all terms in this.list
    		if(j <= p.degree()){ // iterate through all terms of p.list
    			
    			// take two terms and multiply together.  Add result to list
    			sortedInsert(list, getTerm(i).mul(p.getTerm(j)));
    			j++;
    		}else{ // reset for next this.list term
    			j = 0;
    			i++;
    		}
    	}
    	return new RatPoly(list);
    }

    /**
     * Division operation (truncating).
     *
     * @requires p != null
     * @return a RatPoly, q, such that q = "this / p"; if p = 0 or this.isNaN()
     *         or p.isNaN(), returns some q such that q.isNaN()
     *         <p>
     *
     * Division of polynomials is defined as follows: Given two polynomials u
     * and v, with v != "0", we can divide u by v to obtain a quotient
     * polynomial q and a remainder polynomial r satisfying the condition u = "q *
     * v + r", where the degree of r is strictly less than the degree of v, the
     * degree of q is no greater than the degree of u, and r and q have no
     * negative exponents.
     * <p>
     *
     * For the purposes of this class, the operation "u / v" returns q as
     * defined above.
     * <p>
     *
     * The following are examples of div's behavior: "x^3-2*x+3" / "3*x^2" =
     * "1/3*x" (with r = "-2*x+3"). "x^2+2*x+15 / 2*x^3" = "0" (with r =
     * "x^2+2*x+15"). "x^3+x-1 / x+1 = x^2-x+2 (with r = "-3").
     * <p>
     *
     * Note that this truncating behavior is similar to the behavior of integer
     * division on computers.
     */
    public RatPoly div(RatPoly p) {
    	if(this.isNaN() || p.isNaN() || p.equals(ZERO)){
    		return NaN;
    	}
    	
    	List<RatTerm> list = new ArrayList<RatTerm>();
    	
    	if(!this.equals(ZERO)){
	    	RatPoly remain = new RatPoly(terms);
	    	
	    	while(remain.degree() >= p.degree() && remain.terms.size() != 0){ // while degree of this is greater than divisor
	
	    		//set temp to quotient to highest degree term of remainder divided by h_d_t of dividend
				RatTerm temp = remain.getTerm(remain.degree()).div(p.getTerm(p.degree()));
				//check if degree is less than 0
				if(temp.getExpt() >= 0){ //if not, add to list
	
					//modify r by subtracting (v multiplied by coe and with each term's degree increased by diff)
					remain = remain.sub(p.mul(new RatPoly(temp)));
		    		
					//modify q by adding a term with coefficient coe and of degree diff 
					sortedInsert(list, temp);
	    		}
	    	}
    	}
    	return new RatPoly(list);
    }

    /**
     * Return the derivative of this RatPoly.
     *
     * @return a RatPoly, q, such that q = dy/dx, where this == y. In other
     *         words, q is the derivative of this. If this.isNaN(), then return
     *         some q such that q.isNaN()
     *
     * <p>
     * The derivative of a polynomial is the sum of the derivative of each term.
     */
    public RatPoly differentiate() {
    	if(this.isNaN()){
    		return NaN;
    	}
    	
        List<RatTerm> list = new ArrayList<RatTerm>();
        for(RatTerm rt: terms){
        	list.add(rt);
        }
    	
    	if(!list.isEmpty()){
	    	//remove any zero-degree term
	    	if(list.get(list.size()-1).getExpt() == 0){
	    		list.remove(list.size()-1);
	    	}
	    	
	    	for(int i = 0; i < list.size(); i++){
	    		list.set(i, list.get(i).differentiate());
	    	}
    	}
    	
    	return new RatPoly(list);
    }

    /**
     * Returns the antiderivative of this RatPoly.
     *
     * @requires integrationConstant != null
     * @return a RatPoly, q, such that dq/dx = this and the constant of
     *         integration is "integrationConstant" In other words, q is the
     *         antiderivative of this. If this.isNaN() or
     *         integrationConstant.isNaN(), then return some q such that
     *         q.isNaN()
     *
     * <p>
     * The antiderivative of a polynomial is the sum of the antiderivative of
     * each term plus some constant.
     */
    public RatPoly antiDifferentiate(RatNum integrationConstant) {
        if(this.isNaN()){
        	return NaN;
        }
        
        List<RatTerm> list = new ArrayList<RatTerm>();
        for(RatTerm rt: terms){
        	list.add(rt);
        }
        
        for(int i = 0; i < list.size(); i++){
        	list.set(i, list.get(i).antiDifferentiate());
        }
        // add integration constant as a zero-degree term
        sortedInsert(list, new RatTerm(integrationConstant, 0));
        return new RatPoly(list);
    }

    /**
     * Returns the integral of this RatPoly, integrated from lowerBound to
     * upperBound.
     *
     * <p>
     * The Fundamental Theorem of Calculus states that the definite integral of
     * f(x) with bounds a to b is F(b) - F(a) where dF/dx = f(x) NOTE: Remember
     * that the lowerBound can be higher than the upperBound.
     *
     * @return a double that is the definite integral of this with bounds of
     *         integration between lowerBound and upperBound. If this.isNaN(),
     *         or either lowerBound or upperBound is Double.NaN, return
     *         Double.NaN.
     */
    public double integrate(double lowerBound, double upperBound) {
    	if(this.isNaN() || upperBound == Double.NaN || lowerBound == Double.NaN){
    		return Double.NaN;
    	}
    	
    	List<RatTerm> list = new ArrayList<RatTerm>();
    	
    	for(RatTerm rt: terms){
    		sortedInsert(list, rt.div(new RatTerm(new RatNum(rt.getExpt()+1), 0)));// multiply each term by it's coefficient
    	}
    	incremExpt(list, 1);
    	
    	return (new RatPoly(list).eval(upperBound) - new RatPoly(list).eval(lowerBound));
    }

    /**
     * Returns the value of this RatPoly, evaluated at d.
     *
     * @return the value of this polynomial when evaluated at 'd'. For example,
     *         "x+2" evaluated at 3 is 5, and "x^2-x" evaluated at 3 is 6. if
     *         (this.isNaN() == true), return Double.NaN
     */
    public double eval(double d) {
    	if(this.isNaN()){
    		return Double.NaN;
    	}
    	
    	double sum = 0;
    	for(RatTerm rt: terms){
    		sum += rt.eval(d);
    	}
    	return sum;
    }

    /**
     * Returns a string representation of this RatPoly.
     *
     * @return A String representation of the expression represented by this,
     *         with the terms sorted in order of degree from highest to lowest.
     *         <p>
     *         There is no whitespace in the returned string.
     *         <p>
     *         If the polynomial is itself zero, the returned string will just
     *         be "0".
     *         <p>
     *         If this.isNaN(), then the returned string will be just "NaN"
     *         <p>
     *         The string for a non-zero, non-NaN poly is in the form
     *         "(-)T(+|-)T(+|-)...", where "(-)" refers to a possible minus
     *         sign, if needed, and "(+|-)" refer to either a plus or minus
     *         sign, as needed. For each term, T takes the form "C*x^E" or "C*x"
     *         where C > 0, UNLESS: (1) the exponent E is zero, in which case T
     *         takes the form "C", or (2) the coefficient C is one, in which
     *         case T takes the form "x^E" or "x". In cases were both (1) and
     *         (2) apply, (1) is used.
     *         <p>
     *         Valid example outputs include "x^17-3/2*x^2+1", "-x+1", "-1/2",
     *         and "0".
     *         <p>
     */
    @Override
    public String toString() {
        if (terms.size() == 0) {
            return "0";
        }
        if (isNaN()) {
            return "NaN";
        }
        StringBuilder output = new StringBuilder();
        boolean isFirst = true;
        for (RatTerm rt : terms) {
            if (isFirst) {
                isFirst = false;
                output.append(rt.toString());
            } else {
                if (rt.getCoeff().isNegative()) {
                    output.append(rt.toString());
                } else {
                    output.append("+" + rt.toString());
                }
            }
        }
        return output.toString();
    }

    /**
     * Builds a new RatPoly, given a descriptive String.
     *
     * @requires 'polyStr' is an instance of a string with no spaces that
     *           expresses a poly in the form defined in the toString() method.
     *           <p>
     *
     * Valid inputs include "0", "x-10", and "x^3-2*x^2+5/3*x+3", and "NaN".
     *
     * @return a RatPoly p such that p.toString() = polyStr
     */
    public static RatPoly valueOf(String polyStr) {

        List<RatTerm> parsedTerms = new ArrayList<RatTerm>();

        // First we decompose the polyStr into its component terms;
        // third arg orders "+" and "-" to be returned as tokens.
        StringTokenizer termStrings = new StringTokenizer(polyStr, "+-", true);

        boolean nextTermIsNegative = false;
        while (termStrings.hasMoreTokens()) {
            String termToken = termStrings.nextToken();

            if (termToken.equals("-")) {
                nextTermIsNegative = true;
            } else if (termToken.equals("+")) {
                nextTermIsNegative = false;
            } else {
                // Not "+" or "-"; must be a term
                RatTerm term = RatTerm.valueOf(termToken);

                // at this point, coeff and expt are initialized.
                // Need to fix coeff if it was preceeded by a '-'
                if (nextTermIsNegative) {
                    term = term.negate();
                }

                // accumulate terms of polynomial in 'parsedTerms'
                sortedInsert(parsedTerms, term);
            }
        }
        return new RatPoly(parsedTerms);
    }

    /**
     * Standard hashCode function.
     *
     * @return an int that all objects equal to this will also return.
     */
    @Override
    public int hashCode() {
        // all instances that are NaN must return the same hashcode;
        if (this.isNaN()) {
            return 0;
        }
        return terms.hashCode();
    }

    /**
     * Standard equality operation.
     *
     * @return true if and only if 'obj' is an instance of a RatPoly and 'this'
     *         and 'obj' represent the same rational polynomial. Note that all
     *         NaN RatPolys are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RatPoly) {
            RatPoly rp = (RatPoly) obj;

            // special case: check if both are NaN
            if (this.isNaN() && rp.isNaN()) {
                return true;
            } else {
                return terms.equals(rp.terms);
            }
        } else {
            return false;
        }
    }

    /**
     * Checks that the representation invariant holds (if any).
     */
    // Throws a RuntimeException if the rep invariant is violated.
    private void checkRep() throws RuntimeException {
        if (terms == null) {
            assert (false) : "CheckRep assert";
            throw new RuntimeException("terms == null!");
        }
        for (int i = 0; i < terms.size(); i++) {
            if (terms.get(i).getCoeff().equals(new RatNum(0))) {
                throw new RuntimeException("zero coefficient!");
            }
            if (terms.get(i).getExpt() < 0) {
                throw new RuntimeException("negative exponent!");
            }
            if (i < terms.size() - 1) {
                if (terms.get(i + 1).getExpt() >= terms.get(i).getExpt()) {
                    throw new RuntimeException("terms out of order!");
                }
            }
        }
    }
    
}
