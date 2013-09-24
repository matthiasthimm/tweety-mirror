package net.sf.tweety.agents.argumentation;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Objects of this class represent traces of dialogue in an argumentation game,
 * ie. sequences of moves (e.g. sets of arguments or sets of formulas).
 * 
 * @author Tjitze Rienstra, Matthias Thimm
 * @param <S> The type of elements in a move
 * @param <T> The type of moves in this dialgoue trace
 */
public class DialogueTrace<S,T extends Collection<S>> extends LinkedList<T>{

	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Returns all elements mentioned in this dialogue trace.
	 * @return a set of S.
	 */
	public Collection<S> getElements(){
		Collection<S> elements = new HashSet<S>();
		for(T e: this)
			elements.addAll(e);	
		return elements;
	}
	
	/**
	 * Returns a copy of this trace and adds the given move to this copy.
	 * 
	 * @param newMove Move to add to copy.
	 * @return Copy of this trace, with newMove appended.
	 */
	public DialogueTrace<S,T> addAndCopy(T newMove) {
		DialogueTrace<S,T> trace = new DialogueTrace<S,T>();
		trace.addAll(this);
		trace.add(newMove);
		return trace;
	}
	
	/* (non-Javadoc)
	 * @see java.util.LinkedList#size()
	 */
	public int size(){
		//return the number of elements, not the number of moves
		int size = 0;
		for(T e: this)
			size += e.size();
		return size;
	}
}
