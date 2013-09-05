package net.sf.tweety.logics.propositionallogic;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import net.sf.tweety.EntailmentRelation;
import net.sf.tweety.logics.propositionallogic.syntax.Conjunction;
import net.sf.tweety.logics.propositionallogic.syntax.Disjunction;
import net.sf.tweety.logics.propositionallogic.syntax.Negation;
import net.sf.tweety.logics.propositionallogic.syntax.Proposition;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalSignature;

/**
 * Uses the Sat4j library for SAT solving.
 * @author Matthias Thimm
 *
 */
public class Sat4jEntailment extends EntailmentRelation<PropositionalFormula> {

	/** The solver actuall used. */
	private ISolver solver = null;

	/** Max number of variables for this solver. */
	private static final int MAXVAR = 1000000;
	/** Max number of expected clauses for this solver. */
	private static final int NBCLAUSES = 500000;
	
	/**
	 * Initializes the solver.
	 */
	private void init(){
		this.solver = SolverFactory.newLight();
		this.solver.newVar(Sat4jEntailment.MAXVAR);
		this.solver.setExpectedNumberOfClauses(Sat4jEntailment.NBCLAUSES);		
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.EntailmentRelation#entails(java.util.Collection, net.sf.tweety.Formula)
	 */
	@Override
	public boolean entails(Collection<PropositionalFormula> formulas, PropositionalFormula formula) {
		if(this.solver == null) this.init();
		this.solver.reset();
		Set<PropositionalFormula> fset = new HashSet<PropositionalFormula>(formulas);
		fset.add((PropositionalFormula)formula.complement());
		return !this.isConsistent(fset);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.EntailmentRelation#isConsistent(java.util.Collection)
	 */
	@Override
	public boolean isConsistent(Collection<PropositionalFormula> formulas) {
		if(this.solver == null) this.init();
		this.solver.reset();
		PropositionalSignature sig = new PropositionalSignature();
		for(PropositionalFormula f: formulas)
			sig.addAll(f.getAtoms());		
		Map<Proposition, Integer> prop2Idx = new HashMap<Proposition, Integer>();
		Proposition[] idx2Prop = new Proposition[sig.size()];
		int i = 0;
		for(Proposition p: sig){
			prop2Idx.put(p, i);
			idx2Prop[i++] = p;
		}
		try{
			for(PropositionalFormula f: formulas){
				Conjunction conj = f.toCnf();
				for(PropositionalFormula f2: conj){
					Disjunction disj = (Disjunction) f2;
					int[] clause = new int[disj.size()];
					i = 0;
					for(PropositionalFormula f3: disj){
						if(f3 instanceof Proposition){
							clause[i++] = prop2Idx.get(f3) + 1; 
						}else if(f3 instanceof Negation){
							clause[i++] = - prop2Idx.get(((Negation)f3).getFormula()) - 1;
						}else throw new RuntimeException("Unexpected formula type in conjunctive normal form: " + f3.getClass());
					}
					this.solver.addClause(new VecInt(clause));
				}
			}
			return this.solver.isSatisfiable();
		}catch(ContradictionException e){
			return false;
		} catch (TimeoutException e) {
			throw new RuntimeException(e);
		}
	}

}
