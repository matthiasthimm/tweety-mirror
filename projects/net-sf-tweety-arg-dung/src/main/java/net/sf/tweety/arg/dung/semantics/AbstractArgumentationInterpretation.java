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
package net.sf.tweety.arg.dung.semantics;

import java.util.Iterator;
import java.util.Set;

import net.sf.tweety.arg.dung.DungTheory;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.commons.AbstractInterpretation;
import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.Formula;

/**
 * This abstract class acts as a common ancestor for interpretations to
 * abstract argumentation frameworks.
 * 
 * @author Matthias Thimm
 */
public abstract class AbstractArgumentationInterpretation extends AbstractInterpretation {

	/* (non-Javadoc)
	 * @see net.sf.tweety.Interpretation#satisfies(net.sf.tweety.Formula)
	 */
	@Override
	public boolean satisfies(Formula formula) throws IllegalArgumentException {
		if(!(formula instanceof Argument)) throw new IllegalArgumentException("Argument expected.");
		return this.getArgumentsOfStatus(ArgumentStatus.IN).contains(formula);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.Interpretation#satisfies(net.sf.tweety.BeliefBase)
	 */
	@Override
	public boolean satisfies(BeliefBase beliefBase) throws IllegalArgumentException {
		throw new IllegalArgumentException("Satisfaction of belief bases by extensions is undefined.");
	}

	/**
	 * returns true if every attacker on <source>argument</source> is attacked by some 
	 * accepted argument wrt. the given theory.
	 * @param argument an argument
	 * @param dungTheory a Dung theory (the knowledge base)
	 * @return true if every attacker on <source>argument</source> is attacked by some 
	 * accepted argument wrt. the given theory.
	 */
	public boolean isAcceptable(Argument argument, DungTheory dungTheory){
		Set<Argument> attackers = dungTheory.getAttackers(argument);
		Iterator<Argument> it = attackers.iterator();
		while (it.hasNext())			
			if(!dungTheory.isAttacked(it.next(),this.getArgumentsOfStatus(ArgumentStatus.IN)))
				return false;		
		return true;
	}
	
	/**
	 * returns true if no accepted argument attacks another accepted one in
	 * this interpretation wrt. the given theory.
	 * @param dungTheory a Dung theory.
	 * @return true if no accepted argument attacks another accepted one in
	 * this interpretation wrt. the given theory.
	 */
	public boolean isConflictFree(DungTheory dungTheory){
		for(Argument a: this.getArgumentsOfStatus(ArgumentStatus.IN))
			for(Argument b: this.getArgumentsOfStatus(ArgumentStatus.IN))
				if(dungTheory.isAttackedBy(a, b))
					return false;
		return true;
	}
	
	/**
	 * returns true if every accepted argument of this is defended by some accepted
	 * argument wrt. the given Dung theory.
	 * @param dungTheory a Dung theory. 
	 * @return true if every accepted argument of this is defended by some accepted
	 * argument wrt. the given Dung theory.
	 */
	public boolean isAdmissable(DungTheory dungTheory){
		if(!this.isConflictFree(dungTheory)) return false;
		Iterator<Argument> it = this.getArgumentsOfStatus(ArgumentStatus.IN).iterator();
		while(it.hasNext()){			
			if(!this.isAcceptable(it.next(),dungTheory))
				return false;
		}
		return true;
	}
	
	/**
	 * Returns all arguments that have the given status in this interpretation.
	 * @param status the status of the arguments to be returned.
	 * @return the set of arguments with the given status.
	 */
	public abstract Extension getArgumentsOfStatus(ArgumentStatus status);
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();
}
