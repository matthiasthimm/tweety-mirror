package net.sf.tweety.arg.prob.analysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import net.sf.tweety.InconsistencyMeasure;
import net.sf.tweety.TweetyConfiguration;
import net.sf.tweety.TweetyLogging;
import net.sf.tweety.arg.dung.DungTheory;
import net.sf.tweety.arg.dung.semantics.Extension;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.prob.PartialProbabilityAssignment;
import net.sf.tweety.arg.prob.ProbabilisticExtension;
import net.sf.tweety.math.GeneralMathException;
import net.sf.tweety.math.equation.Equation;
import net.sf.tweety.math.equation.Inequation;
import net.sf.tweety.math.norm.RealVectorNorm;
import net.sf.tweety.math.opt.OptimizationProblem;
import net.sf.tweety.math.opt.solver.OpenOptSolver;
import net.sf.tweety.math.probability.Probability;
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
	
	/** Impose that the final probabilistic extension is rational. */
	public static final int IMPOSE_RATIONALITY = 3;
	/** Do not impose that the final probabilistic extension is rational. */
	public static final int NOT_IMPOSE_RATIONALITY = 4;
	
	/** The norm used for measuring the distances. */
	private RealVectorNorm norm;
	/** The Dung theory against the partial prob assignments are measured. */
	private DungTheory dungTheory;
	/** The mode how distances are measured (either DISTANCE_WRT_RATIONALITY or DISTANCE_WRT_JUSTIFIABILITY). */
	private int mode;
	/** Impose that the final probabilistic extension is rational. */
	private int impose;
	
	/**
	 * Creates a new inconsinstency measure which uses the given norm and
	 * measures wrt. the given theory.
	 * @param norm a norm
	 * @param theory a Dung theory
	 * @param mode the mode how distances are measured (either DISTANCE_WRT_RATIONALITY or DISTANCE_WRT_JUSTIFIABILITY).
	 * @param impose whether to impose that the final probabilistic extension is rational (might not have a solution).
	 */
	public PAInconsistencyMeasure(RealVectorNorm norm, DungTheory theory, int mode, int impose){
		if(mode != PAInconsistencyMeasure.DISTANCE_WRT_JUSTIFIABILITY &&
				mode != PAInconsistencyMeasure.DISTANCE_WRT_RATIONALITY)
			throw new IllegalArgumentException("Mode must be either DISTANCE_WRT_RATIONALITY or DISTANCE_WRT_JUSTIFIABILITY");
		if(impose != PAInconsistencyMeasure.IMPOSE_RATIONALITY &&
				impose != PAInconsistencyMeasure.NOT_IMPOSE_RATIONALITY)
			throw new IllegalArgumentException("Impose must be either IMPOSE_RATIONALITY or NOT_IMPOSE_RATIONALITY");
		this.mode = mode;
		this.impose = impose;
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
		// add constraints from partial probability assignment
		for(Argument arg: ppa.keySet()){
			Term leftSide = new FloatConstant(ppa.get(arg).doubleValue());
			Term rightSide = null;
			for(Set<Argument> set: configurations)
				if(set.contains(arg))
					if(rightSide == null)
						rightSide = vars.get(set);
					else rightSide = rightSide.add(vars.get(set));
			problem.add(new Equation(leftSide,rightSide));
		}
		// add constraints imposed by coherence
		Vector<Term> targetVars = new Vector<Term>();
		int cnt = 0;
		for(Attack att: this.dungTheory.getAttacks()){
			Term leftSide = new FloatConstant(-1);
			Term rightSide =  null;
			if(this.impose == PAInconsistencyMeasure.NOT_IMPOSE_RATIONALITY){
				Variable var = new FloatVariable("d"+cnt++,0,1);
				targetVars.add(var);
				rightSide =  var;
			}else rightSide = new FloatConstant(0);
			for(Set<Argument> set: configurations){
				if(set.contains(att.getAttacked()))
					leftSide = leftSide.add(vars.get(set));
				if(set.contains(att.getAttacker()))
					leftSide = leftSide.add(vars.get(set));
			}						
			problem.add(new Inequation(leftSide,rightSide,Inequation.LESS_EQUAL));			
		}		
		// add constraints imposed by justifiability (if needed)
		if(this.mode == PAInconsistencyMeasure.DISTANCE_WRT_JUSTIFIABILITY){
			for(Argument arg: this.dungTheory){			
				Variable var = new FloatVariable("d"+cnt++,0,1);
				targetVars.add(var);
				Term leftSide = var;
				Term rightSide = new FloatConstant(1);
				for(Set<Argument> set: configurations)
					if(set.contains(arg))
						leftSide = leftSide.add(vars.get(set));
				for(Argument attacker: this.dungTheory.getAttackers(arg))
					for(Set<Argument> set: configurations)
						if(set.contains(attacker))
							rightSide = rightSide.minus(vars.get(set));		
				//if(!this.dungTheory.getAttackers(arg).isEmpty())
				problem.add(new Inequation(leftSide,rightSide,Inequation.GREATER_EQUAL));		
			}
		}
		// Target function
		problem.setTargetFunction(this.norm.normTerm(targetVars));
		TweetyLogging.logLevel = TweetyConfiguration.LogLevel.FATAL;
		TweetyLogging.initLogging();
		//System.out.println(problem);System.exit(0);
		try{			
			OpenOptSolver solver = new OpenOptSolver(problem);
			solver.solver = "ralg";
			solver.contol = 0.001;
			Map<Variable,Term> solution = solver.solve();
			// construct probability distribution
			ProbabilisticExtension p = new ProbabilisticExtension();
			for(Set<Argument> w: configurations)
				p.put(new Extension(w), new Probability(solution.get(vars.get(w)).doubleValue()));
			System.out.println(p);
			for(Argument a: this.dungTheory)
				System.out.println(a + "\t" + p.probability(a));
	
			return problem.getTargetFunction().replaceAllTerms(solution).value().doubleValue();
		}catch (GeneralMathException ex){
			// This should not happen as the optimization problem is guaranteed to be feasible
			throw new RuntimeException("Fatal error: Optimization problem to compute the inconsistency measure is not feasible.");
		}
	}

}
