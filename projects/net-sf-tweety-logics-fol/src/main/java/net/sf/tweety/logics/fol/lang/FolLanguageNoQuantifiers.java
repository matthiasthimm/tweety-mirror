package net.sf.tweety.logics.fol.lang;

import net.sf.tweety.*;
import net.sf.tweety.logics.fol.syntax.*;

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
	 * @see net.sf.tweety.logics.firstorderlogic.lang.FolLanguage#isRepresentable(net.sf.tweety.kr.Formula)
	 */
	@Override
	public boolean isRepresentable(Formula formula){
		if(!super.isRepresentable(formula)) return false;
		return !((FolFormula)formula).containsQuantifier();		
	}
}