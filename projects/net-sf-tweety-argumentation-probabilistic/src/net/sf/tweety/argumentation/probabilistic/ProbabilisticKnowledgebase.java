package net.sf.tweety.argumentation.probabilistic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.*;
import net.sf.tweety.argumentation.deductive.DeductiveKnowledgeBase;
import net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;
import net.sf.tweety.math.probability.Probability;
import net.sf.tweety.math.probability.ProbabilityFunction;

/**
 * This class represents a probabilistic knowledge base in the sense of [Hunter, Thimm, 2013, in preparation].
 * 
 * It consists of a (possibly inconsistent) set of propositional formulas (ie. a deductive knowledge base),
 * a set of probability assignments to some set of formulas, and a probability function on the set of sets of
 * arguments contained in the first set.
 *  
 * @author Matthias Thimm
 */
public class ProbabilisticKnowledgebase extends BeliefBase {

	/** The deductive knowledge base. */
	private DeductiveKnowledgeBase kb;
	/** Probability assignments for formulas. */
	private Map<PropositionalFormula,Probability> probabilityAssignments;
	/** Probability function on set of arguments of kb */
	private ProbabilityFunction<Set<DeductiveArgument>> probFunction;
	
	/**
	 * Creates a new empty probabilistic knowledge base
	 */
	public ProbabilisticKnowledgebase() {		
		this(new DeductiveKnowledgeBase(), new HashMap<PropositionalFormula, Probability>(), new ProbabilityFunction<Set<DeductiveArgument>>());
	}
	
	/**
	 * Creates a new probabilistic knowledge base from the given parameters.
	 * @param kb a deductive knowledge base.
	 * @param probabilityAssignments probability assignments for formulas.
	 * @param probFunction probability function on sets of arguments of kb.
	 */
	public ProbabilisticKnowledgebase(DeductiveKnowledgeBase kb, Map<PropositionalFormula, Probability> probabilityAssignments,	ProbabilityFunction<Set<DeductiveArgument>> probFunction) {		
		this.kb = kb;
		this.probabilityAssignments = probabilityAssignments;
		this.probFunction = probFunction;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.BeliefBase#getSignature()
	 */
	@Override
	public Signature getSignature() {
		return this.kb.getSignature();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.BeliefBase#toString()
	 */
	@Override
	public String toString() {
		return "<" + this.kb.toString() + "," + this.probabilityAssignments.toString() + "," + this.probFunction.toString() + ">";
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

	/**
	 * Returns the probability function on sets of arguments
	 * @return the probability function on sets of arguments
	 */
	public ProbabilityFunction<Set<DeductiveArgument>> getProbFunction() {
		return probFunction;
	}

}
