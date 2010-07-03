package edu.cs.ai.math.opt;

import java.util.List;
import java.util.Map;

import edu.cs.ai.math.GeneralMathException;
import edu.cs.ai.math.opt.solver.GradientDescent;
import edu.cs.ai.math.term.*;

/**
 * Implements the gradient descent method to find zeros of a (multi-dimensional)
 * function.
 * 
 * @author Matthias Thimm
 *
 */
public class GradientDescentRootFinder extends OptimizationRootFinder {
	
	/**
	 * Creates a new root finder for the given starting point and the given function
	 * @param startingPoint
	 */
	public GradientDescentRootFinder(Term function, Map<Variable,Term> startingPoint){
		super(function,startingPoint);
	}
	
	/**
	 * Creates a new root finder for the given starting point and the given
	 * (multi-dimensional) function
	 * @param startingPoint
	 */
	public GradientDescentRootFinder(List<Term> functions, Map<Variable,Term> startingPoint){
		super(functions,startingPoint);
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.math.opt.RootFinder#randomRoot()
	 */
	@Override
	public Map<Variable, Term> randomRoot() throws GeneralMathException {			
		return new GradientDescent(this.buildOptimizationProblem(),this.getStartingPoint()).solve();
	}

}
