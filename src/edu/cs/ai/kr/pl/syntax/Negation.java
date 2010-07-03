package edu.cs.ai.kr.pl.syntax;

import java.util.*;

/**
 * This class models classical negation of propositional logic.
 * 
 * @author Matthias Thimm
 */
public class Negation extends PropositionalFormula {

	/**
	 * The formula within this negation.
	 */
	private PropositionalFormula formula;
	
	/**
	 * Creates a new negation with the given formula.
	 * @param formula the formula within the negation.
	 */
	public Negation(PropositionalFormula formula){
		this.formula = formula;	
	}
	
	/**
	 * Returns the formula within this negation.
	 * @return the formula within this negation.
	 */
	public PropositionalFormula getFormula(){
		return this.formula;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.pl.syntax.PropositionalFormula#collapseAssociativeFormulas()
	 */
	public PropositionalFormula collapseAssociativeFormulas(){
		return new Negation(this.formula.collapseAssociativeFormulas());
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.pl.syntax.PropositionalFormula#hasLowerBindingPriority(edu.cs.ai.kr.pl.syntax.PropositionalFormula)
	 */
	public boolean hasLowerBindingPriority(PropositionalFormula other){
		// negations have the highest binding priority
		return false;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.pl.syntax.PropositionalFormula#getPropositions()
	 */
	public Set<Proposition> getPropositions(){
		return this.formula.getPropositions();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		if(this.formula instanceof AssociativeFormula || this.formula instanceof Negation)			
			return PropositionalSignature.CLASSICAL_NEGATION + "(" + this.formula + ")";
		return PropositionalSignature.CLASSICAL_NEGATION + this.formula;
	}
}
