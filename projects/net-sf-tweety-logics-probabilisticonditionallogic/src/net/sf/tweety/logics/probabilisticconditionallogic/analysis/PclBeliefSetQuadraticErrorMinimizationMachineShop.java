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
		// For each conditional add a variable tau and
		// add constraints implied by the conditionals
		Map<ProbabilisticConditional,Variable> taus = new HashMap<ProbabilisticConditional,Variable>();
		Term targetFunction = null;
		i = 0;		
		for(ProbabilisticConditional c: beliefSet){
			FloatVariable tau = new FloatVariable("t" + i++,-1,1);
			taus.put(c, tau);
			// the target function is the quadratic sums of the deviations
			if(targetFunction == null)
				targetFunction = tau.mult(tau);
			else targetFunction = targetFunction.add(tau.mult(tau));
			Term leftSide = null;
			Term rightSide = null;
			if(c.isFact()){
				for(PossibleWorld w: worlds)
					if(w.satisfies(c.getConclusion())){
						if(leftSide == null)
							leftSide = worlds2vars.get(w);
						else leftSide = leftSide.add(worlds2vars.get(w));
					}
				rightSide = new FloatConstant(c.getProbability().getValue()).add(tau);
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
				else rightSide = rightSide.mult(new FloatConstant(c.getProbability().getValue()).add(tau));
			}
			if(leftSide == null)
				leftSide = new FloatConstant(0);
			if(rightSide == null)
				rightSide = new FloatConstant(0);
			problem.add(new Equation(leftSide,rightSide));
		}		
		// add constraints to ensure conformity
		for(Set<ProbabilisticConditional> pair: new SetTools<ProbabilisticConditional>().subsets(beliefSet, 2)){
			Iterator<ProbabilisticConditional> it = pair.iterator();
			ProbabilisticConditional pc1 = it.next();
			ProbabilisticConditional pc2 = it.next();
			if(this.culpabilityMeasure.culpabilityMeasure(beliefSet, pc1).equals(this.culpabilityMeasure.culpabilityMeasure(beliefSet, pc2)))
				problem.add(new Equation(taus.get(pc1).mult(taus.get(pc1)),taus.get(pc2).mult(taus.get(pc2))));
			else if(this.culpabilityMeasure.culpabilityMeasure(beliefSet, pc1) > this.culpabilityMeasure.culpabilityMeasure(beliefSet, pc2))
				problem.add(new Inequation(taus.get(pc1).mult(taus.get(pc1)),taus.get(pc2).mult(taus.get(pc2)),Inequation.GREATER));
			else problem.add(new Inequation(taus.get(pc2).mult(taus.get(pc2)),taus.get(pc1).mult(taus.get(pc1)),Inequation.GREATER));
		}		
		problem.setTargetFunction(targetFunction);		
		try{
			OpenOptSolver solver = new OpenOptSolver(problem);
			solver.contol = 1e-4;
			solver.gtol = 1e-60;
			solver.xtol = 1e-60;
			solver.ftol = 1e-60;
			solver.solver = "ralg";
			Map<Variable,Term> solution = solver.solve();
			this.log.trace("Problem solved, modifying belief set.");
			// Modify belief set
			PclBeliefSet newBeliefSet = new PclBeliefSet();
			for(ProbabilisticConditional pc: beliefSet){
				Double p = pc.getProbability().getValue();
				p += solution.get(taus.get(pc)).doubleValue();
				newBeliefSet.add(new ProbabilisticConditional(pc,new Probability(p)));
			}
			return newBeliefSet;
		}catch (GeneralMathException e){
			// This should not happen as the optimization problem is guaranteed to be feasible
			throw new RuntimeException("Fatal error: Optimization problem to restore consistency is not feasible: " + e.getMessage());
		}
	}

}
