package edu.cs.ai.kr.cl.syntax;

import java.util.*;
import edu.cs.ai.kr.*;
import edu.cs.ai.kr.util.rules.*;
import edu.cs.ai.kr.pl.syntax.*;

/**
 * This class represents a basic conditional (B|A) with formulas A,B.
 * @author Matthias Thimm
 */
public class Conditional implements ClassicalFormula, Rule {
	
	/**
	 * The premise of this conditional. 
	 */
	private PropositionalFormula premise;
	
	/**
	 * The conclusion of this conditional.
	 */
	private PropositionalFormula conclusion;
	
	/**
	 * Creates a new conditional with a tautological given premise
	 * andgiven  conclusion.
	 * @param conclusion the conclusion (a formula) of this conditional.
	 */
	public Conditional(PropositionalFormula conclusion){
		this.premise = new Tautology();
		this.conclusion = conclusion;
	}
	
	/**
	 * Creates a new conditional with the given premise
	 * and conclusion.
	 * @param premise the premise (a formula) of this conditional.
	 * @param conclusion the conclusion (a formula) of this conditional.
	 */
	public Conditional(PropositionalFormula premise, PropositionalFormula conclusion){
		this.premise = premise;
		this.conclusion = conclusion;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.util.rules.Rule#getPremise()
	 */
	public Collection<PropositionalFormula> getPremise(){
		HashSet<PropositionalFormula> premiseSet = new HashSet<PropositionalFormula>();
		premiseSet.add(this.premise);
		return premiseSet;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.util.rules.Rule#getConclusion()
	 */
	public PropositionalFormula getConclusion(){
		return this.conclusion;
	}
		
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.Formula#getSignature()
	 */
	public Signature getSignature(){		
		return this.premise.combineWithAnd(this.conclusion).getSignature();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "(" + conclusion + "|" + premise + ")";
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.ClassicalFormula#combineWithAnd(edu.cs.ai.kr.Formula)
	 */
	public Conditional combineWithAnd(ClassicalFormula f){		
		throw new UnsupportedOperationException("Conditionals cannot be combined by 'AND'");		
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.ClassicalFormula#combineWithOr(edu.cs.ai.kr.ClassicalFormula)
	 */
	public Conditional combineWithOr(ClassicalFormula f){
		throw new UnsupportedOperationException("Conditionals cannot be combined by 'OR'");
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.ClassicalFormula#complement()
	 */
	public Conditional complement(){
		return new Conditional(this.premise,(PropositionalFormula)this.conclusion.complement());
	}
	
}
