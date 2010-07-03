package edu.cs.ai.kr.fol.lang;

import edu.cs.ai.kr.*;

/**
 * This class models a first-order language without quantifiers and without functions.
 * @author Matthias Thimm
 */
public class FolLanguageNoQuantifiersNoFunctions extends FolLanguageNoQuantifiers{
	/**
	 * Creates a new language on the given signature.
	 * @param folSignature a signature.
	 */
	public FolLanguageNoQuantifiersNoFunctions(Signature signature){
		super(signature);
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.lang.FolLanguageNoQuantifiers#isRepresentable(edu.cs.ai.kr.Formula)
	 */
	public boolean isRepresentable(Formula formula){
		if(!super.isRepresentable(formula)) return false;
		return (new FolLanguageNoFunctions(this.getSignature()).isRepresentable(formula));		
	}

}
