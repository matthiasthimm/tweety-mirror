package net.sf.tweety.preferences.aggregation;

import java.util.List;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import net.sf.tweety.preferences.PreferenceOrder;
import net.sf.tweety.preferences.Relation;
import net.sf.tweety.preferences.ranking.RankingFunction;
import net.sf.tweety.util.Triple;

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
	public PreferenceOrder<T> aggregate(List<PreferenceOrder<T>> input) {
		
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
				while (it.hasNext()){
					PreferenceOrder<T> checkPO = it.next();
					for(T e : checkPO.getDomainElements()){
						if(!elem.containsKey(e)){
							//TODO Exception handling for null pointer exception
							System.out.println("Invalid preference order used");;
						}
					}
				}
			}
			
		}
		
		// for each element in each po the weight vector value is
		// requested
		// and
		// subtracted from the current value in the HashMap
		ListIterator<PreferenceOrder<T>> it2 = input.listIterator();
		while (it2.hasNext()) {
			PreferenceOrder<T> tPO = it2.next();
			Map<T, Integer> temp = tPO.getRankingFunction();
			for (Entry<T, Integer> e : temp.entrySet()) {
				T t = e.getKey();
				Integer i = e.getValue();
				int val = v.getWeight(i);
				elem.put(t, elem.get(t)-val);
			}
		}

		// finally each two elements are compared and set to relation in
		// the
		// final po, if not done yet
		
		// TODO: Merging with RankingFunction line 149.
		for (Entry<T, Integer> f : elem.entrySet()) {
			for (Entry<T, Integer> s : elem.entrySet()) {
				if (!f.getKey().equals(s.getKey())){
					if (f.getValue() < s.getValue()){
						Triple<T, T, Relation> rel = new Triple<T, T, Relation>(f.getKey(), s.getKey(), Relation.LESS);
						tempPO.add(rel);
					} else if (f.getValue() == s.getValue()){
						Triple<T, T, Relation> rel = new Triple<T, T, Relation>(f.getKey(), s.getKey(), Relation.LESS_EQUAL);
						tempPO.add(rel);
					} else
						continue;
				}
			}
		}
		

		return tempPO;
	}
}
