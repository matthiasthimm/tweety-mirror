package edu.cs.ai.math.term;

import edu.cs.ai.math.NonDifferentiableException;

/**
 * This class represents an exponential expression by "e".
 * 
 * @author Matthias Thimm
 */
public class Exp extends FunctionalTerm {
	
	/**
	 * Creates a new exponential term with the given term and base.
	 * @param term the potentiated term.
	 */
	public Exp(Term term) {
		super(term);
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.FunctionalTerm#replaceTerm(edu.cs.ai.math.term.Term, edu.cs.ai.math.term.Term)
	 */
	@Override
	public Term replaceTerm(Term toSubstitute, Term substitution) {
		return new Exp(this.getTerm().replaceTerm(toSubstitute, substitution));
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.FunctionalTerm#toString()
	 */
	@Override
	public String toString() {
		return "e^(" + this.getTerm() + ")";
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.FunctionalTerm#value()
	 */
	@Override
	public Constant value() throws IllegalArgumentException {
		return new FloatConstant(Math.exp(this.getTerm().doubleValue()));
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#derive(edu.cs.ai.math.term.Variable)
	 */
	@Override
	public Term derive(Variable v) throws NonDifferentiableException {
		Product t = new Product();
		t.addTerm(this.getTerm().derive(v));
		t.addTerm(this);
		return t;
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#isContinuous(edu.cs.ai.math.term.Variable)
	 */
	@Override
	public boolean isContinuous(Variable v) {
		return this.getTerm().isContinuous(v);
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#simplify()
	 */
	@Override
	public Term simplify() {
		return new Exp(this.getTerm().simplify());
	}

}
