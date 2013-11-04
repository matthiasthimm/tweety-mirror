package net.sf.tweety.arg.prob.dynamics;

import net.sf.tweety.arg.dung.DungTheory;
import net.sf.tweety.arg.prob.PartialProbabilityAssignment;
import net.sf.tweety.arg.prob.semantics.ProbabilisticExtension;

/**
 * Provides common functionality for change operators based on probabilistic semantics.
 * @author Matthias Thimm
 */
public abstract class AbstractPAChangeOperator implements ChangeOperator {

	/* (non-Javadoc)
	 * @see net.sf.tweety.arg.prob.dynamics.ChangeOperator#change(net.sf.tweety.arg.prob.PartialProbabilityAssignment, net.sf.tweety.arg.dung.DungTheory)
	 */
	@Override
	public abstract ProbabilisticExtension change(PartialProbabilityAssignment ppa, DungTheory theory);

	/* (non-Javadoc)
	 * @see net.sf.tweety.arg.prob.dynamics.ChangeOperator#change(net.sf.tweety.arg.prob.semantics.ProbabilisticExtension, net.sf.tweety.arg.dung.DungTheory)
	 */
	@Override
	public ProbabilisticExtension change(ProbabilisticExtension p, DungTheory theory){
		// TODO Auto-generated method stub
		return null;
	}

}
