package edu.cs.ai.kr.rpcl.semantics;

import java.util.*;

import edu.cs.ai.kr.*;
import edu.cs.ai.kr.fol.semantics.HerbrandBase;
import edu.cs.ai.kr.fol.semantics.HerbrandInterpretation;
import edu.cs.ai.kr.fol.syntax.*;
import edu.cs.ai.kr.rcl.syntax.RelationalConditional;
import edu.cs.ai.kr.rpcl.*;
import edu.cs.ai.kr.rpcl.syntax.*;
import edu.cs.ai.util.*;

/**
 * Objects of this class represent probability distributions on the interpretations
 * of the underlying first-order signature for a relational probabilistic conditional knowledge base.
 * @author Matthias Thimm
 */
public class ProbabilityDistribution extends Interpretation implements Map<Interpretation,Probability> {
	
	/**
	 * The semantics used for this probability distribution.
	 */
	private RpclSemantics semantics;
	
	/**
	 * The probabilities of the Herbrand interpretations.
	 */
	private Map<Interpretation,Probability> probabilities;
	
	/**
	 * The signature this probability distribution bases on.
	 */
	private FolSignature signature;
	
	/**
	 * Creates a new probability distribution for the given signature.
	 * @param signature a fol signature.
	 */
	public ProbabilityDistribution(RpclSemantics semantics, FolSignature signature){
		this.semantics = semantics;
		this.signature = signature;
		this.probabilities = new HashMap<Interpretation,Probability>();
	}
	
	/**
	 * Returns the semantics of this distribution.
	 * @return the semantics of this distribution.
	 */
	public RpclSemantics getSemantics(){
		return this.semantics;
	}
	
