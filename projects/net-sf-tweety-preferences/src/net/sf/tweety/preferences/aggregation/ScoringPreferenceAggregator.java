package net.sf.tweety.preferences.aggregation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import net.sf.tweety.preferences.PreferenceOrder;

/**
 * This class extends the interface for preference aggregation with scoring
 * Scorings are implemented separately.
 * 
 * @author Bastian Wolf
 * 
 * @param <T>
 */

public abstract class ScoringPreferenceAggregator<T> implements
		PreferenceAggregator<T> {

	/**
	 * The weight vector for the aggregator given via the constructor
	 */
	private WeightVector v;

	/**
	 * Constructor with given weight vector
	 * 
	 * @param v
	 *            the weight vector
	 */
	public ScoringPreferenceAggregator(WeightVector v) {
		this.v = v;
	}

	/**
	 * Implementation of the aggregation of a given input-array of sets and a
	 * weight vector
	 * 
	 * @return the final, aggregated preference order
	 */
	public PreferenceOrder<T> aggregate(ArrayList<PreferenceOrder<T>> input) {
		PreferenceOrder<T> tempPO = new PreferenceOrder<T>();
		Map<T, Integer> elem = new HashMap<T, Integer>();

		// all single elements are store in one HashMap
		// note that every input-po only consists of the exact same domain
		// elements
		if (!input.isEmpty()) {

			ListIterator<PreferenceOrder<T>> it = input.listIterator();
			if (it.hasNext()) {
				PreferenceOrder<T> tPO = it.next();

				for (T e : tPO.getDomainElements()) {

					if (!elem.containsKey(e)) {
						elem.put(e, 0);
					} else {
						continue;
					}
					
				}
			}
			
		}
		
		// for each element in each po the weight vector value is
		// requested
		// and
		// added to the current value in the HashMap
		ListIterator<PreferenceOrder<T>> it2 = input.listIterator();
		while (it2.hasNext()) {
			PreferenceOrder<T> tPO = it2.next();
			Map<T, Integer> temp = tPO.getRankingFunction();
			for (Entry<T, Integer> e : temp.entrySet()) {
				T t = e.getKey();
				Integer i = e.getValue();
				int val = v.getWeight(i);
				elem.put(t, elem.get(t)+val);				
			}
		}

		// finally each two elements are compared and set to relation in
		// the
		// final po, if not done yet
		for (Entry<T, Integer> f : elem.entrySet()) {
			for (Entry<T, Integer> s : elem.entrySet()) {
				if (!f.getKey().equals(s.getKey())
						&& (!tempPO.containsPair(f.getKey(), s.getKey()) || !tempPO
								.containsPair(s.getKey(),f.getKey()))) {
					if (f.getValue() >= s.getValue()) {
						tempPO.addPair(f.getKey(),s.getKey());
					} else {
						tempPO.addPair((T) s.getKey(),f.getKey());
					}
				}
			}
		}

		return tempPO;
	}
}
