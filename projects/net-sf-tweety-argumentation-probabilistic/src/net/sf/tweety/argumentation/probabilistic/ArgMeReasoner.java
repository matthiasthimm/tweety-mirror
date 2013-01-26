package net.sf.tweety.argumentation.probabilistic;

import net.sf.tweety.Answer;
import net.sf.tweety.BeliefBase;
import net.sf.tweety.Formula;
import net.sf.tweety.Reasoner;
import net.sf.tweety.logics.propositionallogic.semantics.PossibleWorld;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;
import net.sf.tweety.math.probability.Probability;
import net.sf.tweety.math.probability.ProbabilityFunction;

/**
 * This class implements the ME-reasoning approach from [Hunter, Thimm, 2013, in preparation].
 * 
 * @author Matthias Thimm
 */
public class ArgMeReasoner extends Reasoner {

	/** The ME-distribution this reasoner bases on. */
	private ProbabilityFunction<PossibleWorld> meDistribution = null;
	
	public ArgMeReasoner(BeliefBase beliefBase) {
		super(beliefBase);
		if(!(beliefBase instanceof ProbabilisticKnowledgebase))
			throw new IllegalArgumentException("Knowledge base of class ProbabilisticKnowledgebase expected.");
	}

	/**
	 * Returns the ME-distribution this reasoner bases on.
	 * @return the ME-distribution this reasoner bases on.
	 */
	public ProbabilityFunction<PossibleWorld> getMeDistribution(){
		if(this.meDistribution == null)
			this.meDistribution = this.computeMeDistribution();
		return this.meDistribution;
	}
	
	/**
	 * Computes the ME-distribution this reasoner bases on.
	 * @return the ME-distribution this reasoner bases on.
	 */
	private ProbabilityFunction<PossibleWorld> computeMeDistribution(){
		//TODO
		return null;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.Reasoner#query(net.sf.tweety.Formula)
	 */
	@Override
	public Answer query(Formula query) {
		if(!(query instanceof PropositionalFormula))
			throw new IllegalArgumentException("Reasoning in is only defined for propositional queries.");
		ProbabilityFunction<PossibleWorld> meDistribution = this.getMeDistribution();
		Answer answer = new Answer(this.getKnowledgBase(),query);
		Probability bAnswer = new Probability(0d);
		for(PossibleWorld w: meDistribution.keySet())
			if(w.satisfies(query))
				bAnswer = bAnswer.add(meDistribution.probability(w));
		answer.setAnswer(bAnswer.doubleValue());
		answer.appendText("The answer is: " + bAnswer);
		return answer;
	}
	
}