	/**
	 * Returns the signature this probability distribution bases on.
	 * @return the signature this probability distribution bases on.
	 */
	public FolSignature getSignature(){
		return this.signature;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.Interpretation#satisfies(edu.cs.ai.kr.Formula)
	 */
	@Override
	public boolean satisfies(Formula formula) throws IllegalArgumentException {
		if(!(formula instanceof RelationalProbabilisticConditional))
			throw new IllegalArgumentException("Relational probabilistic conditional expected.");
		return semantics.satisfies(this, (RelationalProbabilisticConditional)formula);
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.Interpretation#satisfies(edu.cs.ai.kr.BeliefBase)
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
	 * Returns the entropy of this probability distribution.
	 * @return the entropy of this probability distribution.
	 */
	public double entropy(){
		double entropy = 0;
		for(Interpretation i : this.probabilities.keySet())
			if(this.probability(i).getValue() != 0)
				entropy -= this.probability(i).getValue() * Math.log(this.probability(i).getValue());
		return entropy;
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
	public ProbabilityDistribution convexCombination(double d, ProbabilityDistribution other){
		if(d < 0 || d > 1)
			throw new IllegalArgumentException("The combination parameter must be between 0 and 1.");
		Set<Interpretation> interpretations = this.keySet();
		if(!interpretations.equals(other.keySet()) || !this.signature.equals(other.getSignature()))
			throw new IllegalArgumentException("The distributions cannot be combined as they differ in their definitions.");			
		ProbabilityDistribution p = new ProbabilityDistribution(this.semantics,this.signature);
		for(Interpretation i: interpretations)
			p.put(i, this.probability(i).mult(d).add(other.probability(i).mult(1-d)));
		return p;
	}
	
	/**
	 * Computes the convex combination of this P1 and the
	 * given probability distributions P2,...,PN with parameters factors, i.e.
	 * it returns a P with P(i)=d1 P1(i) + d2 P2(i)+ ... + dN PN(i) for every interpretation i
	 * (with d1,...,dN=factors).
	 * @param factors a vector of doubles.
	 * @param creators a vector of probability distributions.
	 * @return the convex combination of the given distributions with parameters factors.
	 * @throws IllegalArgumentException if either the sum of factors d is not in 1, or this and
	 * the given probability distributions are not defined on the same set of interpretations, or
	 * the lengths of creators and factors differ.
	 */
	public static ProbabilityDistribution convexCombination(double[] factors, ProbabilityDistribution[] creators) throws IllegalArgumentException{
		if(factors.length != creators.length)
			throw new IllegalArgumentException("Length of factors and creators does not coincide.");
		double sum = 0;
		for(double d: factors)
			sum += d;
		if(sum < 1-Probability.PRECISION || sum > 1+Probability.PRECISION)
			throw new IllegalArgumentException("Factors do not sum up to one.");
		Set<Interpretation> interpretations = creators[0].keySet();
		FolSignature signature = creators[0].getSignature();
		for(int i = 1; i < creators.length; i++)
			if(!interpretations.equals(creators[i].keySet()) || !signature.equals(creators[i].getSignature()))
				throw new IllegalArgumentException("The distributions cannot be combined as they differ in their definitions.");
		ProbabilityDistribution p = new ProbabilityDistribution(creators[0].getSemantics(),signature);
		for(Interpretation i: interpretations){
			double prob = 0;
			for(int k =0; k < creators.length; k++)
				prob += factors[k] * creators[k].probability(i).getValue();
			p.put(i, new Probability(prob));
		}
		return p;
	}
	
	/**
	 * Makes a linear combination of this distribution "p1" and the given distribution "other" and
	 * the given parameters, i.e. it returns a P with P(i)=d1 P1(i) + d2 P2(i) for every interpretation i.
	 * NOTE: P is normalized after combination.
	 * @param d1 a double.
	 * @param d2 a double.
	 * @param other a probability distribution.
	 * @return a probability distribution.
	 */
	public ProbabilityDistribution linearCombination(double d1, double d2, ProbabilityDistribution other){
		if(!this.keySet().equals(other.keySet()) || !this.signature.equals(other.getSignature()))
			throw new IllegalArgumentException("The distributions cannot be combined as they differ in their definitions.");
		List<Interpretation> interpretations = new ArrayList<Interpretation>(this.keySet());
		List<Double> probabilities = new LinkedList<Double>();
		for(Interpretation i: interpretations)
			probabilities.add(d1 * this.get(i).getValue() + d2 * other.get(i).getValue());
		ProbabilityDistribution.normalize(probabilities);		
		ProbabilityDistribution p = new ProbabilityDistribution(this.semantics,this.signature);
		Iterator<Double> iterator = probabilities.iterator();
		for(Interpretation i: interpretations)
			p.put(i, new Probability(iterator.next()));
		return p;
	}
	
	/**
	 * Normalizes the given list of probabilities, i.e. divides
	 * each probability by the sum of all probabilities.
	 */
	protected static void normalize(List<Double> probabilities){
		double sum = 0;
		for(Double p : probabilities)
			sum += p;
		for(int i = 0; i < probabilities.size(); i++)
			probabilities.set(i, probabilities.get(i)/sum);		
	}
	
	/**
	 * Gets the probability of the given Herbrand interpretation (this is just an
	 * alias for get(Object o).
	 * @param w a Herbrand interpretation.
	 * @return the probability of the given Herbrand interpretation.
	 */
	public Probability probability(Interpretation w) throws IllegalArgumentException{
		return this.get(w);
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
	public static ProbabilityDistribution getUniformDistribution(RpclSemantics semantics, FolSignature signature){
		ProbabilityDistribution p = new ProbabilityDistribution(semantics,signature);
		Set<HerbrandInterpretation> interpretations = new HerbrandBase(signature).allHerbrandInterpretations(); 
		double size = interpretations.size();
		for(HerbrandInterpretation i: interpretations)
			p.put(i, new Probability(1/size));
		return p;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return this.probabilities.toString();
	}
	
	public String toShortString(){
		String s = "";
		for(Interpretation i: this.keySet())
			s += this.get(i).getValue() + " ";
		return s;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		this.probabilities.clear();		
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		return this.probabilities.containsKey(key);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(Object value) {
		return this.probabilities.containsValue(value);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<java.util.Map.Entry<Interpretation, Probability>> entrySet() {
		return this.probabilities.entrySet();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public Probability get(Object key) {
		return this.probabilities.get(key);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.probabilities.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Probability put(Interpretation key, Probability value) {
		return this.probabilities.put(key, value);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map<? extends Interpretation, ? extends Probability> m) {
		this.probabilities.putAll(m);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public Probability remove(Object key) {
		return this.probabilities.remove(key);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		return this.probabilities.size();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<Probability> values() {
		return this.probabilities.values();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<Interpretation> keySet() {
		return this.probabilities.keySet();
	}

}
