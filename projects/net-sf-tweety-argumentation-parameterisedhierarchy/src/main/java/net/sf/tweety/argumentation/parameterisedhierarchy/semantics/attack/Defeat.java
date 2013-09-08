package net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack;

import net.sf.tweety.argumentation.parameterisedhierarchy.syntax.Argument;


/**
 * This notion of attack models the strong attack relation. 
 * A strongly attacks B iff 
 * (1) A attacks B and
 * (2) B does not undercut A
 *  
 * @author Sebastian Homann
 *
 */
public class Defeat implements AttackStrategy {

	/** Singleton instance. */
	private static Defeat instance = new Defeat();
	
	private Attack attack = Attack.getInstance();
	private Undercut undercut = Undercut.getInstance();
	
	/** Private constructor. */
	private Defeat(){};
	
	/**
	 * Returns the singleton instance of this class.
	 * @return the singleton instance of this class.
	 */
	public static Defeat getInstance(){
		return Defeat.instance;
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack.NotionOfAttack#attacks(net.sf.tweety.argumentation.parameterisedhierarchy.syntax.Argument, net.sf.tweety.argumentation.parameterisedhierarchy.syntax.Argument)
	 */
	public boolean attacks(Argument a, Argument b) {
		
		return attack.attacks(a, b) && (!undercut.attacks(b, a) );
	}

}
