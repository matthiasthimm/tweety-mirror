package net.sf.tweety.preferences;

import java.util.*;

import net.sf.tweety.preferences.ranking.RankingFunction;
import net.sf.tweety.util.Pair;

/**
 * This class extends the BinaryRelation-class with a check for totality and
 * transitivity
 * 
 * @author Bastian Wolf
 * 
 * @param <T>
 *            the generic type of objects/pairs in this preference order
 */

public class PreferenceOrder<T> implements BinaryRelation<T> {
	
	/**
	 * a given set of Pairs
	 */
	private Set<Pair<T, T>> elements;
	
	
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	
//------- Constructor ------------------------------------------------	
	
	/**
	 * Creates an empty HashSet of preference order.
	 */
	public PreferenceOrder() {
		this(new HashSet<Pair<T, T>>());
	}

	/**
	 * generates a preference order with a given set of elements
	 * 
	 * @param elements
	 *            the set of given element pairs
	 */
	public PreferenceOrder(Collection<? extends Pair<T, T>> elements) {
		this.elements = new HashSet<Pair<T, T>>(elements);
	}

// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	
//------- Getter & Setter --------------------------------------------
		

	/**
	 * returns the ranking function for this preference order
	 * @return the ranking function for this preference order
	 */
	public RankingFunction<T> getRankingFunction() {
		return new RankingFunction<T>(this);
	}

	
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//------- Methods ----------------------------------------------------
	
	/**
	 * adds a given pair of generic elements to the set.
	 * 
	 * @param e
	 *            the given set
	 * @return true if successful, false if not
	 */
	public boolean add(Pair<T,T> e) {
		return elements.add((Pair<T, T>) e);
	}

	/**
	 * adds two given (single) elements as pair into the set
	 * 
	 * @param f
	 *            first element of the new pair
	 * @param s
	 *            second element of the new pair
	 * @return true if successful, false if not
	 */
	public boolean addPair(T f, T s) {
		Pair<T, T> pair = new Pair<T, T>(f, s);
		return elements.add(pair);
	}

	/**
	 * (re-)computes a set of single elements in this preference order
	 */
	public Set<T> getDomainElements() {
		Set<T> domainElements = new HashSet<T>();
		
		for (Pair<T, T> pairs : elements) {
			domainElements.add(pairs.getFirst());
			domainElements.add(pairs.getSecond());
		}
		
		return domainElements;
	}

	/**
	 * removes specific pair of the set
	 * 
	 * @param e
	 *            the pair to be removed
	 * @return true if successful, false if not
	 */
	@Override
	public boolean remove(Object o) {
		return this.elements.remove(o);
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
	 * @param a
	 *            the first element to be checked
	 * @param b
	 *            the second element to be checked
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
		return elements.iterator();
	}

	/**
	 * checks existence and returns a demanded pair
	 * 
	 * @param e
	 *            the demanded pair
	 * @return a pair if it exists, null otherwise
	 */
	public Pair<T, T> get(Pair<T, T> e) {
		if (elements.contains(e)) {
			return e;
		}
		return null;
	}

	/**
	 * returns a pair if it consists of of two given elements
	 * 
	 * @param a
	 *            the first element
	 * @param b
	 *            the second element
	 * @return a pair if found, null if not
	 */
	public Pair<T, T> getPair(T a, T b) {
		for (Pair<T, T> p : elements) {
			if (p.getFirst() == a && p.getSecond() == b) {
				return p;
			}
		}
		return null;
	}

	/**
	 * checks whether this preference order contains a pair of given elements
	 * @param a the first element
	 * @param b the second element
	 * @return true if pair is in this preference order, false if not
	 */
	public boolean containsPair(T a, T b){
		for (Pair<T, T> p : elements) 
			if (p.getFirst() == a && p.getSecond() == b){ 
				return true;
				}
		return false;
	}
	
	/**
	 * checks whether this preference order contains a given pair
	 * @param e the given pair
	 * @return true if pair is in this preference order, false if not
	 */
	public boolean contains(Object o){
		return (elements.contains(o));
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
	 * returns an array containing all objects
	 * @return the Object[]-array
	 */
	public Object[] toArray(){
		return this.elements.toArray();
	}
	
	/**
	 * returns all elements in an array
	 * @param a is a given array
	 * @return an array
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		return this.getDomainElements().toArray(a);
	}
	
	/**
	 * checks whether the set is total or not
	 * 
	 * @return true if total, false otherwise
	 */
	public boolean isTotal() {
		for (final T f : getDomainElements()) {
			for (final T s : getDomainElements()) {
				if (f != s && !isRelated(f, s) && !isRelated(s, f))
					return false;
			}
		}
		return true;
	}

	/**
	 * checks whether the given set is transitive or not
	 * 
	 * @return true if transitive, false otherwise
	 */
	public boolean isTransitive() {
		for (final T a : getDomainElements()) {
			for (final T b : getDomainElements()) {
				for (final T c : getDomainElements()) {
					if (a != b && b != c && a != c && isRelated(a, b)
							&& isRelated(b, c) && !isRelated(a, c)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * checks whether the given set represents a valid preference order
	 * @return true if valid, false if not
	 */
	public boolean isValid(){
		for(T a : getDomainElements()){
			for (T b: getDomainElements()){
				if(a != b){
					if(isRelated(a, b) && isRelated(b, a)){
						return false;
					}
				}
			}
		}
		return (true && isTotal() && isTransitive());
	}

	/**
	 * clears the current preference order element set
	 */
	@Override
	public void clear() {
		elements.clear();
	}

	/**
	 * checks, whether all of the given elements are contained in the preference order
	 * @return true iff all elements are contained, false otherwise
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		Iterator<?> it = c.iterator();
		while (it.hasNext()) {
			Object e = it.next();
			if(!elements.contains(e)){
				return false;
			}
		}
		return true;
	}

	/**
	 * removes all given elements from the preference order
	 * @return true if elements-set has changed, false if not
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		Iterator<?> it = c.iterator();
		Set<Pair<T, T>> p = new HashSet<Pair<T, T>>();
		while(it.hasNext()){
			Object e = it.next();
			for(Pair<T, T> a : elements){
				if(!c.contains(a))
					p.add(a);
			}
		}
		if(p.equals(elements)){ 
			return false;
		}
		elements = p;
		return true;
	}
	
	/**
	 * keeps all the given elements in the element set and removes the rest
	 * @return true if the set changed, false if not
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		Iterator<?> it = c.iterator();
		Set<Pair<T,T>> p = new HashSet<Pair<T,T>>();
		while(it.hasNext()){
			Object e = it.next();
			for(Pair<T, T> a : elements){
				if(a.equals(e)){
					p.add(a);
				}
			}
		}	
		if (p.equals(elements)){
			return false;
		}
		elements = p;
		return true;
	}

	/**
	 * adds all given elements to the preference order
	 * @return true if element-set changed, false if not
	 */
	@Override
	public boolean addAll(Collection<? extends Pair<T, T>> c) {
		Set<Pair<T, T>> temp = this;
		for (Pair<T, T> p : c){
			temp.add(p);
		}
		if(!this.equals(temp))
			return true;
		else
			return false;
	}	
}