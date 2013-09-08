package net.sf.tweety.argumentation.parameterisedhierarchy;

import static org.junit.Assert.*;

import net.sf.tweety.argumentation.parameterisedhierarchy.semantics.AttackRelation;
import net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack.Attack;
import net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack.AttackStrategy;
import net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack.Defeat;
import net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack.Rebut;
import net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack.StrongAttack;
import net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack.Undercut;
import net.sf.tweety.argumentation.parameterisedhierarchy.syntax.Argument;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPAtom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPNeg;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPNot;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;

import org.junit.Test;

public class AttackTest {

	@Test
	public void testUndercut() {
		AttackStrategy strategy = Undercut.getInstance();
		
		Rule r1 = new Rule( new DLPAtom("b") );
		Rule r2 = new Rule( new DLPAtom("a"), new DLPNot( new DLPAtom("b") ) ); 
		Program p = new Program();
		p.add(r1); p.add(r2);
		
		Argument A = new Argument(r1);
		Argument B = new Argument(r2);
		assertTrue(strategy.attacks(A, B));
		
		ArgumentationKnowledgeBase kb = new ArgumentationKnowledgeBase(p);
		AttackRelation notion = new AttackRelation(kb, strategy);
		assertTrue(notion.attacks(A, B));
		assertTrue( notion.getAttackingArguments(B).contains(A) );
	}
	
	@Test
	public void testRebut() {
		AttackStrategy strategy = Rebut.getInstance();
		
		Rule r1 = new Rule( new DLPAtom("b") );
		Rule r2 = new Rule( new DLPNeg( new DLPAtom("b") ) ); 
		Program p = new Program();
		p.add(r1); p.add(r2);
		
		Argument A = new Argument(r1);
		Argument B = new Argument(r2);
		assertTrue( strategy.attacks(A, B));
		assertTrue( strategy.attacks(B, A));
		
		ArgumentationKnowledgeBase kb = new ArgumentationKnowledgeBase(p);
		AttackRelation notion = new AttackRelation(kb, strategy );
		assertTrue( notion.attacks(A, B));
		assertTrue( notion.attacks(B, A));
		assertTrue( notion.getAttackingArguments(B).contains(A) );
		assertTrue( notion.getAttackingArguments(A).contains(B) );
	}
	
	@Test
	public void testAttack() {
		AttackStrategy strategy = Attack.getInstance();
		
		Rule r1 = new Rule( new DLPAtom("b") );
		Rule r2 = new Rule( new DLPNeg( new DLPAtom("b") ) );
		Rule r3 = new Rule( new DLPAtom("a"), new DLPNot( new DLPAtom("b") ) ); 
		Program p = new Program();
		p.add(r1); p.add(r2); p.add(r3);
		
		Argument A = new Argument(r1);
		Argument B = new Argument(r2);
		Argument C = new Argument(r3);
		assertTrue( strategy.attacks(A, B));
		assertTrue( strategy.attacks(B, A));
		assertTrue( strategy.attacks(A, C));
		assertFalse( strategy.attacks(C, A));
		assertFalse( strategy.attacks(B, C));
		assertFalse( strategy.attacks(C, B));
		
		
		ArgumentationKnowledgeBase kb = new ArgumentationKnowledgeBase(p);
		AttackRelation notion = new AttackRelation(kb, strategy );
		assertTrue( notion.attacks(A, B) );
		assertTrue( notion.attacks(A, C) );
		assertTrue( notion.getAttackingArguments(B).contains(A) );
		assertTrue( notion.getAttackingArguments(C).contains(A) );
		assertTrue( notion.getAttackingArguments(A).contains(B) );	
	}

	@Test
	public void testDefeat() {
		AttackStrategy strategy = Defeat.getInstance();
		
		Rule r1 = new Rule( new DLPAtom("a") );
		Rule r2 = new Rule( new DLPAtom("b"), new DLPNot(new DLPAtom("a")) );
		Rule r3 = new Rule( new DLPAtom("c"), new DLPNot(new DLPAtom("d")) );
		Rule r4 = new Rule( new DLPNeg(new DLPAtom("c")), new DLPNot(new DLPAtom("c")) );
		Program p = new Program();
		p.add(r1); p.add(r2); p.add(r3); p.add(r4);
		
		Argument A = new Argument(r1);
		Argument B = new Argument(r2);
		Argument C = new Argument(r3);
		Argument D = new Argument(r4);
		
		assertTrue( strategy.attacks(A,B));
		assertTrue( strategy.attacks(C,D));
		assertFalse( strategy.attacks(D,C));
	}

	@Test
	public void testStrongAttack() {
		AttackStrategy strategy = StrongAttack.getInstance();
		
		Rule r1 = new Rule( new DLPAtom("a") );
		Rule r2 = new Rule( new DLPAtom("b"), new DLPNot(new DLPAtom("a")) );
		Rule r3 = new Rule( new DLPNeg(new DLPAtom("a")), new DLPNot(new DLPAtom("a")) );
		Program p = new Program();
		p.add(r1); p.add(r2); p.add(r3);
		
		Argument A = new Argument(r1);
		Argument B = new Argument(r2);
		Argument C = new Argument(r3);
		
		assertTrue( strategy.attacks(A,B));
		assertTrue( strategy.attacks(A,C));
		assertFalse( strategy.attacks(B,A));
		assertFalse( strategy.attacks(C,A));
	}
	
	@Test
	public void testStrongUndercut() {
		AttackStrategy strategy = StrongAttack.getInstance();

		Rule r1 = new Rule( new DLPAtom("b"), new DLPNot(new DLPAtom("c")) );
		Rule r2 = new Rule( new DLPAtom("a"), new DLPNot(new DLPAtom("b")) );
		Program p = new Program();
		p.add(r1); p.add(r2);
		
		Argument A = new Argument(r1);
		Argument B = new Argument(r2);
		
		assertTrue( strategy.attacks(A,B));
		assertFalse( strategy.attacks(B,A));
	}

}
