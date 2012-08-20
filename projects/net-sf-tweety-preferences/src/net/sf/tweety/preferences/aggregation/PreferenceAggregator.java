package net.sf.tweety.preferences.aggregation;

import net.sf.tweety.preferences.PreferenceOrder;


public interface PreferenceAggregator<T> {

	public PreferenceOrder<T> aggregate(PreferenceOrder<T>[] input);
}
