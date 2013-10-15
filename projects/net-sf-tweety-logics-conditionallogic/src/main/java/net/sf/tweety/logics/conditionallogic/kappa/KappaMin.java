package net.sf.tweety.logics.conditionallogic.kappa;

import java.util.ArrayList;
import java.util.List;

public class KappaMin implements KappaTerm {

	int smallestGreaterEqual = Integer.MAX_VALUE;
	
	int smallestEvaluate = Integer.MAX_VALUE;
	
	int value = -1;
	
	List<KappaTerm> elements = new ArrayList<KappaTerm>();
	
	@Override
	public boolean evaluate() {
		if(value != -1 || elements.isEmpty())
			return true;
		
		for(KappaTerm kv : elements) {
			if(kv.evaluate()) {
				int cur = kv.value();
				if(smallestEvaluate < cur) {
					smallestEvaluate = cur;
				}
			} else {
				int cur = kv.greaterEqualThan();
				if(smallestGreaterEqual < cur) {
					smallestGreaterEqual = cur;
				}
			}
		}
		
		if(smallestGreaterEqual <= smallestEvaluate) {
			value = smallestEvaluate;
			return true;
		}
		
		return false;
	}

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
}
