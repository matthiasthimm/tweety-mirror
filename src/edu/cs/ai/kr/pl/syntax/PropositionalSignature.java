package edu.cs.ai.kr.pl.syntax;

import java.util.*;

import edu.cs.ai.kr.*;
import edu.cs.ai.kr.misc.*;

/**
 * This class captures the signature of a specific
 * propositional language.
 * @author Matthias Thimm
 */
public class PropositionalSignature extends SetSignature<Proposition> implements LogicalSymbols {
	
	/**
	 * Creates a new propositional signature with the given set
	 * of propositions.
	 * @param propositions a set of propositions.
	 */
	public PropositionalSignature(Collection<? extends Proposition> propositions){
		super(propositions);
	}
	
	/**
	 * Creates a new (empty) propositional signature.
	 */
	public PropositionalSignature(){
		super();
	}
		
}
