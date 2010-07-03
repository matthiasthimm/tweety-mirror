package edu.cs.ai.kr.rpcl.semantics;

import java.util.*;

import edu.cs.ai.kr.*;
import edu.cs.ai.kr.fol.syntax.*;
import edu.cs.ai.kr.rpcl.syntax.*;
import edu.cs.ai.math.equation.*;
import edu.cs.ai.math.term.*;

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
	public boolean satisfies(ProbabilityDistribution p, RelationalProbabilisticConditional r);
	
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
}
