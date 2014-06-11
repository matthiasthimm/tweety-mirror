package net.sf.tweety.lp.asp.syntax;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.tweety.commons.util.rules.RuleSet;
import net.sf.tweety.logics.commons.syntax.interfaces.LogicProgram;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * this class models an disjunctive logical program, which is
 * a collection of rules. The rules are ordered alphabetically 
 * 
 * @author Tim Janus
 * @author Thomas Vengels
 *
 */
public class Program extends RuleSet<Rule> implements LogicProgram<DLPHead, DLPElement, Rule> {
	
	/** kill warning */
	private static final long serialVersionUID = -5078398905222624805L;
	/** The signature of the logic program */
	private DLPSignature signature;
	
	/** Default Ctor: Does nothing */
	public Program() {}
	
	/** Copy Ctor: Used by clone method */
	public Program(Program other) {
		// TODO: COpy signature
		//this.signature = new ElpSignature(other.signature);
		this.addAll(other); 
	}
	
	/**
	 * Add all rules from base to this program.
	 * @param base a collection of rules
	 */
	public Program(Collection<Rule> base) {
		super(base);
	}

	/**
	 * Adds another programs content to the content of this program.
	 * @param other	Reference to the other program.
	 */
	public void add(Program other) {
		this.addAll(other);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Rule> it = iterator();
		
		if (it.hasNext()) {
			sb.append( it.next() );
		}
		while (it.hasNext()) {
			sb.append("\n" + it.next());
		}
		return sb.toString();
	}
	
	/**
	 * Checks if the program is an extended programs, that means the heads of the
	 * literals have not more than one literal.
	 * @return	True if the program is an extended program, false otherwise.
	 */
	public boolean isExtendedProgram() {
		for(Rule r : this) {
			if(r.head.size() > 1)
				return false;
		}
		return true;
	}
	
	public String toStringFlat() {
		StringBuilder sb = new StringBuilder();
		
		Iterator<Rule> rIter = iterator();
		while (rIter.hasNext()) {
			Rule r = rIter.next();
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
		for(Rule origRule : p) {
			Rule defRule = new Rule();
			if(!origRule.isConstraint()) {
				DLPLiteral head = origRule.head.iterator().next();
				DLPNeg neg = new DLPNeg(head.getAtom());
				defRule.addPremises(origRule.body);
				DLPNot defaultificationLit = null;
				if(head instanceof DLPNeg) {
					defRule.head.add(neg);
					defaultificationLit = new DLPNot(head.getAtom());
				} else {
					defRule.head.add(head);
					defaultificationLit = new DLPNot(neg);
				}
				
				if(defaultificationLit != null && !defRule.body.contains(defaultificationLit)) {
					defRule.addPremise(defaultificationLit);
				}
			} else {
				defRule.addPremises(origRule.body);
			}
			reval.add(defRule);
		}
		return reval;
	}
	
	public void addFact(DLPLiteral fact) {
		this.add(new Rule(fact));
	}
	
	@Override
	public DLPSignature getSignature() {
		if(signature == null)
			calcSignature();
		return signature;
	}
	
	private void calcSignature() {
		signature = new DLPSignature();
		for(Rule r : this) {
			List<DLPElement> literals = new LinkedList<DLPElement>();
			literals.addAll(r.getPremise());
			literals.addAll(r.getConclusion());
			
			for(DLPElement l : literals) {
				signature.add(l);
			}
		}
	}
	
	@Override
	public Program clone() {
		return new Program(this);
	}

	@Override
	public void addFact(DLPHead fact) {
		this.add(new Rule(fact));
		
	}
	
	@Override
	public Program substitute(Term<?> v, Term<?> t) {
		Program reval = new Program();
		for(Rule r : this) {
			reval.add(r.substitute(v, t));
		}
		return reval;
	}

	@Override
	public Program substitute(
			Map<? extends Term<?>, ? extends Term<?>> map)
			throws IllegalArgumentException {
		Program reval = this;
		for(Term<?> t : map.keySet()) {
			reval = reval.substitute(t, map.get(t));
		}
		return reval;
	}

	@Override
	public Program exchange(Term<?> v, Term<?> t)
			throws IllegalArgumentException {
		Program reval = new Program();
		for(Rule r : this) {
			reval.add((Rule)r.exchange(v, t));
		}
		return reval;
	}
}
