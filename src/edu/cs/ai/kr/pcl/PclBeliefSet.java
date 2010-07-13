package edu.cs.ai.kr.pcl;

import java.util.*;

import edu.cs.ai.kr.*;
import edu.cs.ai.kr.pcl.syntax.*;
import edu.cs.ai.kr.pl.syntax.*;

/**
 * This class models a belief set on probabilistic conditional logic, i.e. a set of
 * probabilistic conditionals.
 * 
 * @author Matthias Thimm
 *
 */
public class PclBeliefSet extends BeliefSet<ProbabilisticConditional> {

	/**
	 * Creates a new (empty) conditional belief set.
	 */
	public PclBeliefSet(){
		super();
	}
	
	/**
	 * Creates a new conditional belief set with the given collection of
	 * conditionals.
	 * @param conditionals a collection of conditionals.
	 */
	public PclBeliefSet(Collection<? extends ProbabilisticConditional> conditionals){
		super(conditionals);
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.BeliefSet#getSignature()
	 */
	@Override
	public Signature getSignature() {
		PropositionalSignature sig = new PropositionalSignature();
		for(ProbabilisticConditional c: this)
			sig.addAll(((PropositionalSignature)c.getSignature()));			
		return sig;
	}	
}
