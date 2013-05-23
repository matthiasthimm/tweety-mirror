package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * this class represents an aggregate function. aggregates
 * are functions like sum, times, count over a symbolic set,
 * a set of literals and local variables.
 * 
 * @todo use an enum for relations instead a string
 * @todo implement complement()
 * 
 * @author Tim Janus
 * @author Thomas Vengels
 */
public class Aggregate extends ELPElementAdapter implements ELPElement {

	protected SymbolicSet	symSet = null;
	protected Term<?> leftGuard = null, rightGuard = null;
	protected String leftRel = null, rightRel = null;
	protected String functor;
	
	public Aggregate(String functor, SymbolicSet symSet) {
		this.functor = (functor);
		this.symSet = symSet;
	}
	
	public Aggregate(Aggregate other) {
		this.functor = other.functor;
		this.leftGuard = (Term<?>)other.leftGuard.clone();
		this.rightGuard = (Term<?>)other.rightGuard.clone();
		this.symSet = (SymbolicSet)other.symSet.clone();
	}
	
	
	public boolean	hasLeftGuard() {
		return	leftGuard != null;
	}
	
	public boolean hasRightGuard() {
		return	rightGuard != null;
	}
	
	public Term<?>	getLeftGuard() {
		return leftGuard;
	}
	
	public String getLeftRel() {
		return leftRel;
	}
	
	public Term<?> getRightGuard() {
		return rightGuard;
	}
	
	public String getRightRel() {
		return rightRel;
	}
	
	public void setLeftGuard(Term<?> guard, String rel) {
		this.leftGuard = guard;
		this.leftRel = rel;
	}
	
	public void setRightGuard(Term<?> guard, String rel) {
		this.rightGuard = guard;
		this.rightRel = rel;
	}
	
	public SymbolicSet getSymbolicSet() {
		return this.symSet;
	}
	
	@Override
	public String toString() {
		String ret = "";
		if (this.leftGuard != null) {
			ret += this.leftGuard + " " + this.leftRel + " ";
		}
		ret += functor + this.symSet;
		if (this.rightGuard != null) {
			ret += " " + this.rightRel + " " + this.rightGuard;
		}
		return ret;
	}

	@Override
	/** @todo implement correctly */
	public SortedSet<ELPLiteral> getLiterals() {
		return new TreeSet<ELPLiteral>();
	}

	@Override
	public boolean isGround() {
		return false;
	}
	
	@Override
	public Aggregate clone() {
		return new Aggregate(this);
	}

	@Override
	public Set<ELPPredicate> getPredicates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Term<?>> getTerms() {
		Set<Term<?>> reval = new HashSet<Term<?>>();
		if(leftGuard != null)
			reval.add(leftGuard);
		if(rightGuard != null)
			reval.add(rightGuard);
		return reval;
	}

	@Override
	public Set<ELPAtom> getAtoms() {
		return new HashSet<ELPAtom>();
	}

	@Override
	public Aggregate substitute(Term<?> t, Term<?> v) {
		Aggregate reval = new Aggregate(this);
		if(t.equals(leftGuard)) {
			reval.leftGuard = v;
		}
		if(t.equals(rightGuard)) {
			reval.rightGuard = v;
		}
		return reval;
	}

	@Override
	public ElpSignature getSignature() {
		ElpSignature reval = new ElpSignature();
		reval.add(leftGuard);
		reval.add(rightGuard);
		return reval;
	}
}
