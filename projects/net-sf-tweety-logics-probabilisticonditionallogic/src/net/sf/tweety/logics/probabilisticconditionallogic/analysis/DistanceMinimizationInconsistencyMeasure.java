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
		// For each conditional add variable eta and
		// add constraints implied by the conditionals
		Map<ProbabilisticConditional,Variable> etas = new HashMap<ProbabilisticConditional,Variable>();
		Term targetFunction = null;
		i = 0;		
		for(ProbabilisticConditional c: beliefSet){
			FloatVariable eta = new FloatVariable("e" + i++,-1,1);
			etas.put(c, eta);
			if(targetFunction == null)
				targetFunction = new AbsoluteValue(eta);
			else targetFunction = targetFunction.add(new AbsoluteValue(eta));
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
			solver.contol = 1e-2;
			solver.gtol = 1e-60;
			solver.ftol = 1e-60;
			solver.xtol = 1e-60;
			Map<Variable,Term> solution = solver.solve();
			Double result = targetFunction.replaceAllTerms(solution).doubleValue();
			this.log.debug("Problem solved, the measure is '" + result + "'.");
			String etaValues = "Eta-values for the solution:\n===BEGIN===\n";
			for(ProbabilisticConditional pc: beliefSet)
				etaValues += pc + "\t" + solution.get(etas.get(pc)) + "\n";
			etaValues += "===END===";
			this.log.debug(etaValues);
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
		TweetyLogging.logLevel = TweetyConfiguration.LogLevel.DEBUG;
		TweetyLogging.initLogging();		
		String file = "/Users/mthimm/Desktop/pcl_test";
		try {
			long millis = System.currentTimeMillis();
			PclBeliefSet beliefSet = (PclBeliefSet) new net.sf.tweety.logics.probabilisticconditionallogic.parser.PclParser().parseBeliefBaseFromFile(file);
			//System.out.println(new DistanceMinimizationInconsistencyMeasure().inconsistencyMeasure(beliefSet));
			//AverageDistanceCulpabilityMeasure adMeasure = new AverageDistanceCulpabilityMeasure();
			//ShapleyCulpabilityMeasure shapley = new ShapleyCulpabilityMeasure(new DistanceMinimizationInconsistencyMeasure());
			for(ProbabilisticConditional pc: beliefSet)
				System.out.println(pc + " - " +  pc.getUniformProbability());
			System.out.println(new UnbiasedCreepingMachineShop().repair(beliefSet));
			/*for(ProbabilisticConditional pc: beliefSet)
				System.out.println(pc + " - " +  shapley.culpabilityMeasure(beliefSet, pc));
			for(Set<Formula> f: new PclDefaultConsistencyTester().minimalInconsistentSubsets(beliefSet))
				System.out.println("MinIncon " + f);*/
			System.out.println("Time: " + (System.currentTimeMillis()-millis) + "ms");
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
