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
package net.sf.tweety.logics.ml.analysis;

import java.util.List;

import net.sf.tweety.commons.Reasoner;
import net.sf.tweety.logics.fol.syntax.FolSignature;
import net.sf.tweety.logics.ml.MarkovLogicNetwork;

/**
 * This interface represents a compatibility measure for MLNs.
 * Given a set of MLNs it returns a value indicating how compatible
 * those MLNs are (i.e. how much the probabilities change when merging
 * the MLNs).
 * 
 * @author Matthias Thimm
 */
public interface CompatibilityMeasure {

	/**
	 * Measures the compatibility of the given MLNs wrt. the given signatures using the
	 * given reasoner.
	 * @param mlns a list of MLNs.
	 * @param reasoner some reasoner.
	 * @param signatures a set of signatures, one for each MLN.
	 */
	public abstract double compatibility(List<MarkovLogicNetwork> mlns, Reasoner reasoner, List<FolSignature> signatures);
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();
}
