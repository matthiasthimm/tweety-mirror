package net.sf.tweety.agents.argumentation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import net.sf.tweety.agents.Perceivable;
import net.sf.tweety.argumentation.dung.semantics.Extension;
import net.sf.tweety.argumentation.dung.syntax.Argument;

/**
 * Objects of this class represent traces of dialogue, ie. sequences of sets of
 * arguments.
 * 
 * @author Tjitze Rienstra, Matthias Thimm
 *
 */
public class DialogueTrace extends LinkedList<Extension> implements Perceivable{

	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Returns all arguments mentioned in this dialogue trace.
	 * @return a set of arguments.
	 */
	public Set<Argument> getArguments(){
		Set<Argument> arguments = new HashSet<Argument>();
		for(Extension e: this)
			arguments.addAll(e);
		return arguments;
	}
}
