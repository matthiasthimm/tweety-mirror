package edu.cs.ai.kr.pl.syntax;

import java.util.*;
import edu.cs.ai.kr.*;

/**
 * This class represents the common ancestor for propositional formulae.
 * 
 * @author Matthias Thimm
 */
public abstract class PropositionalFormula implements ClassicalFormula {

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.Formula#getSignature()
	 */
	@Override
	public Signature getSignature() {
		return new PropositionalSignature(this.getPropositions());
	}
	
	/**
	 * Returns the set of propositions that appear in this formula.
	 * @return the set of propositions that appear in this formula.
	 */
	public abstract Set<Proposition> getPropositions();
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.ClassicalFormula#combineWithAnd(edu.cs.ai.kr.ClassicalFormula)
	 */
	public ClassicalFormula combineWithAnd(ClassicalFormula f){
		if(!(f instanceof PropositionalFormula))
			throw new IllegalArgumentException("The given formula " + f + " is not a propositional formula.");
		return new Conjunction(this,(PropositionalFormula)f);
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.ClassicalFormula#combineWithOr(edu.cs.ai.kr.ClassicalFormula)
	 */
	public ClassicalFormula combineWithOr(ClassicalFormula f){
		if(!(f instanceof PropositionalFormula))
			throw new IllegalArgumentException("The given formula " + f + " is not a propositional formula.");
		return new Disjunction(this,(PropositionalFormula)f);
	}
	
	/**
	 * This method collapses all associative operations appearing
	 * in this term, e.g. every a||(b||c) becomes a||b||c.
	 * @return the collapsed formula.
	 */
	public abstract PropositionalFormula collapseAssociativeFormulas();
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.ClassicalFormula#complement()
	 */
	public ClassicalFormula complement(){
		if(this instanceof Negation)
			return ((Negation)this).getFormula();
		return new Negation(this);
	}

}
