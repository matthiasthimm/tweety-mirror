package net.sf.tweety.agents.dialogues.oppmodels;

import java.util.Collection;
import java.util.Set;

import net.sf.tweety.agents.dialogues.DialogueTrace;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.*;
/**
 * Objects of this class represent utility function that assess
 * dialogue traces.
 * 
 * @author Tjitze Rienstra, Matthias Thimm
 * @param <S> The type of elements in a move
 * @param <T> The type of moves in this dialgoue trace
 */
public abstract class UtilityFunction<S,T extends Collection<S>> {

	/** 
	 * Gives the utility of the given dialogue trace.
	 * @param trace some dialogue trace.
	 * @return the utility of the trace
	 */
	public abstract double getUtility(DialogueTrace<Argument,Extension> t);
	
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
	public abstract double getUtility(DialogueTrace<Argument,Extension> t, Set<Argument> additionalArguments, Set<Attack> additionalAttacks);
	
}
