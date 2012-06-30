package net.sf.tweety.preferences;

/**
 * EXPERIMENTAL, MAYBE REMOVED AGAIN SOON
 * This class provides elements for preference orders containing their rank
 * The rank of a element represents its position within a preference order,
 * where a lower rank means a higher (better) position.
 * 
 * @author Bastian Wolf
 *
 * @param <T> the generic element type
 */

public class RankedElement<T> {

	/**
	 * the element itself
	 */
	private T element;
	
	/**
	 * the rank of the element
	 */
	private int rank;
	
	/**
	 * constructor w/ parameters
	 * @param element the element
	 * @param rank the rank
	 */
	public RankedElement(T element, int rank) {
		super();
		this.element = element;
		this.rank = rank;
	}
	
	/**
	 * returns the element itself
	 * @return the element
	 */
	public T getElement() {
		return this.element;
	}

	/**
	 * setter for element
	 * @param element
	 */
	public void setElement(T element) {
		this.element = element;
	}

	/**
	 * returns the rank of the element
	 * @return rank in int
	 */
	public int getRank() {
		return rank;
	}

	/**
	 * sets the rank
	 * @param rank given rank
	 */
	public void setRank(int rank) {
		this.rank = rank;
	}

	/**
	 * decrements the rank of the element
	 */
	public void decrementRank(){
		this.rank--;
	}
	
	/**
	 * increments the rank of the element
	 */
	public void incrementRank(){
		this.rank++;
	}
}
