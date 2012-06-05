package net.sf.tweety.logics.probabilisticconditionallogic.analysis;

import net.sf.tweety.logics.probabilisticconditionallogic.*;

/**
 * Classes implementing this interface represent inconsistency measures
 * on conditional knowledge bases.
 * 
 * @author Matthias Thimm
 */
public interface InconsistencyMeasure {

	/** Tolerance. */
	public static final double MEASURE_TOLERANCE = 0.005;
	
	/**
	 * This method measures the inconsistency of the given belief set.
	 * @param beliefSet a PclBeliefSet,
	 * @return a Double indicating the degree of inconsistency.
	 */
	public Double inconsistencyMeasure(PclBeliefSet beliefSet);	
}
