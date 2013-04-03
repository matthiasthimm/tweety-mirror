package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.List;
import java.util.SortedSet;

/**
 * This interface defines base methods every rule element of a
 * program has to provide. Rule elements are those constructs which
 * are added to the head or body. 
 * 
 * @author Tim Janus
 */
public interface RuleElement {

	/** @return all the literals used in the rule element */
	SortedSet<Literal> getLiterals();
	
	/** @return an instance which express the logical invertion */
	RuleElement invert();
	
	/**
	 * @return All the terms of the program element
	 */
	List<Term<?>> getTerms();
	
	/**
	 * Checks if the atom is grounded or has variables.
	 * @return	true if no variables are bound in the literal, false otherwise.
	 */
	boolean isGround();
	
	/** @return true if the other object has the same content like this rule element */
	boolean equals(Object other);
	
	/** @return a hash code for the hash code, the hash code of two objects must be #
	 * 			the same if the equals() method returns true for those objects 
	 */
	int hashCode();
	
	/** @return a deep copy of the ProgramElement */
	Object clone();
}
