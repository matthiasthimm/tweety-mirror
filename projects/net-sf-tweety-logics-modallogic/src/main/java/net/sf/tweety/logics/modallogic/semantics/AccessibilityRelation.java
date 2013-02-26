package net.sf.tweety.logics.modallogic.semantics;

import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.util.*;


/**
 * This class models an accessibility relation for Kripke
 * models.
 * 
 * @author Matthias Thimm
 */
public class AccessibilityRelation {

	/**
	 * The actual relation
	 */
	private Set<Pair<Interpretation,Interpretation>> tuples;
	
	/**
	 * TODO
	 * @return
	 */
	public Set<Interpretation> getNodes(){
		Set<Interpretation> interpretations = new HashSet<Interpretation>();
		for(Pair<Interpretation,Interpretation> p: this.tuples){
			interpretations.add(p.getFirst());
			interpretations.add(p.getSecond());
		}
		return interpretations;
	}
	
	/**
	 * TODO
	 * @param i
	 * @return
	 */
	public Set<Interpretation> getSuccessors(Interpretation i){
		Set<Interpretation> successors = new HashSet<Interpretation>();
		for(Pair<Interpretation,Interpretation> relation: this.tuples)
			if(relation.getFirst().equals(i))
				successors.add(relation.getSecond());
		return successors;
	}
}
