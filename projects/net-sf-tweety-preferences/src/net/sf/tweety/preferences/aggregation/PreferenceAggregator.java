package net.sf.tweety.preferences.aggregation;

import java.util.List;

import net.sf.tweety.preferences.PreferenceOrder;

/**
 * This interface is meant to be used for the aggregation of some generic preference orders
 * 
 * @author Bastian Wolf
 * 
 * @param <T>
 *            generic preference order type
 */

public interface PreferenceAggregator<T> {

	/**
	 * Abstract class for implementation of different aggregation and scoring methods
	 * @param input the array of preference orders to be aggregated
	 * @returns the final result as a preference order
	 */
	public PreferenceOrder<T> aggregate(List<PreferenceOrder<T>> input);

	
}
