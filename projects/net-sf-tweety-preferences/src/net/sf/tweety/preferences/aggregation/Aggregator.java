package net.sf.tweety.preferences.aggregation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.tweety.math.term.Term;
import net.sf.tweety.math.term.Variable;
import net.sf.tweety.preferences.PreferenceOrder;
import net.sf.tweety.util.Pair;

/**
 * This class aggregates given preference orders according to some rules
 * Elements are compared with each other pairwise.
 * 
 * @author Bastian Wolf
 *
 * @param <T> generic preference order type
 */

public class Aggregator<T>{

	/**
	 * static variable for the rule "plurality"
	 */
	public static final int PLURALITY = 0;
	
	/**
	 * static variable for the rule "borda"
	 */
	public static final int BORDA = 1;
	
	/**
	 * static variable for the rule "veto"
	 */
	public static final int VETO = 2;
	
	/**
	 * empty constructor
	 */
	public Aggregator(){
		
	}
	
	/**
	 * aggregation switch
	 * @param input array of preference orders
	 * @param rule constant for the rule to be used for aggregation
	 * @return the final aggregated preference order
	 */
	public PreferenceOrder<T> aggregate(PreferenceOrder<T>[] input, int rule){
		PreferenceOrder<T> out = new PreferenceOrder<T>();
		
		switch (rule) {
		case PLURALITY:
			out = aggregatePlurality(input);
			break;
		case BORDA:
			out = aggregateBorda(input);
			break;
		case VETO:
			out = aggregateVeto(input);
			break;
		default:
			throw new IllegalArgumentException("No valid rule argument");
		}
		
		return out;
	}
	
	
	/**
	 * with the plurality rule, only the best outcome wins
	 * (weight vector (1,0,0,0,...,0))
	 * @param input the preference orders
	 * @return the final aggregated preference order
	 */
	private PreferenceOrder<T> aggregatePlurality(PreferenceOrder<T>[] input) {
		PreferenceOrder<T> pluralitypreference = new PreferenceOrder<T>();
		Set<T> tempSingleElem = new HashSet<T>();

		for (int i = 0; i <= input.length - 1; i++) {
			for (final T e : input[i].getSingleElements()) {
				tempSingleElem.add(e);
			}
		}

		Set<Pair<T, T>> tempPair = new HashSet<Pair<T, T>>();

		for (T f : tempSingleElem) {
			for (T s : tempSingleElem) {
				tempPair.add(new Pair<T, T>(f, s));
				tempPair.add(new Pair<T, T>(s, f));
			}
		}

		for (T f : tempSingleElem) {
			for (T s : tempSingleElem) {
				
				int order1 = 0;
				int order2 = 0;
				
				if (f != s && tempPair.contains(new Pair<T, T>(s, f))
						&& tempPair.contains(new Pair<T, T>(f, s))) {

					for (int i = 0; i <= input.length - 1; i++) {
						
						if (input[i].containsPair(f, s)) {
							order1++;
						} else if (input[i].containsPair(s, f)) {
							order2++;
						} else
							continue;
					}
					
					if (order1 >= order2) {
						pluralitypreference.addPair(f, s);
					} else {
						pluralitypreference.addPair(s, f);
					}
					
					tempPair.remove(new Pair<T, T>(f, s));
					tempPair.remove(new Pair<T, T>(s, f));
				}
			}
		}
		
		return pluralitypreference;
	}
	
	
	/**
	 * the borda rule weights every preference order with the
	 * weight vector (n-1, n-2, n-3, ..., 0)
	 * @param input the preference orders
	 * @return the final aggregated preference order
	 */
	private PreferenceOrder<T> aggregateBorda(PreferenceOrder<T>[] input) {
		PreferenceOrder<T> bordapreference = new PreferenceOrder<T>();
		Set<T> tempSingleElem = new HashSet<T>();

		for (int i = 0; i <= input.length - 1; i++) {
			for (final T e : input[i].getSingleElements()) {
				tempSingleElem.add(e);
			}
		}

		for (T f : tempSingleElem) {
			for (T s : tempSingleElem) {
				if (f != s) {
					
					int rankf = 0;
					int ranks = 0;

					int rankSumF = 0;
					int rankSumS = 0;

					for (int i = 0; i < input.length - 1; i++) {
						
						Set<Entry<Variable, Term>> ent = input[i]
								.computeRankingFunction().getRankingFunction()
								.entrySet();
						
						Iterator<Entry<Variable, Term>> entIt = ent.iterator();
						
						while (entIt.hasNext()) {
							
							Entry<Variable, Term> tempEnt = entIt.next();
							
							if (tempEnt.getKey().toString()
									.equals(f.toString())) {
								rankf = (int) tempEnt.getValue().doubleValue();
							}
							
							if (tempEnt.getKey().toString()
									.equals(s.toString())) {
								ranks = (int) tempEnt.getValue().doubleValue();
							}
						}
						
						rankSumF += rankf;
						rankSumS += ranks;
					}
					
					if (rankSumF <= rankSumS) {
						bordapreference.addPair(new Pair<T, T>(f, s));
					} else {
						bordapreference.addPair(new Pair<T, T>(s, f));
					}

				}
			}
		}

		return bordapreference;
	}
	
	/**
	 * the veto rule only scores the lowest element in each preference order
	 * weight vector (0,0,0,...,1)
	 * @param input the preference orders
	 * @return the final aggregated preference order
	 */
	private PreferenceOrder<T> aggregateVeto(PreferenceOrder<T>[] input) {
		PreferenceOrder<T> vetopreference = new PreferenceOrder<T>();
		Set<T> tempSingleElem = new HashSet<T>();

		for (int i = 0; i <= input.length - 1; i++) {
			for (final T e : input[i].getSingleElements()) {
				tempSingleElem.add(e);
			}
		}

		Set<Pair<T, T>> tempPair = new HashSet<Pair<T, T>>();

		for (T f : tempSingleElem) {
			for (T s : tempSingleElem) {
				tempPair.add(new Pair<T, T>(f, s));
				tempPair.add(new Pair<T, T>(s, f));
			}
		}

		for (T f : tempSingleElem) {
			for (T s : tempSingleElem) {
				
				int order1 = 0;
				int order2 = 0;
				
				if (f != s && tempPair.contains(new Pair<T, T>(s, f))
						&& tempPair.contains(new Pair<T, T>(f, s))) {

					for (int i = 0; i <= input.length - 1; i++) {
						
						if (input[i].containsPair(f, s)) {
							order1++;
						} else if (input[i].containsPair(s, f)) {
							order2++;
						} else
							continue;
					}
					
					if (order1 < order2) {
						vetopreference.addPair(f, s);
					} else {
						vetopreference.addPair(s, f);
					}
					
					tempPair.remove(new Pair<T, T>(f, s));
					tempPair.remove(new Pair<T, T>(s, f));
				}
			}
		}	
		return vetopreference;
	}
	
}
