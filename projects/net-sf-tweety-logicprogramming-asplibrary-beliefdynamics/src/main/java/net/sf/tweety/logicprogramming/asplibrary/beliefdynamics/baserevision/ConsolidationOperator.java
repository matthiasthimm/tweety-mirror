package net.sf.tweety.logicprogramming.asplibrary.beliefdynamics.baserevision;

import java.util.Collection;

import net.sf.tweety.Formula;

public interface ConsolidationOperator<T extends Formula> {
	public Collection<T> consolidate(Collection<T> p);
}
