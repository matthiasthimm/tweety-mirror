package net.sf.tweety.math.opt.solver;

import java.util.*;

import net.sf.tweety.math.*;
import net.sf.tweety.math.equation.*;
import net.sf.tweety.math.opt.*;
import net.sf.tweety.math.term.*;


/**
 * This class implements a (naive) Langrange solver for optimization problems.<br>
 * <br>
 * This solver only considers optimization problems without inequations. The solution
 * given by this solver is not verified as it only checks for necessary optimality
 * conditions.
 * 
 * @author Matthias Thimm
 */
public class LagrangeSolver implements Solver {

	/**
	 * The starting points for finding the optimum.
	 */
	private Set<Map<Variable,Term>> startingPoints;
	
	/**
	 * possible starting points for Lagrange multiplicators.
	 */
	private Map<Statement,Double> startingPointsLMult = new HashMap<Statement,Double>();

	/**
	 * Creates a new Lagrange solver for the given 
	 * optimization problem
	 * @param startingPoint The starting point for finding the optimum.
	 */
	public LagrangeSolver(Map<Variable,Term> startingPoint){
		this.startingPoints = new HashSet<Map<Variable,Term>>();
		this.startingPoints.add(startingPoint);
	}
	
	/**
	 * Creates a new Lagrange solver for the given 
	 * optimization problem
	 * @param startingPoints Some starting points for finding the optimum.
	 */
	public LagrangeSolver(Set<Map<Variable,Term>> startingPoints){
		this.startingPoints = startingPoints;
	}

	public void setStartingPointsLMult(Map<Statement,Double> startingPointsLMult){
		this.startingPointsLMult = startingPointsLMult;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.math.opt.Solver#solve()
	 */
	@Override
	public Map<Variable, Term> solve(ConstraintSatisfactionProblem prob) throws GeneralMathException {
		for(Statement s: prob)
			if(!(s instanceof Equation))
				throw new IllegalArgumentException("This solver expects optimizations problems without inequations.");
		// for convenience we consider maximization problems.
		OptimizationProblem problem;
		if(((OptimizationProblem)prob).getType() == OptimizationProblem.MAXIMIZE)
			problem = (OptimizationProblem)prob;
		else{
			problem = new OptimizationProblem(OptimizationProblem.MAXIMIZE);
			problem.addAll(prob);
			problem.setTargetFunction(new IntegerConstant(-1).mult(((OptimizationProblem)prob).getTargetFunction()));
		}
		Set<Variable> vars = problem.getVariables();
		// construct lagrangian
		Term l = problem.getTargetFunction();
		// for each constraint add the corresponding term
		// and add starting points for the Lagrange multiplicators
		int idx = 0;
		Set<Variable> langMult = new HashSet<Variable>();
		for(Statement s: problem){
			Variable lm = new FloatVariable("LAMBDA" + idx++);
			vars.add(lm);		
			langMult.add(lm);			
			if(this.startingPointsLMult.containsKey(s)){
				for(Map<Variable,Term> startingPoint: this.startingPoints)
					startingPoint.put(lm, new FloatConstant(this.startingPointsLMult.get(s)));				
			}
			l = l.add(lm.mult(s.toNormalizedForm().getLeftTerm()));
		}
		// get the gradient
		List<Term> partialDerivations = new ArrayList<Term>();
		System.out.println("Determining gradient...");
		for(Variable v: vars){
			try{
				partialDerivations.add(l.derive(v).simplify());
			}catch(NonDifferentiableException e){
				throw new NonDifferentiableException("The Lagrange function is not differentiable.");
			}
		}
		System.out.println("Determining gradient... finished");		
		// try out the starting points until we find a solution
		BfgsRootFinder rootFinder = null;
		for(Map<Variable,Term> startingPoint: this.startingPoints){
			try{
				Map<Variable,Term> actualStartingPoint = new HashMap<Variable,Term>();
				actualStartingPoint.putAll(startingPoint);
				// add starting points for Lagrange multiplicators				
				for(Variable lm: langMult)
					if(!actualStartingPoint.containsKey(lm))
						actualStartingPoint.put(lm, new FloatConstant(-1));
				if(rootFinder == null)
					rootFinder = new BfgsRootFinder(partialDerivations,actualStartingPoint);
				else rootFinder.setStartingPoint(actualStartingPoint);
				// now find the (single?) zero point of the gradient
				return rootFinder.randomRoot();
			}catch(GeneralMathException e){
				// -> Bad starting point, try again
			}
			catch(ProblemInconsistentException e){
				// -> Bad starting point, try again
			}
		}
		throw new GeneralMathException("No feasible solution.");		
	}

}
