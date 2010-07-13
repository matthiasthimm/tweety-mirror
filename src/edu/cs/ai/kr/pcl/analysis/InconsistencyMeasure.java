package edu.cs.ai.kr.pcl.analysis;

import edu.cs.ai.kr.pcl.*;

/**
 * Classes implementing this interface represent inconsistency measures
 * on conditional knowledge bases.
 * 
 * @author Matthias Thimm
 */
public interface InconsistencyMeasure {

	/**
	 * This method measures the inconsistency of the given belief set.
	 * @param beliefSet a PclBeliefSet,
	 * @return a Double indicating the degree of inconsistency.
	 */
	public Double inconsistencyMeasure(PclBeliefSet beliefSet);	
}
