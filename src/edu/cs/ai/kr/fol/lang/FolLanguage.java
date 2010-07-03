package edu.cs.ai.kr.fol.lang;

import edu.cs.ai.kr.*;
import edu.cs.ai.kr.fol.*;
import edu.cs.ai.kr.fol.syntax.*;

/**
 * This class models a first-order language for a given signature.
 * @author Matthias Thimm
 */
public class FolLanguage extends Language {
	
	/**
	 * Creates a new language on the given first-order signature.
	 * @param signature a first-order signature.
	 */
	public FolLanguage(Signature signature){
		super(signature);
		if(!(signature instanceof FolSignature))
			throw new IllegalArgumentException("Signatures for first-order languages must be first-order signatures.");
	}
		
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.Language#isRepresentable(edu.cs.ai.kr.Formula)
	 */
	public boolean isRepresentable(Formula formula){
		if(!(formula instanceof FolFormula)) return false;
		FolSignature folSignature = (FolSignature) this.getSignature();
		FolFormula folFormula = (FolFormula) formula;
		//NOTE: for a full first-order language its just necessary
		// for a formula to be representable by the folSignature and to be well-formed.
		return folSignature.isRepresentable(folFormula) && folFormula.isWellFormed();
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.Language#isRepresentable(edu.cs.ai.kr.BeliefBase)
	 */
	public boolean isRepresentable(BeliefBase beliefBase){
		if(!(beliefBase instanceof FolBeliefSet)) return false;
		for(Formula f : (FolBeliefSet)beliefBase)
			if(!this.isRepresentable(f))
				return false;
		return true;
	}
	

}
