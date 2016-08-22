package net.sf.tweety.arg.aspic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import org.junit.Test;

import net.sf.tweety.arg.aspic.order.LastLinkOrder;
import net.sf.tweety.arg.aspic.order.RuleComparator;
import net.sf.tweety.arg.aspic.order.SimpleAspicOrder;
import net.sf.tweety.arg.aspic.order.WeakestLinkOrder;
import net.sf.tweety.arg.aspic.parser.AspicParser;
import net.sf.tweety.arg.aspic.ruleformulagenerator.FolFormulaGenerator;
import net.sf.tweety.arg.aspic.ruleformulagenerator.PlFormulaGenerator;
import net.sf.tweety.arg.aspic.semantics.AspicAttack;
import net.sf.tweety.arg.aspic.syntax.AspicArgument;
import net.sf.tweety.arg.aspic.syntax.DefeasibleInferenceRule;
import net.sf.tweety.arg.aspic.syntax.InferenceRule;
import net.sf.tweety.arg.aspic.syntax.StrictInferenceRule;
import net.sf.tweety.arg.dung.DungTheory;
import net.sf.tweety.arg.dung.syntax.Attack;
import net.sf.tweety.commons.util.rules.DerivationGraph;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.parser.FolParser;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.pl.parser.PlParser;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

/**
 * @author Nils Geilen
 * Several JUnit test for the package arg.aspic
 */
public class AspicTest {
	
	@SuppressWarnings("unchecked")
	@Test  public void Example1() throws Exception {
		AspicParser<PropositionalFormula> parser = new AspicParser<>(new PlParser(), new PlFormulaGenerator());
		AspicArgumentationTheory<PropositionalFormula> at = parser.parseBeliefBaseFromFile("../../examples/aspic/ex1.aspic");
		
		AspicArgument<PropositionalFormula> A1 = new AspicArgument<>((InferenceRule<PropositionalFormula>)parser.parseFormula("->p"));
		AspicArgument<PropositionalFormula> A2 = new AspicArgument<>((InferenceRule<PropositionalFormula>)parser.parseFormula("d1: p => q"));
		A2.addDirectSub(A1);
		AspicArgument<PropositionalFormula> A3 = new AspicArgument<>((InferenceRule<PropositionalFormula>)parser.parseFormula("s1: p, q -> r"));
		A3.addDirectSub(A1);
		A3.addDirectSub(A2);
		AspicArgument<PropositionalFormula> A4 = new AspicArgument<>((InferenceRule<PropositionalFormula>)parser.parseFormula("s1: p, q -> r"));
		A4.addDirectSub(A2);
		A4.addDirectSub(A1);
		
		Collection<AspicArgument<PropositionalFormula>> args = at.getArguments();
		assertTrue(args.contains(A3));
		assertTrue(args.contains(A4));
	}
	
	@SuppressWarnings("unchecked")
	@Test  public void Example2() throws Exception {
		PlParser plparser = new PlParser();
		AspicParser<PropositionalFormula> parser = new AspicParser<>(plparser, new PlFormulaGenerator());
		AspicArgumentationTheory<PropositionalFormula> at = parser.parseBeliefBaseFromFile("../../examples/aspic/ex2.aspic");
		
		InferenceRule<PropositionalFormula>snores=(InferenceRule<PropositionalFormula>)parser.parseFormula("p1: => Snores"),
			professor = (InferenceRule<PropositionalFormula>)parser.parseFormula("p2: => Professor");
		Comparator<InferenceRule<PropositionalFormula>> prem_comp = new RuleComparator<>(Arrays.asList("p1","p2")),
				rule_comp = new RuleComparator<>(Arrays.asList("d1","d3","d2"));
		assertTrue(prem_comp.compare(snores, professor) < 0);
		
		Collection<AspicArgument<PropositionalFormula>> args = at.getArguments();
		AspicArgument<PropositionalFormula> A3 = null, B1 = null;
		PropositionalFormula access = (PropositionalFormula)plparser.parseFormula("!AccessDenied"),
				no_access = (PropositionalFormula)plparser.parseFormula("AccessDenied");
		for(AspicArgument<PropositionalFormula> arg:args)
			if(arg.getConclusion().equals(access))
				B1 =arg;
			else if (arg.getConclusion().equals(no_access))
				A3 =arg;
		assertTrue(A3 != null);
		assertTrue(B1 != null);
		
		at.setOrder(new LastLinkOrder<>(rule_comp, prem_comp, true));
		DungTheory dt = at.asDungTheory();
		assertTrue(((AspicArgument<PropositionalFormula>)dt.getAttacks().iterator().next().getAttacker()).getConclusion().equals(no_access));
		
		at.setOrder(new WeakestLinkOrder<>(rule_comp, prem_comp, true));
		dt = at.asDungTheory();
		assertTrue(((AspicArgument<PropositionalFormula>)dt.getAttacks().iterator().next().getAttacker()).getConclusion().equals(access));
	}
	
