package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.logics.commons.syntax.TermAdapter;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * this class models a list term that can be used for
 * dlv complex programs.
 * 
 * @author Tim Janus
 * @author Thomas Vengels
 *
 */
public class ListTerm extends TermAdapter<List<Term<?>>> {

	List<Term<?>> head = new LinkedList<Term<?>>();
	List<Term<?>> tail = new LinkedList<Term<?>>();
	
	public ListTerm() {
	}
	
	public ListTerm(List<Term<?>> head) {
		this.head.addAll(head);
	}
	
	public ListTerm(ListTerm other) {
		for(Term<?> t : other.head) {
			this.head.add((Term<?>)t.clone());
		}
		for(Term<?> t : other.tail) {
			this.tail.add((Term<?>)t.clone());
		}
	}
	
	/**
	 * constructor for list elements with given [head|tail].
	 * @param head
	 * @param tail
	 */
	public ListTerm(List<Term<?>> head, List<Term<?>> tail) {
		this.head.addAll(head);
		this.tail.addAll(tail);
	}

	@Override
	public void set(List<Term<?>> value) {
		// not supported
	}

	@Override
	public List<Term<?>> get() {
		List<Term<?>> reval = new LinkedList<Term<?>>();
		reval.addAll(head);
		reval.addAll(tail);
		return reval;
	}

	@Override
	public ListTerm clone() {
		return new ListTerm(this);
	}
	
	@Override
	public String toString() {
		// return list
		String ret = "[";
		boolean headEmpty = head.isEmpty();
		if (!headEmpty) {
			ret += listPrint(this.head);
		}
		if(!tail.isEmpty()) {
			ret += (!headEmpty ? "|" : "");
			ret += listPrint(this.tail);
		}
		ret += "]";
		return ret;
	}
	
	protected String listPrint(Collection<Term<?>> tl) {
		String ret = "";
		Iterator<Term<?>> iter = tl.iterator();
		if (iter.hasNext())
			ret += iter.next().toString();
		while (iter.hasNext())
			ret += ", " + iter.next().toString();
		return ret;
	}

	public List<Term<?>> head() {
		return Collections.unmodifiableList(head);
	}
	
	public List<Term<?>> tail() {
		return Collections.unmodifiableList(tail);
	}
}
