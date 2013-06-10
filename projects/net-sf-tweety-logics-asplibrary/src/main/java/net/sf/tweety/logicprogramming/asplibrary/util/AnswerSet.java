package net.sf.tweety.logicprogramming.asplibrary.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.BeliefSet;
import net.sf.tweety.Signature;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPLiteral;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPSignature;

/**
 * A answer set is a belief set which only contains literals
 * and represents the deductive belief set of an extended logic
 * program under the answer set semantic.
 * 
 * @author Tim Janus
 */
public class AnswerSet extends BeliefSet<DLPLiteral> {
	public final int level;
	public final int weight;	
	
	public AnswerSet() {
		level = 0;
		weight = 1;
	}
	
	public AnswerSet(Collection<DLPLiteral> lits, int level, int weight) {
		super(lits);
		this.level = level;
		this.weight = weight;
	}
	
	public AnswerSet(AnswerSet other) {
		super(other);
		this.level = other.level;
		this.weight = other.weight;
	}
	
	public Set<DLPLiteral> getLiteralsWithName(String name) {
		Set<DLPLiteral> reval = new HashSet<DLPLiteral>();
		for(DLPLiteral lit : this) {
			if(lit.getName().equals(name)) {
				reval.add(lit);
			}
		}
		return reval;
	}
	
	public Set<String> getFunctors() {
		Set<String> reval = new HashSet<String>();
		return reval;
	}
	
	@Override
	public String toString() {
		return super.toString() + " ["+level+","+weight+"]";
	}
	
	@Override
	public Object clone() {
		return new AnswerSet(this);
	}

	@Override
	public Signature getSignature() {
		DLPSignature reval = new DLPSignature();
		for(DLPLiteral lit : this) {
			reval.addSignature(lit.getSignature());
		}
		return reval;
	}
}
