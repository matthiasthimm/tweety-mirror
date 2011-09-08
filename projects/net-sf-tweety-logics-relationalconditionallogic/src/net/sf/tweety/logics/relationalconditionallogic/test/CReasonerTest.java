package net.sf.tweety.logics.relationalconditionallogic.test;

import net.sf.tweety.*;
import net.sf.tweety.logics.firstorderlogic.syntax.*;
import net.sf.tweety.logics.relationalconditionallogic.*;
import net.sf.tweety.logics.relationalconditionallogic.semantics.*;
import net.sf.tweety.logics.relationalconditionallogic.syntax.*;

public class CReasonerTest {
	public static void main(String[] args){
		TweetyLogging.logLevel = TweetyConfiguration.LogLevel.DEBUG;
		TweetyLogging.initLogging();
		
		Predicate p = new Predicate("p",1);
		Predicate q = new Predicate("q",1);
		Variable x = new Variable("X");
		Constant a = new Constant("a");
		Constant b = new Constant("b");
		
		RelationalConditional c1 = new RelationalConditional(new Atom(p).addArgument(x),new Atom(q).addArgument(x));
		RelationalConditional c2 = new RelationalConditional(new Atom(q).addArgument(a));
		RelationalConditional c3 = new RelationalConditional(new Atom(p).addArgument(b));
		
		RclBeliefSet bs = new RclBeliefSet();
		bs.add(c1);
		bs.add(c2);
		bs.add(c3);
		
		System.out.println(bs);
		
		RelationalBruteForceCReasoner reasoner = new RelationalBruteForceCReasoner(bs);
		
		RelationalRankingFunction rf = reasoner.getCRepresentation();
		System.out.println(reasoner.getCRepresentation());
		
		System.out.println(rf.satisfies((BeliefBase)bs));
		
	}
}
