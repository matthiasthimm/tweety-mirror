package net.sf.tweety.action.description.c.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.BeliefBase;
import net.sf.tweety.Formula;
import net.sf.tweety.Parser;
import net.sf.tweety.ParserException;
import net.sf.tweety.action.description.c.CActionDescription;
import net.sf.tweety.action.description.c.syntax.CRule;
import net.sf.tweety.action.description.c.syntax.DynamicRule;
import net.sf.tweety.action.description.c.syntax.StaticRule;
import net.sf.tweety.action.grounding.GroundingRequirement;
import net.sf.tweety.action.grounding.parser.GroundingRequirementsParser;
import net.sf.tweety.action.signature.ActionSignature;
import net.sf.tweety.logics.firstorderlogic.parser.FolParser;
import net.sf.tweety.logics.firstorderlogic.syntax.AssociativeFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Conjunction;
import net.sf.tweety.logics.firstorderlogic.syntax.Contradiction;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import net.sf.tweety.logics.firstorderlogic.syntax.Tautology;
import net.sf.tweety.logics.firstorderlogic.syntax.Variable;

/**
 * This class implements a parser for causal rules in C. The BNF of such rules is given by: (starting symbol is DESC) <br>
 * <br> DESC         ::== RULE ("\n" RULE)*
 * <br> RULE         ::== STATICRULE | DYNAMICRULE 
 * <br> STATICRULE   ::== "caused" FOLFORMULA ("if" FOLFORMULA)? ("requires" REQUIREMENTS)?
 * <br> DYNAMICRULE  ::== "caused" FOLFORMULA ("if" FOLFORMULA)? "after" FOLFORMULA ("requires" REQUIREMENTS)? 
 * <br> REQUIREMENTS ::== REQUIREMENT ("," REQUIREMENT)* 
 * <br> REQUIREMENT  ::== (VARIABLENAME "<>" VARIABLENAME | VARIABLENAME "<>" CONSTANTNAME)*
 * <br>
 * where FOLFORMULA is an unquantified first-order formula without functors, <br>
 * and VARIABLENAME, CONSTANTNAME are sequences of symbols <br>
 * from {a,...,z,A,...,Z,0,...,9} with a letter at the beginning.
 * 
 * @author Sebastian Homann
 */
