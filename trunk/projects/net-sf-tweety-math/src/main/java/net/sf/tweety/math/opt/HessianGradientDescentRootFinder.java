package net.sf.tweety.math.opt;

import java.util.*;

import net.sf.tweety.math.*;
import net.sf.tweety.math.opt.solver.*;
import net.sf.tweety.math.term.*;


/**
 * Implements the hessiane/gradient descent method to find zeros of a (multi-dimensional)
 * function.
 * 
 * @author Matthias Thimm
 *
 */
public class HessianGradientDescentRootFinder extends OptimizationRootFinder {
	
	/**
	 * Creates a new root finder for the given starting point and the given function
	 * @param startingPoint
	 */
	public HessianGradientDescentRootFinder(Term function, Map<Variable,Term> startingPoint){
		super(function,startingPoint);
		//check whether the solver is installed
		if(!HessianGradientDescent.isInstalled())
			throw new RuntimeException("Cannot instantiate HessianGradientDescentRootFinder as the HessianGradientDescent solver is not installed.");
	}
	
	/**
	 * Creates a new root finder for the given starting point and the given
	 * (multi-dimensional) function
	 * @param startingPoint
	 */
	public HessianGradientDescentRootFinder(List<Term> functions, Map<Variable,Term> startingPoint){
		super(functions,startingPoint);
		//check whether the solver is installed
		if(!HessianGradientDescent.isInstalled())
			throw new RuntimeException("Cannot instantiate HessianGradientDescentRootFinder as the HessianGradientDescent solver is not installed.");
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.math.opt.RootFinder#randomRoot()
	 */
	@Override
	public Map<Variable, Term> randomRoot() throws GeneralMathException {			
		return new HessianGradientDescent(this.getStartingPoint()).solve(this.buildOptimizationProblem());
	}

}
