package net.sf.tweety.arg.aba;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import net.sf.tweety.arg.aba.syntax.Assumption;
import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.Formula;

/**
 * @author Nils Geilen <geilenn@uni-koblenz.de>
 * This reasoner for ABA theories performs inference on the stable extensions.
 * @param <T>	the language of the underlying ABA theory
 */
public class StableReasoner<T extends Formula> extends GeneralABAReasoner<T> {

	/**
	 * Creates a new stable reasoner for the given knowledge base.
	 * @param beliefBase a knowledge base.
	 * @param inferenceType The inference type for this reasoner.
	 */
	public StableReasoner(BeliefBase beliefBase, int inferenceType) {
		super(beliefBase, inferenceType);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.arg.aba.GeneralABAReasoner#computeExtensions()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<Collection<Assumption<T>>> computeExtensions() {
		ABATheory<T> abat = (ABATheory<T>)getKnowledgeBase();
		Collection<Collection<Assumption<T>>>result = new HashSet<>();
		Collection<Collection<Assumption<T>>> exts = abat.getAllExtensions();
		for(Collection<Assumption<T>> ext : exts) {
			if(!abat.isConflictFree(ext)) 
				continue;
			if (!abat.isClosed(ext)) 
				continue;
			for(Assumption<T> a: abat.getAssumptions()) {
				if(!abat.attacks(ext, Arrays.asList(a)))
					continue;
			}
			result.add(new HashSet<>(ext));
		}
		return result;
	}

}
