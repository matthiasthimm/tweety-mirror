package edu.cs.ai.kr.rpcl;

import java.util.*;

import edu.cs.ai.kr.Answer;
import edu.cs.ai.kr.BeliefBase;
import edu.cs.ai.kr.Formula;
import edu.cs.ai.kr.Reasoner;
import edu.cs.ai.kr.fol.semantics.*;
import edu.cs.ai.kr.fol.syntax.*;
import edu.cs.ai.kr.fol.syntax.Constant;
import edu.cs.ai.kr.rcl.syntax.RelationalConditional;
import edu.cs.ai.kr.rpcl.semantics.*;
import edu.cs.ai.kr.rpcl.syntax.*;
import edu.cs.ai.math.*;
import edu.cs.ai.math.equation.*;
import edu.cs.ai.math.opt.*;
import edu.cs.ai.math.opt.solver.*;
import edu.cs.ai.math.term.*;
import edu.cs.ai.math.term.Term;
import edu.cs.ai.math.term.Variable;
import edu.cs.ai.util.Probability;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * General ME-reasoner for RPCL.
 * 
 * @author Matthias Thimm
 */
public class RpclMeReasoner extends Reasoner {
	
	/**
	 * Logger.
	 */
	private Log log = LogFactory.getLog(RpclMeReasoner.class);
	
	/**
	 * Integer constant for standard inference.
	 */
	public static final int STANDARD_INFERENCE = 1;
	
	/**
	 * Integer constant for lifted inference.
	 */
	public static final int LIFTED_INFERENCE = 2;

	/**
	 * The semantics used for this reasoner.
	 */
	private RpclSemantics semantics;
	
	/**
	 * The signature for this reasoner.
	 */
	private FolSignature signature;
	
	/**
	 * The ME-distribution for this reasoner.
	 */
	private ProbabilityDistribution meDistribution;
	
	/**
	 * Whether this reasoner should use lifted inference for reasoning.
	 */
	private int inferenceType;
	
	/**
	 * Creates a new reasoner for the given knowledge base.
	 * @param beliefBase a knowledge base.
	 * @param semantics the semantics for this reasoner.
	 * @param signature the fol signature for this reasoner.
	 * @param inferenceType one of RpclMeReasoner.STANDARD_INFERENCE or RpclMeReasoner.LIFTED_INFERENCE 
	 */
	public RpclMeReasoner(BeliefBase beliefBase, RpclSemantics semantics, FolSignature signature, int inferenceType){
		super(beliefBase);
		this.log.trace("Creating instance of 'RpclMeReasoner'.");
		if(inferenceType != RpclMeReasoner.STANDARD_INFERENCE && inferenceType != RpclMeReasoner.LIFTED_INFERENCE){
			this.log.error("The inference type must be either 'standard' or 'lifted'.");
			throw new IllegalArgumentException("The inference type must be either 'standard' or 'lifted'.");
		}
		this.signature = signature;
		this.semantics = semantics;
		this.inferenceType = inferenceType;
		if(!(beliefBase instanceof RpclBeliefSet)){
			this.log.error("Knowledge base of class 'RpclBeliefSet' expected but encountered '" + beliefBase.getClass() + "'.");
			throw new IllegalArgumentException("Knowledge base of class 'RpclBeliefSet' expected but encountered '" + beliefBase.getClass() + "'.");
		}
		RpclBeliefSet beliefSet = (RpclBeliefSet) beliefBase;
		if(!beliefSet.getSignature().isSubSignature(signature)){
			this.log.error("Signature must be super-signature of the belief set's signature.");
			throw new IllegalArgumentException("Signature must be super-signature of the belief set's signature.");
		}
		if(inferenceType == RpclMeReasoner.LIFTED_INFERENCE)
			for(Predicate p: ((FolSignature)beliefSet.getSignature()).getPredicates())
				if(p.getArity()>1){
					this.log.error("Lifted inference only applicable for signatures containing only unary predicates.");
					throw new IllegalArgumentException("Lifted inference only applicable for signatures containing only unary predicates.");
				}
		this.log.trace("Finished creating instance of 'RpclReasoner'.");
	}
	
	/**
	 * Creates a new reasoner for the given knowledge base.
	 * @param beliefBase a knowledge base.
	 * @param semantics the semantics for this reasoner.
	 * @param signature the fol signature for this reasoner.
	 */
	public RpclMeReasoner(BeliefBase beliefBase, RpclSemantics semantics, FolSignature signature){
		this(beliefBase,semantics,signature,RpclMeReasoner.STANDARD_INFERENCE);
	}
	
