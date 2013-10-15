package net.sf.tweety.logics.conditionallogic.kappa;

import java.util.ArrayList;
import java.util.List;

public class KappaSum implements KappaTerm {

	int value = -1;
	
	List<KappaTerm> elements = new ArrayList<KappaTerm>();
	
	@Override
	public boolean evaluate() {
		if(value != -1 || elements.isEmpty())
			return true;
		
		boolean evaluateable = true;
		for(KappaTerm kt : elements) {
			if(!kt.evaluate()) {
				evaluateable = false;
				break;
			}
		}
		
		if(evaluateable) {
			value = 0;
			for(KappaTerm kt : elements) {
				value += kt.value();
			}
		}
			
		return value != -1;
	}

	@Override
	public int value() {
		return elements.isEmpty() ? 0 : value;
	}

	@Override
	public int greaterEqualThan() {
		int reval = 0;
		for(KappaTerm kt : elements) {
			if(kt.evaluate()) {
				reval += kt.value();
			} else {
				reval += kt.greaterEqualThan();
			}
		}
		return reval;
	}

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		for(KappaTerm element : elements) {
			builder.append(element + " + ");
		}
		builder.delete(builder.length()-3, builder.length());
		builder.append(")");
		
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
