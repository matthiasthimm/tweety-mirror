package net.sf.tweety.logics.markovlogic.analysis;

import java.util.List;

/**
 * This class implement the p-norm distance function.
 * @author Matthias Thimm
 */
public class PNormDistanceFunction implements DistanceFunction {
	
	/** The parameter for the p-norm.*/
	private int p;
	
	/** Creates a new p-norm distance function.
	 * @param p the parameter for the p-norm.
	 */
	public PNormDistanceFunction(int p){
		this.p = p;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.markovlogic.analysis.DistanceFunction#distance(java.util.List, java.util.List)
	 */
	@Override
	public double distance(List<Double> l1, List<Double> l2) {
		if(l1.size() != l2.size())
			throw new IllegalArgumentException("Lengths of lists must match.");
		Double sum = new Double(0);
		for(int i = 0; i< l1.size(); i++)
			sum += Math.pow(Math.abs(l1.get(i)-l2.get(i)),p);
		return Math.pow(sum, new Double(1)/p);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return this.p+"-norm";
	}
}
