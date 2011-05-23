package net.sf.tweety.logics.probabilisticconditionallogic.analysis;

import java.io.*;
import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.logics.probabilisticconditionallogic.*;
import net.sf.tweety.logics.probabilisticconditionallogic.parser.*;
import net.sf.tweety.logics.probabilisticconditionallogic.semantics.*;
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
 * This consistency restorer uses the idea of the upper approximative distance minimization inconsistency measure to compute a
 * ME-probability distribution and adjust the probabilities of the conditionals accordingly.
 * 
 * @author Matthias Thimm
 */
public class MaximumEntropyMachineShop implements BeliefBaseMachineShop {

	/* (non-Javadoc)
	 * @see net.sf.tweety.BeliefBaseMachineShop#repair(net.sf.tweety.BeliefBase)
	 */
	@Override
	public BeliefBase repair(BeliefBase beliefBase) {
		if(!(beliefBase instanceof PclBeliefSet))
			throw new IllegalArgumentException("Belief base of type 'PclBeliefSet' expected.");
		PclBeliefSet beliefSet = (PclBeliefSet) beliefBase;
		PclDefaultConsistencyTester tester = new PclDefaultConsistencyTester();
		if(tester.isConsistent(beliefSet))
			return beliefSet;
		// Determine unique values mu/nu that represent minimal adjustments for
		// restoring consistency
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
		// For each conditional add variables mu and nu and
		// add constraints implied by the conditionals
		Map<ProbabilisticConditional,Variable> mus = new HashMap<ProbabilisticConditional,Variable>();
		Map<ProbabilisticConditional,Variable> nus = new HashMap<ProbabilisticConditional,Variable>();
		Term targetFunction = null;
		i = 0;		
		for(ProbabilisticConditional c: beliefSet){
			FloatVariable mu = new FloatVariable("m" + i,0,1);
			FloatVariable nu = new FloatVariable("n" + i++,0,1);
			mus.put(c, mu);
			nus.put(c, nu);
			if(targetFunction == null)
				targetFunction = mu.add(nu).mult(mu.add(nu));
			else targetFunction = targetFunction.add(mu.add(nu).mult(mu.add(nu)));
			Term leftSide = null;
			Term rightSide = null;
			if(c.isFact()){
				for(PossibleWorld w: worlds)
					if(w.satisfies(c.getConclusion())){
						if(leftSide == null)
							leftSide = worlds2vars.get(w);
						else leftSide = leftSide.add(worlds2vars.get(w));
					}
				rightSide = new FloatConstant(c.getProbability().getValue()).add(mu).minus(nu);
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
				else{
					rightSide = rightSide.mult(new FloatConstant(c.getProbability().getValue()));
					rightSide = rightSide.add(mu).minus(nu);
				}
			}
			if(leftSide == null)
				leftSide = new FloatConstant(0);
			if(rightSide == null)
				rightSide = new FloatConstant(0);
			problem.add(new Equation(leftSide,rightSide));			
		}
		problem.setTargetFunction(targetFunction);
		try{			
			OpenOptSolver solver = new OpenOptSolver(problem);
			solver.contol = 1e-6;
			solver.gtol = 1e-30;
			solver.ftol = 1e-30;
			solver.xtol = 1e-30;
			//solver.ignoreNotFeasibleError = true;
			Map<Variable,Term> solution = solver.solve();
			// insert the mu/nu solution into the optimization problem
			for(ProbabilisticConditional pc: beliefSet){
				problem.add(new Equation(mus.get(pc),solution.get(mus.get(pc))));
				problem.add(new Equation(nus.get(pc),solution.get(nus.get(pc))));
			}
			// the new target function is the entropy of the probability function
			targetFunction = null;
			for(Variable v: worlds2vars.values()){
				if(targetFunction == null)
					targetFunction = v.mult(new Logarithm(v));
				else targetFunction = targetFunction.add(v.mult(new Logarithm(v)));
			}
			problem.setTargetFunction(targetFunction);
			//solve for me-distribution
			solver = new OpenOptSolver(problem);
			solver.contol = 0.0001;
			solver.ftol = 0.0001;
			solver.gtol = 0.0001;
			solver.xtol = 0.0001;
			solver.solver = "ralg";
			//System.out.println(solver.getOpenOptCode());
			solution = solver.solve();
			// construct probability distribution
			ProbabilityDistribution<PossibleWorld> meDistribution = new ProbabilityDistribution<PossibleWorld>(beliefSet.getSignature());
			for(PossibleWorld world: worlds)
				meDistribution.put(world, new Probability(solution.get(worlds2vars.get(world)).doubleValue()));
			// prepare result
			PclBeliefSet result = new PclBeliefSet();
			for(ProbabilisticConditional pc: beliefSet)
				result.add(new ProbabilisticConditional(pc,meDistribution.probability(pc)));							
			return result;			
		}catch (GeneralMathException e){
			// This should not happen as the optimization problem is guaranteed to be feasible
			throw new RuntimeException("Fatal error: Optimization problem to compute the minimal distance to a consistent knowledge base is not feasible.");
		}
	}

	public static void main(String[] args) throws FileNotFoundException, ParserException, IOException{
		PclBeliefSet kb = (PclBeliefSet) new PclParser().parseBeliefBaseFromFile("/Users/mthimm/Desktop/R8.pcl");
		System.out.println("INITIAL: " + kb);
		
		System.out.println();
		System.out.println();
		ConvexAggregatingMaxConsMeMachineShop mshop = new ConvexAggregatingMaxConsMeMachineShop();
		
		System.out.println("RESULT: " + mshop.repair(kb));		
	}
	
	
}
