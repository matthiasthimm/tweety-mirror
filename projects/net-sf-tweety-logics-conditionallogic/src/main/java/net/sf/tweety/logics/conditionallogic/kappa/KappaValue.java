package net.sf.tweety.logics.conditionallogic.kappa;

import net.sf.tweety.logics.conditionallogic.syntax.Conditional;

public class KappaValue implements KappaTerm {
	int index;
	
	int value = -1;
	
	KappaMin positiveMinimum = new KappaMin();
	
	KappaMin negativeMinimum = new KappaMin();
	
	Conditional cond;
	
	boolean evaluateProcessing;
	
	public KappaValue(int index, Conditional cond) {
		this.index = index;
		this.cond = cond;
	}

	@Override
	public boolean evaluate() {
		if(evaluateProcessing)
			return false;
		
		evaluateProcessing = true;
		if(positiveMinimum.evaluate() && negativeMinimum.evaluate()) {
			value = 1 + positiveMinimum.value() - negativeMinimum.value();
		}
		evaluateProcessing = false;
		
		return value != -1;
	}

	@Override
	public int value() {
		return value;
	}

	@Override
	public int greaterEqualThan() {
		return 1;
	}
	
	
	@Override
	public String toString() {
		return "K_" + index;
	}
	
	public String fullString() {
		boolean geq = value == -1;
		return "K_" + index + (geq ? " >= " : " = ") + "1 + " + positiveMinimum + " - " + negativeMinimum;
	}
}
