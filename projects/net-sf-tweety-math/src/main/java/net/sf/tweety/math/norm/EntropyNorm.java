package net.sf.tweety.math.norm;

import java.util.Vector;

import net.sf.tweety.math.probability.ProbabilityFunction;
import net.sf.tweety.math.term.FloatConstant;
import net.sf.tweety.math.term.Fraction;
import net.sf.tweety.math.term.Logarithm;
import net.sf.tweety.math.term.Term;

/**
 * The entropy norm. Uses the entropy of a vector of doubles
 * (=probability function) as a measure of norm and the relative entropy of two 
 * probability distributions as distance.
 * Note that entropy is not a actually a norm!
 * @author Matthias Thimm
 */
public class EntropyNorm<T extends Comparable<T>> implements RealVectorNorm{

	/* (non-Javadoc)
	 * @see net.sf.tweety.math.norm.Norm#norm(java.lang.Object)
	 */
	@Override
	public double norm(Vector<Double> obj) {
		double norm = 0;
		for(double d: obj)
			if(d != 0)
				norm -= d * Math.log(d);
		return norm;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.math.norm.Norm#norm(java.lang.Object)
	 */
	public double norm(ProbabilityFunction<T> prob) {
		return this.norm(prob.getProbabilityVectorAsDoubles());
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.math.norm.Norm#distance(java.lang.Object, java.lang.Object)
	 */
	@Override
	public double distance(Vector<Double> obj1,	Vector<Double> obj2) {
		if(obj1.size() != obj2.size())
			throw new IllegalArgumentException("Dimensions of vectors do not match.");
		double distance = 0;
		for(int i=0; i< obj1.size();i++){
			distance -= obj1.get(i) * Math.log(obj1.get(i)/obj2.get(i));
		}
		return distance;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.math.norm.Norm#distance(java.lang.Object, java.lang.Object)
	 */
	public double distance(ProbabilityFunction<T> prob1, ProbabilityFunction<T> prob2) {
		return this.distance(prob1.getProbabilityVectorAsDoubles(), prob2.getProbabilityVectorAsDoubles());
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.math.norm.RealVectorNorm#normTerm(java.util.Vector)
	 */
	@Override
	public Term normTerm(Vector<Term> obj) {
		Term norm = new FloatConstant(0);
		for(Term t: obj)
			norm = norm.minus(t.mult(new Logarithm(t)));
		return norm;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.math.norm.RealVectorNorm#distanceTerm(java.util.Vector, java.util.Vector)
	 */
	@Override
	public Term distanceTerm(Vector<Term> obj1, Vector<Term> obj2) {
		if(obj1.size() != obj2.size())
			throw new IllegalArgumentException("Dimensions of vectors do not match.");
		Term distance = new FloatConstant(0);
		for(int i=0; i< obj1.size();i++)
			distance = distance.minus(obj1.get(i).mult(new Logarithm(new Fraction(obj1.get(i),obj2.get(i)))));
		return distance;
	}
	
}
