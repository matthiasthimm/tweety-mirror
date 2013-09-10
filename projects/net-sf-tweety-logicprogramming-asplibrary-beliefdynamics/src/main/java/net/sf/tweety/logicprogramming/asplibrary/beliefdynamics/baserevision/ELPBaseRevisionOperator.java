package net.sf.tweety.logicprogramming.asplibrary.beliefdynamics.baserevision;

import java.util.Collection;

import net.sf.tweety.beliefdynamics.MultipleBaseRevisionOperator;
import net.sf.tweety.logicprogramming.asplibrary.solver.Solver;
import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;

/**
 * This class implements the base revision operator for extended
 * logic programs as introduced in [KKI12]. The revision of a set
 * of rules is defined as the screened maxi-choice consolidation of the
 * union of the belief base and the new beliefs.
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
public class ELPBaseRevisionOperator extends MultipleBaseRevisionOperator<Rule> {
	private Solver solver;
	private SelectionFunction<Rule> selection;
	
	public ELPBaseRevisionOperator(Solver solver, SelectionFunction<Rule> selection) {
		this.solver = solver;
		this.selection = selection;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.beliefdynamics.MultipleBaseRevisionOperator#revise(java.util.Collection, java.util.Collection)
	 */
	@Override
	public Collection<Rule> revise(Collection<Rule> base,
			Collection<Rule> formulas) {
		Program newKnowledge = new Program(formulas);		
		
		ScreenedMaxichoiceConsolidation consolidationOperator = new ScreenedMaxichoiceConsolidation(newKnowledge, selection, solver);
		Program union = new Program();
		union.addAll(base);
		union.addAll(formulas);
		Program result;
		try {
			result = consolidationOperator.consolidate(union);
		} catch (SolverException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

}
