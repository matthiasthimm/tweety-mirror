package net.sf.tweety.logics.translators.folprop;

import java.util.HashMap;
import java.util.Map;

import net.sf.tweety.logics.translators.Translator;
import net.sf.tweety.logics.fol.syntax.Conjunction;
import net.sf.tweety.logics.fol.syntax.Disjunction;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.util.Pair;

/**
 * A Translator between the FOL and propositonal logic and vice versa.
 * @author Tim Janus
 */
public class FOLPropTranslator extends Translator {

	/** Default-Ctor */
	public FOLPropTranslator() {}
	
	/**
	 * Translates the given FOL-Atom into a Proposition
	 * @param atom	FOL-Atom, if the given Atom has
	 * arguments an exception is thrown.
	 * @return		Propositional form of the given Atom
	 */
	public Proposition toPropositional(FOLAtom atom) {
		return (Proposition) this.translateAtom(atom, Proposition.class);
	}
	
	/**
	 * Translates the given proposition into a FOL-Atom
	 * @param proposition The Proposition
	 * @return	A FOL-Atom representing the given Proposition in first order logic.
	 */
	public FOLAtom toFOL(Proposition proposition) {
		return (FOLAtom) this.translateAtom(proposition, FOLAtom.class);
	}

	/**
	 * Translates the given propositional Disjunction to a FOL Disjunction
	 * @param disjuntion	
	 * @return	The FOL Disjunction
	 */
	public Disjunction toFOL(net.sf.tweety.logics.pl.syntax.Disjunction disjuntion) {
		return (Disjunction) this.translateAssociative(disjuntion, Disjunction.class);
	}
	
	/**
	 * Translates the given FOL Disjunction to a propositional Disjunction
	 * @param disjunction	The FOL-Disjunction, if it contains formulas which
	 * 						are not expressible in propositional logic an exception
	 * 						is thrown.
	 * @return	The propositional Disjunction
	 */
	public net.sf.tweety.logics.pl.syntax.Disjunction toPropositional(Disjunction disjunction) {
		return (net.sf.tweety.logics.pl.syntax.Disjunction)
				this.translateAssociative(disjunction, net.sf.tweety.logics.pl.syntax.Disjunction.class);
	}
	
	/**
	 * Translates the given propositional Conjunction to a FOL Conjunction
	 * @param conjunction	
	 * @return	The FOL Conjunction
	 */
	public Conjunction toFOL(net.sf.tweety.logics.pl.syntax.Conjunction conjunction) {
		return (Conjunction) this.translateAssociative(conjunction, Conjunction.class);
	}
	
	/**
	 * Translates the given FOL Conjunction to a propositional Conjunction
	 * @param conjunction	The FOL-Conjunction, if it contains formulas which
	 * 						are not expressible in propositional logic an exception
	 * 						is thrown.
	 * @return	The propositional Conjunction
	 */
	public net.sf.tweety.logics.pl.syntax.Conjunction toPropositional(Conjunction conjunction) {
		return (net.sf.tweety.logics.pl.syntax.Conjunction)
				this.translateAssociative(conjunction, net.sf.tweety.logics.pl.syntax.Conjunction.class);
	}
	
	@Override
	protected Map<Class<?>, Pair<Integer, Class<?>>> createTranslateMap() {
		Map<Class<?>, Pair<Integer, Class<?>>> tmap = new HashMap<Class<?>, Pair<Integer,Class<?>>>();
		tmap.put(FOLAtom.class, new Pair<Integer, Class<?>>(TT_ATOM, Proposition.class));
		tmap.put(Proposition.class, new Pair<Integer, Class<?>>(TT_ATOM, FOLAtom.class));
		
		tmap.put(Disjunction.class, new Pair<Integer, Class<?>>(
				TT_ASSOC, net.sf.tweety.logics.pl.syntax.Disjunction.class));
		tmap.put(net.sf.tweety.logics.pl.syntax.Disjunction.class, 
				new Pair<Integer, Class<?>>(TT_ASSOC, Disjunction.class));
		
		tmap.put(Conjunction.class, new Pair<Integer, Class<?>>(
				TT_ASSOC, net.sf.tweety.logics.pl.syntax.Conjunction.class));
		tmap.put(net.sf.tweety.logics.pl.syntax.Conjunction.class, 
				new Pair<Integer, Class<?>>(TT_ASSOC, Conjunction.class));
		
		return tmap;
	}
}
