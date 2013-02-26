package net.sf.tweety.logics.relationalprobabilisticconditionallogic.semantics;

import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.logics.firstorderlogic.semantics.*;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.probabilisticconditionallogic.semantics.*;
import net.sf.tweety.logics.relationalprobabilisticconditionallogic.*;
import net.sf.tweety.logics.relationalprobabilisticconditionallogic.syntax.*;
import net.sf.tweety.math.equation.*;
import net.sf.tweety.math.term.*;
import net.sf.tweety.math.probability.*;


/**
 * This class bundles common answering behaviour for
 * relational conditional semantics.
 * 
 * @author Matthias Thimm
 */
public abstract class AbstractRpclSemantics implements RpclSemantics {

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.relationalprobabilisticconditionallogic.semantics.RpclSemantics#satisfies(net.sf.tweety.logics.probabilisticconditionallogic.semantics.ProbabilityDistribution, net.sf.tweety.logics.relationalprobabilisticconditionallogic.syntax.RelationalProbabilisticConditional)
	 */
	public abstract boolean satisfies(ProbabilityDistribution<?> p, RelationalProbabilisticConditional r);

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.relationalprobabilisticconditionallogic.semantics.RpclSemantics#getSatisfactionStatement(net.sf.tweety.logics.relationalprobabilisticconditionallogic.syntax.RelationalProbabilisticConditional, net.sf.tweety.logics.firstorderlogic.syntax.FolSignature, java.util.Map)
	 */
	@Override
	public abstract Statement getSatisfactionStatement(RelationalProbabilisticConditional r, FolSignature signature, Map<? extends Interpretation,FloatVariable> worlds2vars);
	
	/**
	 * Checks whether the given ground conditional is satisfied by the given distribution
	 * wrt. this semantics. For every rational semantics this satisfaction relation
	 * should coincide with the propositional case.
	 * @param p a probability distribution.
	 * @param groundConditional a ground conditional
	 * @return "true" iff the given ground conditional is satisfied by the given distribution
	 * 	wrt. this semantics
	 */
	protected boolean satisfiesGroundConditional(ProbabilityDistribution<?> p, RelationalProbabilisticConditional groundConditional){
		if(!groundConditional.isGround())
			throw new IllegalArgumentException("The conditional " + groundConditional + " is not ground.");
		return p.probability(groundConditional).getValue() < groundConditional.getProbability().getValue() + Probability.PRECISION &&
			p.probability(groundConditional).getValue() > groundConditional.getProbability().getValue() - Probability.PRECISION;
	}
	
	/**
	 * Constructs the term expressing the probability of the given formula "f"
	 * wrt. to the variables (describing probabilities) of the reference worlds.
	 * @param f a fol formula
	 * @param worlds2vars a map mapping reference worlds to variables.
	 * @return the term expressing the probability of the given formula "f".
	 */
	protected Term probabilityTerm(FolFormula f, Map<? extends Interpretation,FloatVariable> worlds2vars){
		Term result = null;		
		for(Interpretation world: worlds2vars.keySet()){
			Integer multiplicator;
			if(world instanceof ReferenceWorld)
				multiplicator = ((ReferenceWorld)world).getMultiplicator(f);
			else if(world instanceof HerbrandInterpretation)
				multiplicator = (((HerbrandInterpretation)world).satisfies(f))?(1):(0);
			else throw new IllegalArgumentException("Intepretation of type reference world or Herbrand interpretation expected.");
			if(multiplicator != 0){
				Term t = new FloatConstant(multiplicator).mult(worlds2vars.get(world));
				if(result == null)
					result = t;
				else result = result.add(t);
			}				
		}			
		return (result == null)? new FloatConstant(0): result;
	}
	
}
