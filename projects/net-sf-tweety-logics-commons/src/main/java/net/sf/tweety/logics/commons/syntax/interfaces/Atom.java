package net.sf.tweety.logics.commons.syntax.interfaces;

import java.util.List;

import net.sf.tweety.logics.commons.error.LanguageException;
import net.sf.tweety.logics.commons.syntax.Predicate;

/**
 * An atomic language construct, linked to its predicate
 * 
 * @author Tim Janus
 */
public interface Atom extends SimpleLogicalFormula  {
	/** @return the predicate of the atom */
	Predicate getPredicate();
	
	/**
	 * Adds an argument to the atom's argument list
	 * @param arg	The next argument
	 * @throws LanguageException	If the language does not support
	 * 								arguments for their constructs.
	 */
	void addArgument(Term<?> arg) throws LanguageException;
	
	/** @return A list containing all the arguments of this specific atom */
	List<? extends Term<?>> getArguments();
	
	/** @return true if the size of the argument list is equal to the arity of the predicate */
	boolean isComplete();
}
