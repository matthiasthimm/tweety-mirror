package net.sf.tweety.logics.markovlogic;

import java.util.Iterator;
import java.util.Set;

import net.sf.tweety.BeliefBase;
import net.sf.tweety.logics.firstorderlogic.semantics.HerbrandBase;
import net.sf.tweety.logics.firstorderlogic.semantics.HerbrandInterpretation;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;
import net.sf.tweety.util.RandomSubsetIterator;

/**
 * This MLN reasoner employs simple random sampling from
 * the set of interpretations to compute the probability of a formula.
 *  
 * @author Matthias Thimm
 *
 */
public class SimpleSamplingMlnReasoner extends AbstractMlnReasoner{

	/** The computation is aborted when the given precision is reached for at least
	 * numOfPositive number of consecutive tests. */
	private double precision = 0.00001;	
	private int numOfPositiveTests = 1000;
	
	/**
	 * Creates a new SimpleSamplingMlnReasoner for the given Markov logic network.
	 * @param beliefBase a Markov logic network. 
	 * @param name another signature (if the probability distribution should be defined 
	 * on that one (that one should subsume the signature of the Markov logic network)
	 * @param precision the precision
	 * @param numOfPositiveTests the number of positive consecutive tests on precision
	 */
	public SimpleSamplingMlnReasoner(BeliefBase beliefBase,	FolSignature signature, double precision, int numOfPositiveTests) {
		super(beliefBase, signature);
		this.precision = precision;
		this.numOfPositiveTests = numOfPositiveTests;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.markovlogic.AbstractMlnReasoner#reset()
	 */
	@Override
	public void reset() {
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.markovlogic.AbstractMlnReasoner#doQuery(net.sf.tweety.logics.firstorderlogic.syntax.FolFormula)
	 */
	@Override
	protected double doQuery(FolFormula query) {
		// The Herbrand base of the signature
		HerbrandBase hBase = new HerbrandBase(this.getSignature());
		// for sampling
		Iterator<Set<Atom>> it = new RandomSubsetIterator<Atom>(hBase.getAtoms(),false);		
		double previousProb = 0;
		double currentProb = 0;
		int consecutiveTests = 0;
		double completeMass = 0;
		long satisfiedMass = 0;
		do{
			HerbrandInterpretation sample = new HerbrandInterpretation(it.next());
			double weight = this.computeWeight(sample);
			if(sample.satisfies(query))
				satisfiedMass += weight;
			completeMass += weight;
			previousProb = currentProb;
			currentProb = (completeMass == 0) ? 0 : satisfiedMass/completeMass;			
			if(Math.abs(previousProb-currentProb) < this.precision){
				consecutiveTests++;
				if(consecutiveTests >= this.numOfPositiveTests)
					break;
			}else consecutiveTests = 0;
		}while(true);		
		return currentProb;
	}


}
