package net.sf.tweety.action.grounding;

import java.util.Map;

import net.sf.tweety.logics.firstorderlogic.syntax.Constant;
import net.sf.tweety.logics.firstorderlogic.syntax.Variable;

/**
 * This class represents a single grounding requirement stating that two
 * variables are not allowed to be substituted by the same constant.
 * 
 * @author Sebastian Homann
 */
public class VarsNeqRequirement
  implements GroundingRequirement
{
  private Variable first;
  private Variable second;
  
  /**
   * Creates a new grounding requirement for the two given variables that are
   * not allowed to be set to the same constant in one ground instance.
   * 
   * @param first a variable.
   * @param second another variable.
   */
  public VarsNeqRequirement( Variable first, Variable second )
  {
    this.first = first;
    this.second = second;
  }
  
  /*
   * (non-Javadoc)
   * @see
   * net.sf.tweety.action.desc.c.syntax.GroundingRequirement#isValid(java.util
   * .Map)
   */
  @Override
  public boolean isValid( Map< Variable, Constant > assignment )
  {
    Constant a = assignment.get( first );
    Constant b = assignment.get( second );
    if ( a == null || b == null || !a.equals( b ) ) {
      return true;
    }
    return false;
  }
  
  /*
   * (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
      return true;
    if ( obj == null )
      return false;
    if ( getClass() != obj.getClass() )
      return false;
    VarsNeqRequirement other = (VarsNeqRequirement) obj;
    if ( !first.equals( other.first ) )
      return false;
    if ( !second.equals( other.second ) )
      return false;
    return true;
  }
  
  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( first == null ) ? 0 : first.hashCode() );
    result = prime * result + ( ( second == null ) ? 0 : second.hashCode() );
    return result;
  }
  
  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return first.getName() + "<>" + second.getName();
  }
}
