package net.sf.tweety.revision;

import java.util.List;

/**
 * Implements the list based method of the Revision interface by iterative calling
 * the revision method which revise two belief bases. Acts as a base class for iterative
 * revision processes.
 * 
 * @author Tim Janus
 *
 * @param <TBeliefBase>	The type of the belief bases
 */
public abstract class IterativeRevision<TBeliefBase> implements Revision<TBeliefBase> {

	@Override
	public abstract TBeliefBase revision(TBeliefBase beliefBase1, TBeliefBase beliefBase2);

	@Override
	public TBeliefBase revision(List<TBeliefBase> ordererList) {
		if(ordererList == null || ordererList.size() == 0)
			throw new IllegalArgumentException("The parameter 'orderList' must not be empty.");
		
		TBeliefBase p1 = ordererList.get(0);
		for(int i=1; i<ordererList.size(); ++i) {
			TBeliefBase p2 = ordererList.get(i);
			p1 = revision(p1,p2);
		}
		return p1;
	}
	
}
