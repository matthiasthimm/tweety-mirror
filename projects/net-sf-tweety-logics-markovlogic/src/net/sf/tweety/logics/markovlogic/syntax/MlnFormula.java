package net.sf.tweety.logics.markovlogic.syntax;

import java.util.Set;

import net.sf.tweety.ClassicalFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Constant;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.FunctionalTerm;
import net.sf.tweety.logics.firstorderlogic.syntax.Functor;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Term;
import net.sf.tweety.logics.firstorderlogic.syntax.Variable;

/**
 * Instances of this class represent first-order formulas with a weight.
 * 
 * @author Matthias Thimm
 */
public class MlnFormula extends RelationalFormula {

	/** the first-order formula. */
	private FolFormula formula;
	
	/** The weight of the formula (null means that the formula is strict). */
	private Double weight;
	
	/** Creates a new strict MLN formula with the given formula.
	 * @param formula the first-order formula.
	 */
	public MlnFormula(FolFormula formula){
		this(formula, null);
	}
	
	/** Creates a new MLN formula with the given formula and weight.
	 * @param formula the first-order formula.
	 * @param weight the weight of the formula (null means that the formula is strict).
	 */
	public MlnFormula(FolFormula formula, Double weight){
		this.formula = formula;
		this.weight = weight;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.ClassicalFormula#combineWithAnd(net.sf.tweety.ClassicalFormula)
	 */
	@Override
	public ClassicalFormula combineWithAnd(ClassicalFormula f) {
		throw new UnsupportedOperationException("Combination with AND not supported for MLN formulas.");
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.ClassicalFormula#combineWithOr(net.sf.tweety.ClassicalFormula)
	 */
	@Override
	public ClassicalFormula combineWithOr(ClassicalFormula f) {
		throw new UnsupportedOperationException("Combination with OR not supported for MLN formulas.");
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.ClassicalFormula#complement()
	 */
	@Override
	public ClassicalFormula complement() {
		throw new UnsupportedOperationException("Complementing not supported for MLN formulas.");
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#getPredicates()
	 */
	@Override
	public Set<Predicate> getPredicates() {
		return this.formula.getPredicates();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#getAtoms()
	 */
	@Override
	public Set<Atom> getAtoms() {
		return this.formula.getAtoms();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#containsQuantifier()
	 */
	@Override
	public boolean containsQuantifier() {
		return this.formula.containsQuantifier();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#substitute(net.sf.tweety.logics.firstorderlogic.syntax.Term, net.sf.tweety.logics.firstorderlogic.syntax.Term)
	 */
	@Override
	public RelationalFormula substitute(Term v, Term t)	throws IllegalArgumentException {
		return new MlnFormula((FolFormula)this.formula.substitute(v, t),this.weight);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#getUnboundVariables()
	 */
	@Override
	public Set<Variable> getUnboundVariables() {
		return this.formula.getUnboundVariables();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#isClosed()
	 */
	@Override
	public boolean isClosed() {
		return this.formula.isClosed();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#isClosed(java.util.Set)
	 */
	@Override
	public boolean isClosed(Set<Variable> boundVariables) {
		return this.formula.isClosed(boundVariables);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#isWellBound()
	 */
	@Override
	public boolean isWellBound() {
		return this.formula.isWellBound();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#isWellBound(java.util.Set)
	 */
	@Override
	public boolean isWellBound(Set<Variable> boundVariables) {
		return this.formula.isWellBound(boundVariables);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#toString()
	 */
	@Override
	public String toString() {
		return "<" + this.formula + ", " + this.weight + ">";
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getConstants()
	 */
	@Override
	public Set<Constant> getConstants() {
		return this.formula.getConstants();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getFunctors()
	 */
	@Override
	public Set<Functor> getFunctors() {
		return this.formula.getFunctors();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getVariables()
	 */
	@Override
	public Set<Variable> getVariables() {
		return this.formula.getVariables();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getFunctionalTerms()
	 */
	@Override
	public Set<FunctionalTerm> getFunctionalTerms() {
		return this.formula.getFunctionalTerms();
	}
	
	/** Returns the inner formula.
	 * @return the inner formula.
	 */
	public FolFormula getFormula(){
		return this.formula;
	}
	
	/** Returns the weight.
	 * @return the weight.
	 */
	public Double getWeight(){
		return this.weight;
	}

	/** Returns "true" iff this formula is strict.
	 * @return "true" iff this formula is strict.
	 */
	public boolean isStrict(){
		return this.weight == null;
	}
}
