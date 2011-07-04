package net.sf.tweety.logicprogramming.asplibrary.solver;

import java.io.StringReader;
import java.util.*;

import net.sf.tweety.logicprogramming.asplibrary.parser.ELPParser;
import net.sf.tweety.logicprogramming.asplibrary.syntax.*;
import net.sf.tweety.logicprogramming.asplibrary.util.*;


/**
 * wrapper class for the dlv answer set solver command line
 * utility.
 * 
 * @author Thomas Vengels
 *
 */
public class DLV {

	String path2dlv = null;
	AspInterface ai = new AspInterface();
	
	public DLV(String path2dlv) {
		this.path2dlv = path2dlv;
	}
	
	public List<AnswerSet> computeModels(Program p, int models) {
		
		return runDLV(p,models,null);
		
	}
	
	
	protected List<AnswerSet> runDLV(Program p, int nModels, String otherOptions) {
	
		String cmdLine = path2dlv + " -- " + "N=" + nModels; 
		
		List<String> result = null;
		List<AnswerSet> ret = new LinkedList<AnswerSet>();
		
		// try running dlv
		try {
			ai.executeProgram(cmdLine,p.toStringFlat());
			result = ai.getOutput();
		} catch (Exception e) {
			System.out.println("dlv error!");
			e.printStackTrace();
		}
		
		// early exit
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
		
		return ret;
	}
	
	protected List<Literal> parseAnswerSet(String s) {
		List<Literal> ret = null;
		try {
			ELPParser ep = new ELPParser( new StringReader( s ));
			ret = ep.dlv_answerset();
		} catch (Exception e) {
			System.err.println("dlv::parseAnswerSet error");
			e.printStackTrace();
		}
		return ret;
	}
}
