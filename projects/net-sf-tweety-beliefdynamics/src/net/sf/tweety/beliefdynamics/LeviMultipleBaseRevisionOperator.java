package net.sf.tweety.beliefdynamics;

import java.util.*;

import net.sf.tweety.*;

/**
 * This class implements the Levi identity for multiple revision, ie. an revision that is composed of the
 * contraction with the negated set of formulas and then expansion with those formulas.
 * 
 * @author Matthias Thimm
 *
 * @param <T> the type of formulas this operators works on.
 */
public class LeviMultipleBaseRevisionOperator<T extends ClassicalFormula> extends MultipleBaseRevisionOperator<T> {

	/**
	 * The contraction operator of this Levi revision.
	 */
	private BaseContractionOperator<T> contraction;
	
	/**
	 * The expansion operator of this Levi revision.
	 */
	private MultipleBaseExpansionOperator<T> expansion;
	
	/**
	 * Creates a new Levi base revision with the given contraction and expansion operators.
	 * @param contraction some contraction operator.
	 * @param expansion some expansion operator.
	 */
	public LeviMultipleBaseRevisionOperator(MultipleBaseContractionOperator<T> contraction, MultipleBaseExpansionOperator<T> expansion){
		this.contraction = contraction;
		this.expansion = expansion;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.beliefdynamics.MultipleBaseRevisionOperator#revise(java.util.Collection, java.util.Collection)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<T> revise(Collection<T> base, Collection<T> formulas) {
		if(formulas.isEmpty())
			return new HashSet<T>(base);			
		// the complement of a set of formulas is the disjunction of the negated formulas
		T formula = null;
		for(T f: formulas)
			if(formula == null)
				formula = (T) f.complement();
			else formula = (T) formula.combineWithOr(f.complement());
		return this.expansion.expand(this.contraction.contract(base, formula), formulas);
	}

}
