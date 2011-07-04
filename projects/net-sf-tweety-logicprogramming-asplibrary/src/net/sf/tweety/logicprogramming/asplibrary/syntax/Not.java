package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * this class models a default negated literal.
 * 
 * @author Thomas Vengels
 *
 */
public class Not implements Literal {

	Literal		lit;

	public Not(Literal inner) {
		this.lit = inner;		
	}
	
	@Override
	public boolean isDefaultNegated() {
		return true;
	}

	@Override
	public boolean isTrueNegated() {
		return lit.isTrueNegated();
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
		return lit.isAggregate();
	}

	@Override
	public Atom getAtom() {
		return lit.getAtom();
	}

	@Override
	public String toString() {
		return "not " + this.lit;
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
	public boolean equals(Object o) {
		if (o instanceof Not) {
			Not on = (Not) o;
			return on.lit.equals(this.lit);
		} else {
			return false;
		}
	}
}
