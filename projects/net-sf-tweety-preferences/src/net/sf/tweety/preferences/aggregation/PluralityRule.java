package net.sf.tweety.preferences.aggregation;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.preferences.PreferenceOrder;
import net.sf.tweety.util.Pair;

public class PluralityRule<T> implements PreferenceAggregator<T> {

	public PreferenceOrder<T> aggregate(PreferenceOrder<T>[] input) {
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

}
