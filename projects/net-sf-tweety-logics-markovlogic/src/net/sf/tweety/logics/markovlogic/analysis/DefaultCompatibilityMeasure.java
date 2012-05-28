package net.sf.tweety.logics.markovlogic.analysis;

import java.util.List;

import net.sf.tweety.Reasoner;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;
import net.sf.tweety.logics.markovlogic.MarkovLogicNetwork;

/**
 * This class represents the default compatibility measure that uses
 * a coherence measure.
 * 
 * @author Matthias Thimm
 */
public class DefaultCompatibilityMeasure implements CompatibilityMeasure{

	/** The coherence measure used for computing compatibility. */
	private AbstractCoherenceMeasure coherenceMeasure;
	
	/** Creates a new compatibility measure.
	 * @param coherenceMeasure the coherence measure used for computing compatibility.
	 */
	public DefaultCompatibilityMeasure(AbstractCoherenceMeasure coherenceMeasure){
		this.coherenceMeasure = coherenceMeasure;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.markovlogic.analysis.CompatibilityMeasure#compatibility(java.util.List, net.sf.tweety.Reasoner, java.util.List)
	 */
	@Override
	public double compatibility(List<MarkovLogicNetwork> mlns, Reasoner reasoner, List<FolSignature> signatures) {
		if(mlns.size() != signatures.size())
			throw new IllegalArgumentException("For each MLN there has to be exactly one signature");
		double result = 0;
		MarkovLogicNetwork mergedMLN = new MarkovLogicNetwork();
		FolSignature mergedSig = new FolSignature();
		for(int i = 0; i < mlns.size(); i++){
			result -=  this.coherenceMeasure.coherence(mlns.get(i), reasoner, signatures.get(i));
			mergedMLN.addAll(mlns.get(i));
			mergedSig.addAll(signatures.get(i).getConstants());
			mergedSig.addAll(signatures.get(i).getPredicates());
			mergedSig.addAll(signatures.get(i).getFunctors());
			mergedSig.addAll(signatures.get(i).getSorts());
		}
		result = 1 + this.coherenceMeasure.coherence(mergedMLN, reasoner, mergedSig) - (1d/mlns.size()) * result;
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "comp[" + this.coherenceMeasure.toString() + "]";
	}

}
