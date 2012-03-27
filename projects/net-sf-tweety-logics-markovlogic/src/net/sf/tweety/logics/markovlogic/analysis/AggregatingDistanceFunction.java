package net.sf.tweety.logics.markovlogic.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * This distance function uses an aggregator on the 1-norm distance of each 
 * value.
 * 
 * @author Matthias Thimm
 */
public class AggregatingDistanceFunction implements DistanceFunction {

	/** The aggregation function used for computing the distance. */	
	private AggregationFunction aggregator;
	
	/** Creates a new distance function with the given aggregator.
	 * @param aggregator some aggregation function.
	 */
	public AggregatingDistanceFunction(AggregationFunction aggregator){
		this.aggregator = aggregator;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.markovlogic.analysis.DistanceFunction#distance(java.util.List, java.util.List)
	 */
	@Override
	public double distance(List<Double> l1, List<Double> l2) {
		if(l1.size() != l2.size())
			throw new IllegalArgumentException("Lengths of lists must match.");
		List<Double> diff = new ArrayList<Double>();
		for(int i = 0; i< l1.size(); i++)
			diff.add(Math.abs(l1.get(i)-l2.get(i)));
		return this.aggregator.aggregate(diff);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return this.aggregator.toString()+"-dist";
	}
}
