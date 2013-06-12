package net.sf.tweety.argumentation.deductive.test;

import java.io.IOException;

import net.sf.tweety.*;
import net.sf.tweety.argumentation.deductive.CompilationReasoner;
import net.sf.tweety.argumentation.deductive.DeductiveKnowledgeBase;
import net.sf.tweety.argumentation.deductive.SimpleReasoner;
import net.sf.tweety.argumentation.deductive.accumulator.SimpleAccumulator;
import net.sf.tweety.argumentation.deductive.categorizer.ClassicalCategorizer;
import net.sf.tweety.logics.propositionallogic.parser.PlParser;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;

/**
 * 
 * For testing purposes.
 * 
 * @author Matthias Thimm
 *
 */
public class DeductiveTest {

	public static void main(String[] args) throws ParserException, IOException{
		TweetyLogging.logLevel = TweetyConfiguration.LogLevel.TRACE;
		TweetyLogging.initLogging();
		DeductiveKnowledgeBase kb = new DeductiveKnowledgeBase();
		
		PlParser parser = new PlParser();
		kb.add((PropositionalFormula)parser.parseFormula("s"));
		kb.add((PropositionalFormula)parser.parseFormula("!s || h"));
		kb.add((PropositionalFormula)parser.parseFormula("l"));
		kb.add((PropositionalFormula)parser.parseFormula("!l || m"));
		kb.add((PropositionalFormula)parser.parseFormula("!m || h"));
		kb.add((PropositionalFormula)parser.parseFormula("!m || !f"));
		kb.add((PropositionalFormula)parser.parseFormula("f"));		
		kb.add((PropositionalFormula)parser.parseFormula("!f || !h"));
		kb.add((PropositionalFormula)parser.parseFormula("v"));
		kb.add((PropositionalFormula)parser.parseFormula("!v || !h"));
		
		System.out.println(kb);
		
		Reasoner reasoner = new SimpleReasoner(kb, new ClassicalCategorizer(), new SimpleAccumulator());
		
		System.out.println(reasoner.query(parser.parseFormula("f")).getAnswerDouble());
		
	}
	
}
