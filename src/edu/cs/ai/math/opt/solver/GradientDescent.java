package edu.cs.ai.math.opt.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.cs.ai.math.GeneralMathException;
import edu.cs.ai.math.opt.OptimizationProblem;
import edu.cs.ai.math.opt.Solver;
import edu.cs.ai.math.term.*;
import edu.cs.ai.util.VectorTools;

/**
 * This class implements the gradient descent method to 
 * find an optimum.
 * 
 * @author Matthias Thimm
 *
 */
public class GradientDescent extends Solver {

	/**
	 * The precision of the approximation.
	 * The actual used precision depends on the number of variables. 
	 */
	public final static double PRECISION = 0.00001;
	
	/**
	 * The max step length for the gradient descent.
	 */
	private static final double MAX_STEP_LENGTH = 0.1;
	
	/**
	 * The min step length for the gradient descent.
	 */
	private static final double MIN_STEP_LENGTH = 0.0000000000000000000001;
	
	/**
	 * The starting point for the solver.
	 */
	private Map<Variable,Term> startingPoint;
	
	/**
	 * Creates a new gradient descent solver for the given optimization problem
	 * @param problem an optimization problem
	 */
	public GradientDescent(OptimizationProblem problem, Map<Variable,Term> startingPoint) {
		super(problem);		
		if(problem.size() > 0)
			throw new IllegalArgumentException("The gradient descent method works only for optimization problems without constraints.");
		this.startingPoint = startingPoint;
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.math.opt.Solver#solve()
	 */
	@Override
	public Map<Variable, Term> solve() throws GeneralMathException {
		Term f = ((OptimizationProblem)this.getProblem()).getTargetFunction();
		if(((OptimizationProblem)this.getProblem()).getType() == OptimizationProblem.MAXIMIZE)
			f = new IntegerConstant(-1).mult(f);	
		// variables need to be ordered
		List<Variable> variables = new ArrayList<Variable>(f.getVariables());
		List<Term> gradient = new LinkedList<Term>();		
		for(Variable v: variables)
			gradient.add(f.derive(v).simplify());
		Map<Variable,Term> currentGuess = startingPoint;
		Map<Variable,Term> newGuess = new HashMap<Variable,Term>();
		List<Double> currentGradient = Term.evaluateVector(gradient, currentGuess);
		List<Double> newGradient; 
		double actualPrecision = GradientDescent.PRECISION * variables.size();
		int idx;
		double step,val;
		do{
			// find the best step length
			step = GradientDescent.MAX_STEP_LENGTH;			
			while(true){
				idx = 0;
				for(Variable v: variables){
					val = currentGuess.get(v).doubleValue()-(step * currentGradient.get(idx++));
					if(v.isPositive())
						if(val < 0)
							val = currentGuess.get(v).doubleValue() * step;
					newGuess.put(v, new FloatConstant(val));
				}
				newGradient = Term.evaluateVector(gradient, newGuess);
				if(f.replaceAllTerms(currentGuess).doubleValue() <= f.replaceAllTerms(newGuess).doubleValue()){
					step /= 2;
				}else{				
					currentGradient = newGradient;
					currentGuess.putAll(newGuess);
					break;
				}
				if(step < GradientDescent.MIN_STEP_LENGTH)
					throw new GeneralMathException();
			}			
		System.out.println(VectorTools.manhattanDistanceToZero(currentGradient));
		}while(VectorTools.manhattanDistanceToZero(currentGradient) > actualPrecision);
		return currentGuess;
	}


}
