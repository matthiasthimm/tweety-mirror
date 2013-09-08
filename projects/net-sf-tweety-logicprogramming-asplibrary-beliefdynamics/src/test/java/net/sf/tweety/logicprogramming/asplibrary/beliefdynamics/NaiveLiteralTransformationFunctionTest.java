package net.sf.tweety.logicprogramming.asplibrary.beliefdynamics;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack.AttackStrategy;
import net.sf.tweety.argumentation.parameterisedhierarchy.semantics.attack.StrongAttack;
import net.sf.tweety.logicprogramming.asplibrary.beliefdynamics.selectiverevision.ScepticalLiteralTransformationFunction;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPAtom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPNeg;
import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPNot;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;

import org.junit.Test;

public class NaiveLiteralTransformationFunctionTest {
	DLPAtom a1 = new DLPAtom("a1");
	DLPAtom a2 = new DLPAtom("a2");
	DLPAtom a3 = new DLPAtom("a3");
	DLPAtom a4 = new DLPAtom("a4");
	DLPAtom b = new DLPAtom("b");

	@Test
	public void testFailsWeakMaximality() {
		Program p = new Program();
		p.add(new Rule(new DLPNeg(a1), new DLPNot(a2)));
		p.add(new Rule(new DLPNeg(a2), new DLPNot(a1)));
		
		AttackStrategy attack = StrongAttack.getInstance();
		AttackStrategy defense = StrongAttack.getInstance();
		ScepticalLiteralTransformationFunction trans;
		trans = new ScepticalLiteralTransformationFunction(p, attack, defense);
		
		Rule r1 = new Rule(a1);
		Rule r2 = new Rule(a2);
		Set<Rule> newKB = new HashSet<Rule>();
		newKB.add(r1); newKB.add(r2);
		Collection<Rule> result = trans.transform(newKB);
		assertFalse(result.contains(r1));
		assertFalse(result.contains(r2));
	}

}
