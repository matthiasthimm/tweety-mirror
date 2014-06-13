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
package net.sf.tweety.action.signature;

import java.util.List;

import net.sf.tweety.action.FluentName;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.commons.syntax.Sort;

/**
 * This class represents a fluent name. It is implemented as a fol predicate to
 * allow for easy grounding of action descriptions.
 * 
 * @author Sebastian Homann
 */
public class FolFluentName
  extends Predicate
  implements FluentName
{
  
  /**
   * Creates a new folfluentname with the given name and arity.
   * 
   * @param name the name of this fluent name
   * @param arity the number of arguments of this fluent name
   */
  public FolFluentName( String name, int arity )
  {
    super( name, arity );
  }
  
  /**
   * Creates a new fluentname predicate with the given name and a list of
   * argument sorts, whose element count equals the arity of this predicate.
   * These arguments are used for grounding.
   * 
   * @param name the name of this fluent name
   * @param arguments a list of arguments 
   */
  public FolFluentName( String name, List< Sort > arguments )
  {
    super( name, arguments );
  }
  
  /**
   * Creates a new folfluentname predicate with the given name and zero-arity.
   * 
   * @param name the name of this fluent name
   */
  public FolFluentName( String name )
  {
    super( name );
  }
  
  /*
   * (non-Javadoc)
   * @see
   * net.sf.tweety.logics.firstorderlogic.syntax.FolBasicStructure#toString()
   */
  public String toString()
  {
    return "fluent " + this.getName();
  }
  
}
