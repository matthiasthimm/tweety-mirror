package net.sf.tweety.agents;

import java.util.*;

/**
 * An agent is a possibly proactive entity situated in some environment.
 * 
 * @author Matthias Thimm
 */
public abstract class Agent {

	/**
	 * Determines the next action of this agent wrt. the given percepts.
	 * @param percepts a collection of percepts.
	 * @return an action.
	 */
	public abstract Executable next(Collection<? extends Perceivable> percepts);
	
}
