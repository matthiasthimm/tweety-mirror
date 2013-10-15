package net.sf.tweety.logics.conditionallogic.kappa;

public interface KappaTerm {
	boolean evaluate();
	
	int value();
	
	int greaterEqualThan();
}
