package net.sf.tweety.agents.argumentation.oppmodels;

import java.util.Collection;

import net.sf.tweety.agents.Agent;
import net.sf.tweety.agents.Executable;
import net.sf.tweety.agents.Perceivable;
import net.sf.tweety.agents.argumentation.DialogueTrace;
import net.sf.tweety.argumentation.dung.semantics.Extension;
import net.sf.tweety.argumentation.dung.syntax.Argument;

/**
 * This class represent a general arguing agent with an belief state. 
 * @author Matthias Thimm
 *
 */
public class ArguingAgent extends Agent {
	
	/** The belief state of the agent. */
	private BeliefState beliefState;
	/** The faction of the agent. */
	private GroundedGameSystem.AgentFaction faction;
	
	/**
	 * Create a new agent with the given name and belief state.
	 * @param type the type of the agent.
	 * @param beliefState a belief state
	 */
	public ArguingAgent(GroundedGameSystem.AgentFaction faction, BeliefState beliefState) {
		super(faction.toString());
		this.faction = faction;
		this.beliefState = beliefState;
	}

	/**
	 * Returns the faction of the agent.
	 * @return the faction of the agent.
	 */
	public GroundedGameSystem.AgentFaction getFaction(){
		return this.faction;
	}
	
	/**
	 * Returns the belief state of the agent.
	 * @return the belief state of the agent.
	 */
	public BeliefState getBeliefState(){
		return this.beliefState;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.Agent#next(java.util.Collection)
	 */
	@Override
	public Executable next(Collection<? extends Perceivable> percepts) {
		// There should be just a single percept and that should be a dialogue trace
		if(percepts.size()!=1)
			throw new IllegalArgumentException("Only one percept expected.");
		if(!(percepts.iterator().next() instanceof GroundedEnvironment))
			throw new IllegalArgumentException("Object of type GroundedEnvironment expected.");
		GroundedEnvironment env = (GroundedEnvironment)percepts.iterator().next();
		this.beliefState.update(env.getDialogueTrace());		
		return this.beliefState.move(env);
	}
	
	/**
	 * Assess the given dialogue trace with the belief states utility function.
	 * @param trace a dialogue trace
	 * @return the utility of this agent for this dialog trace
	 */
	protected double getUtility(DialogueTrace<Argument,Extension> trace){
		return this.beliefState.getUtilityFunction().getUtility(trace);
	}

}
