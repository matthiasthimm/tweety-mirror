package net.sf.tweety.action.description.c;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Signature;
import net.sf.tweety.action.ActionDescription;
import net.sf.tweety.action.CausalRule;
import net.sf.tweety.action.description.c.syntax.CRule;
import net.sf.tweety.action.description.c.syntax.DynamicRule;
import net.sf.tweety.action.description.c.syntax.StaticRule;
import net.sf.tweety.action.signature.ActionSignature;

/**
 * This class represents an action description for the action language C as a
 * set of causal rules, and provides some basic functionality such as grounding.
 * 
 * @author Sebastian Homann
 */
public class CActionDescription
  extends ActionDescription< CRule >
{
  /**
   * Creates a new empty action description.
   */
  public CActionDescription()
  {
    super();
  }
  
  /**
   * Creates a new belief set with the given collection of formulae.
   * 
   * @param c a collection of formulae.
   */
  public CActionDescription( Collection< ? extends CausalRule > c )
  {
    for ( CausalRule r : c ) {
      if ( r instanceof CRule ) {
        add( (CRule) r );
      }
      else {
        throw new IllegalArgumentException(
          "The action description given contains rules of a wrong type." );
      }
    }
  }
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.BeliefSet#getSignature()
   */
  @Override
  public Signature getSignature()
  {
    ActionSignature sig = new ActionSignature();
    for ( CRule r : this )
      sig.addAll( r.getFormulas() );
    return sig;
  }
  
  /**
   * Calculates a new action description containing all ground instances of each
   * rule in this action description.
   * 
   * @return a new action description containing only ground rules.
   */
  public CActionDescription ground()
  {
    Set< CRule > rules = new HashSet< CRule >();
    for ( CRule rule : this ) {
      rules.addAll( rule.getAllGrounded() );
    }
    return new CActionDescription( rules );
  }
  
  /**
   * Calculates a new action description which descibes the same transition
   * system and contains only definite causal rules.
   * 
   * @return a new definite action description.
   * @throws IllegalStateException when there is no equivalent definite action
   *           description
   */
  public CActionDescription toDefinite()
    throws IllegalStateException
  {
    Set< CRule > rules = new HashSet< CRule >();
    for ( CRule rule : this ) {
      rules.addAll( rule.toDefinite() );
    }
    return new CActionDescription( rules );
  }
  
  /**
   * Checks whether this action description contains any non-ground rules.
   * 
   * @return true iff each rule in this action description is grounded.
   */
  public boolean isGround()
  {
    for ( CRule rule : this )
      if ( !rule.isGround() )
        return false;
    return true;
  }
  
  /**
   * Checks whether this action description contains any non-definite rules.
   * 
   * @return ture iff each rule in this action description is definite.
   */
  public boolean isDefinite()
  {
    for ( CRule rule : this )
      if ( !rule.isDefinite() )
        return false;
    return true;
  }
  
  /**
   * Returns a set of all static rules contained in this action description.
   * 
   * @return a set of all static rules contained in this action description.
   */
  public Set< StaticRule > getStaticRules()
  {
    Set< StaticRule > result = new HashSet< StaticRule >();
    for ( CRule r : this ) {
      if ( r instanceof StaticRule )
        result.add( (StaticRule) r );
    }
    return result;
  }
  
  /**
   * Returns a set of all dynamic rules contained in this action description.
   * 
   * @return a set of all dynamic rules contained in this action description.
   */
  public Set< DynamicRule > getDynamicRules()
  {
    Set< DynamicRule > result = new HashSet< DynamicRule >();
    for ( CRule r : this ) {
      if ( r instanceof DynamicRule )
        result.add( (DynamicRule) r );
    }
    return result;
  }
  
  /**
   * Returns a string representation of this action description in human
   * readable form, which may be written to a file or printed on screen.
   * 
   * @return a string representation of this action description.
   */
  public String toOutputString()
  {
    String result = ":- rules\n";
    // static rules first
    for ( CRule r : this.getStaticRules() ) {
      result += r.toString() + "\n";
    }
    for ( CRule r : this.getDynamicRules() ) {
      result += r.toString() + "\n";
    }
    return result;
  }
}
