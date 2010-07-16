package net.sf.tweety;

import java.util.*;

import net.sf.tweety.util.*;

/**
 * Classes extending this abstract class are capable of testing
 * whether a given belief set is consistent and providing
 * the minimal inconsistent subsets. 
 * 
 * @author Matthias Thimm
 */
public abstract class BeliefSetConsistencyTester implements ConsistencyTester {

	/**
	 * This method returns the minimal inconsistent subsets of the given
	 * belief set.<br>
	 * TODO: make this method efficient
	 * @param beliefSet a belief set
	 * @return the minimal inconsistent subsets of the given
	 *  belief set.
	 */
	public Set<Set<Formula>> minimalInconsistentSubsets(BeliefSet<? extends Formula> beliefSet){
		Set<Set<Formula>> result = new HashSet<Set<Formula>>();
		if(this.isConsistent(beliefSet))
			return result;
		Stack<Set<Formula>> subsets = new Stack<Set<Formula>>();
		subsets.addAll(new SetTools<Formula>().subsets(beliefSet));
		while(!subsets.isEmpty()){
			Set<Formula> subset = subsets.pop();
			if(!this.isConsistent(subset)){
				// remove all super sets of subset from result
				Set<Set<Formula>> toBeRemoved = new HashSet<Set<Formula>>();
				for(Set<Formula> set: result)
					if(set.containsAll(subset))
						toBeRemoved.add(set);
				result.removeAll(toBeRemoved);
				// remove all super sets of subset from the stack
				toBeRemoved = new HashSet<Set<Formula>>();
				for(Set<Formula> set: subsets)
					if(set.containsAll(subset))
						toBeRemoved.add(set);
				subsets.removeAll(toBeRemoved);			
				result.add(subset);
			}
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.ConsistencyTester#isConsistent(net.sf.tweety.BeliefBase)
	 */
	@Override
	public abstract boolean isConsistent(BeliefBase beliefBase);
	
	/**
	 * Checks whether the given set of beliefs (formulas) is consistent
	 * @param beliefs a set of formulas.
	 * @return "true" iff the given set is consistent.
	 */
	public abstract boolean isConsistent(Set<? extends Formula> beliefs);

}
