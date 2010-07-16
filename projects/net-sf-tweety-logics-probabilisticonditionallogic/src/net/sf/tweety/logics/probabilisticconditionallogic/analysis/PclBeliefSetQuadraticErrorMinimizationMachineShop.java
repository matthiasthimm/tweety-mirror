package net.sf.tweety.logics.probabilisticconditionallogic.analysis;

import java.util.*;

import org.apache.commons.logging.*;

import net.sf.tweety.*;
import net.sf.tweety.logics.probabilisticconditionallogic.*;
import net.sf.tweety.logics.probabilisticconditionallogic.syntax.*;
import net.sf.tweety.logics.propositionallogic.semantics.*;
import net.sf.tweety.logics.propositionallogic.syntax.*;
import net.sf.tweety.math.*;
import net.sf.tweety.math.equation.*;
import net.sf.tweety.math.opt.*;
import net.sf.tweety.math.opt.solver.*;
import net.sf.tweety.math.term.*;
import net.sf.tweety.util.*;

/**
 * This class is capable of restoring consistency of a possible inconsistent probabilistic
 * conditional belief set. Restoring consistency is performed by minimizing the quadratic
 * distance to the original belief set using some culpability measure, see [Diss, Thimm] for details.
 * @author Matthias Thimm
 */
public class PclBeliefSetQuadraticErrorMinimizationMachineShop implements BeliefBaseMachineShop {

	/**
	 * Logger.
	 */
	private Log log = LogFactory.getLog(PclBeliefSetQuadraticErrorMinimizationMachineShop.class);
	
	/**
	 * The culpability measure this machine shop bases on.
	 */
	private CulpabilityMeasure culpabilityMeasure;
	
