package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.io.StringReader;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.tweety.logicprogramming.asplibrary.parser.ELPParser;
import net.sf.tweety.logics.commons.syntax.Term;
import net.sf.tweety.logics.commons.syntax.Variable;

/**
 * this class models an atom, which is a basic structure for
 * building literals and rules.
 * 
 * @author Thomas Vengels
 * @author Tim Janus
 *
 */
public class Atom implements Literal {

	protected Predicate		pred;
	protected List<Term<?>>	terms = new LinkedList<Term<?>>();
	
	public Atom(Atom other) {
		this.pred = new Predicate(other.getName(), other.getArity());
		for(Term<?> t : other.getTerms()) {
			this.terms.add((Term<?>)t.clone());
		}
	}
	
	public Atom(String symbol, Term<?>... terms) {
		this.pred = new Predicate(symbol, terms.length);
		for(int i=0; i<terms.length; ++i) {
			this.terms.add(terms[i]);
		}
	}
	
	/**
	 * default constructor, create an atom from a functor name
	 * and a list of terms. size of terms determines arity of
	 * functor.
	 * 
	 * @param atomexpr
	 */
	public Atom(String symbol, Collection<Term<?>> terms) {
		this.pred = new Predicate(symbol, terms.size());
		this.terms.addAll(terms);
	}
	
	public Atom(String expr) {
		try {
			ELPParser ep = new ELPParser( new StringReader( expr ));
			Atom a = (Atom)ep.atom();
			this.pred = a.pred;
			this.terms = a.terms;
		} catch (Exception e) {
			System.err.println("Atom: could not parse input!");
			System.err.println(e);
			System.err.println("Input: " + expr);
		}
	}
	
	public static Atom instantiate(String functor, Collection<Term<?>> terms) {
		return new Atom(functor, (terms != null ? terms : new LinkedList<Term<?>>()) );
	}

	@Override
	public Atom getAtom() {
		return this;
	}
	
	public Predicate getPredicate() {
		return this.pred;
	}
	
	public int getArity() {
		return this.pred.getArity();
	}
	
	public List<Term<?>> getTerms() {
		return Collections.unmodifiableList(this.terms);
	}

	@Override 
	public String toString() {
		String ret = "";
		ret += this.pred.getName();
		
		if (terms.size()>0) {
			ret += "(" + this.terms.get(0);
			for (int i = 1; i < terms.size(); i++)
				ret += ", " + this.terms.get(i);
			ret += ")";
		}
		return ret;
	}
	
	public Term<?>	getTerm(int index) {
		if ( (index <0) || (this.terms == null))
			return null;
		if ( index >= this.terms.size())
			return null;
		
		return this.terms.get(index);
	}
	
	@Override
	public Literal addTerm(Term<?> tval)  {
		Atom reval = (Atom)this.clone();
		reval.terms.add(tval);
		reval.pred.arity = reval.terms.size();
		return reval;
	}
	
	public void setTerm(int index, Term<?> tval) {
		if(index >= 0 && index < terms.size()) {
			terms.remove(index);
			terms.add(index, tval);
		}
	}
	
	@Override
	public int hashCode() {
		return this.pred.hashCode() + this.terms.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Atom) {			
			Atom oa = (Atom) o;	
			
			// functors must be equal
			if (!oa.pred.equals( this.pred))
				return false;
			
			if(!oa.terms.equals(terms))
				return false;
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Object clone() {
		return new Atom(this);
	}
	
	public String getName() {
		return pred.getName();
	}

	@Override
	public boolean isGround() {
		if(terms == null)
			return true;
		
		for(Term<?> t : terms) {
			if(t instanceof Variable)
				return false;
			else if(t instanceof Atom) {
				return ((Atom)t).isGround();
			}
		}
		return true;
	}

	@Override
	public RuleElement invert() {
		return new Neg(this);
	}

	@Override
	public SortedSet<Literal> getLiterals() {
		SortedSet<Literal> reval = new TreeSet<Literal>();
		reval.add(this);
		return reval;
	}
}
