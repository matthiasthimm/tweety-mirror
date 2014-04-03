package net.sf.tweety.logics.pcl.analysis;

import java.util.Collection;

import net.sf.tweety.logics.pcl.syntax.ProbabilisticConditional;

/**
 * This class models a normalized approximation from below to the distance minimization inconsistency measure as proposed in [Thimm,UAI,2009], see [PhD thesis, Thimm].
 * 
 * @author Matthias Thimm
 */
public class NormalizedLowerApproxDistanceMinimizationInconsistencyMeasure extends LowerApproxDistanceMinimizationInconsistencyMeasure {

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.pcl.analysis.LowerApproxDistanceMinimizationInconsistencyMeasure#inconsistencyMeasure(java.util.Collection)
	 */
	@Override
	public Double inconsistencyMeasure(Collection<ProbabilisticConditional> formulas) {
		if(formulas.size() == 0) return 0d;
		return super.inconsistencyMeasure(formulas) / formulas.size();
	}
}
