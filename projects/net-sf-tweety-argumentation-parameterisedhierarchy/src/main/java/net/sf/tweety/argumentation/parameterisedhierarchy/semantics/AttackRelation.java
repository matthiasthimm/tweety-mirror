package net.sf.tweety.argumentation.parameterisedhierarchy.semantics;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.argumentation.parameterisedhierarchy.ArgumentationKnowledgeBase;
import net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack.AttackStrategy;
import net.sf.tweety.argumentation.parameterisedhierarchy.syntax.Argument;

public class AttackRelation {
	private ArgumentationKnowledgeBase kb;
	private AttackStrategy strategy;
	
	public AttackRelation(ArgumentationKnowledgeBase kb, AttackStrategy strategy) {
		this.kb = kb;
		this.strategy = strategy;
	}
	
	public boolean attacks(Argument a, Argument b) {
		return strategy.attacks(a, b);
	}

	/**
	 * Is true iff at least one attacking argument attacks b
	 * @param attacker a set of arguments
	 * @param b argument to be attacked by one or more arguments from the attacking set
	 * @return true iff at least one argument from attacker attacks b 
	 */
	public boolean attacks(Set<Argument> attacker, Argument b) {
		for(Argument a : attacker) {
			if(attacks(a, b)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns all arguments from the knowledgebase, that attack argument a.
	 * @param a An argument
	 * @return the set of arguments from kb, that attack argument a
	 */
	public Set<Argument> getAttackingArguments(Argument a) {
		Set<Argument> result = new HashSet<Argument>();
		
		for(Argument b : kb.getArguments()) {
			if(attacks(b, a)) {
				result.add(b);
			}
		}
		return result;
	}
	
}
