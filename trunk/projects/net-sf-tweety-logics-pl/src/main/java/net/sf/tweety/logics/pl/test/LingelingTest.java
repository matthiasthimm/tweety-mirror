package net.sf.tweety.logics.pl.test;

import java.io.IOException;

import net.sf.tweety.ParserException;
import net.sf.tweety.logics.pl.DefaultConsistencyTester;
import net.sf.tweety.logics.pl.LingelingEntailment;
import net.sf.tweety.logics.pl.PlBeliefSet;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

public class LingelingTest {
public static void main(String[] args) throws ParserException, IOException{
		
		
		PlBeliefSet beliefSet = new PlBeliefSet();
		PlParser parser = new PlParser();
		beliefSet.add((PropositionalFormula)parser.parseFormula("a || b || c"));
		beliefSet.add((PropositionalFormula)parser.parseFormula("!a || b && d"));
		beliefSet.add((PropositionalFormula)parser.parseFormula("a"));
		beliefSet.add((PropositionalFormula)parser.parseFormula("!c"));
		
		System.out.println(beliefSet);
		
		DefaultConsistencyTester tester = new DefaultConsistencyTester(new LingelingEntailment("/Users/mthimm/Projects/misc_bins/lingeling"));
		
		System.out.println(tester.getWitness(beliefSet));
	}
}