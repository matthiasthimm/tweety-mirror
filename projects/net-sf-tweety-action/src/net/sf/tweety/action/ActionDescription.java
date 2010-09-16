package net.sf.tweety.action;

import java.util.Collection;

import net.sf.tweety.BeliefSet;

/**
 * This class represents an action description as a set of causal rules.
 * 
 * @author Sebastian Homann
 * @param <T> Type of causal rule to be kept in this action description.
 */
public abstract class ActionDescription< T extends CausalRule >
  extends BeliefSet< T >
{
  
  /**
   * Creates a new empty action description.
   */
  public ActionDescription()
  {
    super();
  }
  
  /**
   * Creates a new action description containing all elements in the collection
   * given.
   * 
   * @param c a collection of causal rules.
   */
  public ActionDescription( Collection< ? extends T > c )
  {
    super( c );
  }
  
}
