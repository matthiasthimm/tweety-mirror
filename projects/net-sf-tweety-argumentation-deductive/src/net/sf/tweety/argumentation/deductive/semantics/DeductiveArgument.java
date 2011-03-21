package net.sf.tweety.argumentation.deductive.semantics;

import java.util.Collection;

import net.sf.tweety.graphs.Node;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;

/**
 * Instances of this class represent arguments in the sense
 * of Definition 3.1 in<br/>
 * <br/>
 * Philippe Besnard and Anthony Hunter. A logic-based theory of deductive arguments.
 * In Artificial Intelligence, 128(1-2):203-235, 2001.
 * 
 * @author Matthias Thimm
 */
public class DeductiveArgument implements Node {

	/** The support of this argument. */
	private Collection<? extends PropositionalFormula> support;
	/** The claim of this argument. */
	private PropositionalFormula claim;
	
	/** 
	 * Creates a new deductive argument with the given support
	 * and claim.
	 * @param support a set of formulas.
	 * @param claim a formula.
	 */
	public DeductiveArgument(Collection<? extends PropositionalFormula> support, PropositionalFormula claim){
		this.support = support;
		this.claim = claim;
	}
	
	/**
	 * Returns the support of this argument.
	 * @return the support of this argument.
	 */
	public Collection<? extends PropositionalFormula> getSupport(){
		return this.support;
	}
	
	/**
	 * Returns the claim of this argument.
	 * @return the claim of this argument.
	 */
	public PropositionalFormula getClaim(){
		return this.claim;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "<" + this.support.toString() + "," + this.claim.toString() + ">";
	}
}
