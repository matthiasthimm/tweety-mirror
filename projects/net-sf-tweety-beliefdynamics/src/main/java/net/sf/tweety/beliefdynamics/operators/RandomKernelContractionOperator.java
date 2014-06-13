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
package net.sf.tweety.beliefdynamics.operators;

import net.sf.tweety.beliefdynamics.kernels.*;
import net.sf.tweety.logics.pl.ClassicalEntailment;
import net.sf.tweety.logics.pl.syntax.*;

/**
 * This class implements a simple kernel base contraction for propositional logic with 
 * an incision function that randomly selects its incisions.
 * <br>
 * NOTE: results of this operator are not deterministic and may not be reproduced (however, each
 * 	 result is a valid kernel contraction)
 * 
 * @author Matthias Thimm
 */
public class RandomKernelContractionOperator extends KernelContractionOperator<PropositionalFormula> {

	/**
	 * Creates a new contraction operator.
	 */
	public RandomKernelContractionOperator() {
		super(new RandomIncisionFunction<PropositionalFormula>(), new ClassicalEntailment());
	}

}
