package net.sf.tweety;

import java.util.*;

import net.sf.tweety.util.*;

/**
 * An entailment relation determines whether a formula is entailed
 * from another formula.
 * 
 * @author Matthias Thimm
 *
 * @param <T> The type of formula the relation works on.
 */
public abstract class EntailmentRelation<T extends Formula> {

	/** 
	 * Checks whether the given set of formulas entails the given formula.
	 * @param formulas a collection of formulas.
	 * @param formula some formula.
	 * @return "true" if the set of formulas entails the formula
	 */
	public abstract boolean entails(Collection<T> formulas, T formula);
	
	/**
	 * Checks whether the given set of formulas is consistent.
	 * @param formulas a set of formulas.
	 * @return "true" if the set is consistent.
	 */
	public abstract boolean isConsistent(Collection<T> formulas);
	
	/** 
	 * Checks whether the given set of formulas entails the other set of formulas.
	 * @param formulas a collection of formulas.
	 * @param formulas2 a collection of formulas.
	 * @return "true" if the former entails the latter.
	 */
	public boolean entails(Collection<T> formulas, Collection<T> formulas2){
		for(T formula: formulas)
			if(!this.entails(formulas, formula))
				return false;
		return true;
	}
	
	/** 
	 * Checks whether the first formula entails the second formula.
	 * @param formula a formula.
	 * @param formula2 a formula.
	 * @return "true" if the former entails the latter.
	 */
	public boolean entails(T formula, T formula2){
		Collection<T> formulas = new HashSet<T>();
		formulas.add(formula);
		return this.entails(formulas, formula2);
	}
	
	/** 
	 * Checks whether the first formula entails the other set of formulas.
	 * @param formula a formula.
	 * @param formulas2 a set of formulas.
	 * @return "true" if the former entails the latter.
	 */
	public boolean entails(T formula, Collection<T>  formulas2){
		for(T f: formulas2)
			if(!this.entails(formula, f))
				return false;
		return true;
	}
	
	/**
	 * Checks whether the two formula are equivalent, i.e. whether
	 * the first entails the second and vice versa.
	 * @param formula some formula
	 * @param formula2 some formula
	 * @return "true" iff the two formulas are equivalent.
	 */
	public boolean isEquivalent(T formula, T formula2){
		return this.entails(formula, formula2) && this.entails(formula2, formula);
	}
	
	/**
	 * Retrieves the set of kernels for the given formula
	 * from the given set of formulas.
	 * @param formulas a set of formulas.
	 * @param formula a formula.
	 * @return the collection of kernels
	 */	
	public Collection<Collection<T>> getKernels(Collection<T> formulas, T formula){
		Collection<Collection<T>> kernels = new HashSet<Collection<T>>();
		if(!this.entails(formulas, formula)) return kernels;
		SubsetIterator<T> it = new IncreasingSubsetIterator<T>(new HashSet<T>(formulas));
		boolean superSetOfKernel;
		double i=0;
		double pow = Math.pow(2, formulas.size());
		while(it.hasNext()){			
			Set<T> candidate = it.next();
			System.out.println(++i + " - " + (i/pow * 100) + "% - " + kernels + " - " + candidate.size());
			superSetOfKernel = false;
			for(Collection<T> kernel: kernels){
				if(candidate.containsAll(kernel)){
					superSetOfKernel = true;
					break;
				}
			}			
			if(!superSetOfKernel)
				if(this.entails(candidate, formula))
					kernels.add(candidate);
		}		
		return kernels;		
	}
}
