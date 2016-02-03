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
package net.sf.tweety.arg.deductive;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.tweety.arg.deductive.accumulator.Accumulator;
import net.sf.tweety.arg.deductive.categorizer.Categorizer;
import net.sf.tweety.arg.deductive.semantics.ArgumentTree;
import net.sf.tweety.arg.deductive.semantics.DeductiveArgument;
import net.sf.tweety.commons.Answer;
import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.Formula;
import net.sf.tweety.commons.Reasoner;
import net.sf.tweety.logics.pl.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains common characteristics for deductive argumentation
 * reasoner. 
 * 
 * @author Matthias Thimm
 */
public abstract class AbstractDeductiveArgumentationReasoner extends Reasoner {

	/** Logger. */
	static private Logger log = LoggerFactory.getLogger(AbstractDeductiveArgumentationReasoner.class);
	
	/** The categorizer used for reasoning. */
	private Categorizer categorizer;
	
	/** The accumulator used for reasoning. */
	private Accumulator accumulator;	
	
	/** Creates a new  reasoner for the given belief base,
	 * categorizer, and accumulator.
	 * @param beliefBase some belief base (must be of class DeductiveKnowledgebase).
	 * @param categorizer some categorizer.
	 * @param accumulator some accumulator.
	 */
	public AbstractDeductiveArgumentationReasoner(BeliefBase beliefBase, Categorizer categorizer, Accumulator accumulator) {
		super(beliefBase);
		if(!(beliefBase instanceof DeductiveKnowledgeBase))
			throw new IllegalArgumentException("Knowledge base of class DeductiveKnowledgebase expected.");
		this.categorizer = categorizer;
		this.accumulator = accumulator;
	}

	/**
	 * Computes the argument tree of the given argument.
	 * @param arg some argument.
	 * @return the argument tree for the argument
	 */
	protected abstract ArgumentTree getArgumentTree(DeductiveArgument arg);
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.Reasoner#query(net.sf.tweety.Formula)
	 */
	@Override
	public Answer query(Formula query) {
		if(!(query instanceof PropositionalFormula))
			throw new IllegalArgumentException("Formula of class PropositionalFormula expected.");
		log.trace("Querying " + this.getKnowledgBase() + " with " + query);
		PropositionalFormula f = (PropositionalFormula) query;
		DeductiveKnowledgeBase kb = (DeductiveKnowledgeBase) this.getKnowledgBase();
		// 1.) get all arguments for the query 
		Set<DeductiveArgument> proArguments = kb.getDeductiveArguments(f);
		// 2.) get all arguments for the negation of the query
		Set<DeductiveArgument> conArguments = kb.getDeductiveArguments(new Negation(f));
		// 3.) get all argument trees for all pro arguments
		Set<ArgumentTree> proTrees = new HashSet<ArgumentTree>();
		for(DeductiveArgument arg: proArguments)
			proTrees.add(this.getArgumentTree(arg));
		// 4.) get all argument trees for all pro arguments
		Set<ArgumentTree> conTrees = new HashSet<ArgumentTree>();
		for(DeductiveArgument arg: conArguments)
			conTrees.add(this.getArgumentTree(arg));
		// 5.) categorize each pro tree
		List<Double> proCategorization = new ArrayList<Double>();
		for(ArgumentTree argTree: proTrees){
			log.trace("Argument tree for " + argTree.getRoot() + "\n" + argTree.prettyPrint());
			//System.out.println("Argument tree for " + argTree.getRoot() + "\n" + argTree.prettyPrint());
			double val = this.categorizer.categorize(argTree);
			proCategorization.add(val);
			log.trace("Categorization " + val);
			//System.out.println("Categorization " + val);
		}
		// 6.) categorize each con tree
		List<Double> conCategorization = new ArrayList<Double>();
		for(ArgumentTree argTree: conTrees){
			log.trace("Argument tree for " + argTree.getRoot() + "\n" + argTree.prettyPrint());
			//System.out.println("Argument tree for " + argTree.getRoot() + "\n" + argTree.prettyPrint());
			double val = this.categorizer.categorize(argTree);
			conCategorization.add(val);
			log.trace("Categorization " + val);
			//System.out.println("Categorization " + val);
		}
		// 7.) evaluate using the accumulator
		Double result = this.accumulator.accumulate(proCategorization, conCategorization);
		log.trace("Result for " + query + ": " + result);
		// 8.) prepare answer
		Answer answer = new Answer(kb, f);
		answer.setAnswer(result > 0);
		answer.setAnswer(result);
		return answer;
	}
}
