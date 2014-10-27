package net.sf.tweety.logics.commons.analysis;

import java.util.Collection;
import java.util.HashSet;

import net.sf.tweety.commons.Formula;
import net.sf.tweety.commons.util.MathTools;
import net.sf.tweety.math.func.SimpleFunction;

/**
 * This class implements the family of "Measures of the Degree of Inconsistency" from
 * [Mu,Liu,Jin, Bell. A syntax-based approach to measuring the degree of inconsistency for belief bases. IJAR 52(7), 2011.]
 * 
 *  
 * @author Matthias Thimm
 *
 * @param <S> The specific type of formulas
 */
public class DfInconsistencyMeasure<S extends Formula> extends BeliefSetInconsistencyMeasure<S> {

	/** The MUs enumerator. */
	private MusEnumerator<S> enumerator;
	
	/** The measure function used to aggregate the normalized cardinalities of minimal
	 * inconsistent subsets. */
	private SimpleFunction<double[],Double> measureFunction;
	
	/**
	 * Creates a new inconsistency measure.
	 * @param measureFunction the measure function used to aggregate the normalized cardinalities of minimal
	 * inconsistent subsets.
	 * @param enumerator some MUs enumerator
	 */
	public DfInconsistencyMeasure(SimpleFunction<double[],Double> measureFunction, MusEnumerator<S> enumerator){
		this.measureFunction = measureFunction;
		this.enumerator = enumerator;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.commons.analysis.BeliefSetInconsistencyMeasure#inconsistencyMeasure(java.util.Collection)
	 */
	@Override
	public Double inconsistencyMeasure(Collection<S> formulas) {
		Collection<Collection<S>> mus = this.enumerator.minimalInconsistentSubsets(formulas);
		double[] vcard = new double[formulas.size()];
		double[] conflictRatios = new double[formulas.size()];
		for(Collection<S> mu: mus)
			vcard[mu.size()-1]++;
		double[] cn = this.getConsistentSetCounts(new HashSet<S>(), formulas, mus, formulas.size());
		for(int i = 0; i < formulas.size(); i++)
			if(vcard[i] + cn[i] == 0)
				conflictRatios[i] = 0d;
			else conflictRatios[i] = vcard[i]/(vcard[i] + cn[i]);
		return this.measureFunction.eval(conflictRatios);
	}
	
	/**
	 * Computes the numbers of consistent subsets for cardinalities |inFormulas|...|inFormulas|+|restFormulas|
	 * (the knowledge base considered is inFormulas\cup restFormulas and the formulas in inFormulas have to be part
	 * of every considered subset).
	 * @param inFormulas a set of formulas that are in.
	 * @param restFormulas the remaining formulas
	 * @param mus the set of mus
	 * @param card the cardinality of the whole knowledge base.
	 * @return the numbers of consistent subsets for cardinalities |inFormulas|...|inFormulas|+|restFormulas|
	 */
	private double[] getConsistentSetCounts(Collection<S> inFormulas, Collection<S> restFormulas, Collection<Collection<S>> mus, int card){
		//if inFormulas already contains a mus there are no consistent subsets anymore
		for(Collection<S> mu: mus)
			if(inFormulas.containsAll(mu))
				return new double[card];
		//if inFormulas\cup restFormulas contains no mus we can end recursion and compute the remaining values directly
		Collection<S> joint = new HashSet<S>();
		joint.addAll(inFormulas);
		joint.addAll(restFormulas);
		boolean c = true;
		for(Collection<S> mu: mus){
			c = true;
			if(joint.containsAll(mu)){
				c = false;
				break;
			}
		}
		if(c){
			double[] result = new double[card];
			int cidx;
			if(!inFormulas.isEmpty())
				result[inFormulas.size()-1] = 1d;
			for(int i = 1; i <= restFormulas.size(); i++){
				cidx = i + inFormulas.size() - 1;
				result[cidx] = new Double(MathTools.binomial(restFormulas.size(), i));
			}
			return result;
		}
		// Otherwise select one formula from restFormulas and proceed recursively.
		// note that restFormulas contains at least one formula, otherwise one of the
		// above conditions would have been true
		S f = restFormulas.iterator().next();
		Collection<S> newRest = new HashSet<S>(restFormulas);
		newRest.remove(f);
		//one try without the formula...
		double[] d1 = this.getConsistentSetCounts(inFormulas, newRest, mus, card);
		// ...and one with the formula
		Collection<S> newIn = new HashSet<S>(inFormulas);
		newIn.add(f);
		double[] d2 = this.getConsistentSetCounts(newIn, newRest, mus, card);
		// aggregate
		double[] result = new double[card];
		for(int i = 0; i < card; i++){
			result[i] = d1[i] + d2[i];
		}		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "df";
	}
}
