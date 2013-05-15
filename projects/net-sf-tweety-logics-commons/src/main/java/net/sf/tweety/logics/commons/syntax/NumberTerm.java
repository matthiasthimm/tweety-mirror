package net.sf.tweety.logics.commons.syntax;


/**
 * This is a term representing an integer number it is used
 * to distinguish between objects like an auto a which is
 * modeled as constant and integral numbers like 42.
 * 
 * @author Tim Janus
 */
public class NumberTerm extends TermAdapter<Integer> {

	/** the number saved in the logical term */
	private int number;
	
	/**
	 * Ctor: Creates a new NumberTerm, the sort "Thing" is used.
	 * @param	number	the value of the number term
	 */
	public NumberTerm(int number) {
		super(Sort.THING);
		this.number = number;
	}
	
	/**
	 * Ctor: Creates a new NumberTerm using the sort and the value
	 * given as parameter.
	 * @param number	The value of the number term
	 * @param sort		The sort representing the type of the number term
	 */
	public NumberTerm(int number, Sort sort) {
		super(sort);
		this.number = number;
	}
	
	/**
	 * Ctor: Creates a new NumberTerm, the sort "Thing" is used.
	 * @param	number	The value of the number term as string
	 */
	public NumberTerm(String number) {
		this(Integer.parseInt(number));
	}
	
	/**
	 * Ctor: Creates a new NumberTerm using the sort and the value
	 * given as parameter.
	 * @param number	The value of the number term as string
	 * @param sort		The sort representing the type of the number term
	 */
	public NumberTerm(String number, Sort sort) {
		this(Integer.parseInt(number), sort);
	}
	
	/**
	 * Copy-Ctor: Creates a deep copy of the given NumberTerm
	 * @param other	The NumberTerm that is the source for the copy.
	 */
	public NumberTerm(NumberTerm other) {
		super(other.getSort());
		this.number = other.number;
	}
	
	@Override
	public Integer get() {
		return number;
	}
	
	@Override
	public void set(Integer value) {
		this.number = value;
	}

	@Override
	public String toString() {
		return String.valueOf(number);
	}
	
	@Override
	public NumberTerm clone() {
		return new NumberTerm(this);
	}

}
