package net.sf.tweety.arg.dung.semantics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.arg.dung.DungTheory;
import net.sf.tweety.arg.dung.syntax.Argument;

/**
 * This class models a labeling of an abstract argumentation framework, i.e.
 * a function mapping arguments to values "in", "out", "undec".
 * 
 * @author Matthias Thimm
 */
public class Labeling extends AbstractArgumentationInterpretation implements Map<Argument,ArgumentStatus> {

	/** The actual labeling. */
	private Map<Argument,ArgumentStatus> labeling;
	
	/**
	 * Creates a new labeling.
	 */
	public Labeling(){
		this.labeling = new HashMap<Argument,ArgumentStatus>();
	}
	
	/**
	 * Creates a new labeling from the given extension wrt. the given theory (this only gives
	 * a valid labeling wrt. some semantics if the semantics is admissibility-based).
	 * @param theory some Dung theory.
	 * @param ext an extension
	 */
	public Labeling(DungTheory theory, Extension ext){
		this();
		for(Argument a: ext)
			this.labeling.put(a, ArgumentStatus.IN);
		if(!theory.containsAll(ext))
			throw new IllegalArgumentException("The arguments of the given extension are not all in the given theory.");
		Extension ext2 = new Extension();
		for(Argument a: theory){
			if(!ext.contains(a))
				if(theory.isAttacked(a, ext))
					ext2.add(a);
		}
		for(Argument a: ext2)
			this.labeling.put(a, ArgumentStatus.OUT);
		for(Argument a: theory)
			if(!this.labeling.containsKey(a))
				this.labeling.put(a, ArgumentStatus.UNDECIDED);	
	}
	
	/* (non-Javadoc)
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object arg0) {
		return this.labeling.containsKey(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(Object arg0) {
		return this.labeling.containsValue(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<java.util.Map.Entry<Argument, ArgumentStatus>> entrySet() {
		return this.labeling.entrySet();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public ArgumentStatus get(Object arg0) {
		return this.labeling.get(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<Argument> keySet() {
		return this.labeling.keySet();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public ArgumentStatus put(Argument arg0, ArgumentStatus arg1) {
		return this.labeling.put(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map<? extends Argument, ? extends ArgumentStatus> arg0) {
		this.labeling.putAll(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<ArgumentStatus> values() {
		return this.labeling.values();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		this.labeling.clear();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.labeling.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public ArgumentStatus remove(Object arg0) {
		return this.labeling.remove(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		return this.labeling.size();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.argumentation.dung.semantics.AbstractArgumentationInterpretation#getArgumentsOfStatus(net.sf.tweety.argumentation.dung.semantics.ArgumentStatus)
	 */
	@Override
	public Extension getArgumentsOfStatus(ArgumentStatus status) {
		Extension ext = new Extension();
		for(Argument a: this.labeling.keySet())
			if(this.labeling.get(a).equals(status))
				ext.add(a);
		return ext;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.argumentation.dung.semantics.AbstractArgumentationInterpretation#toString()
	 */
	@Override
	public String toString() {
		return this.labeling.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((labeling == null) ? 0 : labeling.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Labeling other = (Labeling) obj;
		if (labeling == null) {
			if (other.labeling != null)
				return false;
		} else if (!labeling.equals(other.labeling))
			return false;
		return true;
	}

}
