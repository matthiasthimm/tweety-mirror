package net.sf.tweety.arg.prob.dynamics;

import net.sf.tweety.arg.dung.DungTheory;
import net.sf.tweety.arg.prob.PartialProbabilityAssignment;
import net.sf.tweety.arg.prob.semantics.ProbabilisticExtension;

/**
 * This operator implements a revision of some probabilistic assessment of 
 * arguments upon the observation of an argumentation theory. More specifically, for
 * a given probabilistic semantics S, some norm N, a function f, and a partial probability assignment PPA,
 * it computes the PPA-compliant prob-function that 1.) has minimal N-distance to S-prob'functions and 2.) maximizes
 * function f.  
 * @author Matthias Thimm
 */
public class PARevisionOperator extends AbstractPAChangeOperator {
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.arg.prob.dynamics.ChangeOperator#change(net.sf.tweety.arg.prob.PartialProbabilityAssignment, net.sf.tweety.arg.dung.DungTheory)
	 */
	@Override
	public ProbabilisticExtension change(PartialProbabilityAssignment ppa, DungTheory theory) {
		// TODO Auto-generated method stub
		return null;
	}
}
