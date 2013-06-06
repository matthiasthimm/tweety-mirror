package net.sf.tweety.preferences.update;

import net.sf.tweety.preferences.Operation;
import net.sf.tweety.preferences.PreferenceOrder;
//import net.sf.tweety.preferences.Quadruple;

/**
 * This Update-class provides update-elements used within dynamic preference aggregations
 * @author Bastian Wolf
 *
 * @param <T> the generic element type
 */

public class Update<T> {
	
	/**
	 * The first element
	 */
	PreferenceOrder<T> po;
	
	/**
	 * The second element
	 */
	Operation op;
	
	/**
	 * The third element
	 */
	Integer amount;
	
	/**
	 * The fourth element
	 */
	T element;
	
	/**
	 * The constructor for update-elements
	 * 
	 * @param po the preference operation meant to be changed
	 * @param op the operation that is going to be used (WEAKEN or STRENGTHEN)
	 * @param amount the amount of operations to be used
	 * @param element the element within the preference order to be affected
	 */
	public Update(PreferenceOrder<T> po, Operation op, Integer amount, T element) {
		this.po = po;
		this.op = op;
		this.amount = amount;
		this.element = element;
	}
	

	/**
	 * returns the first element of this quadruple
	 * @return the first element of this quadruple
	 */
	public PreferenceOrder<T> getPreferenceOrder() {
		return po;
	}
	/**
	 * sets the first element of this triple
	 * @param po an object of type E
	 */
	public void setPreferenceOrder(PreferenceOrder<T> po) {
		this.po = po;
	}
	/**
	 * returns the second element of this quadruple
	 * @return the second element of this quadruple
	 */
	public Operation getOperation() {
		return op;
	}
	/**
	 * sets the second element of this triple
	 * @param op an object of type E
	 */
	public void setOperation(Operation op) {
		this.op = op;
	}
	/**
	 * returns the third element of this quadruple
	 * @return the third element of this quadruple
	 */
	public Integer getAmount() { 
		return amount;
	}
	/**
	 * sets the third element of this triple
	 * @param amount an object of type F
	 */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	/**
	 * returns the fourth element of this quadruple
	 * @return the fourth element of this quadruple
	 */
	public T getElement() {
		return element;
	}
	/**
	 * sets the fourth element of this triple
	 * @param element an object of type H
	 */
	public void setElement(T element) {
		this.element = element;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((po == null) ? 0 : po.hashCode());
		result = prime * result + ((op == null) ? 0 : op.hashCode());
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((element == null) ? 0 : element.hashCode());
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Quadruple<?,?,?,?> other = (Quadruple<?,?,?,?>) obj;
//		if (po == null) {
//			if (other.po != null)
//				return false;
//		} else if (!po.equals(other.po))
//			return false;
//		if (op == null) {
//			if (other.op != null)
//				return false;
//		} else if (!op.equals(other.op))
//			return false;
//		if (amount == null) {
//			if (other.amount != null)
//				return false;
//		} else if (!amount.equals(other.amount))
//			return false;
//		if (element == null) {
//			if (other.element != null)
//				return false;
//		} else if (!element.equals(other.element))
//			return false;
//		return true;
//	}
	
}
