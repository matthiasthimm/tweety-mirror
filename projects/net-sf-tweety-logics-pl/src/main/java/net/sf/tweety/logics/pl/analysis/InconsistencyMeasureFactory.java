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
package net.sf.tweety.logics.pl.analysis;

import java.io.File;

import net.sf.tweety.commons.BeliefSet;
import net.sf.tweety.logics.commons.analysis.*;
import net.sf.tweety.logics.pl.sat.PlMusEnumerator;
import net.sf.tweety.logics.pl.sat.SatSolver;
import net.sf.tweety.logics.pl.semantics.PossibleWorldIterator;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

/**
 * Main factory for retrieving inconsistency measures for propositional logic.
 * @author Matthias Thimm
 */
public abstract class InconsistencyMeasureFactory {

	/** An enumeration of all available inconsistency measures. */
	public enum Measure{
		DRASTIC ("drastic", "Drastic Inconsistency Measure", "/inc/DrasticInconsistencyMeasure.html"),
		MI ("mi", "MI Inconsistency Measure", "/inc/MiInconsistencyMeasure.html"),
		MIC ("mic", "MIC Inconsistency Measure", "/inc/MicInconsistencyMeasure.html"),
		CONTENSION ("contension", "Contension Inconsistency Measure", "/inc/ContensionInconsistencyMeasure.html"),
		HS ("hs", "Hitting Set Inconsistency Measure", "/inc/HittingSetInconsistencyMeasure.html"),
		PR ("pr", "P Inconsistency Measure", "/inc/PrInconsistencyMeasure.html"),
		ETA ("eta", "Eta Inconsistency Measure", "/inc/EtaInconsistencyMeasure.html");
		
		public String id;
		public String label;
		public File description;
		
		Measure(String id, String label, String description){
			this.id = id;
			this.label = label;
			this.description = new File(getClass().getResource(description).getFile());
		}
		
		public static Measure getMeasure(String id){
			for(Measure m: Measure.values())
				if(m.id.equals(id))
					return m;
			return null;
		}
	}
		
	/**
	 * Creates a new inconsistency measure of the given type with default
	 * settings.
	 * @param im some identifier of an inconsistency measure.
	 * @return the requested inconsistency measure.
	 */
	public static InconsistencyMeasure<BeliefSet<PropositionalFormula>> getInconsistencyMeasure(Measure im){
		switch(im){
			case DRASTIC:
				return new DrasticInconsistencyMeasure<PropositionalFormula>(SatSolver.getDefaultSolver());
			case CONTENSION:
				return new ContensionInconsistencyMeasure();
			case MI:
				return new MiInconsistencyMeasure<PropositionalFormula>(PlMusEnumerator.getDefaultEnumerator());
			case MIC:
				return new MicInconsistencyMeasure<PropositionalFormula>(PlMusEnumerator.getDefaultEnumerator());
			case HS:
				return new HsInconsistencyMeasure<PropositionalFormula>(new PossibleWorldIterator());
			case PR:
				return new PrInconsistencyMeasure<PropositionalFormula>(PlMusEnumerator.getDefaultEnumerator());
			case ETA:
				return new EtaInconsistencyMeasure<PropositionalFormula>(new PossibleWorldIterator());
			default:
				throw new RuntimeException("No measure found for " + im.toString());
		}
	}
}
