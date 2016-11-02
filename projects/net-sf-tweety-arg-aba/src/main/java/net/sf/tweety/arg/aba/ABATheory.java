package net.sf.tweety.arg.aba;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.arg.aba.semantics.ABAAttack;
import net.sf.tweety.arg.aba.syntax.ABARule;
import net.sf.tweety.arg.aba.syntax.Assumption;
import net.sf.tweety.arg.aba.syntax.Deduction;
import net.sf.tweety.arg.aba.syntax.InferenceRule;
import net.sf.tweety.arg.dung.DungTheory;
import net.sf.tweety.commons.BeliefBase;
import net.sf.tweety.commons.Signature;
import net.sf.tweety.commons.util.DigraphNode;
import net.sf.tweety.commons.util.rules.DerivationGraph;
import net.sf.tweety.logics.commons.syntax.interfaces.Invertable;

public class ABATheory<T extends Invertable> implements BeliefBase {

	private Collection<InferenceRule<T>> rules = new HashSet<>();
	private Collection<Assumption<T>> assumptions = new HashSet<>();
	
	public Collection<Deduction<T>> getAllDeductions() {
		return getAllDeductions(assumptions);
	}
	
	public Collection<Deduction<T>> getAllDeductions(Collection<Assumption<T>> assumptions) {
		Set<Deduction<T>> result = new HashSet<>();
		DerivationGraph<T, ABARule<T>> graph = new DerivationGraph<>();
		Collection<ABARule<T>> allrules = new HashSet<> ();
		allrules.addAll(rules);
		allrules.addAll(assumptions);
		graph.allDerivations(allrules);
		for(DigraphNode<ABARule<T>> leaf : graph.getLeafs())
			createDeduction(leaf, result);
		return result;
	}
	
	private Deduction<T> createDeduction(DigraphNode<ABARule<T>> node, Set<Deduction<T>> set) {
		Deduction<T> result = new Deduction<>("");
		result.setRule(node.getValue());
		for (DigraphNode<ABARule<T>> parent : node.getParents()) {
			result.addSubDeduction(createDeduction(parent, set));
		}
		set.add(result);
		return result;
	}

	public void add(ABARule<T> rule) {
		if (rule instanceof Assumption)
			assumptions.add((Assumption<T>) rule);
		else if (rule instanceof InferenceRule)
			rules.add((InferenceRule<T>) rule);
	}
	
	public void addAssumption(T assumption) {
		assumptions.add(new Assumption<>(assumption));
	}

	/**
	 * @return the rules
	 */
	public Collection<InferenceRule<T>> getRules() {
		return rules;
	}

	/**
	 * @return the assumptions
	 */
	public Collection<Assumption<T>> getAssumptions() {
		return assumptions;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.commons.BeliefBase#getSignature()
	 */
	@Override
	public Signature getSignature() {
		// TODO Auto-generated method stub
		return null;
	}

	public DungTheory asDungTheory() {
		Collection<Deduction<T>> ds = getAllDeductions();
		DungTheory dt = new DungTheory();
		dt.addAll(ds);
		dt.addAllAttacks(ABAAttack.allAttacks(this));
		return dt;
	}

}
