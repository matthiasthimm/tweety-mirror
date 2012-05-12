package net.sf.tweety.preferences;

import java.util.Iterator;

import net.sf.tweety.util.Pair;

/**
 * This class extends the BinaryRelation-class with a check for totality and transitivity
 * 
 * @author Bastian Wolf
 *
 * @param <T> the generic type of objects/pairs in this preference order
 */

public class PreferenceOrder<T> extends BinaryRelation<T> {
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.preferences.BinaryRelation#addPair(net.sf.tweety.util.Pair)
	 */

	@Override
	public boolean addPair(Pair<T, T> e) {
		return super.addPair(e);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.preferences.BinaryRelation#addPair(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean addPair(T f, T s) {
		return super.addPair(f, s);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.preferences.BinaryRelation#removePair(net.sf.tweety.util.Pair)
	 */
	@Override
	public boolean removePair(Pair<T, T> e) {
		
		return super.removePair(e);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.preferences.BinaryRelation#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		
		return super.isEmpty();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.preferences.BinaryRelation#iterator()
	 */
	@Override
	public Iterator<Pair<T, T>> iterator() {
		
		return super.iterator();
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.preferences.BinaryRelation#getPair(net.sf.tweety.util.Pair)
	 */
	@Override
	public Pair<T, T> getPair(Pair<T, T> e) {
		
		return super.getPair(e);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.preferences.BinaryRelation#size()
	 */
	@Override
	public int size() {
		
		return super.size();
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.preferences.BinaryRelation#toString()
	 */
	@Override
	public String toString() {
		
		return super.toString();
	}
	
	// TODO Implementation of total and transitive check for given preference order
/*	
	public boolean isTotal(){
		return false;
	}
	
	public boolean isTransitive(){
		return false;
	}
*/
}
