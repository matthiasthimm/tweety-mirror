package net.sf.tweety.action.description.c.syntax;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Signature;
import net.sf.tweety.action.CausalRule;
import net.sf.tweety.action.grounding.GroundingRequirement;
import net.sf.tweety.action.signature.ActionSignature;
import net.sf.tweety.action.signature.FolFluentName;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Conjunction;
import net.sf.tweety.logics.firstorderlogic.syntax.Contradiction;
import net.sf.tweety.logics.firstorderlogic.syntax.Disjunction;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Tautology;

/**
 * The action description language C consists of two distinct expressions:
 * static rules and dynamic rules. Both share some common functionalities which
 * are implemented in this base class.
 * 
 * @author Sebastian Homann
 */
public abstract class CRule
  implements CausalRule
{
  protected FolFormula headFormula = new Contradiction();
  protected FolFormula ifFormula = new Tautology();
  protected Set< GroundingRequirement > requirements =
    new HashSet< GroundingRequirement >();
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.kr.Formula#getSignature()
   */
  public abstract Signature getSignature();
  
  /**
   * Returns true iff this rule is definite. A causal rule is definite if it's
   * head is a literal or a contradiction and all formulas are conjunctions of
   * literals.
   * 
   * @return true, if this rule is definite, false otherwise.
   */
  public abstract boolean isDefinite();
  
  /**
   * Returns an equivalent definite causal rule. A causal rule is definite if
   * it's head is a literal or a contradiction and all formulas are conjunctions
   * of literals.
   * 
   * @return the equivalent definite causal rule if one exists.
   * @throws IllegalStateException when there is no equivalent definite causal
   *           rule.
   */
  public abstract Set< CRule > toDefinite()
    throws IllegalStateException;
  
  /**
   * Returns the set of propositions in all formulas in this rule.
   * 
   * @return the set of propositions in all formulas in this rule.
   */
  public abstract Set< Atom > getAtoms();
  
  /**
   * Returns the set of formulas contained in this causal rule, e.g. in a static
   * rule, this contains the head formula and the if formula.
   * 
   * @return the set of formulas contained in this causal rule.
   */
  public abstract Set< FolFormula > getFormulas();
  
  /**
   * Creates an empty causal rule.
   */
  public CRule()
  {
  }
  
  /**
   * Creates a causal rule of the form caused headFormula if True
   * 
   * @param headFormula
   */
  public CRule( FolFormula headFormula )
  {
    setHeadFormula( headFormula );
  }
  
  /**
   * Creates a causal rule of the form caused headFormula if True requires
   * requirements
   * 
   * @param headFormula
   * @param requirements
   */
  public CRule( FolFormula headFormula, Set< GroundingRequirement > requirements )
  {
    setHeadFormula( headFormula );
    setGroundingRequirements( requirements );
  }
  
  /**
   * Creates a causal rule of the form caused headFormula if ifFormula requires
   * requirements
   * 
   * @param headFormula
   */
  public CRule( FolFormula headFormula, FolFormula ifFormula,
    Set< GroundingRequirement > requirements )
  {
    setHeadFormula( headFormula );
    setIfFormula( ifFormula );
    setGroundingRequirements( requirements );
  }
  
  /**
   * Creates a causal rule of the form caused headFormula if ifFormula
   * 
   * @param headFormula
   */
  public CRule( FolFormula headFormula, FolFormula ifFormula )
  {
    setHeadFormula( headFormula );
    setIfFormula( ifFormula );
  }
  
  private void setGroundingRequirements(
    Set< GroundingRequirement > requirements )
  {
    if ( requirements != null )
      this.requirements.addAll( requirements );
  }
  
  /**
   * Sets the headFormula of this causal Rule
   * 
   * @param headFormula The new headFormula of this causal rule.
   */
  private void setHeadFormula( FolFormula headFormula )
  {
    if ( headFormula == null ) {
      this.headFormula = new Contradiction();
      return;
    }
    
    if ( !( new ActionSignature( headFormula ).isRepresentable( headFormula ) ) ) {
      throw new IllegalArgumentException(
        "The formula given has an illegal form" );
    }
    this.headFormula = headFormula.collapseAssociativeFormulas();
  }
  
  /**
   * Sets the IfFormula of this causal rule
   * 
   * @param IfFormula The new IfFormula of this causal rule.
   */
  private void setIfFormula( FolFormula ifFormula )
  {
    if ( ifFormula == null ) {
      this.ifFormula = new Tautology();
      return;
    }
    if ( !( new ActionSignature( ifFormula ).isRepresentable( ifFormula ) ) ) {
      throw new IllegalArgumentException(
        "The formula given has an illegal form" );
    }
    this.ifFormula = ifFormula.collapseAssociativeFormulas();
  }
  
  public void addGroundingRequirement( GroundingRequirement c )
  {
    requirements.add( c );
  }
  
  /**
   * Returns the headFormula of this causal rule.
   * 
   * @return the headFormula of this causal rule.
   */
  public FolFormula getHeadFormula()
  {
    return headFormula;
  }
  
  /**
   * Returns the ifFormula of this causal rule.
   * 
   * @return the ifFormula of this causal rule.
   */
  public FolFormula getIfFormula()
  {
    return ifFormula;
  }
  
  public boolean isGround()
  {
    for ( Atom a : getAtoms() )
      if ( !a.isGround() )
        return false;
    return true;
  }
  
  /**
   * Returns the set of all grounded instances of this causal rule.
   * 
   * @return the set of all grounded instances of this causal rule.
   */
  public abstract Set< CRule > getAllGrounded();
  
  /**
   * Checks if a propositional formula is a valid head formula for a definite
   * causal rule, which means either a contradiction, a fluent or the negation
   * of a fluent.
   * 
   * @param pl a propositional formula
   * @return true, if pl is a valid definite head formula
   */
  protected boolean isValidDefiniteHead( RelationalFormula pl )
  {
    
    if ( pl instanceof Contradiction )
      return true;
    if ( pl instanceof Negation )
      pl = ( (Negation) pl ).getFormula();
    if ( pl instanceof Atom )
      return ( (Atom) pl ).getPredicate() instanceof FolFluentName;
    return false;
  }
  
  /**
   * a conjunctive clause is either a literal or a conjunction of literals.
   * 
   * @param pl a propositional formula
   * @return true, if pl is a conjunctive clause
   */
  protected boolean isConjunctiveClause( FolFormula pl )
  {
    if ( pl instanceof Conjunction ) {
      for ( RelationalFormula p : ( (Conjunction) pl ) ) {
        
        if ( !( (FolFormula) p ).isLiteral() )
          return false;
      }
    }
    else if ( pl instanceof Disjunction ) {
      return false;
    }
    return true;
  }
}
