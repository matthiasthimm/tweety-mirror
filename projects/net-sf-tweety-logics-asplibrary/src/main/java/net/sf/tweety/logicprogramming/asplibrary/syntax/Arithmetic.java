package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * this class extends an ordinary atom to be used as
 * an arithmetic expression.
 * 
 * @todo implement complement()
 * @todo use an enum for operators instead string
 * 
 * @author Thomas Vengels
 * @author Tim Janus
 */
public class Arithmetic extends ELPElementAdapter implements ELPElement {
	
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
	public SortedSet<ELPLiteral> getLiterals() {
		return new TreeSet<ELPLiteral>();
	}

	@Override
	public Set<Term<?>> getTerms() {
		Set<Term<?>> reval = new HashSet<Term<?>>();
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
	public Arithmetic clone() {
		return new Arithmetic(this);
	}

	@Override
	public Set<ELPPredicate> getPredicates() {
		return new HashSet<ELPPredicate>();
	}

	@Override
	public Set<ELPAtom> getAtoms() {
		return new HashSet<ELPAtom>();
	}

	@Override
	public Arithmetic substitute(Term<?> t, Term<?> v) {
		Arithmetic reval = new Arithmetic(this);
		if(t.equals(arg1))
			reval.arg1 = v;
		if(t.equals(arg2))
			reval.arg2 = v;
		if(t.equals(result)) 
			reval.result = v;
		return reval;
	}

	@Override
	public ElpSignature getSignature() {
		ElpSignature reval = new ElpSignature();
		reval.add(arg1);
		reval.add(arg2);
		if(result != null)
			reval.add(result);
		return reval;
	}
}
