package edu.cs.ai.math.term;

import edu.cs.ai.math.NonDifferentiableException;

/**
 * Instances of this class represent application of the logarithm function on some term.
 * 
 * @author Matthias Thimm
 */
public class Logarithm extends FunctionalTerm {

	/**
	 * Creates a new logarithm term for the give inner term. 
	 * @param term a term
	 */
	public Logarithm(Term term) {
		super(term);
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.FunctionalTerm#replaceTerm(edu.cs.ai.math.term.Term, edu.cs.ai.math.term.Term)
	 */
	@Override
	public Term replaceTerm(Term toSubstitute, Term substitution) {
		if(toSubstitute == this)
			return substitution;
		return new Logarithm(this.getTerm().replaceTerm(toSubstitute, substitution));
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.FunctionalTerm#toString()
	 */
	@Override
	public String toString() {
		return "log(" + this.getTerm() + ")";
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#derive(edu.cs.ai.math.term.Variable)
	 */
	public Term derive(Variable v) throws NonDifferentiableException{
		if(!this.getVariables().contains(v)) return new IntegerConstant(0);
		return this.getTerm().derive(v).mult(new Fraction(new IntegerConstant(1),this.getTerm()));
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#simplify()
	 */
	public Term simplify(){
		Term t = this.getTerm().simplify();
		if(t instanceof Constant)
			return new FloatConstant(Math.log(t.doubleValue()));
		return new Logarithm(t);
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#isContinuous(edu.cs.ai.math.term.Variable)
	 */
	public boolean isContinuous(Variable v){
		return this.getTerm().isContinuous(v);		
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.FunctionalTerm#value()
	 */
	@Override
	public Constant value() throws IllegalArgumentException {
		Constant c = this.getTerm().value();
		if(c instanceof IntegerConstant){
			if(((IntegerConstant)c).getValue() <= 0)				
				return new FloatConstant(Float.NEGATIVE_INFINITY);
			else return new FloatConstant(new Float(Math.log(((IntegerConstant)c).getValue())));
		}else if(c instanceof FloatConstant){
			if(((FloatConstant)c).getValue() <= 0)				
				return new FloatConstant(Float.NEGATIVE_INFINITY);
			else return new FloatConstant(new Float(Math.log(((FloatConstant)c).getValue())));
		}
		throw new IllegalArgumentException("Unrecognized atomic term type.");
	}

}
