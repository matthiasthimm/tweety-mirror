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
package net.sf.tweety.math.func.fuzzy;

/**
 * Implements the default fuzzy negation x -> x-1
 * 
 * @author Matthias Thimm
 */
public class DefaultNegation extends FuzzyNegation{

	/* (non-Javadoc)
	 * @see net.sf.tweety.math.func.fuzzy.FuzzyNegation#eval(java.lang.Double)
	 */
	@Override
	public Double eval(Double x) {
		if(x < 0 || x > 1)
			throw new IllegalArgumentException("Default negation only defined for values in [0,1].");
		return 1-x;
	}
}
