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
package net.sf.tweety.arg.delp.semantics;

import net.sf.tweety.arg.delp.*;
import net.sf.tweety.arg.delp.syntax.*;

/**
 * This class implements the empty criterion to compare two arguments. Using this criterion no two arguments are comparable.
 *
 * @author Matthias Thimm
 *
 */
public class EmptyCriterion extends ComparisonCriterion {

	/* (non-Javadoc)
	 * @see edu.cs.ai.thimm.uacs.defeasiblelogicprogramming.ComparisonCriterion#compare(edu.cs.ai.thimm.uacs.defeasiblelogicprogramming.DeLPArgument, edu.cs.ai.thimm.uacs.defeasiblelogicprogramming.DeLPArgument, edu.cs.ai.thimm.uacs.defeasiblelogicprogramming.DefeasibleLogicProgram)
	 */
	@Override
	public int compare(DelpArgument argument1, DelpArgument argument2, DefeasibleLogicProgram context) {
		return ComparisonCriterion.NOT_COMPARABLE;
	}

}
