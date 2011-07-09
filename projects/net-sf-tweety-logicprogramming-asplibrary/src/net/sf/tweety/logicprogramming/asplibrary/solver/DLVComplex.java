package net.sf.tweety.logicprogramming.asplibrary.solver;

import java.io.StringReader;
import java.util.*;

import net.sf.tweety.logicprogramming.asplibrary.parser.ELPParser;
import net.sf.tweety.logicprogramming.asplibrary.syntax.*;
import net.sf.tweety.logicprogramming.asplibrary.util.*;


public class DLVComplex {

	String path2dlx = null;
	AspInterface ai = new AspInterface();
	SolveTime st = null;
	
	public DLVComplex(String path2dlx) {
		this.path2dlx = path2dlx;
	}
	
	public List<AnswerSet> computeModels(Program p, int models) {
		return runDLV(p,models,null);
	}
	
	protected List<AnswerSet> runDLV(Program p, int nModels, String otherOptions) {
	
		String cmdLine = path2dlx + " -- " + "-nofdcheck " + "N=" + nModels; 
		
		List<String> result = null;
		
		// try running dlv
		try {
			st = new SolveTime();
			st.beginWrite();
			String out = p.toStringFlat();
			st.endWrite();
			st.beginCalculate();
			ai.executeProgram(cmdLine, out );
			st.endCalculate();
			result = ai.getOutput();
		} catch (Exception e) {
			System.out.println("dlvcomplex error!");
			e.printStackTrace();
		}
		
		return processResults(result);
	}
	
	public List<AnswerSet> computeModels(String program, int models) {
		String cmdLine = path2dlx + " -- " + "-nofdcheck";
		if (models > 0)
			cmdLine += " N=" + models; 
		
		List<String> result = null;
		
		// try running dlv
		try {
			ai.executeProgram(cmdLine, program );
			result = ai.getOutput();
		} catch (Exception e) {
			System.out.println("dlvcomplex error!");
			e.printStackTrace();
		}
		
		return processResults(result);
	}
		
	protected List<AnswerSet> processResults(List<String> result) {
		
		st.beginRead();
		List<AnswerSet> ret = new LinkedList<AnswerSet>();

		// early return
		if (result == null)
			return ret;
		
		// process results
		List<Literal> lastAS = null;
		for (String s : result) {
			if (s.length() <= 0)
				continue;
			//System.out.println(s);
			
			// answer sets starts with a '{'
			if (s.charAt(0) == '{') {
				if (lastAS != null) {
					ret.add( new AnswerSet(lastAS,0,0));
				}
				lastAS = parseAnswerSet(s);
			}
			// answer set with weak constraints
			else if (s.startsWith("Best model")) {
				
				if (lastAS != null) {
					ret.add( new AnswerSet(lastAS,0,0));
				}
				
				s = s.substring(11);
				lastAS = parseAnswerSet(s);
			}
			else if (s.startsWith("Cost")) {
				s = s.substring(25, s.length()-2 );
				String[] wl = s.split(":");
				int weight = Integer.parseInt(wl[0]);
				int level = Integer.parseInt(wl[1]);
				ret.add(new AnswerSet(lastAS,weight,level));
				lastAS = null;
			}
		}
		
		if (lastAS != null)
			ret.add( new AnswerSet(lastAS,0,0));
		
		st.endRead();
		
		return ret;
	}

	
	protected List<Literal> parseAnswerSet(String s) {
		List<Literal> ret = null;
		
		try {
			ELPParser ep = new ELPParser( new StringReader( s ));
			List<Literal> lits = ep.dlv_answerset();
			ret = lits;
		} catch (Exception e) {
			System.err.println("dlvcomplex::parseAnswerSet error");
			System.err.println(e);
			System.err.println(e.getStackTrace());
		}
		
		return ret;
	}
	
	public SolveTime getTimings() {
		return this.st;
	}
}