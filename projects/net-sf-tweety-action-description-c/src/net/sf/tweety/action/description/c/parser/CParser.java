package net.sf.tweety.action.description.c.parser;

import java.io.IOException;
import java.io.Reader;


import net.sf.tweety.BeliefBase;
import net.sf.tweety.Formula;
import net.sf.tweety.Parser;
import net.sf.tweety.ParserException;
import net.sf.tweety.action.signature.ActionSignature;
import net.sf.tweety.action.signature.parser.ActionSignatureParser;

/**
 * This class implements a parser for an Action Description in C. 
 * The BNF is given by: (starting symbol is DESC) <br>
 * <br> DESC ::== ":- signature" "\n" SIGNATURE "\n" ":- rules" "\n" RULES
 * <br>
 * where SIGNATURE is parsed by CSignatureParser and RULES is parsed by CRuleParser.
 * @author Sebastian Homann
 */
public class CParser
  extends Parser {
  protected ActionSignature signature;
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.Parser#parseBeliefBase(java.io.Reader)
   */
  @Override
  public BeliefBase parseBeliefBase( Reader reader )
    throws IOException, ParserException {
    // State 0 : initialize
    // State 1 : read signature
    // State 2 : read rulebase
    int state = 0;
    String s = "";
    String sig = "";
    String rules = "";
    // read from the reader and separate formulas by "\n"
    try {
      int c;
      do {
        c = reader.read();
        if ( c == 10 || c == -1 ) {
          if ( !s.trim().equals( "" ) ) {
            if(s.trim().contains( ":- signature" )) {
              state = 1;
            } else if(s.trim().contains( ":- rules" )) {
              state = 2;
            } else {
              if(state == 1) sig += s+"\n";
              else rules += s+"\n";
            }
          }
          s = "";
        } else {
          s += (char) c;
        }
      } while ( c != -1 );
      
      signature = new ActionSignatureParser().parseSignature( sig );
      return new CRuleParser( signature ).parseBeliefBase( rules );
    } catch ( Exception e ) {
      throw new ParserException( e );
    }
  }

  /*
   * (non-Javadoc)
   * @see net.sf.tweety.Parser#parseFormula(java.io.Reader)
   */
  @Override
  public Formula parseFormula( Reader reader )
    throws IOException, ParserException {
    String s = "";
    int c;
    do{
     c = reader.read();
     s += (char) c;
    } while(c != -1);
    return parseFormula(s);
  }
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.Parser#parseFormula(java.lang.String)
   */
  @Override
  public Formula parseFormula( String formula) throws ParserException, IOException {
    return new CRuleParser( signature ).parseFormula( formula );
  }
  
  public void setSignature( ActionSignature signature) {
    this.signature = signature;
  }
  
  public ActionSignature getSignature() {
    return this.signature;
  }
}
