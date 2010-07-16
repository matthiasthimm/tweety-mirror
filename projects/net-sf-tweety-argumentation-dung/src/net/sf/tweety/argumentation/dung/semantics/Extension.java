package net.sf.tweety.argumentation.dung.semantics;

import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.argumentation.dung.*;
import net.sf.tweety.argumentation.dung.syntax.*;


/**
 * This class models a (possible) extension of a Dung theory, i.e. a set of arguments.
 * <br>
 * This class implements "Executable" because in structured argumentation systems
 * an extension (ie a set of arguments) is a valid action for agents.
 *  
 * @author Matthias Thimm
 *
 */
public class Extension extends InterpretationSet<Argument> {
	
	/**
	 * Creates a new empty extension.
	 */
	public Extension(){
		this(new HashSet<Argument>());
	}

	/**
	 * Creates a new extension with the given set of arguments.
	 * @param arguments a set of arguments
	 */
	public Extension(Collection<? extends Argument> arguments){
		super(arguments);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Interpretation#satisfies(net.sf.tweety.kr.Formula)
	 */
	@Override
	public boolean satisfies(Formula formula) throws IllegalArgumentException {
		if(!(formula instanceof Argument)) throw new IllegalArgumentException("Argument expected.");
		return this.contains(formula);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Interpretation#satisfies(net.sf.tweety.kr.BeliefBase)
	 */
	@Override
	public boolean satisfies(BeliefBase beliefBase) throws IllegalArgumentException{
		throw new IllegalArgumentException("Satisfaction of belief bases by extensions is undefined.");
	}
		
	/**
	 * returns true if every element of this is defended by some element in this wrt. the
	 * given Dung theory.
	 * @param dungTheory a Dung theory. 
	 * @return true if every element of this is defended by some element in this wrt. the
	 * given Dung theory.
	 */
	public boolean isAdmissable(DungTheory dungTheory){
		if(!isConflictFree(dungTheory)) return false;
		Iterator<Argument> it = this.iterator();
		while(it.hasNext()){			
			if(!isAcceptable(it.next(),dungTheory))
				return false;
		}
		return true;
	}
	
	/**
	 * returns true if no argument attacks another one in <source>arguments</source>.
	 * @param arguments a set of arguments
	 * @return true if no argument attacks another one in <source>arguments</source>.
	 */
	public boolean isConflictFree(DungTheory dungTheory){
		Iterator<Attack> it = dungTheory.getAttacks().iterator();
		while (it.hasNext()){
			Attack attack = (Attack) it.next();
			if(!attack.isConflictFree(this))
				return false;
		}
		return true;
	}	

	/**
	 * returns true if every attacker on <source>argument</source> is attacked by some element from <source>this</source>.
	 * @param argument an argument
	 * @param dungTheory a Dung theory (the knowledge base)
	 * @return true if every attacker on <source>argument</source> is attacked by some element from <source>this</source>.
	 */
	public boolean isAcceptable(Argument argument, DungTheory dungTheory){
		Set<Argument> attackers = dungTheory.getAttackers(argument);
		Iterator<Argument> it = attackers.iterator();
		while (it.hasNext())			
			if(!dungTheory.isAttacked(it.next(),this))
				return false;		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String s = "{";
		boolean first = true;
		for(Argument a: this)
			if(first){
				s += a;
				first = false;
			}else s += "," + a;		
		return s+="}";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return super.hashCode() * 3;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj))
			return false;
		return this.getClass() == obj.getClass();		
	}

}
