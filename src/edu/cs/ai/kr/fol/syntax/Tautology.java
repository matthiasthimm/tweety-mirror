package edu.cs.ai.kr.fol.syntax;

/**
 * A tautological formula.
 * @author Matthias Thimm
 */
public class Tautology extends SpecialFormula {

	/**
	 * Creates a new tautology.
	 */
	public Tautology() {		
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#toString()
	 */
	@Override
	public String toString() {
		return FolSignature.TAUTOLOGY;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolBasicStructure#hashCode()
	 */
	public int hashCode(){
		return 5;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolBasicStructure#equals(java.lang.Object)
	 */
	public boolean equals(Object obj){
		if (this == obj)
			return true;		
		if (getClass() != obj.getClass())
			return false;		
		return true;
	}
}
