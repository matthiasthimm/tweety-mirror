package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.Set;
import java.util.SortedSet;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * this class models a default negated literal. in answer set
 * programming, the body of a rule is usually composed of a
 * set of positive and negative literals, where this valuation
 * refers to default negation or negation as failure. when
 * implementing a rule, there are two opportunities:
 * - implement the rule with two distinct lists, representing
 *   the sets of positive and negative literals
 * - implement the rule with one set containing super literals,
 *   where a super literal can be positive or strictly negated,
 *   with or without default negation.
 * the library takes the second approach, which allows more
 * flexibility, but comes at the cost that malformed constructs
 * like "not not a" are not intercepted by the library.
 * 
 * @author Tim Janus
 * @author Thomas Vengels
 *
 */
public class Not extends ELPElementAdapter implements ELPElement {

	ELPLiteral		lit;

	public Not(ELPLiteral inner) {
		this.lit = inner;		
	}
	
	public Not(Not other) {
		this.lit = (ELPLiteral)other.lit.clone();
	}

	@Override
	public String toString() {
		return "not " + this.lit;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Not) {
			Not on = (Not) o;
			return on.lit.equals(this.lit);
		} else {
			return false;
		}
	}

	@Override
	public boolean isGround() {
		return lit.isGround();
	}
	
	@Override
	public Not clone() {
		return new Not(this);
	}

	@Override
	public Set<ELPAtom> getAtoms() {
		return lit.getAtoms();
	}

	@Override
	public Set<ELPPredicate> getPredicates() {
		return lit.getPredicates();
	}

	@Override
	public Class<? extends Predicate> getPredicateCls() {
		return ELPPredicate.class;
	}

	@Override
	public ElpSignature getSignature() {
		return lit.getSignature();
	}

	@Override
	public Set<Term<?>> getTerms() {
		return lit.getTerms();
	}

	@Override
	public Not substitute(Term<?> v, Term<?> t)
			throws IllegalArgumentException {
		return new Not(lit.substitute(v,t));
	}

	@Override
	public SortedSet<ELPLiteral> getLiterals() {
		return lit.getLiterals();
	}

	/*
	@Override
	public Not complement() {
		return new Not((Literal)lit.invert());
	}
	*/
}
