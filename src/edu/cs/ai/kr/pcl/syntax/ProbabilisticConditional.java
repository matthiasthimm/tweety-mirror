package edu.cs.ai.kr.pcl.syntax;

import edu.cs.ai.kr.cl.syntax.*;
import edu.cs.ai.kr.pl.syntax.*;
import edu.cs.ai.util.*;

/**
 * This class represents a probabilistic conditional of the form (B|A)[p]
 * with formulas A,B and a probability p.
 * @author Matthias Thimm
 *
 */
public class ProbabilisticConditional extends Conditional {

	/**
	 * The probability of this conditional.
	 */
	private Probability probability;
	
	/**
	 * Creates a new probabilistic conditional with a tautological premise
	 * and given conclusion and probability.
	 * @param conclusion the conclusion (a formula) of this conditional.
	 * @param probability a probability.
	 */
	public ProbabilisticConditional(PropositionalFormula conclusion, Probability probability){
		super(conclusion);
		this.probability = probability;
	}
	
	/**
	 * Creates a new probabilistic conditional with the given premise,
	 * conclusion, and probability.
	 * @param premise the premise (a formula) of this conditional.
	 * @param conclusion the conclusion (a formula) of this conditional.
	 * @param probability a probability.
	 */
	public ProbabilisticConditional(PropositionalFormula premise, PropositionalFormula conclusion, Probability probability){
		super(premise,conclusion);
		this.probability = probability;
	}
	
	/**
	 * Creates a new probabilistic conditional using the given conditional
	 * and probability.
	 * @param conditional a conditional. 
	 * @param probability a probability.
	 */
	public ProbabilisticConditional(Conditional conditional, Probability probability){
		this(conditional.getPremise().iterator().next(),conditional.getConclusion(),probability);
	}
	
	/**
	 * Returns the probability of this conditional.
	 * @return the probability of this conditional.
	 */
	public Probability getProbability(){
		return this.probability;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return super.toString() + "[" + this.probability.getValue() + "]";
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.ClassicalFormula#complement()
	 */
	@Override
	public ProbabilisticConditional complement(){
		return new ProbabilisticConditional(this,this.probability.complement());
	}

}
