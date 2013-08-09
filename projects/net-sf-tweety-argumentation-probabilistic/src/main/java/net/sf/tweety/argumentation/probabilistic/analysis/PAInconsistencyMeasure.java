package net.sf.tweety.argumentation.probabilistic.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import net.sf.tweety.InconsistencyMeasure;
import net.sf.tweety.TweetyConfiguration;
import net.sf.tweety.TweetyLogging;
import net.sf.tweety.argumentation.dung.DungTheory;
import net.sf.tweety.argumentation.dung.syntax.Argument;
import net.sf.tweety.argumentation.dung.syntax.Attack;
import net.sf.tweety.argumentation.probabilistic.PartialProbabilityAssignment;
import net.sf.tweety.math.GeneralMathException;
import net.sf.tweety.math.equation.Equation;
import net.sf.tweety.math.equation.Inequation;
import net.sf.tweety.math.norm.RealVectorNorm;
import net.sf.tweety.math.opt.OptimizationProblem;
import net.sf.tweety.math.opt.solver.OpenOptSolver;
import net.sf.tweety.math.term.FloatConstant;
import net.sf.tweety.math.term.FloatVariable;
import net.sf.tweety.math.term.Term;
import net.sf.tweety.math.term.Variable;
import net.sf.tweety.util.SetTools;


/**
 * This inconsistency measure measures the distance between a given partial
 * probability assignment to the set of rational/justifiable probabilistic extensions
 * of a given Dung theory.
 * 
 * @author Matthias Thimm
 *
 */
public class PAInconsistencyMeasure implements InconsistencyMeasure<PartialProbabilityAssignment> {

	/** Measure the distance wrt. rational probability functions. */
	public static final int DISTANCE_WRT_RATIONALITY = 1;
	/** Measure the distance wrt. justifiable probability functions. */
	public static final int DISTANCE_WRT_JUSTIFIABILITY = 2;
	
	/** The norm used for measuring the distances. */
	private RealVectorNorm norm;
	/** The Dung theory against the partial prob assignments are measured. */
	private DungTheory dungTheory;
	/** The mode how distances are measured (either DISTANCE_WRT_RATIONALITY or DISTANCE_WRT_JUSTIFIABILITY). */
	private int mode;
	
	/**
	 * Creates a new inconsinstency measure which uses the given norm and
	 * measures wrt. the given theory.
	 * @param norm a norm
	 * @param theory a Dung theory
	 * @param mode the mode how distances are measured (either DISTANCE_WRT_RATIONALITY or DISTANCE_WRT_JUSTIFIABILITY).
	 */
	public PAInconsistencyMeasure(RealVectorNorm norm, DungTheory theory, int mode){
		if(mode != PAInconsistencyMeasure.DISTANCE_WRT_JUSTIFIABILITY &&
				mode != PAInconsistencyMeasure.DISTANCE_WRT_RATIONALITY)
			throw new IllegalArgumentException("Mode must be either DISTANCE_WRT_RATIONALITY or DISTANCE_WRT_JUSTIFIABILITY");
		this.mode = mode;
		this.norm = norm;
		this.dungTheory = theory;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.InconsistencyMeasure#inconsistencyMeasure(net.sf.tweety.BeliefBase)
	 */
	@Override
	public Double inconsistencyMeasure(PartialProbabilityAssignment ppa) {
		// construct optimization problem
		OptimizationProblem problem = new OptimizationProblem(OptimizationProblem.MINIMIZE);
		Set<Set<Argument>> configurations = new SetTools<Argument>().subsets(this.dungTheory);
		Map<Set<Argument>,Variable> vars = new HashMap<Set<Argument>,Variable>();
		Term normConstraint = null;
		for(Set<Argument> w: configurations){
			Variable var = new FloatVariable("w" + w.toString(),0,1);
			vars.put(w, var);
			if(normConstraint == null)
				normConstraint = var;
			else normConstraint = normConstraint.add(var);
		}
		problem.add(new Equation(normConstraint,new FloatConstant(1)));
		// add constraints imposed by coherence
		for(Attack att: this.dungTheory.getAttacks()){			
			Term leftSide = null;
			Term rightSide = new FloatConstant(1);
			for(Set<Argument> set: configurations){
				if(set.contains(att.getAttacked()))
					if(leftSide == null)
						leftSide = vars.get(set);
					else leftSide = leftSide.add(vars.get(set));
				if(set.contains(att.getAttacker()))
					rightSide = rightSide.minus(vars.get(set));
			}						
			problem.add(new Inequation(leftSide,rightSide,Inequation.LESS_EQUAL));			
		}		
		// add constraints imposed by justifiability (if needed)
		if(this.mode == PAInconsistencyMeasure.DISTANCE_WRT_JUSTIFIABILITY){
			for(Argument arg: this.dungTheory){			
				Term leftSide = null;
				Term rightSide = new FloatConstant(1);
				for(Set<Argument> set: configurations)
					if(set.contains(arg))
						if(leftSide == null)
							leftSide = vars.get(set);
						else leftSide = leftSide.add(vars.get(set));
				for(Argument attacker: this.dungTheory.getAttackers(arg))
					for(Set<Argument> set: configurations)
						if(set.contains(attacker))
							rightSide = rightSide.minus(vars.get(set));				
				problem.add(new Inequation(leftSide,rightSide,Inequation.GREATER_EQUAL));		
			}
		}
		// add constraints from partial probability assignment
		Map<Argument,Variable> ppaVars = new HashMap<Argument,Variable>();
		for(Argument arg: ppa.keySet()){
			Variable var = new FloatVariable("p" + arg.toString(),0,1);
			ppaVars.put(arg, var);
			Term leftSide = null;
			Term rightSide = var;
			for(Set<Argument> set: configurations)
				if(set.contains(arg))
					if(leftSide == null)
						leftSide = vars.get(set);
					else leftSide = leftSide.add(vars.get(set));
			problem.add(new Equation(leftSide,rightSide));
		}
		Vector<Term> ppaTerms = new Vector<Term>();
		Vector<Term> argTerms = new Vector<Term>();
		for(Argument arg: ppa.keySet()){
			ppaTerms.add(new FloatConstant(ppa.get(arg).doubleValue()));
			Term t = null;
			for(Set<Argument> set: configurations)
				if(set.contains(arg))
					if(t == null)
						t = vars.get(set);
					else t = t.add(vars.get(set));
			argTerms.add(t);
		}
		problem.setTargetFunction(this.norm.distanceTerm(ppaTerms, argTerms));
		TweetyLogging.logLevel = TweetyConfiguration.LogLevel.FATAL;
		TweetyLogging.initLogging();
		try{			
			OpenOptSolver solver = new OpenOptSolver(problem);
			solver.solver = "ralg";
			solver.contol = 0.001;
			Map<Variable,Term> solution = solver.solve();
			// construct probability distribution
			//ProbabilisticExtension p = new ProbabilisticExtension();
			//for(Set<Argument> w: configurations)
			//	p.put(new Extension(w), new Probability(solution.get(vars.get(w)).doubleValue()));
			return problem.getTargetFunction().replaceAllTerms(solution).value().doubleValue();
		}catch (GeneralMathException ex){
			// This should not happen as the optimization problem is guaranteed to be feasible
			throw new RuntimeException("Fatal error: Optimization problem to compute the inconsistency measure is not feasible.");
		}
	}

}
