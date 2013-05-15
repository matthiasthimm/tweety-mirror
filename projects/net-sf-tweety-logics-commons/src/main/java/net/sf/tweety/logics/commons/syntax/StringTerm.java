package net.sf.tweety.logics.commons.syntax;

/**
 * This class represents terms which are objects identified by a
 * string. Subclasses are Variable and Constant.
 * 
 * @author Tim Janus
 * @author Thomas Vengels
 */
public abstract class StringTerm extends TermAdapter<String> {

	/** the value of the term */
	protected String value;

	/** 
	 * Ctor: Creates a string term with the given String as value, uses the
	 * Sort "Thing"
	 * @param value	The value for the string term.
	 */
	public StringTerm(String value) {
		super();
		set(value);
	}
	
	/**
	 * Ctor: Create a string term with the given value and sort.
	 * @param value	The value of for the string term.
	 * @param sort	The sort representing the type of the StringTerm.
	 */
	public StringTerm(String value, Sort sort) {
		super(sort);
		set(value);
	}
	
	/** 
	 * Copy-Ctor: Creates a deep copy of the StringTerm
	 * @param other	The StringTerm that acts as source for the copy
	 */
	public StringTerm(StringTerm other) {
		super(other.getSort());
		set(other.get());
	}
	
	@Override
	public abstract void set(String value);

	@Override
	public String get() {
		return value;
	}
	
	@Override
	public String toString(){
		return this.value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringTerm other = (StringTerm) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.get()))
			return false;
		if(!other.getSort().equals(this.getSort()))
			return false;
		return true;
	}
}
