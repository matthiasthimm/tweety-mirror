package net.sf.tweety.logicprogramming.asplibrary.beliefdynamics.baserevision;

import java.util.Collection;
import java.util.HashSet;

import net.sf.tweety.Formula;

public abstract class RemainderSets<T extends Formula> extends HashSet<Collection<T>> {
	private static final long serialVersionUID = 1L;

	public abstract Collection<T> getInputProgram();
}
