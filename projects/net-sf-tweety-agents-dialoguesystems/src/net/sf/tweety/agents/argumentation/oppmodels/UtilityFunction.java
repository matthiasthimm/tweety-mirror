package net.sf.tweety.agents.argumentation.oppmodels;

import net.sf.tweety.agents.argumentation.DialogueTrace;

/**
 * Objects of this class represent utility function that assess
 * dialogue traces.
 * 
 * @author Tjitze Rienstra, Matthias Thimm
 */
public abstract class UtilityFunction {

	/** 
	 * Gives the utility of the given dialogue trace.
	 * @param trace some dialogue trace.
	 * @return the utility of the trace
	 */
	public abstract float getUtility(DialogueTrace t);
	
}
