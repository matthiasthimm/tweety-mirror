package net.sf.tweety.arg.prob.semantics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.arg.dung.DungTheory;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.math.equation.Equation;
import net.sf.tweety.math.equation.Statement;
import net.sf.tweety.math.probability.Probability;
import net.sf.tweety.math.term.FloatConstant;
import net.sf.tweety.math.term.FloatVariable;

/**
 * P is involutary wrt. AF if for every A,B∈Arg, if A→B then P(A)=1−P(B).
 * @author Matthias Thimm
 */
public class InvolutaryPASemantics extends AbstractPASemantics{

	/* (non-Javadoc)
	 * @see net.sf.tweety.arg.prob.semantics.AbstractPASemantics#satisfies(net.sf.tweety.arg.prob.semantics.ProbabilisticExtension, net.sf.tweety.arg.dung.DungTheory)
	 */
	@Override
	public boolean satisfies(ProbabilisticExtension p, DungTheory theory) {
		for(Attack att: theory.getAttacks()){
			if(p.probability(att.getAttacker()).doubleValue() > 1-p.probability(att.getAttacked()).doubleValue() + Probability.PRECISION ||
					p.probability(att.getAttacker()).doubleValue() < 1-p.probability(att.getAttacked()).doubleValue() - Probability.PRECISION)
				return false;
		}		
		return true;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.arg.prob.semantics.AbstractPASemantics#getSatisfactionStatement(net.sf.tweety.arg.dung.DungTheory, java.util.Map)
	 */
	@Override
	public Collection<Statement> getSatisfactionStatements(DungTheory theory, Map<Collection<Argument>, FloatVariable> worlds2vars) {
		Set<Statement> stats = new HashSet<Statement>();
		for(Attack att: theory.getAttacks()){
			stats.add(new Equation(this.probabilityTerm(att.getAttacker(), worlds2vars),new FloatConstant(1).minus(this.probabilityTerm(att.getAttacked(), worlds2vars))));			
		}
		return stats;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.arg.prob.semantics.AbstractPASemantics#toString()
	 */
	@Override
	public String toString() {
		return "Involutary Semantics";
	}

}