package net.sf.logicprogramming.asplibrary.syntax;

import java.io.StringReader;

import junit.framework.TestCase;
import net.sf.tweety.logicprogramming.asplibrary.parser.ELPParser;
import net.sf.tweety.logicprogramming.asplibrary.parser.ParseException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.ELPAtom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logicprogramming.asplibrary.syntax.ELPElement;
import net.sf.tweety.logics.commons.syntax.Constant;

public class ParserTest extends TestCase {
	
	public void testRuleParsing() {
		String str = "test :- predWithConstant(bobby).";
		ELPParser parser = new ELPParser(new StringReader(str));
		try {
			Rule r = parser.rule();
			assertEquals(new ELPAtom("test"), r.getHead().iterator().next());
			assertEquals(new ELPAtom("predWithConstant", new Constant("bobby")), r.getBody().get(0));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testRelationParsing() {
		/*
		String str = "X < Y";
		ELPParser parser = new ELPParser(new StringReader(str));
		try {
			RuleElement a = parser.LiteralExpr();
			assertEquals(new Relation("<", new Variable("X"), new Variable("Y")), a);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	public void testDoubleArgument() {
		String str = "info(excused(a_SELF))";
		ELPParser parser = new ELPParser(new StringReader(str));
		try {
			ELPElement a = parser.LiteralExpr();
			assertEquals(new ELPAtom("info", new Constant("excused(a_SELF)")), a);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
