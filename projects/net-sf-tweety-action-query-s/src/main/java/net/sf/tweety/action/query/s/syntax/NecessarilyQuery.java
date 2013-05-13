package net.sf.tweety.action.query.s.syntax;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.action.signature.FolAction;
import net.sf.tweety.action.signature.ActionSignature;
import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.Variable;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

/**
 * This class represents a necessarily query in the action query language S.
 * Such queries have the following form: 
 *   necessarily F after A_1 ; A_2 ; ... ; A_n 
 * where F is a state formula, and each A_i (0 < i < n+1) is an action.
 * 
 * @author Sebastian Homann
 */
public class NecessarilyQuery
  extends QueryProposition
{
  private List< FolAction > actions = new LinkedList< FolAction >();
  
  /**
   * Creates a new necessarily query with an empty action sequence.
   * 
   * @param formula the inner formula of this query.
   */
  public NecessarilyQuery( FolFormula formula )
  {
    super( formula, "necessarily " + formula.toString() );
    if ( !getActionSignature().isValidFormula( formula ) )
      throw new IllegalArgumentException(
        "Invalid inner formula in query proposition." );
  }
  
  /**
   * Creates a new necessarily query with the given inner formula and list of
   * actions.
   * 
   * @param formula the inner formula of this necessarily query.
   * @param actions the action sequence of this necessarily query.
   */
  public NecessarilyQuery( FolFormula formula, List< FolAction > actions )
  {
    super( formula, "necessarily " + formula.toString() + " after " +
      actions.toString() );
    this.actions.addAll( actions );
    
    if ( !getActionSignature().isValidFormula( formula ) )
      throw new IllegalArgumentException(
        "Invalid inner formula in query proposition." );
  }
  
  /**
   * Creates a new necessarily query with the given inner formula and a single
   * action.
   * 
   * @param formula the inner formula of this necessarily query.
   * @param action a single action.
   */
  public NecessarilyQuery( FolFormula formula, FolAction action )
  {
    super( formula, "necessarily " + formula.toString() + " after " +
      action.toString() );
    actions.add( action );
    
    if ( !getActionSignature().isValidFormula( formula ) )
      throw new IllegalArgumentException(
        "Invalid inner formula in query proposition." );
  }
  
  /**
   * Returns the list of actions of this necessarily query in the correct order.
   * 
   * @return the list of actions of this necessarily query in the correct order.
   */
  public List< FolAction > getActions()
  {
    List< FolAction > result = new LinkedList< FolAction >();
    result.addAll( actions );
    return result;
  }
  
  /*
   * (non-Javadoc)
   * @see
   * net.sf.tweety.action.query.s.syntax.QueryProposition#getActionSignature()
   */
  @Override
  public ActionSignature getActionSignature()
  {
    ActionSignature result = super.getActionSignature();
    for ( FolAction a : actions ) {
      result.addAll( a.getAtoms() );
    }
    return result;
  }
  
  /*
   * (non-Javadoc)
   * @see
   * net.sf.tweety.action.query.s.syntax.QueryProposition#substitute(java.util
   * .Map)
   */
  @Override
  public QueryProposition substitute( Map< Variable, Constant > map )
  {
    List< FolAction > newActions = new LinkedList< FolAction >();
    for ( FolAction a : actions ) {
      newActions.add( a.substitute( map ) );
    }
    return new NecessarilyQuery( (FolFormula) formula.substitute( map ),
      newActions );
  }
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.action.query.s.syntax.QueryProposition#getInnerActions()
   */
  @Override
  public Set< FolAction > getInnerActions()
  {
    return new HashSet< FolAction >( actions );
  }
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.action.query.s.syntax.QueryProposition#getVariables()
   */
  @Override
  public Set< Variable > getVariables()
  {
    Set< Variable > result = formula.getUnboundVariables();
    for ( FolAction fa : actions ) {
      for ( Atom a : fa.getAtoms() ) {
        result.addAll( a.getUnboundVariables() );
      }
    }
    
    return result;
  }
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.action.query.s.syntax.QueryProposition#toString()
   */
  @Override
  public String toString()
  {
    String result = "necessarily [";
    result += formula.toString();
    result += "]";
    if ( !actions.isEmpty() )
      result +=
        " after " +
          actions.toString().replaceAll( "\\},", "\\};" ).replaceAll(
            "\\[|\\]", "" );
    return result;
  }
}
