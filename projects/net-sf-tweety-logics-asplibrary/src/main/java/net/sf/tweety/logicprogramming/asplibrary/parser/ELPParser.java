/* Generated By:JavaCC: Do not edit this line. ELPParser.java */
package net.sf.tweety.logicprogramming.asplibrary.parser;

import java.util.*;
import net.sf.tweety.logicprogramming.asplibrary.syntax.*;
import net.sf.tweety.logics.commons.syntax.*;

@SuppressWarnings("all")
public class ELPParser implements ELPParserConstants {
  public static void main(String args []) throws ParseException
  {
    ELPParser parser = new ELPParser(System.in);
    while (true)
    {
      System.out.println("Reading from standard input...");
      System.out.print("Enter an expression like \u005c"1+(2+3)*4;\u005c" :");
      try
      {
        switch (parser.one_line())
        {
          case 0 :
          System.out.println("OK.");
          break;
          case 1 :
          System.out.println("Goodbye.");
          break;
          default :
          break;
        }
      }
      catch (Exception e)
      {
        System.out.println("NOK.");
        System.out.println(e.getMessage());
        break;
      }
      catch (Error e)
      {
        System.out.println("Oops.");
        System.out.println(e.getMessage());
        break;
      }
    }
  }

  final public int one_line() throws ParseException {
    rule();
    {if (true) return 0;}
    throw new Error("Missing return statement in function");
  }

