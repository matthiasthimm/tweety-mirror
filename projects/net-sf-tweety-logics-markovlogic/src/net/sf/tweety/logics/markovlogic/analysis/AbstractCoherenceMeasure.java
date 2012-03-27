package net.sf.tweety.logics.markovlogic.analysis;

import net.sf.tweety.Reasoner;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;
import net.sf.tweety.logics.markovlogic.MarkovLogicNetwork;

/**
 * This class represents an abstract coherence measure, i.e. a function
 * that measures the coherence of an MLN by comparing the probabilities for 
 * the MLN's formulas with the intended ones.
 * 
 * @author Matthias Thimm
 */
public abstract class AbstractCoherenceMeasure {

	/** Measures the coherence of the given MLN using the given reasoner.
	 * @param mln some MLN
	 * @param reasoner some reasoner
	 * @param signature a signature
	 * @return the coherence measure of the MLN.
	 */
	public abstract double coherence(MarkovLogicNetwork mln, Reasoner reasoner, FolSignature signature);
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();
}
