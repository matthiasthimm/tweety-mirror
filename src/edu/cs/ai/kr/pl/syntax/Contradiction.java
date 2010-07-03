package edu.cs.ai.kr.pl.syntax;

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
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return PropositionalSignature.CONTRADICTION;
	}
}
