package net.sf.tweety.agents.argumentation;

import java.util.LinkedList;

import net.sf.tweety.argumentation.dung.semantics.Extension;

/**
 * Objects of this class represent traces of dialogue, ie. sequences of sets of
 * arguments.
 * 
 * @author Tjitze Rienstra, Matthias Thimm
 *
 */
public class DialogueTrace extends LinkedList<Extension>{

	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Returns all arguments mentioned in this dialogue trace.
	 * @return a set of arguments.
	 */
	public Extension getArguments(){
		Extension arguments = new Extension();
		for(Extension e: this)
			arguments.addAll(e);
		return arguments;
	}
	
	/**
	 * Returns a copy of this trace and adds the given move to this copy.
	 * 
	 * @param newMove Move to add to copy.
	 * @return Copy of this trace, with newMove appended.
	 */
	public DialogueTrace addAndCopy(ExecutableExtension newMove) {
		DialogueTrace trace = new DialogueTrace();
		trace.addAll(this);
		trace.add(newMove);
		return trace;
	}
	
	/* (non-Javadoc)
	 * @see java.util.LinkedList#size()
	 */
	public int size(){
		//return the number of arguments, not the number of moves
		int size = 0;
		for(Extension e: this)
			size += e.size();
		return size;
	}
}
