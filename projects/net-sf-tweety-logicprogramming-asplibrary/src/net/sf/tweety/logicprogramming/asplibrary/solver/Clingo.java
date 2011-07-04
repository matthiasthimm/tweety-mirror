package net.sf.tweety.logicprogramming.asplibrary.solver;

import java.io.StringReader;
import java.util.*;

import net.sf.tweety.logicprogramming.asplibrary.parser.ELPParser;
import net.sf.tweety.logicprogramming.asplibrary.syntax.*;
import net.sf.tweety.logicprogramming.asplibrary.util.*;

public class Clingo {
	protected AspInterface ai = new AspInterface();
	protected String path2clingo = null;
	
	public Clingo(String path2clingo) {
		this.path2clingo = path2clingo;
	}
	
	public List<AnswerSet> computeModels(Program p) {
		
		List<String> result = null;
		try {
			ai.executeProgram( path2clingo, p.toStringFlat() );
			result = ai.getOutput();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// process output and return answer set
		List<AnswerSet> asl = new LinkedList<AnswerSet>();
		boolean prevIsAnswer = false;
		for (String s : result) {
			if (s.startsWith("Answer:")) {
				prevIsAnswer = true;
			} else if (prevIsAnswer) {
				prevIsAnswer = false;
				List<Literal> lits = parseAnswerSet(s);
				if (lits != null)
					asl.add( new AnswerSet(lits,0,0));
			}
		}
		return asl;
	}

	private List<Literal> parseAnswerSet(String s) {
		List<Literal> ret = null;
		try {
			ELPParser ep = new ELPParser( new StringReader( s ));
			ret = ep.clasp_answerset();
		} catch (Exception e) {
			System.err.println("clingo: error parsing answer set!");
			e.printStackTrace();
		}
		return ret;
	}
}
