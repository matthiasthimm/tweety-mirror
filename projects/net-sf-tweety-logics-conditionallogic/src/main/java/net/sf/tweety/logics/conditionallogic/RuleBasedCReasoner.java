package net.sf.tweety.logics.conditionallogic;


import net.sf.tweety.Answer;
import net.sf.tweety.BeliefBase;
import net.sf.tweety.Formula;
import net.sf.tweety.Reasoner;
import net.sf.tweety.logics.conditionallogic.semantics.ConditionalStructure;
import net.sf.tweety.logics.conditionallogic.semantics.RankingFunction;

public class RuleBasedCReasoner extends Reasoner {
	
	private ClBeliefSet beliefBase;
	
	public RuleBasedCReasoner(BeliefBase beliefBase) {
		super(beliefBase);
		if(! (beliefBase instanceof ClBeliefSet))
			throw new IllegalArgumentException();
		
		this.beliefBase = (ClBeliefSet)beliefBase;
	}
	
	public RankingFunction getSemantic() {
		ConditionalStructure cs = new ConditionalStructure(this.beliefBase);
		
		return null;
	}

	@Override
	public Answer query(Formula query) {
		return null;
	}

}
