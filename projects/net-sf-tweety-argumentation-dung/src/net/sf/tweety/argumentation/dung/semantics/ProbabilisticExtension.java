package net.sf.tweety.argumentation.dung.semantics;

import net.sf.tweety.BeliefBase;
import net.sf.tweety.Formula;
import net.sf.tweety.argumentation.dung.DungTheory;
import net.sf.tweety.argumentation.dung.syntax.Argument;
import net.sf.tweety.argumentation.dung.syntax.DungSignature;
import net.sf.tweety.logics.conditionallogic.syntax.Conditional;
import net.sf.tweety.logics.probabilisticconditionallogic.semantics.ProbabilityDistribution;
import net.sf.tweety.math.probability.*;


/**
 * This class implements a probabilistic interpretation for Dung argumentation frameworks, cf. [Thimm:2012].
 * 
 * @author Matthias Thimm
 */
public class ProbabilisticExtension extends ProbabilityDistribution<Extension> {

	/**
	 * Creates a new probabilistic extension for the given theory.
	 * @param theory some Dung theory.
	 */
	public ProbabilisticExtension(DungSignature signature){
		super(signature);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.probabilisticconditionallogic.semantics.ProbabilityDistribution#probability(net.sf.tweety.logics.conditionallogic.syntax.Conditional)
	 */
	@Override
	public Probability probability(Conditional c){
		throw new UnsupportedOperationException("Probability of conditionals not defined for probabilistic extensions.");
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.probabilisticconditionallogic.semantics.ProbabilityDistribution#satisfies(net.sf.tweety.Formula)
	 */
	@Override
	public boolean satisfies(Formula formula) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Satisfaction of formulas not defined for probabilistic extensions.");
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.Interpretation#satisfies(net.sf.tweety.BeliefBase)
	 */
	@Override
	public boolean satisfies(BeliefBase beliefBase)	throws IllegalArgumentException {
		throw new UnsupportedOperationException("Satisfaction of belief bases not defined for probabilistic extensions, use \"isPJustifiable\" and \"isPAdmissable\" instead.");		
	}
	
	/**
	 * Checks whether this probabilistic extension is p-justifiable, i.e.
	 * whether P(B)<= 1-P(A) for every A attacking B and P(B)=1 for every
	 * unattacked B.
	 * @param theory some Dung theory.
	 * @return "true" iff theory is p-justifiable.
	 */
	public boolean isPJustifiable(DungTheory theory){
		for(Argument argument: theory){
			for(Argument attacker: theory.getAttackers(argument))
				if(this.probability(argument).doubleValue() > 1 - this.probability(attacker).doubleValue())
					return false;
			if(theory.getAttackers(argument).isEmpty())
				if(this.probability(argument).doubleValue() != 1)
					return false;
		}		
		return true;
	}
	
	/** Checks whether this probabilistic extension is p-admissable, i.e.
	 * whether P(B)<= 1-P(A1),...,P(B)<= 1-P(An) and P(B)>=1-P(A1)-...-P(An) for
	 * A1,...An attacking B.
	 * @param theory some Dung theory.
	 * @return "true" iff theory is p-admissable.
	 */
	public boolean isPAdmissable(DungTheory theory){
		for(Argument argument: theory){
			double allProbs = 0;
			for(Argument attacker: theory.getAttackers(argument)){
				if(this.probability(argument).doubleValue() > 1 - this.probability(attacker).doubleValue()){
					System.out.println(argument + " " + attacker);
					return false;
				}
				allProbs += this.probability(attacker).doubleValue();
			}	
			if(this.probability(argument).doubleValue() < 1 - allProbs){
				System.out.println("B");
				return false;
			}
		}		
		return true;	
	}
	
	/** Returns the upper cut of this probabilistic extension wrt. delta, i.e.
	 * all arguments that have probability >= delta.
	 * @param a Dung theory.
	 * @param delta some threshold.
	 * @return the set of arguments that have probability >= delta.
	 */
	public Extension getUpperCut(DungTheory theory, double delta){
		Extension e = new Extension();
		for(Argument arg: theory)
			if(this.probability(arg).doubleValue() >= delta)
				e.add(arg);
		return e;
	}
	
	/** Returns the lower cut of this probabilistic extension wrt. delta, i.e.
	 * all arguments that have probability <= delta.
	 * @param a Dung theory.
	 * @param delta some threshold.
	 * @return the set of arguments that have probability <= delta.
	 */
	public Extension geLowerCut(DungTheory theory, double delta){
		Extension e = new Extension();
		for(Argument arg: theory)
			if(this.probability(arg).doubleValue() <= delta)
				e.add(arg);
		return e;
	}
}
