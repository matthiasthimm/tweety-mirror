package edu.cs.ai.math.equation;

import edu.cs.ai.math.term.*;

/**
 * This class represent an equation of two terms.
 * @author Matthias Thimm
 */
public class Equation extends Statement{

	/**
	 * Creates a new equation with the given terms.
	 * @param leftTerm a term.
	 * @param rightTerm a term.
	 */
	public Equation(Term leftTerm, Term rightTerm){
		super(leftTerm,rightTerm); 
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.equation.Statement#replaceTerm(edu.cs.ai.math.term.Term, edu.cs.ai.math.term.Term)
	 */
	public Statement replaceTerm(Term toSubstitute, Term substitution){
		return new Equation(this.getLeftTerm().replaceTerm(toSubstitute, substitution),this.getRightTerm().replaceTerm(toSubstitute, substitution));
	}
		
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.equation.Statement#isNormalized()
	 */
	public boolean isNormalized(){
		if(this.getRightTerm() instanceof Constant){
			if(this.getRightTerm() instanceof FloatConstant){
				if(((FloatConstant)this.getRightTerm()).getValue() == 0)
					return true;
			}
			if(this.getRightTerm() instanceof IntegerConstant){
				if(((IntegerConstant)this.getRightTerm()).getValue() == 0)
					return true;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.equation.Statement#toNormalizedForm()
	 */
	public Statement toNormalizedForm(){
		// Check whether it is already normalized
		if(this.isNormalized()) return this;
		// rearrange the terms
		return new Equation(this.getLeftTerm().minus(this.getRightTerm()),new IntegerConstant(0));		
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.equation.Statement#toLinearForm()
	 */
	public Statement toLinearForm(){
		Term left = this.getLeftTerm().toLinearForm();
		Term right = (this.isNormalized())?(this.getRightTerm()):(this.getRightTerm().toLinearForm());
		return new Equation(left,right);
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.equation.Statement#getRelationSymbol()
	 */
	public String getRelationSymbol(){
		return "=";
	}
}
