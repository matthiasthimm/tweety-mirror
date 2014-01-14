package net.sf.tweety.logics.pcl.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lpsolve.LpSolve;

import org.ojalgo.matrix.BigMatrix;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PhysicalStore;
import org.ojalgo.matrix.store.PrimitiveDenseStore;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.quadratic.QuadraticSolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.tweety.BeliefBase;
import net.sf.tweety.BeliefBaseMachineShop;
import net.sf.tweety.logics.pcl.PclBeliefSet;
import net.sf.tweety.logics.pcl.semantics.ProbabilityDistribution;
import net.sf.tweety.logics.pcl.syntax.ProbabilisticConditional;
import net.sf.tweety.logics.pl.semantics.PossibleWorld;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;
import net.sf.tweety.math.GeneralMathException;
import net.sf.tweety.math.equation.Equation;
import net.sf.tweety.math.norm.RealVectorNorm;
import net.sf.tweety.math.opt.OptimizationProblem;
import net.sf.tweety.math.opt.solver.OpenOptSolver;
import net.sf.tweety.math.opt.solver.OpenOptWebSolver;
import net.sf.tweety.math.probability.Probability;
import net.sf.tweety.math.term.FloatConstant;
import net.sf.tweety.math.term.FloatVariable;
import net.sf.tweety.math.term.IntegerConstant;
import net.sf.tweety.math.term.Term;
import net.sf.tweety.math.term.Variable;

/**
 * Repairs a probabilistic belief base by taking the probabilities from the probability function
 * that minimizes the "minimal violation inconsistency measure" with respect to the euclidean norm.
 * 
 * @author Nico Potyka
 */
public class MinimalViolationEuclideanMachineShop implements BeliefBaseMachineShop {

	/**
	 * Logger.
	 */
	static protected Logger log = LoggerFactory.getLogger(MinimalViolationEuclideanMachineShop.class);


	
	/* (non-Javadoc)
	 * @see net.sf.tweety.BeliefBaseMachineShop#repair(net.sf.tweety.BeliefBase)
	 */
	@Override
	public BeliefBase repair(BeliefBase beliefBase) {

		log.info("Start repair.");
		
		if(!(beliefBase instanceof PclBeliefSet)) {
			log.debug("Belief base is not an instance of PCLBeliefSet.");
			throw new IllegalArgumentException("Belief base of type 'PclBeliefSet' expected.");
		}
		PclBeliefSet beliefSet = (PclBeliefSet) beliefBase;

		
		Set<PossibleWorld> worlds = PossibleWorld.getAllPossibleWorlds((PropositionalSignature) beliefSet.getSignature());
		int noWorlds = worlds.size();
		int noConditionals = beliefSet.size();
		
		log.debug("Create constraint matrix.");
		int i = 0;
		double tmpA[][] = new double[noConditionals][noWorlds];
		for(ProbabilisticConditional c: beliefSet) {
			
			int j = 0; 
			double p = c.getProbability().doubleValue();
			PropositionalFormula conclusion = c.getConclusion();
			
			if(c.isFact()) {
				
				log.debug(c.toString()+" is fact.");
				
				for(PossibleWorld w: worlds) {
					if(w.satisfies(conclusion)) {
						tmpA[i][j] = 1-p;
					}
					else {
						tmpA[i][j] = -p;
					}
					
					j++;
				}
				
			}
			else {
				
				log.debug(c.toString()+" is conditional.");
				
				PropositionalFormula premise = c.getPremise().iterator().next();
				
				for(PossibleWorld w: worlds) {
					
					if(w.satisfies(premise)) {
						
						if(w.satisfies(conclusion)) {
							tmpA[i][j] = 1-p;
						}
						else {
							tmpA[i][j] = -p;
						}
						
					}
					else {
						
						tmpA[i][j] = 0;
					}

					j++;
				}
			}
			
			i++;
		}
		
		//Objective is 1/2 x' Q x + C' x
		//Q = 2 A' A for constraint matrix A, C' = 0
		//tmpQ = 1/2 Q = A' A to avoid scalar multiplication. If interested in correct function value, multiply optimization result by 2. 
		log.debug("Create Q and C for objective.");
		PrimitiveDenseStore tmpM = PrimitiveDenseStore.FACTORY.rows(tmpA);
		MatrixStore<Double> tmpQ = tmpM.transpose().multiplyRight(tmpM);
		
		PrimitiveDenseStore tmpC = PrimitiveDenseStore.FACTORY.makeZero(noWorlds, 1);


		//Equations AE x = BE
		log.debug("Create normalization constraint.");
		tmpA = new double[1][noWorlds];
		for(i=0; i<noWorlds; i++) {
			tmpA[0][i] = 1;
		}
		PrimitiveDenseStore tmpAE = PrimitiveDenseStore.FACTORY.rows(tmpA);
		PrimitiveDenseStore tmpBE = PrimitiveDenseStore.FACTORY.rows(new double[][]{{1}});

		
		//Inequalities AI x <= BI
		log.debug("Create non-negativity constraints.");
		tmpA = new double[noWorlds][noWorlds];
		for(i=0; i<noWorlds; i++) {
			tmpA[i][i] = -1;
		}
		PrimitiveDenseStore tmpAI = PrimitiveDenseStore.FACTORY.rows(tmpA);
		PrimitiveDenseStore tmpBI = PrimitiveDenseStore.FACTORY.makeZero(noWorlds, 1);
		
		

		log.debug("Create solver.");
		//by construction, the computed solution value has to be multiplied by 2 to obtain the correct solution
		QuadraticSolver qSolver = new QuadraticSolver.Builder(tmpQ, tmpC).equalities(tmpAE, tmpBE).inequalities(tmpAI, tmpBI).build();
		qSolver.options.validate = true;
		
		long time = System.currentTimeMillis();
		Optimisation.Result tmpResult = qSolver.solve();
		log.info("Finished computation after "+(System.currentTimeMillis()-time)+" ms. State: "+tmpResult.getState());
		
		log.debug("Create solver.");
		PhysicalStore<Double> result = BigMatrix.FACTORY.columns(tmpResult).toPrimitiveStore();
		ProbabilityDistribution<PossibleWorld> p = new ProbabilityDistribution<PossibleWorld>(beliefSet.getSignature());
		i=0;
		for(PossibleWorld world: worlds) {
			p.put(world, new Probability(result.doubleValue(i++)));
		}
		
		PclBeliefSet repairedSet = new PclBeliefSet();
		for(ProbabilisticConditional pc: beliefSet)
			repairedSet.add(new ProbabilisticConditional(pc,p.probability(pc)));
		
		
		return repairedSet;
	
	}

}
