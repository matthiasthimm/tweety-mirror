package net.sf.tweety.argumentation.probabilistic.test;

import net.sf.tweety.argumentation.dung.DungTheory;
import net.sf.tweety.argumentation.dung.syntax.Argument;
import net.sf.tweety.argumentation.dung.syntax.Attack;
import net.sf.tweety.argumentation.probabilistic.PartialProbabilityAssignment;
import net.sf.tweety.argumentation.probabilistic.analysis.PAInconsistencyMeasure;
import net.sf.tweety.math.norm.ManhattanNorm;
import net.sf.tweety.math.probability.Probability;

public class IncProbTest {
	public static void main(String[] args){
		DungTheory theory = new DungTheory();
		Argument a = new Argument("A");
		Argument b = new Argument("B");
		theory.add(a);
		theory.add(b);
		theory.add(new Attack(a,b));
		
		PartialProbabilityAssignment ppa = new PartialProbabilityAssignment();
		ppa.put(a, new Probability(0.2));
		
		PAInconsistencyMeasure mes = new PAInconsistencyMeasure(new ManhattanNorm(), theory, PAInconsistencyMeasure.DISTANCE_WRT_JUSTIFIABILITY);
		
		System.out.println(mes.inconsistencyMeasure(ppa));
	}
}
