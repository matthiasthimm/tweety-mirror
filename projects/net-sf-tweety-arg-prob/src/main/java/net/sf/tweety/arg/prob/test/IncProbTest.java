package net.sf.tweety.arg.prob.test;

import net.sf.tweety.arg.dung.DungTheory;
import net.sf.tweety.arg.dung.syntax.Argument;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.arg.prob.PartialProbabilityAssignment;
import net.sf.tweety.arg.prob.analysis.PAInconsistencyMeasure;
import net.sf.tweety.math.norm.ManhattanNorm;
import net.sf.tweety.math.probability.Probability;

public class IncProbTest {
	public static void main(String[] args){
		DungTheory theory = new DungTheory();
		Argument a = new Argument("A");
		Argument b = new Argument("B");
		Argument c = new Argument("C");
		theory.add(a);
		theory.add(b);
		theory.add(c);
		theory.add(new Attack(a,b));
		theory.add(new Attack(b,c));
		theory.add(new Attack(a,c));
		
		PartialProbabilityAssignment ppa = new PartialProbabilityAssignment();
		ppa.put(b, new Probability(0.7));
		ppa.put(c, new Probability(0.6));
		
		PAInconsistencyMeasure mes = new PAInconsistencyMeasure(new ManhattanNorm(), theory, PAInconsistencyMeasure.DISTANCE_WRT_JUSTIFIABILITY, PAInconsistencyMeasure.NOT_IMPOSE_RATIONALITY);
		
		System.out.println(mes.inconsistencyMeasure(ppa));
	}
}
