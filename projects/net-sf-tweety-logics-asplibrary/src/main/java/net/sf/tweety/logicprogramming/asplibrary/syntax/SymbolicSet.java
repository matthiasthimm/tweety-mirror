package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.*;

import net.sf.tweety.logics.commons.syntax.Variable;

/**
 * This class is used to store symbolic sets of aggregate functions
 * of dlv rules. A symbolic set contains a non empty set of open
 * variables and a conjunction of elements. Comparative
 * and Arithmetics are allowed in the conjunction and also default 
 * negated and classic literals are allowed but the conjunction
 * is not allowed to hold another aggregate.
 *  
 * @author Tim Janus
 * @author Thomas Vengels
 *
 */
public class SymbolicSet {
	
	protected Set<Variable> openVariables = new HashSet<Variable>();
	protected Set<DLPElement> conjunctions = new HashSet<DLPElement>();
	
	public SymbolicSet(Collection<Variable> variables, Collection<DLPElement> literals ) {
		this.openVariables.addAll(variables);
		this.conjunctions.addAll(literals);
	}
	
	public SymbolicSet(SymbolicSet other) {
		this.openVariables = new HashSet<Variable>(other.openVariables);
		this.conjunctions = new HashSet<DLPElement>();
		for(DLPElement l : other.conjunctions) {
			conjunctions.add((DLPLiteral)l.clone());
		}
	}
	
	public Set<Variable> getVariables() {
		return Collections.unmodifiableSet(this.openVariables);
	}
	
	public Set<DLPElement> getConjunction() {
		return Collections.unmodifiableSet(this.conjunctions);
	}
	
	@Override
	public String toString() {
		String reval = "{";
		
		Iterator<Variable> sIter = this.openVariables.iterator();	
		String vars = "";
		while(sIter.hasNext()) {
			vars += ", "+sIter.next();
		}
		reval += vars.substring(2) + ":";	
		
		Iterator<DLPElement> lIter = conjunctions.iterator();
		String lits = "";
		while(lIter.hasNext()) {
			lits += ", " + lIter.next();
		}
		reval += lits.substring(2) + "}";
		
		return reval;
	}
	
	@Override
	public int hashCode() {
		return (openVariables.hashCode() + conjunctions.hashCode()) * 7;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof SymbolicSet))
			return false;
		SymbolicSet cother = (SymbolicSet)other;
		return openVariables.equals(cother.openVariables) &&
				conjunctions.equals(cother.conjunctions);
	}
	
	@Override
	public Object clone() {
		return new SymbolicSet(this);
	}
}
