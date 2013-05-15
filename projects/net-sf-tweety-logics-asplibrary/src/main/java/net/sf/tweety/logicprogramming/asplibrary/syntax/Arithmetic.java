package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * this class extends an ordinary atom to be used as
 * an arithmetic expression.
 * 
 * @todo implmeent invert
 * @todo use an enum for operators instead string
 * @author Thomas Vengels
 * @author Tim Janus
 */
public class Arithmetic implements RuleElement {
	
	private String operator;
	private Term<?> arg1, arg2, result;
	
	public Arithmetic(String op, Term<?> arg1, Term<?> arg2) {
		this.operator = op;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.result = null;
	}
	
	public Arithmetic(String op, Term<?> arg1, Term<?> arg2, Term<?> result) {
		this.operator = op;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.result = result;
	}
	
	public Arithmetic(Arithmetic other) {
		this.operator = other.operator;
		this.arg1 = (Term<?>)other.arg1.clone();
		this.arg2 = (Term<?>)other.arg2.clone();
		this.result = (Term<?>)other.result.clone();

	}
	
	public Term<?> getResult() {
		return result;
	}
	
	public Term<?> getFirstArgument() {
		return arg1;
	}
	
	public Term<?> getSecondArgument() {
		return arg2;
	}
	
	public String getOperator() {
		return operator;
	}
	
	@Override
	public String toString() {
		if(result != null)
			return result + " = " + arg1 + " " + operator + " " + arg2;
		return arg1 + " " + operator + " " + arg2;
	}

	@Override
	public SortedSet<Literal> getLiterals() {
		return new TreeSet<Literal>();
	}

	@Override
	public RuleElement invert() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Term<?>> getTerms() {
		List<Term<?>> reval = new LinkedList<Term<?>>();
		reval.add(arg1);
		reval.add(arg2);
		reval.add(result);
		return reval;
	}

	@Override
	public boolean isGround() {
		return false;
	}
	
	@Override
	public Object clone() {
		return new Arithmetic(this);
	}
}
