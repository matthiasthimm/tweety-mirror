package net.sf.tweety.argumentation.probabilistic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.tweety.argumentation.dung.DungTheory;
import net.sf.tweety.argumentation.dung.syntax.Argument;
import net.sf.tweety.argumentation.dung.syntax.Attack;
import net.sf.tweety.math.probability.Probability;

/**
 * This class extends a Dung theory by adding partial probability assignments to arguments.
 * @author Matthias Thimm
 */
public class ProbabilisticDungTheory extends DungTheory {

	/** Partial probability assignments. */
	private Map<Argument,Probability> probAssignments = new HashMap<Argument,Probability>();
	
	/**
	 * Adds the given probability assignment, previous assignments for that
	 * argument are overwritten.
	 * @param arg some argument
	 * @param p some probability
	 * @return the previous probability of that argument or null. 
	 */
	public Probability addProbabilityAssignment(Argument arg, Probability p){
		return this.probAssignments.put(arg, p);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String output = new String();
		Iterator<Argument> it = this.iterator();
		Argument arg;
		while(it.hasNext()){
			arg = it.next();
			output += "argument("+arg.toString()+")";
			if(this.probAssignments.containsKey(arg))
				output += "[" + this.probAssignments.get(arg).doubleValue() +"]";
			output += ".\n";
		}
		output += "\n";
		Iterator<Attack> it2 = this.getAttacks().iterator();
		while(it2.hasNext())
			output += "attack"+it2.next().toString()+".\n";
		return output;
	}
}
