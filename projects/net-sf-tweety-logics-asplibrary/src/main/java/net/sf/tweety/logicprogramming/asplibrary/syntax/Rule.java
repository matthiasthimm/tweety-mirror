package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.io.StringReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.tweety.logicprogramming.asplibrary.parser.ELPParser;
import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.Variable;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;


/**
 * This class models a rule for a disjunctive logic program.
 * A rule is a collection of literals and more sophisticated rule elements
 * like Aggregate or Arithmetic. It uses separate lists for the
 * head and the body. It also implements the Comparable interface to allow
 * the ordering in collections.
 * 
 * @author Tim Janus
 * @author Thomas Vengels
 *
 */
public class Rule extends ELPElementAdapter implements Comparable<Rule>, ELPElement, net.sf.tweety.util.rules.Rule {

	ELPHead head = new ELPHead();
	List<ELPElement>	body = new LinkedList<ELPElement>();
	
	/** Default-Ctor: Creates an empty rule without any head literals or body elements */
	public Rule() {}
	
	/** Copy-Ctor: Makes a deep copy of the given rule */
	public Rule(Rule other) {
		for(ELPLiteral headLits : other.head) {
			this.head.add((ELPLiteral)headLits.clone());
		}
		
		for(ELPElement bodyElement : other.body) {
			this.body.add((ELPElement)bodyElement.clone());
		}
	}
	
	/** 
	 * Ctor: Create a rule with the given head, cause there are no
	 * body elements the created rule is a fact.
	 * 
	 * @param head	The head of the rule as ELPHead
	 */
	public Rule(ELPHead head) {
		this.head = head;
	}
	
	/**
	 * Ctor: Create a rule with the given head, cause there are no
	 * body elements the created rule is a fact.
	 * 
	 * @param head	The head of the rule as ELPLiteral
	 */
	public Rule(ELPLiteral head) {
		this.head.add(head);
	}
	
	public Rule(ELPLiteral head, List<ELPElement> litsBody) {
		this.head.add(head);
		this.body.addAll(litsBody);
	}
	
	public Rule(List<ELPLiteral> litsHead, List<ELPElement> litsBody) {
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
	
	public ELPHead getHead() {
		return this.head;
	}
	
	public List<ELPElement> getBody() {
		return Collections.unmodifiableList(this.body); 
	}
	
	public List<ELPElement> getProgramElements() {
		List<ELPElement> reval = new LinkedList<ELPElement>();
		reval.addAll(head);
		reval.addAll(body);
		return reval;
	}
	
	@Override
	public SortedSet<ELPLiteral> getLiterals() {
		SortedSet<ELPLiteral> literals = new TreeSet<ELPLiteral>();
		literals.addAll(head);
		for(ELPElement pe : body) {
			literals.addAll(pe.getLiterals());
		}
		return literals;
	}
	
	public void	addHead(ELPLiteral l) {
		this.head.add(l);
	}
	
	public void	addBody(ELPElement l) {
		this.body.add(l);
	}
	
	public void	addHead(Collection<? extends ELPLiteral> l) {
		this.head.addAll(l);
	}
	
	public void	addBody(Collection<? extends ELPElement> l) {
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
		Set<ELPLiteral> allLit = getLiterals();
		
		// TODO: only depth of one... the entire asp-library has major desing issues... best solution: Redesign core interfaces
		// TOTALLY HACKED WILL NOT WORK FOR EVERYTHING:...
		for(ELPLiteral l : allLit) {
			if(!l.isGround()) {
				for(Term<?> t : l.getAtom().getTerms()) {
					if(t instanceof Variable) {
						variables.add((Variable)t);
					} else if(t instanceof ELPAtom) {
						if(!((ELPAtom)t).isGround()) {
							for(Term<?> t2 : ((ELPAtom)t).getTerms()) {
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
			for(ELPLiteral l : allLit) {
				if(	l instanceof Neg || l instanceof ELPAtom ) {
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
			ret += head.toString();	
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
		comp = getHead().toString().compareTo(arg0.getHead().toString());
		if(comp != 0)
			return comp;
		
		// if the head is the same use the body.
		for(int i=0; i<getBody().size() && i<arg0.getBody().size(); ++i) {
			comp = getBody().get(i).toString().compareTo(arg0.getBody().get(i).toString());
			if(comp != 0)
				return comp;
		}
		
		return comp;
	}
	
	@Override
	public Rule clone() {
		return new Rule(this);
	}

	@Override
	public ElpSignature getSignature() {
		ElpSignature reval = new ElpSignature();
		reval.addSignature(head.getSignature());
		for(ELPElement bodyElement : body) {
			reval.addSignature(bodyElement.getSignature());
		}
		return reval;
	}

	@Override
	public Collection<ELPElement> getPremise() {
		return Collections.unmodifiableList(body);
	}

	@Override
	public ELPHead getConclusion() {
		return head;
	}

	@Override
	public Rule substitute(Term<?> v, Term<?> t)
			throws IllegalArgumentException {
		Rule reval = new Rule();
		reval.head = head.substitute(v, t);
		for(ELPElement bodyElement : body) {
			reval.body.add(bodyElement.substitute(v,t));
		}
		return reval;
	}

	@Override
	public Set<ELPAtom> getAtoms() {
		Set<ELPAtom> reval = new HashSet<ELPAtom>();
		reval.addAll(head.getAtoms());
		for(ELPElement bodyElement : body) {
			reval.addAll(bodyElement.getAtoms());
		}
		return reval;
	}

	@Override
	public Set<ELPPredicate> getPredicates() {
		Set<ELPPredicate> reval = new HashSet<ELPPredicate>();
		reval.addAll(head.getPredicates());
		for(ELPElement bodyElement : body) {
			reval.addAll(bodyElement.getPredicates());
		}
		return reval;
	}

	@Override
	public Set<Term<?>> getTerms() {
		Set<Term<?>> reval = new HashSet<Term<?>>();
		reval.addAll(head.getTerms());
		for(ELPElement bodyElement : body) {
			reval.addAll(bodyElement.getTerms());
		}
		return reval;
	}
}
