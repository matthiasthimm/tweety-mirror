package net.sf.tweety.math.opt;

import java.util.*;

import org.apache.commons.logging.*;

import net.sf.tweety.math.*;
import net.sf.tweety.math.term.*;


/**
 * This class is the common ancestor for root finders that work with optimizers.
 * 
 * @author Matthias Thimm
 *
 */
public abstract class OptimizationRootFinder extends RootFinder {

	/**
	 * Logger.
	 */
	private Log log = LogFactory.getLog(OptimizationRootFinder.class);
	
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
		this.log.trace("Constructing optimization problem to find a root of the function '" + this.getFunctions() + "'.");
		OptimizationProblem problem = new OptimizationProblem(OptimizationProblem.MINIMIZE);
		Term target = null;
		for(Term f: this.getFunctions())
			if(target == null)
				target = f.mult(f);
			else target = target.add(f.mult(f));
		problem.setTargetFunction(target);
		this.log.trace("Constructing optimization problem finished; the target function is '" + target + "'.");
		return problem;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.math.opt.RootFinder#randomRoot()
	 */
	@Override
	public abstract Map<Variable, Term> randomRoot() throws GeneralMathException;

}