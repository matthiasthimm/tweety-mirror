package net.sf.tweety.revision;

import java.util.List;

/**
 * Interface for a revision process on belief bases of type TBeliefBase, it
 * provides a method to revise one belief base with another and a method 
 * to revise a ordered list of belief bases.
 * 
 * @author Tim Janus
 *
 * @param <TBeliefBase>	The type of the belief bases
 */
public interface Revision<TBeliefBase> {
	/**
	 * Revises the two given belief bases and returns the result.
	 * @param beliefBase1	The lower priority belief base
	 * @param beliefBase2	The higher priority belief base
	 * @return	The belief base which is the result of the revision.
	 */
	TBeliefBase revision(TBeliefBase beliefBase1, TBeliefBase beliefBase2);
	
	
	/**
	 * Revises the belief bases in the orderer list into one belief base.
	 * @param orderedBeliefBases	An orderer list of belief bases which assumes
	 * 								that belief bases with a lower index have a
	 * 								lower priority.
	 * @return	The belief base which is the result of the revision.
	 */
	TBeliefBase revision(List<TBeliefBase> orderedBeliefBases);
}
