package net.sf.tweety.preferences.aggregation;

import net.sf.tweety.preferences.Operation;
import net.sf.tweety.preferences.PreferenceOrder;
import net.sf.tweety.preferences.Quadruple;

/**
 * This Update-class provides update-elements used within dynamic preference aggregations
 * @author Bastian Wolf
 *
 * @param <T> the generic element type
 */

public class Update<T> extends Quadruple<PreferenceOrder<T>, Operation, Integer, T> {
	
	/**
	 * The constructor for update-elements
	 * 
	 * @param po the preference operation meant to be changed
	 * @param op the operation that is going to be used (WEAKEN or STRENGTHEN)
	 * @param amount the amount of operations to be used
	 * @param element the element within the preference order to be affected
	 */
	public Update (PreferenceOrder<T> po, Operation op, Integer amount, T element) {
		new Quadruple<PreferenceOrder<T>, Operation, Integer, T>(po, op, amount, element);
	}
	
	
}
