package net.sf.tweety.argumentation.delp;

import java.io.*;
import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.argumentation.delp.semantics.*;
import net.sf.tweety.argumentation.delp.syntax.*;
import net.sf.tweety.argumentation.dung.*;
import net.sf.tweety.argumentation.dung.syntax.*;
import net.sf.tweety.logics.firstorderlogic.syntax.*;
import net.sf.tweety.util.rules.*;

/**
 * This class models a defeasible logic program (DeLP).
 *
 * @author Matthias Thimm
 *
 */
public class DefeasibleLogicProgram extends BeliefSet<DelpRule>{

	/**
	 * Default constructor; initializes empty delpFacts, strict and defeasible rules
	 * and empty comparison criterion.
	 */
	public DefeasibleLogicProgram(){
		super();		
	}

	/**
	 * constructor; initializes this program with the given program
	 * @param delp a defeasible logic program
	 */
	public DefeasibleLogicProgram(DefeasibleLogicProgram delp){
		super(delp);		
	}

	/**
	 * In general, a delp comprises of rule schemes with variables. This methods returns the
	 * corresponding grounded theory, i.e., all schematic elements are replaced with all their grounded instances, where
	 * all occurring variables are replaced with constants in every possible way. The set of constants used is the set
	 * of constants appearing in this delp.
	 * @return the grounded version of <source>this</source>
	 */
	public DefeasibleLogicProgram ground(){
		return this.ground(((FolSignature)this.getSignature()).getConstants());
	}
	
	/**
	 * In general, a delp comprises of rule schemes with variables. This methods returns the
	 * corresponding grounded theory, i.e., all schematic elements are replaced with all their grounded instances, where
	 * all occurring variables are replaced with constants in every possible way.
	 * @param constants some set of constants. 
	 * @return the grounded version of <source>this</source>
	 */
	public DefeasibleLogicProgram ground(Set<Term> constants){
		if(this.isGround()) return new DefeasibleLogicProgram(this);
		DefeasibleLogicProgram groundedDelp = new DefeasibleLogicProgram();
		for(DelpRule rule: this)
			for(Formula groundedRule: rule.allGroundInstances(constants))
				groundedDelp.add((DelpRule)groundedRule);		
		return groundedDelp;
	}

	/** 
	 * This method translates this delp into an abstract Dung theory. All arguments, that can
	 * be built in this theory are interpreted as abstract arguments. The attack relation is built using
	 * the dialectical proof theory of delp.
	 * @return the abstract Dung theory induced by this delp.
	 */
	public DungTheory getDungTheory(){
		DungTheory dungTheory = new DungTheory();
		//add arguments
		Iterator<DelpArgument> it = this.getArguments().iterator();
		while(it.hasNext())
			dungTheory.add(new Argument(it.next().toString()));
		//add attacks
		it = getArguments().iterator();
		while(it.hasNext()){
			DelpArgument arg1 = (DelpArgument) it.next();
			Iterator<DelpArgument> it2 = getArguments().iterator();
			while(it2.hasNext()){
				DelpArgument arg2 = (DelpArgument) it2.next();
				if(arg1.getDisagreementSubargument(arg2.getConclusion(), this) != null){
					dungTheory.add(new Attack(new Argument(arg2.toString()), new Argument(arg1.toString())));
				}
			}
		}

		return dungTheory;
	}
	
	/**
	 * Returns the set of all possible arguments, that can be built in this delp.
	 * @return the set of all possible arguments, that can be built in this delp.
	 */
	public Set<DelpArgument> getArguments(){
		if(!this.isGround())
			throw new IllegalArgumentException("This program must be grounded first before computing arguments.");
		Set<Derivation<DelpRule>> derivations = Derivation.allDerivations(this);
		Set<DelpArgument> arguments = new HashSet<DelpArgument>();
		for(Derivation<DelpRule> derivation: derivations){
			Set<DefeasibleRule> rules = new HashSet<DefeasibleRule>();
			for(DelpRule rule: derivation)
				if(rule instanceof DefeasibleRule)
					rules.add((DefeasibleRule)rule);
			arguments.add(new DelpArgument(rules,(FolFormula)derivation.getConclusion()));
		}
		// subargument test
		Set<DelpArgument> result = new HashSet<DelpArgument>();
		for(DelpArgument argument1: arguments){
			boolean is_minimal = true;
			for(DelpArgument argument2: arguments){
				if(argument1.getConclusion().equals(argument2.getConclusion()) && argument2.isStrongSubargumentOf(argument1)){
					is_minimal = false;
					break;
				}
			}
			if(is_minimal) result.add(argument1);
		}
		return result;
	}

	/**
	 * Computes the strict closure of the program, i.e., the set of all strictly derivable literals. For this computation the program
	 * may be extended by the given parameters
	 * @param literals a set of literals
	 * @param srules a set of defeasible rules
	 * @param usefacts set to <source>true</source> iff the delpFacts of this program shall be used in computing the closure
	 * @return the closure of this program and the given parameters
	 */
	public Set<FolFormula> getStrictClosure(Set<FolFormula> literals, Set<DefeasibleRule> srules, boolean usefacts){
		if(!isGround())
			throw new IllegalArgumentException("Delp must be grounded first.");
		Set<FolFormula> strictClosure = new HashSet<FolFormula>();
		strictClosure.addAll(literals);
		if(usefacts){
			for(DelpRule rule: this)
				if(rule instanceof DelpFact)
					strictClosure.add((FolFormula)rule.getConclusion());
		}
		boolean modified = true;
		Set<StrictRule> rules = new HashSet<StrictRule>();
		for(DelpRule rule: this)
			if(rule instanceof StrictRule)
				rules.add((StrictRule)rule);
		Iterator<DefeasibleRule> r_it = srules.iterator();
		while(r_it.hasNext()){
			Rule rule = r_it.next();			
			Set<FolFormula> premise = new HashSet<FolFormula>();
			for(Formula f: rule.getPremise())
				premise.add((FolFormula)f);
			rules.add(new StrictRule((FolFormula)rule.getConclusion(),premise));
		}
		while(modified){
			modified = false;
			Set<StrictRule> rules2 = new HashSet<StrictRule>();
			Iterator<StrictRule> rules_it = rules.iterator();
			while(rules_it.hasNext()){
				StrictRule rule = rules_it.next();
				if(rule.isApplicable(strictClosure)){
					strictClosure.add((FolFormula)rule.getConclusion());
					modified = true;
				}
				else rules2.add(rule);
			}
			rules = rules2;
		}
		return strictClosure;
	}

