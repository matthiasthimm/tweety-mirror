package edu.cs.ai.kr.pl.lang;

import edu.cs.ai.kr.*;
import edu.cs.ai.kr.misc.*;
import edu.cs.ai.kr.pl.*;
import edu.cs.ai.kr.pl.syntax.*;

/**
 * This class models a propositional language for a given signature.
 * @author mthimm
 *
 */
public class PropositionalLanguage extends Language implements LogicalSymbols {

	/**
	 * Creates a new language on the given first-order signature.
	 * @param signature a first-order signature.
	 */
	public PropositionalLanguage(Signature signature){
		super(signature);
		if(!(signature instanceof PropositionalSignature))
			throw new IllegalArgumentException("Signatures for propositional languages must be propositional signatures.");
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.Language#isRepresentable(edu.cs.ai.kr.Formula)
	 */
	@Override
	public boolean isRepresentable(Formula formula) {
		return formula instanceof PropositionalFormula;
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.Language#isRepresentable(edu.cs.ai.kr.BeliefBase)
	 */
	@Override
	public boolean isRepresentable(BeliefBase beliefBase) {
		return beliefBase instanceof PlBeliefSet;
	}

}
