package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * This class implements the Arithmetic semantic given
 * by the DLV Manual. Because for build-in functions like
 * #rand a complement is not possible this class does not
 * implement the Invertable interface.
 * 
 * @todo use an enum for operators instead string
 * 
 * @author Tim Janus
 * @author Thomas Vengels
 */
public class Arithmetic extends DLPElementAdapter implements DLPElement {
	
	/** the operator or the id of the build-in function */
	private String operator;
	
	/** 
	 * the term X on the left of an arithmetic expression: Z=X+Y or the first
	 * argument of an build-in function #func(X,Y,Z)
	 */
	private Term<?> left; 
	
	/** 
	 * the term Y on the right of the arithmetic expression: Z=X+Y or the second
	 * argument of an build-in function #func(X,Y,Z)
	 */
	private Term<?> right;
	
	/** 
	 * the term Z giving the result of the arithmetic expression: Z=X+Y or the third
	 * argument of an build-in function #func(X,Y,Z)
	 */
	private Term<?> result;
		
	public Arithmetic(String op, Term<?> left, Term<?> right, Term<?> result) {
		this.operator = op;
		this.left = left;
		this.right = right;
		this.result = result;
	}
	
	/**
	 * Copy-Ctor
	 * @param other
	 */
	public Arithmetic(Arithmetic other) {
		this.operator = other.operator;
		this.left = (Term<?>)other.left.clone();
		this.right = (Term<?>)other.right.clone();
		this.result = (Term<?>)other.result.clone();
	}
	
	/** 
	 * @return the term Z giving the result of the arithmetic expression: Z=X+Y 
	 * or the third argument of an build-in function #func(X,Y,Z)
	 */
	public Term<?> getResult() {
		return result;
	}
	
	/** 
	 * @return the term X on the left of an arithmetic expression: Z=X+Y or 
	 * the first argument of an build-in function #func(X,Y,Z)
	 */
	public Term<?> getLeftArgument() {
		return left;
	}
	
	/** 
	 * @return the term Y on the right of the arithmetic expression: Z=X+Y or 
	 * the second argument of an build-in function #func(X,Y,Z)
	 */
	public Term<?> getRightArgument() {
		return right;
	}
	
	/** @return the operator or the id of the build-in function */
	public String getOperator() {
		return operator;
	}
	
	@Override
	public String toString() {
		if(result != null)
			return result + " = " + left + " " + operator + " " + right;
		return left + " " + operator + " " + right;
	}

	@Override
	public SortedSet<DLPLiteral> getLiterals() {
		return new TreeSet<DLPLiteral>();
	}

	@Override
	public Set<Term<?>> getTerms() {
		Set<Term<?>> reval = new HashSet<Term<?>>();
		if(left != null) {
			reval.add(left);
			if(right != null) {
				reval.add(right);
				if(result != null) {
					reval.add(result);
				}
			}
		}
		return reval;
	}

	@Override
	public boolean isGround() {
		return false;
	}
	
	@Override
	public Arithmetic clone() {
		return new Arithmetic(this);
	}

	@Override
	public Set<DLPPredicate> getPredicates() {
		return new HashSet<DLPPredicate>();
	}

	@Override
	public Set<DLPAtom> getAtoms() {
		return new HashSet<DLPAtom>();
	}

	@Override
	public Arithmetic substitute(Term<?> t, Term<?> v) {
		Arithmetic reval = new Arithmetic(this);
		if(t.equals(left))
			reval.left = v;
		if(t.equals(right))
			reval.right = v;
		if(t.equals(result)) 
			reval.result = v;
		return reval;
	}

	@Override
	public DLPSignature getSignature() {
		DLPSignature reval = new DLPSignature();
		reval.add(left);
		reval.add(right);
		if(result != null)
			reval.add(result);
		return reval;
	}
	
	@Override
	public int hashCode() {
		int prime = 23;
		int factor = 0;
		factor += left == null ? 0 : left.hashCode();
		factor += right == null ? 0 : right.hashCode();
		factor += result == null ? 0 : result.hashCode();
		factor += operator.hashCode();
		return prime*factor;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Arithmetic)) {
			return false;
		}
		Arithmetic cother = (Arithmetic)other;
		if(left != null) {
			if(!left.equals(cother.left)) {
				return false;
			}
		} else if(cother.left != null) {
			return false;
		}
		
		if(right != null) {
			if(!right.equals(cother.right)) {
				return false;
			}
		} else if(cother.right != null) {
			return false;
		}
		
		if(result != null) {
			if(!result.equals(cother.result)) {
				return false;
			}
		} else if(cother.result != null) {
			return false;
		}
		
		return operator.equals(cother.operator);
	}
}
