package edu.cs.ai.kr.pl.syntax;

import java.util.*;

/**
 * This class captures the common functionalities of the special
 * formulas tautology and contradiction.
 * 
 * @author Matthias Thimm
 */
public abstract class SpecialFormula extends PropositionalFormula {

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.pl.syntax.PropositionalFormula#collapseAssociativeFormulas()
	 */
	public PropositionalFormula collapseAssociativeFormulas(){
		return this;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.pl.syntax.PropositionalFormula#getPropositions()
	 */
	@Override
	public Set<Proposition> getPropositions() {
		return new HashSet<Proposition>();
	}
	
}
