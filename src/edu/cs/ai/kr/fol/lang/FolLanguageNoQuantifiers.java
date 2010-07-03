package edu.cs.ai.kr.fol.lang;

import edu.cs.ai.kr.*;
import edu.cs.ai.kr.fol.syntax.*;

/**
 * This class models a first-order language without quantifiers.
 * @author Matthias Thimm
 */
public class FolLanguageNoQuantifiers extends FolLanguage{
	
	/**
	 * Creates a new language on the given signature.
	 * @param folSignature a signature.
	 */
	public FolLanguageNoQuantifiers(Signature signature){
		super(signature);
	}
		
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.lang.FolLanguage#isRepresentable(edu.cs.ai.kr.Formula)
	 */
	public boolean isRepresentable(Formula formula){
		if(!super.isRepresentable(formula)) return false;
		return !((FolFormula)formula).containsQuantifier();		
	}
}
