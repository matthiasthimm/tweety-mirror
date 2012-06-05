package net.sf.tweety.logicprogramming.asplibrary.syntax;

public class NumberTerm implements Term {

	protected int	num;
	
	public	NumberTerm(int n) {
		this.num = n;
	}
	
	public	NumberTerm(String n) {
		this.num = Integer.parseInt(n);
	}
	
	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public boolean isAtom() {
		return false;
	}

	@Override
	public boolean isList() {
		return false;
	}

	@Override
	public boolean isSet() {
		return false;
	}

	@Override
	public boolean isNumber() {
		return true;
	}

	@Override
	public boolean isString() {
		return false;
	}

	@Override
	public void set(String value) {
		// TODO Auto-generated method stub
		this.num = Integer.parseInt(value);
	}

	@Override
	public String get() {
		return ""+num;
	}

	@Override
	public void set(int value) {
		this.num = value;
	}

	@Override
	public int getInt() {
		return this.num;
	}

	@Override
	public TermType type() {
		return TermType.Number;
	}

	@Override
	public String	toString() {
		return ""+this.num;
	}
	
	@Override
	public boolean	equals(Object o) {
		if (o instanceof Integer) {
			Integer i = (Integer) o;
			return i == num;
		} else if (o instanceof NumberTerm) {
			NumberTerm n = (NumberTerm) o;
			return n.num == this.num;
		} else
			return false;
	}
}
