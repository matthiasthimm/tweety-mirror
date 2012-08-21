package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.io.StringReader;
import java.util.*;

import net.sf.tweety.logicprogramming.asplibrary.parser.*;

/**
 * this class models an atom, which is a basic structure for
 * building literals and rules.
 * 
 * @author Thomas Vengels
 *
 */
public class Atom implements Term, Literal, Predicate {

	protected String	functor;
	protected Term		terms[];
	
	/**
	 * default constructor, create an atom from a functor name
	 * and a list of terms. size of terms determines arity of
	 * functor.
	 * 
	 * @param atomexpr
	 */
	public Atom(String symbol, Term... terms) {
		this.functor = symbol;
		this.terms = terms;		
	}
	
	public Atom(String expr) {
		try {
			ELPParser ep = new ELPParser( new StringReader( expr ));
			Atom a = ep.atom();
			this.functor = a.functor;
			this.terms = a.terms;
		} catch (Exception e) {
			System.err.println("Atom: could not parse input!");
			System.err.println(e);
			System.err.println("Input: " + expr);
		}
	}
	
	protected Atom(String functor, int arity, Term ...terms) {
		this.functor = functor;
		if (arity > 0) {
			this.terms = new Term[arity];
			for (int i = 0; i < arity; i++)
				this.terms[i] = terms[i];
		} else {
			this.terms = null;
		}
	}
	
	protected Atom(String functor, Collection<Term> terms) {
		this.functor = functor;
		if ((terms != null) && (terms.size() > 0))  {
			this.terms = new Term[terms.size()];
			int i = 0;
			Iterator<Term> iter = terms.iterator();
			while (iter.hasNext()) {
				this.terms[i++] = iter.next();
			}
		} else {
			this.terms = null;
		}
	}
	
	public static Atom instantiate(String functor, Collection<Term> terms) {
		return new Atom(functor, terms);
	}
		
	@Override
	public boolean isDefaultNegated() {
		return false;
	}

	@Override
	public boolean isTrueNegated() {
		return false;
	}

	@Override
	public boolean isCondition() {
		return false;
	}

	@Override
	public boolean isWeightLiteral() {
		return false;
	}

	@Override
	public boolean isAggregate() {
		return false;
	}

	@Override
	public Atom getAtom() {
		return this;
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public boolean isAtom() {
		return true;
	}

	@Override
	public boolean isList() {
		return false;
	}

	@Override
	public boolean isSet() {
		return false;
	}

	@Override
	public String getSymbol() {
		return this.functor;
	}

	@Override
	public int getArity() {
		return (this.terms == null)?0:this.terms.length;
	}
	
	public Term[] getTerms() {
		return this.terms;
	}

	@Override public String toString() {
		String ret = "";
		ret += this.functor;
		int ari = this.getArity();
		if (ari>0) {
			ret += "(" + this.terms[0];
			for (int i = 1; i < ari; i++)
				ret += ", " + this.terms[i];
			ret += ")";
		}
		return ret;
	}
	
	public Term	getTerm(int index) {
		if ( (index <0) || (this.terms == null))
			return null;
		if ( index >= this.terms.length)
			return null;
		
		return this.terms[index];
	}
	
	public String getTermStr(int index) {
		Term t = this.getTerm(index);
		if (t != null)
			return t.toString();
		else
			return null;
	}
	
	public int getTermInt(int index) {
		Term t = this.getTerm(index);
		if (t!= null) {			
			if (t.isNumber())
				return t.getInt();
			else 
				return Integer.parseInt(t.toString());
		} else
			return 0;
	}
	
	public void setTerm(int index, String sval) {
		// set a term. if term at index is already
		// a StdTerm, it can hold the string directly
		
		// make sure the term is not null
		if (this.terms[index] == null) {
			this.terms[index] = new StdTerm(sval);
		} else {
			this.terms[index].set(sval);
		}
	}
	
	public void setTerm(int index, int ival) {		
		if (this.terms[index] == null) {
			this.terms[index] = new StdTerm(ival);
		} else {
			this.terms[index].set(ival);
		}
	}
	
	public void setTerm(int index, Term tval) {
		if ((this.terms != null) && (this.terms.length > index))
			this.terms[index] = tval;
	}

	@Override
	public boolean isNumber() {
		return false;
	}

	@Override
	public void set(String value) {
		// not supported
	}

	@Override
	public String get() {
		// not supported
		return null;
	}

	@Override
	public void set(int value) {
	}

	@Override
	public int getInt() {
		// not supported
		return 0;
	}

	@Override
	public boolean isArithmetic() {
		return false;
	}

	@Override
	public boolean isRelational() {
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.functor.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Atom) {			
			Atom oa = (Atom) o;	
			
			// functors must be equal
			if (oa.functor.compareTo( this.functor) != 0)
				return false;
			
			// terms must be equal			
			if ((oa.terms != null) && (this.terms != null)) {
				if (oa.terms.length != this.terms.length)
					return false;
				
				// always stop after first fail
				for (int i = 0; i < this.terms.length; i++) {
					boolean termEq = this.terms[i].equals(oa.terms[i]);
					if (!termEq)
						return false;
				}
				
				// comparison ok, return true
				return true;
			}
			
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * this method rewrites an atom with a new name prefix
	 * and additional terms
	 * 
	 * @param terms additional terms
	 * @return rewritten atom
	 */
	public Atom rewrite(String prefix, Term...terms) {
		int ari = this.getArity();
		int ariNew = ari + terms.length;
		Term tNew[] = new Term[ ariNew ];
		for (int i = 0; i < ari; i++)
			tNew[i] = this.terms[i];
		for (int i = ari; i < ariNew; i++)
			tNew[i] = terms[i-ari];
		return new Atom(prefix+this.functor, tNew);
	}

	@Override
	public boolean isString() {
		return false;
	}

	@Override
	public TermType type() {
		return TermType.Atom;
	}
	
	/**
	 * this method converts a term to a double if
	 * the term is a number or a string constant
	 * representing a double. otherwise, this
	 * method returns 0.0;
	 * 
	 * @param i
	 * @return
	 */
	public double getTermDbl(int i) {
		Term t = this.terms[i];
		
		if (t.isNumber()) {
			return (double) t.getInt();
		}
		else if (t.isString()) {
			String s = t.toString();
			int l = s.length();
			return Double.parseDouble( s.substring(1, l-1) );
		}
		else
			return 0.0;
	}
}