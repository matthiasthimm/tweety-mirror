package net.sf.tweety.logicprogramming.asplibrary.beliefdynamics.baserevision;

import java.util.Collection;

import net.sf.tweety.beliefdynamics.MultipleBaseRevisionOperator;
import net.sf.tweety.logicprogramming.asplibrary.solver.Solver;
import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;

public class ELPBaseRevisionOperator extends MultipleBaseRevisionOperator<Rule> {
	private Solver solver;
	private SelectionFunction<Rule> selection;
	
	public ELPBaseRevisionOperator(Solver solver, SelectionFunction<Rule> selection) {
		this.solver = solver;
		this.selection = selection;
	}

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
