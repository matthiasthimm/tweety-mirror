package edu.cs.ai.math.opt.solver;

import java.util.Map;

import edu.cs.ai.math.opt.ConstraintSatisfactionProblem;
import edu.cs.ai.math.term.Term;
import edu.cs.ai.math.term.Variable;
//import choco.*;


/**
 * see http://choco.sourceforge.net
 * @author Matthias Thimm
 *
 */
public class Choco extends edu.cs.ai.math.opt.Solver {

	public Choco(ConstraintSatisfactionProblem problem) {
		super(problem);
		throw new UnsupportedOperationException("IMPLEMENT ME!");
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.math.opt.Solver#solve()
	 */
	@Override
	public Map<Variable, Term> solve() {
		throw new UnsupportedOperationException("IMPLEMENT ME!");
	}
	
}
