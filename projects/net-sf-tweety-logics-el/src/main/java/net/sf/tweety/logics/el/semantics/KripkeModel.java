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
package net.sf.tweety.logics.el.semantics;

import java.util.*;

import net.sf.tweety.commons.*;
import net.sf.tweety.logics.el.syntax.*;
import net.sf.tweety.logics.fol.syntax.*;

/**
 * This class models a Kripke model, i.e. a set of possible worlds (with evaluation functions) together with an
 * accessibility relation.
 * 
 * @author Matthias Thimm
 *
 */
public class KripkeModel extends AbstractInterpretation {

	/**
	 * The possible worlds of this model.
	 */
	private Set<Interpretation> possibleWorlds;
	
	/**
	 * The accessibility relation. 
	 */
	private AccessibilityRelation accRelation;
	
	/**
	 * Creates a new Kripke model.
	 * @param possibleWorlds a set of interpretations.
	 * @param accRelation an accessibility relation under the given interpretations.
	 */
	public KripkeModel(Set<Interpretation> possibleWorlds, AccessibilityRelation accRelation){		
		if(!possibleWorlds.containsAll(accRelation.getNodes()))
			throw new IllegalArgumentException("The accessibility relations mentions unknown interpretations.");
		this.possibleWorlds = possibleWorlds;
		this.accRelation = accRelation;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Interpretation#satisfies(net.sf.tweety.kr.Formula)
	 */
	@Override
	public boolean satisfies(Formula formula) throws IllegalArgumentException {
		if(!(formula instanceof FolFormula) || !(formula instanceof ModalFormula))
			throw new IllegalArgumentException("Formula " + formula + " is not a first-order formula nor a modal formula.");
		for(Interpretation i: this.possibleWorlds){
			if(formula instanceof FolFormula){
				if(!i.satisfies(formula))
					return false;
			}else{
				if(formula instanceof Necessity){
					for(Interpretation j: this.accRelation.getSuccessors(i)){
						if(!j.satisfies(((ModalFormula)formula).getFormula()))
								return false;
						
					}
				}else if(formula instanceof Possibility){
					boolean satisfied = false;
					for(Interpretation j: this.accRelation.getSuccessors(i)){
						if(j.satisfies(((ModalFormula)formula).getFormula())){
							satisfied = true;
							break;
						}
					}
					if(!satisfied) return false;
				}
			}
		}		
		return true;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Interpretation#satisfies(net.sf.tweety.kr.BeliefBase)
	 */
	@Override
	public boolean satisfies(BeliefBase beliefBase) throws IllegalArgumentException {
		// TODO IMPLEMENT ME
		return false;
	}

}