  final public RuleElement atom() throws ParseException {
        Atom ret = null;
        Token pred = null;
        List<Term<?> > terms = null;
        SymbolicSet symset = null;
    // atom with predicate
            pred = jj_consume_token(SYMBOL);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRA:
    case CURLBRA:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRA:
        jj_consume_token(LBRA);
        terms = termlist();
        jj_consume_token(RBRA);
        break;
      case CURLBRA:
        jj_consume_token(CURLBRA);
        symset = symbolicSet();
        jj_consume_token(CURRBRA);
        break;
      default:
        jj_la1[0] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    default:
      jj_la1[1] = jj_gen;
      ;
    }
          if (symset == null)
                {if (true) return Atom.instantiate(pred.image, terms);}
          else
                {if (true) return new Aggregate(pred.image,symset);}
    throw new Error("Missing return statement in function");
  }

  final public SymbolicSet symbolicSet() throws ParseException {
        Set<String >  vars = new HashSet<String >();
        List<Literal > lits = null;
        Token v0 = null;
    v0 = jj_consume_token(VAR);
                     vars.add(v0.image);
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 26:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_1;
      }
      jj_consume_token(26);
      v0 = jj_consume_token(VAR);
                           vars.add(v0.image);
    }
    jj_consume_token(COLON);
    lits = literalList();
        {if (true) return new SymbolicSet(vars,lits);}
    throw new Error("Missing return statement in function");
  }

  final public List<Term<?>> termlist() throws ParseException {
        List<Term<?>> ret = new LinkedList<Term<?>>();
        Token s;
        Term t;
    t = term();
                ret.add(t);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 26:
        ;
        break;
      default:
        jj_la1[3] = jj_gen;
        break label_2;
      }
      jj_consume_token(26);
      t = term();
                        ret.add(t);
    }
          {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public Term<?> term() throws ParseException {
  Token s;
  List<Term<?>> tl1 = null, tl2 = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case VAR:
      s = jj_consume_token(VAR);
                {if (true) return new Variable(s.image);}
      break;
    case SYMBOL:
      s = jj_consume_token(SYMBOL);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRA:
        jj_consume_token(LBRA);
        tl1 = termlist();
        jj_consume_token(RBRA);
        break;
      default:
        jj_la1[4] = jj_gen;
        ;
      }
                if (tl1 == null)
                {
                        char c = s.image.charAt(0);
                        boolean uppercase = c >= 65 && c <= 90;
                        boolean lowercase = c >= 97 && c <= 122;
                        if(uppercase)
                                {if (true) return new Variable(s.image);}
                        else if(lowercase)
                                {if (true) return new Constant(s.image);}
                        else
                                {if (true) throw new ParseException("Cannot parse term: '" + s.image + "'");}
                }
                //throw new ParseException("Atom are no terms anymore: '" + s.image + "(" + tl1 + ")'");
                // TODO: Atoms are no terms anymore, handle them as functors not as Constant
                //else
                //	return Atom.instantiate(s.image,tl1);
                String str = s.image + "(";
                if(tl1.size() > 0)
                {
                  str += tl1.get(0);
                }
                for(int i=1; i<tl1.size(); i++)
                {
                  str += ", " + tl1.get(i);
                }
                str += ")";
                {if (true) return new Constant(str);}
      break;
    case NUMBER:
      s = jj_consume_token(NUMBER);
                {if (true) return new NumberTerm(s.image);}
      break;
    case SQLBRA:
      jj_consume_token(SQLBRA);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SQLBRA:
      case CURLBRA:
      case NUMBER:
      case SYMBOL:
      case VAR:
        tl1 = termlist();
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case PIPE:
          jj_consume_token(PIPE);
          tl2 = termlist();
          break;
        default:
          jj_la1[5] = jj_gen;
          ;
        }
        break;
      default:
        jj_la1[6] = jj_gen;
        ;
      }
      jj_consume_token(SQRBRA);
                if(tl1 == null)
                        {if (true) return new ListTerm();}
                if(tl2 == null)
                        {if (true) return new ListTerm(tl1);}
                {if (true) return new ListTerm(tl1,tl2);}
      break;
    case CURLBRA:
      jj_consume_token(CURLBRA);
      tl1 = termlist();
      jj_consume_token(CURRBRA);
                {if (true) return new SetTerm(tl1);}
      break;
    default:
      jj_la1[7] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public Rule rule() throws ParseException {
  Rule r;
  List<Literal> head = new LinkedList<Literal >();
  List<RuleElement > body = new LinkedList<RuleElement >();
        boolean wc = false;
    head = literalListHead();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LOGIMPL:
    case WEAKIMPL:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LOGIMPL:
        jj_consume_token(LOGIMPL);
        body = literalListBody();
        break;
      case WEAKIMPL:
        jj_consume_token(WEAKIMPL);
        body = literalListBody();
                  wc = true;
        break;
      default:
        jj_la1[8] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    default:
      jj_la1[9] = jj_gen;
      ;
    }
    jj_consume_token(DOT);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SQLBRA:
      jj_consume_token(SQLBRA);
      jj_consume_token(SQRBRA);
      break;
    default:
      jj_la1[10] = jj_gen;
      ;
    }
          if (wc)
          {

          } else
                {if (true) return new Rule(head,body);}
    throw new Error("Missing return statement in function");
  }

  final public List<Rule> program() throws ParseException {
        List<Rule > elp = new LinkedList<Rule >();
        Rule r = null;
    label_3:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case DOT:
      case LOGIMPL:
      case WEAKIMPL:
      case NOT:
      case SYMBOL:
      case NEG:
        ;
        break;
      default:
        jj_la1[11] = jj_gen;
        break label_3;
      }
      r = rule();
                        if (r != null) elp.add(r);
    }
                {if (true) return elp;}
    throw new Error("Missing return statement in function");
  }

  final public List<Literal> literalList() throws ParseException {
        List<Literal>   lits = null;
        Object l = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NOT:
    case SYMBOL:
    case NEG:
      l = literal();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 26:
        jj_consume_token(26);
        lits = literalList();
        break;
      default:
        jj_la1[12] = jj_gen;
        ;
      }
      break;
    default:
      jj_la1[13] = jj_gen;
      ;
    }
                LinkedList<Literal > ret = new LinkedList<Literal >();
                if (l != null)
                        ret.add((Literal)l);
                if (lits != null)
                        ret.addAll(lits);
                {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public List<RuleElement > literalListBody() throws ParseException {
        List<RuleElement >      lits = new LinkedList<RuleElement>();
        RuleElement l = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NOT:
    case SYMBOL:
    case NEG:
      l = LiteralExpr();
                                    lits.add(l);
      label_4:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 26:
          ;
          break;
        default:
          jj_la1[14] = jj_gen;
          break label_4;
        }
        jj_consume_token(26);
        l = LiteralExpr();
                                            lits.add(l);
      }
      break;
    default:
      jj_la1[15] = jj_gen;
      ;
    }
                {if (true) return lits;}
    throw new Error("Missing return statement in function");
  }

  final public List<Literal> literalListHead() throws ParseException {
        List<Literal>   lits = null;
        Object l = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NOT:
    case SYMBOL:
    case NEG:
      l = literal();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PIPE:
      case TEXTOR:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case PIPE:
          jj_consume_token(PIPE);
          break;
        case TEXTOR:
          jj_consume_token(TEXTOR);
          break;
        default:
          jj_la1[16] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        lits = literalListHead();
        break;
      default:
        jj_la1[17] = jj_gen;
        ;
      }
      break;
    default:
      jj_la1[18] = jj_gen;
      ;
    }
                LinkedList<Literal > ret = new LinkedList<Literal >();
                if (l != null)
                        ret.add((Literal)l);
                if (lits != null)
                        ret.addAll(lits);
                {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public RuleElement LiteralExpr() throws ParseException {
        Object l0 = null, l1 = null, l2 = null;
        String op01 = null, op12 = null;
        Term t0 = null, t1 = null;
    l0 = literal();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 27:
    case 28:
    case 29:
    case 30:
    case 31:
    case 32:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 27:
        jj_consume_token(27);
                              op01 = "=";
        break;
      case 28:
        jj_consume_token(28);
                                op01 = "<=";
        break;
      case 29:
        jj_consume_token(29);
                               op01 = "<";
        break;
      case 30:
        jj_consume_token(30);
                                op01 = ">=";
        break;
      case 31:
        jj_consume_token(31);
                               op01 = ">";
        break;
      case 32:
        jj_consume_token(32);
                                op01 = "!=";
        break;
      default:
        jj_la1[19] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      l1 = literal();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 27:
      case 28:
      case 29:
      case 32:
      case 33:
      case 34:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 27:
          jj_consume_token(27);
                                      op12 = "=";
          break;
        case 32:
          jj_consume_token(32);
                                        op12 = "!=";
          break;
        case 28:
          jj_consume_token(28);
                                        op12 = "<=";
          break;
        case 29:
          jj_consume_token(29);
                                       op12 = "<";
          break;
        case 33:
          jj_consume_token(33);
                                       op12 = "+";
          break;
        case 34:
          jj_consume_token(34);
                                       op12 = "*";
          break;
        default:
          jj_la1[20] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        l2 = literal();
        break;
      default:
        jj_la1[21] = jj_gen;
        ;
      }
      break;
    default:
      jj_la1[22] = jj_gen;
      ;
    }
                boolean dbgOut = false;
                // check what to do
                if ((l0 != null) && (l1 == null) && (l2 == null))
                {
                        if (dbgOut) System.out.println("unary");
                        {if (true) return (RuleElement)l0;}
                }
                else if ((l0 != null) && (l1 != null) && (l2 == null))
                {
                        if (dbgOut) System.out.println("binary");

                        // check if one of the predicates is an aggregate
                        if (l0 instanceof Aggregate)
                        {
                          // l1 is right guard (maybe an assignment guard?)
                          Aggregate ag = (Aggregate) l0;
                          //ag.setRightGuard(l1.getAtom(), op01);
                      {if (true) return ag;}
                        }
                        else if (l1 instanceof Aggregate)
                        {
                          // l0 is left guard
                          Aggregate ag = (Aggregate) l1;
                          ag.setLeftGuard((Term<?>)l0, op01);
                          {if (true) return ag;}
                        }
                        else
                        {
                                // no aggregates, could be something else
                                // TODO: Readd Relation support: 
                                {if (true) return new Relation(op01,(Term<?>)l0,(Term<?>)l1);}
                        }
                }
                else if ((l0 != null) && (l1 != null) && (l2 != null))
                {
                        // three operators: if l1 is an aggregate, we have an
                        // aggregate function with lower and upper bounds.
                        // otherwise, we have an arithmetic operation with
                        // an assignment (op12 is the arithmetic operator,
                        // op01 must be "=" and is the assignment
                        if (l1 instanceof Aggregate)
                        {
                                Aggregate ag = (Aggregate) l1;
                                ag.setLeftGuard((Term<?>)l0,op01);
                                ag.setRightGuard((Term<?>)l2,op12);
                                {if (true) return (Aggregate)l1;}

                        }
                        else {if (true) return new Arithmetic(op12,(Term<?>)l1,(Term<? >)l2,(Term<?>)l0);}
                }
    throw new Error("Missing return statement in function");
  }

  final public Object literal() throws ParseException {
        boolean dneg = false;
        boolean tneg = false;
        Object reval = null;
        Literal l = null;
        Literal r = null;
        String set_leftop = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NOT:
      jj_consume_token(NOT);
                        dneg = true;
      break;
    default:
      jj_la1[23] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case NEG:
      jj_consume_token(NEG);
                        tneg = true;
      break;
    default:
      jj_la1[24] = jj_gen;
      ;
    }
    reval = atom();
                        Atom a0 = (Atom)reval;
                        if (dneg && tneg)
                                {if (true) return new Not( new Neg(a0) );}
                        else if (dneg && !tneg)
                                {if (true) return new Not( a0 );}
                        else if (!dneg && tneg)
                                {if (true) return new Neg( a0 );}
                        else
                                {if (true) return a0;}
          {if (true) return reval;}
    throw new Error("Missing return statement in function");
  }

  final public List<Literal > dlv_answerset() throws ParseException {
        List<Literal > ret = null;
    jj_consume_token(CURLBRA);
    ret = literalList();
    jj_consume_token(CURRBRA);
                {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  final public List<Literal > clasp_answerset() throws ParseException {
        List<Literal >  ret = new LinkedList<Literal >();
        Object l = null;
    label_5:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case NOT:
      case SYMBOL:
      case NEG:
        ;
        break;
      default:
        jj_la1[25] = jj_gen;
        break label_5;
      }
      l = literal();
                  System.out.println(l);
                        ret.add((Literal)l);
    }
                {if (true) return ret;}
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public ELPParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[26];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x1100,0x1100,0x4000000,0x4000000,0x100,0x4000,0x621400,0x621400,0x18000,0x18000,0x400,0x2298040,0x4000000,0x2280000,0x4000000,0x2280000,0x104000,0x104000,0x2280000,0xf8000000,0x38000000,0x38000000,0xf8000000,0x80000,0x2000000,0x2280000,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x0,0x1,0x7,0x7,0x1,0x0,0x0,0x0,};
   }

  /** Constructor with InputStream. */
  public ELPParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public ELPParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ELPParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 26; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 26; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public ELPParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ELPParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 26; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 26; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public ELPParser(ELPParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 26; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(ELPParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 26; i++) jj_la1[i] = -1;
  }

  private Token jj_consume_token(int kind) throws ParseException {
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
  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[35];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 26; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 35; i++) {
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
  final public void enable_tracing() {
  }

  /** Disable tracing. */
  final public void disable_tracing() {
  }

}
