package net.sf.tweety.logics.markovlogic.analysis;

import java.util.List;

/**
 * This class aggregates a list of doubles to a single double.
 * 
 * @author Matthias Thimm
 */
public interface AggregationFunction {

	/** Aggregates the elements to a single double.
	 * @param elements a list of double
	 * @return a double
	 */
	public double aggregate(List<Double> elements);
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString();
}
