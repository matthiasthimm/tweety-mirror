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
package net.sf.tweety.logics.commons.analysis;

import java.util.Collection;

import net.sf.tweety.commons.Formula;

/**
 * This class models the I_M inconsistency measure from e.g. [Grant,Hunter,2011a]. It takes
 * as inconsistency value the number of maximal consistent subsets plus the number of formulas
 * that are self-contradicting minus 1.
 * 
 * @author Matthias Thimm
 */
public class MaInconsistencyMeasure<S extends Formula> extends BeliefSetInconsistencyMeasure<S> {

	/** The MUs enumerator. */
	private MusEnumerator<S> enumerator;
	
	/**
	 * Creates a new inconsistency measure.
	 * @param enumerator some MUs enumerator
	 */
	public MaInconsistencyMeasure(MusEnumerator<S> enumerator){
		this.enumerator = enumerator;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.commons.analysis.BeliefSetInconsistencyMeasure#inconsistencyMeasure(java.util.Collection)
	 */
	@Override
	public Double inconsistencyMeasure(Collection<S> formulas) {
		Double scs = 0d;
		for(S f: formulas)
			if(!this.enumerator.isConsistent(f))
				scs++;
		return scs + this.enumerator.maximalConsistentSubsets(formulas).size() - 1;
	}

}