	/**
	 * Creates a new machine shop based on the given culpability measure.
	 * @param culpabilityMeasure a culpability measure.
	 */
	public PclBeliefSetQuadraticErrorMinimizationMachineShop(CulpabilityMeasure culpabilityMeasure){
		this.culpabilityMeasure = culpabilityMeasure;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.BeliefBaseMachineShop#repair(net.sf.tweety.BeliefBase)
	 */
	@Override
	public BeliefBase repair(BeliefBase beliefBase) {
		if(!(beliefBase instanceof PclBeliefSet))
			throw new IllegalArgumentException("Belief base of type 'PclBeliefSet' expected.");
		PclBeliefSet beliefSet = (PclBeliefSet) beliefBase;
		// check whether the belief set is already consistent
		if(new PclDefaultConsistencyTester().isConsistent(beliefSet))
			return beliefSet;
		this.log.trace("'" + beliefSet + "' is inconsistent, preparing optimization problem to restore consistency.");
		// Create variables for the probability of each possible world and
		// set up the optimization problem for computing the minimal
		// distance to a consistent belief set.
		OptimizationProblem problem = new OptimizationProblem(OptimizationProblem.MINIMIZE);
		Set<PossibleWorld> worlds = PossibleWorld.getAllPossibleWorlds((PropositionalSignature)beliefSet.getSignature());
		Map<PossibleWorld,Variable> worlds2vars = new HashMap<PossibleWorld,Variable>();
		int i = 0;
		Term normConstraint = null;
		for(PossibleWorld w: worlds){
			FloatVariable var = new FloatVariable("w" + i++,0,1);
			worlds2vars.put(w, var);
			if(normConstraint == null)
				normConstraint = var;
			else normConstraint = normConstraint.add(var);
		}		
		problem.add(new Equation(normConstraint, new IntegerConstant(1)));
		// For each conditional add variables tau and eta and
		// add constraints implied by the conditionals
		Map<ProbabilisticConditional,Variable> taus = new HashMap<ProbabilisticConditional,Variable>();
		Map<ProbabilisticConditional,Variable> etas = new HashMap<ProbabilisticConditional,Variable>();
		Term targetFunction = null;
		i = 0;		
		for(ProbabilisticConditional c: beliefSet){
			//TODO '1000' -> infinity
			FloatVariable eta = new FloatVariable("e" + i,0,1000);
			FloatVariable tau = new FloatVariable("t" + i++,0,1000);
			taus.put(c, tau);
			etas.put(c, eta);
			// the target function is the quadratic sums of the deviations
			if(targetFunction == null)
				targetFunction = tau.add(eta).mult(tau.add(eta));
			else targetFunction = targetFunction.add(tau.add(eta).mult(tau.add(eta)));
			Term leftSide = null;
			Term rightSide = null;
			if(c.isFact()){
				for(PossibleWorld w: worlds)
					if(w.satisfies(c.getConclusion())){
						if(leftSide == null)
							leftSide = worlds2vars.get(w);
						else leftSide = leftSide.add(worlds2vars.get(w));
					}
				rightSide = new FloatConstant(c.getProbability().getValue()).add(new FloatConstant(this.culpabilityMeasure.culpabilityMeasure(beliefSet, c)).mult(eta.minus(tau)));
			}else{				
				PropositionalFormula body = c.getPremise().iterator().next();
				PropositionalFormula head_and_body = (PropositionalFormula) c.getConclusion().combineWithAnd(body);
				for(PossibleWorld w: worlds){
					if(w.satisfies(head_and_body)){
						if(leftSide == null)
							leftSide = worlds2vars.get(w);
						else leftSide = leftSide.add(worlds2vars.get(w));
					}
					if(w.satisfies(body)){
						if(rightSide == null)
							rightSide = worlds2vars.get(w);
						else rightSide = rightSide.add(worlds2vars.get(w));
					}					
				}
				if(rightSide == null)
					rightSide = new FloatConstant(0);
				else rightSide = rightSide.mult(new FloatConstant(c.getProbability().getValue()).add(new FloatConstant(this.culpabilityMeasure.culpabilityMeasure(beliefSet, c)).mult(eta.minus(tau))));
			}
			problem.add(new Equation(leftSide,rightSide));
		}			
		problem.setTargetFunction(targetFunction);		
		// Get a feasible starting point for the optimization
		List<Term> functions = new ArrayList<Term>();
		Map<Variable,Term> startingPoint = new HashMap<Variable,Term>();
		for(Variable v: problem.getVariables())
			startingPoint.put(v, new FloatConstant(0));
		// all statements are equations
		for(Statement s: problem)
			functions.add(s.getLeftTerm().minus(s.getRightTerm()));
		OpenOptRootFinder rootFinder = new OpenOptRootFinder(functions,startingPoint);
		// Solve the problem using the OpenOpt library
		this.log.trace("Problem prepared, now finding feasible starting point.");
		// TODO make the following a little better
		rootFinder.contol = 1e-8;
		rootFinder.xtol = 1e-8;
		rootFinder.ftol = 1e-8;
		rootFinder.gtol = 1e-8;
		try{
			// TODO the following should be changed
			RootFinder.PRECISION = 0.01;
			Map<Variable,Term> startingPointForOptimization = rootFinder.randomRoot();
			this.log.trace("Starting point found, now solving the main problem.");
			Solver solver = new OpenOptSolver(problem,startingPointForOptimization);
			Map<Variable,Term> solution = solver.solve();
			this.log.trace("Problem solved, modifying belief set.");
			// Modify belief set
			PclBeliefSet newBeliefSet = new PclBeliefSet();
			for(ProbabilisticConditional pc: beliefSet){
				Double p = pc.getProbability().getValue();
				p += this.culpabilityMeasure.culpabilityMeasure(beliefSet, pc) * (solution.get(etas.get(pc)).doubleValue() - solution.get(taus.get(pc)).doubleValue());
				//System.out.println(p + " = " + this.culpabilityMeasure.culpabilityMeasure(beliefSet, pc)  + " * ( "+ solution.get(etas.get(pc)).doubleValue() + " - " + solution.get(taus.get(pc)).doubleValue() + " )");
				newBeliefSet.add(new ProbabilisticConditional(pc,new Probability(p)));
			}
			System.out.println("Really consistent: " + new PclDefaultConsistencyTester().isConsistent(newBeliefSet));
			return newBeliefSet;
		}catch (GeneralMathException e){
			// This should not happen as the optimization problem is guaranteed to be feasible
			throw new RuntimeException("Fatal error: Optimization problem to restore consistency is not feasible: " + e.getMessage());
		}
	}

}
