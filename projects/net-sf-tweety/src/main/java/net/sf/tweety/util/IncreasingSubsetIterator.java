package net.sf.tweety.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Iterates over all subsets of a given set. This iterator first returns the empty
 * set, then all 1-element subsets, then all 2-element subsets,... 
 * 
 * @author Matthias Thimm
 *
 * @param <T> The element class which is iterated.
 */
public class IncreasingSubsetIterator<T> extends SubsetIterator<T> {

	/** The actual set in a list. */
	private List<T> set;
	
	/** The indices of the generated subsets. */
	private int[] indices;
	
	/** The current size of the subsets generated. */
	private int currentSize;
	
	/** Creates a new subset iterator for the given set.
	 * @param set some set.
	 */
	public IncreasingSubsetIterator(Set<T> set) {
		super(set);
		this.set = new ArrayList<T>(set);
		this.indices = new int[set.size()];
		this.currentSize = 0;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.util.SubsetIterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return this.currentSize < this.set.size();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.util.SubsetIterator#next()
	 */
	@Override
	public Set<T> next() {		
		Set<T> result = new HashSet<T>();
		for(int i = 0; i < this.currentSize; i++){
			result.add(this.set.get(this.indices[i]));
		}
		this.increment();
		return result;
	}
	
	/**
	 * Increments the indices.
	 */
	private void increment(){
		if(this.currentSize == 0){
			this.currentSize = 1;
			this.indices[0] = 0;
		}else{
			//TODO go on
			
		}		
	}

}
