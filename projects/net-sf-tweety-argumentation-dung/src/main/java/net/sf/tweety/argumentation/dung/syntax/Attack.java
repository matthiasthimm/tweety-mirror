package net.sf.tweety.argumentation.dung.syntax;

import java.util.*;

import net.sf.tweety.*;

/**
 * This class models an attack between two arguments. It comprises of two attributes of <source>Argument</source> and is mainly used by
 * abstract argumentation theories as e.g. <source>DungTheory</source>.
 *
 * @author Matthias Thimm
 *
 */
public class Attack implements Formula {
	/**
	 * The two arguments that stand in the attack-relation
	 */
	private Argument attacker;
	private Argument attacked;

	/**
	 * Default constructor; initializes the two arguments used in this attack relation
	 * @param attacker the attacking argument
	 * @param attacked the attacked argument
	 */
	public Attack(Argument attacker, Argument attacked){
		this.attacker = attacker;
		this.attacked = attacked;
	}

	/**
	 * returns true if one arguments in <source>arguments</source> attacks another within this attack relation.
	 * @param arguments a list of arguments
	 * @return returns true if one arguments in <source>arguments</source> attacks another.
	 */
	public boolean isConflictFree(Collection<? extends Argument> arguments){
		Iterator<? extends Argument> it = arguments.iterator();
		while(it.hasNext()){
			Argument arg = (Argument) it.next();
			if(arg.equals(attacker)){
				Iterator<? extends Argument> it2 = arguments.iterator();
				while(it2.hasNext()){
					Argument arg2 = (Argument) it2.next();
					if(arg2.equals(attacked))
						return false;
				}
			}
		}
		return true;
	}

	/**
	 * returns the attacked argument of this attack relation.
	 * @return the attacked argument of this attack relation.
	 */
	public Argument getAttacked() {
		return attacked;
	}

	/**
	 * sets the attacked argument of this attack relation.
	 * @param attacked the attacked argument of this attack relation.
	 */
	public void setAttacked(Argument attacked) {
		this.attacked = attacked;
	}

	/**
	 * returns the attacking argument of this attack relation.
	 * @return the attacking argument of this attack relation.
	 */
	public Argument getAttacker() {
		return attacker;
	}

	/**
	 * sets the attacking argument of this attack relation.
	 * @param attacker the attacking argument of this attack relation.
	 */
	public void setAttacker(Argument attacker) {
		this.attacker = attacker;
	}
	
	/**
	 * Return true if the given argument is in this attack relation.
	 * @param argument some argument
	 * @return true if the given argument is in this attack relation.
	 */
	public boolean contains(Argument argument){
		return this.attacked.equals(argument) || this.attacker.equals(argument);
	}

	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Formula#getSignature()
	 */
	public Signature getSignature(){
		DungSignature sig = new DungSignature();
		sig.add(attacked);
		sig.add(attacker);
		return sig;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "("+attacker.toString()+","+attacked.toString()+")";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o){
		if(!o.getClass().equals(this.getClass())) return false;
		if(!attacker.equals(((Attack)o).getAttacker())) return false;
		if(!attacked.equals(((Attack)o).getAttacked())) return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode(){
		return this.attacked.hashCode() + 7 * this.attacker.hashCode();
	}

}