	@SuppressWarnings("unchecked")
	@Test  public void Example3 () throws Exception {
		PlParser plparser = new PlParser();
		AspicParser<PropositionalFormula> parser = new AspicParser<>(plparser, new PlFormulaGenerator());
		AspicArgumentationTheory<PropositionalFormula> at = parser.parseBeliefBaseFromFile("../../examples/aspic/ex3.aspic");

		Comparator<InferenceRule<PropositionalFormula>> rule_comp = new RuleComparator<>(Arrays.asList("d1","d3","d2"));
		
		Collection<AspicArgument<PropositionalFormula>> args = at.getArguments();
		AspicArgument<PropositionalFormula> B2 = null;
		PropositionalFormula not = (PropositionalFormula)plparser.parseFormula("! LikesWhisky");
		for(AspicArgument<PropositionalFormula> arg:args)
			if(arg.getConclusion().equals(not))
				B2 =arg;
		assertTrue(B2 != null);
		
		at.setOrder(new WeakestLinkOrder<>(rule_comp, new RuleComparator<>(new ArrayList<>()), true));
		DungTheory dt = at.asDungTheory();
		assertTrue(((AspicArgument<PropositionalFormula>)dt.getAttacks().iterator().next().getAttacker()).getConclusion().equals(not));
	}
	
	@Test public void Example4 () throws Exception {
		PlParser plparser = new PlParser();
		AspicParser<PropositionalFormula> parser = new AspicParser<>(plparser, new PlFormulaGenerator());
		AspicArgumentationTheory<PropositionalFormula> at = parser.parseBeliefBaseFromFile("../../examples/aspic/ex4.aspic");
		
		DungTheory dt = at.asDungTheory();
		assertTrue(dt.getAttacks().size() == 4);
		for(Attack a: dt.getAttacks())
			assertFalse(((AspicArgument<PropositionalFormula>)a.getAttacker()).getTopRule().isDefeasible());
	}
	
	
	@Test public void ManualTest(){
		Proposition a = new Proposition("a");
		Proposition b = new Proposition("b");
		Proposition c = new Proposition("c");
		
		AspicArgumentationTheory<PropositionalFormula> t = new AspicArgumentationTheory<>(new PlFormulaGenerator());

		DefeasibleInferenceRule<PropositionalFormula> r1 = new DefeasibleInferenceRule<>();
		r1.setConclusion(a);
		r1.addPremise(b);
		r1.addPremise(c);
		t.addRule(r1);
		
		StrictInferenceRule<PropositionalFormula> s1 = new StrictInferenceRule<PropositionalFormula>();
		s1.setConclusion(b);
		t.addRule(s1);

		s1 = new StrictInferenceRule<PropositionalFormula>();
		s1.setConclusion(c);
		t.addRule(s1);
		
		System.out.println(t);
		
		System.out.println(t.asDungTheory());
	}
	
	@Test public void ComplementTest() throws Exception {
		PropositionalFormula f = new Proposition("a");
		assertTrue(f.equals(f.complement().complement()));
	}

	
	@Test
	public void ParserTest1() throws Exception {
		FolParser folparser = new FolParser();
		String folbsp = "Animal = {horse, cow, lion} \n"
				+ "type(Tame(Animal)) \n"
				+ "type(Ridable(Animal)) \n";
		folparser.parseBeliefBase(folbsp);
		AspicParser<FolFormula> aspicparser = new AspicParser<>(folparser, folfg);
		aspicparser.setSymbolComma(";");
		aspicparser.setSymbolDefeasible("==>");
		aspicparser.setSymbolStrict("-->");
		String aspicbsp = "d1: Tame(cow) ==> Ridable(cow)\n"
				+ "s1 : Tame(horse) && Ridable(lion) --> Tame(horse)";
		AspicArgumentationTheory<FolFormula> aat = aspicparser.parseBeliefBase(aspicbsp);
		assertTrue(aat.getRules().size() == 2);
	}
	
