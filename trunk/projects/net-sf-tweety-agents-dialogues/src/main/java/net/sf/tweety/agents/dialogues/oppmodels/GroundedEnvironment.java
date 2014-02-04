package net.sf.tweety.agents.dialogues.oppmodels;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.agents.Agent;
import net.sf.tweety.agents.Environment;
import net.sf.tweety.agents.Executable;
import net.sf.tweety.agents.Perceivable;
import net.sf.tweety.agents.dialogues.DialogueTrace;
import net.sf.tweety.agents.dialogues.ExecutableExtension;
import net.sf.tweety.arg.dung.DungTheory;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;

/**
 * This class models the environment for agents in a grounded
 * argumentation game. It consists of the universal Dung theory used
 * for argumentation (but not completely revealed to all agents) and
 * the current trace of disclosed arguments.
 * 
 * @author Matthias Thimm
 */
public class GroundedEnvironment implements Environment, Perceivable {

	/** The current dialogue trace. */
	private DialogueTrace<Argument,Extension> trace;
	/** The universal Dung theory used for argumentation. */
	private DungTheory universalTheory;
	
	/**
	 * Creates a new grounded environment.
	 * @param universalTheory the universal Dung theory used for argumentation.
	 */
	public GroundedEnvironment(DungTheory universalTheory){
		this.trace = new DialogueTrace<Argument,Extension>();
		this.universalTheory = universalTheory;
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
		//this environment is added as percept so that
		//the agent can inquire the necessary information
		//himself.
		Set<Perceivable> percepts = new HashSet<Perceivable>();
		percepts.add(this);
		return percepts;
	}
	
	/**
	 * Returns the current dialogue trace.
	 * @return the current dialogue trace.
	 */
	public DialogueTrace<Argument,Extension> getDialogueTrace(){
		return this.trace;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.agents.Environment#reset()
	 */
	public boolean reset(){
		this.trace = new DialogueTrace<Argument,Extension>();
		return true;
	}
	
	/**
	 * Returns the view of the universal Dung theory restricted to
	 * the given set of arguments.
	 * @param arguments a set of arguments.
	 * @return the projection of the universal theory.
	 */
	public DungTheory getPerceivedDungTheory(Extension arguments){
		return this.universalTheory.getRestriction(arguments);
	}
}