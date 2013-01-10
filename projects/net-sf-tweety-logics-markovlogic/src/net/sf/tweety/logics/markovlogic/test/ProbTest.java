package net.sf.tweety.logics.markovlogic.test;

import java.io.IOException;

import net.sf.tweety.ParserException;
import net.sf.tweety.logics.firstorderlogic.parser.FolParser;
import net.sf.tweety.logics.firstorderlogic.syntax.Constant;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import net.sf.tweety.logics.markovlogic.MarkovLogicNetwork;
import net.sf.tweety.logics.markovlogic.NaiveMlnReasoner;
import net.sf.tweety.logics.markovlogic.syntax.MlnFormula;

public class ProbTest {

	public static void main(String[] args) throws ParserException, IOException{
		Predicate a = new Predicate("a",1);
		Predicate b = new Predicate("b",1);
		Predicate c = new Predicate("c",1);
		
		FolSignature sig = new FolSignature();
		sig.add(a);
		sig.add(b);
		sig.add(c);
		
		sig.add(new Constant("d1"));
		
		FolParser parser = new FolParser();
		parser.setSignature(sig);
		
		MarkovLogicNetwork mln = new MarkovLogicNetwork();
		
		FolFormula f = (FolFormula)parser.parseFormula("a(X) && b(X)");
		
		
		
		double p = 0.9;		
		double w = Math.log(p/(1-p)*3);
		
		mln.add(new MlnFormula(f, w));
		
		NaiveMlnReasoner reasoner = new NaiveMlnReasoner(mln,sig);
		reasoner.setTempDirectory("/Users/mthimm/Desktop/tmp");
		
		System.out.println(w + "\t\t" + reasoner.query((FolFormula)parser.parseFormula("a(d1) && b(d1)")).getAnswerDouble());
		

	}
}
