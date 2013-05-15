package net.sf.tweety.logics.propositionallogic.syntax;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.tweety.logics.commons.syntax.interfaces.Atom;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * This class represents a simple proposition in propositional logic. 
 * 
 * @author Matthias Thimm
 * @author Tim Janus
 */
public class Proposition extends PropositionalFormula implements Atom {
	
	/**
	 * The name of the proposition
	 */
	private PropositionalPredicate predicate;

	/**
	 * Creates a new proposition of the given name.
	 * @param name the name of the proposition.
	 */
	public Proposition(String name){
		this.predicate = new PropositionalPredicate(name);
	}
	
	public Proposition(Proposition other) {
		this.predicate = new PropositionalPredicate(other.getName());
	}
	
	/**
	 * @return the name of this proposition.
	 */
	public String getName(){
		return this.predicate.getName();
	}
	
	@Override
	public PropositionalPredicate getPredicate() {
		return this.predicate;
	}
	
	@Override
	public Set<PropositionalPredicate> getPredicates() {
		Set<PropositionalPredicate> reval = new HashSet<PropositionalPredicate>();
		reval.add(predicate);
		return reval;
	}
	
	public String toString(){
		return this.predicate.getName();
	}
	
	public PropositionalFormula collapseAssociativeFormulas(){
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((predicate == null) ? 0 : predicate.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Proposition other = (Proposition) obj;
		if (predicate == null) {
			if (other.predicate != null)
				return false;
		} else if (!predicate.equals(other.predicate))
			return false;
		return true;
	}
	
	@Override
	public PropositionalFormula toNnf() {
		return this;
	}

	@Override
	public Proposition clone() {
		return new Proposition(this);
	}

	@Override
	public void addArgument(Term<?> arg) {
		throw new UnsupportedOperationException("addArgument not supported by Propositional-Logic");
	}

	@Override
	public List<? extends Term<?>> getArguments() {
		return new ArrayList<Term<?>>();
	}

	@Override
	public boolean isComplete() {
		return true;
	}

	@Override
	public Set<Proposition> getAtoms() {
		Set<Proposition> reval = new HashSet<Proposition>();
		reval.add(this);
		return reval;
	}

	@Override
	public boolean isLiteral() {
		return true;
	}
	
}
