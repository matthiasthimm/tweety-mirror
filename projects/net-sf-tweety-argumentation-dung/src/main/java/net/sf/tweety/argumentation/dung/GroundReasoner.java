package net.sf.tweety.argumentation.dung;

import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.argumentation.dung.semantics.*;

/**
 * This reasoner for Dung theories performs inference on the grounded extension.
 * Computes the (unique) grounded extension, i.e., the least fixpoint of the characteristic function faf.
 * 
 * @author Matthias Thimm
 *
 */
public class GroundReasoner extends AbstractExtensionReasoner {

	/**
	 * Creates a new ground reasoner for the given knowledge base.
	 * @param beliefBase a knowledge base.
	 * @param inferenceType The inference type for this reasoner.
	 */
	public GroundReasoner(BeliefBase beliefBase, int inferenceType){
		super(beliefBase, inferenceType);		
	}
	
	/**
	 * Creates a new ground reasoner for the given knowledge base using sceptical inference.
	 * @param beliefBase The knowledge base for this reasoner.
	 */
	public GroundReasoner(BeliefBase beliefBase){
		super(beliefBase);		
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.argumentation.dung.AbstractExtensionReasoner#computeExtensions()
	 */
	protected Set<Extension> computeExtensions(){		
		Extension ext = new Extension();
		int size;
		do{
			size = ext.size();
			ext = ((DungTheory)this.getKnowledgBase()).faf(ext);
		}while(size!=ext.size());
		Set<Extension> extensions = new HashSet<Extension>();
		extensions.add(ext);
		return extensions;
	}
	
}
