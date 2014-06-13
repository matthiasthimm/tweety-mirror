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
package net.sf.tweety.arg.deductive.semantics.attacks;

import net.sf.tweety.arg.deductive.semantics.DeductiveArgument;
import net.sf.tweety.logics.pl.ClassicalEntailment;
import net.sf.tweety.logics.pl.syntax.Negation;


/**
 * This attack notion models the rebuttal relation; A is defeated by B iff claim(B) == \neg claim(A).
 * @author Matthias Thimm
 */
public class Rebuttal implements Attack{

	/** Singleton instance. */
	private static Rebuttal instance = new Rebuttal();
	
	/** Private constructor. */
	private Rebuttal(){};
	
	/**
	 * Returns the singleton instance of this class.
	 * @return the singleton instance of this class.
	 */
	public static Rebuttal getInstance(){
		return Rebuttal.instance;
	}	
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.argumentation.deductive.semantics.attacks.Attack#isAttackedBy(net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument, net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument)
	 */
	@Override
	public boolean isAttackedBy(DeductiveArgument a, DeductiveArgument b) {
		ClassicalEntailment entailment = new ClassicalEntailment();
		if(entailment.isEquivalent(b.getClaim(), new Negation(a.getClaim())))
			return true;		
		return false;
	}

}
