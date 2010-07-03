package edu.cs.ai.kr.rpcl.parser;

import java.io.*;

import edu.cs.ai.kr.*;
import edu.cs.ai.kr.fol.parser.*;
import edu.cs.ai.kr.fol.syntax.*;
import edu.cs.ai.kr.rpcl.*;
import edu.cs.ai.kr.rpcl.syntax.*;
import edu.cs.ai.util.*;

/**
 * This class implements a parser for relational probabilistic conditional logic. The BNF for a conditional
 * knowledge base is given by (starting symbol is KB)
 * <br>
 * <br> KB 			::== SORTSDEC PREDDECS CONDITIONALS
 * <br> SORTSDEC    ::== ( SORTNAME "=" "{" (CONSTANTNAME ("," CONSTANTNAME)*)? "}" "\n" )*
 * <br> PREDDECS	::== ( "type" "(" PREDICATENAME "(" (SORTNAME ("," SORTNAME)*)? ")" ")" "\n" )*
 * <br> CONDITIONAL ::== "(" FORMULA ")" "[" PROB "]" | "(" FORMULA "|" FORMULA ")" "[" PROB "]" 
 * <br> FORMULA     ::== ATOM | "(" FORMULA ")" | FORMULA "&&" FORMULA | FORMULA "||" FORMULA | "!" FORMULA | "+" | "-"
 * <br> ATOM		::== PREDICATENAME "(" (TERM ("," TERM)*)? ")"
 * <br> TERM		::== VARIABLENAME | CONSTANTNAME 
 * <br> 
 * <br> where SORTNAME, PREDICATENAME, CONSTANTNAME, and VARIABLENAME are sequences of
 * <br> symbols from {a,...,z,A,...,Z,0,...,9} with a letter at the beginning and PROB is a float in [0,1].
 * 
 *  @author Matthias Thimm
 */
public class RpclParser extends FolParser {

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.Parser#parseBeliefBase(java.io.Reader)
	 */
	@Override
	public BeliefBase parseBeliefBase(Reader reader) throws IOException, ParserException {
		RpclBeliefSet beliefSet = new RpclBeliefSet();
		String s = "";
		// for keeping track of the section of the file
		// 0 means sorts declaration
		// 1 means functor/predicate declaration
		// 2 means conditional section
		int section = 0; 
		// read from the reader and separate formulas by "\n"
		try{
			for(int c = reader.read(); c != -1; c = reader.read()){
				if(c == 10){
					if(!s.equals("")){
						if(s.trim().startsWith("type")) section = 1;
						else if(section == 1) section = 2;
						
						if(section == 2)
							beliefSet.add(this.parseFormula(new StringReader(s)));
						else if(section == 1)
							this.parseTypeDeclaration(s);
						else this.parseSortDeclaration(s);
					}
					s = "";
				}else{
					s += (char) c;
				}
			}		
			if(!s.equals("")){
				if(s.trim().startsWith("type")) section = 1;
				else if(section == 1) section = 2;
				
				if(section == 2)
					beliefSet.add(this.parseFormula(new StringReader(s)));
				else if(section == 1)
					this.parseTypeDeclaration(s);
				else this.parseSortDeclaration(s);
			}
		}catch(Exception e){
			throw new ParserException(e);
		}
		return beliefSet;
	}

	/* (non-Javadoc)
	 * @see edu.cs.ai.kr.fol.parser.FolParser#parseFormula(java.io.Reader)
	 */
	@Override
	public RelationalProbabilisticConditional parseFormula(Reader reader) throws IOException, ParserException {
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
		FolParser parser = new FolParser();
		parser.setSignature(this.getSignature());
		if(idx == -1){
			RelationalProbabilisticConditional r = new RelationalProbabilisticConditional((FolFormula)parser.parseFormula(condString.substring(0, condString.length())),prob);
			this.setSignature(parser.getSignature());
			return r;
		}
		// check whether variables have the correct sort wrt. the scope of the whole conditional		
		RelationalProbabilisticConditional cond = new RelationalProbabilisticConditional((FolFormula)parser.parseFormula(condString.substring(idx+1, condString.length())),(FolFormula)parser.parseFormula(condString.substring(0, idx)),prob);
		this.setSignature(parser.getSignature());
		parser.parseFormula(condString.substring(idx+1, condString.length()) + " && " + condString.substring(0, idx));
		return cond;
	}
	
}
