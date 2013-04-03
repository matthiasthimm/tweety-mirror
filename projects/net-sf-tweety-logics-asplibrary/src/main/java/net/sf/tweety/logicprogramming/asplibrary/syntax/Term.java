package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * This interfaces defines common functionality for
 * terms. A term is any construct appearing as an
 * argument in the constructor based definition of
 * a literal.
 * 
 * @author Tim Janus
 * @author Thomas Vengels
 *
 */
public interface Term<T> {
	
	/**
	 * returns the java-object representation
	 * of the term.
	 * @return
	 */
	T get();
	
	/**
	 * changes the java-object representation 
	 * of the term.
	 * @param value
	 */
	void set(T value);
	
	/**
	 * Copies the data of the term to a new object
	 * @return	The cloned object
	 */
	Object clone();
}