package net.sf.tweety.preferences.ranking;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.tweety.math.equation.Inequation;
import net.sf.tweety.math.opt.*;
import net.sf.tweety.math.opt.solver.*;
import net.sf.tweety.math.term.IntegerVariable;
import net.sf.tweety.math.term.Term;
import net.sf.tweety.math.term.Variable;
import net.sf.tweety.preferences.PreferenceOrder;
import net.sf.tweety.util.Pair;

/**
 * UNDER CONSTRUCTION This class is meant to provide ranking functions to given
 * preference orders and vice versa. To be implemented. A ranking function
 * characterizes a preference order uniquely as: 1.: rank: O -> N+ where O is
 * the set of elements in the preference order. 2.: the sum of all ranks for
 * each element in O is minimal
 * 
 * @author Bastian Wolf
 * 
 */

public class RankingFunction<T> {

	/**
	 * the map containing the ranking function
	 */
	Map<Variable, Term> rankingFunction;

	/**
	 * empty constructor
	 */
	public RankingFunction() {

	}

	//TODO: Fixing error occurring w/ more than 4 elements
	
	/**
	 * method for generating the ranking function
	 * 
	 * @param po
	 *            the given ranking function
	 */
	public void generateRankingFunction(PreferenceOrder<T> po) {

		List<IntegerVariable> integerVariables = new LinkedList<IntegerVariable>();

		for (final T e : po.getSingleElements()) {
			integerVariables.add(new IntegerVariable(e.toString(), true));
		}

		OptimizationProblem opt = new OptimizationProblem(
				OptimizationProblem.MINIMIZE);

		Iterator<Pair<T, T>> it = po.iterator();

		while (it.hasNext()) {

			Pair<T, T> temp = it.next();
			
			Iterator<IntegerVariable> integerIt = integerVariables.iterator();
			IntegerVariable tempVarF = null;
			IntegerVariable tempVarS = null;
			while (integerIt.hasNext()) {

				IntegerVariable tempvar = integerIt.next();

				if (temp.getFirst().toString().equals(tempvar.toString())) {
					tempVarF = tempvar;
				}

				if (temp.getSecond().toString().equals(tempvar.toString())) {
					tempVarS = tempvar;
				}
				if (tempVarF != null && tempVarS != null) {
					opt.add(new Inequation(tempVarF, tempVarS, Inequation.LESS));
					continue;
				}
			}
		}

		Term target;
		Iterator<IntegerVariable> termIt = integerVariables.listIterator();

		if (termIt.hasNext()) {
			target = termIt.next();

			while (termIt.hasNext()) {
				target.add(termIt.next());
			}
		

		opt.setTargetFunction(target);
		}
		LpSolve solver = new LpSolve(opt);

		rankingFunction = solver.solve();
	}

	/**
	 * prints the ranking function
	 */
	public void printRankingFunction() {
		System.out.println(rankingFunction);
	}

	/**
	 * returns the ranking function
	 * 
	 * @return ranking function
	 */
	public Map<Variable, Term> getRankingFunction() {
		return rankingFunction;
	}
}
