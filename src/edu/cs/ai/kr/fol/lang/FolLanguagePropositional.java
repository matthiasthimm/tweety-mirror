package edu.cs.ai.kr.fol.lang;

import edu.cs.ai.kr.*;
import edu.cs.ai.kr.fol.syntax.*;

/**
 * This class represents a classical propositional language, i.e. a language without
 * variables, constants, functors, and predicates of arity greater zero.
 * @author Matthias Thimm
 */
public class FolLanguagePropositional extends FolLanguage {
	/**
	 * Creates a new language on the given signature.
	 * @param folSignature a signature.
	 */
	public FolLanguagePropositional(Signature signature){
		super(signature);
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.lang.FolLanguage#isRepresentable(edu.cs.ai.kr.Formula)
	 */
	public boolean isRepresentable(Formula formula){
		if(!super.isRepresentable(formula)) return false;
		// it is sufficient to check whether there are predicates of arity greater zero.
		for(Predicate p: ((FolFormula)formula).getPredicates())
			if(p.getArity() != 0)
				return false;
		return true;
	}
}
