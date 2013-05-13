package net.sf.tweety.logics.markovlogic.test;

import java.io.IOException;

import net.sf.tweety.ParserException;
import net.sf.tweety.logics.commons.syntax.Constant;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.firstorderlogic.parser.FolParser;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;
import net.sf.tweety.logics.markovlogic.MarkovLogicNetwork;
import net.sf.tweety.logics.markovlogic.NaiveMlnReasoner;
import net.sf.tweety.logics.markovlogic.syntax.MlnFormula;

public class ProbTest {

	public static void main(String[] args) throws ParserException, IOException{
		Predicate a = new Predicate("a",1);
		Predicate b = new Predicate("b",1);
		Predicate c = new Predicate("c",1);
		Predicate d = new Predicate("d",1);
		
		FolSignature sig = new FolSignature();
		sig.add(a);
		sig.add(b);
		sig.add(c);
		sig.add(d);
		
		sig.add(new Constant("d1"));
		
		FolParser parser = new FolParser();
		parser.setSignature(sig);
		
		MarkovLogicNetwork mln = new MarkovLogicNetwork();
		
		FolFormula f = (FolFormula)parser.parseFormula("a(X) && b(X) || c(X) && d(X)");
		double p = 0.1;		
		
		double factor = f.getSatisfactionRatio(); 
		
		double w = Math.log(p/(1-p)*factor);
		
		mln.add(new MlnFormula(f, w));
		
		NaiveMlnReasoner reasoner = new NaiveMlnReasoner(mln,sig);
		reasoner.setTempDirectory("/Users/mthimm/Desktop/tmp");
		
		System.out.println(w + "\t\t" + reasoner.query((FolFormula)parser.parseFormula("a(d1) && b(d1) || c(d1) && d(d1)")).getAnswerDouble());
		

	}
}
