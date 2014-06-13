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
package net.sf.tweety.lp.asp.beliefdynamics.baserevision;

import java.util.Collection;
import java.util.HashSet;

import net.sf.tweety.commons.Formula;

/**
 * This class represents the set of remainder sets constructed
 * from a belief base.
 *  
 * @author Sebastian Homann
 *
 * @param <T> the type of formulas these remainder sets are based upon
 */
public abstract class RemainderSets<T extends Formula> extends HashSet<Collection<T>> {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Returns the belief base that seeded this remainder set. 
	 * @return a belief base
	 */
	public abstract Collection<T> getSourceBeliefBase();
}