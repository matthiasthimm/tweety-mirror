package edu.cs.ai.kr.pcl.analysis;

import java.util.*;

import edu.cs.ai.kr.pcl.*;
import edu.cs.ai.kr.pcl.syntax.*;
import edu.cs.ai.kr.pl.semantics.*;
import edu.cs.ai.kr.pl.syntax.*;
import edu.cs.ai.math.equation.*;
import edu.cs.ai.math.opt.*;
import edu.cs.ai.math.term.*;

/**
 * This class models the distance minimization inconsistency measure as proposed in [Thimm,UAI,2009].
 * 
 * @author Matthias Thimm
 */
public class DistanceMinimizationInconsistencyMeasure implements InconsistencyMeasure {

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.pcl.analysis.InconsistencyMeasure#inconsistencyMeasure(edu.cs.ai.kr.pcl.PclBeliefSet)
	 */
	@Override
	public Double inconsistencyMeasure(PclBeliefSet beliefSet) {
		// first check whether the belief set is consistent
		if(new PclDefaultConsistencyTester().isConsistent(beliefSet))
			return new Double(0);
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
		// For each conditional add variables tau and eta
		// add constraints implied by the conditionals
		Map<ProbabilisticConditional,Variable> taus = new HashMap<ProbabilisticConditional,Variable>();
		Map<ProbabilisticConditional,Variable> etas = new HashMap<ProbabilisticConditional,Variable>();
		Term targetFunction = null;
		i = 0;		
		for(ProbabilisticConditional c: beliefSet){
			FloatVariable eta = new FloatVariable("e" + i,0,1);
			FloatVariable tau = new FloatVariable("t" + i++,0,1);
			taus.put(c, tau);
			etas.put(c, eta);
			if(targetFunction == null)
				targetFunction = tau.add(eta);
			else targetFunction = targetFunction.add(tau).add(eta);
			Term leftSide = null;
			Term rightSide = null;
			if(c.isFact()){
				for(PossibleWorld w: worlds)
					if(w.satisfies(c.getConclusion())){
						if(leftSide == null)
							leftSide = worlds2vars.get(w);
						else leftSide = leftSide.add(worlds2vars.get(w));
					}
				rightSide = new FloatConstant(c.getProbability().getValue()).add(eta).minus(tau);
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
					rightSide = rightSide.mult(new FloatConstant(c.getProbability().getValue()).add(eta).minus(tau));
				}
			}
			problem.add(new Equation(leftSide,rightSide));
		}			
		problem.setTargetFunction(targetFunction);		
		//TODO go on
		return null;
	}

}
