package edu.cs.ai.math.opt;

import java.util.*;

import edu.cs.ai.math.*;
import edu.cs.ai.math.opt.solver.BfgsSolver;
import edu.cs.ai.math.term.*;

/**
 * Implements the BFGS method to find zeros of a (multi-dimensional)
 * function.
 * 
 * @author Matthias Thimm
 *
 */
public class BfgsRootFinder extends OptimizationRootFinder {

	/**
	 * Creates a new root finder for the given starting point and the given function
	 * @param startingPoint
	 */
	public BfgsRootFinder(Term function, Map<Variable,Term> startingPoint){
		super(function,startingPoint);
	}
	
	/**
	 * Creates a new root finder for the given starting point and the given
	 * (multi-dimensional) function
	 * @param startingPoint
	 */
	public BfgsRootFinder(List<Term> functions, Map<Variable,Term> startingPoint){
		super(functions,startingPoint);
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.math.opt.RootFinder#randomRoot()
	 */
	@Override
	public Map<Variable, Term> randomRoot() throws GeneralMathException {			
		return new BfgsSolver(this.buildOptimizationProblem(),this.getStartingPoint()).solve();
	}

}
