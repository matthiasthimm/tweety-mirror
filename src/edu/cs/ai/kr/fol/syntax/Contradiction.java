package edu.cs.ai.kr.fol.syntax;

/**
 * A contradictory formula.
 * @author Matthias Thimm
 */
public class Contradiction extends SpecialFormula{
	
	/**
	 * Creates a new contradiction.
	 */
	public Contradiction() {
		
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolFormula#toString()
	 */
	@Override
	public String toString() {
		return FolSignature.CONTRADICTION;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.syntax.FolBasicStructure#hashCode()
	 */
	public int hashCode(){
		return 3;
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
