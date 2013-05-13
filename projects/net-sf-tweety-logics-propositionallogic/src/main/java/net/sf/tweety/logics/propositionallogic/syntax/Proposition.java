package net.sf.tweety.logics.propositionallogic.syntax;

import java.util.*;

/**
 * This class represents a simple proposition in propositional logic. 
 * 
 * @author Matthias Thimm
 */
public class Proposition extends PropositionalFormula {
	
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
	
	/**
	 * @return the name of this proposition.
	 */
	public String getName(){
		return this.predicate.getName();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula#getPropositions()
	 */
	public Set<Proposition> getPropositions(){
		Set<Proposition> propositions = new HashSet<Proposition>();
		propositions.add(this);
		return propositions;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return this.predicate.getName();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula#collapseAssociativeFormulas()
	 */
	public PropositionalFormula collapseAssociativeFormulas(){
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((predicate == null) ? 0 : predicate.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
	
  /* (non-Javadoc)
   * @see net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula#toNNF()
   */
	@Override
	public PropositionalFormula toNnf() {
	  return this;
	}
	
}
