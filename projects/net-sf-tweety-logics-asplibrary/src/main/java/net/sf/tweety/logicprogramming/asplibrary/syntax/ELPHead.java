package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.tweety.Signature;
import net.sf.tweety.logics.commons.syntax.AssociativeFormulaSupport;
import net.sf.tweety.logics.commons.syntax.AssociativeFormulaSupport.AssociativeSupportBridge;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.commons.syntax.interfaces.AssociativeFormula;
import net.sf.tweety.logics.commons.syntax.interfaces.Atom;
import net.sf.tweety.logics.commons.syntax.interfaces.ComplexLogicalFormula;
import net.sf.tweety.logics.commons.syntax.interfaces.Disjunctable;
import net.sf.tweety.logics.commons.syntax.interfaces.SimpleLogicalFormula;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * This formula represents the head of an disjunctive rule which is a
 * disjunction of ELP literals. An ELP itself only allows one literal in
 * the head.
 * 
 * @author Tim Janus
 */
public class ELPHead extends ELPElementAdapter 
	implements AssociativeFormula<ELPLiteral>, 
				Disjunctable, 
				ComplexLogicalFormula,
				AssociativeSupportBridge{

	private AssociativeFormulaSupport<ELPLiteral> assocSupport =
			new AssociativeFormulaSupport<ELPLiteral>(this);
	
	public ELPHead() {
		
	}
	
	public ELPHead(ELPHead other) {
		
	}
	
	@Override
	public Set<ELPAtom> getAtoms() {
		Set<ELPAtom> reval = new HashSet<ELPAtom>();
		for(Atom at : assocSupport.getAtoms()) {
			if(at instanceof ELPAtom) {
				reval.add((ELPAtom)at);
			}
		}
		return reval;
	}

	@Override
	public Set<ELPPredicate> getPredicates() {
		Set<ELPPredicate> reval = new HashSet<ELPPredicate>();
		for(Predicate pred : assocSupport.getPredicates()) {
			if(pred instanceof ELPPredicate) {
				reval.add((ELPPredicate)pred);
			}
		}
		return reval;
	}

	@Override
	public Class<? extends Predicate> getPredicateCls() {
		return ELPPredicate.class;
	}

	@Override
	public ElpSignature getSignature() {
		return (ElpSignature)assocSupport.getSignature();
	}

	@Override
	public Set<ELPLiteral> getFormulas() {
		return assocSupport.getFormulas();
	}

	@Override
	public <C extends SimpleLogicalFormula> Set<C> getFormulas(Class<C> cls) {
		return assocSupport.getFormulas(cls);
	}
	
	@Override
	public ELPHead substitute(Term<?> v, Term<?> t)
			throws IllegalArgumentException {
		return (ELPHead)assocSupport.substitute(v,t);
	}
	
	@Override
	public SimpleLogicalFormula combineWithOr(Disjunctable f) {
		if(! (f instanceof ELPLiteral))
			throw new IllegalArgumentException();
		
		ELPLiteral arg = (ELPLiteral)f;
		ELPHead reval = new ELPHead(this);
		reval.add(arg);
		return reval;
	}

	@Override
	public <T extends SimpleLogicalFormula> AssociativeFormula<T> createEmptyFormula() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Signature createEmptySignature() {
		return new ElpSignature();
	}

	@Override
	public String getOperatorSymbol() {
		return ",";
	}

	@Override
	public String getEmptySymbol() {
		return "";
	}


	@Override
	public ELPHead clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other instanceof AssociativeFormula<?>) {
			AssociativeFormula<?> cast = (AssociativeFormula<?>)other;
			return assocSupport.equals(cast.getFormulas()) && this.getClass().equals(other.getClass());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return assocSupport.hashCode() + this.getClass().hashCode();
	}
	
	@Override
	public String toString() {
		return assocSupport.toString();
	}
	
	//-------------------------------------------------------------------------
	//	COLLECTION METHODS
	//-------------------------------------------------------------------------
	
	@Override
	public boolean add(ELPLiteral arg0) {
		return assocSupport.add(arg0);
	}

	@Override
	public boolean addAll(Collection<? extends ELPLiteral> arg0) {
		return assocSupport.addAll(arg0);
	}

	@Override
	public void clear() {
		assocSupport.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		return assocSupport.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return assocSupport.containsAll(arg0);
	}

	@Override
	public boolean isEmpty() {
		return assocSupport.isEmpty();
	}

	@Override
	public Iterator<ELPLiteral> iterator() {
		return assocSupport.iterator();
	}

	@Override
	public boolean remove(Object arg0) {
		return assocSupport.remove(arg0);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return assocSupport.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return assocSupport.retainAll(arg0);
	}

	@Override
	public int size() {
		return assocSupport.size();
	}

	@Override
	public Object[] toArray() {
		return assocSupport.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return assocSupport.toArray(arg0);
	}

	@Override
	public Set<Term<?>> getTerms() {
		return assocSupport.getTerms();
	}

	@Override
	public SortedSet<ELPLiteral> getLiterals() {
		SortedSet<ELPLiteral> reval = new TreeSet<ELPLiteral>();
		for(ELPElement element : this) {
			reval.addAll(element.getLiterals());
		}
		return reval;
	}
}
