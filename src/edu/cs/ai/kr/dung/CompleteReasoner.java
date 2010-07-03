package edu.cs.ai.kr.dung;

import java.util.*;

import edu.cs.ai.kr.*;
import edu.cs.ai.kr.dung.semantics.*;
import edu.cs.ai.kr.dung.syntax.*;

/**
 * This reasoner for Dung theories performs inference on the complete extensions.
 * Computes the set of all complete extensions, i.e., all admissable sets that contain all their acceptable arguments.
 * @author Matthias Thimm
 *
 */
public class CompleteReasoner extends AbstractExtensionReasoner {

	/**
	 * Creates a new complete reasoner for the given knowledge base.
	 * @param beliefBase a knowledge base.
	 * @param inferenceType The inference type for this reasoner.
	 */
	public CompleteReasoner(BeliefBase beliefBase, int inferenceType){
		super(beliefBase, inferenceType);		
	}
	
	/**
	 * Creates a new complete reasoner for the given knowledge base using sceptical inference.
	 * @param beliefBase The knowledge base for this reasoner.
	 */
	public CompleteReasoner(BeliefBase beliefBase){
		super(beliefBase);		
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.dung.AbstractExtensionReasoner#computeExtensions()
	 */
	public Set<Extension> computeExtensions(){
		Extension groundedExtension = new GroundReasoner(this.getKnowledgBase(),this.getInferenceType()).getExtensions().iterator().next();
		return this.getCompleteExtensions(groundedExtension);
	}

	/**
	 * Auxiliary method to compute all complete extensions
	 * @param arguments a set of arguments
	 * @return all complete extensions that are supersets of an argument in <source>arguments</source>
	 */
	private Set<Extension> getCompleteExtensions(Extension ext){
		Set<Extension> extensions = new HashSet<Extension>();
		DungTheory dungTheory = (DungTheory) this.getKnowledgBase();
		if(ext.isConflictFree(dungTheory) && dungTheory.faf(ext).equals(ext)){
			extensions.add(ext);
		}
		Extension ext2 = new Extension();
		for(Formula f: dungTheory)
			ext2.add((Argument) f);
		ext2.removeAll(ext);
		Iterator<Argument> it = ext2.iterator();
		while(it.hasNext()){
			Argument argument = it.next();
			Extension ext3 = new Extension(ext);
			ext3.add(argument);
			extensions.addAll(this.getCompleteExtensions(ext3));
		}
		return extensions;
	}
	
}
