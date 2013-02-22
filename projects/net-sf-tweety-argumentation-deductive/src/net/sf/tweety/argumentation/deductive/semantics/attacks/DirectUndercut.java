package net.sf.tweety.argumentation.deductive.semantics.attacks;

import net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument;
import net.sf.tweety.logics.propositionallogic.semantics.ClassicalEntailment;
import net.sf.tweety.logics.propositionallogic.syntax.Negation;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;

/**
 * This attack notion models the direct undercut relation; A is defeated by B iff there is c in support(A) with claim(B) == \neg c.
 * @author Matthias Thimm
 */
public class DirectUndercut implements Attack{

	/** Singleton instance. */
	private static DirectUndercut instance = new DirectUndercut();
	
	/** Private constructor. */
	private DirectUndercut(){};
	
	/**
	 * Returns the singleton instance of this class.
	 * @return the singleton instance of this class.
	 */
	public static DirectUndercut getInstance(){
		return DirectUndercut.instance;
	}	
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.argumentation.deductive.semantics.attacks.Attack#isAttackedBy(net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument, net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument)
	 */
	@Override
	public boolean isAttackedBy(DeductiveArgument a, DeductiveArgument b) {
		ClassicalEntailment entailment = new ClassicalEntailment();
		for(PropositionalFormula f: a.getSupport())
			if(entailment.isEquivalent(b.getClaim(), new Negation(f)))
				return true;
		return false;
	}

}
