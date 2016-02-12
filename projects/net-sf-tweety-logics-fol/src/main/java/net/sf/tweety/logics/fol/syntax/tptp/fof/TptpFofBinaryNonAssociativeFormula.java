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
package net.sf.tweety.logics.fol.syntax.tptp.fof;

public abstract class TptpFofBinaryNonAssociativeFormula extends TptpFofBinaryFormula {

	TptpFofUnitaryFormula left;
	
	TptpFofUnitaryFormula right;
	
	TptpFofBinaryNonAssociative nonassoc;


	@Override
	public boolean isAssociative() {
		
		return false;
	}

	@Override
	public boolean isNonAssociative() {
		
		return true;
	}


}