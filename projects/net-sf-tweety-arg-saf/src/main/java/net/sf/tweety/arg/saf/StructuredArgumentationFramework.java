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
package net.sf.tweety.arg.saf;

import java.util.*;

import net.sf.tweety.arg.dung.*;
import net.sf.tweety.arg.dung.syntax.*;
import net.sf.tweety.arg.saf.syntax.*;
import net.sf.tweety.commons.*;
import net.sf.tweety.commons.util.rules.*;
import net.sf.tweety.logics.pl.syntax.*;

/**
 * This class represents a structured argumentation framework, i.e. a set of 
 * basic arguments and an attack relation.
 * 
 * @author Matthias Thimm
 */
public class StructuredArgumentationFramework extends DungTheory{
	
	/**
	 * Creates a new empty structured argumentation framework.
	 */
	public StructuredArgumentationFramework(){
		super();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.argumentation.dung.DungTheory#getSignature()
	 */
	@Override
	public PropositionalSignature getSignature() {
		PropositionalSignature sig = new PropositionalSignature();
		for(Formula a: this){
			if(!(a instanceof BasicArgument))
				throw new IllegalArgumentException("Unexpected type of class.");
			sig.addAll(((BasicArgument)a).getSignature());
		}
		return sig;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.argumentation.dung.DungTheory#isAttackedBy(net.sf.tweety.argumentation.dung.syntax.Argument, net.sf.tweety.argumentation.dung.syntax.Argument)
	 */
	@Override
	public boolean isAttackedBy(Argument arg1, Argument arg2){
		if(arg1 instanceof ArgumentStructure && arg2 instanceof ArgumentStructure)
			return ((ArgumentStructure)arg2).attacks((ArgumentStructure) arg1, this);
		return super.isAttackedBy(arg1, arg2);		
	}
	
	/**
	 * Constructs a (pure) Dung theory from this structured argumentation framework.
	 * The set of arguments of this theory comprises of all possible argument structures of this framework,
	 * and the attack relation is determined by their attack relation.
	 * @return a Dung theory.
	 */
	public DungTheory toDungTheory(){
		DungTheory dungTheory = new DungTheory();
		// generate arguments
		Set<BasicArgument> basicArguments = new HashSet<BasicArgument>();
		for(Formula a: this)
			if(!(a instanceof BasicArgument))
				throw new IllegalArgumentException("Unexpected type of class.");
			else basicArguments.add((BasicArgument) a);
		Set<Derivation<BasicArgument>> derivations = Derivation.allDerivations(basicArguments);
		for(Derivation<BasicArgument> derivation: derivations){
			ArgumentStructure argStructure = new ArgumentStructure(derivation);
			if(argStructure.isValid(this))
				dungTheory.add(argStructure);
		}
		// generate attacks
		for(Formula a: dungTheory){
			ArgumentStructure arg1 = (ArgumentStructure) a;
			for(Formula b: dungTheory){
				ArgumentStructure arg2 = (ArgumentStructure) b;
				if(this.isAttackedBy(arg1, arg2))
					dungTheory.add(new Attack(arg2, arg1));					
			}
		}		
		return dungTheory;
	}

}
