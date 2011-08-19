package net.sf.tweety.beliefdynamics.mas;

import java.util.*;

import net.sf.tweety.agents.*;
import net.sf.tweety.argumentation.deductive.categorizer.*;
import net.sf.tweety.argumentation.deductive.semantics.*;
import net.sf.tweety.graphs.orders.*;
import net.sf.tweety.logics.propositionallogic.syntax.*;

/**
 * This class implements a credibility-based categorizer that works like
 * the classical categorizer but dismisses arguments where
 * the least credible agent which uttered a formula in that argument
 * is not as least as credible as the least credible agent which uttered
 * a formula of the parent argument.
 *   
 * @author Matthias Thimm
 */
public class CredibilityCategorizer implements Categorizer {

	/**
	 *  The credibility order used to guide the categorizing.
	 */
	private Order<Agent> credOrder;
	
	/**
	 * The information objects that hold the information which agents
	 * uttered the formulas.
	 */
	private Collection<InformationObject<PropositionalFormula>> formulas;
	
	/**
	 * Creates a new credibility categorizer that is guided by the giving information which
	 * agents uttered the formulas and the credibility order. 
	 * @param formulas The information objects that hold the information which agents
	 * 		uttered the formulas.
	 * @param credOrder The credibility order used to guide the categorizing.
	 */
	public CredibilityCategorizer(Collection<InformationObject<PropositionalFormula>> formulas, Order<Agent> credOrder){
		this.formulas = formulas;
		this.credOrder = credOrder;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.argumentation.deductive.categorizer.Categorizer#categorize(net.sf.tweety.argumentation.deductive.semantics.ArgumentTree)
	 */
	@Override
	public double categorize(ArgumentTree argumentTree) {
		return this.categorize(argumentTree, null, argumentTree.getRoot());
	}
	
	/**
	 * Performs the bottom-up search.
	 * @param argumentTree some argument tree.
	 * @param parent the parent of the current node.
	 * @param node the current node.
	 * @return "1" if node is undefeated, "0" otherwise.
	 */
	private double categorize(ArgumentTree argumentTree, DeductiveArgument parent, DeductiveArgument node){
		Set<DeductiveArgument> children = argumentTree.getNeighbors(node);
		if(parent != null) children.remove(parent);
		if(children.isEmpty())
			return 1;
		for(DeductiveArgument child: children){
			if(this.isAtLeastAsPreferredAs(child, node))
				if(this.categorize(argumentTree, node, child) != 0)
					return 0;
		}
		return 1;
	}

	/**
	 * Checks whether arg1 is at least as preferred as arg2 wrt. the credibility order.
	 * @param arg1 some argument
	 * @param arg2 some argument
	 * @return "true" iff arg1 is at least as preferred as arg2 wrt. the credibility order. 
	 */
	private boolean isAtLeastAsPreferredAs(DeductiveArgument arg1, DeductiveArgument arg2){
		for(PropositionalFormula f: arg1.getSupport()){
			if(!this.isAtLeastAsPreferredAs(f, arg2.getSupport()))
				return false;
		}		
		return true;
	}
	
	/**
	 * Checks whether f is at least as preferred as some formula in "formulas"
	 * @param f some formula
	 * @param formulas a set of formulas
	 * @return "true" iff f is at least as preferred as each formula in "formulas"
	 */
	private boolean isAtLeastAsPreferredAs(PropositionalFormula f, Collection<? extends PropositionalFormula> formulas){
		for(PropositionalFormula f2: formulas){
			if(this.isAtLeastAsPreferredAs(f, f2))
				return true;
		}		
		return false;
	}
	
	/**
	 * Checks whether f is at least as preferred as f2
	 * @param f some formula
	 * @param f2 some formula
	 * @return "true" iff f is at least as preferred as f2
	 */
	private boolean isAtLeastAsPreferredAs(PropositionalFormula f, PropositionalFormula f2){
		// Retrieve all agents that uttered f
		Set<Agent> agents1 = new HashSet<Agent>();
		for(InformationObject<PropositionalFormula> i: this.formulas)
			if(i.getFormula().equals(f))
				agents1.add(i.getSource());
		// Retrieve all agents that uttered f2
		Set<Agent> agents2 = new HashSet<Agent>();
		for(InformationObject<PropositionalFormula> i: this.formulas)
			if(i.getFormula().equals(f2))
				agents2.add(i.getSource());
		// f is at least as preferred as f2 if there is one agent in agents1 such that no agent in
		// agents2 is more credible than that one.
		for(Agent a: agents1){
			boolean mostCredible = true;
			for(Agent b: agents2){
				if(this.credOrder.isOrderedBefore(b, a)){
					mostCredible = false;
					break;
				}
			}
			if(mostCredible)
				return true;
		}		
		return false;
	}
}
