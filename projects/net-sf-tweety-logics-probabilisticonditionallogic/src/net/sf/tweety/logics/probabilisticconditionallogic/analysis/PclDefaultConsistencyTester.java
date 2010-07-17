package net.sf.tweety.logics.probabilisticconditionallogic.analysis;

import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.logics.probabilisticconditionallogic.*;
import net.sf.tweety.logics.probabilisticconditionallogic.syntax.*;
import net.sf.tweety.logics.propositionallogic.semantics.*;
import net.sf.tweety.logics.propositionallogic.syntax.*;
import net.sf.tweety.math.*;
import net.sf.tweety.math.opt.*;
import net.sf.tweety.math.term.*;


/**
 * This class is capable of checking whether a given conditional knowledge base
 * is consistent by searching for the root of some equivalent multi-dimensional function.
 * 
 * @author Matthias Thimm
 */
public class PclDefaultConsistencyTester extends BeliefSetConsistencyTester {
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.ConsistencyTester#isConsistent(net.sf.tweety.kr.BeliefBase)
	 */
	@Override
	public boolean isConsistent(BeliefBase beliefBase) {
		if(!(beliefBase instanceof PclBeliefSet))
			throw new IllegalArgumentException("Expected belief base of class 'PclBeliefSet'.");
		PclBeliefSet beliefSet = (PclBeliefSet) beliefBase;
		// Create variables for the probability of each possible world and
		// create a multi-dimensional function that has a root iff the belief base is consistent
		List<Term> functions = new ArrayList<Term>();
		Set<PossibleWorld> worlds = PossibleWorld.getAllPossibleWorlds((PropositionalSignature)beliefSet.getSignature());
		Map<PossibleWorld,Variable> worlds2vars = new HashMap<PossibleWorld,Variable>();
		int i = 0;
		Term normConstraint = null;
		for(PossibleWorld w: worlds){
			FloatVariable var = new FloatVariable("w" + i++,0,1);
			worlds2vars.put(w, var);
			if(normConstraint == null)
				normConstraint = var;
			else normConstraint = normConstraint.add(var);
		}
		normConstraint = normConstraint.add(new IntegerConstant(-1));
		functions.add(normConstraint);
		// add constraints implied by the conditionals
		for(ProbabilisticConditional c: beliefSet){
			Term leftSide = null;
			Term rightSide = null;
			if(c.isFact()){
				for(PossibleWorld w: worlds)
					if(w.satisfies(c.getConclusion())){
						if(leftSide == null)
							leftSide = worlds2vars.get(w);
						else leftSide = leftSide.add(worlds2vars.get(w));
					}
				rightSide = new FloatConstant(c.getProbability().getValue());
			}else{				
				PropositionalFormula body = c.getPremise().iterator().next();
				PropositionalFormula head_and_body = (PropositionalFormula) c.getConclusion().combineWithAnd(body);
				for(PossibleWorld w: worlds){
					if(w.satisfies(head_and_body)){
						if(leftSide == null)
							leftSide = worlds2vars.get(w);
						else leftSide = leftSide.add(worlds2vars.get(w));
					}
					if(w.satisfies(body)){
						if(rightSide == null)
							rightSide = worlds2vars.get(w);
						else rightSide = rightSide.add(worlds2vars.get(w));
					}					
				}
				if(rightSide == null)
					rightSide = new FloatConstant(0);
				else rightSide = rightSide.mult(new FloatConstant(c.getProbability().getValue()));
			}
			functions.add(leftSide.minus(rightSide));			
		}
		// Search for a root of "functions" using OpenOpt
		Map<Variable,Term> startingPoint = new HashMap<Variable,Term>();
		for(PossibleWorld w: worlds)
			startingPoint.put(worlds2vars.get(w), new IntegerConstant(1));
		RootFinder rootFinder = new GradientDescentRootFinder(functions,startingPoint);
		try {
			rootFinder.randomRoot();
		} catch (GeneralMathException e) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.BeliefSetConsistencyTester#isConsistent(java.util.Set)
	 */
	@Override
	public boolean isConsistent(Set<? extends Formula> beliefs) {
		PclBeliefSet beliefSet = new PclBeliefSet();
		for(Formula f: beliefs){
			if(!(f instanceof ProbabilisticConditional))
				throw new IllegalArgumentException("Formula of type probabilistic conditional expected.");
			beliefSet.add((ProbabilisticConditional)f);
		}
		return this.isConsistent(beliefSet);
	}

}
