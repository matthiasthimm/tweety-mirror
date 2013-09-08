package net.sf.tweety.argumentation.parameterisedhierarchy;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Answer;
import net.sf.tweety.BeliefBase;
import net.sf.tweety.Formula;
import net.sf.tweety.Reasoner;
import net.sf.tweety.argumentation.parameterisedhierarchy.semantics.AttackRelation;
import net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack.AttackStrategy;
import net.sf.tweety.argumentation.parameterisedhierarchy.syntax.Argument;

/**
 * This class models a reasoner for extended logic programming based arguments using
 * the fixpoint semantics from [1] parameterised by a notion of attack x for the opponent
 * and another notion of attack y as a defense for the proponent. This base implementation
 * only allows to query whether an argument A is x/y-justified in a ELP P.
 * A is called x/y-acceptable wrt. a set of arguments S if for every argument B in P such 
 * that (B,A) \in x there exists an argument C \in S such that (C,B) \in y. 
 * The set of acceptable arguments for P is defined as the fixpoint J_{P,x/y} of the function
 *  F_{P,x/y}(S) = { A | A is x/y-acceptable with regard to S}
 *  
 *  In [1] it is shown that J_{a/u} equals Dung's grounded semantics, J_{u/su} equals the
 *  well founded semantics for normal logic programs and J_{u/a} equals the well-founded
 *  semantics for logic programs with explicit negation.
 *  
 * 
 * [1] Ralf Schweimeier and Michael Schroeder: A Parameterised Hierarchy of 
 * Argumentation Semantics for Extended Logic Programming and its 
 * Application to the Well-founded Semantics. 
 * In: Theory and Practice of Logic Programming, 5(1-2):207–242, 2003.
 * 
 * @author Sebastian Homann
 */
public class ArgumentationReasoner extends Reasoner {
	private AttackRelation attack;
	private AttackRelation defense;
	
	/**
	 * Creates a new ArgumentationReasoner for the given belief base and parameterised
	 * by a notion of attack for the opponent and another notion of attack for the defense
	 * @param beliefBase
	 * @param attack
	 * @param defence
	 */
	public ArgumentationReasoner(BeliefBase beliefBase, AttackStrategy attack, AttackStrategy defence) {
		super(beliefBase);
		if(!(beliefBase instanceof ArgumentationKnowledgeBase)) {
			throw new IllegalArgumentException("Knowledge base of class ArgumentationKnowledgebase expected.");
		}
		ArgumentationKnowledgeBase kb = (ArgumentationKnowledgeBase) beliefBase;
		this.attack = new AttackRelation(kb, attack);
		this.defense = new AttackRelation(kb, defence);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.Reasoner#query(net.sf.tweety.Formula)
	 */
	@Override
	public Answer query(Formula query) {
		if(! (query instanceof Argument) ) {
			throw new IllegalArgumentException("Formula of class Argument expected.");
		}
		Argument a = (Argument) query;
		Answer answer = new Answer(this.getKnowledgBase(), query);
		answer.setAnswer(getJustifiedArguments().contains(a));
		return answer;
	}
	
	public boolean isOverruled(Argument arg) {
		for(Argument attacker : getJustifiedArguments()) {
			if(attack.attacks(attacker, arg)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isJustified(Argument arg) {
		return query(arg).getAnswerBoolean();
	}
	
	public boolean isDefensible(Argument arg) {
		return (! isOverruled(arg) ) && (! isJustified(arg) );
	}
	
	
	/**
	 * Returns the set of justified arguments using a bottom-up fixpoint calculation
	 * @return
	 */
	public Set<Argument> getJustifiedArguments() {
		ArgumentationKnowledgeBase kb = (ArgumentationKnowledgeBase) this.getKnowledgBase();
		Set<Argument> arguments = kb.getArguments();
		Set<Argument> result = new HashSet<Argument>();
		
		// fixpoint calculation, add defended arguments until nothing changes
		boolean changes = true;
		while(changes) {
			changes = false;
			for(Argument arg : arguments) {
				if(isAcceptable(arguments, result, arg)) {
					if(!result.contains(arg)) {
						result.add(arg);
						changes = true;
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Returns true iff the argument toCheck is x/y-acceptable wrt. the set of defendingArguments.
	 * A is called x/y-acceptable wrt. a set of arguments S if for every argument B in P such 
     * that (B,A) \in x there exists an argument C \in S such that (C,B) \in y.
     *  
	 * @param arguments
	 * @param defendingArguments
	 * @param toCheck
	 * @return true iff toCheck is x/y-acceptable wrt. defendingArguments
	 */
	private boolean isAcceptable(Set<Argument> arguments, Set<Argument> defendingArguments, Argument toCheck) {
		Set<Argument> attackingArguments = attack.getAttackingArguments(toCheck);
		for(Argument attackingArgument : attackingArguments) {
			// check if  there is an argument in defendingArguments, that defends against the attacker
			if(! defense.attacks(defendingArguments, attackingArgument)) {
				// no defense against attacker
				return false;
			}
		}
		return true;
	}

}