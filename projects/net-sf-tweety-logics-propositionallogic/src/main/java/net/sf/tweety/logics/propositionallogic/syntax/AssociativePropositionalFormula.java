package net.sf.tweety.logics.propositionallogic.syntax;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import net.sf.tweety.Signature;
import net.sf.tweety.logics.commons.syntax.AssociativeFormulaSupport;
import net.sf.tweety.logics.commons.syntax.AssociativeFormulaSupport.AssociativeSupportBridge;
import net.sf.tweety.logics.commons.syntax.interfaces.AssociativeFormula;
import net.sf.tweety.logics.commons.syntax.interfaces.SimpleLogicalFormula;


/**
 * This class captures the common functionalities of formulas with an associative
 * operation like conjunction, disjunction, etc.
 *
 * @author Matthias Thimm
 * @author Tim Janus
 */
public abstract class AssociativePropositionalFormula extends PropositionalFormula 
	implements AssociativeFormula<PropositionalFormula>,
	AssociativeSupportBridge,
	Collection<PropositionalFormula> {

	/**
	 * The inner formulas of this formula 
	 */
	protected AssociativeFormulaSupport<PropositionalFormula> support;
	
	/**
	 * Creates a new (empty) associative formula.
	 */
	public AssociativePropositionalFormula(){
		support = new AssociativeFormulaSupport<PropositionalFormula>(this);
	}
	
	/**
	 * Creates a new associative formula with the given inner formulas. 
	 * @param formulas a collection of formulas.
	 */
	public AssociativePropositionalFormula(Collection<? extends PropositionalFormula> formulas){
		this();
		this.support.addAll(formulas);
	}
	
	/**
	 * Creates a new associative formula with the two given formulae
	 * @param first a propositional formula.
	 * @param second a propositional formula.
	 */
	public AssociativePropositionalFormula(PropositionalFormula first, PropositionalFormula second){
		this();
		this.add(first);
		this.add(second);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<PropositionalPredicate> getPredicates() {
		return (Set<PropositionalPredicate>) support.getPredicates();
	}
	
	@Override
	public Set<? extends SimpleLogicalFormula> getFormulas() {
		return support.getFormulas();
	}

	@Override
	public <C extends SimpleLogicalFormula> Set<C> getFormulas(Class<C> cls) {
		return support.getFormulas(cls);
	
	}
	
	@SuppressWarnings("unchecked")
	public Set<Proposition> getAtoms() {
		return (Set<Proposition>) support.getAtoms();
	}
	
	@Override
	public Signature createEmptySignature() {
		return new PropositionalSignature();
	}
	
	//-------------------------------------------------------------------------
	//	UTILITY METHODS
	//-------------------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((support == null) ? 0 : support.hashCode());
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
		AssociativePropositionalFormula other = (AssociativePropositionalFormula) obj;
		if (support == null) {
			if (other.support != null)
				return false;
		} else if (!support.equals(other.support))
			return false;
		return true;
	}
	
	//-------------------------------------------------------------------------
	//	COLLECTION INTERFACE
	//-------------------------------------------------------------------------
	
	@Override
	public boolean add(PropositionalFormula f){
		return this.support.add(f);
	}
	
	@Override
	public boolean addAll(Collection<? extends PropositionalFormula> c){
		return this.support.addAll(c);
	}
	
	@Override
	public void clear(){
		this.support.clear();
	}
	
	@Override
	public boolean contains(Object o){
		return this.support.contains(o);
	}
	
	@Override
	public boolean containsAll(Collection<?> c){
		return this.support.containsAll(c);
	}
	
	@Override
	public boolean isEmpty(){
		return this.support.isEmpty();
	}
	
	@Override
	public Iterator<PropositionalFormula> iterator(){
		return this.support.iterator();
	}

	@Override
	public boolean remove(Object o){
		return this.support.remove(o);
	}
	
	@Override
	public boolean removeAll(Collection<?> c){
		return this.support.removeAll(c);
	}
	
	@Override
	public boolean retainAll(Collection<?> c){
		return this.support.retainAll(c);
	}
	
	@Override
	public int size(){
		return this.support.size();
	}
	
	@Override
	public Object[] toArray(){
		return this.support.toArray();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Object[] toArray(Object[] a){
		return this.support.toArray(a);
	}

}
