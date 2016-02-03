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
package net.sf.tweety.logics.pl.analysis;

import java.util.Collection;

import net.sf.tweety.logics.commons.analysis.AbstractMusEnumerator;
import net.sf.tweety.logics.commons.analysis.BeliefSetInconsistencyMeasure;
import net.sf.tweety.logics.pl.sat.PlMusEnumerator;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;

/**
 * This class implements the "MUS-variable based inconsistency measure" proposed in 
 * [Xiao,Ma. Inconsistency Measurement based on Variables in Minimal Unsatisfiable Subsets. ECAI2012.]
 * This inconsistency measure is defined as the ratio of the number of propositions appearing in 
 * any minimal inconsistent subsets and the total number of propositions. 
 * 
 * @author Matthias Thimm
 *
 */
public class MusVarInconsistencyMeasure extends BeliefSetInconsistencyMeasure<PropositionalFormula>{

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.commons.analysis.BeliefSetInconsistencyMeasure#inconsistencyMeasure(java.util.Collection)
	 */
	@Override
	public Double inconsistencyMeasure(Collection<PropositionalFormula> formulas) {
		// check empty set of formulas
		if(formulas.isEmpty())
			return 0d;		
		AbstractMusEnumerator<PropositionalFormula> musEnum = PlMusEnumerator.getDefaultEnumerator();
		Collection<Collection<PropositionalFormula>> muses = musEnum.minimalInconsistentSubsets(formulas);
		PropositionalSignature allSig = new PropositionalSignature();
		PropositionalSignature musSig = new PropositionalSignature();
		for(PropositionalFormula f: formulas)
			allSig.add(f.getSignature());
		for(Collection<PropositionalFormula> mus: muses)
			for(PropositionalFormula f: mus)
				musSig.add(f.getSignature());
		return new Double(musSig.size())/new Double(allSig.size());
	}
}
