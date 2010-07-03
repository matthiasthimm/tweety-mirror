package edu.cs.ai.math.term;

import java.util.HashSet;
import java.util.Set;

/**
 * This class models an abstract constant, e.g. a float or an integer.
 * @author Matthias Thimm
 */
public abstract class Constant extends Term{
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#value()
	 */
	public Constant value(){
		return this;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#getVariables()
	 */
	public Set<Variable> getVariables(){
		return new HashSet<Variable>();
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#getProducts()
	 */
	public Set<Product> getProducts(){
		return new HashSet<Product>();
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#getProducts()
	 */
	public Set<Minimum> getMinimums(){
		return new HashSet<Minimum>();
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#getAbsoluteValues()
	 */
	public Set<AbsoluteValue> getAbsoluteValues(){
		return new HashSet<AbsoluteValue>();
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#collapseAssociativeOperations()
	 */
	public void collapseAssociativeOperations(){
		// do nothing
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#expandAssociativeOperations()
	 */
	public void expandAssociativeOperations(){
		// do nothing
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#simplify()
	 */
	public Term simplify(){
		return this;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#toLinearForm()
	 */
	public Sum toLinearForm() throws IllegalArgumentException{
		Sum sum = new Sum();
		Product p = new Product();
		p.addTerm(this);
		sum.addTerm(p);
		return sum;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#derive(edu.cs.ai.math.term.Variable)
	 */
	public Term derive(Variable v){		
		return new IntegerConstant(0);
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#isContinuous(edu.cs.ai.math.term.Variable)
	 */
	public boolean isContinuous(Variable v){
		return true;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.math.term.Term#replaceTerm(edu.cs.ai.math.term.Term, edu.cs.ai.math.term.Term)
	 */
	public Term replaceTerm(Term toSubstitute, Term substitution){
		if(toSubstitute == this)
			return substitution;
		return this;
	}
}
