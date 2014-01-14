package net.sf.tweety.logics.pcl.analysis;

import java.util.Set;

import net.sf.tweety.BeliefBase;
import net.sf.tweety.BeliefBaseMachineShop;
import net.sf.tweety.logics.pcl.PclBeliefSet;
import net.sf.tweety.logics.pcl.semantics.ProbabilityDistribution;
import net.sf.tweety.logics.pcl.syntax.ProbabilisticConditional;
import net.sf.tweety.logics.pl.semantics.PossibleWorld;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;
import net.sf.tweety.math.probability.Probability;

import org.ojalgo.constant.BigMath;
import org.ojalgo.matrix.BigMatrix;
import org.ojalgo.matrix.store.MatrixStore;
import org.ojalgo.matrix.store.PhysicalStore;
import org.ojalgo.matrix.store.PrimitiveDenseStore;
import org.ojalgo.optimisation.Expression;
import org.ojalgo.optimisation.ExpressionsBasedModel;
import org.ojalgo.optimisation.Optimisation;
import org.ojalgo.optimisation.Variable;
import org.ojalgo.optimisation.quadratic.QuadraticSolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		
//		return repairOjAlgoMatrixModel(beliefBase);
		return repairOjAlgoExpressionModel(beliefBase);
	
	}
	
	
	
	/**
	 * Compute solution using ojalgos matrix representation.
	 * @param beliefBase
	 * @return
	 */
	private BeliefBase repairOjAlgoMatrixModel(BeliefBase beliefBase) {

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
	
	

	/**
	 * Compute solution using ojalgos expression based model (more stable).
	 * @param beliefBase
	 * @return
	 */
	public BeliefBase repairOjAlgoExpressionModel(BeliefBase beliefBase) {

		log.info("Start repair.");
		
		if(!(beliefBase instanceof PclBeliefSet)) {
			log.debug("Belief base is not an instance of PCLBeliefSet.");
			throw new IllegalArgumentException("Belief base of type 'PclBeliefSet' expected.");
		}
		PclBeliefSet beliefSet = (PclBeliefSet) beliefBase;

		
		Set<PossibleWorld> worlds = PossibleWorld.getAllPossibleWorlds((PropositionalSignature) beliefSet.getSignature());
		int noWorlds = worlds.size();
		int noConditionals = beliefSet.size();
		
		

		log.debug("Create variables and lower bounds.");
		
		Variable[] tmpVariables = new Variable[noWorlds];		
		for(int i=0; i<noWorlds; i++) {
			tmpVariables[i] = new Variable(""+i).lower(BigMath.ZERO);
		}
		

		log.debug("Create expression based model.");
		
		ExpressionsBasedModel tmpModel = new ExpressionsBasedModel(tmpVariables);
		
		
		log.debug("Create constraint matrix.");
		double tmpA[][] = new double[noConditionals][noWorlds];
		int i = 0;
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

		
		
		log.debug("Create objective.");
		
		//Objective is 1/2 x' Q x + C' x
		//Q = 2 A' A for constraint matrix A, C' = 0
		//tmpQ = 1/2 Q = A' A to avoid scalar multiplication. If interested in correct function value, multiply optimization result by 2. 
		PrimitiveDenseStore tmpM = PrimitiveDenseStore.FACTORY.rows(tmpA);
		MatrixStore<Double> tmpQ = tmpM.transpose().multiplyRight(tmpM);
		
		Expression tmpExpr = tmpModel.addExpression("Objective");
		tmpModel.setMinimisation(true);
		for(i=0; i<tmpQ.countRows(); i++) {
			for(int j=0; j<tmpQ.countColumns(); j++) {
				double q = tmpQ.get(i, j);
				if(q != 0) { //entries of Q can become arbitrary close to 0, therefore we have to check for equality
					tmpExpr.setQuadraticFactor(i, j, q);
				}
			}
		}
		tmpExpr.weight(BigMath.ONE);
		
		
		log.debug("Create normalization constraint.");
		tmpExpr = tmpModel.addExpression("Normalization").level(BigMath.ONE);
		for(i=0; i<noWorlds; i++) {
			tmpExpr.setLinearFactor(i, BigMath.ONE);
		}
		

		log.debug("Solve.");
		//by construction, the computed solution value has to be multiplied by 2 to obtain the correct solution
		long time = System.currentTimeMillis();
		double tmpObjFuncVal = 2 * tmpModel.minimise().getValue();
		log.info("Finished computation after "+(System.currentTimeMillis()-time)+" ms.");
		

		log.debug("Repair knowledge base.");
		
		ProbabilityDistribution<PossibleWorld> p = new ProbabilityDistribution<PossibleWorld>(beliefSet.getSignature());
		i=0;
		for(PossibleWorld world: worlds) {
			p.put(world, new Probability(tmpVariables[i++].getValue().doubleValue()));
		}
		
		PclBeliefSet repairedSet = new PclBeliefSet();
		for(ProbabilisticConditional pc: beliefSet)
			repairedSet.add(new ProbabilisticConditional(pc,p.probability(pc)));
		
		
		return repairedSet;
	
	}

}
