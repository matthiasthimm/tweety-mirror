/*
 *  This file is part of "Tweety", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  Tweety is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 *  Copyright 2016 The Tweety Project Team <http://tweetyproject.org/contact/>
 */
package net.sf.tweety.arg.dung;

import java.util.*;

import net.sf.tweety.arg.dung.semantics.*;
import net.sf.tweety.arg.dung.syntax.*;
import net.sf.tweety.commons.*;
import net.sf.tweety.logics.pl.PlBeliefSet;
import net.sf.tweety.logics.pl.syntax.Proposition;

/**
 * This reasoner for Dung theories performs inference on the complete extensions.
 * Computes the set of all complete extensions, i.e., all admissible sets that contain all their acceptable arguments.
 * Exploits the structure of the strongly-connected components (SCCs) to determine extensions.
 * @author Matthias Thimm
 *
 */
public class SccCompleteReasoner extends AbstractExtensionReasoner {

	/**
	 * Creates a new complete reasoner for the given knowledge base.
	 * @param beliefBase a knowledge base.
	 * @param inferenceType The inference type for this reasoner.
	 */
	public SccCompleteReasoner(BeliefBase beliefBase, int inferenceType){
		super(beliefBase, inferenceType);		
	}
	
	/**
	 * Creates a new complete reasoner for the given knowledge base using sceptical inference.
	 * @param beliefBase The knowledge base for this reasoner.
	 */
	public SccCompleteReasoner(BeliefBase beliefBase){
		super(beliefBase);		
	}
	
	/**
	 * Computes extensions recursively following the SCC structure.
	 * @param theory the theory
	 * @param sccs all SCCs topologically sorted
	 * @param idx the current SCC to be processed
	 * @param in all arguments currently in
	 * @param out all arguments currently out
	 * @param undec all arguments currently undecided
	 * @return the set of extensions
	 */
	private Set<Extension> computeExtensionsViaSccs(DungTheory theory, List<Collection<Argument>> sccs, int idx, Collection<Argument> in, Collection<Argument> out, Collection<Argument> undec){
		if(idx >= sccs.size()){
			Set<Extension> result = new HashSet<Extension>();
			result.add(new Extension(in));
			return result;
		}
		// construct theory
		DungTheory subTheory = (DungTheory) ((DungTheory)this.getKnowledgBase()).getRestriction(sccs.get(idx));
		// remove all out arguments
		subTheory.removeAll(out);
		// for all arguments that are attacked by an already undecided argument outside the scc, add attack
		// from an auxiliary self-attacking argument
		Argument aux = new Argument("_aux_argument8937");
		subTheory.add(aux);
		subTheory.add(new Attack(aux,aux));
		for(Argument a: subTheory)
			if(theory.isAttacked(a, new Extension(undec)))				
				subTheory.add(new Attack(aux,a));
		// compute complete extensions of sub theory
		Set<Extension> subExt = new CompleteReasoner(subTheory).computeExtensions();
		Set<Extension> result = new HashSet<Extension>();
		Collection<Argument> new_in, new_out, new_undec, attacked;
		for(Extension ext: subExt){
			new_in = new HashSet<Argument>(in);
			new_out = new HashSet<Argument>(out);
			new_undec = new HashSet<Argument>(undec);
			attacked= new HashSet<Argument>();
			new_in.addAll(ext);			
			for(Argument a: ext)
				attacked.addAll(theory.getAttacked(a));
			new_out.addAll(attacked);
			for(Argument a: subTheory)
				if(a != aux && !ext.contains(a) && !attacked.contains(a))
					new_undec.add(a);			
			result.addAll(this.computeExtensionsViaSccs(theory, sccs, idx+1, new_in, new_out, new_undec));
		}		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.argumentation.dung.AbstractExtensionReasoner#computeExtensions()
	 */
	public Set<Extension> computeExtensions(){
		DungTheory theory = (DungTheory) this.getKnowledgBase();
		List<Collection<Argument>> sccs = new ArrayList<Collection<Argument>>(theory.getStronglyConnectedComponents());		
		// order SCCs in a DAG
		boolean[][] dag = new boolean[sccs.size()][sccs.size()];
		for(int i = 0; i < sccs.size(); i++){
			dag[i] = new boolean[sccs.size()];
			Arrays.fill(dag[i], false);
		}		
		for(int i = 0; i < sccs.size(); i++)
			for(int j = 0; j < sccs.size(); j++)
				if(i != j)
					if(theory.isAttacked(new Extension(sccs.get(i)), new Extension(sccs.get(j))))
						dag[i][j] = true;						
		// order SCCs topologically
		List<Collection<Argument>> sccs_ordered = new ArrayList<Collection<Argument>>();
		while(sccs_ordered.size() < sccs.size()){
			for(int i = 0; i < sccs.size();i++){
				if(sccs_ordered.contains(sccs.get(i)))
					continue;
				boolean isNull = true;
				for(int j = 0; j < sccs.size(); j++)
					if(dag[i][j]){
						isNull = false;
						break;
					}
				if(isNull){
					sccs_ordered.add(sccs.get(i));
					for(int j = 0; j < sccs.size(); j++)
						dag[j][i] = false;
				}
			}
		}		
		return this.computeExtensionsViaSccs(theory, sccs_ordered, 0, new HashSet<Argument>(), new HashSet<Argument>(), new HashSet<Argument>());
	}
		
	/* (non-Javadoc)
	 * @see net.sf.tweety.argumentation.dung.AbstractExtensionReasoner#getPropositionalCharacterisationBySemantics(java.util.Map, java.util.Map, java.util.Map)
	 */
	@Override
	protected PlBeliefSet getPropositionalCharacterisationBySemantics(Map<Argument, Proposition> in, Map<Argument, Proposition> out,Map<Argument, Proposition> undec) {
		return new CompleteReasoner(this.getKnowledgBase()).getPropositionalCharacterisationBySemantics(in, out, undec);
	}
	
}
