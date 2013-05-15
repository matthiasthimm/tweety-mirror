package net.sf.tweety.logicprogramming.asplibrary.syntax;

import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * This interface defines common functionality for an ELP literal.
 * literals are atoms or strictly negated atoms.
 * 
 * @author Tim Janus
 * 
 */
public interface Literal extends RuleElement {

	/**
	 * Creates a copy of the literal and adds the
	 * given term as argument to the end of the argument
	 * list.
	 * @param term	the new argument.
	 * @return A copy of the literal containing the given term new argument.
	 */
	Literal addTerm(Term<?> term);
	
	/**
	 * @return The atom representing the literal.
	 */
	Atom getAtom();
}
