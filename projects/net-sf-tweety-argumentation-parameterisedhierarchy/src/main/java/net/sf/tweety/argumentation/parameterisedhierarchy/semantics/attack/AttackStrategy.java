package net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack;

import net.sf.tweety.argumentation.parameterisedhierarchy.syntax.Argument;


/**
 * This interface is the common interface for notions of attack between two arguments.
 * (analogous to Attack-interface in deductive package)
 * @author Sebastian Homann
 */
public interface AttackStrategy {

	/**
	 * Returns "true" iff the first argument attacks the second argument.
	 * @param a some argument
	 * @param b some argument
	 * @return "true" iff <code>a</code> attacks <code>b</code>.
	 */
	public boolean attacks(Argument a, Argument b);
}
