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
	 * constant value for less-relations
	 */
	public final static int LESS = 0;
	
	/**
	 * constant value for less-equal-relations
	 */
	public final static int LEQ = 1;
	
//	/**
//	 * a given set of Pairs
//	 */
//	private Set<Pair<T, T>> elements;
	
	/**
	 * pairs contained in this po related in a less-relation
	 */
	private Set<Pair<T, T>> lessRelations;
	
	/**
	 * pairs contained in this po related in a less-equal-relation
	 */
	private Set<Pair<T, T>> leqRelations;
	
	
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	
//------- Constructor ------------------------------------------------	
	
	/**
	 * Creates an empty HashSet of preference order.
	 */
	public PreferenceOrder() {
		this(new HashSet<Pair<T, T>>(), new HashSet<Pair<T, T>>());
	}

	/**
	 * generates a preference order with a given set of elements
	 * 
	 * @param elements
	 *            the set of given element pairs
	 */
	public PreferenceOrder(Collection<? extends Pair<T, T>> lessRelations, Collection<? extends Pair<T, T>> leqRelations) {
		this.lessRelations = new HashSet<Pair<T, T>>(lessRelations);
		this.leqRelations = new HashSet<Pair<T, T>>(leqRelations);
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
	@Override
	public boolean add(Pair<Pair<T, T>, Integer> p) {
		switch (p.getSecond()) {
		case LESS:
			return this.lessRelations.add(p.getFirst());
		case LEQ:
			return this.leqRelations.add(p.getFirst());
		default:
			return false;
		}
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
	public boolean addPair(T f, T s, Integer relation) {
		Pair<T, T> pair = new Pair<T, T>(f, s);
		return this.add(new Pair<Pair<T, T>, Integer>(pair, relation));
	}

	/**
	 * (re-)computes a set of single elements in this preference order
	 */
	public Set<T> getDomainElements() {
		Set<T> domainElements = new HashSet<T>();
		
		for (Pair<T, T> pairs : lessRelations) {
			domainElements.add(pairs.getFirst());
			domainElements.add(pairs.getSecond());
		}
		for (Pair<T, T> pairs : leqRelations) {
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
		if (this.leqRelations.contains(o)){
			return leqRelations.remove(o);
		}
		if (this.lessRelations.contains(o)){
			return lessRelations.remove(o);
		}
		return false;
	}

	/**
	 * returns whether the set is empty or not
	 * 
	 * @return true if empty, false if not
	 */
	public boolean isEmpty() {
		return (this.lessRelations.isEmpty() && this.leqRelations.isEmpty());
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
		for (Pair<T, T> pair : lessRelations) {
			if (pair.getFirst() == a) {
				if (pair.getSecond() == b) {
					return true;
				}
			}
		}
		for (Pair<T, T> pair : leqRelations) {
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

	public Iterator<Pair<T, T>> iterator(Set<Pair<T, T>> s) {
		return s.iterator();
	}

	/**
	 * checks existence and returns a demanded pair
	 * 
	 * @param e
	 *            the demanded pair
	 * @return a pair if it exists, null otherwise
	 */
	public Pair<T, T> get(Pair<T, T> e) {
		if(lessRelations.contains(e)) {
			return e;
		}
		if(leqRelations.contains(e)){
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
		for (Pair<T, T> p : lessRelations) {
			if (p.getFirst() == a && p.getSecond() == b) {
				return p;
			}
		}
		for (Pair<T, T> p : leqRelations) {
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
		for (Pair<T, T> p : lessRelations)
			if (p.getFirst() == a && p.getSecond() == b){ 
				return true;
				}
		for (Pair<T, T> p : leqRelations)
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
		return (leqRelations.contains(o) || lessRelations.contains(o));
	}
	
	/**
	 * returns the size of the set
	 * 
	 * @return the size of the set
	 */
	public int size() {
		return (this.lessRelations.size()+this.leqRelations.size());
	}

	/**
	 * returns a String with the elements of this set
	 * 
	 * @return a String with the elements of this set
	 */
	@Override
	public String toString() {
		String s = "{";
		Iterator<Pair<Pair<T, T>, Integer>> it = iterator();
		while (it.hasNext()) {
			s += it.next().getFirst();
		}
		s += "}";
		return s;

	}
	
	/**
	 * returns an array containing all objects
	 * @return the Object[]-array
	 */
	public Object[] toArray(){
		Set<Pair<T, T>> elements = new HashSet<Pair<T, T>>();
		elements.addAll(leqRelations);
		elements.addAll(lessRelations);
		return elements.toArray();
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
	
	public Set<Pair<Pair<T, T>, Integer>> getAllPairs(){
		Set<Pair<Pair<T, T>, Integer>> allTemp = new HashSet<Pair<Pair<T,T>,Integer>>();
		for(Pair<T, T> p : lessRelations){
			allTemp.add(new Pair<Pair<T, T>, Integer>(p, LESS)); 
		}
		for(Pair<T, T> p : leqRelations){
			allTemp.add(new Pair<Pair<T, T>, Integer>(p, LEQ)); 
		}
		return allTemp;
	}
	
	/**
	 * clears the current preference order element set
	 */
	@Override
	public void clear() {
		lessRelations.clear();
		leqRelations.clear();
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
			if(!this.contains(e)){
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
		Set<Pair<T,T>> pLess = new HashSet<Pair<T,T>>();
		Set<Pair<T,T>> pLeq = new HashSet<Pair<T, T>>();
		while(it.hasNext()){
			Object e = it.next();
			for(Pair<T, T> a : lessRelations){
				if(!c.contains(a))
					pLess.add(a);
			}
			for(Pair<T, T> a : leqRelations){
				if(!c.contains(a))
					pLeq.add(a);
			}
		}
		if (pLess.equals(lessRelations) && pLeq.equals(leqRelations)){
			return false;
		}
		lessRelations = pLess;
		leqRelations = pLeq;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Set#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		Iterator<?> it = c.iterator();
		Set<Pair<T,T>> pLess = new HashSet<Pair<T,T>>();
		Set<Pair<T,T>> pLeq = new HashSet<Pair<T, T>>();
		while(it.hasNext()){
			Object e = it.next();
			for(Pair<T, T> a : lessRelations){
				if(a.equals(e)){
					pLess.add(a);
				}
			}
			for(Pair<T, T> a : leqRelations){
				if(a.equals(e)){
					pLeq.add(a);
				}
			}
		}	
		if (pLess.equals(lessRelations) && pLeq.equals(leqRelations)){
			return false;
		}
		lessRelations = pLess;
		leqRelations = pLeq;
		return true;
	}

	/**
	 * adds all given elements to the preference order
	 * @return true if element-set changed, false if not
	 */
//	@Override
//	public boolean addAll(Collection<? extends Pair<T, T>> c) {
//		Set<Pair<T, T>> temp = this;
//		for (Pair<T, T> p : c){
//			temp.add(p);
//		}
//		if(!this.equals(temp))
//			return true;
//		else
//			return false;
//	}

	@Override
	public boolean addAll(Collection<? extends Pair<Pair<T, T>, Integer>> c) {
		Set<Pair<T, T>> lessTemp = this.lessRelations;
		Set<Pair<T, T>> leqTemp = this.leqRelations;
		for (Pair<Pair<T, T>, Integer> p : c){
			switch (p.getSecond()) {
			case LESS:
				lessTemp.add(p.getFirst());
			case LEQ:
				leqTemp.add(p.getFirst());
			default:
				continue;
			}
		}
		if (!lessTemp.equals(lessRelations) || !leqTemp.equals(leqRelations)){
			this.lessRelations = lessTemp;
			this.leqRelations = leqTemp;
			return true;
		}
		return false;
	}

	@Override
	public Iterator<Pair<Pair<T, T>, Integer>> iterator() {
		return getAllPairs().iterator();	
	}
}