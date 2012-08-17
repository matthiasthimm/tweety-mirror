package net.sf.tweety.testing;

import java.util.Map;

import net.sf.tweety.math.equation.Inequation;
import net.sf.tweety.math.opt.*;
import net.sf.tweety.math.opt.solver.*;
import net.sf.tweety.math.term.IntegerVariable;
import net.sf.tweety.math.term.Term;
import net.sf.tweety.math.term.Variable;


public class SolverTest {

	public static void main(String[] args) {
		IntegerVariable a = new IntegerVariable("a", true);
		IntegerVariable b = new IntegerVariable("b", true);
		IntegerVariable c = new IntegerVariable("c", true);
		IntegerVariable d = new IntegerVariable("d", true);
		IntegerVariable e = new IntegerVariable("e", true);
		IntegerVariable f = new IntegerVariable("f", true);
		
		OptimizationProblem opt = new OptimizationProblem(OptimizationProblem.MINIMIZE);
		
		opt.add(new Inequation(a, b, Inequation.LESS));
		opt.add(new Inequation(b, c, Inequation.LESS));
		opt.add(new Inequation(f, a, Inequation.LESS));
		opt.add(new Inequation(d, f, Inequation.LESS));
		opt.add(new Inequation(e, d, Inequation.LESS));
		
		Term target = a.add(b).add(c).add(d).add(e).add(f);
		opt.setTargetFunction(target);
		
		LpSolve solver = new LpSolve(opt);
		Map<Variable, Term> solution = solver.solve();
		System.out.println(solution);
	}
	
}
