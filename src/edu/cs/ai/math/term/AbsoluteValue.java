package edu.cs.ai.math.term;

import java.util.*;

import edu.cs.ai.math.NonDifferentiableException;

/**
 * This class models the absolute value of the inner term.
 * 
 * @author Matthias Thimm
 */
public class AbsoluteValue extends FunctionalTerm {	
	
	/**
	 * Creates a new absolute value term with the given inner term.
	 * @param term a term
	 */
	public AbsoluteValue(Term term){
		super(term);
	}	
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#getAbsoluteValues()
	 */
	@Override
	public Set<AbsoluteValue> getAbsoluteValues(){
		Set<AbsoluteValue> avs = this.getTerm().getAbsoluteValues();
		avs.add(this);
		return avs;
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#replaceTerm(edu.cs.ai.math.term.Term, edu.cs.ai.math.term.Term)
	 */
	@Override
	public Term replaceTerm(Term toSubstitute, Term substitution) {
		if(toSubstitute == this)
			return substitution;
		return new AbsoluteValue(this.getTerm().replaceTerm(toSubstitute, substitution));
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#derive(edu.cs.ai.math.term.Variable)
	 */
	public Term derive(Variable v) throws NonDifferentiableException{
		throw new NonDifferentiableException();
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#simplify()
	 */
	public Term simplify(){
		Term t = this.getTerm().simplify();
		if(t instanceof Constant)
			return new FloatConstant(Math.abs(t.doubleValue()));
		return new AbsoluteValue(t);
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#toString()
	 */
	@Override
	public String toString() {
		return "abs(" + this.getTerm().toString() + ")";
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#isContinuous(edu.cs.ai.math.term.Variable)
	 */
	public boolean isContinuous(Variable v){
		return this.getTerm().isContinuous(v);
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#value()
	 */
	@Override
	public Constant value() throws IllegalArgumentException {
		Constant c = this.getTerm().value();
		if(c instanceof IntegerConstant){
			if(((IntegerConstant)c).getValue() < 0)
				return new IntegerConstant(((IntegerConstant)c).getValue()*-1);
			else return c;
		}else if(c instanceof FloatConstant){
			if(((FloatConstant)c).getValue() < 0)
				return new FloatConstant(((FloatConstant)c).getValue()*-1);
			else return c;
		}
		throw new IllegalArgumentException("Unrecognized atomic term type.");
	}

}
