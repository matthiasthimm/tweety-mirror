package net.sf.tweety.logicprogramming.asplibrary.syntax;

import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * this class extends an ordinary atom to explicitly
 * represent comparisons used in logical rules.
 *  
 * @author Tim Janus
 * @author Thomas Vengels
 *
 */
public class Relation extends ELPAtom {
	
	public Relation(String op, Term<?> lefthand, Term<?> righthand) {
		super(op,lefthand,righthand);
	}
	
	public Term<?> getLefthand() {
		return this.arguments.get(0);
	}
	
	public Term<?> getRighthand() {
		return this.arguments.get(1);
	}
	
	public String getOperator() {
		return this.getName();
	}
	
	@Override
	public String toString() {
		String ret = this.arguments.get(0) + " " + this.getName() + this.arguments.get(1);
		return ret;
	}
}
