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
package net.sf.tweety.logics.commons.syntax.interfaces;



/**
 * This interface models a classical formula, i.e. a formula that can be connected
 * to other classical formulas using AND and OR and where the complement is
 * well-defined.
 * 
 * @author Matthias Thimm
 * @author Tim Janus
 */
public interface ClassicalFormula extends 
	Disjunctable, 
	Conjuctable, 
	Invertable,
	ProbabilityAware {
}
