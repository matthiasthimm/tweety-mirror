package net.sf.tweety.logicprogramming.asplibrary.syntax;

import net.sf.tweety.logics.commons.syntax.ComplexLogicalFormulaAdapter;
import net.sf.tweety.logics.commons.syntax.Predicate;

/**
 * This acts as abstract base class for classes implement 
 * the ELPElement interface
 * @author Tim Janus
 */
public abstract class ELPElementAdapter 
	extends ComplexLogicalFormulaAdapter 
	implements ELPElement {

	@Override
	public Class<? extends Predicate> getPredicateCls() {
		return ELPPredicate.class;
	}
	
	public abstract ELPElement clone();
}
