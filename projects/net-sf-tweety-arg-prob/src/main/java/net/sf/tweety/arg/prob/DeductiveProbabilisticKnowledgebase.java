package net.sf.tweety.arg.prob;

import java.util.HashMap;
import java.util.Map;

import net.sf.tweety.*;
import net.sf.tweety.arg.deductive.DeductiveKnowledgeBase;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;
import net.sf.tweety.math.probability.Probability;

/**
 * This class represents a probabilistic knowledge base in the sense of [Hunter, Thimm, 2013, in preparation].
 * 
 * It consists of a (possibly inconsistent) set of propositional formulas (ie. a deductive knowledge base)
 * and a set of probability assignments to some set of formulas.
 *  
 * @author Matthias Thimm
 */
public class DeductiveProbabilisticKnowledgebase implements BeliefBase {

	/** The deductive knowledge base. */
	private DeductiveKnowledgeBase kb;
	/** Probability assignments for formulas. */
	private Map<PropositionalFormula,Probability> probabilityAssignments;
	
	/**
	 * Creates a new empty probabilistic knowledge base
	 */
	public DeductiveProbabilisticKnowledgebase() {		
		this(new DeductiveKnowledgeBase(), new HashMap<PropositionalFormula, Probability>());
	}
	
	/**
	 * Creates a new probabilistic knowledge base from the given parameters.
	 * @param kb a deductive knowledge base.
	 * @param probabilityAssignments probability assignments for formulas.
	 */
	public DeductiveProbabilisticKnowledgebase(DeductiveKnowledgeBase kb, Map<PropositionalFormula, Probability> probabilityAssignments) {		
		this.kb = kb;
		this.probabilityAssignments = probabilityAssignments;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.BeliefBase#getSignature()
	 */
	@Override
	public Signature getSignature() {
		PropositionalSignature sig = (PropositionalSignature) this.kb.getSignature();
		for(PropositionalFormula f: this.probabilityAssignments.keySet())
			sig.addSignature(f.getSignature());
		return sig;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.BeliefBase#toString()
	 */
	@Override
	public String toString() {
		return "<" + this.kb.toString() + "," + this.probabilityAssignments.toString() + ">";
	}

	/**
	 * Returns the deductive knowledge base
	 * @return the deductive knowledge base
	 */
	public DeductiveKnowledgeBase getKb() {
		return kb;
	}

	/**
	 * Returns the probability assignments
	 * @return the probability assignments
	 */
	public Map<PropositionalFormula, Probability> getProbabilityAssignments() {
		return probabilityAssignments;
	}

}
