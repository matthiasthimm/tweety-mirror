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
	 * Computes the convex combination of this P1 and the
	 * given probability distribution P2 with parameter d, i.e.
	 * it returns a P with P(i)=d P1(i) + (1-d) P2(i) for every interpretation i.
	 * @param d a double
	 * @param other a probability distribution
	 * @return the convex combination of this P1 and the
	 * 	given probability distribution P2 with parameter d.
	 * @throws IllegalArgumentException if either d is not in [0,1] or this and
	 * the given probability distribution are not defined on the same set of interpretations.
	 */
	public RpclProbabilityDistribution convexCombination(double d, RpclProbabilityDistribution other){
		if(d < 0 || d > 1)
			throw new IllegalArgumentException("The combination parameter must be between 0 and 1.");
		Set<HerbrandInterpretation> interpretations = this.keySet();
		if(!interpretations.equals(other.keySet())|| !this.getSignature().equals(other.getSignature()))
			throw new IllegalArgumentException("The distributions cannot be combined as they differ in their definitions.");			
		RpclProbabilityDistribution p = new RpclProbabilityDistribution(this.semantics, (FolSignature) this.getSignature());
		for(HerbrandInterpretation i: interpretations)
			p.put(i, this.probability(i).mult(d).add(other.probability(i).mult(1-d)));
		return p;
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
