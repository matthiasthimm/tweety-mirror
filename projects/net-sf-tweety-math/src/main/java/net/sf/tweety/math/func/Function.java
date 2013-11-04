package net.sf.tweety.math.func;

/**
 * Encapsulates common methods of mathematical functions.
 * 
 * @author Matthias Thimm
 * @param <T> The type of the domain
 * @param <S> The type of the co-domain
 */
public interface Function<T extends Object,S extends Object> {
	
	/**
	 * Evaluates the function for the given element.
	 * @param x some element
	 * @return the value of the element.
	 */
	public S eval(T x);
}
