package edu.cs.ai.kr.fol.syntax;

import java.util.Iterator;
import java.util.Set;

public class ExistsQuantifiedFormula extends QuantifiedFormula{
	
	/**
	 * Creates a new for-all-quantified formula with the given formula and variables.
	 * @param folFormula the formula this for-all-quantified formula ranges over.
	 * @param variables the variables of this for-all-quantified formula.
	 */
	public ExistsQuantifiedFormula(RelationalFormula folFormula, Set<Variable> variables){
		super(folFormula,variables);		
	}
	
	/**
	 * Creates a new for-all-quantified formula with the given formula and variable.
	 * @param folFormula the formula this for-all-quantified formula ranges over.
	 * @param variables the variable of this for-all-quantified formula.
	 */
	public ExistsQuantifiedFormula(FolFormula folFormula, Variable variable){
		super(folFormula,variable);
	}	
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.RelationalFormula#substitute(edu.cs.ai.kr.fol.syntax.Term, edu.cs.ai.kr.fol.syntax.Term)
	 */
	public RelationalFormula substitute(Term v, Term t) throws IllegalArgumentException{
		if(this.getQuantifierVariables().contains(v))
			return new ExistsQuantifiedFormula(this.getFormula(),this.getQuantifierVariables());
		return new ExistsQuantifiedFormula(this.getFormula().substitute(v, t),this.getQuantifierVariables());
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#toString()
	 */
	public String toString(){
		String s = FolSignature.EXISTSQUANTIFIER + " ";
		Iterator<Variable> it = this.getQuantifierVariables().iterator();
		if(it.hasNext())
			s += it.next();
		while(it.hasNext())
			s += "," + it.next();
		s += ":" + this.getFormula();
		return s;
	}
}
