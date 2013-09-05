package net.sf.tweety.logics.propositionallogic.test;

import java.io.IOException;

import net.sf.tweety.ParserException;
import net.sf.tweety.logics.propositionallogic.PlBeliefSet;
import net.sf.tweety.logics.propositionallogic.Sat4jEntailment;
import net.sf.tweety.logics.propositionallogic.parser.PlParser;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;

public class SatTest {
	public static void main(String[] args) throws ParserException, IOException{
		PlBeliefSet beliefSet = new PlBeliefSet();
		PlParser parser = new PlParser();
		beliefSet.add((PropositionalFormula)parser.parseFormula("a || b || c"));
		beliefSet.add((PropositionalFormula)parser.parseFormula("!a || b"));
		beliefSet.add((PropositionalFormula)parser.parseFormula("!b || c"));
		beliefSet.add((PropositionalFormula)parser.parseFormula("!c || (!a && !b && !c && !d)"));
		
		System.out.println(beliefSet);
		PropositionalFormula f = beliefSet.toCnf();
		System.out.println(f);
		
		Sat4jEntailment entail = new Sat4jEntailment();
		
		System.out.println(entail.isConsistent(beliefSet));
		
	}
}
