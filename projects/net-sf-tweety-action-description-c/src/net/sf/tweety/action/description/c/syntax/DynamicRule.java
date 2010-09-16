package net.sf.tweety.action.description.c.syntax;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.Signature;
import net.sf.tweety.action.grounding.GroundingRequirement;
import net.sf.tweety.action.grounding.GroundingTools;
import net.sf.tweety.action.signature.ActionSignature;
import net.sf.tweety.logics.firstorderlogic.syntax.*;

/**
 * A dynamic rule in C has the form caused F if G after U where F is a
 * propositional formula over the set of fluent names (called headFormula) G is
 * a propositional formula over the set of fluent names (called ifFormula) U is
 * a propositional formula over the set of fluent names and the set of action
 * names (called afterFormula)
 * 
 * @author wutsch
 */
public class DynamicRule
  extends CRule
{
  
  protected FolFormula afterFormula = new Tautology();
  
  /**
   * Constructs a new empty dynamic rule.
   */
  public DynamicRule()
  {
    super();
  }
  
  /**
   * Creates a new dynamic rule of the form: caused headFormula if ifFormula
   * after afterFormula
   * 
   * @param headFormula
   * @param ifFormula
   * @param afterFormula
   */
  public DynamicRule( FolFormula headFormula, FolFormula ifFormula,
    FolFormula afterFormula )
  {
    super( headFormula, ifFormula );
    setAfterFormula( afterFormula );
  }
  
  /**
   * Creates a new dynamic rule of the form: caused headFormula if ifFormula
   * after afterFormula requires requirements
   * 
   * @param headFormula
   * @param ifFormula
   * @param afterFormula
   * @param requirements
   */
  public DynamicRule( FolFormula headFormula, FolFormula ifFormula,
    FolFormula afterFormula, Set< GroundingRequirement > requirements )
  {
    super( headFormula, ifFormula, requirements );
    setAfterFormula( afterFormula );
  }
  
  /**
   * Creates a new dynamic rule of the form caused headFormula after
   * afterFormula
   * 
   * @param headFormula
   * @param afterFormula
   */
  public DynamicRule( FolFormula headFormula, FolFormula afterFormula )
  {
    super( headFormula );
    setAfterFormula( afterFormula );
  }
  
  /**
   * Creates a new dynamic rule of the form caused headFormula after
   * afterFormula requires requirements
   * 
   * @param headFormula
   * @param afterFormula
   * @param requirements
   */
  public DynamicRule( FolFormula headFormula, FolFormula afterFormula,
    Set< GroundingRequirement > requirements )
  {
    super( headFormula, requirements );
    setAfterFormula( afterFormula );
  }
  
  /**
   * Sets the afterFormula of this causal Rule
   * 
   * @param afterFormula The new afterFormula of this causal rule.
   */
  private void setAfterFormula( FolFormula afterFormula )
  {
    if ( afterFormula == null ) {
      return;
    }
    
    if ( !( new ActionSignature( afterFormula ).isRepresentable( afterFormula ) ) ) {
      throw new IllegalArgumentException(
        "The formula given has an illegal form" );
    }
    this.afterFormula = afterFormula.collapseAssociativeFormulas();
  }
  
  /**
   * Returns the afterFormula of this causal rule.
   * 
   * @return the afterFormula of this causal rule.
   */
  public FolFormula getAfterFormula()
  {
    return afterFormula;
  }
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.action.desc.c.syntax.CausalRule#isDefinite()
   */
  @Override
  public boolean isDefinite()
  {
    if ( !headFormula.isLiteral() )
      return false;
    
    if ( !isConjunctiveClause( ifFormula ) )
      return false;
    
    if ( !isConjunctiveClause( afterFormula ) )
      return false;
    
    return true;
  }
  
  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    if ( ifFormula.equals( headFormula ) && ifFormula.equals( afterFormula ) ) {
      return "inertial " + headFormula.toString();
    }
    else {
      String r = "caused " + headFormula.toString();
      if ( !( ifFormula instanceof Tautology ) )
        r += " if " + ifFormula.toString();
      
      r += " after " + afterFormula.toString();
      
      return r;
    }
  }
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.action.desc.c.syntax.CausalRule#getSignature()
   */
  @Override
  public Signature getSignature()
  {
    ActionSignature sig = new ActionSignature( headFormula );
    sig.add( ifFormula );
    sig.add( afterFormula );
    return sig;
  }
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.action.desc.c.syntax.CausalRule#getAtoms()
   */
  @Override
  public Set< Atom > getAtoms()
  {
    Set< Atom > result = new HashSet< Atom >();
    result.addAll( headFormula.getAtoms() );
    result.addAll( ifFormula.getAtoms() );
    result.addAll( afterFormula.getAtoms() );
    return result;
  }
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.action.desc.c.syntax.CausalRule#toDefinite()
   */
  @Override
  public Set< CRule > toDefinite()
  {
    if ( !isValidDefiniteHead( headFormula ) ) {
      throw new IllegalStateException(
        "Cannot convert causal rule with nonliteral head formula to definite form." );
    }
    Set< CRule > definit = new HashSet< CRule >();
    Set< RelationalFormula > ifClauses = new HashSet< RelationalFormula >();
    Set< RelationalFormula > afterClauses = new HashSet< RelationalFormula >();
    
    FolFormula ifDNF = ifFormula.toDnf();
    if ( ifDNF instanceof Disjunction ) {
      Disjunction conjClause = (Disjunction) ifDNF;
      for ( RelationalFormula p : conjClause ) {
        ifClauses.add( p );
      }
    }
    else {
      ifClauses.add( ifDNF );
    }
    FolFormula afterDNF = afterFormula.toDnf().collapseAssociativeFormulas();
    if ( afterDNF instanceof Disjunction ) {
      Disjunction conjClause = (Disjunction) afterDNF;
      for ( RelationalFormula p : conjClause ) {
        afterClauses.add( p );
      }
    }
    else {
      afterClauses.add( afterDNF );
    }
    for ( RelationalFormula ifClause : ifClauses ) {
      for ( RelationalFormula afterClause : afterClauses ) {
        definit.add( new DynamicRule( headFormula, (FolFormula) ifClause,
          (FolFormula) afterClause, requirements ) );
      }
    }
    
    return definit;
  }
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.action.desc.c.syntax.CausalRule#getAllGroundings()
   */
  @Override
  public Set< CRule > getAllGrounded()
  {
    Set< CRule > result = new HashSet< CRule >();
    Set< Variable > variables = new HashSet< Variable >();
    
    for ( Atom a : getAtoms() ) {
      variables.addAll( a.getUnboundVariables() );
    }
    Set< Map< Variable, Constant >> substitutions =
      GroundingTools.getAllSubstitutions( variables );
    
    for ( Map< Variable, Constant > map : substitutions ) {
      if ( GroundingTools.isValidGroundingApplication( map, requirements ) )
        result.add( new DynamicRule(
          (FolFormula) headFormula.substitute( map ), (FolFormula) ifFormula
            .substitute( map ), (FolFormula) afterFormula.substitute( map ),
          requirements ) );
    }
    return result;
  }
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.action.description.c.syntax.CRule#getFormulas()
   */
  @Override
  public Set< FolFormula > getFormulas()
  {
    Set< FolFormula > result = new HashSet< FolFormula >();
    result.add( headFormula );
    result.add( ifFormula );
    result.add( afterFormula );
    return result;
  }
}
