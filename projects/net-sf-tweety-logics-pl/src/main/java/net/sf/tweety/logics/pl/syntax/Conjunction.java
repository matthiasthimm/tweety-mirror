package net.sf.tweety.logics.pl.syntax;

import java.util.Collection;
import java.util.HashSet;

import net.sf.tweety.logics.commons.LogicalSymbols;

/**
 * This class represents a conjunction in propositional logic.
 * 
 * @author Matthias Thimm
 * @author Tim Janus
 */
public class Conjunction extends AssociativePropositionalFormula {
		
	/**
	 * Creates a new conjunction with the given inner formulas. 
	 * @param formulas a collection of formulas.
	 */
	public Conjunction(Collection<? extends PropositionalFormula> formulas){
		super(formulas);
	}
	
	/**
	 * Creates a new (empty) conjunction.
	 */
	public Conjunction(){
		this(new HashSet<PropositionalFormula>());
	}
	
	/**
	 * Creates a new conjunction with the two given formulae
	 * @param first a propositional formula.
	 * @param second a propositional formula.
	 */
	public Conjunction(PropositionalFormula first, PropositionalFormula second){
		this();
		this.add(first);
		this.add(second);
	}	

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula#collapseAssociativeFormulas()
	 */
	@Override
	public PropositionalFormula collapseAssociativeFormulas(){
		if(this.isEmpty())
			return new Tautology();
		if(this.size() == 1)
			return this.iterator().next().collapseAssociativeFormulas();
		Conjunction newMe = new Conjunction();
		for(PropositionalFormula f: this){
			PropositionalFormula newF = f.collapseAssociativeFormulas();
			if(newF instanceof Conjunction)
				newMe.addAll((Conjunction) newF);
			else newMe.add(newF);
		}
		return newMe;
	}
		
	
  /* (non-Javadoc)
   * @see net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula#toNNF()
   */
	@Override
	public PropositionalFormula toNnf() {
    Conjunction c = new Conjunction();
    for(PropositionalFormula p : this) {
      c.add( p.toNnf() );
    }
    return c;
	}

	@Override
	public Conjunction clone() {
		return new Conjunction(support.copyHelper(this));
	}


	@SuppressWarnings("unchecked")
	@Override
	public Conjunction createEmptyFormula() {
		return new Conjunction();
	}

	@Override
	public String getOperatorSymbol() {
		return LogicalSymbols.CONJUNCTION();
	}

	@Override
	public String getEmptySymbol() {
		return LogicalSymbols.CONTRADICTION();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula#toCnf()
	 */
	@Override
	public Conjunction toCnf() {
		Collection<PropositionalFormula> cnf = new HashSet<PropositionalFormula>();
		for(PropositionalFormula conjunct: this)
			for(PropositionalFormula f: conjunct.toCnf())
			cnf.add(f);		
		return new Conjunction(cnf);
	}
}
