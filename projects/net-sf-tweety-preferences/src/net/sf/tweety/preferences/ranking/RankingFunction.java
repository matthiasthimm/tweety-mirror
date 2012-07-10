package net.sf.tweety.preferences.ranking;

import java.util.*;

import net.sf.tweety.preferences.PreferenceOrder;
import net.sf.tweety.util.*;

/**
 * UNDER CONSTRUCTION
 * This class is meant to provide ranking functions to given
 * preference orders and vice versa. To be implemented. A ranking function
 * characterizes a preference order uniquely as:
 * 1.: rank: O -> N+ where O is the set of elements in the preference order.
 * 2.: the sum of all ranks for each element in O is minimal
 * 
 * @author Bastian Wolf
 * 
 */

public class RankingFunction<T> {

	/**
	 * every element is paired with its rank
	 */
	private Set<Pair<T, Integer>> elements;

	
	/**
	 * the preference order of this ranking function
	 */
	private PreferenceOrder<T> preforder;
	
	
	/**
	 * empty ranking function
	 */
	public RankingFunction() {
		this(new HashSet<Pair<T, Integer>>());
	}

	
	/**
	 * ranking function
	 * @param elements the elements given for this ranking function
	 */
	public RankingFunction(Collection<? extends Pair<T, Integer>> elements) {
		this.setElements(new HashSet<Pair<T, Integer>>(elements));
	}

	
	/**
	 * returns a set of ranked elements
	 * @return
	 */
	public Set<Pair<T, Integer>> getElements() {
		return elements;
	}

	
	/**
	 * sets ranked elements
	 * @param elements
	 */
	public void setElements(Set<Pair<T, Integer>> elements) {
		this.elements = elements;
	}

	
	/**
	 * Setter for the preference order this ranking function represents
	 *
	 */
	public void setPrefOrder(PreferenceOrder<T> preforder){
		this.preforder = preforder;
	}

	
	/**
	 * returns the preference order this ranking function represents 
	 * @return the preference order
	 */
	public PreferenceOrder<T> getPrefOrder(){
		return this.preforder;
	}
	
	
	/**
	 * Adds a single pair representing an element and its rank 
	 * @param e the element
	 * @param rank the elements rank
	 * @return true if successful, false if not
	 */
	public boolean addPair(T e, Integer rank) {
		Pair<T, Integer> pair = new Pair<T, Integer>(e, rank);
		return elements.add(pair);
	}
	
// Methods under Construction:
	
//	/**
//	 *	This functions computes the rank for each element in a preference order 
//	 */
//	public void computeRanks(){
//		int[] ranks = new int[preforder.getSingleElements().size()];
//		for(final T f : preforder.getSingleElements()){
//			for(final T s : preforder.getSingleElements()){
//				if (this.elements.contains(f) && this.elements.contains(s)){
//					{
//						if (f!=s && preforder.isRelated(f, s) && !preforder.isRelated(s, f)){
//						}
//					}
//				}
//			}
//		}
//	}
//	
//	/**
//	 * 
//	 */
//	public void initialiseEmptyRankingFunction(){
//		elements.clear();
//		for(final T e : preforder.getSingleElements()){
//			
//		}
//	}
//	
//	/**
//	 * 
//	 * @return
//	 */
//	public PreferenceOrder<T> computePreferenceOrder() {
//		PreferenceOrder<T> preforder = new PreferenceOrder<T>();
//		for (final Pair<T, Integer> f : elements) {
//			for (final Pair<T, Integer> s : elements) {
//				if (f != s) {
//					if (f.getSecond() < s.getSecond()) {
//						preforder.addPair(f.getFirst(), s.getFirst());
//					} else {
//						preforder.addPair(s.getFirst(), f.getFirst());
//					}
//				}
//			}
//		}
//		return preforder;
//	}
}
