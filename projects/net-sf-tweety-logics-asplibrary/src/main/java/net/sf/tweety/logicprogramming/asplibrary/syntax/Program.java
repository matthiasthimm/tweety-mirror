package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sf.tweety.Signature;
import net.sf.tweety.logicprogramming.asplibrary.parser.ELPParser;

/**
 * this class models an disjunctiv logical program, which is
 * a collection of rules. The rules are ordered alphabetically 
 * 
 * @author Tim Janus
 * @author Thomas Vengels
 *
 */
public class Program {
	
	/** The signature of the logic program */
	private ElpSignature signature;
	
	/** a set of all rules of the logic program */
	private Set<Rule> rules = new TreeSet<Rule>();
	
	/** Default Ctor: Does nothing */
	public Program() {}
	
	/** Copy Ctor: Used by clone method */
	public Program(Program other) {
		// TODO: COpy signature
		//this.signature = new ElpSignature(other.signature);
		this.rules.addAll(other.getRules()); 
	}
	
	//Differs from contains in that it does a deep comparision of the rules rather than a reference-based one
	//Could be turned into an override of the ArrayList contains method later
	//--Daniel
	public boolean hasRule(Rule compRule)
	{
		/*
		for(Rule r : this)
		{
			//This solution depends on the rule.equals being a deep comparision
			if(compRule.equals(r))
				return true;
		}
		*/
		if (this.toString().contains(compRule.toString())) //Temporary fix
			return true;
		return false;
	}
	
	public void add(String expr) {
		try {
			expr = expr.trim();
			if(expr.charAt(expr.length() -1) != '.')
				expr += ".";
			
			ELPParser ep = new ELPParser( new StringReader( expr ));
			List<Rule> rules = ep.program();
			this.addAll(rules);
		} catch (Exception e) {
			System.err.println("Rule: could not parse input!");
			System.err.println(e);
			System.err.println("Input: " + expr);
		}
	}
	
	/**
	 * Adds the given rule to the program
	 * @param rule	Reference to the rule to add
	 * @return		true if the rule was successful added (not part of the programs
	 * 				rules yet), false otherwise
	 */
	public boolean add(Rule rule) {
		boolean reval = rules.add(rule);
		return reval;
	}
	
	public void addAll(Collection<Rule> rules) {
		this.rules.addAll(rules);
	}
	
	/** @return 	An unmodifiable set of the rules of the program */
	public Set<Rule> getRules() {
		return Collections.unmodifiableSet(this.rules);
	}
	
	/**
	 * removes all the given rules from the program 
	 * @param toRemove	collection with rules which has to be remove from the program
	 */
	public void removeAllRules(Collection<Rule> toRemove) {
		rules.removeAll(toRemove);
	}
	
	/** 
	 * empties the program.
	 */
	public void clearRules() {
		rules.clear();
	}
	
	public int size() {
		return rules.size();
	}
	
	/**
	 * Adds another programs content to the content of this program.
	 * @param other	Reference to the other program.
	 */
	public void add(Program other) {
		rules.addAll(other.getRules());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Rule> it = rules.iterator();
		
		if (it.hasNext()) {
			sb.append( it.next() );
		}
		while (it.hasNext()) {
			sb.append("\n" + it.next());
		}
		return sb.toString();
	}
	
	public static Program loadFrom(String file) {
		try {
			return loadFrom(new FileReader( file ));
		} catch (FileNotFoundException e) {
			System.err.println("Error cant find file: " + file);
			e.printStackTrace();
		}
		return null;
	}
	
	public static Program loadFrom(Reader stream) {
		Program ret = null;
		
		try {
			ELPParser ep = new ELPParser( stream );
			List<Rule> lr = ep.program();
			ret = new Program();
			for(Rule r: lr)
				ret.add(r);
			ret.calcSignature();
		} catch (Exception e) {
			System.err.println("Error while loading program: " + e.getMessage());
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public Signature getSignature() {
		if(signature == null)
			calcSignature();
		return signature;
	}
	
	private void calcSignature() {
		signature = new ElpSignature();
		for(Rule r : rules) {
			List<RuleElement> literals = new LinkedList<RuleElement>();
			literals.addAll(r.getBody());
			literals.addAll(r.getHead());
			
			for(RuleElement l : literals) {
				signature.add(l);
			}
		}
	}
	
	/**
	 * Checks if the program is an extendend programs, that means the heads of the
	 * literals have not more than one literal.
	 * @return	True if the program is an extended program, false otherwise.
	 */
	public boolean isExtendedProgram() {
		for(Rule r : rules) {
			if(r.head.size() > 1)
				return false;
		}
		return true;
	}
	
	public void saveTo(String filename) {
		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(filename));
			for (Rule r : rules) {
				w.write(r.toString());
				w.newLine();
			}
			w.flush();
			w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String toStringFlat() {
		StringBuilder sb = new StringBuilder();
		
		Iterator<Rule> rIter = rules.iterator();
		while (rIter.hasNext()) {
			Rule r = rIter.next();
			if (r.isComment())
				continue;
			sb.append(r.toString()+"\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * Creates the defaultifictation p_d of a given program p.
	 * A defaultificated program p' of p adds for every Rule r in p a modified rule r_d 
	 * of the form: H(r) :- B(r), not -H(r). to the program p'. 
	 * @param p	The program which is not defaultificated yet
	 * @return a program p' which is the defaultificated version of p.
	 */
	public static Program defaultification(Program p) {
		Program reval = new Program();
		for(Rule origRule : p.getRules()) {
			Rule defRule = new Rule();
			if(!origRule.isConstraint()) {
				Literal head = origRule.getHead().get(0);
				Neg neg = new Neg(head.getAtom());
				defRule.addBody(origRule.getBody());
				Not defaultificationLit = null;
				if(head instanceof Neg) {
					defRule.addHead(neg);
					defaultificationLit = new Not(head.getAtom());
				} else {
					defRule.addHead(head);
					defaultificationLit = new Not(neg);
				}
				
				if(defaultificationLit != null && !defRule.getBody().contains(defaultificationLit)) {
					defRule.addBody(defaultificationLit);
				}
			} else {
				defRule.addBody(origRule.getBody());
			}
			reval.add(defRule);
		}
		return reval;
	}
	
	/**
	 * Adds the given atom as fact to the logical program.
	 * @param fact	atom representing the fact.
	 * @return
	 */
	public boolean add(Atom fact) {
		Rule r = new Rule();
		r.addHead(fact);
		return add(r);
	}
	
	public boolean add(Literal head, RuleElement... bodyElements) {
		Rule r = new Rule(head);
		for(RuleElement a : bodyElements) {
			r.addBody(a);
		}
		return add(r);
	}
	
	@Override
	public Object clone() {
		return new Program(this);
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Program)) return false;
		Program op = (Program)other;
		return op.rules.equals(rules);
	}
	
	@Override
	public int hashCode() {
		return rules.hashCode();
	}
}
