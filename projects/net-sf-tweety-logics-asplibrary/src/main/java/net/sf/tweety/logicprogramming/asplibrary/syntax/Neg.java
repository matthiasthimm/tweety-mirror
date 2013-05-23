package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.tweety.logics.commons.error.LanguageException;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * This class models strict negation for atoms.
 * 
 * @author Tim Janus
 * @author Thomas Vengels
 */
public class Neg extends ELPElementAdapter implements ELPLiteral {

	ELPAtom	atom;
	
	public Neg(ELPAtom inner) {
		this.atom = inner;
	}
	
	public Neg(Neg other) {
		this.atom = (ELPAtom)other.getAtom().clone();
	}
	
	public Neg(String symbol, Term<?>... terms) {
		atom = new ELPAtom(symbol, terms);
	}
	
	/**
	 * default constructor, create an atom from a functor name
	 * and a list of terms. size of terms determines arity of
	 * functor.
	 * 
	 * @param atomexpr
	 */
	public Neg(String symbol, Collection<Term<?>> terms) {
		atom = new ELPAtom(symbol, terms);
	}
	
	public Neg(String expr) {
		atom = new ELPAtom(expr);
	}

	@Override
	public ELPAtom getAtom() {
		return this.atom;
	}
	
	@Override
	public String toString() {
		return "-" + this.atom;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Neg) {
			Neg on = (Neg) o;
			
			// compare atom
			return on.getAtom().equals( this.getAtom() );
		} else {
			return false;
		}
	}

	@Override
	public boolean isGround() {
		return atom.isGround();
	}
	
	@Override
	public Neg clone() {
		return new Neg(this);
	}

	@Override
	public ELPAtom complement() {
		return atom;
	}

	@Override
	public ELPLiteral addTerm(Term<?> term) {
		Neg reval = (Neg)this.clone();
		reval.atom.addTerm(term);
		return reval;
	}
	
	@Override 
	public int hashCode() {
		return 7 + atom.hashCode();
	}

	@Override
	public Neg substitute(Term<?> v, Term<?> t)
			throws IllegalArgumentException {
		return new Neg(this.atom.substitute(v,t));
	}

	@Override
	public Set<ELPAtom> getAtoms() {
		return atom.getAtoms();
	}

	@Override
	public Set<ELPPredicate> getPredicates() {
		return atom.getPredicates();
	}

	@Override
	public ElpSignature getSignature() {
		return atom.getSignature();
	}

	@Override
	public Set<Term<?>> getTerms() {
		return atom.getTerms();
	}

	@Override
	public String getName() {
		return atom.getName();
	}

	@Override
	public ELPPredicate getPredicate() {
		return atom.getPredicate();
	}

	@Override
	public RETURN_SET_PREDICATE setPredicate(Predicate predicate) {
		return atom.setPredicate(predicate);
	}

	@Override
	public void addArgument(Term<?> arg) throws LanguageException {
		atom.addArgument(arg);
	}

	@Override
	public List<? extends Term<?>> getArguments() {
		return atom.getArguments();
	}

	@Override
	public boolean isComplete() {
		return atom.isComplete();
	}

	@Override
	public SortedSet<ELPLiteral> getLiterals() {
		SortedSet<ELPLiteral> reval = new TreeSet<ELPLiteral>();
		reval.add(this);
		return reval;
	}

	@Override
	public int compareTo(ELPLiteral o) {
		if(o instanceof ELPAtom) {
			return 1;
		}
		return this.toString().compareTo(o.toString());
	}
}
