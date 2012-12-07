package net.sf.tweety.logics.markovlogic.test;

import java.io.IOException;

import net.sf.tweety.ParserException;
import net.sf.tweety.logics.firstorderlogic.parser.FolParser;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;
import net.sf.tweety.logics.markovlogic.AlchemyMlnReasoner;
import net.sf.tweety.logics.markovlogic.MarkovLogicNetwork;
import net.sf.tweety.util.Pair;

public class AlchemyTest {

	public static void main(String[] args) throws ParserException, IOException{
		Pair<MarkovLogicNetwork,FolSignature> exp1 = MlnTest.SmokersExample(4);
		AlchemyMlnReasoner reasoner = new AlchemyMlnReasoner(exp1.getFirst(),exp1.getSecond());
		FolParser parser = new FolParser();
		parser.setSignature(exp1.getSecond());
		FolFormula query = (FolFormula) parser.parseFormula("cancer(d0)");
		reasoner.setAlchemyInferenceCommand("/Users/mthimm/Desktop/infer");
		reasoner.query(query);
	}
}