	/**
	 * Computes the strict closure of the program, i.e., the set of all strictly derivable literals. The program is extended
	 * with delpFacts and defeasible rules (which are interpreted as strict rules here) described by the parameters
	 * <source>literals</source> and <source>srules</source>.
	 * @param literals a set of literals
	 * @param srules a set of defeasible rules
	 * @return the set of all strictly derivable literals.
	 */
	public Set<FolFormula> getStrictClosure(Set<FolFormula> literals, Set<DefeasibleRule> srules){
		return getStrictClosure(literals,srules,true);
	}

	/**
	 * Computes the strict closure of the program, i.e., the set of all strictly derivable literals. The program is extended
	 * with delpFacts described by the parameter <source>literals</source>
	 * @param literals a set of literals
	 * @return the set of all strictly derivable literals.
	 */
	public Set<FolFormula> getStrictClosure(Set<FolFormula> literals){
		return getStrictClosure(literals, new HashSet<DefeasibleRule>());
	}

	/**
	 * Computes the strict closure of the program, i.e., the set of all strictly derivable literals.
	 * @return the set of all strictly derivable literals.
	 */
	public Set<FolFormula> getStrictClosure(){
		return this.getStrictClosure(new HashSet<FolFormula>());
	}

	/**
	 * Checks whether the given set of defeasible rules are consistent given the strict part of this
	 * program.
	 * @param rules a set of defeasible rules
	 * @return <source>false</source> if the union of this program's delpFacts and strict rules with the given set
	 * 	of defeasible rules defeasibly derives two complementary literals
	 */
	public boolean isConsistent(Set<DefeasibleRule> rules){
		if(!isGround())	
			throw new IllegalArgumentException("Delp must be ground.");
		DefeasibleLogicProgram delp = new DefeasibleLogicProgram();
		for(DelpRule rule: this)
			if(rule instanceof DelpFact || rule instanceof StrictRule)
				delp.add(rule);
		for(DefeasibleRule rule: rules)
			delp.add(rule.toStrictRule());
		Set<FolFormula> closure = delp.getStrictClosure();
		Iterator<FolFormula> lit_it = closure.iterator();
		while(lit_it.hasNext())
			if(closure.contains(lit_it.next().complement()))
				return false;
		return true;
	}

	/**
	 * Checks whether the given set of literals disagree with respect to the strict part of this program.
	 * @param literals a set of literals
	 * @return <source>true</source> if the union of this program's delpFacts and strict rules with the given set
	 * 	of literals defeasibly derives two complementary literals
	 */
	public boolean disagree(Set<FolFormula> literals){
		if(!isGround()) 
			throw new IllegalArgumentException("Delp must be grounded first.");
		DefeasibleLogicProgram delp = new DefeasibleLogicProgram(this);
		Iterator<FolFormula> it = literals.iterator();
		while(it.hasNext()){
			delp.add(new DelpFact(it.next()));
		}
		Set<FolFormula> closure = delp.getStrictClosure();
		Iterator<FolFormula> lit_it = closure.iterator();
		while(lit_it.hasNext())
			if(closure.contains(lit_it.next().complement()))
				return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.thimm.uacs.RuleBasedArgumentationTheory#isGround()
	 */
	public boolean isGround(){
		for(DelpRule rule: this)
			if(!rule.isGround())
				return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String str = new String();
		for(DelpRule rule: this)
			str += rule.toString()+"\n";		
		return str;
	}

	/**
	 * Returns all defeasible and strict rules appearing in this program with the given literal as head
	 * @param l a literal
	 * @return a set of strict and defeasible rules
	 */
	public Set<DelpRule> getRulesWithHead(FolFormula l){
		Set<DelpRule> rules = new HashSet<DelpRule>();
		for(DelpRule rule: this)
			if(rule instanceof DefeasibleRule || rule instanceof StrictRule)
				if(rule.getConclusion().equals(l))
					rules.add(rule);		
		return rules;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.BeliefBase#getSignature()
	 */
	@Override
	public Signature getSignature() {
		FolSignature signature = new FolSignature();
		for(DelpRule rule: this){
			signature.addAll(rule.getPredicates());
			signature.addAll(rule.getConstants());
		}
		return signature;
	}
	
	public static void main(String[] args) throws FileNotFoundException, ParserException, IOException{
		DefeasibleLogicProgram delp = (DefeasibleLogicProgram) new net.sf.tweety.argumentation.delp.parser.DelpParser().parseBeliefBaseFromFile("/Users/mthimm/Desktop/delp");
		System.out.println(delp);
				
		for(DelpArgument arg: new DelpReasoner(delp, new GeneralizedSpecificity()).getWarrants())
			System.out.println(arg);
	}

}
