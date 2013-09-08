package net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack;

import net.sf.tweety.argumentation.parameterisedhierarchy.syntax.Argument;


/**
 * This notion of attack models the attack relation. 
 * A attacks B iff A undercuts or rebuts B.
 *  
 * @author Sebastian Homann
 *
 */
public class StrongAttack implements AttackStrategy {

	/** Singleton instance. */
	private static StrongAttack instance = new StrongAttack();
	
	private Attack attack = Attack.getInstance();
	private Undercut undercut = Undercut.getInstance();
	
	/** Private constructor. */
	private StrongAttack(){};
	
	/**
	 * Returns the singleton instance of this class.
	 * @return the singleton instance of this class.
	 */
	public static StrongAttack getInstance(){
		return StrongAttack.instance;
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack.NotionOfAttack#attacks(net.sf.tweety.argumentation.parameterisedhierarchy.syntax.Argument, net.sf.tweety.argumentation.parameterisedhierarchy.syntax.Argument)
	 */
	public boolean attacks(Argument a, Argument b) {
		return attack.attacks(a, b) && (! undercut.attacks(b, a) );
	}

}
