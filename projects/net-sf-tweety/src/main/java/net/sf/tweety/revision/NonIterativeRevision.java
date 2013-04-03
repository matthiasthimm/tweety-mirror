package net.sf.tweety.revision;

import java.util.LinkedList;
import java.util.List;

/**
 * Implements the revision method with two belief bases by delegating the processing
 * to the revision method bases on an ordered list of belief bases. It acts as base
 * class for revision approaches which support the revision of multiple belief bases
 * in one step.
 * 
 * @author Tim Janus
 *
 * @param <TBeliefBase> The type of the belief base
 */
public abstract class NonIterativeRevision<TBeliefBase> implements Revision<TBeliefBase>{

	@Override
	public TBeliefBase revision(TBeliefBase beliefBase1, TBeliefBase beliefBase2) {
		List<TBeliefBase> param = new LinkedList<TBeliefBase>();
		param.add(beliefBase1);
		param.add(beliefBase2);
		return revision(param);
	}

	@Override
	public abstract TBeliefBase revision(List<TBeliefBase> ordererList);

}
