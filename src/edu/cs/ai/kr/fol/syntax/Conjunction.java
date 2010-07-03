package edu.cs.ai.kr.fol.syntax;

import java.util.*;


/**
 * The classical conjunction of first-order logic.
 * @author Matthias Thimm
 */
public class Conjunction extends AssociativeFormula {

	/**
	 * Creates a new conjunction with the given inner formulas. 
	 * @param formulas a collection of formulas.
	 */
	public Conjunction(Collection<? extends RelationalFormula> formulas){
		super(formulas);
	}
	
	/**
	 * Creates a new (empty) conjunction.
	 */
	public Conjunction(){
		this(new HashSet<RelationalFormula>());
	}
	
	/**
	 * Creates a new conjunction with the two given formulae
	 * @param first a relational formula.
	 * @param second a relational formula.
	 */
	public Conjunction(RelationalFormula first, RelationalFormula second){
		this();
		this.add(first);
		this.add(second);
	}	
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#toString()
	 */
	public String toString(){
		if(this.isEmpty())
			return FolSignature.CONTRADICTION;
		String s = "";
		boolean isFirst = true;
		for(RelationalFormula f: this){
			if(isFirst)			
				isFirst = false;
			else
				s  += FolSignature.CONJUNCTION;
			// check if parentheses are needed
			if(f instanceof Disjunction && ((Disjunction)f).size()>1 )
				s += FolSignature.PARENTHESES_LEFT + f.toString() + FolSignature.PARENTHESES_RIGHT;
			else s += f.toString();
		}
		return s;
	}
		
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#getConjunctions()
	 */	
	public Set<Conjunction> getConjunctions(){
		Set<Conjunction> conjuncts = super.getConjunctions();
		conjuncts.add(this);
		return conjuncts;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#isDnf()
	 */
	public boolean isDnf(){
		return this.getDisjunctions().isEmpty() && this.getQuantifiedFormulas().isEmpty();
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.RelationalFormula#substitute(edu.cs.ai.kr.fol.syntax.Term, edu.cs.ai.kr.fol.syntax.Term)
	 */
	public FolFormula substitute(Term v, Term t) throws IllegalArgumentException{
		Set<RelationalFormula> newFormulas = new HashSet<RelationalFormula>();
		for(RelationalFormula f: this)
			newFormulas.add((FolFormula)f.substitute(v, t));
		return new Conjunction(newFormulas);
	}
	
}
