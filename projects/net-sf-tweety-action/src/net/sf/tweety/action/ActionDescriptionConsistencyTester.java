package net.sf.tweety.action;

import java.util.Set;

import net.sf.tweety.BeliefBase;
import net.sf.tweety.ConsistencyTester;

/**
 * Classes implementing this interface are capable of checking whether a given
 * action description is consistent according to some consistency measurements.
 * 
 * @author Sebastian Homann
 */
public interface ActionDescriptionConsistencyTester
  extends ConsistencyTester
{
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.ConsistencyTester#isConsistent(net.sf.tweety.BeliefBase)
   */
  public boolean isConsistent( BeliefBase actionDescription );
  
  /**
   * Checks whether the given set of causal rules is consistent.
   * 
   * @param causalRules a set of causal rules.
   * @return true iff the given set of causal rules is consistent.
   */
  public boolean isConsistent( Set< CausalLaw > causalRules );
}