public class CRuleParser
  extends Parser {

  protected ActionSignature signature;

  /**
   * This parser needs a valid action signature to parse causal rules.
   * @param signature
   */
  public CRuleParser( ActionSignature signature ) {
    this.signature = signature;
  }

  /*
   * (non-Javadoc)
   * @see net.sf.tweety.Parser#parseBeliefBase(java.io.Reader)
   */
  @Override
  public BeliefBase parseBeliefBase( Reader reader )
    throws ParserException {
    CActionDescription actionDescription = new CActionDescription();

    String s = "";
    // read from the reader and separate formulas by "\n"
    try {
      int c;
      do {
        c = reader.read();
        if ( c == 10 || c == -1 ) {
          if ( !s.equals( "" ) && !s.trim().startsWith( "%" )) {
            actionDescription.add( (CRule) this.parseFormula(  s ) );
          }
          s = "";
        } else {
          s += (char) c;
        }
      } while ( c != -1 );
    } catch ( Exception e ) {
      throw new ParserException( e );
    }
    return actionDescription;
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

  /**
   * Parses a FolFormula from a string using the FolParser class
   * @param s
   * @return A first order formula
   * @throws ParserException
   * @throws IOException
   */
  protected FolFormula parseFolFormula( String s )
    throws ParserException, IOException {
    FolParser p = new FolParser();
    p.setSignature( signature );
    return (FolFormula) p.parseFormula( s );
  }
  
  /*
   * (non-Javadoc)
   * @see net.sf.tweety.Parser#parseFormula(java.lang.String)
   */
  @Override
  public Formula parseFormula(String s) throws ParserException, IOException {
    s = s.trim();
    String reqString = null;
    // handle grounding requirements
    if(s.contains( " requires " )) {
      reqString = s.substring( s.indexOf( " requires " ) + 10 );
      Set<String> wrongKey = containedKeywords( reqString );
      if(!wrongKey.isEmpty())
        throw new ParserException("Unexpected keyword(s) '"+wrongKey.toString()+"' in formula '"+s+"' due to wrong order or multiple use.");
      s = s.substring( 0, s.indexOf( " requires " ) );
    }
    
    if(s.startsWith( "caused " ))
      return parseCausedFormula( s, reqString );
    if(s.startsWith( "inertial " ))
      return parseInertialFormula( s, reqString );
    if(s.startsWith( "default " ))
      return parseDefaultFormula( s, reqString );
    if(s.startsWith( "always " ))
      return parseAlwaysFormula( s, reqString );
    if(s.startsWith( "nonexecutable "))
      return parseNonexecutableFormula( s, reqString );
    if(s.contains( " causes " ))
      return parseCausesFormula( s, reqString );
    if(s.contains( " may cause "))
      return parseMayCauseFormula( s, reqString );
    
    throw new ParserException("Unsupported causal rule: "+s);
  }
   
  /**
   * Parses a string containing a single causal rule of the form
   *   caused A (if B)? (after C)?
   * @param s a string containing a single causal rule.
   * @param reqString the grounding requirements of this rule.
   * @return the corresponding causal rule
   * @throws ParserException
   * @throws IOException
   */
  private Formula parseCausedFormula(String s, String reqString) throws ParserException, IOException {
    if(!s.startsWith("caused "))
      throw new ParserException("Missing 'caused' expression in causal rule: "+s);
    FolFormula afterFormula = null;
    FolFormula ifFormula = null;
    FolFormula headFormula = null;

    if(s.contains( " after " )) {
      String afterString = s.substring( s.indexOf( " after ") + 7 );
      Set<String> wrongKey = containedKeywords( afterString );
      if(!wrongKey.isEmpty())
        throw new ParserException("Unexpected keyword(s) '"+wrongKey.toString()+"' in formula '"+s+"' due to wrong order or multiple use.");
      afterFormula = parseFolFormula( afterString );
      s = s.substring( 0, s.indexOf( " after " ) );
    }
    if(s.contains( " if " )) {
      String ifString = s.substring( s.indexOf(" if ") + 4 );
      Set<String> wrongKey = containedKeywords( ifString );
      if(!wrongKey.isEmpty())
        throw new ParserException("Unexpected keyword(s) '"+wrongKey.toString()+"' in formula '"+s+"' due to wrong order or multiple use.");
      ifFormula = parseFolFormula( ifString );
      s = s.substring( 0, s.indexOf( " if ") );
    }

    String headString = s.substring( s.indexOf("caused ") + 7 );
    Set<String> wrongKey = containedKeywords( headString );
    if(!wrongKey.isEmpty())
      throw new ParserException("Unexpected keyword(s) '"+wrongKey.toString()+"' in formula '"+s+"' due to wrong order or multiple use of keywords.");
    headFormula = parseFolFormula( headString );
    
    // parse grounding requirements
    Set<Variable> variables = new HashSet<Variable>();
    variables.addAll( headFormula.getUnboundVariables() );
    if(ifFormula != null)
      variables.addAll( ifFormula.getUnboundVariables() );
    if(afterFormula != null)
      variables.addAll( afterFormula.getUnboundVariables() );
    Set<GroundingRequirement> requirements = null;
    requirements = new GroundingRequirementsParser().parseRequirements( reqString, variables );
    
    if(afterFormula != null)
      return new DynamicRule(headFormula, ifFormula, afterFormula,  requirements);
    else
      return new StaticRule(headFormula, ifFormula, requirements);
  }
  
  /**
   * Parses a string containing a single causal rule of the form
   *   inertial A
   * which is converted to the causal rule
   *   caused A if A after A
   * @param s a string containing a single inertial rule.
   * @param reqString the grounding requirements of this rule.
   * @return the corresponding causal rule
   * @throws ParserException
   * @throws IOException
   */
  private Formula parseInertialFormula(String s, String reqString) throws ParserException, IOException {
    if(!s.startsWith("inertial "))
      throw new ParserException("Missing 'inertial' expression in causal rule: "+s);
    FolFormula formula = null;
    String headString = s.substring( s.indexOf("inertial ") + 9 );
    Set<String> wrongKey = containedKeywords( headString );
    if(!wrongKey.isEmpty())
      throw new ParserException("Unexpected keyword(s) '"+wrongKey.toString()+"' in formula '"+s+"' due to wrong order or multiple use of keywords.");
    formula = parseFolFormula( headString );
    if(formula instanceof AssociativeFormula)
      throw new ParserException("Inertial expression contains illegal argument.");
    if(formula instanceof Negation)
      if(!((((Negation)formula).getFormula()) instanceof Atom))
        throw new ParserException( "Inertial expression contains illegal argument." );
    Set<GroundingRequirement> requirements = null;
    requirements = new GroundingRequirementsParser().parseRequirements( reqString, formula.getUnboundVariables() );
    return new DynamicRule(formula, formula, formula, requirements);
  }

  /**
   * Parses a string containing a single causal rule of the form
   *   default A (if B)?
   * which is converted to the causal rule
   *   caused A if A && B
   * @param s a string containing a single causal rule.
   * @param reqString the grounding requirements of this rule.
   * @return the corresponding causal rule
   * @throws ParserException
   * @throws IOException
   */
  private Formula parseDefaultFormula(String s, String reqString) throws ParserException, IOException {
    if(!s.startsWith("default "))
      throw new ParserException("Missing 'default' expression in causal rule: "+s);
    FolFormula ifFormula = null;
    FolFormula headFormula = null;
    if(s.contains( " if " )) {
      String ifString = s.substring( s.indexOf(" if ") + 4 );
      Set<String> wrongKey = containedKeywords( ifString );
      if(!wrongKey.isEmpty())
        throw new ParserException("Unexpected keyword(s) '"+wrongKey.toString()+"' in formula '"+s+"' due to wrong order or multiple use.");
      ifFormula = parseFolFormula( ifString );
      s = s.substring( 0, s.indexOf( " if ") );
    }
    String headString = s.substring( s.indexOf("default ") + 8 );
    Set<String> wrongKey = containedKeywords( headString );
    if(!wrongKey.isEmpty())
      throw new ParserException("Unexpected keyword(s) '"+wrongKey.toString()+"' in formula '"+s+"' due to wrong order or multiple use of keywords.");
    headFormula = parseFolFormula( headString );
    
    // parse grounding requirements
    Set<Variable> variables = new HashSet<Variable>();
    variables.addAll( headFormula.getUnboundVariables() );
    if(ifFormula != null)
      variables.addAll( ifFormula.getUnboundVariables() );
    Set<GroundingRequirement> requirements = null;
    requirements = new GroundingRequirementsParser().parseRequirements( reqString, variables );
    
    if(ifFormula != null) {
      Conjunction c = new Conjunction();
      c.add( headFormula );
      c.add( ifFormula );
      return new StaticRule( headFormula, c, requirements );
    } else {
      return new StaticRule( headFormula, requirements );
    }
  }
  
  /**
   * Parses a string containing a single causal rule of the form
   *   A causes B if C
   * which is converted to
   *   caused B if + after A && C
   * @param s a string containing a single causal rule.
   * @param reqString the grounding requirements of this rule.
   * @return the corresponding causal rule
   * @throws ParserException
   * @throws IOException
   */
  private Formula parseCausesFormula(String s, String reqString) throws ParserException, IOException {
    if(!s.contains( " causes " ))
      throw new ParserException("Missing 'causes' expression in causal rule: "+s);
    FolFormula headFormula = null;
    FolFormula causesFormula = null;
    FolFormula ifFormula = null;

    if(s.contains( " if " )) {
      String ifString = s.substring( s.indexOf(" if ") + 4 );
      Set<String> wrongKey = containedKeywords( ifString );
      if(!wrongKey.isEmpty())
        throw new ParserException("Unexpected keyword(s) '"+wrongKey.toString()+"' in formula '"+s+"' due to wrong order or multiple use.");
      ifFormula = parseFolFormula( ifString );
      s = s.substring( 0, s.indexOf( " if ") );
    }
    String[] split = s.split( " causes " );
    String headString = split[0];
    Set<String> wrongKey = containedKeywords( headString );
    if(!wrongKey.isEmpty())
      throw new ParserException("Unexpected keyword(s) '"+wrongKey.toString()+"' in formula '"+s+"' due to wrong order or multiple use of keywords.");
    headFormula = parseFolFormula( headString );
    
    String causesString = split[1];
    wrongKey = containedKeywords( causesString );
    if(!wrongKey.isEmpty())
      throw new ParserException("Unexpected keyword(s) '"+wrongKey.toString()+"' in formula '"+s+"' due to wrong order or multiple use of keywords.");
    causesFormula = parseFolFormula( causesString );
    
    // parse grounding requirements
    Set<Variable> variables = new HashSet<Variable>();
    variables.addAll( headFormula.getUnboundVariables() );
    variables.addAll( causesFormula.getUnboundVariables() );
    if(ifFormula != null)
      variables.addAll( ifFormula.getUnboundVariables() );
    Set<GroundingRequirement> requirements = null;
    requirements = new GroundingRequirementsParser().parseRequirements( reqString, variables );
    
    if(ifFormula != null) {
      Conjunction c = new Conjunction();
      c.add( headFormula );
      c.add( ifFormula );
      return new DynamicRule( causesFormula, new Tautology(), c, requirements );
    } else {
      return new DynamicRule( causesFormula, new Tautology(), headFormula, requirements );
    }
  }
  
  /**
   * Parses a string containing a single causal rule of the form
   *   always A
   * which is converted to
   *   caused - if !A
   * @param s a string containing a single causal rule.
   * @param reqString the grounding requirements of this rule.
   * @return the corresponding causal rule
   * @throws ParserException
   * @throws IOException
   */
  private Formula parseAlwaysFormula(String s, String reqString) throws ParserException, IOException {
    if(!s.startsWith( "always " ))
      throw new ParserException("Missing 'always' expression in causal rule: "+s);
    FolFormula headFormula = null;

    Set<String> wrongKey = containedKeywords( s );
    if(!wrongKey.isEmpty())
      throw new ParserException("Unexpected keyword(s) '"+wrongKey.toString()+"' in formula '"+s+"' due to wrong order or multiple use of keywords.");
    headFormula = parseFolFormula( s );
    
    // parse grounding requirements
    Set<Variable> variables = new HashSet<Variable>();
    variables.addAll( headFormula.getUnboundVariables() );
    Set<GroundingRequirement> requirements = null;
    requirements = new GroundingRequirementsParser().parseRequirements( reqString, variables );
    
    return new StaticRule(new Contradiction(), new Negation( headFormula ), requirements);
  }
  
  /**
   * Parses a string containing a single causal rule of the form
   *   nonexecutable A if B
   * which is converted to
   *   caused - after A && B
   * @param s a string containing a single causal rule.
   * @param reqString the grounding requirements of this rule.
   * @return the corresponding causal rule
   * @throws ParserException
   * @throws IOException
   */
  private Formula parseNonexecutableFormula(String s, String reqString) throws ParserException, IOException {
    if(!s.startsWith("nonexecutable "))
      throw new ParserException("Missing 'nonexecutable' expression in causal rule: "+s);
    FolFormula ifFormula = null;
    FolFormula headFormula = null;
    if(s.contains( " if " )) {
      String ifString = s.substring( s.indexOf(" if ") + 4 );
      Set<String> wrongKey = containedKeywords( ifString );
      if(!wrongKey.isEmpty())
        throw new ParserException("Unexpected keyword(s) '"+wrongKey.toString()+"' in formula '"+s+"' due to wrong order or multiple use.");
      ifFormula = parseFolFormula( ifString );
      s = s.substring( 0, s.indexOf( " if ") );
    }
    String headString = s.substring( s.indexOf("nonexecutable ") + 14 );
    Set<String> wrongKey = containedKeywords( headString );
    if(!wrongKey.isEmpty())
      throw new ParserException("Unexpected keyword(s) '"+wrongKey.toString()+"' in formula '"+s+"' due to wrong order or multiple use of keywords.");
    headFormula = parseFolFormula( headString );
    
    // parse grounding requirements
    Set<Variable> variables = new HashSet<Variable>();
    variables.addAll( headFormula.getUnboundVariables() );
    if(ifFormula != null)
      variables.addAll( ifFormula.getUnboundVariables() );
    Set<GroundingRequirement> requirements = null;
    requirements = new GroundingRequirementsParser().parseRequirements( reqString, variables );
    
    if(ifFormula != null) {
      Conjunction c = new Conjunction();
      c.add( headFormula );
      c.add( ifFormula );
      return new DynamicRule( new Contradiction(), c, requirements );
    } else {
      return new DynamicRule( new Contradiction(), headFormula, requirements );
    }
  }
  
  /**
   * Parses a string containing a single causal rule of the form
   *   A may cause B if C
   * which is converted to
   *   caused B if B after A && C
   * @param s a string containing a single causal rule.
   * @param reqString the grounding requirements of this rule.
   * @return the corresponding causal rule
   * @throws ParserException
   * @throws IOException
   */
  private Formula parseMayCauseFormula(String s, String reqString) throws ParserException, IOException {
    if(!s.contains( " may cause " ))
      throw new ParserException("Missing 'may cause' expression in causal rule: "+s);
    FolFormula headFormula = null;
    FolFormula maycauseFormula = null;
    FolFormula ifFormula = null;

    if(s.contains( " if " )) {
      String ifString = s.substring( s.indexOf(" if ") + 4 );
      Set<String> wrongKey = containedKeywords( ifString );
      if(!wrongKey.isEmpty())
        throw new ParserException("Unexpected keyword(s) '"+wrongKey.toString()+"' in formula '"+s+"' due to wrong order or multiple use.");
      ifFormula = parseFolFormula( ifString );
      s = s.substring( 0, s.indexOf( " if ") );
    }
    String[] split = s.split( " may cause " );
    String headString = split[0];
    Set<String> wrongKey = containedKeywords( headString );
    if(!wrongKey.isEmpty())
      throw new ParserException("Unexpected keyword(s) '"+wrongKey.toString()+"' in formula '"+s+"' due to wrong order or multiple use of keywords.");
    headFormula = parseFolFormula( headString );
    
    String maycauseString = split[1];
    wrongKey = containedKeywords( maycauseString );
    if(!wrongKey.isEmpty())
      throw new ParserException("Unexpected keyword(s) '"+wrongKey.toString()+"' in formula '"+s+"' due to wrong order or multiple use of keywords.");
    maycauseFormula = parseFolFormula( maycauseString );
    
    // parse grounding requirements
    Set<Variable> variables = new HashSet<Variable>();
    variables.addAll( headFormula.getUnboundVariables() );
    variables.addAll( maycauseFormula.getUnboundVariables() );
    if(ifFormula != null)
      variables.addAll( ifFormula.getUnboundVariables() );
    Set<GroundingRequirement> requirements = null;
    requirements = new GroundingRequirementsParser().parseRequirements( reqString, variables );
    
    if(ifFormula != null) {
      Conjunction c = new Conjunction();
      c.add( headFormula );
      c.add( ifFormula );
      return new DynamicRule( maycauseFormula, maycauseFormula, c, requirements );
    } else {
      return new DynamicRule( maycauseFormula, maycauseFormula, headFormula, requirements );
    }
  }
  
  /**
   * Returns the set of forbidden keywords, that are contained in a string. This is used
   * for error recognition in input strings.
   * @param s 
   * @return
   */
  protected Set<String> containedKeywords(String s) {
    final String[] keywords = {"caused ", " if ", " after ", " requires ", "inertial ", "default ", " causes ", "always ", "nonexecutable ", " may cause " };
    Set<String> result = new HashSet<String>();
    for(String key : keywords) {
      if(s.contains( key ))
        result.add( key );
    }
    return result;
  }
}