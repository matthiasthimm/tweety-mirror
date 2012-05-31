package net.sf.tweety.preferences;

import java.util.*;

/**
 * This abstract class provides a basic implementation of a generic set of pairs to be used for
 * preference ordering.
 * 
 * @author Bastian Wolf
 * 
 * @param <T> the generic type of objects/pairs in this binary relation
 */

public abstract class BinaryRelation<T> {

	/**
	 * returns whether the elements a and b are related
	 * @param a the first element to be checked
	 * @param b the second element to be checked
	 * @return true if related, false if not.
	 */
	public abstract boolean isRelated(T a, T b);

	/**
	 * returns a set of the single elements in this binary relation
	 */
	public abstract Set<T> computeSingleElements();
	
	/**
	 * checks whether the set is total or not
	 * @return true if total, false otherwise
	 */
	public abstract boolean isTotal();
	
	/**
	 * checks whether the given set is transitive or not
	 * @return true if transitive, false otherwise 
	 */
	public abstract boolean isTransitive();
	

	/**
	 * returns a String with the elements of this set
	 * @return a String with the elements of this set
	 */
	public abstract String toString();
		
	
}
