package edu.cs.ai.kr.pcl.syntax.parser;

import java.io.*;

import edu.cs.ai.kr.*;
import edu.cs.ai.kr.pl.parser.*;
import edu.cs.ai.kr.pl.syntax.*;
import edu.cs.ai.kr.pcl.*;
import edu.cs.ai.kr.pcl.syntax.*;
import edu.cs.ai.util.*;

/**
 * This class implements a parser for probabilistic conditional logic. The BNF for a conditional
 * knowledge base is given by (starting symbol is KB)
 * <br>
 * <br> KB 			::== (CONDITIONAL "\n")*
 * <br> CONDITIONAL ::== "(" FORMULA ")" "[" PROB "]" | "(" FORMULA "|" FORMULA ")" "[" PROB "]" 
 * <br> FORMULA     ::== PROPOSITION | "(" FORMULA ")" | FORMULA "&&" FORMULA | FORMULA "||" FORMULA | "!" FORMULA | "+" | "-"
 * <br> 
 * <br> where PROPOSITION is a sequence of symbols from {a,...,z,A,...,Z,0,...,9} with a letter at the beginning and PROB is a float in [0,1].
 * 
 *  @author Matthias Thimm
 */
public class PclParser extends Parser{

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.Parser#parseBeliefBase(java.io.Reader)
	 */
	@Override
	public PclBeliefSet parseBeliefBase(Reader reader) throws IOException, ParserException {
		PclBeliefSet beliefSet = new PclBeliefSet();
		String s = "";		 
		// read from the reader and separate formulas by "\n"
		try{
			for(int c = reader.read(); c != -1; c = reader.read()){
				if(c == 10){
					beliefSet.add(this.parseFormula(new StringReader(s)));					
					s = "";
				}else
					s += (char) c;
				
			}		
			if(!s.equals(""))
				beliefSet.add(this.parseFormula(new StringReader(s)));				
		}catch(Exception e){
			throw new ParserException(e);
		}
		return beliefSet;
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.pl.parser.PlParser#parseFormula(java.io.Reader)
	 */
	public ProbabilisticConditional parseFormula(Reader reader) throws IOException, ParserException {
		// read into string
		String s = "";
		try{
			for(int c = reader.read(); c != -1; c = reader.read())
				s += (char) c;
		}catch(Exception e){
			throw new ParserException(e);
		}
		// check probability 
		if(!s.contains("[")) throw new ParserException("Missing '[' in conditional definition.");
		if(!s.contains("]")) throw new ParserException("Missing ']' in conditional definition.");
		String probString = s.substring(s.lastIndexOf("[")+1,s.lastIndexOf("]"));
		Probability prob;
		try{
			prob = new Probability(Double.parseDouble(probString));
		}catch(Exception e){
			throw new ParserException("Could not parse probability '" + probString + "'.");
		}
		if(!s.contains("(") || !s.contains(")")) 
			throw new ParserException("Conditionals must be enclosed by parantheses.");
		String condString = s.substring(1,s.lastIndexOf(")"));
		//check for a single "|" (note, that "||" denotes disjunction)
		int idx = 0;		
		while(idx != -1){
			idx = condString.indexOf("|", idx);
			if(condString.charAt(idx+1) != '|')
				break;			
		}		
		PlParser parser = new PlParser();
		
		if(idx == -1){
			ProbabilisticConditional r = new ProbabilisticConditional((PropositionalFormula)parser.parseFormula(condString.substring(0, condString.length())),prob);			
			return r;
		}
		return new ProbabilisticConditional((PropositionalFormula)parser.parseFormula(condString.substring(idx+1, condString.length())),(PropositionalFormula)parser.parseFormula(condString.substring(0, idx)),prob);		
	}
	
}
