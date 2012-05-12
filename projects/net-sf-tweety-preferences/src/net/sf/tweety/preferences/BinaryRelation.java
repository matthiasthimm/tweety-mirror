package net.sf.tweety.preferences;

import java.util.*;

import net.sf.tweety.util.Pair;

/**
 * This abstract class provides an implementation of a generic set of pairs to be used for
 * preference ordering.
 * 
 * @author Bastian Wolf
 * 
 * 
 * @param <T> the generic type of objects/pairs in this binary relation
 */

public abstract class BinaryRelation<T> {

	
	/**
	 * a given set of Pairs
	 */
	private Set<Pair<T,T>> elements;
	
	
	/**
	 * Creates an empty HashSet of binary relations.
	 */
	public BinaryRelation(){
		this(new HashSet<Pair<T, T>>());
	}
	
	/**
	 * 
	 * @param elements the set of given element pairs
	 */
	public BinaryRelation(Collection<? extends Pair<T, T>> elements){
		this.elements = new HashSet<Pair<T, T>>(elements);
	}
	
	// TODO maybe the implementation of easy access to single pairs without
	// string-identifier or similar (intended for human users)
	
	/**
	 * adds a given pair of generic elements to the set.
	 * @param e the given set
	 * @return true if successful, false if not
	 */
	
	public boolean addPair(Pair<T, T> e){
		return this.elements.add(e);
	}

	/**
	 * adds two given (single) elements as pair into the set
	 * @param f first element of the new pair
	 * @param s second element of the new pair
	 * @return true if successful, false if not
	 */
	public boolean addPair(T f, T s){
		Pair<T, T> pair = new Pair<T, T>(f, s);
		return elements.add(pair);
	}
	
	/**
	 * removes specific pair of the set
	 * @param e the pair to be removed
	 * @return true if successful, false if not
	 */
	public boolean removePair(Pair<T, T> e){
		return this.elements.remove(e);
	}
	
	/**
	 * returns whether the set is empty or not
	 * @return true if empty, false if not
	 */
	public boolean isEmpty(){
		return this.elements.isEmpty();
	}
	
	/**
	 * returns an iterator over a set of pairs
	 * @return an interator over a set of pairs
	 */
	public Iterator<Pair<T, T>> iterator(){
		return this.elements.iterator();
	}
	
	/**
	 * checks existence and returns a demanded pair
	 * @param e the demanded pair
	 * @return a pair if it exists, null otherwise
	 */
	public Pair<T, T> getPair(Pair<T, T> e){
		if (elements.contains(e)){
			return e;
		}
		return null;
	}
	
	/**
	 * returns the size of the set
	 * @return the size of the set
	 */
	public int size(){
		return this.elements.size();
	}
	
	// TODO Implementation of the array-methods
	/*
	
	*/
	
	/**
	 * returns a String with the elements of this set
	 * @return a String with the elements of this set
	 */
	public String toString() {
		String s = "{";
		Iterator<Pair<T, T>> it = iterator();
		while (it.hasNext()){
			s += it.next();
		}
		s += "}";
		return s;
		
	}
}
