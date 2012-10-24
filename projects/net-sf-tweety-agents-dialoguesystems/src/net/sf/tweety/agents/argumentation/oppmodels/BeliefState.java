package net.sf.tweety.agents.argumentation.oppmodels;

import net.sf.tweety.agents.argumentation.DialogueTrace;
import net.sf.tweety.agents.argumentation.ExecutableExtension;

/**
 * This interface encapsulates the common characteristics of 
 * a belief state for arguing agents.
 * 
 * @author Matthias Thimm
 */
public interface BeliefState {

	/** 
	 * Updates the current belief state accordingly to
	 * the given dialogue trace.
	 * @param trace a dialogue trace
	 */
	public void update(DialogueTrace trace);
	
	/**
	 * Gives the next best move according to the
	 * belief state.
	 * @return a set of arguments.
	 */
	public ExecutableExtension move();
}
