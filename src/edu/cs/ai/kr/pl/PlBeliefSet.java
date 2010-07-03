package edu.cs.ai.kr.pl;

import java.util.*;
import edu.cs.ai.kr.*;
import edu.cs.ai.kr.pl.syntax.*;

/**
 * This class represents a knowledge base of propositional formulae.
 * 
 * @author Matthias Thimm
 *
 */
public class PlBeliefSet extends BeliefSet<PropositionalFormula> {

	/**
	 * Creates a new (empty) knowledge base.
	 */
	public PlBeliefSet(){
		super();
	}
	
	/**
	 * Creates a new knowledge base with the given
	 * set of formulas.
	 * @param formulas a set of formulas.
	 */
	public PlBeliefSet(Set<PropositionalFormula> formulas){
		super(formulas);
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.BeliefBase#getSignature()
	 */
	@Override
	public Signature getSignature() {
		PropositionalSignature signature = new PropositionalSignature();
		for(Formula f: this)
			signature.addAll(((PropositionalFormula)f).getPropositions());
		return signature;
	}

}
