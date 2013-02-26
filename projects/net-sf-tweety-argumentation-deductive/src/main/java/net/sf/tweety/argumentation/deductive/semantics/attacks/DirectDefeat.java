package net.sf.tweety.argumentation.deductive.semantics.attacks;

import net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument;
import net.sf.tweety.logics.propositionallogic.semantics.ClassicalEntailment;
import net.sf.tweety.logics.propositionallogic.syntax.Negation;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;

/**
 * This attack notion models the direct defeat relation; A is defeated by B iff there is c in support(A) with claim(B) |- \neg c.
 * @author Matthias Thimm
 */
public class DirectDefeat implements Attack{

	/** Singleton instance. */
	private static DirectDefeat instance = new DirectDefeat();
	
	/** Private constructor. */
	private DirectDefeat(){};
	
	/**
	 * Returns the singleton instance of this class.
	 * @return the singleton instance of this class.
	 */
	public static DirectDefeat getInstance(){
		return DirectDefeat.instance;
	}	
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.argumentation.deductive.semantics.attacks.Attack#isAttackedBy(net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument, net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument)
	 */
	@Override
	public boolean isAttackedBy(DeductiveArgument a, DeductiveArgument b) {
		ClassicalEntailment entailment = new ClassicalEntailment();
		for(PropositionalFormula f: a.getSupport())
			if(entailment.entails(b.getClaim(), new Negation(f)))
				return true;
		return false;
	}

}
