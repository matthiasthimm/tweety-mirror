package net.sf.tweety.agents.structuredargumentationsystems;

import java.util.*;

import net.sf.tweety.agents.*;
import net.sf.tweety.argumentation.dung.*;
import net.sf.tweety.argumentation.dung.syntax.*;
import net.sf.tweety.argumentation.structuredargumentationframeworks.*;
import net.sf.tweety.argumentation.structuredargumentationframeworks.syntax.*;
import net.sf.tweety.logics.propositionallogic.syntax.*;


/**
 * This class models an overcautious argumentation agent, i.e.
 * an agent that only brings forward arguments that cannot be harmful
 * to this agent's goal of proving some given proposition.
 *  
 * @author Matthias Thimm
 */
public class OvercautiousArgumentationAgent extends SasAgent {

	/**
	 * The focal element of this agent.
	 */
	private Proposition focalElement;
	
	/**
	 * Creates a new (non-single-step) agent with the given (local) view and utility function. 
	 * @param view the view of the agent on the argumentation.
	 * @param utility a utility function.
	 * @param focalElement the focal element of this agent.
	 */
	public OvercautiousArgumentationAgent(StructuredArgumentationFramework view, UtilityFunction utility, Proposition focalElement) {
		this(view, utility,false,focalElement);
	}
	
	/**
	 * Creates a new agent with the given (local) view and utility function. 
	 * @param view the view of the agent on the argumentation.
	 * @param utility a utility function.
	 * @param isSingleStep indicates whether this agent is a single-step argumentation agent,
	 * i.e. whether he may bring forward only one argument at a time or multiple.
	 * @param focalElement the focal element of this agent.
	 */
	public OvercautiousArgumentationAgent(StructuredArgumentationFramework view, UtilityFunction utility, boolean isSingleStep, Proposition focalElement){
		super(view,utility,isSingleStep);
		if(!utility.equals(new IndicatorUtilityFunction(focalElement)))
			throw new IllegalArgumentException("The utility function of an overcautious argumentation agent with focal element " + focalElement + " should be an indicator utility function with focal element " + focalElement + ".");
		this.focalElement = focalElement;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.sas.SasAgent#next(java.util.Collection)
	 */
	@Override
	public Executable next(Collection<? extends Perceivable> percepts) {
		super.next(percepts);
		Set<Argument> possibleArguments = this.getPossibleArguments();
		possibleArguments.removeAll(this.attackSet());
		if(possibleArguments.isEmpty())
			return Executable.NO_OPERATION;
		if(this.isSingleStep()){
			//get the first argument and return it
			Set<Argument> result = new HashSet<Argument>();
			result.add(possibleArguments.iterator().next());
			return new ExecutableExtension(result);
		}else{
			return new ExecutableExtension(possibleArguments);
		}
	}
	
	/**
	 * Computes the attack set of this agent's focal element wrt. its view,
	 * i.e. the set of basic arguments "arg" such that there are argumentation
	 * structures "AS1" and "AS2" with "arg" in "AS1", "AS1" indirectly attacks "AS2"
	 * and the claim of "AS2" is the agent's focal element. 
	 * @return the attack set of this agent's focal element wrt. its view.
	 */
	protected Set<Argument> attackSet(){
		DungTheory commonView = this.getCommonView().toDungTheory();
		Set<Argument> attackSet = new HashSet<Argument>();
		for(Argument a : commonView){
			ArgumentStructure arg1 = (ArgumentStructure) a;
			if(arg1.getClaim().equals(this.focalElement)){
				for(Argument b: commonView){
					ArgumentStructure arg2 = (ArgumentStructure) b;
					if(commonView.isIndirectAttack(arg2, arg1)){
						attackSet.addAll(arg2);
					}
				}
			}
		}
		return attackSet;
	}

}
