package net.sf.tweety.beliefdynamics;

import java.util.Collection;
import java.util.List;

import net.sf.tweety.Formula;

/**
 * Abstract base class for a revision process on belief bases of type TBeliefBase, it
 * provides a method to revise one belief base with another and a method 
 * to revise a ordered list of belief bases. The ordering of the list defines
 * the credibility of the different belief bases.
 * 
 * @author Tim Janus
 *
 * @tparam <T>	The type of formulas of the belief bases
 * @tparam <TBeliefBase> The type of the belief bases used for revision
 */
public abstract class CredibilityRevision<T extends Formula> 
	extends MultipleBaseRevisionOperator<T> {
	/** 
	 * Revises the given belief base with the given formula, the credibility of the
	 * formula is higher.
	 * @param base		Collection of formulas forming the basis belief base.
	 * @param formula	The formula representing the knowledge used for revision.
	 * @return			A new belief base containing the revised knowledge.
	 */
	@Override
	public Collection<T> revise(Collection<T> base, T formula) {return super.revise(base, formula);}
	
	/**
	 * Revises the two given belief bases and returns the result, the former belief base has
	 * a lesser credibility than the latter.
	 * @param beliefBase1	The lower priority belief base
	 * @param beliefBase2	The higher priority belief base
	 * @return	The belief base which is the result of the revision.
	 */
	@Override
	public abstract Collection<T> revise(Collection<T> beliefBase1, Collection<T> beliefBase2);
	
	
	/**
	 * Revises the belief bases in the orderer list into one belief base.
	 * @param orderedBeliefBases	An orderer list of belief bases which assumes
	 * 								that belief bases with a lower index have a
	 * 								lower priority.
	 * @return	The belief base which is the result of the revision.
	 */
	public abstract Collection<T> revise(List<Collection<T>> orderedBeliefBases);
}
