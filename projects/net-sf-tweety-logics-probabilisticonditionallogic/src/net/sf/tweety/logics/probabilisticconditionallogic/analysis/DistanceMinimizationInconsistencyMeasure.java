package net.sf.tweety.logics.probabilisticconditionallogic.analysis;

import java.io.*;
import java.util.*;

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

import org.apache.commons.logging.*;


/**
 * This class models the distance minimization inconsistency measure as proposed in [Thimm,UAI,2009].
 * 
 * @author Matthias Thimm
 */
public class DistanceMinimizationInconsistencyMeasure implements InconsistencyMeasure {

	/**
	 * Logger.
	 */
	private Log log = LogFactory.getLog(DistanceMinimizationInconsistencyMeasure.class);
	
	/**
	 * For archiving.
	 */
	private Map<PclBeliefSet,Double> archive = new HashMap<PclBeliefSet,Double>();
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.probabilisticconditionallogic.analysis.InconsistencyMeasure#inconsistencyMeasure(net.sf.tweety.logics.probabilisticconditionallogic.PclBeliefSet)
	 */
	@Override
	public Double inconsistencyMeasure(PclBeliefSet beliefSet) {
		this.log.trace("Starting to compute minimal distance inconsistency measure for '" + beliefSet + "'.");
		// check archive
		if(this.archive.containsKey(beliefSet))
			return this.archive.get(beliefSet);
		// first check whether the belief set is consistent		
		this.log.trace("Checking whether '" + beliefSet + "' is inconsistent.");
		if(beliefSet.size() == 0 || new PclDefaultConsistencyTester().isConsistent(beliefSet)){
			// update archive
			this.archive.put(beliefSet, new Double(0));
			return new Double(0);
		}
		this.log.trace("'" + beliefSet + "' is inconsistent, preparing optimization problem for computing the measure.");
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
		// For each conditional add variables eta and tau and
		// add constraints implied by the conditionals
		Map<ProbabilisticConditional,Variable> etas = new HashMap<ProbabilisticConditional,Variable>();
		Map<ProbabilisticConditional,Variable> taus = new HashMap<ProbabilisticConditional,Variable>();
		Term targetFunction = null;
		i = 0;		
		for(ProbabilisticConditional c: beliefSet){
			FloatVariable eta = new FloatVariable("e" + i,0,1);
			FloatVariable tau = new FloatVariable("t" + i++,0,1);
			etas.put(c, eta);
			taus.put(c, tau);
			if(targetFunction == null)
				targetFunction = eta.add(tau);
			else targetFunction = targetFunction.add(eta.add(tau));
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
				}
				if(rightSide == null)
					rightSide = new FloatConstant(0);
				else rightSide = rightSide.mult(new FloatConstant(c.getProbability().getValue()).add(eta).minus(tau));
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
			solver.contol = 1e-8;
			solver.gtol = 1e-15;
			solver.ftol = 1e-15;
			solver.xtol = 1e-15;
			//solver.ignoreNotFeasibleError = true;
			Map<Variable,Term> solution = solver.solve();
			Double result = targetFunction.replaceAllTerms(solution).doubleValue();
			this.log.debug("Problem solved, the measure is '" + result + "'.");
			String values = "Eta/Tau-values for the solution:\n===BEGIN===\n";
			for(ProbabilisticConditional pc: beliefSet)
				values += pc + "\teta: " + solution.get(etas.get(pc)) + "\ttau: " + solution.get(taus.get(pc)) +  "\n";
			values += "===END===";
			this.log.debug(values);
			// update archive
			this.archive.put(beliefSet, result);
			// return value of target function
			return result;
		}catch (GeneralMathException e){
			// This should not happen as the optimization problem is guaranteed to be feasible
			throw new RuntimeException("Fatal error: Optimization problem to compute the minimal distance to a consistent knowledge base is not feasible.");
		}		
	}
	
	public static void main(String[] args){
		TweetyLogging.logLevel = TweetyConfiguration.LogLevel.ERROR;
		TweetyLogging.initLogging();
		BeliefBaseMachineShop ms = new BalancedMachineShop(new MeanDistanceCulpabilityMeasure(false));		
		for(int i = 0; i < 10; i++){
			String file = "/Users/mthimm/Desktop/R" + i + ".pcl";			
			try {
				PclBeliefSet beliefSet = (PclBeliefSet) new net.sf.tweety.logics.probabilisticconditionallogic.parser.PclParser().parseBeliefBaseFromFile(file);
				System.out.println(file);				
				PclBeliefSet repaired = (PclBeliefSet)ms.repair(beliefSet);
				System.out.println("======================================");
				for(Formula f: repaired)
					System.out.println(f);
				System.out.println();
				//System.out.println("Drastic inconsistency measure:                 " + new DrasticInconsistencyMeasure().inconsistencyMeasure(beliefSet));
				//System.out.println("MI inconsistency measure:                      " + new MiInconsistencyMeasure().inconsistencyMeasure(beliefSet));
				//System.out.println("Normalized MI inconsistency measure:           " + new NormalizedMiInconsistencyMeasure().inconsistencyMeasure(beliefSet));
				//System.out.println("MI^C inconsistency measure:                    " + new MicInconsistencyMeasure().inconsistencyMeasure(beliefSet));
				//System.out.println("Normalized MI^C inconsistency measure:         " + new NormalizedMicInconsistencyMeasure().inconsistencyMeasure(beliefSet));
				//System.out.println("MinDev inconsistency measure:                  " + new DistanceMinimizationInconsistencyMeasure().inconsistencyMeasure(beliefSet));
				//System.out.println("Normalized MinDev inconsistency measure:       " + new NormalizedDistanceMinimizationInconsistencyMeasure().inconsistencyMeasure(beliefSet));
				//System.out.println("Lower MinDev inconsistency measure:            " + new LowerApproxDistanceMinimizationInconsistencyMeasure().inconsistencyMeasure(beliefSet));
				//System.out.println("Normalized lower MinDev inconsistency measure: " + new NormalizedLowerApproxDistanceMinimizationInconsistencyMeasure().inconsistencyMeasure(beliefSet));
				//System.out.println("Upper MinDev inconsistency measure:            " + new UpperApproxDistanceMinimizationInconsistencyMeasure().inconsistencyMeasure(beliefSet));
				//System.out.println("Normalized upper MinDev inconsistency measure: " + new NormalizedUpperApproxDistanceMinimizationInconsistencyMeasure().inconsistencyMeasure(beliefSet));			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
