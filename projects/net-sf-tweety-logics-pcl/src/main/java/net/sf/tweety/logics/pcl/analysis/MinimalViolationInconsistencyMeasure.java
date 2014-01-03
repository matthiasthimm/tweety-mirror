package net.sf.tweety.logics.pcl.analysis;

import java.util.Set;

import net.sf.tweety.InconsistencyMeasure;
import net.sf.tweety.logics.pcl.PclBeliefSet;
import net.sf.tweety.logics.pcl.syntax.ProbabilisticConditional;
import net.sf.tweety.logics.pl.semantics.PossibleWorld;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class models the minimal violation inconsistency measure.
 * 
 * @author Nico Potyka
 */
public abstract class MinimalViolationInconsistencyMeasure implements InconsistencyMeasure<PclBeliefSet> {

	/**
	 * Logger.
	 */
	static protected Logger log = LoggerFactory.getLogger(MinimalViolationInconsistencyMeasure.class);

	/**
	 * Set world constraints (1-p, -p, 0) in variable vector for current conditional c. 
	 * @param worlds
	 * @param c
	 * @param variableVector
	 */
	protected void setWorldConstraints(Set<PossibleWorld> worlds, ProbabilisticConditional c, double[] variableVector) {
		
		log.debug("<Call> setWorldConstraints(worlds, "+c.toString()+", variableVector)");
		
		int i = 1; //LPSolve starts indexing at 1
		double p = c.getProbability().doubleValue();
		PropositionalFormula conclusion = c.getConclusion();
		
		
		if(c.isFact()) {
			
			log.debug(c.toString()+" is fact.");
			
			for(PossibleWorld w: worlds) {
				if(w.satisfies(conclusion)) {
					variableVector[i] = 1-p;
				}
				else {
					variableVector[i] = -p;
				}
				
				i++;
			}
			
		}
		else {
			
			log.debug(c.toString()+" is conditional.");
			
			PropositionalFormula premise = c.getPremise().iterator().next();
			
			for(PossibleWorld w: worlds) {
				
				if(w.satisfies(premise)) {
					
					if(w.satisfies(conclusion)) {
						variableVector[i] = 1-p;
					}
					else {
						variableVector[i] = -p;
					}
					
				}
				else {
					
					variableVector[i] = 0;
				}
				
				i++;
			}
		}
		

		log.debug("<Leave> setWorldConstraints(worlds, "+c.toString()+", variableVector)");
		
	}

}
