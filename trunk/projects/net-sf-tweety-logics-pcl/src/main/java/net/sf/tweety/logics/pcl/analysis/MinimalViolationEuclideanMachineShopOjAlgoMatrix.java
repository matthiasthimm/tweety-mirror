package net.sf.tweety.logics.pcl.analysis;

import java.util.Set;

import net.sf.tweety.BeliefBase;
import net.sf.tweety.logics.pcl.PclBeliefSet;
import net.sf.tweety.logics.pcl.semantics.ProbabilityDistribution;
import net.sf.tweety.logics.pcl.syntax.ProbabilisticConditional;
import net.sf.tweety.logics.pcl.util.OjAlgoPclUtils;
import net.sf.tweety.logics.pl.semantics.PossibleWorld;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;
import net.sf.tweety.math.probability.Probability;
import net.sf.tweety.math.util.OjAlgoMathUtils;

import org.ojalgo.matrix.BigMatrix;
import org.ojalgo.matrix.PrimitiveMatrix;
import org.ojalgo.matrix.store.PhysicalStore;
import org.ojalgo.matrix.store.PrimitiveDenseStore;
import org.ojalgo.matrix.store.SingleStore;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.quadratic.QuadraticSolver;

/**
 * Repairs a probabilistic belief base by taking the probabilities from the probability function
 * that minimizes the "minimal violation inconsistency measure" with respect to the euclidean norm.
 * 
 * Implementation uses ojAlgos matrix representation (currently bad numerical performance).
 * 
 * @author Nico Potyka
 */
public class MinimalViolationEuclideanMachineShopOjAlgoMatrix extends MinimalViolationEuclideanMachineShop {


	
	/**
	 * Compute solution using ojalgos matrix representation.
	 * @param beliefBase
	 * @return
	 */
	protected BeliefBase repair(PclBeliefSet beliefSet) {

		
		Set<PossibleWorld> worlds = PossibleWorld.getAllPossibleWorlds((PropositionalSignature) beliefSet.getSignature());
		int noWorlds = worlds.size();
		
		
		
		log.debug("Create constraint matrix and objective.");
		
		PrimitiveMatrix tmpM = OjAlgoPclUtils.createConstraintMatrix(beliefSet, worlds);
		
		//Objective is sqrt( 1/2 x' Q x + C' x)
		//Q = 2 A' A for constraint matrix A, C' = 0
		//tmpQ = 1/2 Q = A' A to avoid scalar multiplication. If interested in correct function value, multiply optimization result by 2 and compute square root. 
		PrimitiveMatrix tmpQ = tmpM.transpose().multiplyRight(tmpM);
		
		PrimitiveDenseStore tmpC = PrimitiveDenseStore.FACTORY.makeZero(noWorlds, 1);

		

		log.debug("Create normalization constraint.");
		//Equations AE x = BE
		
		PrimitiveMatrix tmpAE = OjAlgoMathUtils.getOnes(1, noWorlds);
		SingleStore<Double> tmpBE = SingleStore.makePrimitive(1.0);

		
		
		log.debug("Create non-negativity constraints.");
		//Inequalities AI x <= BI

		PrimitiveMatrix tmpAI = OjAlgoMathUtils.getUnityMultiple(noWorlds, -1);
		PrimitiveDenseStore tmpBI = PrimitiveDenseStore.FACTORY.makeZero(noWorlds, 1);
		
		

		log.debug("Create solver.");

		//by construction, the correct solution is the square root of the multiple of 2 of the computed solution
		QuadraticSolver qSolver = new QuadraticSolver.Builder(tmpQ.toPrimitiveStore(), tmpC)
										.equalities(tmpAE.toPrimitiveStore(), tmpBE)
										.inequalities(tmpAI.toPrimitiveStore(), tmpBI)
										.build();
		qSolver.options.validate = true;
		
		long time = System.currentTimeMillis();
		Optimisation.Result tmpResult = qSolver.solve();
		log.info("Finished computation after "+(System.currentTimeMillis()-time)+" ms. State: "+tmpResult.getState());
		
		
		
		log.debug("Repair knowledge base.");
		
		PhysicalStore<Double> result = BigMatrix.FACTORY.columns(tmpResult).toPrimitiveStore();
		ProbabilityDistribution<PossibleWorld> p = new ProbabilityDistribution<PossibleWorld>(beliefSet.getSignature());
		
		int k=0;
		for(PossibleWorld world: worlds) {
			p.put(world, new Probability(result.doubleValue(k++)));
		}
		
		PclBeliefSet repairedSet = new PclBeliefSet();
		for(ProbabilisticConditional pc: beliefSet) {
			repairedSet.add(new ProbabilisticConditional(pc,p.probability(pc)));
		}
		
		return repairedSet;
	
	}



	

}