	/**
	 * Returns the inference type of this reasoner, i.e. one of
	 * RpclMeReasoner.STANDARD_INFERENCE or RpclMeReasoner.LIFTED_INFERENCE 
	 * @return the inference type of this reasoner.
	 */
	public int getInferenceType(){
		return this.inferenceType;
	}
	
	/**
	 * Returns the ME-distribution this reasoner bases on.
	 * @return the ME-distribution this reasoner bases on.
	 */
	public ProbabilityDistribution getMeDistribution() throws ProblemInconsistentException{
		if(this.meDistribution == null)
			this.meDistribution = this.computeMeDistribution();
		return this.meDistribution;		
	}

	/**
	 * Computes the ME-distribution for this reasoner's knowledge base. 
	 * @return the ME-distribution for this reasoner's knowledge base.
	 */	
	private ProbabilityDistribution computeMeDistribution() throws ProblemInconsistentException{		
		RpclBeliefSet kb = ((RpclBeliefSet)this.getKnowledgBase());
		this.log.info("Computing ME-distribution for the knowledge base " + kb.toString() + ".");
		// TODO extract common parts from the following if/else
		this.log.info("Constructing optimization problem for finding the ME-distribution.");
		if(this.inferenceType == RpclMeReasoner.LIFTED_INFERENCE){
			// determine equivalence classes of the knowledge base
			Set<Set<Constant>> equivalenceClasses = kb.getEquivalenceClasses(this.getSignature());
			// determine the reference worlds needed to represent a probability distribution on the knowledge base.
			Set<ReferenceWorld> worlds = ReferenceWorld.enumerateReferenceWorlds(this.getSignature().getPredicates(), equivalenceClasses);
			int numberOfInterpretations = 0;
			for(ReferenceWorld w: worlds)
				numberOfInterpretations += w.spanNumber();
			// Generate Variables for the probability of each reference world,
			// range constraints for probabilities, and construct normalization sum
			Map<ReferenceWorld,FloatVariable> worlds2vars = new HashMap<ReferenceWorld,FloatVariable>();
			// check for empty kb
			if(kb.size() == 0)
				return CondensedProbabilityDistribution.getUniformDistribution(this.semantics, this.getSignature(), equivalenceClasses);
			int i=0;
			// We first construct the necessary constraints for the optimization problem
			Set<Statement> constraints = new HashSet<Statement>();
			Term normalization_sum = null;
			for(ReferenceWorld world: worlds){
				// variables representing probabilities should be in [0,1]
				FloatVariable v = new FloatVariable("X"+i++,0,1);
				worlds2vars.put(world, v);			
				// add term for normalization sum
				Term t = new FloatConstant(world.spanNumber()).mult(v);
				if(normalization_sum == null)
					normalization_sum = t;
				else normalization_sum = normalization_sum.add(t);
			}
			// add normalization constraint for probabilities
			Statement norm = new Equation(normalization_sum,new FloatConstant(1));
			constraints.add(norm);
			//for each conditional, add the corresponding constraint		
			// TODO remove conditionals with probability 0 or 1		
			for(RelationalProbabilisticConditional r: kb)
				constraints.add(this.semantics.getSatisfactionStatement(r, this.signature, worlds2vars));	
			// optimize for entropy
			OptimizationProblem problem = new OptimizationProblem(OptimizationProblem.MAXIMIZE);
			problem.addAll(constraints);
			Term targetFunction = null;
			for(ReferenceWorld w: worlds2vars.keySet()){
				Term t = new IntegerConstant(-w.spanNumber()).mult(worlds2vars.get(w).mult(new Logarithm(worlds2vars.get(w))));
				if(targetFunction == null)
					targetFunction = t;
				else targetFunction = targetFunction.add(t);
			}
			problem.setTargetFunction(targetFunction);			
			try{
				this.log.info("Applying the OpenOpt optimization library to find the ME-distribution.");
				Solver solver = new OpenOptSolver(problem,this.getFeasibleStartingPoint(problem));
				Map<Variable,Term> solution = solver.solve();				
				CondensedProbabilityDistribution p = new CondensedProbabilityDistribution(this.semantics,this.getSignature());
				for(ReferenceWorld w: worlds2vars.keySet()){
					edu.cs.ai.math.term.Constant c = solution.get(worlds2vars.get(w)).value();
					Double value = new Double(c.doubleValue());
					p.put(w, new Probability(value));			
				}
				return p;
			}catch(GeneralMathException e){
				this.log.error("The knowledge base " + kb + " is inconsistent.");
				throw new ProblemInconsistentException();				
			}
		}else{
			// get interpretations
			Set<HerbrandInterpretation> worlds = new HerbrandBase(this.signature).allHerbrandInterpretations();
			// Generate Variables for the probability of each world,
			// range constraints for probabilities, and construct normalization sum
			Map<HerbrandInterpretation,FloatVariable> worlds2vars = new HashMap<HerbrandInterpretation,FloatVariable>();
			// check for empty kb
			if(kb.size() == 0)
				return ProbabilityDistribution.getUniformDistribution(this.semantics, this.getSignature());
			int i=0;
			// We first construct the necessary constraints for the optimization problem
			Set<Statement> constraints = new HashSet<Statement>();
			Term normalization_sum = null;
			for(HerbrandInterpretation world: worlds){
				// variables representing probabilities should be in [0,1]
				FloatVariable v = new FloatVariable("X"+i++,0,1);
				worlds2vars.put(world, v);			
				if(normalization_sum == null)
					normalization_sum = v;
				else normalization_sum = normalization_sum.add(v);
			}
			// add normalization constraint for probabilities
			Statement norm = new Equation(normalization_sum,new FloatConstant(1));
			constraints.add(norm);
			//for each conditional, add the corresponding constraint		
			// TODO remove conditionals with probability 0 or 1		
			for(RelationalProbabilisticConditional r: kb)
				constraints.add(this.semantics.getSatisfactionStatement(r, this.signature, worlds2vars));	
			// optimize for entropy
			OptimizationProblem problem = new OptimizationProblem(OptimizationProblem.MAXIMIZE);
			problem.addAll(constraints);
			Term targetFunction = null;
			for(HerbrandInterpretation w: worlds2vars.keySet()){
				Term t = new IntegerConstant(-1).mult(worlds2vars.get(w).mult(new Logarithm(worlds2vars.get(w))));
				if(targetFunction == null)
					targetFunction = t;
				else targetFunction = targetFunction.add(t);
			}
			problem.setTargetFunction(targetFunction);			
			try{
				this.log.info("Applying the OpenOpt optimization library to find the ME-distribution.");
				Solver solver = new OpenOptSolver(problem,this.getFeasibleStartingPoint(problem));
				Map<Variable,Term> solution = solver.solve();
				ProbabilityDistribution p = new ProbabilityDistribution(this.semantics,this.getSignature());
				for(HerbrandInterpretation w: worlds2vars.keySet()){
					edu.cs.ai.math.term.Constant c = solution.get(worlds2vars.get(w)).value();
					Double value = new Double(c.doubleValue());
					p.put(w, new Probability(value));			
				}
				return p;
			}catch(GeneralMathException e){
				this.log.error("The knowledge base " + kb + " is inconsistent.");
				throw new ProblemInconsistentException();				
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.Reasoner#query(edu.cs.ai.kr.Formula)
	 */
	@Override
	public Answer query(Formula query) {
		if(!(query instanceof RelationalConditional) && !(query instanceof FolFormula))
			throw new IllegalArgumentException("Reasoning in relational probabilistic conditional logic is only defined for conditional and first-order queries.");
		ProbabilityDistribution meDistribution = this.getMeDistribution();
		RelationalConditional re;
		if(query instanceof FolFormula)
			re = new RelationalConditional((FolFormula)query);
		else re = (RelationalConditional) query;
		Probability prob = meDistribution.probability(re);
		Answer answer = new Answer(this.getKnowledgBase(),query);			
		answer.setAnswer(prob.getValue());
		answer.appendText("The probability of the query is " + prob + ".");
		return answer;		
	}
	
	/**
	 * Finds a feasible starting point for the given optimization problem.
	 * @param problem an optimization problem
	 * @return a feasible starting point.
	 * @throws GeneralMathException iff something went wrong.
	 */
	private Map<Variable,Term> getFeasibleStartingPoint(OptimizationProblem problem) throws GeneralMathException{
		this.log.info("Determining feasible starting point for optimizing entropy.");
		ConstraintSatisfactionProblem csp = new ConstraintSatisfactionProblem();
		csp.addAll(problem);
		if(csp.isLinear()){
			this.log.info("The problem is linear, we use a simplex algorithm.");
			return new ApacheCommonsSimplex(csp).solve();
		}else{
			this.log.info("The problem is not linear, we use the OpenOpt optimization library.");			
			Map<Variable,Term> startingPoint = new HashMap<Variable,Term>();
			for(Variable v: problem.getVariables())
				startingPoint.put(v, new FloatConstant(1));
			List<Term> functions = new ArrayList<Term>();
			//every s is an equation
			for(Statement s: problem)
				functions.add(s.toNormalizedForm().getLeftTerm());
			RootFinder rootFinder = new OpenOptRootFinder(functions,startingPoint);	
			return rootFinder.randomRoot();			
		}		
	}

	/**
	 * Returns the signature of this reasoner.
	 * @return the signature of this reasoner.
	 */
	public FolSignature getSignature(){
		return this.signature;
	}	
}