	@Test
	public void ParserTest2() throws Exception {
		PlParser plparser = new PlParser();
		AspicParser<FolFormula> aspicparser = new AspicParser<>(plparser, folfg);
		aspicparser.setSymbolComma(";");
		aspicparser.setSymbolDefeasible("==>");
		aspicparser.setSymbolStrict("-->");
		String aspicbsp = "d1: a ==> b\n"
				+ "s1 : c; d ==> e \n"
				+ "d ; r --> a";
		AspicArgumentationTheory<FolFormula> aat = aspicparser.parseBeliefBase(aspicbsp);
		assertTrue(aat.getRules().size() == 3);
	}
	
	@Test
	public void DerivationGraphTest() throws Exception {
		AspicParser<PropositionalFormula> parser = new AspicParser<>(new PlParser(), pfg);
		String input = "-> a \n => b \n b,c =>d \n a=> e \n b -> e \n e, b-> f";
		AspicArgumentationTheory<PropositionalFormula> aat = parser.parseBeliefBase(input);
		Collection<InferenceRule<PropositionalFormula>> rules = aat.getRules();
		assertTrue(rules.size() == 6);
		DerivationGraph<PropositionalFormula, InferenceRule<PropositionalFormula>> g = new DerivationGraph<>();
		g.allDerivations(rules);
		assertTrue(g.numberOfNodes()==6);
		for(InferenceRule<PropositionalFormula> r:g.getValues())
			assertTrue(rules.contains(r));
		for(InferenceRule<PropositionalFormula> r: rules)
			if(r.getConclusion().equals(new PlParser().parseFormula("d")))
				assertFalse(g.getValues().contains(r));
			else
				assertTrue(g.getValues().contains(r));
		assertTrue(g.numberOfEdges() == 6);
		assertTrue(g.getLeafs().size() == 2);
	}
	
	
	
