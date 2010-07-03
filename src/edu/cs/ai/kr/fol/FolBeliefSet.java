package edu.cs.ai.kr.fol;

import java.util.*;

import edu.cs.ai.kr.*;
import edu.cs.ai.kr.fol.syntax.*;

/**
 * This class models a first-order knowledge base, i.e. a set of formulas
 * in first-order logic.
 * @author Matthias Thimm
 *
 */
public class FolBeliefSet extends BeliefSet<FolFormula>{
	
	/**
	 * Creates a new and empty first-order knowledge base.
	 */
	public FolBeliefSet(){
		super();
	}
	
	/**
	 * Creates a new first-order knowledge base with the given set of formulas.
	 * @param formulas
	 */
	public FolBeliefSet(Set<FolFormula> formulas){
		super(formulas);
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.BeliefBase#getSignature()
	 */
	public Signature getSignature(){
		FolSignature sig = new FolSignature();
		sig.addAll(this);
		return sig;
	}
}
