package net.sf.tweety.action.description.syntax;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.Signature;
import net.sf.tweety.action.grounding.GroundingRequirement;
import net.sf.tweety.action.grounding.GroundingTools;
import net.sf.tweety.action.signature.ActionSignature;
import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.Variable;
import net.sf.tweety.logics.fol.syntax.Disjunction;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.RelationalFormula;
import net.sf.tweety.logics.fol.syntax.Tautology;

/**
 * This class represents a static rule in C, which has the following form:
 * caused H if G where H is a propositional formula over the set of fluents and
 * G is a propositional formula over the set of fluents and the set of actions
 * 
 * @author Sebastian Homann
 */
public class StaticLaw
  extends CLaw
{
  
  /**
   * Creates an empty static law.
   */
  public StaticLaw()
  {
    super();
  }
  
  /**
   * Creates an empty static law of the form caused headFormula if True
   * 
   * @param headFormula
   */
  public StaticLaw( FolFormula headFormula )
  {
    super( headFormula );
  }
  
  /**
   * Creates an empty static law of the form caused headFormula if True
   * requires requirements
   * 
   * @param headFormula
   * @param requirements
   */
  public StaticLaw( FolFormula headFormula,
    Set< GroundingRequirement > requirements )
  {
    super( headFormula, requirements );
  }
  
  /**
   * Creates an empty static law of the form caused headFormula if ifFormula
   * 
   * @param headFormula
   * @param ifFormula
   */
  public StaticLaw( FolFormula headFormula, FolFormula ifFormula )
  {
    super( headFormula, ifFormula );
  }
  
  /**
   * Creates an empty static law of the form caused headFormula if ifFormula
   * requires requirements
   * 
   * @param headFormula
   * @param ifFormula
   * @param requirements
   */
  public StaticLaw( FolFormula headFormula, FolFormula ifFormula,
    Set< GroundingRequirement > requirements )
  {
    super( headFormula, ifFormula, requirements );
  }
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.action.desc.c.syntax.CausalRule#isDefinite()
   */
  @Override
  public boolean isDefinite()
  {
    return isValidDefiniteHead( headFormula ) &&
      isConjunctiveClause( ifFormula );
  }
  
  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    String r = "caused " + headFormula.toString();
    if ( !( ifFormula instanceof Tautology ) )
      r += " if " + ifFormula.toString();
    
    return r;
  }
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.action.desc.c.syntax.CausalRule#toDefinite()
   */
  @Override
  public Set< CLaw > toDefinite()
    throws IllegalStateException
  {
    Set< CLaw > result = new HashSet< CLaw >();
    if ( !isValidDefiniteHead( headFormula ) )
      throw new IllegalStateException(
        "Cannot convert causal law with nonliteral head formula to definite form." );
    FolFormula ifForm = ifFormula.toDnf();
    if ( ifForm instanceof Disjunction ) {
      Disjunction conjClause = (Disjunction) ifForm;
      for ( RelationalFormula p : conjClause ) {
        result
          .add( new StaticLaw( headFormula, (FolFormula) p, requirements ) );
      }
    }
    else {
      result.add( new StaticLaw( headFormula, ifForm, requirements ) );
    }
    return result;
  }
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.kr.Formula#getSignature()
   */
  @Override
  public Signature getSignature()
  {
    ActionSignature sig = new ActionSignature( headFormula );
    sig.add( ifFormula );
    return sig;
  }
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.action.desc.c.syntax.CausalRule#getAtoms()
   */
  @Override
  public Set< FOLAtom > getAtoms()
  {
    Set< FOLAtom > atoms = new HashSet< FOLAtom >();
    atoms.addAll( headFormula.getAtoms() );
    atoms.addAll( ifFormula.getAtoms() );
    return atoms;
  }
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.action.desc.c.syntax.CausalRule#getAllGroundings()
   */
  @Override
  public Set< CLaw > getAllGrounded()
  {
    Set< CLaw > result = new HashSet< CLaw >();
    Set< Variable > variables = new HashSet< Variable >();
    
    for ( FOLAtom a : getAtoms() ) {
      variables.addAll( a.getUnboundVariables() );
    }
    Set< Map< Variable, Constant >> substitutions =
      GroundingTools.getAllSubstitutions( variables );
    for ( Map< Variable, Constant > map : substitutions ) {
      if ( GroundingTools.isValidGroundingApplication( map, requirements ) )
        result.add( new StaticLaw( (FolFormula) headFormula.substitute( map ),
          (FolFormula) ifFormula.substitute( map ), requirements ) );
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
    return result;
  }
}
