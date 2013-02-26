package net.sf.tweety.argumentation.dung.semantics;

import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.argumentation.dung.*;
import net.sf.tweety.argumentation.dung.syntax.*;
import net.sf.tweety.math.probability.*;


/**
 * This class models a (possible) extension of a Dung theory, i.e. a set of arguments.
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
	 * Returns all arguments that are "in" in this extension wrt.
	 * the given theory, i.e. this extension itself.
	 * @param dungTheory some Dung theory.
	 * @return this extension
	 */
	public Extension getInArguments(DungTheory dungTheory){
		if(dungTheory.containsAll(this))
			return new Extension(this);
		throw new IllegalArgumentException("The arguments of this extension are not all in the given theory.");
	}
	
	/**
	 * Returns all arguments that are "out" in this extension wrt.
	 * the given theory, i.e. all arguments in the given theory
	 * that are attacked by some argument in this extension.
	 * @param dungTheory some Dung theory.
	 * @return the set of arguments attacked by this extension.
	 */
	public Extension getOutArguments(DungTheory dungTheory){
		if(dungTheory.containsAll(this)){
			Extension ext = new Extension();
			for(Argument a: dungTheory){
				if(!this.contains(a))
					if(dungTheory.isAttacked(a, this))
						ext.add(a);
			}
			return ext;
		}
		throw new IllegalArgumentException("The arguments of this extension are not all in the given theory.");
	}
	
	/**
	 * Returns all arguments that are "undecided" in this extension wrt.
	 * the given theory, i.e. all arguments that are neither in nor out.
	 * @param dungTheory some Dung theory.
	 * @return the set of undecided arguments.
	 */
	public Extension getUndecidedArguments(DungTheory dungTheory){
		if(dungTheory.containsAll(this)){
			Extension ext = this.getInArguments(dungTheory);
			ext.addAll(this.getOutArguments(dungTheory));
			Extension result = new Extension(dungTheory);
			result.removeAll(ext);
			return result;
		}
		throw new IllegalArgumentException("The arguments of this extension are not all in the given theory.");
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
	
	/**
	 * Returns the characteristic probabilistic extension of this extension,
	 * i.e. the probabilistic extension that assigns probability 0.5
	 * to this extension and 0.5 to the union of this extension and the set
	 * of undecided arguments.
	 * @param theory some Dung theory
	 * @return the characteristic probabilistic extension of this extension.
	 */
	public ProbabilisticExtension getCharacteristicProbabilisticExtension(DungTheory theory){
		ProbabilisticExtension pe = new ProbabilisticExtension((DungSignature)theory.getSignature());
		if(this.getUndecidedArguments(theory).isEmpty()){
			pe.put(new Extension(this), new Probability(1d));
			return pe;
		}
		pe.put(new Extension(this), new Probability(0.5));
		Extension e = new Extension(this);
		e.addAll(this.getUndecidedArguments(theory));
		pe.put(e, new Probability(0.5));		
		return pe;
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
		return 1;
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
