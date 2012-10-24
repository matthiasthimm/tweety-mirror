package net.sf.tweety.agents.argumentation.oppmodels;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.agents.Agent;
import net.sf.tweety.agents.Environment;
import net.sf.tweety.agents.Executable;
import net.sf.tweety.agents.Perceivable;
import net.sf.tweety.agents.argumentation.DialogueTrace;
import net.sf.tweety.agents.argumentation.ExecutableExtension;

/**
 * This class models the environment for agents in a grounded
 * argumentation game. It only consists of the current trace of
 * disclosed arguments.
 * 
 * @author Matthias Thimm
 */
public class GroundedEnvironment implements Environment {

	/** The actual dialogue trace. */
	private DialogueTrace trace;
	
	/**
	 * Creates a new grounded environment.
	 */
	public GroundedEnvironment(){
		this.trace = new DialogueTrace();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.Environment#execute(net.sf.tweety.agents.Executable)
	 */
	@Override
	public Set<Perceivable> execute(Executable action) {
		if(!(action instanceof ExecutableExtension))
			throw new IllegalArgumentException("Object of type ExecutableExtension expected");
		this.trace.add((ExecutableExtension)action);
		return this.getPercepts(null);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.Environment#execute(java.util.Collection)
	 */
	@Override
	public Set<Perceivable> execute(Collection<? extends Executable> actions) {
		for(Executable exec: actions){
			if(!(exec instanceof ExecutableExtension))
				throw new IllegalArgumentException("Object of type ExecutableExtension expected");
			this.trace.add((ExecutableExtension)exec);
		}
		return this.getPercepts(null);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.Environment#getPercepts(net.sf.tweety.agents.Agent)
	 */
	@Override
	public Set<Perceivable> getPercepts(Agent agent) {
		Set<Perceivable> percepts = new HashSet<Perceivable>();
		percepts.add(this.trace);
		return percepts;
	}
}
