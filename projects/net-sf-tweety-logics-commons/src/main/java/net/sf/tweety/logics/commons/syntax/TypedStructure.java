package net.sf.tweety.logics.commons.syntax;

import java.util.List;

import net.sf.tweety.logics.commons.error.LanguageException;

/**
 * This interface defines method which are given by every TypedStructure like a
 * Predicate or an Functor. It has an name, an arity (argument count) and a list
 * of Sort representing the types for the different arguments.
 * 
 * @author Tim Janus
 */
public interface TypedStructure {
	
	/** @return the unique name of the structure */
	String getName();
	
	void setName(String name) throws LanguageException;
	
	/** @return the arity of this structure */
	int getArity();
	
	void setArity(int arity) throws LanguageException;
	
	/** 
	 * @return an unmodifiable list which length equals arity if the structure isComplete().
	 */
	List<Sort> getArgumentTypes();
	
	void addArgumentType(Sort argType) throws LanguageException;
	
	Sort removeArgumentType(int index) throws LanguageException;
	
	boolean removeArgumentType(Sort argType) throws LanguageException;
	
	/**
	 * @return true if at least one sort for an argument is not "Thing".
	 */
	boolean isTyped();
	
	/**
	 * @return 	true if the arity of this structure matches the length of it's arguments,
	 * 			false if the arity is bigger than the length of it's arguments.
	 */
	boolean isComplete();
}
