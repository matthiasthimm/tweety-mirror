package net.sf.tweety.argumentation.dung;

import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.argumentation.dung.semantics.*;
import net.sf.tweety.argumentation.dung.syntax.*;


/**
 * This reasoner for Dung theories performs inference on the preferred extensions.
 * Computes the set of all preferred extensions, i.e., all maximal admissable sets.
 * @author Matthias Thimm
 *
 */
public class PreferredReasoner extends AbstractExtensionReasoner {

	/**
	 * Creates a new preferred reasoner for the given knowledge base.
	 * @param beliefBase a knowledge base.
	 * @param inferenceType The inference type for this reasoner.
	 */
	public PreferredReasoner(BeliefBase beliefBase, int inferenceType){
		super(beliefBase, inferenceType);		
	}
	
	/**
	 * Creates a new preferred reasoner for the given knowledge base using sceptical inference.
	 * @param beliefBase The knowledge base for this reasoner.
	 */
	public PreferredReasoner(BeliefBase beliefBase){
		super(beliefBase);		
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.argumentation.dung.AbstractExtensionReasoner#computeExtensions()
	 */
	protected Set<Extension> computeExtensions(){		
		return this.getPreferredExtensions(new Extension());		
	}
	
	/**
	 * Auxiliary method to compute the set of all preferred extensions.
	 * @param arguments
	 * @return
	 */
	private Set<Extension> getPreferredExtensions(Extension ext){
		Set<Extension> extensions = new HashSet<Extension>();
		DungTheory dungTheory = (DungTheory) this.getKnowledgBase();
		Extension ext2 = new Extension();
		for(Formula f: dungTheory)
			ext2.add((Argument) f);		
		ext2.removeAll(ext);
		Iterator<Argument> it = ext2.iterator();
		boolean isMaximal = true;
		while(it.hasNext()){
			Argument argument =it.next();
			Extension ext3 = new Extension(ext);
			ext3.add(argument);
			Set<Extension> extensions2 = this.getPreferredExtensions(ext3);
			if(extensions2.size()>0){
				isMaximal = false;
				extensions.addAll(extensions2);
			}
		}
		if(isMaximal && ext.isAdmissable(dungTheory))
				extensions.add(ext);
		return extensions;
	}
	
}
