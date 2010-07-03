package edu.cs.ai.kr.saf.syntax;

import java.util.*;
import edu.cs.ai.kr.util.rules.*;
import edu.cs.ai.kr.pl.syntax.*;
import edu.cs.ai.kr.dung.syntax.*;

/**
 * This class models a basic argument in structured argumentation frameworks, i.e.
 * a claim (a proposition) together with a support (a set of propositions) where
 * the claim is not in the support.
 * 
 * @author Matthias Thimm
 *
 */
public class BasicArgument extends Argument implements Rule{

	/**
	 * The claim of this basic argument. 
	 */
	private Proposition claim;
	
	/**
	 * The support of this basic argument.
	 */
	private Set<Proposition> support;
	
	/**
	 * Deprecated for basic arguments.
	 * @param name
	 */
	@Deprecated
	public BasicArgument(String name){
		super(name);
		throw new IllegalArgumentException("Illegal constructor call for a basic argument");
	}
	
	/**
	 * Creates a new basic argument with the given claim
	 * and empty support.
	 * @param claim a proposition.
	 */
	public BasicArgument(Proposition claim){
		this(claim, new HashSet<Proposition>());
	}
	
	/**
	 * Creates a new basic argument with the given claim
	 * and the given support.
	 * @param claim a proposition
	 * @param support a set of propositions
	 */
	public BasicArgument(Proposition claim, Set<Proposition> support){
		super("<" + claim + "," + support + ">");
		if(support.contains(claim))
			throw new IllegalArgumentException("Claim is contained in support.");
		this.claim = claim;
		this.support = support;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.util.rules.Rule#getConclusion()
	 */
	public Proposition getConclusion(){
		return this.claim;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.util.rules.Rule#getPremise()
	 */
	public Set<Proposition> getPremise(){
		return this.support;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.dung.syntax.Argument#getSignature()
	 */
	public PropositionalSignature getSignature() {
		PropositionalSignature sig = new PropositionalSignature();
		sig.add(this.claim);
		for(Proposition p: this.support)
			sig.add(p);
		return sig;
	}

}