	@Test
	public void ArgSysTest() throws Exception {
		AspicParser<PropositionalFormula> parser = new AspicParser<>(new PlParser(), pfg);
		String input = "-> a \n => b \n b,c =>d \n a-> e \n b -> e \n e, b=> f \n a, f -> g";
		AspicArgumentationTheory<PropositionalFormula> at = parser.parseBeliefBase(input);
		Collection<AspicArgument<PropositionalFormula>> args = at.getArguments();
		//for(AspicArgument<PropositionalFormula> a:args)
			//System.out.println(a);
		assertTrue(args.size() == 8);
		for(AspicArgument<PropositionalFormula> a:args)
			if(a.getConclusion() .equals(new Proposition("f"))
					|| a.getConclusion() .equals(new Proposition("g")))
				assertTrue(a.hasDefeasibleSub());
			else
				assertFalse(a.hasDefeasibleSub());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void AttackTest() throws Exception {
		AspicParser<PropositionalFormula> parser = new AspicParser<>(new PlParser(), pfg);
		String input = "=> ! a \n"
				+ " => a \n"
				+ "-> ! b \n"
				+ "-> b \n"
				+ "a,b->c\n";
		AspicArgumentationTheory<PropositionalFormula> at = parser.parseBeliefBase(input);
		Collection<AspicArgument<PropositionalFormula>> args = at.getArguments();
		
		AspicArgument<PropositionalFormula> not_a = new AspicArgument<>((InferenceRule<PropositionalFormula>)parser.parseFormula("=> ! a"));
		AspicArgument<PropositionalFormula> arg_a = new AspicArgument<>((InferenceRule<PropositionalFormula>)parser.parseFormula("=> a"));
		AspicArgument<PropositionalFormula> not_b = new AspicArgument<>((InferenceRule<PropositionalFormula>)parser.parseFormula("-> ! b"));
		AspicArgument<PropositionalFormula> not_c = new AspicArgument<>((InferenceRule<PropositionalFormula>)parser.parseFormula("-> ! c"));
		AspicArgument<PropositionalFormula> ab_mapsto_c = new AspicArgument<>((InferenceRule<PropositionalFormula>)parser.parseFormula("a,b->c"));
		assertTrue(args.contains(not_a));
		assertTrue(args.contains(not_b));
		assertFalse(args.contains(not_c));
		assertFalse(args.contains(ab_mapsto_c));
		
		int sum = 0;
		for (AspicArgument<PropositionalFormula> arg: args) {
			AspicAttack<PropositionalFormula> a=new AspicAttack<PropositionalFormula>(not_a,arg);
			a.resolve();
			//System.out.println(a.getOutput());
			if(a.isSuccessfull())
				sum++;
		}
		assertTrue(sum==2);
		for (AspicArgument<PropositionalFormula> arg: args) {
			AspicAttack<PropositionalFormula> a=new AspicAttack<PropositionalFormula>(not_b,arg);
			a.resolve();
			//System.out.println(a.getOutput());
			assertFalse(a.isSuccessfull());
		}
		for (AspicArgument<PropositionalFormula> arg: args) {
			AspicAttack<PropositionalFormula> a=new AspicAttack<PropositionalFormula>(arg_a,arg);
			a.resolve();
			//System.out.println(a.getOutput());
			if(arg.equals(not_a))
				assertTrue(a.isSuccessfull());
			else
				assertFalse(a.isSuccessfull());
		}
		
	}
	
	final PlFormulaGenerator pfg = new PlFormulaGenerator();
	final FolFormulaGenerator folfg = new FolFormulaGenerator();
	
	@SuppressWarnings("unchecked")
	@Test public void PropositionalFormulaGeneratorTest() throws Exception {
		AspicParser<PropositionalFormula> parser = new AspicParser<>(new PlParser(), pfg);
		String input = "-> a \n"
				+ "d1: a => b \n"
				+ "d2: a => !d1 \n"
				+ "s1: a -> e \n"
				+ "s2: a -> !s1";
		AspicArgumentationTheory<PropositionalFormula> at = parser.parseBeliefBase(input);
		DungTheory dt = at.asDungTheory();
		assertTrue(dt.getAttacks().size() == 1);
		assertTrue(((AspicArgument<PropositionalFormula>)dt.getAttacks().iterator().next().getAttacked()).getConclusion().equals(new Proposition("b")));
	}
	
	@SuppressWarnings("unchecked")
	@Test public void FolFormulaGeneratorTest() throws Exception {
		FolParser parser = new FolParser();
		String kb = "Rule = {d1,d2,s1,s2} \n"
				+ "type(a) \n type(b) \n type(c) \n type(e) \n"
				+ "type(__rule(Rule)) \n"
				;
		parser.parseBeliefBase(kb);
		AspicParser<FolFormula> aspicparser = new AspicParser<>(parser, folfg);
		String input = "-> a \n"
				+ "d1: a => b \n"
				+ "d2: a => !__rule(d1) \n"
				+ "s1: a -> e \n"
				+ "s2: a -> !__rule(s1)";
		AspicArgumentationTheory<FolFormula> at = aspicparser.parseBeliefBase(input);
		DungTheory dt = at.asDungTheory();
		assertTrue(dt.getAttacks().size() == 1);
		assertTrue(((AspicArgument<FolFormula>)dt.getAttacks().iterator().next().getAttacked()).getConclusion().equals(new FOLAtom(new Predicate("b"))));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void SimpleOrderTest() throws Exception {
		AspicParser<PropositionalFormula> parser = new AspicParser<>(new PlParser(), pfg);
		String input = "=> BornInScotland\n"
				+ " => FitnessLover \n"
				+ "d1: BornInScotland => Scottish \n"
				+ "d2: Scottish => LikesWhiskey \n"
				+ "d3: FitnessLover => ! LikesWhiskey\n",
				order = "d1<d3<d2";
		AspicArgumentationTheory<PropositionalFormula> at = parser.parseBeliefBase(input+order);
		
		
		DungTheory dt = at.asDungTheory();
		assertTrue(dt.getNodes().size() == 5);
		assertTrue(dt.getAttacks().size() == 1);
		assertTrue(((AspicArgument<PropositionalFormula>)dt.getAttacks().iterator().next().getAttacker()).getTopRule().getName().equals("d2"));
		
		Comparator<AspicArgument<PropositionalFormula>> new_order = parser.parseSimpleOrder("d1<d2<d3");
		at.setOrder(new_order);
		dt = at.asDungTheory();
		assertTrue(dt.getNodes().size() == 5);
		assertTrue(dt.getAttacks().size() == 1);
		assertTrue(((AspicArgument<PropositionalFormula>)dt.getAttacks().iterator().next().getAttacker()).getTopRule().getName().equals("d3"));
		
		at.setOrder(new SimpleAspicOrder<>());
		dt = at.asDungTheory();
		assertTrue(dt.getNodes().size() == 5);
		assertTrue(dt.getAttacks().size() == 2);
		
		at = parser.parseBeliefBase(input);
		at.setRuleFormulaGenerator(pfg);
		dt = at.asDungTheory();
		assertTrue(dt.getNodes().size() == 5);
		assertTrue(dt.getAttacks().size() == 2);
	}
}