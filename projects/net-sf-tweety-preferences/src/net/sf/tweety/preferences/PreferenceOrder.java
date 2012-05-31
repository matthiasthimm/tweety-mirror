package net.sf.tweety.preferences;

import java.util.*;

import net.sf.tweety.util.Pair;

/**
 * This class extends the BinaryRelation-class with a check for totality and
 * transitivity
 * 
 * @author Bastian Wolf
 * 
 * @param <T> the generic type of objects/pairs in this preference order
 */

public class PreferenceOrder<T> extends BinaryRelation<T> {

	/**
	 * the single elements used
	 */
	private Set<T> singleElements;
	
	/**
	 * a given set of Pairs
	 */
	private Set<Pair<T, T>> elements;
	
	/**
	 * Creates an empty HashSet of preference order.
	 */
	public PreferenceOrder() {
		this(new HashSet<Pair<T, T>>());
	}

	/**
	 * generates a preference order with a given set of elements
	 * @param elements the set of given element pairs
	 */
	public PreferenceOrder(Collection<? extends Pair<T, T>> elements) {
		this.elements = new HashSet<Pair<T, T>>(elements);
		computeSingleElements();
	}

	/**
	 * (re-)computes a set of single elements in this preference order
	 */
	public Set<T> computeSingleElements() {
		singleElements.clear();
		for (Pair<T, T> pairs : elements) {
			singleElements.add(pairs.getFirst());
			singleElements.add(pairs.getSecond());
		}
		return singleElements;
	}
	
	/**
	 * a setter for the single elements of a preference order
	 * @param singleElements the given set of single elements
	 */
	public void setSingleElements(Set<T> singleElements){
		this.singleElements = singleElements;
	}
	
	/**
	 * returns the single elements in this preference order
	 * @return the single elements in this preference order
	 */
	public Set<T> getSingleElements(){
		if (singleElements == null)
			computeSingleElements();
		return singleElements;
	}
	
	/**
	 * adds a given pair of generic elements to the set.
	 * 
	 * @param e the given set
	 * @return true if successful, false if not
	 */

	public boolean addPair(Pair<T, T> e) {
		return this.elements.add(e);
	}

	/**
	 * adds two given (single) elements as pair into the set
	 * 
	 * @param f first element of the new pair
	 * @param s second element of the new pair
	 * @return true if successful, false if not
	 */
	public boolean addPair(T f, T s) {
		Pair<T, T> pair = new Pair<T, T>(f, s);
		return elements.add(pair);
	}

	/**
	 * removes specific pair of the set
	 * 
	 * @param e the pair to be removed
	 * @return true if successful, false if not
	 */
	public boolean removePair(Pair<T, T> e) {
		return this.elements.remove(e);
	}

	/**
	 * returns whether the set is empty or not
	 * 
	 * @return true if empty, false if not
	 */
	public boolean isEmpty() {
		return this.elements.isEmpty();
	}

	/**
	 * returns whether the elements a and b are related
	 * 
	 * @param a the first element to be checked
	 * @param b the second element to be checked
	 * @return true if related, false if not.
	 */
	public boolean isRelated(T a, T b) {
		for (Pair<T, T> pair : elements) {
			if (pair.getFirst() == a) {
				if (pair.getSecond() == b) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * returns an iterator over a set of pairs
	 * 
	 * @return an iterator over a set of pairs
	 */
	public Iterator<Pair<T, T>> iterator() {
		return this.elements.iterator();
	}

	/**
	 * checks existence and returns a demanded pair
	 * 
	 * @param e
	 *            the demanded pair
	 * @return a pair if it exists, null otherwise
	 */
	public Pair<T, T> getPair(Pair<T, T> e) {
		if (elements.contains(e)) {
			return e;
		}
		return null;
	}

	/**
	 * returns the size of the set
	 * 
	 * @return the size of the set
	 */
	public int size() {
		return this.elements.size();
	}

	/**
	 * returns a String with the elements of this set
	 * 
	 * @return a String with the elements of this set
	 */
	@Override
	public String toString() {
		String s = "{";
		Iterator<Pair<T, T>> it = iterator();
		while (it.hasNext()) {
			s += it.next();
		}
		s += "}";
		return s;

	}

	
	/**
	 * checks whether the set is total or not
	 * @return true if total, false otherwise
	 */
	public boolean isTotal() {
		for (final T f : getSingleElements()) {
			for (final T s : getSingleElements()) {
				if (f != s && !isRelated(f, s) && !isRelated(s, f))
					return false;
			}
		}
		return true;
	}
	
	/**
	 * checks whether the given set is transitive or not
	 * @return true if transitive, false otherwise 
	 */
	public boolean isTransitive() {
		for (final T a : getSingleElements()) {
			for (final T b : getSingleElements()) {
				for (final T c : getSingleElements()) {
					if (a != b && b != c && a != c && isRelated(a, b)
							&& isRelated(b, c) && !isRelated(a, c)) {
						return false;
					}
				}

			}
		}
		return true;
	}
}