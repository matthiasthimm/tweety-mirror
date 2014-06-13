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
package net.sf.tweety.math.func;

import java.util.Vector;

import net.sf.tweety.math.term.Logarithm;
import net.sf.tweety.math.term.Term;

/**
 * The entropy function.
 * @author Matthias Thimm
 */
public class EntropyFunction implements SimpleRealValuedFunction {

	/* (non-Javadoc)
	 * @see net.sf.tweety.math.func.Function#eval(java.lang.Object)
	 */
	@Override
	public Double eval(Vector<Double> x) {
		Double result = 0d;
		for(Double d: x){
			if(d < 0)
				throw new IllegalArgumentException("Entropy is undefined if negative elements are present.");
			if(d > 0)
				result -= Math.log(d) * d;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.math.func.RealValuedFunction#getTerm(java.util.Vector)
	 */
	@Override
	public Term getTerm(Vector<Term> element) {
		Term result = null;
		for(Term t: element){
			if(result == null)
				result = t.mult(new Logarithm(t));
			else result = result.minus(t.mult(new Logarithm(t)));
		}
		return result;
	}

}
