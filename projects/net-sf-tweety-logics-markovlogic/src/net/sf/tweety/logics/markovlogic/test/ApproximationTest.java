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
		ApproximateNaiveMlnReasoner appReasoner = new ApproximateNaiveMlnReasoner(ex.getFirst(),ex.getSecond(), -1, 100000);
		for(MlnFormula f: ex.getFirst()){
			for(RelationalFormula groundFormula: f.getFormula().allGroundInstances(ex.getSecond().getConstants())){
				System.out.println(appReasoner.query(groundFormula).getAnswerDouble());
				break;
			}
		}
	}
}
