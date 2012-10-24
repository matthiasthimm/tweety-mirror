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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((argument == null) ? 0 : argument.hashCode());
		result = prime * result + ((epsilon == null) ? 0 : epsilon.hashCode());
		result = prime * result + ((theory == null) ? 0 : theory.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GroundedGameUtilityFunction other = (GroundedGameUtilityFunction) obj;
		if (argument == null) {
			if (other.argument != null)
				return false;
		} else if (!argument.equals(other.argument))
			return false;
		if (epsilon == null) {
			if (other.epsilon != null)
				return false;
		} else if (!epsilon.equals(other.epsilon))
			return false;
		if (theory == null) {
			if (other.theory != null)
				return false;
		} else if (!theory.equals(other.theory))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
