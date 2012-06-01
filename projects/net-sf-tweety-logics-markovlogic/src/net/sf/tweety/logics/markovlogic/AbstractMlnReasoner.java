package net.sf.tweety.logics.markovlogic;

import net.sf.tweety.Answer;
import net.sf.tweety.BeliefBase;
import net.sf.tweety.Formula;
import net.sf.tweety.Reasoner;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;

/**
 * This class provides common methods for MLN reasoner.
 * 
 * @author Matthias Thimm
 */
public abstract class AbstractMlnReasoner extends Reasoner {

	/**
	 * The signature of the reasoner.
	 */
	private FolSignature signature = null;
	
	/**
	 * Creates a new reasoner for the given Markov logic network.
	 * @param beliefBase a Markov logic network. 
	 */
	public AbstractMlnReasoner(BeliefBase beliefBase){
		this(beliefBase, (FolSignature) beliefBase.getSignature());
	}
	
	/**
	 * Creates a new reasoner for the given Markov logic network.
	 * @param beliefBase a Markov logic network. 
	 * @param name another signature (if the probability distribution should be defined 
	 * on that one (that one should subsume the signature of the Markov logic network)
	 */
	public AbstractMlnReasoner(BeliefBase beliefBase, FolSignature signature){
		super(beliefBase);		
		if(!(beliefBase instanceof MarkovLogicNetwork))
			throw new IllegalArgumentException("Knowledge base of class MarkovLogicNetwork expected.");
		if(!beliefBase.getSignature().isSubSignature(signature))
			throw new IllegalArgumentException("Given signature is not a super-signature of the belief base's signature.");
		this.signature = signature;
	}
	
	/** Returns the signature used for reasoning.
	 * @return the signature used for reasoning.
	 */
	protected FolSignature getSignature(){
		return this.signature;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.Reasoner#query(net.sf.tweety.Formula)
	 */
	@Override
	public Answer query(Formula query) {
		if(!(query instanceof FolFormula) && !( ((FolFormula)query).isGround() ))
			throw new IllegalArgumentException("Reasoning in Markov logic with naive MLN reasoner is only defined for ground FOL formulas.");		
		return this.doQuery((FolFormula)query);
	}
	
	/** Performs the actual querying. 
	 * @param query a fol formula guaranteed to be ground.
	 * @return the answer of the query.
	 */
	protected abstract Answer doQuery(FolFormula query);
}
