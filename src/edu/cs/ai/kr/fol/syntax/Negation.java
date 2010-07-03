package edu.cs.ai.kr.fol.syntax;

import java.util.Set;

/**
 * The classical negation of first-order logic.
 * @author Matthias Thimm
 */
public class Negation extends FolFormula{
	
	private FolFormula folFormula;
	
	public Negation(RelationalFormula formula){
		if(!(formula instanceof FolFormula))
			throw new IllegalArgumentException("Formula must be first-order formula.");
		if(!formula.isWellFormed())
			throw new IllegalArgumentException("FolFormula not well-formed.");		
		this.folFormula = (FolFormula)formula;		
	}
	
	public FolFormula getFormula(){
		return this.folFormula;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#getConstants()
	 */
	public Set<Constant> getConstants(){
		return this.folFormula.getConstants();
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#getPredicates()
	 */
	public Set<Predicate> getPredicates(){
		return this.folFormula.getPredicates();
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#getFunctors()
	 */
	public Set<Functor> getFunctors(){
		return this.folFormula.getFunctors();
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#getVariables()
	 */
	public Set<Variable> getVariables(){
		return this.folFormula.getVariables();
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#getAtoms()
	 */
	public Set<Atom> getAtoms(){
		return this.folFormula.getAtoms();
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.LogicStructure#getFunctionalTerms()
	 */
	public Set<FunctionalTerm> getFunctionalTerms(){
		return this.folFormula.getFunctionalTerms();
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#containsQuantifier()
	 */
	public boolean containsQuantifier(){
		return this.folFormula.containsQuantifier();
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#isClosed()
	 */
	public boolean isClosed(){
		return this.folFormula.isClosed();
	}
		
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.RelationalFormula#substitute(edu.cs.ai.kr.fol.syntax.Term, edu.cs.ai.kr.fol.syntax.Term)
	 */
	public RelationalFormula substitute(Term v, Term t) throws IllegalArgumentException{
		return new Negation(this.folFormula.substitute(v, t));
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#isClosed(java.util.Set)
	 */
	public boolean isClosed(Set<Variable> boundVariables){
		return this.folFormula.isClosed(boundVariables);
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#getUnboundVariables()
	 */
	public Set<Variable> getUnboundVariables(){
		return this.getVariables();
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#isWellBound()
	 */
	public boolean isWellBound(){
		return this.folFormula.isWellBound();
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#isWellBound(java.util.Set)
	 */
	public boolean isWellBound(Set<Variable> boundVariables){
		return this.folFormula.isWellBound(boundVariables);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return FolSignature.CLASSICAL_NEGATION + this.folFormula;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((folFormula == null) ? 0 : folFormula.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Negation other = (Negation) obj;
		if (folFormula == null) {
			if (other.folFormula != null)
				return false;
		} else if (!folFormula.equals(other.folFormula))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#getConjunctions()
	 */
	public Set<Conjunction> getConjunctions() {
		return this.folFormula.getConjunctions();
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#getDisjunctions()
	 */
	public Set<Disjunction> getDisjunctions() {
		return this.folFormula.getDisjunctions();
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#getQuantifiedFormulas()
	 */
	public Set<QuantifiedFormula> getQuantifiedFormulas() {
		return this.folFormula.getQuantifiedFormulas();
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#isDnf()
	 */
	public boolean isDnf() {
		return (this.folFormula instanceof Atom);
	}
}
