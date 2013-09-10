package net.sf.tweety.logicprogramming.asplibrary.beliefdynamics.baserevision;

import java.util.Collection;
import java.util.Collections;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;

/**
 * This class implements a monotone global maxichoise selection function
 * for remainder sets of extended logic programs as introduced in [KKI12]. A selection
 * function is monotone if it treats 
 * 
 *  [KKI12] Krümpelmann, Patrick und Gabriele Kern-Isberner: 
 * 	Belief Base Change Operations for Answer Set Programming. 
 *  In: Cerro, Luis Fariñas, Andreas Herzig und Jérôme Mengin (Herausgeber):
 *  Proceedings of the 13th European conference on Logics in Artificial 
 *  Intelligence, Band 7519, Seiten 294–306, Toulouse, France, 2012. 
 *  Springer Berlin Heidelberg.
 *  
 * @author Sebastian Homann
 */
public class MonotoneGlobalMaxichoiceSelectionFunction implements SelectionFunction<Rule> {

	/**
	 * Selects the maximal remainder set from the set of all remainder sets according to
	 * a total order on all extended logic programs. This entails the monotony-property
	 * for this selection function.
	 * @param remainderSets set of all remainder sets
	 * @return a single remainder set or P, if there is no remainder set of P with screen R
	 */
	public Collection<Rule> select(ScreenedRemainderSets remainderSets) {
		if(remainderSets.isEmpty()) {
			return remainderSets.getSourceBeliefBase();
		}
		
		return Collections.max(remainderSets.asPrograms(), new ELPLexicographicalComparator());
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.logicprogramming.asplibrary.beliefdynamics.baserevision.SelectionFunction#select(net.sf.tweety.logicprogramming.asplibrary.beliefdynamics.baserevision.RemainderSets)
	 */
	@Override
	public Collection<Rule> select(RemainderSets<Rule> remainderSets) {
		if(remainderSets instanceof ScreenedRemainderSets) {
			return select((ScreenedRemainderSets) remainderSets);
		}
		return null;
	}

}
