package net.sf.tweety.logics.probabilisticconditionallogic.analysis;

import java.util.*;

import org.apache.commons.logging.*;

import net.sf.tweety.*;
import net.sf.tweety.logics.probabilisticconditionallogic.*;
import net.sf.tweety.logics.probabilisticconditionallogic.syntax.*;
import net.sf.tweety.util.*;

/**
 * This class is capable of restoring consistency of a possible inconsistent probabilistic
 * conditional belief set. Restoring consistency is performed by an unbiased creeping of
 * the original belief set towards an uniform belief set, see [Diss, Thimm] for details.
 * @author Matthias Thimm
 */
public class UnbiasedCreepingMachineShop extends AbstractCreepingMachineShop {

	/**
	 * Logger.
	 */
	private Log log = LogFactory.getLog(UnbiasedCreepingMachineShop.class);
	
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
		double lowerBound = 0;
		double upperBound = 1;
		PclBeliefSet lastConsistentBeliefSet = beliefSet;
		PclBeliefSet newBeliefSet = beliefSet;
		while(upperBound - lowerBound > AbstractCreepingMachineShop.PRECISION){
			double delta = (upperBound + lowerBound)/2;
			this.log.debug("Current delta: " + delta);
			Map<ProbabilisticConditional,Probability> values = new HashMap<ProbabilisticConditional,Probability>();
			for(ProbabilisticConditional pc: beliefSet)
				values.put(pc, new Probability((1-delta) * pc.getProbability().getValue() + delta * pc.getUniformProbability().getValue()));
			newBeliefSet = this.characteristicFunction(beliefSet, values);
			if(tester.isConsistent(newBeliefSet)){
				lastConsistentBeliefSet = newBeliefSet;
				upperBound = delta;
			}else{
				lowerBound = delta;
			}			
		}
		this.log.debug("Repair complete, final knowledge base: " + lastConsistentBeliefSet);
		return lastConsistentBeliefSet;
	}

}
