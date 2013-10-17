package net.sf.tweety.logics.relationalprobabilisticconditionallogic;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.util.MapTools;


/**
 * Instances of this class represent assignenment of true instances for a given
 * predicate wrt. to a set of constants.
 *  
 * @author Matthias Thimm
 */
public class InstanceAssignment extends HashMap<Collection<? extends Constant>,Integer> {

	/**
	 * For serialization.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The predicate of this instance assignment. 
	 */
	private Predicate predicate;
	
	/**
	 * Creates a new instance assignment for the given predicate with
	 * no assignments.
	 * @param predicate a predicate.
	 */
	public InstanceAssignment(Predicate predicate){
		this.predicate = predicate;
	}
	
	/**
	 * Creates a new instance assignment for the given predicate
	 * with the given map.
	 * @param predicate a predicate.
	 * @param map a map mapping sets of constants to integers.
	 */
	public InstanceAssignment(Predicate predicate, Map<? extends Collection<? extends Constant>,Integer> map){
		this(predicate);
		for(Collection<? extends Constant> key: map.keySet())
			this.put(key, map.get(key));
	}

	/**
	 * Returns the predicate of this assignment.
	 * @return the predicate of this assignment.
	 */
	public Predicate getPredicate(){
		return this.predicate;
	}
	
	/* (non-Javadoc)
	 * @see java.util.HashMap#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Integer put(Collection<? extends Constant> key, Integer value){
		if(key.size() < value)
			throw new IllegalArgumentException("Value must be less or equal the number of given constants.");
		return super.put(key, value);
	}
	
	/**
	 * Returns the number of constants assigned by this assignment,
	 * i.e. the sum of the values in this map.
	 * @return the number of constants assigned by this assignment.
	 */
	public int numberOfConstants(){
		Integer sum = 0;
		for(Integer i: this.values())
			sum += i;
		return sum;
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractMap#toString()
	 */
	@Override
	public String toString(){
		String result = "<" + this.predicate.toString() + ", {";
		boolean first1 = true;
		for(Collection<? extends Constant> constants: this.keySet()){
			String constantString = "{";
			boolean first2 = true;
			for(Constant c: constants)
				if(first2){
					constantString += c.toString();
					first2 = false;
				}else constantString += "," + c.toString();
			constantString += "}";
			if(first1){
				result += constantString + " = " + this.get(constants); 
				first1 = false;
			}else {
				result += "," + constantString + " = " + this.get(constants);
			}
		}
		result += "}>";
		return result;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((predicate == null) ? 0 : predicate.hashCode());
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
		InstanceAssignment other = (InstanceAssignment) obj;
		if (predicate == null) {
			if (other.predicate != null)
				return false;
		} else if (!predicate.equals(other.predicate))
			return false;
		return true;
	}

	/**
	 * Determines the set of all instance assignments for the given predicate and 
	 * equivalence classes.
	 * @param p a predicate.
	 * @param constants a set of constants.
	 * @return a set of instance assignments.
	 */
	public static Set<InstanceAssignment> enumerateInstanceAssignments(Predicate p, Set<Set<Constant>> constants){
		Set<InstanceAssignment> ias = new HashSet<InstanceAssignment>();
		Map<Set<Set<Constant>>,Set<Integer>> tmp = new HashMap<Set<Set<Constant>>,Set<Integer>>();
		for(Set<Constant> c: constants){
			Set<Set<Constant>> cTmp = new HashSet<Set<Constant>>();
			cTmp.add(c);
			Set<Integer> ints = new HashSet<Integer>();
			for(int i = 0; i <= c.size(); i++)
				ints.add(new Integer(i));
			tmp.put(cTmp, ints);
		}
		Set<Map<Set<Constant>,Integer>> assignments = new MapTools<Set<Constant>,Integer>().allMaps(tmp);
		for(Map<Set<Constant>,Integer> elem: assignments)
			ias.add(new InstanceAssignment(p,elem));
		return ias;
	}
	
}
