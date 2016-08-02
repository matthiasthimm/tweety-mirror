package net.sf.tweety.arg.aspic.syntax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.commons.util.DigraphNode;
import net.sf.tweety.logics.commons.syntax.interfaces.Invertable;

/**
 * 
 * @author Nils Geilen
 *
 * An argument according to the ASPIC+ specification
 */

public class AspicArgument<T extends Invertable> extends Argument {
	
	/** The conclusion of the argument's top rule **/
	private T conc = null;;
	/** The argument's direct children, whose conclusions fit its prerequisites **/
	private Collection<AspicArgument<T>> directsubs = new ArrayList<>();
	/** The srgument's top rule **/
	private InferenceRule<T> toprule = null;
	
	
	/**
	 * Creates an empty Argument 
	 * @param toprule the argument's TopRule
	 */
	public AspicArgument(InferenceRule<T> toprule) {
		super(null);
		this.toprule = toprule;
		conc = toprule.getConclusion();	
		
		generateName();
	}
	
	/**
	 * Creates an new argument with and all of its subarguments and adds them to as
	 * @param node contains the TopRule
	 * @param as a set of AspicArguments, all subarguments will be added to this set
	 */
	public AspicArgument(DigraphNode<InferenceRule<T>> node, Collection<AspicArgument<T>> as ) {
		super(null);
		for(DigraphNode<InferenceRule<T>> parentnode : node.getParents()) {
			AspicArgument<T> subarg = new AspicArgument<T>(parentnode, as);
			directsubs.add(subarg);
			as.add(subarg);
		}
		
		toprule = node.getValue();
		conc = toprule.getConclusion();	
		
		generateName();
	}
	
	private void generateName() {
		setName(toprule + (directsubs.isEmpty()  ? "": " "+directsubs ));
	}
	
	
	
	/**
	 * @return whether this has a defeasible subrule
	 */
	public boolean isDefeasible() {
		return !getDefRules().isEmpty();
	}
	
/*	public Collection<AspicInferenceRule> getPrems() {
		Collection<AspicInferenceRule> result = new HashSet<>();
		if(toprule.isFact())
			result.add(toprule);
		for(AspicArgument arg : directsubs)
			result.addAll(arg.getPrems());
		return result;
	}*/
	
	/**
	 * @return all ordinary premises
	 */
	public Collection<AspicArgument<T>> getOrdinaryPremises() {
		Collection<AspicArgument<T>> result = new HashSet<>();
		if (toprule.isFact() && toprule.isDefeasible()) {
			result.add(this);
			return result;
		}
		for(AspicArgument<T> a: directsubs)
			result.addAll(a.getOrdinaryPremises());
		return result;
	}
	
	/**
	 * Returns Conc according to the ASPIC+ specification
	 * @return the top rule's conclusion
	 */
	public T getConc() {
		return conc;
	}
	
	/**
	 * Change the conclusion
	 * @param conc the new conclusion
	 */
	public void setConc(T conc) {
		this.conc = conc;
	}
	
	/**
	 * returns the Subs according to the ASPIC+ specification
	 * @return all subarguments including this
	 */
	public Collection<AspicArgument<T>> getAllSubs() {
		Collection<AspicArgument<T>> result = new HashSet<>();
		result.add(this);
		for(AspicArgument<T> a : directsubs)
			result.addAll(a.getAllSubs());
		return result;
	}
	
	/**
	 * @return all arguments in Subs with defeasible top rules
	 */
	public Collection<AspicArgument<T>> getDefSubs() {
		Collection<AspicArgument<T>> result = new HashSet<>();
		if(toprule.isFact())
			return result;
		if(toprule.isDefeasible())
			result.add(this);
		for(AspicArgument<T> arg : directsubs)
			result.addAll(arg.getDefSubs());
		return result;
	}
	
	/**
	 * returns the DefRules according to ASPIC+ specification
	 * @return this argument's defeasible rules
	 */
	public Collection<InferenceRule<T>> getDefRules() {
		Collection<InferenceRule<T>> result = new HashSet<>();
		for(AspicArgument<T> a : getDefSubs())
			result.add(a.toprule);
		return result;
	}
	
	/**
	 * The argument's direct children, whose conclusions fit its prerequisites
	 * @return  the direct subrules
	 */
	public Collection<AspicArgument<T>> getDirectSubs() {
		return directsubs;
	}

	/**
	 * Retruns the TopRule according to ASPIC+ specification
	 * @return the top rule
	 */
	public InferenceRule<T> getTopRule() {
		return toprule;
	}
	
	/**
	 * Changes the TopRule
	 * @param toprule the new TopRule
	 */
	public void setTopRule(InferenceRule<T> toprule) {
		this.toprule = toprule;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

	

}
