package net.sf.tweety.preferences;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
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
	 * the ranking function for this preference order
	 */
	private RankingFunction<T> rankingFunction;
	
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
		this.singleElements = new HashSet<T>();
		computeSingleElements();
	}

// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	
//------- Getter & Setter --------------------------------------------
		
	/**
	 * a setter for the single elements of a preference order
	 * 
	 * @param singleElements
	 *            the given set of single elements
	 */
	public void setSingleElements(Set<T> singleElements) {
		this.singleElements = singleElements;
	}

	/**
	 * returns the single elements in this preference order
	 * 
	 * @return the single elements in this preference order
	 */
	public Set<T> getSingleElements() {
		if (singleElements.isEmpty())
			computeSingleElements();
		return singleElements;
	}

	/**
	 * returns the ranking function for this preference order
	 * @return the ranking function for this preference order
	 */
	public RankingFunction<T> getRankingFunction() {
		return rankingFunction;
	}

	/**
	 * the setter for the ranking function of this preference order
	 * @param rankingFunction the ranking function of this order
	 */
	public void setRankingFunction(RankingFunction<T> rankingFunction) {
		this.rankingFunction = rankingFunction;
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

	public boolean addPair(Pair<T, T> e) {
		return this.elements.add(e);
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
	public void computeSingleElements() {
		if (!singleElements.isEmpty())
			singleElements.clear();
		for (Pair<T, T> pairs : elements) {
			singleElements.add(pairs.getFirst());
			singleElements.add(pairs.getSecond());
		}
	}

	/**
	 * removes specific pair of the set
	 * 
	 * @param e
	 *            the pair to be removed
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

	
	public boolean containsPair(T a, T b){
		for (Pair<T, T> p : elements) 
			if (p.getFirst() == a && p.getSecond() == b){ 
				return true;
				}
		return false;
	}
	
	public boolean containsPair(Pair<T, T> e){
		return (elements.contains(e));
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
	
//	Experimental:	
	
//	/**
//	 * returns an array containing all objects
//	 * @return the Object[]-array
//	 */
//	public Object[] toArray(){
//		return this.elements.toArray();
//	}
//	
//	/**
//	 * returns all elements in an array
//	 * @param a is a given array
//	 * @return an array
//	 */
//	public T[] toArray(T[] a){
//		return a;
//	}
	

//	Under Construction:
	/**
	 * 
	 */
	public RankingFunction<T> computeRankingFunction(){
		RankingFunction<T> rankfunc = new RankingFunction<T>();
		rankfunc.generateRankingFunction(this);
		return rankfunc;
	}
	
	
	/**
	 * a method for writing preference orders into files from which they can be read in again
	 * @param filename the filename for the file this order has do be written in
	 */
	public void writeToFile(String filename){
		
		PrintWriter pw = null;
		try {
		Writer fw = new FileWriter(filename);
		Writer bw = new BufferedWriter(fw);
		pw = new PrintWriter(bw);
		
		String s = "{";
		int count = 1;
		for (T e : getSingleElements()){
			
			if (count < singleElements.size())
				s += e.toString() + ", ";
			else
				s += e.toString();
		count++;
		}
		
		s += "}";
		
		pw.println(s);
		
		Iterator<Pair<T,T>> it = iterator();
		while (it.hasNext()){
			Pair<T, T> temp = it.next();
			pw.println(temp.getFirst() + " < " + temp.getSecond());
		}
		} catch (IOException e){
			System.out.println("File could not be generated");
		} finally {
			if (pw != null)
				pw.close();
		}
	}
	
	
	/**
	 * checks whether the set is total or not
	 * 
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
	 * 
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