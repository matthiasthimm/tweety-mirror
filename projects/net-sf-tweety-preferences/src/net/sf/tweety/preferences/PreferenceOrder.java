package net.sf.tweety.preferences;

import java.util.*;

import net.sf.tweety.preferences.ranking.RankingFunction;
import net.sf.tweety.util.Triple;

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
	 * a given set of Triples
	 */
	private Set<Triple<T, T, Relation>> relations;
	
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	
//------- Constructor ------------------------------------------------	
	
	/**
	 * Creates an empty HashSet of preference order.
	 */
	public PreferenceOrder() {
		this(new HashSet<Triple<T, T, Relation>>());
	}

	/**
	 * generates a preference order with a given set of elements
	 * 
	 * @param elements
	 *            the set of given element pairs
	 */
	public PreferenceOrder(Collection<? extends Triple<T, T, Relation>> relations) {
		this.relations = new HashSet<Triple<T, T, Relation>>(relations);
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
	public boolean add(Triple<T, T, Relation> t) {
		return this.relations.add(t);
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
	public boolean addPair(T f, T s, Relation relation) {
		return this.add(new Triple<T, T, Relation>(f, s, relation));
	}

	/**
	 * (re-)computes a set of single elements in this preference order
	 */
	public Set<T> getDomainElements() {
		Set<T> domainElements = new HashSet<T>();
		
		for(Triple<T, T, Relation> t : relations){
			domainElements.add(t.getFirst());
			domainElements.add(t.getSecond());
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
		if(this.relations.contains(o))
			return relations.remove(o);
		else
			return false;
	}

	/**
	 * returns whether the set is empty or not
	 * 
	 * @return true if empty, false if not
	 */
	public boolean isEmpty() {
		return (this.relations.isEmpty());
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
		for(Triple<T, T, Relation> t : relations){
			if(t.getFirst() == a){
				if(t.getSecond() == b){
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

	public Iterator<Triple<T, T, Relation>> iterator(Set<Triple<T, T, Relation>> s) {
		return s.iterator();
	}

	/**
	 * checks existence and returns a demanded pair
	 * 
	 * @param e
	 *            the demanded pair
	 * @return a pair if it exists, null otherwise
	 */
	public Triple<T, T, Relation> get(Triple<T, T, Relation> e) {
		for(Triple<T, T, Relation> t : relations){
			if(t.getFirst() == e.getFirst()){
				if(t.getSecond() == e.getSecond()){
					return t;
				}
			}
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
	public Triple<T, T, Relation> getTriple(T a, T b) {
		for(Triple<T, T, Relation> t : relations){
			if(t.getFirst() == a){
				if(t.getSecond() == b){
					return t;
				}
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
	public boolean containsRelation(T a, T b){
		for(Triple<T, T, Relation> t : relations){
			if(t.getFirst() == a){
				if (t.getSecond() == b){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * checks whether this preference order contains a given pair
	 * @param e the given pair
	 * @return true if pair is in this preference order, false if not
	 */
	public boolean contains(Object o){
		return (relations.contains(o));
	}
	
	/**
	 * returns the size of the set
	 * 
	 * @return the size of the set
	 */
	public int size() {
		return (this.relations.size());
	}

	/**
	 * returns a String with the elements of this set
	 * 
	 * @return a String with the elements of this set
	 */
	@Override
	public String toString() {
		String s = "{";
		Iterator<Triple<T, T, Relation>> it = iterator();
		while (it.hasNext()) {
			Triple<T, T, Relation> t = it.next();
			if(it.hasNext()){
				s += "("+ t.getFirst().toString() + "," + t.getSecond().toString()+ "), ";
			} else {
				s += "("+ t.getFirst().toString() + "," + t.getSecond().toString()+ ")";
			}
		}
		s += "}";
		return s;

	}
	
	/**
	 * returns an array containing all objects
	 * @return the Object[]-array
	 */
	public Object[] toArray(){
		Set<Triple<T, T, Relation>> elements = new HashSet<Triple<T, T, Relation>>();
		elements.addAll(relations);
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
		return (isTotal() && isTransitive());
	}
	
	/**
	 * clears the current preference order element set
	 */
	@Override
	public void clear() {
		relations.clear();	
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
		Set<Triple<T,T,Relation>> tempRel = new HashSet<Triple<T,T,Relation>>();
		while(it.hasNext()){
			Object e = it.next();
			for(Triple<T, T, Relation> a : relations){
				if(!c.contains(a))
					tempRel.add(a);
			}
		}
		if (tempRel.equals(this.relations)){
			return false;
		}
		this.relations = tempRel;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Set#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		Iterator<?> it = c.iterator();
		Set<Triple<T,T,Relation>> tempRel = this.relations;
		while(it.hasNext()){
			Object e = it.next();
			for(Triple<T, T, Relation> a : relations){
				if(a.equals(e)){
					tempRel.add(a);
				}
			}
		}	
		if (tempRel.equals(relations)){
			return false;
		}
		relations = tempRel;
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
	public boolean addAll(Collection<? extends Triple<T, T, Relation>> c) {
		Set<Triple<T,T,Relation>> tempRel = this.relations;
		for(Triple<T,T,Relation> t : c){
			tempRel.add(t);
		}
		if(!this.relations.equals(tempRel)){
			this.relations = tempRel;
			return true;
		}
		return false;
	}

	@Override
	public Iterator<Triple<T, T, Relation>> iterator() {
		return relations.iterator();
	}



}