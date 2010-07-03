package edu.cs.ai.math.opt;

import java.util.*;

import edu.cs.ai.math.*;
import edu.cs.ai.math.term.*;

/**
 * This class is the common ancestor for root finders that work with optimizers.
 * 
 * @author Matthias Thimm
 *
 */
public abstract class OptimizationRootFinder extends RootFinder {

	/**
	 * Creates a new root finder for the given function.
	 * @param function a term
	 */
	public OptimizationRootFinder(Term function){
		super(function);
	}
	
	/**
	 * Creates a new root finder for the given starting point and the given function
	 * @param startingPoint
	 */
	public OptimizationRootFinder(Term function, Map<Variable,Term> startingPoint){
		super(function,startingPoint);
	}
	
	/**
	 * Creates a new root finder for the given starting point and the given
	 * (multi-dimensional) function
	 * @param startingPoint
	 */
	public OptimizationRootFinder(List<Term> functions, Map<Variable,Term> startingPoint){
		super(functions,startingPoint);
	}

	/**
	 * Builds an optimization problem for the task of root finding.
	 * @return an optimization problem for the task of root finding.
	 */
	protected OptimizationProblem buildOptimizationProblem(){
		OptimizationProblem problem = new OptimizationProblem(OptimizationProblem.MINIMIZE);
		Term target = null;
		for(Term f: this.getFunctions())
			if(target == null)
				target = f.mult(f);
			else target = target.add(f.mult(f));
		problem.setTargetFunction(target);
		return problem;
	}
	
	@Override
	public Map<Variable, Term> randomRoot() throws GeneralMathException {
		// TODO Auto-generated method stub
		return null;
	}

}
