package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.tweety.logics.commons.syntax.Term;

/**
 * This class models strict negation for atoms.
 * 
 * @author Tim Janus
 * @author Thomas Vengels
 */
public class Neg implements Literal {

	Atom	atom;
	
	public Neg(Atom inner) {
		this.atom = inner;
	}
	
	public Neg(Neg other) {
		this.atom = (Atom)other.getAtom().clone();
	}
	
	public Neg(String symbol, Term<?>... terms) {
		atom = new Atom(symbol, terms);
	}
	
	/**
	 * default constructor, create an atom from a functor name
	 * and a list of terms. size of terms determines arity of
	 * functor.
	 * 
	 * @param atomexpr
	 */
	public Neg(String symbol, Collection<Term<?>> terms) {
		atom = new Atom(symbol, terms);
	}
	
	public Neg(String expr) {
		atom = new Atom(expr);
	}

	@Override
	public Atom getAtom() {
		return this.atom;
	}
	
	@Override
	public String toString() {
		return "-" + this.atom;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Neg) {
			Neg on = (Neg) o;
			
			// compare atom
			return on.getAtom().equals( this.getAtom() );
		} else {
			return false;
		}
	}

	@Override
	public boolean isGround() {
		return atom.isGround();
	}
	
	@Override
	public Object clone() {
		return new Neg(this);
	}

	@Override
	public List<Term<?>> getTerms() {
		return atom.getTerms();
	}

	@Override
	public RuleElement invert() {
		return atom;
	}

	@Override
	public SortedSet<Literal> getLiterals() {
		SortedSet<Literal> reval = new TreeSet<Literal>();
		reval.add(this);
		return reval;
	}

	@Override
	public Literal addTerm(Term<?> term) {
		Neg reval = (Neg)this.clone();
		reval.atom.terms.add(term);
		reval.atom.pred.arity = reval.atom.terms.size();
		return reval;
	}
	
	@Override 
	public int hashCode() {
		return 7 + atom.hashCode();
	}
}
