package net.sf.tweety.arg.aspic.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import net.sf.tweety.arg.aspic.syntax.AspicArgument;
import net.sf.tweety.arg.aspic.syntax.InferenceRule;
import net.sf.tweety.logics.commons.syntax.interfaces.Invertable;


/**
 * @author Nils Geilen
 * 
 * A comparator for Aspic Arguments, that compares all deafeasible rules
 * 
 * @param <T>	is the type of the language that the ASPIC theory's rules range over 
 */
public class WeakestLinkOrder <T extends Invertable> implements Comparator<AspicArgument<T>> {
	
	/**
	 * Comparators for defeasible rules and ordinary premises
	 */
	private Comparator<Collection<InferenceRule<T>>> ruleset_comp;
	private Comparator<Collection<InferenceRule<T>>> premset_comp;

	

	/**
	 * Constructs a new weakest link ordering
	 * @param rule_comp	comparator for defeasible rules	
	 * @param prem_comp	comparator for ordinary premises
	 * @param elitist	
	 */
	public WeakestLinkOrder(Comparator<InferenceRule<T>> rule_comp, Comparator<InferenceRule<T>> prem_comp, boolean elitist) {
		ruleset_comp = new SetComparator<>(rule_comp, elitist);
		premset_comp = new SetComparator<>(prem_comp, elitist);
		
	}



	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(AspicArgument<T> a, AspicArgument<T> b) {
		List<InferenceRule<T>> a_prems = new ArrayList<>();
		for (AspicArgument<T> arg :a.getOrdinaryPremises() )
			a_prems.add(arg.getTopRule());
		List<InferenceRule<T>> b_prems = new ArrayList<>();
		for (AspicArgument<T> arg :b.getOrdinaryPremises() )
			b_prems.add(arg.getTopRule());
		if(a.isStrict()&&b.isStrict()) {
			return premset_comp.compare(a_prems, b_prems);
		}
		if(a.isFirm()&&b.isFirm())
			return ruleset_comp.compare(a.getDefeasibleRules(), b.getDefeasibleRules());
		int i= premset_comp.compare(a_prems, b_prems), j=ruleset_comp.compare(a.getDefeasibleRules(), b.getDefeasibleRules());
		if(i>0&&j>0)
			return 1;
		if(i<0&&j<0)
			return -1;
		return 0;
	}

}
