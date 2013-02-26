package net.sf.tweety.argumentation.dung;

import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.argumentation.dung.semantics.*;
import net.sf.tweety.argumentation.dung.syntax.*;


/**
 * This class models an abstract extension reasoner used for Dung theories.
 * @author Matthias Thimm
 */
public abstract class AbstractExtensionReasoner extends Reasoner {
	
	/**
	 * The extensions this reasoner bases upon.
	 */
	private Set<Extension> extensions = null;
	
	/**
	 * The type of inference for this reasoner, either sceptical or
	 * credulous.
	 */
	private int inferenceType;
	
	/**
	 * Creates a new reasoner for the given knowledge base.
	 * @param beliefBase The knowledge base for this reasoner.
	 * @param inferenceType The inference type for this reasoner.
	 */
	public AbstractExtensionReasoner(BeliefBase beliefBase, int inferenceType){
		super(beliefBase);
		if(!(beliefBase instanceof DungTheory))
			throw new IllegalArgumentException("Knowledge base of class DungTheory expected.");
		if(inferenceType != Semantics.CREDULOUS_INFERENCE && inferenceType != Semantics.SCEPTICAL_INFERENCE)
			throw new IllegalArgumentException("Inference type must be either sceptical or credulous.");
		this.inferenceType = inferenceType;
	}
	
	/**
	 * Creates a new reasoner for the given knowledge base using sceptical inference.
	 * @param beliefBase The knowledge base for this reasoner.
	 */
	public AbstractExtensionReasoner(BeliefBase beliefBase){
		this(beliefBase,Semantics.SCEPTICAL_INFERENCE);		
	}	
	
	/**
	 * Creates a reasoner for the given semantics.
	 * @param beliefBase some Dung theory
	 * @param semantics a semantics
	 * @param inferenceType an inference type
	 * @return a reasoner for the given Dung theory, inference type, and semantics
	 */
	public static AbstractExtensionReasoner getReasonerForSemantics(BeliefBase beliefBase, int semantics, int inferenceType){
		switch(semantics){
			case Semantics.COMPLETE_SEMANTICS: return new CompleteReasoner(beliefBase, inferenceType);
			case Semantics.GROUNDED_SEMANTICS: return new GroundReasoner(beliefBase, inferenceType);
			case Semantics.PREFERRED_SEMANTICS: return new PreferredReasoner(beliefBase, inferenceType);
			case Semantics.STABLE_SEMANTICS: return new StableReasoner(beliefBase, inferenceType);
		}
		throw new IllegalArgumentException("Unknown semantics.");
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Reasoner#query(net.sf.tweety.kr.Formula)
	 */
	public Answer query(Formula query){
		if(!(query instanceof Argument))
			throw new IllegalArgumentException("Formula of class argument expected");
		Argument arg = (Argument) query;
		if(this.inferenceType == Semantics.SCEPTICAL_INFERENCE){
			Answer answer = new Answer(this.getKnowledgBase(),arg);
			for(Extension e: this.getExtensions()){
				if(!e.contains(arg)){
					answer.setAnswer(false);
					answer.appendText("The answer is: false");
					return answer;
				}
			}			
			answer.setAnswer(true);
			answer.appendText("The answer is: true");
			return answer;
		}
		// so its credulous semantics
		Answer answer = new Answer(this.getKnowledgBase(),arg);
		for(Extension e: this.getExtensions()){
			if(e.contains(arg)){
				answer.setAnswer(true);
				answer.appendText("The answer is: true");
				return answer;
			}
		}			
		answer.setAnswer(false);
		answer.appendText("The answer is: false");
		return answer;
	}
	
	/**
	 * Returns the extensions this reasoner bases upon.
	 * @return the extensions this reasoner bases upon.
	 */
	public Set<Extension> getExtensions(){
		if(this.extensions == null)
			this.extensions = this.computeExtensions();
		return this.extensions;
	}
	
	/**
	 * Returns the inference type of this reasoner.
	 * @return the inference type of this reasoner.
	 */
	public int getInferenceType(){
		return this.inferenceType;
	}
	
	/**
	 * Computes the extensions this reasoner bases upon.
	 * @return A set of extensions.
	 */
	protected abstract Set<Extension> computeExtensions();
}
