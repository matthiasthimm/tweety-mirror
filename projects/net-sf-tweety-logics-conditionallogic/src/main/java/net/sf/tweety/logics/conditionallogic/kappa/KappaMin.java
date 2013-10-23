package net.sf.tweety.logics.conditionallogic.kappa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents a minimum, its elements are {@link KappaTerm} instances although
 * in c-representation this are sums of Kappas {@link KappaSum}. 
 * 
 * If it is empty it is evaluated to zero.
 * 
 * @author Tim Janus
 */
public class KappaMin implements KappaTerm {

	int smallestGreaterEqual = Integer.MAX_VALUE;
	
	int smallestEvaluate = Integer.MAX_VALUE;
	
	/** The value of the kappa minimum, is -1 as long as it cannot be evaluated */
	int value = -1;
	
	/** This list contains the elements of the minimum */
	List<KappaTerm> elements = new ArrayList<KappaTerm>();
	
	@Override
	public boolean evaluate() {
		if(value != -1 || elements.isEmpty())
			return true;
		
		// search the element with the smallest evaluation value 
		for(KappaTerm kv : elements) {
			if(kv.evaluate()) {
				int cur = kv.value();
				if(cur < smallestEvaluate) {
					smallestEvaluate = cur;
				}
			} else {
				int cur = kv.greaterEqualThan();
				if(cur < smallestGreaterEqual) {
					smallestGreaterEqual = cur;
				}
			}
		}
		
		// if the smallest evaluation value is less or equal than the smallest-greater-equal value
		// then we can evaluate the value of this minimum:
		if(smallestEvaluate <= smallestGreaterEqual) {
			value = smallestEvaluate;
			return true;
		}
		
		return false;
	}

	/** 
	 * The neutral element is zero, that means if the minimum is empty this method returns zero.
	 */
	@Override
	public int value() {
		return elements.isEmpty() ? 0 : value;
	}

	@Override
	public int greaterEqualThan() {
		int reval = Integer.MAX_VALUE;
		for(KappaTerm kv : elements) {
			int cur = kv.greaterEqualThan();
			if(cur < reval) {
				reval = cur;
			}
		}
		return reval == Integer.MAX_VALUE ? 0 : reval;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("min{ ");
		for(KappaTerm kt : elements) {
			builder.append(kt.toString() + ", ");
		}
		if(!elements.isEmpty())
			builder.delete(builder.length()-2, builder.length());
		builder.append(" }");
		
		/*
		if(value == -1) {
			builder.append(" >= ");
			builder.append(greaterEqualThan());
		} else {
			builder.append(" = ");
			builder.append(value);
		}
		*/
		
		return builder.toString();
	}

	@Override
	public Set<KappaTerm> getSubTerms() {
		Set<KappaTerm> reval = new HashSet<KappaTerm>();
		for(KappaTerm kappa : elements) {
			reval.addAll(kappa.getSubTerms());
		}
		return reval;
	}
}
