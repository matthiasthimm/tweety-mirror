package net.sf.tweety.logics.relationalprobabilisticconditionallogic.semantics;

import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.logics.firstorderlogic.syntax.*;
import net.sf.tweety.logics.probabilisticconditionallogic.semantics.*;
import net.sf.tweety.logics.relationalprobabilisticconditionallogic.syntax.*;
import net.sf.tweety.math.equation.*;
import net.sf.tweety.math.term.*;


/**
 * This interface describes semantics for relational probabilistic logic.
 * 
 * @author Matthias Thimm
 */
public interface RpclSemantics {

	/**
	 * Checks whether the given probability distribution satisfies the given
	 * conditional wrt. this semantics.
	 * @param p a probability distribution
	 * @param r a relational probability conditional.
	 * @return "true" iff the given distribution satisfies the given conditional.
	 */
	public boolean satisfies(ProbabilityDistribution<?> p, RelationalProbabilisticConditional r);
	
	/**
	 * Returns the mathematical statement corresponding to the satisfaction
	 * of the given conditional wrt. this semantics and the given signature.
	 * @param r a relational probabilistic conditional
	 * @param signature a fol signature
	 * @param a map mapping the interpretations of the fol to mathematical variables.
	 * @return the mathematical statement corresponding to the satisfaction
	 * of the given conditional wrt. this semantics and the given signature.
	 */
	public Statement getSatisfactionStatement(RelationalProbabilisticConditional r, FolSignature signature, Map<? extends Interpretation,FloatVariable> worlds2vars);
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString();
}
