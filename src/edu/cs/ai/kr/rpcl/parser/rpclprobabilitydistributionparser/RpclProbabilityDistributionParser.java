/* Generated By:JavaCC: Do not edit this line. RpclProbabilityDistributionParser.java */
package edu.cs.ai.kr.rpcl.parser.rpclprobabilitydistributionparser;

import java.io.*;
import java.util.*;
import edu.cs.ai.kr.*;
import edu.cs.ai.kr.fol.syntax.*;
import edu.cs.ai.kr.fol.semantics.*;
import edu.cs.ai.kr.rpcl.semantics.*;
import edu.cs.ai.util.*;

/**
 * This class implements a parser for relational probability distributions. The BNF for 
 * relational probability distributions is given by (start symbol is DISTRIBUTION)
 * <br>
 * <br>DISTRIBUTION				::== (PROBABILITYASSIGNMENT)*
 * <br>PROBABILITYASSIGNMENT	::== INTERPRETATION "=" PROBABILITY
 * <br>INTERPRETATION			::== "{" (GROUNDATOM ("," GROUNDATOM)*)? "}"
 * <br>GROUNDATOM				::== PREDICATE ("(" CONSTANT ("," CONSTANT)* ")")?
 * <br>
 * <br>PREDICATE is a sequence of symbols from {a,...,z,A,...,Z,0,...,9} with a lowercase letter at the beginning.<br>
 * <br>CONSTANT is a sequence of symbols from {a,...,z,A,...,Z,0,...,9} with a lowercase letter at the beginning.<br>
 * <br>PROBABILITY is a number in [0,1].<br>
 */
@SuppressWarnings("all")
public class RpclProbabilityDistributionParser implements RpclProbabilityDistributionParserConstants {

        /**
     * The semantics used for the distribution to be read. 
     */
        private RpclSemantics semantics = null;

        /**
	 * The signature for this parser (if one has been given)
	 */
        private FolSignature signature = null;

        public RpclProbabilityDistributionParser(){
        }

        public RpclProbabilityDistributionParser(RpclSemantics semantics){
                this(semantics,null);
        }

        public RpclProbabilityDistributionParser(RpclSemantics semantics, FolSignature signature){
                this.semantics = semantics;
                this.signature = signature;
        }

        public void setSemantics(RpclSemantics semantics){
                this.semantics = semantics;
        }

        public void setSignature(FolSignature signature){
                this.signature = signature;
        }

        public ProbabilityDistribution parseProbabilityDistribution(Reader reader) throws ParserException{
                try
                {
                        RpclProbabilityDistributionParser theParser = new RpclProbabilityDistributionParser(reader);
                        return theParser.Distribution(this.semantics, this.signature);
                }catch(ParseException e){
                        throw new ParserException(e);
                }
        }

  static final public ProbabilityDistribution Distribution(RpclSemantics semantics, FolSignature signature) throws ParseException {
        Set<Pair<HerbrandInterpretation,Probability>> assignments = new HashSet<Pair<HerbrandInterpretation,Probability>>();
        Pair<HerbrandInterpretation,Probability> assignment;
                if(signature == null)
                        signature = new FolSignature();
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 7:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      assignment = ProbabilityAssignment(signature);
                assignments.add(assignment);
    }
    jj_consume_token(0);
        ProbabilityDistribution distribution = new ProbabilityDistribution(semantics,signature);
        for(Pair<HerbrandInterpretation,Probability> a: assignments)
                distribution.put(a.getFirst(),a.getSecond());
        {if (true) return distribution;}
    throw new Error("Missing return statement in function");
  }

  static final public Pair<HerbrandInterpretation,Probability> ProbabilityAssignment(FolSignature signature) throws ParseException {
        Set<Atom> atoms = new HashSet<Atom>();
        Atom atom;
        Token probability;
    jj_consume_token(7);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case STRUCTURENAME:
      atom = GroundAtom(signature);
                atoms.add(atom);
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 8:
          ;
          break;
        default:
          jj_la1[1] = jj_gen;
          break label_2;
        }
        jj_consume_token(8);
        atom = GroundAtom(signature);
                atoms.add(atom);
      }
      break;
    default:
      jj_la1[2] = jj_gen;
      ;
    }
    jj_consume_token(9);
    jj_consume_token(10);
    probability = jj_consume_token(PROBABILITY);
                {if (true) return new Pair<HerbrandInterpretation,Probability>(new HerbrandInterpretation(atoms),new Probability(new Double(token.image)));}
    throw new Error("Missing return statement in function");
  }

  static final public Atom GroundAtom(FolSignature signature) throws ParseException {
        Token predicate;
        Token constant;
        List<Constant> parameters = new ArrayList<Constant>();
        Constant c = null;
    predicate = jj_consume_token(STRUCTURENAME);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 11:
      jj_consume_token(11);
      constant = jj_consume_token(STRUCTURENAME);
                if(signature.containsConstant(constant.image)){
                        c = signature.getConstant(constant.image);
                        parameters.add(c);
                }else{
                        c = new Constant(constant.image);
                        signature.add(c);
                        parameters.add(c);
                }
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 8:
          ;
          break;
        default:
          jj_la1[3] = jj_gen;
          break label_3;
        }
        jj_consume_token(8);
        constant = jj_consume_token(STRUCTURENAME);
                if(signature.containsConstant(constant.image)){
                        c = signature.getConstant(constant.image);
                        parameters.add(c);
                }else{
                        c = new Constant(constant.image);
                        signature.add(c);
                        parameters.add(c);
                }
      }
      jj_consume_token(12);
      break;
    default:
      jj_la1[4] = jj_gen;
      ;
    }
                Predicate p;
                if(signature.containsPredicate(predicate.image))
                        p = signature.getPredicate(predicate.image);
                else{
                        p = new Predicate(predicate.image, parameters.size());
                        signature.add(p);
                }
                {if (true) return new Atom(p,parameters);}
    throw new Error("Missing return statement in function");
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public RpclProbabilityDistributionParserTokenManager token_source;
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
      jj_la1_0 = new int[] {0x80,0x100,0x20,0x100,0x800,};
   }

  /** Constructor with InputStream. */
  public RpclProbabilityDistributionParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public RpclProbabilityDistributionParser(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new RpclProbabilityDistributionParserTokenManager(jj_input_stream);
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
  public RpclProbabilityDistributionParser(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new RpclProbabilityDistributionParserTokenManager(jj_input_stream);
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
  public RpclProbabilityDistributionParser(RpclProbabilityDistributionParserTokenManager tm) {
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
  public void ReInit(RpclProbabilityDistributionParserTokenManager tm) {
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
    boolean[] la1tokens = new boolean[13];
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
    for (int i = 0; i < 13; i++) {
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
