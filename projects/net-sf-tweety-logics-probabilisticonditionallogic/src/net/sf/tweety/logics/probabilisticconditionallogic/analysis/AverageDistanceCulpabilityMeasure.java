package net.sf.tweety.logics.probabilisticconditionallogic.analysis;

import java.util.*;

import org.apache.commons.logging.*;

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
 * This class implements the average distance culpability measure, see [Diss, Thimm]
 * for details.
 * @author Matthias Thimm
 */
public class AverageDistanceCulpabilityMeasure implements SignableCulpabilityMeasure {

	/**
	 * Logger.
	 */
	private Log log = LogFactory.getLog(AverageDistanceCulpabilityMeasure.class);
	
	/**
	 * For archiving. 
	 */
	private Map<Triple<PclBeliefSet,ProbabilisticConditional,Double>,Double> archive = new HashMap<Triple<PclBeliefSet,ProbabilisticConditional,Double>,Double>();
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.probabilisticconditionallogic.analysis.CulpabilityMeasure#culpabilityMeasure(net.sf.tweety.logics.probabilisticconditionallogic.PclBeliefSet, net.sf.tweety.logics.probabilisticconditionallogic.syntax.ProbabilisticConditional)
	 */
	@Override
	public Double culpabilityMeasure(PclBeliefSet beliefSet, ProbabilisticConditional conditional) {
		Double result = Math.abs((this.getMinimumDistance(beliefSet, conditional)+this.getMaximumDistance(beliefSet, conditional))/2);
		this.log.debug("Culpability of '" + conditional + "' is '" + result + "'.");
		return result;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.probabilisticconditionallogic.analysis.SignableCulpabilityMeasure#culpabilitySignum(net.sf.tweety.logics.probabilisticconditionallogic.PclBeliefSet, net.sf.tweety.logics.probabilisticconditionallogic.syntax.ProbabilisticConditional)
	 */
	@Override
	public Double culpabilitySignum(PclBeliefSet beliefSet, ProbabilisticConditional conditional) {
		return Math.signum(this.getMinimumDistance(beliefSet, conditional)+this.getMaximumDistance(beliefSet, conditional));
	}
	
	/**
	 * Returns the minimal necessary value by that the given conditional must deviate
	 * in order to render the given belief set consistent (neglecting deviations by other
	 * conditionals). 
	 * @param beliefSet a belief set.
	 * @param conditional a probabilistic conditional.
	 * @return the minimum necessary deviation.
	 */
	private Double getMinimumDistance(PclBeliefSet beliefSet, ProbabilisticConditional conditional){
		Double d1 = this.getWeightedDistance(beliefSet, conditional, 0.0000005);
		Double d2 = this.getWeightedDistance(beliefSet, conditional, 1.5);
		return (d1>d2)?(d2):(d1);
	}

	/**
	 * Returns the maximal minimal value by that the given conditional must deviate
	 * in order to render the given belief set consistent (neglecting deviations by other
	 * conditionals). 
	 * @param beliefSet a belief set.
	 * @param conditional a probabilistic conditional.
	 * @return the maximal minimal deviation.
	 */
	private Double getMaximumDistance(PclBeliefSet beliefSet, ProbabilisticConditional conditional){
		Double d1 = this.getWeightedDistance(beliefSet, conditional, 0.0000005);
		Double d2 = this.getWeightedDistance(beliefSet, conditional, 1.5);
		return (d1>d2)?(d1):(d2);
	}
	
	/**
	 * Returns the deviation of the given conditional when weighted with the given
	 * parameter.
	 * @param beliefSet a belief set
	 * @param conditional a probabilistic conditional
	 * @param weigth a weight
	 * @return the deviation of the given conditional when weighted with the given
	 * parameter.
	 */
	private Double getWeightedDistance(PclBeliefSet beliefSet, ProbabilisticConditional conditional, double weigth){
		this.log.trace("Starting to compute the weigthed minimal distance inconsistency measure for '" + conditional + "' in '" + beliefSet + "' with weight " + weigth + ".");
		// check archive
		Triple<PclBeliefSet,ProbabilisticConditional,Double> archiveKey = new Triple<PclBeliefSet,ProbabilisticConditional,Double>(beliefSet,conditional,weigth);
		if(this.archive.containsKey(archiveKey))
			return this.archive.get(archiveKey);
		// Create variables for the probability of each possible world and
		// set up the optimization problem for computing the weighted minimal
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
		// For each conditional add variable eta and
		// add constraints implied by the conditionals
		Map<ProbabilisticConditional,Variable> etas = new HashMap<ProbabilisticConditional,Variable>();
		Term targetFunction = null;
		i = 0;		
		for(ProbabilisticConditional c: beliefSet){
			FloatVariable eta = new FloatVariable("e" + i++,-1,1);
			etas.put(c, eta);
			Term term = new AbsoluteValue(eta);
			if(c.equals(conditional))
				term = term.mult(new FloatConstant(weigth));
			if(targetFunction == null)
				targetFunction = term;
			else targetFunction = targetFunction.add(term);
			Term leftSide = null;
			Term rightSide = null;
			if(c.isFact()){
				for(PossibleWorld w: worlds)
					if(w.satisfies(c.getConclusion())){
						if(leftSide == null)
							leftSide = worlds2vars.get(w);
						else leftSide = leftSide.add(worlds2vars.get(w));
					}
				rightSide = new FloatConstant(c.getProbability().getValue()).add(eta);
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
				else rightSide = rightSide.mult(new FloatConstant(c.getProbability().getValue()).add(eta));
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
			solver.contol = 1e-1;
			solver.gtol = 1e-60;
			solver.ftol = 1e-60;
			solver.xtol = 1e-60;
			Map<Variable,Term> solution = solver.solve();
			Double value = solution.get(etas.get(conditional)).doubleValue();
			this.log.debug("Problem solved, the weighted value is '" + value + "'.");
			// update archive
			this.archive.put(archiveKey, value);
			// return value
			return value;
		}catch (GeneralMathException e){
			// This should not happen as the optimization problem is guaranteed to be feasible
			throw new RuntimeException("Fatal error: Optimization problem to compute the weighted minimal distance to a consistent knowledge base is not feasible.");
		}	
	}
	
}
