package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * this interfaces defines common functionality for
 * terms.
 * 
 * @author Thomas Vengels
 *
 */
public interface Term {

	public boolean isConstant();
	
	public boolean isVariable();
	
	public boolean isAtom();
	
	public boolean isList();
	
	public boolean isSet();
	
	public boolean isNumber();

	/**
	 * string value term manipulation
	 * @param value
	 */
	public void set(String value);
	
	public String get();
	
	/**
	 * integer number value manipulation
	 * @param value
	 */
	public void set(int value);
		
	public int getInt();
		
}