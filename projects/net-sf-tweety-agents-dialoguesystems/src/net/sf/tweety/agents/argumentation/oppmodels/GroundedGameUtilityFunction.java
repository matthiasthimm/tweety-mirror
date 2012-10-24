package net.sf.tweety.agents.argumentation.oppmodels;

import net.sf.tweety.agents.argumentation.DialogueTrace;
import net.sf.tweety.argumentation.dung.DungTheory;
import net.sf.tweety.argumentation.dung.GroundReasoner;
import net.sf.tweety.argumentation.dung.semantics.Extension;
import net.sf.tweety.argumentation.dung.syntax.Argument;

/**
 * The grounded game utility function u_a^g. See definition in paper.
 * This function can either function as the proponent's utility or the
 * opposite opponent's utility.
 * 
 * @author Tjitze Rienstra, Matthias Thimm
 *
 */
public class GroundedGameUtilityFunction extends UtilityFunction {

	/** The type of this utility function. */
	public enum Type {PRO, CONTRA};
	
	/** The argument which is played for or against. */
	private final Argument argument;
	/** The type of this utility function */
	private Type type;
	/** The underlying Dung theory*/ 
	private final DungTheory theory;
	/** The epsilon value. */
	private final Float epsilon = 0.01f;	
	
	/**
	 * Construct utility function.
	 * 
	 * @param theory A Dung theory
	 * @param argument the argument which is played for or against.
	 * @param type the type of this utility function.
	 */
	public GroundedGameUtilityFunction(DungTheory theory, Argument argument, Type type) {
		this.theory = theory;
		this.argument = argument;
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.argumentation.oppmodels.UtilityFunction#getUtility(net.sf.tweety.agents.argumentation.DialogueTrace)
	 */
	@Override
	public float getUtility(DialogueTrace trace) {		
		DungTheory theory = this.theory.getRestriction(trace.getArguments());
		Extension groundedExtension = new GroundReasoner(theory).getExtensions().iterator().next();				
		switch(this.type){
			case PRO:
				if(groundedExtension.contains(this.argument))
					return 1f - (this.epsilon * (float)trace.size());				
				if(theory.isAttacked(this.argument, groundedExtension))
					return -1f - (epsilon * (float)trace.size());				
				break;
			case CONTRA:
				if(groundedExtension.contains(this.argument)) 
					return -1f - (epsilon * (float)trace.size());
				if(theory.isAttacked(this.argument, groundedExtension))
					return 1f - (epsilon * (float)trace.size());			
				break;
		}
		return 0;
	}
}
