package net.sf.tweety.math.opt;

import java.util.*;

import net.sf.tweety.math.*;
import net.sf.tweety.math.opt.solver.*;
import net.sf.tweety.math.term.*;


/**
 * A wrapper for the open opt library.
 * 
 * @author Matthias Thimm
 *
 */
public class OpenOptRootFinder extends OptimizationRootFinder {

	/**
	 * Creates a new root finder for the given starting point and the given function
	 * @param startingPoint
	 */
	public OpenOptRootFinder(Term function, Map<Variable,Term> startingPoint){
		super(function,startingPoint);
	}
	
	/**
	 * Creates a new root finder for the given starting point and the given
	 * (multi-dimensional) function
	 * @param startingPoint
	 */
	public OpenOptRootFinder(List<Term> functions, Map<Variable,Term> startingPoint){
		super(functions,startingPoint);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.math.opt.RootFinder#randomRoot()
	 */
	@Override
	public Map<Variable, Term> randomRoot() throws GeneralMathException {
		OpenOptSolver solver = new OpenOptSolver(this.buildOptimizationProblem(),this.getStartingPoint());
		// set some parameters
		solver.contol = 1e-15;
		solver.xtol = 1e-15;
		solver.ftol = 1e-15;
		solver.gtol = 1e-15;
		Map<Variable,Term> solution = solver.solve();
		// Check whether the solution is really a root
		for(Term t: this.getFunctions()){
			Double val = t.replaceAllTerms(solution).doubleValue();
			if(val < -RootFinder.PRECISION || val > RootFinder.PRECISION  )
				throw new GeneralMathException("The given function has no root.");
		}
		return solution;
	}

}