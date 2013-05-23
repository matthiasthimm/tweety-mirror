package net.sf.tweety.logicprogramming.asplibrary.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.ELPLiteral;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Neg;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;

public class BeliefSet {

	public Map<String, Set<ELPLiteral>>	literals = new HashMap<String, Set<ELPLiteral>>();
	
	public BeliefSet() {}
	
	public BeliefSet(BeliefSet other) {
		for(String key : other.literals.keySet()) {
			Set<ELPLiteral> set = new HashSet<ELPLiteral>();
			this.literals.put(key, set);
			
			for(ELPLiteral l : other.literals.get(key)) {
				set.add((ELPLiteral)l.clone());
			}
		}
	}
	
	public BeliefSet(Collection<ELPLiteral> lits) {
		literals = new HashMap<String,Set<ELPLiteral>>();
		for (ELPLiteral l : lits)
			add(l);
	}
	
	
	public void add(ELPLiteral l) {
		String functor = l.getAtom().getName();
		
		Set<ELPLiteral> sl = literals.get(functor);
		if (sl == null) {
			sl = new HashSet<ELPLiteral>();
			literals.put(functor, sl);
		}
		
		sl.add(l);
	}
	
	/** @return all literals in the belief set */
	public Set<ELPLiteral> getLiterals() {
		Set<ELPLiteral> reval = new HashSet<ELPLiteral>();
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
	
	public Set<ELPLiteral> getLiteralsBySymbol(String functor) {
		Set<ELPLiteral> ret = literals.get(functor);
		
		if (ret == null)
			return Collections.<ELPLiteral>emptySet();
		else
			return ret;
	}
	
	
	public int	size() {
		int ret = 0;
		for (Set<ELPLiteral> s : literals.values()) {
			ret += s.size();
		}
		
		return ret;
	}
	
		
	@Override
	public String toString() {
		String ret = "";
		boolean first = true;
		
		for (Set<ELPLiteral> s : literals.values()) {
			for (ELPLiteral l : s) {
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
			Collection<ELPLiteral> lits = literals.get(pred);
			for (ELPLiteral l : lits) {
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
	public Set<ELPLiteral> replace(String functor, Set<ELPLiteral> literals) {
		if (literals == null) {
			return this.literals.remove(functor);
		} else {
			return this.literals.put(functor, literals);
		}
	}
	
	public void pos_replace(String functor, Set<ELPLiteral> literals) {
		this.literals.remove(functor);
		
		Set<ELPLiteral> sl = new HashSet<ELPLiteral>();
		for (ELPLiteral l : literals) {
			if (!( l instanceof Neg) )
				sl.add(l);
		}
		
		this.literals.put(functor, sl);
	}

	public boolean contains(ELPLiteral lit) {
		Set<ELPLiteral> set = literals.get(lit.getAtom().getPredicate().getName());
		return set != null ? set.contains(lit) : false;
	}
	
	public boolean containsAll(Collection<ELPLiteral> lits) {
		if(lits == null)
			throw new NullPointerException();
		
		for(ELPLiteral l : lits) {
			Set<ELPLiteral> getted = literals.get(l.getAtom().getName());
			if(getted == null || !getted.contains(l))
				return false;
		}
		
		return true;
	}
	
	public boolean holds(Collection<ELPLiteral> posLits, Collection<ELPLiteral> negLits) {
		if (posLits != null) {
			for(ELPLiteral l : posLits) {
				Set<ELPLiteral> lits = literals.get(l.getAtom().getName());
				if (lits == null)
					return false;
				
				if (!lits.contains(l))
					return false;
			}
		}
		
		if (negLits != null) {
			for(ELPLiteral l : negLits) {
				Set<ELPLiteral> lits = literals.get(l.getAtom().getName());
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
