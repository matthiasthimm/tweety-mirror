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
import net.sf.tweety.logics.commons.error.LanguageException;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.commons.syntax.Variable;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * this class models an atom, which is a basic structure for
 * building literals and rules for logic programs
 * 
 * @author Tim Janus
 * @author Thomas Vengels
 */
public class ELPAtom extends ELPElementAdapter implements ELPLiteral {

	/** the predicate identifying the atom */
	protected ELPPredicate		pred;
	
	/** a list of arguments of the atom */
	protected List<Term<?>>	arguments = new LinkedList<Term<?>>();
	
	/**
	 * Copy-Ctor: Generates a deep copy of the given atom
	 * @param other	The atom acting as source for the deep copy
	 */
	public ELPAtom(ELPAtom other) {
		this.pred = new ELPPredicate(other.getName(), other.getArity());
		for(Term<?> t : other.getTerms()) {
			this.arguments.add((Term<?>)t.clone());
		}
	}
	
	/**
	 * Ctor: Creates an atom with the given predicate as name and the
	 * given terms as argument
	 * @param symbol	The name of the atom
	 * @param terms		A list of Term<?> definining the arguments of the term
	 */
	public ELPAtom(String symbol, Term<?>... terms) {
		this.pred = new ELPPredicate(symbol, terms.length);
		for(int i=0; i<terms.length; ++i) {
			this.arguments.add(terms[i]);
		}
	}
	
	/**
	 * Creates a predicate for the atom from symbol name
	 * and uses a list of terms as arguments for the atom. 
	 * Size of terms determines arity of the predicate.
	 */
	public ELPAtom(String symbol, Collection<Term<?>> terms) {
		this.pred = new ELPPredicate(symbol, terms.size());
		this.arguments.addAll(terms);
	}
	
	/**
	 * Creates an ELPAtom from the given String using the ELPParser.
	 * @param expr	String representing the Atom
	 */
	public ELPAtom(String expr) {
		try {
			ELPParser ep = new ELPParser( new StringReader( expr ));
			ELPAtom a = (ELPAtom)ep.atom();
			this.pred = a.pred;
			this.arguments = a.arguments;
		} catch (Exception e) {
			System.err.println("Atom: could not parse input!");
			System.err.println(e);
			System.err.println("Input: " + expr);
		}
	}
	
	public static ELPAtom instantiate(String functor, Collection<Term<?>> terms) {
		return new ELPAtom(functor, (terms != null ? terms : new LinkedList<Term<?>>()) );
	}

	@Override
	public ELPAtom getAtom() {
		return this;
	}
	
	public int getArity() {
		return this.pred.getArity();
	}

	@Override 
	public String toString() {
		String ret = "";
		ret += this.pred.getName();
		
		if (arguments.size()>0) {
			ret += "(" + this.arguments.get(0);
			for (int i = 1; i < arguments.size(); i++)
				ret += ", " + this.arguments.get(i);
			ret += ")";
		}
		return ret;
	}
	
	public Term<?>	getTerm(int index) {
		if ( (index <0) || (this.arguments == null))
			return null;
		if ( index >= this.arguments.size())
			return null;
		
		return this.arguments.get(index);
	}
	
	@Override
	public ELPLiteral addTerm(Term<?> tval)  {
		ELPAtom reval = (ELPAtom)this.clone();
		reval.pred.setArity(reval.pred.getArity() + 1);
		reval.arguments.add(tval);
		return reval;
	}
	
	@Override
	public int hashCode() {
		return this.pred.hashCode() + this.arguments.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof ELPAtom) {			
			ELPAtom oa = (ELPAtom) o;	
			
			// functors must be equal
			if (!oa.pred.equals( this.pred))
				return false;
			
			if(!oa.arguments.equals(arguments))
				return false;
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public ELPAtom clone() {
		return new ELPAtom(this);
	}
	
	@Override
	public String getName() {
		return pred.getName();
	}

	@Override
	public boolean isGround() {
		if(arguments == null)
			return true;
		
		for(Term<?> t : arguments) {
			if(t instanceof Variable)
				return false;
			else if(t instanceof ELPAtom) {
				return ((ELPAtom)t).isGround();
			}
		}
		return true;
	}

	@Override
	public Neg complement() {
		return new Neg(this);
	}

	@Override
	public ELPAtom substitute(Term<?> v, Term<?> t)
			throws IllegalArgumentException {
		ELPAtom reval = new ELPAtom(this);
		reval.arguments.clear();
		for(int i=0; i<arguments.size(); i++) {
			if(arguments.get(i).equals(v)) {
				reval.arguments.add(t);
			} else {
				reval.arguments.add(arguments.get(i));
			}
		}
		return reval;
	}

	@Override
	public Set<ELPAtom> getAtoms() {
		Set<ELPAtom> reval = new HashSet<ELPAtom>();
		reval.add(this);
		return reval;
	}

	@Override
	public Set<ELPPredicate> getPredicates() {
		Set<ELPPredicate> reval = new HashSet<ELPPredicate>();
		reval.add(pred);
		return reval;
	}

	@Override
	public ElpSignature getSignature() {
		ElpSignature reval = new ElpSignature();
		reval.add(this);
		return reval;
	}

	@Override
	public Set<Term<?>> getTerms() {
		Set<Term<?>> reval = new HashSet<Term<?>>();
		reval.addAll(arguments);
		return reval;
	}

	@Override
	public ELPPredicate getPredicate() {
		return pred;
	}

	@Override
	public RETURN_SET_PREDICATE setPredicate(Predicate predicate) {
		Predicate old = this.pred;
		this.pred = (ELPPredicate)predicate;
		return AtomImpl.implSetPredicate(old, this.pred, arguments);
	}

	@Override
	public void addArgument(Term<?> arg) throws LanguageException {
		this.arguments.add(arg);
	}

	@Override
	public List<? extends Term<?>> getArguments() {
		return Collections.unmodifiableList(arguments);
	}

	@Override
	public boolean isComplete() {
		return getTerms().size() == pred.getArity();
	}

	@Override
	public SortedSet<ELPLiteral> getLiterals() {
		SortedSet<ELPLiteral> reval = new TreeSet<ELPLiteral>();
		reval.add(this);
		return reval;
	}

	@Override
	public int compareTo(ELPLiteral o) {
		if(o instanceof Neg) { 
			return -1;
		}
		return toString().compareTo(o.toString());
	}
}
