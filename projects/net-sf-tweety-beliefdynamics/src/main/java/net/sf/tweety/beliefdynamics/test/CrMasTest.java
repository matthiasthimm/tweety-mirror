package net.sf.tweety.beliefdynamics.test;

import java.io.*;
import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.agents.*;
import net.sf.tweety.beliefdynamics.*;
import net.sf.tweety.beliefdynamics.kernels.*;
import net.sf.tweety.beliefdynamics.mas.*;
import net.sf.tweety.beliefdynamics.operators.*;
import net.sf.tweety.graphs.orders.*;
import net.sf.tweety.logics.propositionallogic.ClassicalEntailment;
import net.sf.tweety.logics.propositionallogic.parser.*;
import net.sf.tweety.logics.propositionallogic.syntax.*;

public class CrMasTest {

	public static void main(String[] args) throws ParserException, IOException{		
		//TweetyLogging.logLevel = TweetyConfiguration.LogLevel.TRACE;
		//TweetyLogging.initLogging();
		PlParser parser = new PlParser();
		
		// some agents
		List<Agent> agents = new ArrayList<Agent>();
		agents.add(new DummyAgent("A1"));
		agents.add(new DummyAgent("A2"));
		agents.add(new DummyAgent("A3"));
		
		// some credibility order A3 < A2 < A1 (A1 is most credible)
		Order<Agent> credOrder = new Order<Agent>(agents);
		credOrder.setOrderedBefore(agents.get(0), agents.get(1));
		credOrder.setOrderedBefore(agents.get(1), agents.get(2));
		
		// a belief base (we use propositional logic)
		CrMasBeliefSet<PropositionalFormula> bs = new CrMasBeliefSet<PropositionalFormula>(credOrder);
		bs.add(new InformationObject<PropositionalFormula>((PropositionalFormula) parser.parseFormula("!c"), agents.get(1)));
		bs.add(new InformationObject<PropositionalFormula>((PropositionalFormula) parser.parseFormula("b"), agents.get(2)));
		bs.add(new InformationObject<PropositionalFormula>((PropositionalFormula) parser.parseFormula("!b||!a"), agents.get(2)));
		
		// some new information
		Collection<InformationObject<PropositionalFormula>> newInformation = new HashSet<InformationObject<PropositionalFormula>>();
		newInformation.add(new InformationObject<PropositionalFormula>((PropositionalFormula) parser.parseFormula("a"), agents.get(2)));
		newInformation.add(new InformationObject<PropositionalFormula>((PropositionalFormula) parser.parseFormula("!a||c"), agents.get(2)));		
		
		System.out.println(bs + " * " + newInformation);
		System.out.println();
				
		// simple prioritized revision (without considering credibilities)
		CrMasRevisionWrapper<PropositionalFormula> rev = new CrMasRevisionWrapper<PropositionalFormula>(
				new LeviMultipleBaseRevisionOperator<PropositionalFormula>(
						new KernelContractionOperator<PropositionalFormula>(new RandomIncisionFunction<PropositionalFormula>(), new ClassicalEntailment()),
						new DefaultMultipleBaseExpansionOperator<PropositionalFormula>()
						));
		System.out.println("PRIO       :\t " + rev.revise(bs, newInformation));
		
		// simple non-prioritized revision (with credibilities)
		CrMasSimpleRevisionOperator rev2 = new CrMasSimpleRevisionOperator();
		System.out.println("N-PRIO CRED:\t " + rev2.revise(bs, newInformation));
		
		// credibility-based argumentative revision
		CrMasArgumentativeRevisionOperator theRevision = new CrMasArgumentativeRevisionOperator();		
		System.out.println("ARG        :\t " + theRevision.revise(bs, newInformation));
		
	}
}
