package edu.cs.ai.kr;

import java.util.*;

/**
 * This class models a signature as a set of formulas.
 * 
 * @author Matthias Thimm
 *
 * @param T The type of formulas in this signature.
 */
public class SetSignature<T extends Formula> extends Signature implements Collection<T> {

	private Set<T> formulas;
	
	/**
	 * Creates a empty new set signature.
	 */
	public SetSignature(){
		this(new HashSet<T>());
	}
	
	/**
	 * Creates a new set signature with the single given formula.
	 * @param f a formula.
	 */
	public SetSignature(T f){
		this(new HashSet<T>());
		this.add(f);
	}
	
	/**
	 * Creates a new set signature with the given set of formulas.
	 * @param formulas a collection of formulas.
	 */
	public SetSignature(Collection<? extends T> formulas){
		this.formulas = new HashSet<T>(formulas);
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.Signature#isSubSignature(edu.cs.ai.kr.Signature)
	 */
	public boolean isSubSignature(Signature other){
		if(!(other instanceof SetSignature<?>))
			return false;
		return ((SetSignature<?>)other).containsAll(this);
	}
		
	/* (non-Javadoc)
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	@Override
	public boolean add(T e) {
		return this.formulas.add(e);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends T> c) {
		return this.formulas.addAll(c);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#clear()
	 */
	@Override
	public void clear() {
		this.formulas.clear();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o) {
		return this.formulas.contains(o);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		return this.formulas.containsAll(c);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.formulas.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return this.formulas.iterator();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		return this.formulas.remove(o);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		return this.formulas.removeAll(c);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		return this.formulas.retainAll(c);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#size()
	 */
	@Override
	public int size() {
		return this.formulas.size();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#toArray()
	 */
	@Override
	public Object[] toArray() {
		return this.formulas.toArray();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#toArray(T[])
	 */
	@Override
	public <S> S[] toArray(S[] a) {
		return this.formulas.toArray(a);
	}

}
