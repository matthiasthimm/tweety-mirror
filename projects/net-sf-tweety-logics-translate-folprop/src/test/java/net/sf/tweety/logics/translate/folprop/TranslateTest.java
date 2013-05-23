package net.sf.tweety.logics.translate.folprop;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import net.sf.tweety.logics.commons.translate.Translator;
import net.sf.tweety.logics.firstorderlogic.syntax.FOLAtom;
import net.sf.tweety.logics.propositionallogic.syntax.Proposition;

import org.junit.Test;

public class TranslateTest {

	@Test
	public void test() {
		Translator test = new Translator(new HashMap<Class<?>, Class<?>>());
		Proposition prop = new Proposition("test");
		FOLAtom atom = test.translateAtom(prop, FOLAtom.class);
		assertEquals(prop.getName(), atom.getName());
		
		/*
		Proposition prop2 = new Proposition("b");
		prop.setPredicate(new PropositionalPredicate("a"));
		net.sf.tweety.logics.propositionallogic.syntax.Disjunction propDis = 
				new net.sf.tweety.logics.propositionallogic.syntax.Disjunction(prop, prop2);
		net.sf.tweety.logics.firstorderlogic.syntax.Conjunction con = 
				test.translateAssociative(propDis, 
						net.sf.tweety.logics.firstorderlogic.syntax.Conjunction.class);
		*/
		// todo: assertions
	}
}
