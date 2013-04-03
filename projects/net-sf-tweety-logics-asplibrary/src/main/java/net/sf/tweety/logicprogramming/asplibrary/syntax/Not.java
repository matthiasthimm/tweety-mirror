package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

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
public class Not implements RuleElement {

	Literal		lit;

	public Not(Literal inner) {
		this.lit = inner;		
	}
	
	public Not(Not other) {
		this.lit = (Literal)other.lit.clone();
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
	public Object clone() {
		return new Not(this);
	}

	@Override
	public RuleElement invert() {
		return new Not((Literal)lit.invert());
	}

	@Override
	public List<Term<?>> getTerms() {
		return lit.getTerms();
	}

	@Override
	public SortedSet<Literal> getLiterals() {
		SortedSet<Literal> reval = new TreeSet<Literal>();
		reval.add(lit);
		return reval;
	}
}
