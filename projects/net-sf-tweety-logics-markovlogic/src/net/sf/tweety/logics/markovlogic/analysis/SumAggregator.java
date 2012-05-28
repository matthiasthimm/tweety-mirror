package net.sf.tweety.logics.markovlogic.analysis;

import java.util.List;

/** This aggregation function models the sum function.
 * @author Matthias Thimm
 *
 */
public class SumAggregator implements AggregationFunction {

	private static final long serialVersionUID = -8518226177117879461L;

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.markovlogic.analysis.AggregationFunction#aggregate(java.util.List)
	 */
	@Override
	public double aggregate(List<Double> elements) {
		Double sum = new Double(0);
		for(Double elem: elements)
			sum += elem;
		return sum;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "sum";
	}
}
