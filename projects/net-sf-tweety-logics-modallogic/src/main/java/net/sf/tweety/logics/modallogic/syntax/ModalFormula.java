package net.sf.tweety.logics.modallogic.syntax;

import java.util.Set;

import net.sf.tweety.logics.commons.syntax.Functor;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.commons.syntax.Variable;
import net.sf.tweety.logics.commons.syntax.interfaces.Conjuctable;
import net.sf.tweety.logics.commons.syntax.interfaces.Disjunctable;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;
import net.sf.tweety.logics.firstorderlogic.syntax.Conjunction;
import net.sf.tweety.logics.firstorderlogic.syntax.Disjunction;
import net.sf.tweety.logics.firstorderlogic.syntax.FOLAtom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula;
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
	public FolSignature getSignature() {
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
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getPredicates()
	 */
	public Set<? extends Predicate> getPredicates(){
		return this.formula.getPredicates();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getFunctors()
	 */
	public Set<Functor> getFunctors(){
		return this.formula.getFunctors();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getAtoms()
	 */
	public Set<FOLAtom> getAtoms(){
		return this.formula.getAtoms();
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
		return this.getTerms(Variable.class);
	}
	
	@Override
	public boolean isWellBound(){
		return this.formula.isWellBound();
	}
	
	@Override
	public boolean isWellBound(Set<Variable> boundVariables){
		return this.formula.isWellBound(boundVariables);
	}
	
	@Override
	public Conjunction combineWithAnd(Conjuctable f){
		if(!(f instanceof ModalFormula))
			throw new IllegalArgumentException("The given formula " + f + " is not a modal formula.");
		return new Conjunction(this,(ModalFormula)f);
	}
	
	@Override
	public Disjunction combineWithOr(Disjunctable f){
		if(!(f instanceof ModalFormula))
			throw new IllegalArgumentException("The given formula " + f + " is not a modal formula.");
		return new Disjunction(this,(ModalFormula)f);
	}
	
	@Override
	public RelationalFormula complement(){		
		return new Negation(this);
	}

	@Override
	public boolean isLiteral() {
		return formula.isLiteral();
	}

	@Override
	public Set<Term<?>> getTerms() {
		return formula.getTerms();
	}

	@Override
	public <C extends Term<?>> Set<C> getTerms(Class<C> cls) {
		return formula.getTerms(cls);
	}

	@Override
	public Set<Variable> getQuantifierVariables() {
		return formula.getQuantifierVariables();
	}

	@Override
	public RelationalFormula substitute(Term<?> v, Term<?> t)
			throws IllegalArgumentException {
		return formula.substitute(v, t);
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
