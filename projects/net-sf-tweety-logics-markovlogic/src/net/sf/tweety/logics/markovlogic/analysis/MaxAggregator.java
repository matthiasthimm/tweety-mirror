package net.sf.tweety.logics.markovlogic.analysis;

import java.util.List;

/** This aggregation function models the maximum function.
 * @author Matthias Thimm
 *
 */
public class MaxAggregator implements AggregationFunction {

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.markovlogic.analysis.AggregationFunction#aggregate(java.util.List)
	 */
	@Override
	public double aggregate(List<Double> elements) {
		Double max = Double.MIN_VALUE;
		for(Double elem: elements)
			if(elem > max) max = elem;
		return max;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "max";
	}
}
