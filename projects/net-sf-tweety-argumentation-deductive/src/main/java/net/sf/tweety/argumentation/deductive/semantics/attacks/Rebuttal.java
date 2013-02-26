package net.sf.tweety.argumentation.deductive.semantics.attacks;

import net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument;
import net.sf.tweety.logics.propositionallogic.semantics.ClassicalEntailment;
import net.sf.tweety.logics.propositionallogic.syntax.Negation;


/**
 * This attack notion models the rebuttal relation; A is defeated by B iff claim(B) == \neg claim(A).
 * @author Matthias Thimm
 */
public class Rebuttal implements Attack{

	/** Singleton instance. */
	private static Rebuttal instance = new Rebuttal();
	
	/** Private constructor. */
	private Rebuttal(){};
	
	/**
	 * Returns the singleton instance of this class.
	 * @return the singleton instance of this class.
	 */
	public static Rebuttal getInstance(){
		return Rebuttal.instance;
	}	
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.argumentation.deductive.semantics.attacks.Attack#isAttackedBy(net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument, net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument)
	 */
	@Override
	public boolean isAttackedBy(DeductiveArgument a, DeductiveArgument b) {
		ClassicalEntailment entailment = new ClassicalEntailment();
		if(entailment.isEquivalent(b.getClaim(), new Negation(a.getClaim())))
			return true;		
		return false;
	}

}
