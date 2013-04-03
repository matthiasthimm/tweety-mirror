package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * A Variable is a specialized StringTerm which only allows
 * name with have a uppercase character as first letter.
 * 
 * @author Tim Janus
 */
public class Variable extends StringTerm {

	public Variable(String value) {
		super(value);
	}
	
	public Variable(Variable other) {
		super(other.get());
	}

	@Override
	public void set(String value) {
		if(value == null || value.length() == 0)
			throw new IllegalArgumentException();
		
		char c = value.charAt(0);
		if( !(c > 64 && c <= 90) && c != '_')
			throw new IllegalArgumentException("Variable names start with a upper-case character." +
					"'" + value +"'");
		
		this.name = value;
	}
	
	@Override
	public Object clone() {
		return new Variable(this);
	}
}
