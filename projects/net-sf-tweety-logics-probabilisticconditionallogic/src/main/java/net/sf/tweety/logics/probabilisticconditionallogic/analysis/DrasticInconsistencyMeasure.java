package net.sf.tweety.logics.probabilisticconditionallogic.analysis;

import net.sf.tweety.logics.probabilisticconditionallogic.*;

/**
 * This class models the drastic inconsistency measure, see [PhD thesis, Thimm].
 * 
 * @author Matthias Thimm
 */
public class DrasticInconsistencyMeasure implements InconsistencyMeasure {

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.probabilisticconditionallogic.analysis.InconsistencyMeasure#inconsistencyMeasure(net.sf.tweety.logics.probabilisticconditionallogic.PclBeliefSet)
	 */
	@Override
	public Double inconsistencyMeasure(PclBeliefSet beliefSet) {
		PclDefaultConsistencyTester consistencyTester = new PclDefaultConsistencyTester();
		if(consistencyTester.isConsistent(beliefSet)) return 0d;
		return 1d;
	}

}
