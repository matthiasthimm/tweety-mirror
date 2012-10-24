package net.sf.tweety.agents.argumentation.oppmodels;

import java.util.Collection;

import net.sf.tweety.agents.Agent;
import net.sf.tweety.agents.Executable;
import net.sf.tweety.agents.Perceivable;
import net.sf.tweety.agents.argumentation.DialogueTrace;

/**
 * This class represent a general arguing agent with an belief state. 
 * @author Matthias Thimm
 *
 */
public class ArguingAgent extends Agent {

	/** The belief state of the agent. */
	private BeliefState beliefState;
	/**
	 * Create a new agent with the given name and belief state.
	 * @param name some name
	 * @param beliefState a belief state
	 */
	public ArguingAgent(String name, BeliefState beliefState) {
		super(name);
		this.beliefState = beliefState;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.Agent#next(java.util.Collection)
	 */
	@Override
	public Executable next(Collection<? extends Perceivable> percepts) {
		// There should be just a single percept and that should be a dialogue trace
		if(percepts.size()!=1)
			throw new IllegalArgumentException("Only one percept expected.");
		if(!(percepts.iterator().next() instanceof DialogueTrace))
			throw new IllegalArgumentException("Object of type GroundedEnvironment expected.");
		GroundedEnvironment env = (GroundedEnvironment)percepts.iterator().next();
		this.beliefState.update(env.getDialogueTrace());		
		return this.beliefState.move(env);
	}

}
