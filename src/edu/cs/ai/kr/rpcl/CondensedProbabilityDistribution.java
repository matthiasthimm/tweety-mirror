package edu.cs.ai.kr.rpcl;

import java.util.*;

import edu.cs.ai.kr.*;
import edu.cs.ai.kr.fol.syntax.*;
import edu.cs.ai.kr.rpcl.semantics.*;
import edu.cs.ai.util.*;

/**
 * Instances of this class represent condensed probability distributions, rf. [PhD thesis, Thimm].
 * 
 * @author Matthias Thimm
 *
 */
public class CondensedProbabilityDistribution extends ProbabilityDistribution {

	/**
	 * Creates a new condensed probability distribution for the given signature.
	 * @param semantics the semantics used for this distribution.
	 * @param signature a fol signature.
	 */
	public CondensedProbabilityDistribution(RpclSemantics semantics, FolSignature signature){
		super(semantics,signature);
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.rpcl.semantics.ProbabilityDistribution#entropy()
	 */
	@Override
	public double entropy(){
		double entropy = 0;
		for(Interpretation i : this.keySet())
			if(this.probability(i).getValue() != 0)
				entropy -= ((ReferenceWorld)i).spanNumber() * this.probability(i).getValue() * Math.log(this.probability(i).getValue());
		return entropy;
	}
	
	/**
	 * Returns the condensed entropy of this distribution (neglecting multiplicators of
	 * reference worlds.
	 * @return the condensed entropy of this distribution
	 */
	public double condensedEntropy(){
		return super.entropy();
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.rpcl.semantics.ProbabilityDistribution#convexCombination(double, edu.cs.ai.kr.rpcl.semantics.ProbabilityDistribution)
	 */
	@Override
	public CondensedProbabilityDistribution convexCombination(double d, ProbabilityDistribution other){
		if(!(other instanceof CondensedProbabilityDistribution))
			throw new IllegalArgumentException("The distribution '" + other + "' is not condensend.");
		ProbabilityDistribution p = super.convexCombination(d, other);
		CondensedProbabilityDistribution p2 = new CondensedProbabilityDistribution(this.getSemantics(),this.getSignature());
		p2.putAll(p);		
		return p2;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.rpcl.semantics.ProbabilityDistribution#convexCombination(double[], edu.cs.ai.kr.rpcl.CondensedProbabilityDistribution[])
	 */
	public static CondensedProbabilityDistribution convexCombination(double[] factors, CondensedProbabilityDistribution[] creators) throws IllegalArgumentException{
		ProbabilityDistribution p = ProbabilityDistribution.convexCombination(factors, creators);
		CondensedProbabilityDistribution p2 = new CondensedProbabilityDistribution(creators[0].getSemantics(),creators[0].getSignature());
		p2.putAll(p);		
		return p2;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.rpcl.semantics.ProbabilityDistribution#linearCombination(double, double, edu.cs.ai.kr.rpcl.semantics.ProbabilityDistribution)
	 */
	@Override
	public CondensedProbabilityDistribution linearCombination(double d1, double d2, ProbabilityDistribution other){
		if(!(other instanceof CondensedProbabilityDistribution))
			throw new IllegalArgumentException("The distribution '" + other + "' is not condensend.");
		List<Double> probabilities = new ArrayList<Double>();
		for(Interpretation i: this.keySet()){
			ReferenceWorld world = (ReferenceWorld) i;
			int spanNumber = world.spanNumber();
			probabilities.add(d1 * this.get(i).getValue() * spanNumber + d2 * other.get(i).getValue() * spanNumber);
		}
		ProbabilityDistribution.normalize(probabilities);		
		CondensedProbabilityDistribution p = new CondensedProbabilityDistribution(this.getSemantics(),this.getSignature());
		Iterator<Double> iterator = probabilities.iterator();
		for(Interpretation i: this.keySet()){
			ReferenceWorld world = (ReferenceWorld) i;
			int spanNumber = world.spanNumber();
			p.put(i, new Probability(iterator.next() / spanNumber));
		}			
		return p;		
	}
	
	/**
	 * Returns the uniform distribution on the given signature.
	 * @param semantics the semantics used for the distribution
	 * @param signature a fol signature
	 * @return the uniform distribution on the given signature.
	 */
	public static CondensedProbabilityDistribution getUniformDistribution(RpclSemantics semantics, FolSignature signature, Set<Set<Constant>> equivalenceClasses){
		CondensedProbabilityDistribution p = new CondensedProbabilityDistribution(semantics,signature);
		Set<ReferenceWorld> interpretations = ReferenceWorld.enumerateReferenceWorlds(signature.getPredicates(), equivalenceClasses); 
		double size = 0;
		for(ReferenceWorld i: interpretations)
			size += i.spanNumber();
		for(ReferenceWorld i: interpretations)
			p.put(i, new Probability(i.spanNumber()*(1/size)));
		return p;
	}
	
	/**
	 * Returns a random distribution on the given signature.
	 * @param semantics the semantics used for the distribution
	 * @param signature a fol signature
	 * @return a random distribution on the given signature.
	 */
	public static CondensedProbabilityDistribution getRandomDistribution(RpclSemantics semantics, FolSignature signature, Set<Set<Constant>> equivalenceClasses){
		CondensedProbabilityDistribution p = new CondensedProbabilityDistribution(semantics,signature);
		Set<ReferenceWorld> interpretations = ReferenceWorld.enumerateReferenceWorlds(signature.getPredicates(), equivalenceClasses); 
		Random rand = new Random();
		List<Double> probs = new ArrayList<Double>();
		for(int i = 0; i < interpretations.size(); i++)
			probs.add(rand.nextDouble());
		ProbabilityDistribution.normalize(probs);
		Iterator<Double> itProbs = probs.iterator();
		for(ReferenceWorld i: interpretations)
			p.put(i, new Probability(itProbs.next()/i.spanNumber()));
		return p;
	}
	
	/**
	 * Converts this condensed probability distribution into an ordinary
	 * probability distribution.
	 * @return a probability distribution.
	 */
	public ProbabilityDistribution toProbabilityDistribution(){
		//TODO implement me
		return null;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.rpcl.semantics.ProbabilityDistribution#put(edu.cs.ai.kr.Interpretation, edu.cs.ai.util.Probability)
	 */
	@Override
	public Probability put(Interpretation key, Probability value) {
		if(!(key instanceof ReferenceWorld))
			throw new IllegalArgumentException("Condensed probability distribution operate on reference worlds only.");
		return super.put(key, value);
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.rpcl.semantics.ProbabilityDistribution#probability(edu.cs.ai.kr.fol.syntax.FolFormula)
	 */
	@Override
	public Probability probability(FolFormula f){
		Probability p = new Probability(0d);
		for(Interpretation w: this.keySet()){
			ReferenceWorld rw = (ReferenceWorld) w;
			p = p.add(this.probability(rw).mult(rw.getMultiplicator(f)));
		}
		return p;
	}
}
