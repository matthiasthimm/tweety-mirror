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
package net.sf.tweety.math.term;

/**
 * This class encapsulates a float as a term.
 * @author Matthias Thimm
 */
public class FloatConstant extends Constant {
	
	/**
	 * the actual float.
	 */	
	private double f;
	
	/**
	 * Creates a new Float.
	 * @param f a float.
	 */
	public FloatConstant(float f){
		this.f = f;
	}
	
	/**
	 * Creates a new Float.
	 * @param f a double.
	 */
	public FloatConstant(double f){
		this.f = new Float(f);
	}
	
	/**
	 * Get the value of this float.
	 * @return the value of this float.
	 */
	public double getValue(){
		return this.f;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.math.term.Term#isInteger()
	 */
	@Override
	public boolean isInteger(){
		return false;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.math.term.Term#toString()
	 */
	@Override
	public String toString(){
		return String.valueOf(this.f);
	}
}
