package net.sf.tweety.logics.firstorderlogic.analysis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.ParserException;
import net.sf.tweety.logics.firstorderlogic.FolBeliefSet;
import net.sf.tweety.logics.firstorderlogic.parser.FolParser;
import net.sf.tweety.logics.firstorderlogic.semantics.HerbrandBase;
import net.sf.tweety.logics.firstorderlogic.semantics.HerbrandInterpretation;
import net.sf.tweety.logics.firstorderlogic.syntax.*;
import net.sf.tweety.math.GeneralMathException;
import net.sf.tweety.math.equation.Equation;
import net.sf.tweety.math.equation.Inequation;
import net.sf.tweety.math.opt.OptimizationProblem;
import net.sf.tweety.math.opt.solver.LpSolve;
import net.sf.tweety.math.opt.solver.OpenOptSolver;
import net.sf.tweety.math.term.FloatConstant;
import net.sf.tweety.math.term.FloatVariable;
import net.sf.tweety.math.term.Logarithm;
import net.sf.tweety.math.term.Term;
import net.sf.tweety.math.term.Variable;

public class TestIncon {

	public static void main(String[] args) throws ParserException, IOException{
		FolSignature sig = new FolSignature();
		sig.add(new Constant("c1"));
		sig.add(new Constant("c2"));
		sig.add(new Predicate("S",1));
		sig.add(new Predicate("R",2));
		FolParser parser = new FolParser();
		parser.setSignature(sig);
		FolBeliefSet beliefSet = new FolBeliefSet();
		beliefSet.add((FolFormula)parser.parseFormula("S(c1)"));
		beliefSet.add((FolFormula)parser.parseFormula("S(c2)"));
		beliefSet.add((FolFormula)parser.parseFormula("R(c1,c2)"));
		// to realize closed-world assumption just add the rest as negation
		beliefSet.add((FolFormula)parser.parseFormula("!R(c1,c1)"));
		beliefSet.add((FolFormula)parser.parseFormula("!R(c2,c1)"));
		beliefSet.add((FolFormula)parser.parseFormula("!R(c2,c2)"));
		
		
		beliefSet.add((FolFormula)parser.parseFormula("forall X:(!S(X) || (exists Y:(R(X,Y))))"));
		//beliefSet.add((FolFormula)parser.parseFormula("forall X: (forall Y: (!R(X,Y) || R(Y,X)))"));
		
		System.out.println(beliefSet);
		System.out.println();
		//TestIncon.getMaxProb(beliefSet);
		TestIncon.getMaxEnt(beliefSet,0.745f);
	}
	
	public static void getMaxEnt(FolBeliefSet beliefSet, float p){
		OptimizationProblem problem = new OptimizationProblem(OptimizationProblem.MAXIMIZE);
		Set<HerbrandInterpretation> ints = new HerbrandBase((FolSignature)beliefSet.getSignature()).allHerbrandInterpretations();
		Map<HerbrandInterpretation,Variable> worlds2vars = new HashMap<HerbrandInterpretation,Variable>();
		int k = 1;
		Term t = null;
		for(HerbrandInterpretation i: ints){
			Variable v = new FloatVariable("a"+ k++, 0,1);
			worlds2vars.put(i, v);
			if(t == null)
				t = new FloatConstant(-1).mult(v.mult(new Logarithm(v)));
			else 
				t = t.minus(v.mult(new Logarithm(v)));
		}
		problem.setTargetFunction(t);
		t = null;
		for(Variable v: worlds2vars.values())
			if(t == null)
				t = v;
			else t = t.add(v);
		problem.add(new Equation(t,new FloatConstant(1)));
		for(FolFormula f: beliefSet){
			t = null;
			for(HerbrandInterpretation i: ints){
				if(i.satisfies(f)){
					if(t == null)
						t = worlds2vars.get(i);
					else t = t.add(worlds2vars.get(i));
				}				
			}
			problem.add(new Inequation(t,new FloatConstant(p),Inequation.GREATER_EQUAL));
		}
		System.out.println(problem);
		try{			
			OpenOptSolver solver = new OpenOptSolver(problem);
			solver.solver = "ralg";
			solver.python = "/usr/bin/python";
			Map<Variable,Term> solution = solver.solve();
			System.out.println();
			System.out.println(solution);	
			System.out.println(problem.getTargetFunction().replaceAllTerms(solution).doubleValue());
		}catch (Exception e){
			throw new RuntimeException(e);
		}	
	}
	
	
	public static void getMaxProb(FolBeliefSet beliefSet){		
		OptimizationProblem problem = new OptimizationProblem(OptimizationProblem.MAXIMIZE);
		Set<HerbrandInterpretation> ints = new HerbrandBase((FolSignature)beliefSet.getSignature()).allHerbrandInterpretations();
		Map<HerbrandInterpretation,Variable> worlds2vars = new HashMap<HerbrandInterpretation,Variable>();
		int k = 1;
		for(HerbrandInterpretation i: ints)
			worlds2vars.put(i, new FloatVariable("a"+ k++, 0,1));
		Variable p = new FloatVariable("p",0,1);
		problem.setTargetFunction(p);
		net.sf.tweety.math.term.Term t = null;
		for(Variable v: worlds2vars.values())
			if(t == null)
				t = v;
			else t = t.add(v);
		problem.add(new Equation(t,new FloatConstant(1)));
		for(FolFormula f: beliefSet){
			t = null;
			for(HerbrandInterpretation i: ints){
				if(i.satisfies(f)){
					if(t == null)
						t = worlds2vars.get(i);
					else t = t.add(worlds2vars.get(i));
				}				
			}
			problem.add(new Inequation(t,p,Inequation.GREATER_EQUAL));
		}
		System.out.println(problem);
		
		try{			
			LpSolve solver = new LpSolve(problem);
			Map<Variable,Term> solution = solver.solve();
			System.out.println();
			System.out.println(solution);	
			for(HerbrandInterpretation i: worlds2vars.keySet()){
				if(solution.get(worlds2vars.get(i)).doubleValue()>0)
					System.out.println(i + "\t" + solution.get(worlds2vars.get(i)).doubleValue());
			}
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}
}
