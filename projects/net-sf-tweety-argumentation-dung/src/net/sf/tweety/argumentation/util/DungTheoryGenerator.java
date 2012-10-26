package net.sf.tweety.argumentation.util;

import net.sf.tweety.argumentation.dung.DungTheory;
import net.sf.tweety.argumentation.dung.syntax.Argument;

/**
 * Class implementing this interface provide the capability
 * to generate Dung theories.
 * @author Matthias Thimm
 */
public interface DungTheoryGenerator {

	/**
	 * Generates a new Dung theory
	 * @return a Dung theory,
	 */
	public DungTheory generate();
	
	/**
	 * Generates a new Dung theory where the given argument
	 * is enforced to be in the grounded extension
	 * @param Argument arg an argument that is enforced
	 *  to be in the grounded extension of the generated theory.
	 * @return a Dung theory,
	 */
	public DungTheory generate(Argument arg);
}
