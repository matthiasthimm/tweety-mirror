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
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.probabilisticconditionallogic.analysis.InconsistencyMeasure#inconsistencyMeasure(net.sf.tweety.logics.probabilisticconditionallogic.PclBeliefSet)
	 */
	@Override
	public Double inconsistencyMeasure(PclBeliefSet beliefSet) {
		this.log.trace("Starting to compute minimal distance inconsistency measure for '" + beliefSet + "'.");
		// first check whether the belief set is consistent		
		this.log.trace("Checking whether '" + beliefSet + "' is inconsistent.");
		if(new PclDefaultConsistencyTester().isConsistent(beliefSet))
			return new Double(0);
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
				}
				if(rightSide == null)
					rightSide = new FloatConstant(0);
				else rightSide = rightSide.mult(new FloatConstant(c.getProbability().getValue()).add(eta).minus(tau));
			}
			problem.add(new Equation(leftSide,rightSide));
		}			
		problem.setTargetFunction(targetFunction);		
		// Get a feasible starting point for the optimization
		List<Term> functions = new ArrayList<Term>();
		Map<Variable,Term> startingPoint = new HashMap<Variable,Term>();
		for(Variable v: problem.getVariables())
			startingPoint.put(v, new FloatConstant(0));
		// all statement are equations
		for(Statement s: problem)
			functions.add(s.getLeftTerm().minus(s.getRightTerm()));
		RootFinder rootFinder = new OpenOptRootFinder(functions,startingPoint);
		// Solve the problem using the OpenOpt library
		this.log.trace("Problem prepared, now finding feasible starting point.");
		try{
			Map<Variable,Term> startingPointForOptimization =rootFinder.randomRoot();
			this.log.trace("Starting point found, now solving the main problem.");
			Solver solver = new OpenOptSolver(problem,startingPointForOptimization);
			Map<Variable,Term> solution = solver.solve();
			Double result = targetFunction.replaceAllTerms(solution).doubleValue();
			this.log.trace("Problem solved, the measure is '" + result + "'.");
			//return value of target function
			return result;
		}catch (GeneralMathException e){
			// This should not happen as the optimization problem is guaranteed to be feasible
			throw new RuntimeException("Fatal error: Optimization problem to compute the minimal distance to a consistent knowledge base is not feasible.");
		}		
	}

	
	public static void main(String[] args){
		//TweetyCli.initLogging();
		String file = "/Users/mthimm/Desktop/pcl_test";
		try {
			PclBeliefSet beliefSet = (PclBeliefSet) new net.sf.tweety.logics.probabilisticconditionallogic.parser.PclParser().parseBeliefBaseFromFile(file);
			System.out.println(new DistanceMinimizationInconsistencyMeasure().inconsistencyMeasure(beliefSet));
			
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
