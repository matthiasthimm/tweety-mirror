package net.sf.tweety.logics.markovlogic.test;

import java.io.IOException;

import net.sf.tweety.ParserException;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;
import net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula;
import net.sf.tweety.logics.markovlogic.MarkovLogicNetwork;
import net.sf.tweety.util.Pair;
import net.sf.tweety.logics.markovlogic.*;
import net.sf.tweety.logics.markovlogic.syntax.MlnFormula;

public class ApproximationTest {

	public static void main(String[] args) throws ParserException, IOException{
		Pair<MarkovLogicNetwork,FolSignature> ex = MlnTest.iterateExamples(1, 3);
		NaiveMlnReasoner naiveReasoner = new NaiveMlnReasoner(ex.getFirst(),ex.getSecond());
		ApproximateNaiveMlnReasoner appReasoner = new ApproximateNaiveMlnReasoner(ex.getFirst(),ex.getSecond(), -1, 10000);
		for(MlnFormula f: ex.getFirst()){
			for(RelationalFormula groundFormula: f.getFormula().allGroundInstances(ex.getSecond().getConstants())){
				System.out.println(naiveReasoner.query(groundFormula).getAnswerDouble() + "\t" + appReasoner.query(groundFormula).getAnswerDouble());
			}
		}
	}
}
