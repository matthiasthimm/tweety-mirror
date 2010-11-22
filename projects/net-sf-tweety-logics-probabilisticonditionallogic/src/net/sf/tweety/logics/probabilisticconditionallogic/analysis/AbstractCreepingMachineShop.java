package net.sf.tweety.logics.probabilisticconditionallogic.analysis;

import java.util.*;

import org.apache.commons.logging.*;

import net.sf.tweety.*;
import net.sf.tweety.logics.probabilisticconditionallogic.*;
import net.sf.tweety.logics.probabilisticconditionallogic.syntax.*;
import net.sf.tweety.util.*;

/**
 * The common ancestor vor creeping machine shops, see [Diss, Thimm] for details.
 * @author Matthias Thimm
 *
 */
public abstract class AbstractCreepingMachineShop implements BeliefBaseMachineShop {

	/**
	 * Logger.
	 */
	private Log log = LogFactory.getLog(AbstractCreepingMachineShop.class);
	
	/**
	 * The precision for finding the minimal consistent knowledge base.
	 */
	public static final double PRECISION = 0.000000001;
	
	/**
	 * The maximum number of steps in the line search.
	 */
	public static final int MAX_ITERATIONS = 10000000;
	
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
		this.log.trace("'" + beliefSet + "' is inconsistent, preparing optimization problem to restore consistency.");
		this.init(beliefSet);
		double lowerBound = this.getLowerBound();
		double upperBound = this.getUpperBound();
		PclBeliefSet lastConsistentBeliefSet = beliefSet;
		PclBeliefSet newBeliefSet;
		int cnt = 0;
		while(upperBound - lowerBound > AbstractCreepingMachineShop.PRECISION){
			double delta = (upperBound + lowerBound)/2;
			this.log.debug("Current delta: " + delta);
			Map<ProbabilisticConditional,Probability> values = this.getValues(delta,beliefSet);
			newBeliefSet = this.characteristicFunction(beliefSet, values);
			if(tester.isConsistent(newBeliefSet)){
				lastConsistentBeliefSet = newBeliefSet;
				upperBound = delta;
			}else{
				lowerBound = delta;
			}
			cnt++;
			if(cnt >= AbstractCreepingMachineShop.MAX_ITERATIONS)
				throw new RuntimeException("Consistent knowledge base cannot be found for '" + beliefBase + "'.");
		}
		this.log.debug("Repair complete, final knowledge base: " + lastConsistentBeliefSet);
		return lastConsistentBeliefSet;
	}
	
	/**
	 * Performs some optional initializations before beginning
	 * to restore consistency. 
	 * @param beliefSet a PCL belief set.
	 */
	protected void init(PclBeliefSet beliefSet){ }
	
	/**
	 * Returns a modified belief base that replaces each conditionals probability
	 * by the one given by "values".
	 * @param beliefSet a belief set
	 * @param values a map from conditionals to probabilities.
	 * @return a modified belief set.
	 */
	protected PclBeliefSet characteristicFunction(PclBeliefSet beliefSet, Map<ProbabilisticConditional,Probability> values){
		PclBeliefSet result = new PclBeliefSet();
		for(ProbabilisticConditional pc: beliefSet)
			result.add(new ProbabilisticConditional(pc,values.get(pc)));
		return result;
	}
	
	/**
	 * Computes the values of the conditionals for step delta
	 * @param delta the step parameter.
	 * @param beliefSet the belief set.
	 * @return a map mapping conditionals to probabilities.
	 */
	protected abstract Map<ProbabilisticConditional,Probability> getValues(double delta, PclBeliefSet beliefSet);
	
	/**
	 * Retrieves the lower bound for delta for this machine shop.
	 * @return the lower bound for delta for this machine shop.
	 */
	protected abstract double getLowerBound();
	
	/**
	 * Retrieves the upper bound for delta for this machine shop.
	 * @return the upper bound for delta for this machine shop.
	 */
	protected abstract double getUpperBound();

}
