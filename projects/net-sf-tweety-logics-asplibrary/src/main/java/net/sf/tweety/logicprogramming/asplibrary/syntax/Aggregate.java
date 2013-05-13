package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.tweety.logics.commons.syntax.Term;

/**
 * this class represents an aggregate function. aggregates
 * are functions like sum, times, count over a symbolic set,
 * a set of literals and local variables.
 * 
 * @author Thomas Vengels
 * @author Tim Janus
 *
 */
public class Aggregate implements RuleElement {

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
	public SortedSet<Literal> getLiterals() {
		return new TreeSet<Literal>();
	}

	@Override
	/** @todo implement correctly */
	public RuleElement invert() {
		return null;
	}

	@Override
	public List<Term<?>> getTerms() {
		List<Term<?>> reval = new LinkedList<Term<?>>();
		if(leftGuard != null)
			reval.add(leftGuard);
		if(rightGuard != null)
			reval.add(rightGuard);
		return reval;
	}

	@Override
	public boolean isGround() {
		return false;
	}
	
	@Override
	public Object clone() {
		return new Aggregate(this);
	}
}
