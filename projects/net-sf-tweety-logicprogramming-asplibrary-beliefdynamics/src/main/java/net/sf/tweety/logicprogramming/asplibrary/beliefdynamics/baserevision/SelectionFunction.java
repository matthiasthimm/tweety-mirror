package net.sf.tweety.logicprogramming.asplibrary.beliefdynamics.baserevision;

import java.util.Collection;

import net.sf.tweety.Formula;

/**
 * This interface models a general selection function for remainder sets
 * @author Sebastian Homann
 *
 */
public interface SelectionFunction<T extends Formula> {
	
	/**
	 * Select exactly one remainder set from the set of remainder sets.
	 * 
	 * @param remainderSets a set of remainder sets
	 * @return one remainder set.
	 */
	public Collection<T> select(RemainderSets<T> remainderSets);
}
