package edu.cs.ai.kr.ml.syntax;

import edu.cs.ai.kr.fol.syntax.RelationalFormula;
import edu.cs.ai.kr.fol.syntax.Term;

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
	 * @see edu.cs.ai.kr.fol.syntax.RelationalFormula#substitute(edu.cs.ai.kr.fol.syntax.Term, edu.cs.ai.kr.fol.syntax.Term)
	 */
	public RelationalFormula substitute(Term v, Term t) throws IllegalArgumentException{
		return new Possibility(this.getFormula().substitute(v, t));
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.RelationalFormula#toString()
	 */
	public String toString(){
		return "<>("+this.getFormula()+")";
	}
}
