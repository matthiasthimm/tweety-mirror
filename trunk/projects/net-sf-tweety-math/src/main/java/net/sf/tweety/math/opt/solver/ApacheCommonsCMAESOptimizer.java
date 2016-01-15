package net.sf.tweety.math.opt.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimplePointChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer;
import org.apache.commons.math3.random.JDKRandomGenerator;

import net.sf.tweety.math.GeneralMathException;
import net.sf.tweety.math.opt.ConstraintSatisfactionProblem;
import net.sf.tweety.math.opt.OptimizationProblem;
import net.sf.tweety.math.opt.Solver;
import net.sf.tweety.math.term.FloatConstant;
import net.sf.tweety.math.term.Term;
import net.sf.tweety.math.term.Variable;

/**
 * This class is a wrapper for the Apache Commons Math3 CMAES optimizer.
 * See https://commons.apache.org/proper/commons-math/  
 * @author Matthias Thimm
 */
public class ApacheCommonsCMAESOptimizer extends Solver{

	/** population size */
	private int populationSize;
	/** Maximal number of iterations.*/
	private int maxIterations; 
	/** Whether to stop if objective function value is smaller than stopFitness. */
	private double stopFitness;
	/** Chooses the covariance matrix update method. */ 
	private boolean isActiveCMA;
	/** Number of initial iterations, where the covariance matrix remains diagonal. */
	private int diagonalOnly;
	/** Determines how often new random objective variables are generated in case they are out of bounds. */
	private int checkFeasableCount; 
	/** the precision of the optimization*/
	private double precision;
	
	/**
	 * Parameters from org.apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer:
	 * @param maxIterations Maximal number of iterations.
	 * @param stopFitness Whether to stop if objective function value is smaller than stopFitness.
	 * @param isActiveCMA Chooses the covariance matrix update method.
	 * @param diagonalOnly Number of initial iterations, where the covariance matrix remains diagonal.
	 * @param checkFeasableCount Determines how often new random objective variables are generated in case they are out of bounds.
	 * @param precision the precision of the optimization
	 */
	public ApacheCommonsCMAESOptimizer(int populationSize, int maxIterations, double stopFitness, boolean isActiveCMA,int diagonalOnly, int checkFeasableCount, double precision){
		this.populationSize = populationSize;
		this.maxIterations = maxIterations;
		this.stopFitness = stopFitness;
		this.isActiveCMA = isActiveCMA;
		this.diagonalOnly = diagonalOnly;
		this.checkFeasableCount = checkFeasableCount;
		this.precision = precision;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.math.opt.Solver#solve(net.sf.tweety.math.opt.ConstraintSatisfactionProblem)
	 */
	@Override
	public Map<Variable, Term> solve(ConstraintSatisfactionProblem problem) throws GeneralMathException {
		// only optimization problems
		if(!(problem instanceof OptimizationProblem))
			throw new IllegalArgumentException("Only optimization problems allowed for this solver.");
		OptimizationProblem p = (OptimizationProblem) problem; 
		// only box constraints allowed (so far)
		if(!p.isEmpty())
			throw new IllegalArgumentException("Only optimization problems with box constraints on variables allowed for this solver (no other constraints.");
		final List<Variable> vars = new ArrayList<Variable>(p.getTargetFunction().getVariables());
		double[] lb = new double[vars.size()];
		double[] ub = new double[vars.size()];
		double[] s = new double[vars.size()];
		double[] sigma = new double[vars.size()];
		for(int i = 0; i < vars.size(); i++){
			lb[i] = vars.get(i).getLowerBound();
			ub[i] = vars.get(i).getUpperBound();
			s[i] = (lb[i]+ub[i])/2;
			sigma[i] = ub[i]-lb[i];
		}
		final Term targetFunction = p.getTargetFunction();
		MultivariateFunction target = new MultivariateFunction(){
			@Override
			public double value(double[] arg0) {	
				return targetFunction.replaceAllTerms(arg0, vars).doubleValue();
			}			
		};		
		// construct solver
		CMAESOptimizer optimizer = new CMAESOptimizer(this.maxIterations, this.stopFitness, this.isActiveCMA, this.diagonalOnly, this.checkFeasableCount, new JDKRandomGenerator(), false, new SimplePointChecker<PointValuePair>(this.precision,this.precision));
		PointValuePair val = optimizer.optimize(new CMAESOptimizer.Sigma(sigma),
				new ObjectiveFunction(target),
				new InitialGuess(s),
				p.getType() == OptimizationProblem.MAXIMIZE ? GoalType.MAXIMIZE : GoalType.MINIMIZE,
				new MaxEval(this.maxIterations),
				new SimpleBounds(lb,ub),
				new CMAESOptimizer.PopulationSize(this.populationSize));
		Map<Variable,Term> result = new HashMap<Variable,Term>();
		for(int i = 0; i < vars.size(); i++)
			result.put(vars.get(i), new FloatConstant(val.getPoint()[i]));
		return result;
	}

}
