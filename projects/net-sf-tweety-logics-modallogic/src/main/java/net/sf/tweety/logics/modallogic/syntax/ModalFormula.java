package net.sf.tweety.logics.modallogic.syntax;

import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.logics.commons.ClassicalFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.*;
import net.sf.tweety.math.probability.Probability;

/**
 * This class models a modal formula, i.e. it encapsulates an modal operator
 * and a formula (either a modal formula or a FolFormula).
 *  
 * @author Matthias Thimm
 */
public abstract class ModalFormula extends RelationalFormula {

	/**
	 * The inner formula of this modal formula 
	 */
	private RelationalFormula formula;
	
	public ModalFormula(RelationalFormula formula){
		if(!(formula instanceof ModalFormula) && !(formula instanceof FolFormula))
			throw new IllegalArgumentException("Excpecting first-order formula or modal formula for inner formula.");
		this.formula = formula;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Formula#getSignature()
	 */
	@Override
	public Signature getSignature() {
		return this.formula.getSignature();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#getUniformProbability()
	 */
	@Override
	public Probability getUniformProbability() {
		throw new UnsupportedOperationException("IMPLEMENT ME");
	}
	
	/**
	 * Returns the inner formula of this modal formula.
	 * @return the inner formula of this modal formula.
	 */
	public RelationalFormula getFormula(){
		return this.formula;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getConstants()
	 */
	public Set<Constant> getConstants(){
		return this.formula.getConstants();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getPredicates()
	 */
	public Set<Predicate> getPredicates(){
		return this.formula.getPredicates();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getFunctors()
	 */
	public Set<Functor> getFunctors(){
		return this.formula.getFunctors();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getVariables()
	 */
	public Set<Variable> getVariables(){
		return this.formula.getVariables();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getAtoms()
	 */
	public Set<Atom> getAtoms(){
		return this.formula.getAtoms();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getFunctionalTerms()
	 */
	public Set<FunctionalTerm> getFunctionalTerms(){
		return this.formula.getFunctionalTerms();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#containsQuantifier()
	 */
	public boolean containsQuantifier(){
		return this.formula.containsQuantifier();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isClosed()
	 */
	public boolean isClosed(){
		return this.formula.isClosed();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isClosed(java.util.Set)
	 */
	public boolean isClosed(Set<Variable> boundVariables){
		return this.formula.isClosed(boundVariables);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getUnboundVariables()
	 */
	public Set<Variable> getUnboundVariables(){
		return this.getVariables();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isWellBound()
	 */
	public boolean isWellBound(){
		return this.formula.isWellBound();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isWellBound(java.util.Set)
	 */
	public boolean isWellBound(Set<Variable> boundVariables){
		return this.formula.isWellBound(boundVariables);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.ClassicalFormula#combineWithAnd(net.sf.tweety.kr.Formula)
	 */
	public RelationalFormula combineWithAnd(ClassicalFormula f){
		if(!(f instanceof ModalFormula))
			throw new IllegalArgumentException("The given formula " + f + " is not a modal formula.");
		return new Conjunction(this,(ModalFormula)f);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.ClassicalFormula#combineWithOr(net.sf.tweety.kr.ClassicalFormula)
	 */
	public RelationalFormula combineWithOr(ClassicalFormula f){
		if(!(f instanceof ModalFormula))
			throw new IllegalArgumentException("The given formula " + f + " is not a modal formula.");
		return new Disjunction(this,(ModalFormula)f);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.ClassicalFormula#complement()
	 */
	public RelationalFormula complement(){		
		return new Negation(this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((formula == null) ? 0 : formula.hashCode());
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
		ModalFormula other = (ModalFormula) obj;
		if (formula == null) {
			if (other.formula != null)
				return false;
		} else if (!formula.equals(other.formula))
			return false;
		return true;
	}
	
}
