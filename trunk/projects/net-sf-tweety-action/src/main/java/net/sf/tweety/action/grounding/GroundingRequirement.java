/*
 *  This file is part of "Tweety", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  Tweety is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *  Copyright 2016 The Tweety Project Team <http://tweetyproject.org/contact/>
 */
package net.sf.tweety.action.grounding;

import java.util.Map;

import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.Variable;

/**
 * This is a common interface for grounding constraints, which have to be met by
 * a grounder when calculating possible applications of constants to variables.
 * Example: 
 *   caused at(X) after go(X) && at(Y) requires X <> Y 
 * Here, variables X and Y are required to have different values.
 * 
 * @author Sebastian Homann
 */
public interface GroundingRequirement
{
  /**
   * This method checks, if an assignment of constants to variables satisfies a
   * given grounding condition.
   * 
   * @param assignment the assignment to be validated.
   * @return true, if the assignment satisfies this requirement.
   */
  public boolean isValid( Map< Variable, Constant > assignment );
}
