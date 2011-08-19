package net.sf.tweety.beliefdynamics.operators;

import java.util.*;

import net.sf.tweety.argumentation.deductive.accumulator.*;
import net.sf.tweety.argumentation.deductive.categorizer.*;
import net.sf.tweety.beliefdynamics.*;
import net.sf.tweety.beliefdynamics.selectiverevision.*;
import net.sf.tweety.beliefdynamics.selectiverevision.argumentative.*;
import net.sf.tweety.logics.propositionallogic.*;
import net.sf.tweety.logics.propositionallogic.syntax.*;

/**
 * This class is an exemplary instantiation of a revision operator based on deductive argumentation [Krümpelmann:2011] where
 * several parameters have been fixed:
 * - the inner revision is a Levi revision which bases on the random kernel contraction
 * - the transformation function is credulous
 * - the accumulator used for deductive argumentation is the simple accumulator
 * - the categorizer used for deductive argumentation is the classical categorizer
 * 
 * @author Matthias Thimm
 */
public class ArgumentativeRevisionOperator extends MultipleBaseRevisionOperator<PropositionalFormula>{

	/* (non-Javadoc)
	 * @see net.sf.tweety.beliefdynamics.MultipleBaseRevisionOperator#revise(java.util.Collection, java.util.Collection)
	 */
	@Override
	public Collection<PropositionalFormula> revise(Collection<PropositionalFormula> base, Collection<PropositionalFormula> formulas) {		
		MultipleBaseRevisionOperator<PropositionalFormula> kernelRevision = new LeviMultipleBaseRevisionOperator<PropositionalFormula>(
				new RandomKernelContractionOperator(),
				new DefaultMultipleBaseExpansionOperator<PropositionalFormula>());		
		MultipleTransformationFunction<PropositionalFormula> transFunc = new ArgumentativeTransformationFunction(
				new ClassicalCategorizer(),
				new SimpleAccumulator(),
				new PlBeliefSet(base),
				false);		
		MultipleSelectiveRevisionOperator<PropositionalFormula> rev = new MultipleSelectiveRevisionOperator<PropositionalFormula>(transFunc, kernelRevision);		
		return rev.revise(base, formulas);
	}

}
