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
package net.sf.tweety.beliefdynamics;

import java.util.*;

import net.sf.tweety.commons.*;

/**
 * This is the interface for a classic multiple belief base expansion operator, ie. an
 * operator that takes some set of formulas and another set of formulas and expands
 * the former by the latter. 
 * 
 * @author Matthias Thimm
 *
 * @param <T> The type of formulas that this operator works on.
 */
public abstract class MultipleBaseExpansionOperator<T extends Formula> implements BaseExpansionOperator<T> {

	/* (non-Javadoc)
	 * @see net.sf.tweety.beliefdynamics.BaseExpansionOperator#expand(java.util.Collection, net.sf.tweety.Formula)
	 */
	public Collection<T> expand(Collection<T> base, T formula) {
		Set<T> formulas = new HashSet<T>();
		formulas.add(formula);
		return this.expand(base, formulas);
	}

	/**
	 * Expands the first collection of formulas by the second collection of formulas.
	 * @param base some collection of formulas.
	 * @param formulas some formulas.
	 * @return the expanded collection.
	 */
	public abstract Collection<T> expand(Collection<T> base, Collection<T> formulas);
	
}
