package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.Set;
import java.util.SortedSet;

import net.sf.tweety.logics.commons.syntax.interfaces.ComplexLogicalFormula;
import net.sf.tweety.logics.commons.syntax.interfaces.Term;

/**
 * This interface defines base methods every element of a
 * program has to provide. 
 * 
 * @author Tim Janus
 */
public interface ELPElement extends ComplexLogicalFormula {

	/** @return all the literals used in the rule element */
	SortedSet<ELPLiteral> getLiterals();
	
	@Override
	Set<ELPPredicate> getPredicates();
	
	@Override 
	Set<ELPAtom> getAtoms(); 
	
	@Override
	ELPElement substitute(Term<?> t, Term<?> v);
	
	@Override
	ElpSignature getSignature();
	
	@Override
	ELPElement clone();
}
