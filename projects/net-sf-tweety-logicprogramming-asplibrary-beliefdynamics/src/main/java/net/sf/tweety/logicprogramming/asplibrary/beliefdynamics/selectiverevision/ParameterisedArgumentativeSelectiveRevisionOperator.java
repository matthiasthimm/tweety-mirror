package net.sf.tweety.logicprogramming.asplibrary.beliefdynamics.selectiverevision;

import java.util.Collection;

import net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack.AttackStrategy;
import net.sf.tweety.beliefdynamics.MultipleBaseRevisionOperator;
import net.sf.tweety.beliefdynamics.selectiverevision.MultipleSelectiveRevisionOperator;
import net.sf.tweety.beliefdynamics.selectiverevision.MultipleTransformationFunction;
import net.sf.tweety.logicprogramming.asplibrary.beliefdynamics.baserevision.ELPBaseRevisionOperator;
import net.sf.tweety.logicprogramming.asplibrary.beliefdynamics.baserevision.MonotoneGlobalMaxichoiceSelectionFunction;
import net.sf.tweety.logicprogramming.asplibrary.beliefdynamics.baserevision.SelectionFunction;
import net.sf.tweety.logicprogramming.asplibrary.solver.Solver;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;


/**
 * This class represents a selective revision using the base revision approach
 *  from [1] for the inner revision and the sceptical argumentative transformation 
 *  function from [2]. The selective revision operator is parameterised by two
 *  notions of attack used by the argumentation framework which in turn is invoked 
 *  by the transformation function. In [2] it is shown, that there are 4 classes
 *  of distinct plausible instantiatiations of this operator: a/a, d/d, sa/sa and sa/a
 *  where "a" stands for Attack, "d" for Defeat, and "sa" for Strong Attack.
 *  For further information see net.sf.tweety.argumentation.parameterisedhierarchy and [2]. 
 * 
 * [1] Krümpelmann, Patrick und Gabriele Kern-Isberner: 
 * 	Belief Base Change Operations for Answer Set Programming. 
 *  In: Cerro, Luis Fariñas, Andreas Herzig und Jérôme Mengin (Herausgeber):
 *  Proceedings of the 13th European conference on Logics in Artificial 
 *  Intelligence, Band 7519, Seiten 294–306, Toulouse, France, 2012. 
 *  Springer Berlin Heidelberg.
 * 
 * [2] Homann, Sebastian:
 *  Master thesis: Argumentationsbasierte selektive Revision von erweiterten logischen
 *  Programmen.
 * 
 * @author Sebastian Homann
 *
 */
public class ParameterisedArgumentativeSelectiveRevisionOperator extends
		MultipleBaseRevisionOperator<Rule> {
	
	private Solver solver;
	private AttackStrategy attackRelation;
	private AttackStrategy defenseRelation;

	/**
	 * Constructs a new selective revision operator using the given attack relations
	 * and a asp solver.
	 * @param solver an answer set solver
	 * @param attackRelation a notion of attack
	 * @param defenseRelation a notion of attack
	 */
	public ParameterisedArgumentativeSelectiveRevisionOperator(Solver solver, AttackStrategy attackRelation, AttackStrategy defenseRelation) {
		this.solver = solver;
		this.attackRelation = attackRelation;
		this.defenseRelation = defenseRelation;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.beliefdynamics.MultipleBaseRevisionOperator#revise(java.util.Collection, java.util.Collection)
	 */
	@Override
	public Collection<Rule> revise(Collection<Rule> base,
			Collection<Rule> formulas) {
		
		// inner revision operator: base revision
		SelectionFunction<Rule> selection = new MonotoneGlobalMaxichoiceSelectionFunction();		
		MultipleBaseRevisionOperator<Rule> innerRevision;
		innerRevision = new ELPBaseRevisionOperator(solver, selection);
		
		// transformation function
		MultipleTransformationFunction<Rule> transformationFunction;
		transformationFunction = new ScepticalLiteralTransformationFunction(base, attackRelation, defenseRelation);

		MultipleSelectiveRevisionOperator<Rule> revisionOperator;
		revisionOperator = new MultipleSelectiveRevisionOperator<Rule>(transformationFunction, innerRevision);
		
		return revisionOperator.revise(base, formulas);
	}

}
