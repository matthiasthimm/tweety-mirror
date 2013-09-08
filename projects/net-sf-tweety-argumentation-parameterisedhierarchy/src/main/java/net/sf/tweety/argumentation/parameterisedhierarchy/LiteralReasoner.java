package net.sf.tweety.argumentation.parameterisedhierarchy;

import net.sf.tweety.Answer;
import net.sf.tweety.BeliefBase;
import net.sf.tweety.Formula;
import net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack.AttackStrategy;
import net.sf.tweety.argumentation.parameterisedhierarchy.syntax.Argument;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPLiteral;

/**
 * This class extends the default argumentation reasoner to 
 * @author Sebastian Homann
 *
 */
public class LiteralReasoner extends ArgumentationReasoner {
	
	public LiteralReasoner(BeliefBase beliefBase, AttackStrategy attack, AttackStrategy defence) {
		super(beliefBase, attack, defence);
	}

	@Override
	public Answer query(Formula query) {
		if(! (query instanceof DLPLiteral) ) {
			throw new IllegalArgumentException("Reasoning with parametrerised argumentation is only defined for literals.");
		}
		DLPLiteral literal = (DLPLiteral) query;
		boolean answerValue = false;
		for(Argument arg : super.getJustifiedArguments()) {
			if(arg.getConclusions().contains(literal)) {
				answerValue = true;
			}
		}
		
		Answer answer = new Answer(super.getKnowledgBase(), query);
		answer.setAnswer(answerValue);
		return answer;		
	}
	
	public boolean isOverruled(DLPLiteral arg) {
		return !query(arg).getAnswerBoolean();
	}
	
	public boolean isJustified(DLPLiteral arg) {
		return query(arg).getAnswerBoolean();
	}
}
