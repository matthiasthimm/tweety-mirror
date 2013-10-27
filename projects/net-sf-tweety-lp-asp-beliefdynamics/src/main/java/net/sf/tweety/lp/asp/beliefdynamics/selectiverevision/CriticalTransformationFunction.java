package net.sf.tweety.lp.asp.beliefdynamics.selectiverevision;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import net.sf.tweety.arg.lp.ArgumentationKnowledgeBase;
import net.sf.tweety.arg.lp.ArgumentationReasoner;
import net.sf.tweety.arg.lp.semantics.attack.AttackStrategy;
import net.sf.tweety.arg.lp.syntax.Argument;
import net.sf.tweety.beliefdynamics.selectiverevision.MultipleTransformationFunction;
import net.sf.tweety.lp.asp.syntax.Program;
import net.sf.tweety.lp.asp.syntax.Rule;

/**
 * This class represents the critical transformation function
 * for arbitrary extended logic programs as introduced in [1].
 * 
 * [1] Homann, Sebastian:
 *  Master thesis: Argumentationsbasierte selektive Revision von erweiterten logischen
 *  Programmen.
 * 
 * @author Sebastian Homann
 *
 */
public class CriticalTransformationFunction implements
		MultipleTransformationFunction<Rule> {
	
	private Program beliefSet;
	private AttackStrategy attackRelation;
	private AttackStrategy defenseRelation;
	
	/**
	 * Creates a new critical transformation function for literals.
	 * @param beliefSet The belief set used for this transformation function.
	 * @param attackRelation the notion of attack used for attacking arguments
	 * @param defenseRelation the notion of attack used to attack attacking arguments
	 */
	public CriticalTransformationFunction(Collection<Rule> beliefSet, AttackStrategy attackRelation, AttackStrategy defenseRelation) {
		this.beliefSet = new Program(beliefSet);
		this.attackRelation = attackRelation;
		this.defenseRelation = defenseRelation;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.beliefdynamics.selectiverevision.MultipleTransformationFunction#transform(java.util.Collection)
	 */
	@Override
	public Collection<Rule> transform(Collection<Rule> formulas) {
		Set<Rule> result = new HashSet<Rule>();
		Program checkSet = new Program(beliefSet);
		checkSet.addAll(formulas);
		ArgumentationKnowledgeBase argkb = new ArgumentationKnowledgeBase(checkSet);
		ArgumentationReasoner reasoner = new ArgumentationReasoner(argkb, attackRelation, defenseRelation);
		Set<Argument> justifiedArguments = reasoner.getJustifiedArguments();
		
		for(Rule r : formulas) {
			if(isRuleContainedIn(justifiedArguments, r)) {
				result.add(r);
			}
		}
		return result;
	}
	
	/**
	 * "Transforms" the single rule by either accepting or rejecting it.
	 * @param rule a single elp rule
	 * @return the rule if it is acceptable for the given attack-relations and beliefbase, an empty collection otherwise
	 */
	public Collection<Rule> transform(Rule rule) {
		LinkedList<Rule> in = new LinkedList<Rule>();
		in.add(rule);
		return transform(in);
	}
	
	/**
	 * Returns true iff rule is part of some argument in arguments 
	 * @param arguments
	 * @param r
	 * @return true iff rule is part of some argument in arguments 
	 */
	private boolean isRuleContainedIn(Set<Argument> arguments, Rule r) {
		for(Argument a : arguments) {
			if(a.contains(r)) {
				return true;
			}
		}
		return false;
	}
}
