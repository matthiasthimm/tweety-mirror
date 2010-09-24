package net.sf.tweety.action.description.c;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Signature;
import net.sf.tweety.action.ActionDescription;
import net.sf.tweety.action.CausalLaw;
import net.sf.tweety.action.description.c.syntax.CLaw;
import net.sf.tweety.action.description.c.syntax.DynamicLaw;
import net.sf.tweety.action.description.c.syntax.StaticLaw;
import net.sf.tweety.action.signature.ActionSignature;

/**
 * This class represents an action description for the action language C as a
 * set of causal rules, and provides some basic functionality such as grounding.
 * 
 * @author Sebastian Homann
 */
public class CActionDescription
  extends ActionDescription< CLaw >
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
  public CActionDescription( Collection< ? extends CausalLaw > c )
  {
    for ( CausalLaw r : c ) {
      if ( r instanceof CLaw ) {
        add( (CLaw) r );
      }
      else {
        throw new IllegalArgumentException(
          "The action description given contains laws of a wrong type." );
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
    for ( CLaw r : this )
      sig.addAll( r.getFormulas() );
    return sig;
  }
  
  /**
   * Calculates a new action description containing all ground instances of each
   * law in this action description.
   * 
   * @return a new action description containing only ground laws.
   */
  public CActionDescription ground()
  {
    Set< CLaw > laws = new HashSet< CLaw >();
    for ( CLaw law : this ) {
      laws.addAll( law.getAllGrounded() );
    }
    return new CActionDescription( laws );
  }
  
  /**
   * Calculates a new action description which descibes the same transition
   * system and contains only definite causal laws.
   * 
   * @return a new definite action description.
   * @throws IllegalStateException when there is no equivalent definite action
   *           description
   */
  public CActionDescription toDefinite()
    throws IllegalStateException
  {
    Set< CLaw > laws = new HashSet< CLaw >();
    for ( CLaw law : this ) {
      laws.addAll( law.toDefinite() );
    }
    return new CActionDescription( laws );
  }
  
  /**
   * Checks whether this action description contains any non-ground laws.
   * 
   * @return true iff each law in this action description is grounded.
   */
  public boolean isGround()
  {
    for ( CLaw law : this )
      if ( !law.isGround() )
        return false;
    return true;
  }
  
  /**
   * Checks whether this action description contains any non-definite laws.
   * 
   * @return ture iff each law in this action description is definite.
   */
  public boolean isDefinite()
  {
    for ( CLaw law : this )
      if ( !law.isDefinite() )
        return false;
    return true;
  }
  
  /**
   * Returns a set of all static laws contained in this action description.
   * 
   * @return a set of all static laws contained in this action description.
   */
  public Set< StaticLaw > getStaticLaws()
  {
    Set< StaticLaw > result = new HashSet< StaticLaw >();
    for ( CLaw r : this ) {
      if ( r instanceof StaticLaw )
        result.add( (StaticLaw) r );
    }
    return result;
  }
  
  /**
   * Returns a set of all dynamic laws contained in this action description.
   * 
   * @return a set of all dynamic laws contained in this action description.
   */
  public Set< DynamicLaw > getDynamicLaws()
  {
    Set< DynamicLaw > result = new HashSet< DynamicLaw >();
    for ( CLaw r : this ) {
      if ( r instanceof DynamicLaw )
        result.add( (DynamicLaw) r );
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
    String result = ":- laws\n";
    // static laws first
    for ( CLaw r : this.getStaticLaws() ) {
      result += r.toString() + "\n";
    }
    for ( CLaw r : this.getDynamicLaws() ) {
      result += r.toString() + "\n";
    }
    return result;
  }
}
