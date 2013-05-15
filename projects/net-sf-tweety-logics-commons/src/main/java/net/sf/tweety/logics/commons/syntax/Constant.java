package net.sf.tweety.logics.commons.syntax;

/**
 * A Constant represents an constant object in the world of
 * a logical language. It is implemented as a specialized StringTerm 
 * which only allows name with have a lower-case character as first letter.
 * 
 * @author Tim Janus
 */
public class Constant extends StringTerm {	
	
	/**
	 * Ctor: Creates a new Constant with the given name, uses "Thing"
	 * as sort.
	 * @param name	The name of the Constant
	 */
	public Constant(String name){
		this(name,Sort.THING);
	}
	
	/**
	 * Ctor: Creates a new Constant with the given name and sort
	 * @param name	The name of the Constant
	 * @param sort	The sort of the Constant
	 */
	public Constant(String name, Sort sort){
		super(name, sort);
	}
	
	/**
	 * Copy-Ctor: Creates a deep copy of the given Constant
	 * @param other	The Constant that acts as source for the copy
	 */
	public Constant(Constant other) {
		super(other.value, other.getSort());
	}

	@Override
	public void set(String value) {
		if(value == null || value.length() == 0)
			throw new IllegalArgumentException();
		
		if( !(value.charAt(0) > 96 && value.charAt(0) <= 122))
			throw new IllegalArgumentException("Constant names start with a lower-case character. " +
					"'" + value + "'");
		
		this.value = value;
	}
	
	@Override
	public Constant clone() {
		return new Constant(this);
	}
}
