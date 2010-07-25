package net.sf.tweety.logics.probabilisticconditionallogic.analysis;

import java.util.*;

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
	 * The precision for finding the minimal consistent knowledge base.
	 */
	public static final double PRECISION = 0.000001;
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.BeliefBaseMachineShop#repair(net.sf.tweety.BeliefBase)
	 */
	@Override
	public abstract BeliefBase repair(BeliefBase beliefBase);
	
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

}
