package net.sf.tweety.argumentation.dung.test;

import net.sf.tweety.argumentation.dung.DungTheory;
import net.sf.tweety.argumentation.dung.StratifiedLabelingReasoner;
import net.sf.tweety.argumentation.dung.semantics.Semantics;
import net.sf.tweety.argumentation.dung.semantics.StratifiedLabeling;
import net.sf.tweety.argumentation.dung.syntax.Argument;
import net.sf.tweety.argumentation.dung.syntax.Attack;

public class StratifiedLabelingTest {

	public static void main(String[] args){
		// create some Dung theory
		DungTheory theory = new DungTheory();
		Argument a = new Argument("a");
		Argument b = new Argument("b");
		Argument c = new Argument("c");
		Argument d = new Argument("d");
		//Argument e = new Argument("e");
		theory.add(a);
		theory.add(b);
		theory.add(c);
		theory.add(d);
		//theory.add(e);
		theory.add(new Attack(a,b));
		theory.add(new Attack(b,a));
		theory.add(new Attack(b,c));
		theory.add(new Attack(c,b));
		theory.add(new Attack(d,c));
		theory.add(new Attack(c,d));
		
		StratifiedLabelingReasoner reasoner = new StratifiedLabelingReasoner(theory,Semantics.STABLE_SEMANTICS, Semantics.CREDULOUS_INFERENCE);
		
		for(StratifiedLabeling labeling: reasoner.getLabelings()){
			System.out.println(labeling);			
		}
	}
}
