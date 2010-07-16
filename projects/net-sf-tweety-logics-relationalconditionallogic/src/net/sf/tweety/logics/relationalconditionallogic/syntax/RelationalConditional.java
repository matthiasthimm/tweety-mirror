package net.sf.tweety.logics.relationalconditionallogic.syntax;

import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.logics.firstorderlogic.lang.*;
import net.sf.tweety.logics.firstorderlogic.syntax.*;
import net.sf.tweety.util.rules.*;


/**
 * Instances of this class represent relational conditionals.
 * <br>
 * Premise and conclusion of this conditional must confirm to a fol language without
 * quantifiers and without functions (@see net.sf.tweety.logics.firstorderlogic.lang.FolLanguageNoQuantifiersNoFunctions)
 * 
 * @author Matthias Thimm
 */
public class RelationalConditional extends RelationalFormula implements Rule{

	/**
	 * The premise of the conditional.
	 */
	private FolFormula premise;
	
	/**
	 * The conclusion of the conditional.
	 */
	private FolFormula conclusion;
	
	/**
	 * Creates a new conditional with the given premise and conclusion.
	 * @param premise a fol formula.
	 * @param conclusion a fol formula.
	 */
	public RelationalConditional(FolFormula premise, FolFormula conclusion){
		Signature sig = premise.getSignature();
		if(!(new FolLanguageNoQuantifiersNoFunctions(sig)).isRepresentable(premise)) 
			throw new IllegalArgumentException("Premise contains either function symbols or quantification.");
		sig = conclusion.getSignature();
		if(!(new FolLanguageNoQuantifiersNoFunctions(sig)).isRepresentable(conclusion)) 
			throw new IllegalArgumentException("Conclusion contains either function symbols or quantification.");
		this.premise = premise;
		this.conclusion = conclusion;		
	}
	
	/**
	 * Creates a new conditional with the given conclusion and
	 * a tautological premise.
	 * @param conclusion a fol formula.
	 */
	public RelationalConditional(FolFormula conclusion){
		this(new Tautology(),conclusion);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#containsQuantifier()
	 */
	@Override
	public boolean containsQuantifier() {
		return this.premise.containsQuantifier() || this.conclusion.containsQuantifier();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#getAtoms()
	 */
	@Override
	public Set<Atom> getAtoms() {
		Set<Atom> result = new HashSet<Atom>();
		result.addAll(this.premise.getAtoms());
		result.addAll(this.conclusion.getAtoms());
		return result;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#getPredicates()
	 */
	@Override
	public Set<Predicate> getPredicates() {
		Set<Predicate> result = new HashSet<Predicate>();
		result.addAll(this.premise.getPredicates());
		result.addAll(this.conclusion.getPredicates());
		return result;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#getUnboundVariables()
	 */
	@Override
	public Set<Variable> getUnboundVariables() {
		Set<Variable> result = new HashSet<Variable>();
		result.addAll(this.premise.getUnboundVariables());
		result.addAll(this.conclusion.getUnboundVariables());
		return result;
	}
	
	/**
	 * Checks whether this conditional is a fact, i.e.
	 * has a tautological premise.
	 * @return "true" iff this conditional is a fact.
	 */
	public boolean isFact(){
		return (this.premise instanceof Tautology);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#isClosed()
	 */
	@Override
	public boolean isClosed() {
		return this.premise.isClosed() && this.conclusion.isClosed();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#isClosed(java.util.Set)
	 */
	@Override
	public boolean isClosed(Set<Variable> boundVariables) {
		return this.premise.isClosed(boundVariables) && this.conclusion.isClosed(boundVariables);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#isWellBound()
	 */
	@Override
	public boolean isWellBound() {
		return this.premise.isWellBound() && this.conclusion.isWellBound();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#isWellBound(java.util.Set)
	 */
	@Override
	public boolean isWellBound(Set<Variable> boundVariables) {
		return this.premise.isWellBound(boundVariables) && this.conclusion.isWellBound(boundVariables);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#substitute(net.sf.tweety.logics.firstorderlogic.syntax.Term, net.sf.tweety.logics.firstorderlogic.syntax.Term)
	 */
	@Override
	public RelationalFormula substitute(Term v, Term t)	throws IllegalArgumentException {
		return new RelationalConditional(
				(FolFormula)this.premise.substitute(v, t),
				(FolFormula)this.conclusion.substitute(v, t));
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#toString()
	 */
	@Override
	public String toString() {
		return "(" + this.conclusion + "|" + this.premise + ")";
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getConstants()
	 */
	@Override
	public Set<Constant> getConstants() {
		Set<Constant> result = new HashSet<Constant>();
		result.addAll(this.premise.getConstants());
		result.addAll(this.conclusion.getConstants());
		return result;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getFunctionalTerms()
	 */
	@Override
	public Set<FunctionalTerm> getFunctionalTerms() {
		Set<FunctionalTerm> result = new HashSet<FunctionalTerm>();
		result.addAll(this.premise.getFunctionalTerms());
		result.addAll(this.conclusion.getFunctionalTerms());
		return result;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getFunctors()
	 */
	@Override
	public Set<Functor> getFunctors() {
		Set<Functor> result = new HashSet<Functor>();
		result.addAll(this.premise.getFunctors());
		result.addAll(this.conclusion.getFunctors());
		return result;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getVariables()
	 */
	@Override
	public Set<Variable> getVariables() {
		Set<Variable> result = new HashSet<Variable>();
		result.addAll(this.premise.getVariables());
		result.addAll(this.conclusion.getVariables());
		return result;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.util.rules.Rule#getConclusion()
	 */
	@Override
	public FolFormula getConclusion() {
		return this.conclusion;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.util.rules.Rule#getPremise()
	 */
	@Override
	public Collection<? extends FolFormula> getPremise() {
		Collection<FolFormula> result = new HashSet<FolFormula>();
		result.add(this.premise);
		return result;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.ClassicalFormula#combineWithAnd(net.sf.tweety.kr.ClassicalFormula)
	 */
	@Override
	public ClassicalFormula combineWithAnd(ClassicalFormula f) {
		throw new UnsupportedOperationException("Conditionals cannot be combined by 'AND'");
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.ClassicalFormula#combineWithOr(net.sf.tweety.kr.ClassicalFormula)
	 */
	@Override
	public ClassicalFormula combineWithOr(ClassicalFormula f) {
		throw new UnsupportedOperationException("Conditionals cannot be combined by 'OR'");
	}

	@Override
	public ClassicalFormula complement() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((conclusion == null) ? 0 : conclusion.hashCode());
		result = prime * result + ((premise == null) ? 0 : premise.hashCode());
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
		RelationalConditional other = (RelationalConditional) obj;
		if (conclusion == null) {
			if (other.conclusion != null)
				return false;
		} else if (!conclusion.equals(other.conclusion))
			return false;
		if (premise == null) {
			if (other.premise != null)
				return false;
		} else if (!premise.equals(other.premise))
			return false;
		return true;
	}

}
