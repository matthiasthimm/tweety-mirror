package net.sf.tweety.preferences.ranking;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.math.equation.Inequation;
import net.sf.tweety.math.opt.*;
import net.sf.tweety.math.opt.solver.*;
import net.sf.tweety.math.term.IntegerVariable;
import net.sf.tweety.math.term.Term;
import net.sf.tweety.math.term.Variable;
import net.sf.tweety.preferences.PreferenceOrder;
import net.sf.tweety.preferences.Relation;
import net.sf.tweety.util.Triple;

/**
 * This class is meant to provide ranking functions to given preference orders
 * and vice versa. To be implemented. A ranking function characterizes a
 * preference order uniquely as: 1.: rank: O -> N+ where O is the set of
 * elements in the preference order. 2.: the sum of all ranks for each element
 * in O is minimal
 * 
 * TODO exception handling for invalid preference orders (total preorder)
 * 
 * @author Bastian Wolf
 * @param <T>
 * 
 */

public class RankingFunction<T> extends HashMap<T, Integer> implements
		Map<T, Integer> {

	private static final long serialVersionUID = 1L;

	/**
	 * this constructor creates a ranking function using a given preference
	 * order
	 * 
	 * @param po
	 *            the given preference order
	 */
	public RankingFunction(PreferenceOrder<T> po) {

		Map<T, IntegerVariable> intVar = new HashMap<T, IntegerVariable>();

		Set<Triple<IntegerVariable, IntegerVariable, Relation>> optIneq = new HashSet<Triple<IntegerVariable, IntegerVariable, Relation>>();
		OptimizationProblem opt = new OptimizationProblem(
				OptimizationProblem.MINIMIZE);

		for (final T e : po.getDomainElements()) {
			intVar.put(e, new IntegerVariable(e.toString(), true));
		}

		Iterator<Triple<T, T, Relation>> it = po.iterator();

		while (it.hasNext()) {
			Triple<T, T, Relation> tempRel = it.next();

			IntegerVariable tempVarF = null;
			IntegerVariable tempVarS = null;

			if (po.contains(tempRel)) {
				tempVarF = intVar.get(tempRel.getFirst());
				tempVarS = intVar.get(tempRel.getSecond());
				switch (tempRel.getThird()) {
				case LESS:
					opt.add(new Inequation(tempVarF, tempVarS, Inequation.LESS));
				case LESS_EQUAL:
					opt.add(new Inequation(tempVarF, tempVarS,
							Inequation.LESS_EQUAL));
				default:
					continue;
				}
			} else {
				continue;
			}
		}

		List<Term> terms = new LinkedList<Term>();

		for (Entry<T, IntegerVariable> e : intVar.entrySet()) {
			Term t = e.getValue();
			terms.add(t);
		}

		Iterator<Term> termIt = terms.listIterator();

		if (termIt.hasNext()) {
			Term t = termIt.next();
			while (termIt.hasNext()) {
				t = t.add(termIt.next());
			}
			opt.setTargetFunction(t);
		}

		LpSolve solver = new LpSolve(opt);
		Map<Variable, Term> solution = solver.solve();
		Map<T, Integer> sol = new HashMap<T, Integer>();
		for (Entry<Variable, Term> e : solution.entrySet()) {
			T key = (T) e.getKey().toString();
			Integer val = (int) e.getValue().doubleValue();
			sol.put(key, val);
		}

		this.putAll(sol);
	}

	/**
	 * returns a string representation for this ranking function
	 */
	public String toString() {
		String s = "{";
		int count = 1;
		for (Entry<T, Integer> e : this.entrySet()) {

			if (count < this.entrySet().size())
				s += e.toString() + ", ";
			else
				s += e.toString();
			count++;
		}
		s += "}";

		return s;

	}

	/**
	 * returns the ranking function
	 * 
	 * @return ranking function
	 */
	public Map<T, Integer> getRankingFunction() {
		return this;
	}

	/**
	 * this method returns a preference order made out of an ranking function
	 * 
	 * @returns a preference order out of a given ranking function
	 */
	//TODO Merging with ScoringPreferenceAggregator line 102.
	public PreferenceOrder<T> generatePreferenceOrder() {
		Set<Triple<T, T, Relation>> tempPO = new HashSet<Triple<T, T, Relation>>();
		
		Map<T, Integer> in = this;

		for (Entry<T, Integer> f : in.entrySet()) {
			for (Entry<T, Integer> s : in.entrySet()) {
				
				if (!f.getKey().equals(s.getKey())){
					if (f.getValue() < s.getValue()){
						Triple<T, T, Relation> rel = new Triple<T, T, Relation>(f.getKey(), s.getKey(), Relation.LESS);
						tempPO.add(rel);
					} else if (f.getValue() == s.getValue()){
						Triple<T, T, Relation> rel = new Triple<T, T, Relation>(f.getKey(), s.getKey(), Relation.LESS_EQUAL);
						tempPO.add(rel);
					} else
						continue;
						
				}				
			}
		}
		PreferenceOrder<T> po = new PreferenceOrder<T>(tempPO);
		return po;
	}

	/**
	 * checks whether the key is present in the entry-set of the map
	 */
	@Override
	public boolean containsKey(Object key) {
		for (Entry<T, Integer> o : this.entrySet()) {
			if (o.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * checks whether the value is present in the entry-set of the map
	 */
	@Override
	public boolean containsValue(Object value) {
		Iterator<Entry<T, Integer>> temp = this.entrySet().iterator();
		if (temp.hasNext()) {
			while (temp.hasNext()) {
				if (temp.next().getValue().equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * returns the value to a given key
	 * 
	 * @return the value if present, null otherwise (but value.equals(null) is
	 *         possible)
	 */
	@Override
	public Integer get(Object key) {
		Iterator<Entry<T, Integer>> temp = this.entrySet().iterator();
		if (temp.hasNext()) {
			while (temp.hasNext()) {
				Entry<T, Integer> e = temp.next();
				if (e.getKey().equals(key)) {
					return e.getValue();
				}
			}
		}
		return null;
	}

	
	/**
	 * returns a collection containing all values of the map
	 */
	@Override
	public Collection<Integer> values() {
		Set<Integer> v = new HashSet<Integer>();
		Iterator<Entry<T, Integer>> temp = this.entrySet().iterator();
		if (temp.hasNext()) {
			while (temp.hasNext()) {
				Entry<T, Integer> e = temp.next();
				v.add(e.getValue());
			}
		}

		return v;
	}

	public void weakenElement(T element){
		this.put(element, this.get(element)+1);
	}
	
	public void strengthenElement(T element){
		this.put(element, this.get(element)-1);
	}
	
}
