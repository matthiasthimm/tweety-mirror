package net.sf.tweety.logics.markovlogic;

import java.io.Serializable;
import java.util.Collection;

import net.sf.tweety.BeliefSet;
import net.sf.tweety.Signature;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;
import net.sf.tweety.logics.markovlogic.syntax.MlnFormula;

/**
 * Instances of this class represent Markov Logic Networks [Domingos et. al.].
 * 
 * @author Matthias Thimm
 */
public class MarkovLogicNetwork extends BeliefSet<MlnFormula> implements Serializable {

	private static final long serialVersionUID = 3313039501304912746L;

	/**
	 * Creates a new (empty) MLN.
	 */
	public MarkovLogicNetwork(){
		super();
	}
	
	/**
	 * Creates a new conditional MLN with the given collection of
	 * MLN formulas.
	 * @param formulas a collection of MLN formulas.
	 */
	public MarkovLogicNetwork(Collection<? extends MlnFormula> formulas){
		super(formulas);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.BeliefSet#getSignature()
	 */
	@Override
	public Signature getSignature() {
		FolSignature sig = new FolSignature();
		for(MlnFormula formula: this){
			sig.addAll(formula.getPredicates());
			sig.addAll(formula.getConstants());
			sig.addAll(formula.getFunctors());
		}
		return sig;
	}

}
