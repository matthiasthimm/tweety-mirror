package net.sf.tweety.logicprogramming.asplibrary.syntax;

import net.sf.tweety.logics.commons.error.LanguageException;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.commons.syntax.Sort;


/**
 * A predicate has a name and an arity. 
 * @author Tim Janus
 */
public class ELPPredicate extends Predicate {
	
	public ELPPredicate(String name) {
		this(name, 0);
	}
	
	public ELPPredicate(String name, int arity) {
		super(name, arity);
	}
	
	@Override
	public void addArgumentType(Sort sort) {
		throw new LanguageException();
	}
}
