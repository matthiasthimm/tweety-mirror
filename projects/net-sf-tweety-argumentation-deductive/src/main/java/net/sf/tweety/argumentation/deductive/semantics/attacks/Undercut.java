package net.sf.tweety.argumentation.deductive.semantics.attacks;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument;
import net.sf.tweety.logics.propositionallogic.ClassicalEntailment;
import net.sf.tweety.logics.propositionallogic.syntax.Conjunction;
import net.sf.tweety.logics.propositionallogic.syntax.Negation;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;
import net.sf.tweety.util.SubsetIterator;

/**
 * This attack notion models the undercut relation; A is defeated by B iff there is C subset of support(A) with claim(B) == \neg C.
 * @author Matthias Thimm
 */
public class Undercut implements Attack{

	/** Singleton instance. */
	private static Undercut instance = new Undercut();
	
	/** Private constructor. */
	private Undercut(){};
	
	/**
	 * Returns the singleton instance of this class.
	 * @return the singleton instance of this class.
	 */
	public static Undercut getInstance(){
		return Undercut.instance;
	}	
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.argumentation.deductive.semantics.attacks.Attack#isAttackedBy(net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument, net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument)
	 */
	@Override
	public boolean isAttackedBy(DeductiveArgument a, DeductiveArgument b) {
		ClassicalEntailment entailment = new ClassicalEntailment();
		SubsetIterator<PropositionalFormula> it = new SubsetIterator<PropositionalFormula>(new HashSet<PropositionalFormula>(a.getSupport()));
		Set<PropositionalFormula> set = null;
		while(it.hasNext()){
			set = it.next();
			if(entailment.isEquivalent(b.getClaim(), new Negation(new Conjunction(set))))
				return true;	
		}
		return false;
	}

}
