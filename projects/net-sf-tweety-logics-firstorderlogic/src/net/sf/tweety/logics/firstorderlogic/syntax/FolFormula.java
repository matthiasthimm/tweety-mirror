package net.sf.tweety.logics.firstorderlogic.syntax;

import java.util.*;

import net.sf.tweety.*;


/**
 * The common abstract class for formulas of first-order logic.
 * 
 * NOTE: "RelationalFormula" and "FolFormula" differ in their meaning as follows:
 * <ul>
 * 	<li>A relational formula is any formula over a first-order signature, i.e. even a conditional</li>
 *  <li>A first-order formula is the actual first-order formula in the classical sense.</li>
 * </ul>
 * @author Matthias Thimm
  */
public abstract class FolFormula extends RelationalFormula{	
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.ClassicalFormula#combineWithAnd(net.sf.tweety.kr.Formula)
	 */
	public FolFormula combineWithAnd(ClassicalFormula f){
		if(!(f instanceof FolFormula))
			throw new IllegalArgumentException("The given formula " + f + " is not a first-order formula.");
		return new Conjunction(this,(FolFormula)f);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.ClassicalFormula#combineWithOr(net.sf.tweety.kr.ClassicalFormula)
	 */
	public FolFormula combineWithOr(ClassicalFormula f){
		if(!(f instanceof FolFormula))
			throw new IllegalArgumentException("The given formula " + f + " is not a first-order formula.");
		return new Disjunction(this,(FolFormula)f);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.ClassicalFormula#complement()
	 */
	public FolFormula complement(){
		if(this instanceof Negation) return ((Negation)this).getFormula();
		return new Negation(this);
	}	
	
	/**
	 * Makes a disjunctive normal form of this formula.
	 */
	public FolFormula toDnf(){
		if(this.isDnf()) return this;
		//TODO: implement
		throw new UnsupportedOperationException("Implement me!");
	}
	
	/**
	 * Returns all quantified formulas appearing in this formula.
	 * @return the set of all quantified formulas appearing in this formula.
	 */
	public abstract Set<QuantifiedFormula> getQuantifiedFormulas();
	
	/**
	 * Returns all disjunctions appearing in this formula.
	 * @return the set of all disjunctions appearing in this formula.
	 */
	public abstract Set<Disjunction> getDisjunctions();
	
	/**
	 * Returns all conjunctions appearing in this formula.
	 * @return the set of all conjunctions appearing in this formula.
	 */
	public abstract Set<Conjunction> getConjunctions();
	
	/**
	 * Checks whether this formula is in disjunctive normal form.
	 * @return "true" iff this formula is in disjunctive normal form.
	 */
	public abstract boolean isDnf();
}
