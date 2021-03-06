package net.sf.tweety.arg.aba;

import java.util.Collection;

import net.sf.tweety.arg.aba.syntax.Assumption;
import net.sf.tweety.arg.dung.semantics.Semantics;
import net.sf.tweety.commons.Answer;
import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.Formula;
import net.sf.tweety.commons.Reasoner;

/**
 * @author Nils Geilen <geilenn@uni-koblenz.de>
 * This is an abstract gerneralization over non-flat ABA reasoners
 * @param <T>	the language of the underlying ABA theory
 */
public abstract class GeneralABAReasoner <T extends Formula> extends Reasoner {
	private int inferenceType;


	/**
	 * Creates a new general reasoner for the given knowledge base.
	 * @param beliefBase a knowledge base.
	 * @param inferenceType The inference type for this reasoner.
	 */
	public GeneralABAReasoner(BeliefBase beliefBase, int inferenceType) {
		super(beliefBase);
		this.inferenceType = inferenceType;
	}
	
	


	/**
	 * @return the inferenceType
	 */
	public int getInferenceType() {
		return inferenceType;
	}




	/**
	 * @param inferenceType the inferenceType to set
	 */
	public void setInferenceType(int inferenceType) {
		this.inferenceType = inferenceType;
	}




	public abstract Collection<Collection<Assumption<T>>> computeExtensions();


	/* (non-Javadoc)
	 * @see net.sf.tweety.commons.Reasoner#query(net.sf.tweety.commons.Formula)
	 */
	@Override
	public Answer query(Formula query) {
		Answer result = new Answer(getKnowledgeBase(),   query);
		Collection<Collection<Assumption<T>>> exts = computeExtensions();
		if(inferenceType == Semantics.CREDULOUS_INFERENCE) {
			result.setAnswer(false);
			for(Collection<Assumption<T>> ext:exts) {
				if(ext.contains(query)) {
					result.setAnswer(true);
					break;
				}
			}
		} else {
			result.setAnswer(true);
			for(Collection<Assumption<T>> ext:exts) {
				if(!ext.contains(query)) {
					result.setAnswer(false);
					break;
				}
			}
		}
		return result;
		
	}
	
	

}
