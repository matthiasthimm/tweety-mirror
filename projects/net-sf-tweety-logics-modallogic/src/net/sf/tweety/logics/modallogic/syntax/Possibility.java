package net.sf.tweety.logics.modallogic.syntax;

import net.sf.tweety.logics.firstorderlogic.syntax.*;

/**
 * This class models the possibility modality.
 * @author Matthias Thimm
 */
public class Possibility extends ModalFormula {

	/**
	 * Creates a new possibility formula with the
	 * given inner formula
	 * @param formula a formula, either a modal formula or a first-order formula.
	 */
	public Possibility(RelationalFormula formula){
		super(formula);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#substitute(net.sf.tweety.logics.firstorderlogic.syntax.Term, net.sf.tweety.logics.firstorderlogic.syntax.Term)
	 */
	public RelationalFormula substitute(Term v, Term t) throws IllegalArgumentException{
		return new Possibility(this.getFormula().substitute(v, t));
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#toString()
	 */
	public String toString(){
		return "<>("+this.getFormula()+")";
	}
}
