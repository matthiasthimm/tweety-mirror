package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.io.StringReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.parser.ELPParser;


/**
 * This class models a rule for a disjunctive logic program.
 * A rule is a collection of literals and more sophisticated rule elements
 * like Aggregate or Arithmetic. It uses separate lists for the
 * head and the body. It also implements the Comperable interface to allow
 * the ordering in collections.
 * 
 * @author Tim Janus
 * @author Thomas Vengels
 *
 */
public class Rule implements Comparable<Rule>{

	List<Literal>	head = new LinkedList<Literal>();
	List<RuleElement>	body = new LinkedList<RuleElement>();
	
	public Rule() {}
	
	public Rule(Rule other) {
		for(Literal headLits : other.head) {
			this.head.add((Literal)headLits.clone());
		}
		
		for(RuleElement bodyElement : other.body) {
			this.body.add((RuleElement)bodyElement.clone());
		}
	}
	
	public Rule(Literal head) {
		this.head.add(head);
	}
	
	public Rule(Literal head, List<RuleElement> litsBody) {
		this.head.add(head);
		this.body.addAll(litsBody);
	}
	
	public Rule(List<Literal> litsHead, List<RuleElement> litsBody) {
		this.head.addAll(litsHead);
		this.body.addAll(litsBody);
	}
	
	public Rule(String ruleexpr) {
		try {
			ELPParser ep = new ELPParser( new StringReader( ruleexpr ));
			Rule r = ep.rule();
			this.head = r.head;
			this.body = r.body;
		} catch (Exception e) {
			System.err.println("Rule: could not parse input!");
			System.err.println(e);
			System.err.println("Input: " + ruleexpr);
		}
	}
	
	public List<Literal> getHead() {
		return Collections.unmodifiableList(this.head);
	}
	
	public List<RuleElement> getBody() {
		return Collections.unmodifiableList(this.body); 
	}
	
	public List<RuleElement> getProgramElements() {
		List<RuleElement> reval = new LinkedList<RuleElement>();
		reval.addAll(head);
		reval.addAll(body);
		return reval;
	}
	
	public Set<Literal> getLiterals() {
		Set<Literal> literals = new HashSet<Literal>();
		literals.addAll(head);
		for(RuleElement pe : body) {
			literals.addAll(pe.getLiterals());
		}
		return literals;
	}
	
	public void	addHead(Literal l) {
		this.head.add(l);
	}
	
	public void	addBody(RuleElement l) {
		this.body.add(l);
	}
	
	public void	addHead(Collection<? extends Literal> l) {
		this.head.addAll(l);
	}
	
	public void	addBody(Collection<? extends RuleElement> l) {
		this.body.addAll(l);
	}
	
	public	boolean		isFact() {
		return body.size() == 0 && head.size() == 1;
	}
	
	public	boolean 	isChoice() {
		return false;
	}
	
	public	boolean		isConstraint() {
		return head.size() == 0;
	}
	
	public boolean		isWeakConstraint() {
		return false;
	}
	
	public boolean		isComment() {
		return false;
	}
	
	/**
	 * Proofs if the given rule is safe for use in a solver.
	 * To get a felling when a rule is safe read the following text
	 * from the dlv documentation:
	 * 
	 * A variable X in an aggregate-free rule is safe if at least one of the following conditions is satisfied:
	 * X occurs in a positive standard predicate in the body of the rule;
	 * X occurs in a true negated standard predicate in the body of the rule;
	 * X occurs in the last argument of an arithmetic predicate A and all other arguments of A are safe. (*not supported yet)
	 * A rule is safe if all its variables are safe. However, cyclic dependencies are disallowed, e.g., :- #succ(X,Y), #succ(Y,X) is not safe.
	 * 
	 * @return true if the rule is safe considering the above conditions, false otherwise.
	 */
	public boolean isSafe() {
		Set<Term<?>> variables = new HashSet<Term<?>>();
		Set<Literal> allLit = getLiterals();
		
		// TODO: only depth of one... the entire asp-library has major desing issues... best solution: Redesign core interfaces
		// TOTALLY HACKED WILL NOT WORK FOR EVERYTHING:...
		for(Literal l : allLit) {
			if(!l.isGround()) {
				for(Term<?> t : l.getAtom().getTerms()) {
					if(t instanceof Variable) {
						variables.add((Variable)t);
					} else if(t instanceof Atom) {
						if(!((Atom)t).isGround()) {
							for(Term<?> t2 : ((Atom)t).getTerms()) {
								if(t2 instanceof Variable) {
									variables.add((Variable)t2);
								} else if(t2 instanceof Constant) {
									Constant st = (Constant) t2;
									if(st.get().charAt(0) >= 65 && st.get().charAt(0) <= 90) {
										variables.add(st);
									}
								}
							}
						}
					}
				}
			}
		}
		
		if(variables.size() == 0)
			return true;
		
		for(Term<?> x : variables) {
			boolean safe = false;
			for(Literal l : allLit) {
				if(	l instanceof Neg || l instanceof Atom ) {
					for(Term<?> t : l.getAtom().getTerms()) {
						if(t.equals(x)) {
							safe = true;
						}
					}
				}
			}
			
			if(!safe)
				return false;
		}
		
		return true;
	}
	
	@Override
	public String	toString() {
		String ret = "";
		if (head.size() > 0) {
			ret += head.get(0);
			for (int i=1; i<head.size(); ++i)
				ret += " || " + head.get(i);			
		}
		if (body.size() > 0) {
			ret += ":- " + body.get(0);
			for(int i=1; i<body.size(); ++i) {
				ret += ", " + body.get(i);
			}
		}
		ret += ".";
		
		return ret;
	}
	
	@Override 
	public boolean equals(Object other) {
		if(!(other instanceof Rule)) 	return false;
		Rule or = (Rule)other;
		
		boolean reval = this.head.equals(or.head) && this.body.equals(or.body);
		return reval;
	}
	
	@Override
	public int hashCode() {
		return head.hashCode() + body.hashCode();
	}

	@Override
	public int compareTo(Rule arg0) {
		int comp = 0;
		
		// facts first:
		if(getBody().size() == 0 && arg0.getBody().size() != 0) {
			return -1;
		} else if(getBody().size() != 0 && arg0.getBody().size() == 0) {
			return 1;
		}
		
		// then order alphabetically starting by the head.
		for(int i=0; i<getHead().size() && i<arg0.getHead().size(); ++i) {
			comp = getHead().get(i).toString().compareTo(arg0.getHead().get(i).toString());
			if(comp != 0)
				return comp;
		}
		
		// if the head is the same use the body.
		for(int i=0; i<getBody().size() && i<arg0.getBody().size(); ++i) {
			comp = getBody().get(i).toString().compareTo(arg0.getBody().get(i).toString());
			if(comp != 0)
				return comp;
		}
		
		return comp;
	}
	
	@Override
	public Object clone() {
		return new Rule(this);
	}
}
