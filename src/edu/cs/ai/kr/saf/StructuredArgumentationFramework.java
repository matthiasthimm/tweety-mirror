package edu.cs.ai.kr.saf;

import java.util.*;
import edu.cs.ai.kr.util.rules.*;
import edu.cs.ai.kr.dung.*;
import edu.cs.ai.kr.dung.syntax.*;
import edu.cs.ai.kr.*;
import edu.cs.ai.kr.pl.syntax.*;
import edu.cs.ai.kr.saf.syntax.*;
import edu.cs.ai.agents.*;

/**
 * This class represents a structured argumentation framework, i.e. a set of 
 * basic arguments and an attack relation.
 * <br>
 * This class implements "Perceivable" because in structured argumentation systems
 * a SAF by be perceived by agents.
 * 
 * @author Matthias Thimm
 */
public class StructuredArgumentationFramework extends DungTheory implements Perceivable {
	
	/**
	 * Creates a new empty structured argumentation framework.
	 */
	public StructuredArgumentationFramework(){
		super();
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.dung.DungTheory#getSignature()
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
	 * @see edu.cs.ai.kr.dung.DungTheory#isAttackedBy(edu.cs.ai.kr.dung.syntax.Argument, edu.cs.ai.kr.dung.syntax.Argument)
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
