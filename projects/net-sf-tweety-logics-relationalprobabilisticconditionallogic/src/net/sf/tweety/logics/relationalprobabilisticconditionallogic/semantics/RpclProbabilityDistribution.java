package net.sf.tweety.logics.relationalprobabilisticconditionallogic.semantics;

import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.logics.firstorderlogic.semantics.*;
import net.sf.tweety.logics.firstorderlogic.syntax.*;
import net.sf.tweety.logics.relationalconditionallogic.syntax.*;
import net.sf.tweety.logics.relationalprobabilisticconditionallogic.*;
import net.sf.tweety.logics.relationalprobabilisticconditionallogic.syntax.*;
import net.sf.tweety.logics.probabilisticconditionallogic.semantics.*;
import net.sf.tweety.util.*;


/**
 * Objects of this class represent probability distributions on the interpretations
 * of an underlying first-order signature for a relational probabilistic conditional knowledge base.
 * @author Matthias Thimm
 */
public class RpclProbabilityDistribution extends ProbabilityDistribution<HerbrandInterpretation> {
	
	/**
	 * The semantics used for this probability distribution.
	 */
	private RpclSemantics semantics;
	
	/**
	 * Creates a new probability distribution for the given signature.
	 * @param signature a fol signature.
	 */
	public RpclProbabilityDistribution(RpclSemantics semantics, FolSignature signature){
		super(signature);
		this.semantics = semantics;		
	}
	
	/**
	 * Returns the semantics of this distribution.
	 * @return the semantics of this distribution.
	 */
	public RpclSemantics getSemantics(){
		return this.semantics;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Interpretation#satisfies(net.sf.tweety.kr.Formula)
	 */
	@Override
	public boolean satisfies(Formula formula) throws IllegalArgumentException {
		if(!(formula instanceof RelationalProbabilisticConditional))
			throw new IllegalArgumentException("Relational probabilistic conditional expected.");
		return semantics.satisfies(this, (RelationalProbabilisticConditional)formula);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Interpretation#satisfies(net.sf.tweety.kr.BeliefBase)
	 */
	@Override
	public boolean satisfies(BeliefBase beliefBase)	throws IllegalArgumentException {
		if(!(beliefBase instanceof RpclBeliefSet))
			throw new IllegalArgumentException("Relational probabilistic conditional knowledge base expected.");
		RpclBeliefSet kb = (RpclBeliefSet) beliefBase;
		for(Formula f: kb)
			if(!this.satisfies(f)) return false;
		return true;
	}
	
	/**
	 * Gets the probability of the given closed formula, i.e. the sum of the
	 * probabilities of all interpretations satisfying it.
	 * @param f a closed fol formula.
	 * @return a probability.
	 */
	public Probability probability(FolFormula f){
		if(!f.isClosed()) throw new IllegalArgumentException("Formula '" + f + "' is not closed.");
		Probability result = new Probability(0d);
		for(Interpretation i: this.keySet())
			if(i.satisfies(f))
				result = result.add(this.probability(i));
		return result;
	}
	
	/**
	 * Gets the probability of the given closed relational conditional "re", i.e.
	 * the probability of the head of "re" given its body.
	 * @param re a closed relational conditional.
	 * @return a probability.
	 */
	public Probability probability(RelationalConditional re){
		if(!re.isClosed()) throw new IllegalArgumentException("Conditional '" + re + "' is not closed.");
		FolFormula head = (FolFormula)re.getConclusion();
		if(re.isFact())
			return this.probability(head);
		FolFormula body = (FolFormula)re.getPremise().iterator().next();		
		return this.probability(head.combineWithAnd(body)).divide(this.probability(body));
	}
	
	/**
	 * Returns the uniform distribution on the given signature.
	 * @param semantics the semantics for the distribution.
	 * @param signature a fol signature
	 * @return the uniform distribution on the given signature.
	 */
	public static RpclProbabilityDistribution getUniformDistribution(RpclSemantics semantics, FolSignature signature){
		RpclProbabilityDistribution p = new RpclProbabilityDistribution(semantics,signature);
		Set<HerbrandInterpretation> interpretations = new HerbrandBase(signature).allHerbrandInterpretations(); 
		double size = interpretations.size();
		for(HerbrandInterpretation i: interpretations)
			p.put(i, new Probability(1/size));
		return p;
	}

}
