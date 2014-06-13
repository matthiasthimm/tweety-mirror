/*
 *  This file is part of "Tweety", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  Tweety is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.tweety.action.transitionsystem;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.action.signature.FolFluentName;
import net.sf.tweety.logics.fol.syntax.FOLAtom;

/**
 * Represents a state in an action transition system, which is a representation
 * of an interpretation of all fluent names in an action description.
 * 
 * @author Sebastian Homann
 */
public class State
{
  private Set< FOLAtom > fluents = new HashSet< FOLAtom >();
  
  /**
   * Creates a new State with a set of fluents that are mapped to true.
   * 
   * @param fluents The fluents which are mapped to true by this state.
   */
  public State( Set< FOLAtom > fluents )
  {
    this.fluents.addAll( fluents );
  }
  
  /**
   * Returns true iff the fluent given is mapped to true by this state.
   * 
   * @param fluent
   * @return true iff the fluent given is mapped to true by this state.
   */
  public boolean isMappedToTrue( FOLAtom fluent )
  {
    if ( fluent.getPredicate() instanceof FolFluentName )
      return fluents.contains( fluent );
    return false;
  }
  
  /**
   * Returns the set of fluent atoms that are mapped to true by this state.
   */
  public Set< FOLAtom > getPositiveFluents()
  {
    return new HashSet< FOLAtom >( fluents );
  }
  
  /*
   * (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals( Object obj )
  {
    return fluents.equals( obj );
  }
  
  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return fluents.hashCode();
  }
  
  /*
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return fluents.toString();
  }
}
