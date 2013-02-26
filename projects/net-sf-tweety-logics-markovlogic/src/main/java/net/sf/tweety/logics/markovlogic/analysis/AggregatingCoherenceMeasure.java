package net.sf.tweety.logics.markovlogic.analysis;

import java.util.ArrayList;
import java.util.List;

import net.sf.tweety.Reasoner;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;
import net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula;
import net.sf.tweety.logics.markovlogic.MarkovLogicNetwork;
import net.sf.tweety.logics.markovlogic.syntax.MlnFormula;

/**
 * This coherence measure uses an aggregation function and a distance function
 * to measure the coherence of an MLN. For each formula in the MLN the distance
 * function looks at the probabilities of all ground instance and compares this 
 * vector to the intended probabilities. The aggregation function is used to
 * aggregate the distances for all formulas. 
 * 
 * @author Matthias Thimm
 *
 */
public class AggregatingCoherenceMeasure extends AbstractCoherenceMeasure {

	private static final long serialVersionUID = 4162719595968757160L;
	
	/** The distance function used to measure the difference of the probabilities
	 * of each ground instance for a single formula. */
	private DistanceFunction distance;
	/** The aggregation function used to aggregate the distances for each formula. */
	private AggregationFunction aggregator;
	
	public AggregatingCoherenceMeasure(DistanceFunction distance, AggregationFunction aggregator){
		this.aggregator = aggregator;
		this.distance = distance;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.markovlogic.analysis.AbstractCoherenceMeasure#coherence(net.sf.tweety.logics.markovlogic.MarkovLogicNetwork, net.sf.tweety.Reasoner, net.sf.tweety.logics.firstorderlogic.syntax.FolSignature)
	 */
	@Override
	public double coherence(MarkovLogicNetwork mln, Reasoner reasoner, FolSignature signature) {
		List<Double> distances = new ArrayList<Double>();
		for(MlnFormula f: mln){
			List<Double> intended = new ArrayList<Double>();
			List<Double> observed = new ArrayList<Double>();
			Double pObserved;
			if(f.isStrict())
				pObserved = 1d;
			else pObserved = (Math.exp(f.getWeight())/(f.getFormula().getSatisfactionRatio()+Math.exp(f.getWeight())));
			for(RelationalFormula groundFormula: f.getFormula().allGroundInstances(signature.getConstants())){
				observed.add(reasoner.query(groundFormula).getAnswerDouble());
				intended.add(pObserved);
			}			
			distances.add(this.distance.distance(intended, observed));
		}
		return 1-this.aggregator.aggregate(distances);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.markovlogic.analysis.AbstractCoherenceMeasure#toString()
	 */
	@Override
	public String toString() {		
		return "C<" + this.distance.toString() + ", " + this.aggregator.toString() + ">";
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((aggregator == null) ? 0 : aggregator.hashCode());
		result = prime * result
				+ ((distance == null) ? 0 : distance.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AggregatingCoherenceMeasure other = (AggregatingCoherenceMeasure) obj;
		if (aggregator == null) {
			if (other.aggregator != null)
				return false;
		} else if (!aggregator.equals(other.aggregator))
			return false;
		if (distance == null) {
			if (other.distance != null)
				return false;
		} else if (!distance.equals(other.distance))
			return false;
		return true;
	}

}
