package net.sf.tweety.agents.argumentation.oppmodels;

import java.util.Set;

import net.sf.tweety.agents.argumentation.DialogueTrace;
import net.sf.tweety.argumentation.dung.syntax.*;
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
	public abstract double getUtility(DialogueTrace t);
	
	/** 
	 * Gives the utility of the given dialogue trace that
	 * takes the additional arguments and attacks into account.
	 * @param trace some dialogue trace.
	 * @param additionalArguments a set of arguments that have to 
	 * be taken into account
	 * @param additionalAttacks a set of attacks that have to 
	 * be taken into account
	 * @return the utility of the trace
	 */
	public abstract double getUtility(DialogueTrace t, Set<Argument> additionalArguments, Set<Attack> additionalAttacks);
	
}
