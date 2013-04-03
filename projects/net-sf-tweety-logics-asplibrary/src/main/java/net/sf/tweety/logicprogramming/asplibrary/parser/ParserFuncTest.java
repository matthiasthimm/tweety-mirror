package net.sf.tweety.logicprogramming.asplibrary.parser;

import java.io.StringReader;
import java.util.List;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Neg;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Not;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.RuleElement;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;

/**
 * A functional test for the ELP-Parser.
 * @author Tim Janus
 */
public class ParserFuncTest {
	public static void main(String [] args) throws ParseException {
		String rule = "-who_argued(X, Y) :- not someone_argued(X, Y).";
		ELPParser parser = new ELPParser(new StringReader(rule));
		
		Program p = new Program();
		List<Rule> rules = parser.program();
		p.addAll(rules);
		
		System.out.println("Analyse der Parser-Ausgabe: " + rule);
		for(Rule r : p) {
			Atom head = r.getHead().get(0).getAtom();
			Output(head);
			System.out.println();
			System.out.println("Body:");
			for(RuleElement l : r.getBody()) {
				System.out.println(l);
			}
		}
	}
	
	public static void Output(RuleElement atom) {
		
		System.out.println("Literal: " + atom);
		System.out.println("Is StrictNegated: " + (atom instanceof Neg));
		System.out.println("Is DefaultNegated: " + (atom instanceof Not));
		/*
		for(Term<?> t : atom.getAtom().getTerms()) {
			System.out.println("IsConstant: "+ (t instanceof Constant));
			System.out.println("IsList: "+ (t instanceof ListTerm));
			System.out.println("IsSet: "+ (t instanceof SetTerm));
			System.out.println("IsVariable: "+ (t instanceof Variable));
			System.out.println("IsNumber: "+ (t instanceof Number));
		}
		*/
	}
}
