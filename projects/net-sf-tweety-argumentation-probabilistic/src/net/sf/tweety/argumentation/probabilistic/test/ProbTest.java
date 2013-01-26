package net.sf.tweety.argumentation.probabilistic.test;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import net.sf.tweety.ParserException;
import net.sf.tweety.Reasoner;
import net.sf.tweety.argumentation.deductive.semantics.DeductiveArgument;
import net.sf.tweety.argumentation.probabilistic.ArgMeReasoner;
import net.sf.tweety.argumentation.probabilistic.ProbabilisticKnowledgebase;
import net.sf.tweety.logics.propositionallogic.parser.PlParser;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;
import net.sf.tweety.math.probability.Probability;
import net.sf.tweety.util.RandomSubsetIterator;

public class ProbTest {

	public static void main(String[] args) throws ParserException, IOException{
		// Create some knowledge base
		ProbabilisticKnowledgebase myKb = new ProbabilisticKnowledgebase();
		PlParser parser = new PlParser();
		// add formulas
		myKb.getKb().add((PropositionalFormula)parser.parseFormula("a"));
		myKb.getKb().add((PropositionalFormula)parser.parseFormula("b"));
		myKb.getKb().add((PropositionalFormula)parser.parseFormula("!a || !b"));
		myKb.getKb().add((PropositionalFormula)parser.parseFormula("!c || d"));
		// add probability assignments
		myKb.getProbabilityAssignments().put((PropositionalFormula)parser.parseFormula("a || b"), new Probability(0.9));
		myKb.getProbabilityAssignments().put((PropositionalFormula)parser.parseFormula("c"), new Probability(0.3));
		// add "some" probabilities to sets of arguments
		Set<DeductiveArgument> arguments = myKb.getKb().getDeductiveArguments();
		Iterator<Set<DeductiveArgument>> it = new RandomSubsetIterator<DeductiveArgument>(arguments, true);
		myKb.getProbFunction().put(it.next(), new Probability(0.3));
		myKb.getProbFunction().put(it.next(), new Probability(0.3));
		myKb.getProbFunction().put(it.next(), new Probability(0.4));
		
		// List signature and knowledge base
		System.out.println("Signature: " + myKb.getSignature());
		System.out.println("Knowledge base: " + myKb);
		System.out.println("Arguments of knowledge base: " + myKb.getKb().getDeductiveArguments());
		System.out.println("#Arguments of knowledge base: " + myKb.getKb().getDeductiveArguments().size());
		System.out.println();
		
		// Compute ME-function for the above knowledge base and give some probabilities to queries
		Reasoner reasoner = new ArgMeReasoner(myKb);
		System.out.println("P(a) = " + reasoner.query(parser.parseFormula("a")));
		System.out.println("P(b) = " + reasoner.query(parser.parseFormula("b")));
		System.out.println("P(a || b) = " + reasoner.query(parser.parseFormula("a || b")));
		System.out.println("P(a && c) = " + reasoner.query(parser.parseFormula("a && c")));
		
	}
}
