package net.sf.tweety.logics.commons.analysis;

import java.util.Collection;

import net.sf.tweety.Formula;

/**
 * This class models the drastic inconsistency measure.
 * 
 * @author Matthias Thimm
 */
public class DrasticInconsistencyMeasure<S extends Formula> extends BeliefSetInconsistencyMeasure<S> {

	/** The consistency tester used for measuring. */
	private BeliefSetConsistencyTester<S> consTester;
	
	/**
	 * Creates a new drastic inconsistency measure.
	 * @param consTester some consistency tester
	 */
	public DrasticInconsistencyMeasure(BeliefSetConsistencyTester<S> consTester){
		this.consTester = consTester;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.commons.analysis.BeliefSetInconsistencyMeasure#inconsistencyMeasure(java.util.Collection)
	 */
	@Override
	public Double inconsistencyMeasure(Collection<S> formulas) {
		if(this.consTester.isConsistent(formulas)) return 0d;
		return 1d;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "drastic";
	}
}