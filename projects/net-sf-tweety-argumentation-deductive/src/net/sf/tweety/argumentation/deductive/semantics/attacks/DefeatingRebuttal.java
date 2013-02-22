package net.sf.tweety.argumentation.deductive.semantics.attacks;

import net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument;
import net.sf.tweety.logics.propositionallogic.semantics.ClassicalEntailment;
import net.sf.tweety.logics.propositionallogic.syntax.Negation;


/**
 * This attack notion models the defeating rebuttal relation; A is defeated by B iff claim(B) |- \neg claim(A).
 * @author Matthias Thimm
 */
public class DefeatingRebuttal implements Attack{

	/** Singleton instance. */
	private static DefeatingRebuttal instance = new DefeatingRebuttal();
	
	/** Private constructor. */
	private DefeatingRebuttal(){};
	
	/**
	 * Returns the singleton instance of this class.
	 * @return the singleton instance of this class.
	 */
	public static DefeatingRebuttal getInstance(){
		return DefeatingRebuttal.instance;
	}	
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.argumentation.deductive.semantics.attacks.Attack#isAttackedBy(net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument, net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument)
	 */
	@Override
	public boolean isAttackedBy(DeductiveArgument a, DeductiveArgument b) {
		ClassicalEntailment entailment = new ClassicalEntailment();
		if(entailment.entails(b.getClaim(), new Negation(a.getClaim())))
			return true;		
		return false;
	}

}
