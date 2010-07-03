package edu.cs.ai.kr.cl;

import edu.cs.ai.kr.*;
import edu.cs.ai.kr.cl.syntax.Conditional;
import edu.cs.ai.kr.fol.syntax.FolFormula;

/**
 * Implements the approach from<br>
 * 
 * James P. Delgrande. Relevance, Conditionals, and Defeasible Reasoning. In preparation.
 * 
 * @author Matthias Thimm
 *
 */
public class RelevanceReasoner extends Reasoner {

	/**
	 * The extension of the knowledgebase. Once this
	 * extension has been computed it is used for
	 * subsequent queries in order to avoid unnecessary
	 * computations.
	 */
	private ClBeliefSet extension;
	
	/**
	 * Creates a new relevance reasoner for the given knowledge base.
	 * @param beliefBase a knowledge base.	
	 */
	public RelevanceReasoner(BeliefBase beliefBase){
		super(beliefBase);		
		if(!(beliefBase instanceof ClBeliefSet))
			throw new IllegalArgumentException("Knowledge base of class ClBeliefSet expected.");
	}		
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.Reasoner#query(edu.cs.ai.kr.Formula)
	 */
	@Override
	public Answer query(Formula query) {
		if(!(query instanceof Conditional) && !(query instanceof FolFormula))
			throw new IllegalArgumentException("Reasoning in conditional logic is only defined for conditional and first-order queries.");
		if(query instanceof Conditional){
			Answer answer = new Answer(this.getKnowledgBase(),query);
			//TODO
			return answer;			
		}
		if(query instanceof FolFormula){			
			Answer answer = new Answer(this.getKnowledgBase(),query);			
			//TODO
			return answer;
		}			
		return null;
	}

	/**
	 * Returns the extended knowledge base this reasoner bases on.
	 * @return the extended knowledge base this reasoner bases on.
	 */
	public ClBeliefSet getExtension(){
		if(this.extension == null)
			this.extension = this.computeExtension();
		return this.extension;
	}
	
	/**
	 * Computes the extended knowledge base this reasoner bases on.
	 * @return the extended knowledge base this reasoner bases on.
	 */
	private ClBeliefSet computeExtension(){
		//TODO
		return null;
	}
	
}
