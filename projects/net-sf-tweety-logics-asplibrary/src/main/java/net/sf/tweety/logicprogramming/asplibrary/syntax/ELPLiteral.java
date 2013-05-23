package net.sf.tweety.logicprogramming.asplibrary.syntax;

import net.sf.tweety.logics.commons.syntax.interfaces.Atom;
import net.sf.tweety.logics.commons.syntax.interfaces.Invertable;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * This interface defines common functionality for an ELP literal.
 * literals are atoms or strictly negated atoms.
 * 
 * @author Tim Janus
 * 
 */
public interface ELPLiteral extends ELPElement, Atom, Invertable, Comparable<ELPLiteral> {

	/**
	 * Creates a copy of the literal and adds the
	 * given term as argument to the end of the argument
	 * list.
	 * @param term	the new argument.
	 * @return A copy of the literal containing the given term as new argument.
	 */
	ELPLiteral addTerm(Term<?> term);
	
	/**
	 * @return The atom representing the literal.
	 */
	ELPAtom getAtom();
	
	@Override
	ELPLiteral complement();
	
	@Override
	ELPLiteral substitute(Term<?> v, Term<?> t) throws IllegalArgumentException;
}
