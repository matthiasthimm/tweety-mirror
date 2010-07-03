/* Generated By:JavaCC: Do not edit this line. DungParser.java */
package edu.cs.ai.kr.dung.parser;

import java.io.*;
import edu.cs.ai.kr.*;
import edu.cs.ai.kr.dung.*;
import edu.cs.ai.kr.dung.syntax.*;

/**
  * This class implements a parser for dung argumentation theories. The BNF for dung abstract
  * argumentation theory files is given by (start symbol is THEORY)
  * <br>
  * <br>THEORY 			::== (EXPRESSION)*
  * <br>EXPRESSION		::== ARGUMENT | ATTACK
  * <br>ARGUMENT		::== "argument(" + ARGUMENT_NAME + ")."
  * <br>ATTACK			::== "attack(" + ARGUMENT_NAME + "," + ARGUMENT_NAME + ")."
  * <br>
  * <br>ARGUMENT_NAME is a sequence of symbols from {a,...,z,A,...,Z,0,...,9,_,-,(,),[,],{,},~,<,>,.} with a letter at the beginning.
  */
@SuppressWarnings("all")
public class DungParser extends Parser implements DungParserConstants {

        public DungParser(){
        }

        public DungTheory parseBeliefBase(Reader reader) throws ParserException{
                try
                {
                        DungParser theParser = new DungParser(reader);
                        return theParser.Theory();
                }catch(ParseException e){
                        throw new ParserException(e);
                }
        }

        public Formula parseFormula(Reader reader) throws ParserException{
                try{
                        DungParser theParser = new DungParser(reader);
                        return theParser.SingleFormula();
                }catch(ParseException e){
                        throw new ParserException(e);
                }
        }

  static final public DungTheory Theory() throws ParseException {
        DungTheory dungTheory = new DungTheory();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 6:
      case 9:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      Expression(dungTheory);
    }
    jj_consume_token(0);
        {if (true) return dungTheory;}
    throw new Error("Missing return statement in function");
  }

  static final public Formula SingleFormula() throws ParseException {
        Formula f;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ARGUMENT_NAME:
    case 6:
      f = SingleArgument();
      break;
    case 9:
    case 11:
      f = SingleAttack();
                {if (true) return f;}
      break;
    default:
      jj_la1[1] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public void Expression(DungTheory dungTheory) throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 6:
      Argument(dungTheory);
      break;
    case 9:
      Attack(dungTheory);
      break;
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  static final public void Argument(DungTheory dungTheory) throws ParseException {
        Token argumentName;
    jj_consume_token(6);
    argumentName = jj_consume_token(ARGUMENT_NAME);
    jj_consume_token(7);
                        Argument argument = new Argument(argumentName.image);
                        dungTheory.add(argument);
  }

  static final public Argument SingleArgument() throws ParseException {
        Token argumentName;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 6:
      jj_consume_token(6);
      argumentName = jj_consume_token(ARGUMENT_NAME);
      jj_consume_token(8);
      break;
    case ARGUMENT_NAME:
      argumentName = jj_consume_token(ARGUMENT_NAME);
                        {if (true) return new Argument(argumentName.image);}
      break;
    default:
      jj_la1[3] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public Attack SingleAttack() throws ParseException {
        Token attackerName;
        Token attackedName;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 9:
      jj_consume_token(9);
      attackerName = jj_consume_token(ARGUMENT_NAME);
      jj_consume_token(10);
      attackedName = jj_consume_token(ARGUMENT_NAME);
      jj_consume_token(8);
      break;
    case 11:
      jj_consume_token(11);
      attackerName = jj_consume_token(ARGUMENT_NAME);
      jj_consume_token(10);
      attackedName = jj_consume_token(ARGUMENT_NAME);
      jj_consume_token(8);
                Argument attacker = new Argument(attackerName.image);
                Argument attacked = new Argument(attackerName.image);
                {if (true) return new Attack(attacker,attacked);}
      break;
    default:
      jj_la1[4] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public void Attack(DungTheory dungTheory) throws ParseException {
        Token attackerName;
        Token attackedName;
    jj_consume_token(9);
    attackerName = jj_consume_token(ARGUMENT_NAME);
    jj_consume_token(10);
    attackedName = jj_consume_token(ARGUMENT_NAME);
    jj_consume_token(7);
                Argument attacker = new Argument(attackerName.image);
                Argument attacked = new Argument(attackerName.image);
                if(!dungTheory.contains(attacker) || !dungTheory.contains(attacked))
                        {if (true) throw new ParseException("undefined argument in attack relation.");}
                dungTheory.add(new Attack(attacker,attacked));
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public DungParserTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[5];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x240,0xa60,0x240,0x60,0xa00,};
   }

  /** Constructor with InputStream. */
  public DungParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public DungParser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new DungParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public DungParser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new DungParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public DungParser(DungParserTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(DungParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 5; i++) jj_la1[i] = -1;
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[12];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 5; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 12; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

}
