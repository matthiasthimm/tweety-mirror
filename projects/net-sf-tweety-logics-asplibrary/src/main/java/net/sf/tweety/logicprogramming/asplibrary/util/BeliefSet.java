package net.sf.tweety.logicprogramming.asplibrary.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Literal;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Neg;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;

public class BeliefSet {

	public Map<String, Set<Literal>>	literals = new HashMap<String, Set<Literal>>();
	
	public BeliefSet() {}
	
	public BeliefSet(BeliefSet other) {
		for(String key : other.literals.keySet()) {
			Set<Literal> set = new HashSet<Literal>();
			this.literals.put(key, set);
			
			for(Literal l : other.literals.get(key)) {
				set.add((Literal)l.clone());
			}
		}
	}
	
	public BeliefSet(Collection<Literal> lits) {
		literals = new HashMap<String,Set<Literal>>();
		for (Literal l : lits)
			add(l);
	}
	
	
	public void add(Literal l) {
		String functor = l.getAtom().getName();
		
		Set<Literal> sl = literals.get(functor);
		if (sl == null) {
			sl = new HashSet<Literal>();
			literals.put(functor, sl);
		}
		
		sl.add(l);
	}
	
	/** @return all literals in the belief set */
	public Set<Literal> getLiterals() {
		Set<Literal> reval = new HashSet<Literal>();
		for(String key : literals.keySet()) {
			reval.addAll(literals.get(key));
		}
		return reval;
	}
	
	public Set<String> getFunctors() {
		return literals.keySet();
	}
	
	public void remove(String functor) {
		literals.remove(functor);
	}
	
	public void removeAll(Collection<String> functors) {
		for(String str : functors) {
			remove(str);
		}
	}
	
	public Set<Literal> getLiteralsBySymbol(String functor) {
		Set<Literal> ret = literals.get(functor);
		
		if (ret == null)
			return Collections.<Literal>emptySet();
		else
			return ret;
	}
	
	
	public int	size() {
		int ret = 0;
		for (Set<Literal> s : literals.values()) {
			ret += s.size();
		}
		
		return ret;
	}
	
		
	@Override
	public String toString() {
		String ret = "";
		boolean first = true;
		
		for (Set<Literal> s : literals.values()) {
			for (Literal l : s) {
				if (!first)
					ret+=", ";
				ret += l;
				first = false;
			}
		}
		
		return ret;
	}
	
	public Program toProgram() {
		Program p = new Program();
		
		for (String pred : literals.keySet() ) {
			Collection<Literal> lits = literals.get(pred);
			for (Literal l : lits) {
				Rule r = new Rule();
				r.addHead(l);
				p.add(r);
			}
		}
		
		return p;
	}
	
	/**
	 * this method replaces all literals within the belief set
	 * given a predicate symbol by all literals provided.
	 * if the passed literal set is null, the entry is removed
	 * from the belief set.
	 * 
	 * @param functor predicate symbol for replace operation
	 * @param literals set of new literals
	 * @return literals matching symbol being replaced
	 */
	public Set<Literal> replace(String functor, Set<Literal> literals) {
		if (literals == null) {
			return this.literals.remove(functor);
		} else {
			return this.literals.put(functor, literals);
		}
	}
	
	public void pos_replace(String functor, Set<Literal> literals) {
		this.literals.remove(functor);
		
		Set<Literal> sl = new HashSet<Literal>();
		for (Literal l : literals) {
			if (!( l instanceof Neg) )
				sl.add(l);
		}
		
		this.literals.put(functor, sl);
	}

	public boolean contains(Literal lit) {
		Set<Literal> set = literals.get(lit.getAtom().getPredicate().getName());
		return set != null ? set.contains(lit) : false;
	}
	
	public boolean containsAll(Collection<Literal> lits) {
		if(lits == null)
			throw new NullPointerException();
		
		for(Literal l : lits) {
			Set<Literal> getted = literals.get(l.getAtom().getName());
			if(getted == null || !getted.contains(l))
				return false;
		}
		
		return true;
	}
	
	public boolean holds(Collection<Literal> posLits, Collection<Literal> negLits) {
		if (posLits != null) {
			for(Literal l : posLits) {
				Set<Literal> lits = literals.get(l.getAtom().getName());
				if (lits == null)
					return false;
				
				if (!lits.contains(l))
					return false;
			}
		}
		
		if (negLits != null) {
			for(Literal l : negLits) {
				Set<Literal> lits = literals.get(l.getAtom().getName());
				// @Thomas: Close world assumption??? I dont get it.
				if ((lits != null) && (lits.contains(l)))
					return false;
			}
		}
		
		return true;
	}
	
	@Override
	public Object clone() {
		return new BeliefSet(this);
	}
}