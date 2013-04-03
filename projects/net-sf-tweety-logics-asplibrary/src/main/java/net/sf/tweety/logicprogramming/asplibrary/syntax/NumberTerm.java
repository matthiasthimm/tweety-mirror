package net.sf.tweety.logicprogramming.asplibrary.syntax;

public class NumberTerm implements Term<Integer> {

	protected int	num;
	
	public	NumberTerm(int n) {
		this.num = n;
	}
	
	public	NumberTerm(String n) {
		this.num = Integer.parseInt(n);
	}
	
	public NumberTerm(NumberTerm other) {
		this.num = other.num;
	}

	public void set(String value) {
		// TODO Auto-generated method stub
		this.num = Integer.parseInt(value);
	}

	@Override
	public Integer get() {
		return num;
	}

	@Override
	public void set(Integer value) {
		this.num = value;
	}

	@Override
	public String	toString() {
		return ""+this.num;
	}
	
	public Object clone() {
		return new NumberTerm(this);
	}
}
