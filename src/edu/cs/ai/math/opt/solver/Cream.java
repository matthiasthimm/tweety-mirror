package edu.cs.ai.math.opt.solver;

import java.util.Map;

import edu.cs.ai.math.opt.ConstraintSatisfactionProblem;
import edu.cs.ai.math.term.Term;
import edu.cs.ai.math.term.Variable;
//import jp.ac.kobe_u.cs.cream.*;

/**
 * See http://bach.istc.kobe-u.ac.jp/cream/
 * @author Matthias Thimm
 */
public class Cream extends edu.cs.ai.math.opt.Solver {

	/**
	 * Creates a new Cream solver.
	 * @param problem a csp
	 */
	public Cream(ConstraintSatisfactionProblem problem) {
		super(problem);
		throw new UnsupportedOperationException("IMPLEMENT ME");
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.math.opt.Solver#solve()
	 */
	@Override
	public Map<Variable, Term> solve() {
		throw new UnsupportedOperationException("IMPLEMENT ME");		
	}

}
