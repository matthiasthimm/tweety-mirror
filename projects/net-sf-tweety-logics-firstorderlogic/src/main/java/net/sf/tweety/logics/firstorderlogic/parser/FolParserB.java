/* Generated By:JavaCC: Do not edit this line. FolParserB.java */
package net.sf.tweety.logics.firstorderlogic.parser;

import java.util.*;
import net.sf.tweety.logics.firstorderlogic.syntax.*;
import net.sf.tweety.logics.commons.syntax.*;
import net.sf.tweety.logics.commons.syntax.interfaces.*;
import net.sf.tweety.util.Pair;

import net.sf.tweety.logics.firstorderlogic.FolBeliefSet;

@SuppressWarnings("all")
/**
 *	A parser for first-order logical knowledge-bases and formulas.
 *	It supports the 'forceDefinition' flag which turns the before-hand
 *	definition of sorts, predicates and functors on/off.
 */
public class FolParserB implements FolParserBConstants {
  public void setForce(boolean forceDefinitions)
  {
    this.forceDefinitions = forceDefinitions;
  }
  private boolean forceDefinitions = false;

  private FolFormula helper(FolFormula form, Pair<Integer, FolFormula > exform)
        throws ParseException
  {
    FolFormula reval = null;
    if(exform.getFirst() == 0)
    {
      return form;
    }
    if(exform.getFirst() == 1)
    {
      return new Conjunction(form, exform.getSecond());
    }
    if(exform.getFirst() == 2)
    {
      return new Disjunction(form, exform.getSecond());
    }
    throw new ParseException("Error");
  }

  /**
   *
   */
  private List<Sort > getSorts(FolSignature sig, List<String > names)
        throws ParseException
  {
    List<Sort >reval = new LinkedList<Sort >();
    for(String name : names)
    {
      Sort s = sig.getSort(name);
      if(s == null)
      {
        if(forceDefinitions)
        {
          throw new ParseException("Sort with name '" + name + "' was not defined.");
        }
        else
        {
          s = new Sort(name);
          sig.add(s);
        }
      }
      reval.add(s);
    }
    return reval;
  }

  private Functor getFunctor(String name, FolSignature signature)
  {
    return signature.getFunctor(name);
  }

  private Functor getOrCreateFunctor(String name, List<Term<?>> args, Sort targetSort, FolSignature signature)
        throws ParseException
  {
    Functor reval = signature.getFunctor(name);
    if(reval == null)
    {
      if(forceDefinitions)
      {
        throw new ParseException("Functor with name '" + name + "' was not defined.");
      }
      else
      {
        List<Sort >sorts = new LinkedList<Sort >();
        for(Term t : args)
        {
          sorts.add(t.getSort());
        }
        reval = new Functor(name, sorts, targetSort);
      }
    }
    return reval;
  }

  private Predicate getPredicate(String name, FolSignature sig)
  {
    return sig.getPredicate(name);
  }

  private Sort getArgumentType(TypedStructure fbs, int count)
    throws ParseException
  {
    if(fbs == null)
    {
      return Sort.THING;
    }
    if(count >= fbs.getArgumentTypes().size())
    {
          return null;
    }

    return fbs.getArgumentTypes().get(count);
  }

  private Constant getOrCreateConstant(String name, Sort type, FolSignature sig)
    throws ParseException
  {
    Constant reval = sig.getConstant(name);
    if(reval == null)
    {
      reval = new Constant(name, type);
      sig.add(reval);
    }
    else if(!reval.getSort().equals(type))
    {
      throw new ParseException("It exists a constant '" + name + "' with type '" + reval.getSort() + "' but asked for type '" + type + "'. Do not try to mix before-hand declarations without the force-delcaration flag, because every constant defined before hand will be in THING sort and this might generate errors.");
    }
    return reval;
  }

  final public FolBeliefSet KB() throws ParseException {
  List<Sort > sorts;
  FolFormula ff;
  FolBeliefSet bb = new FolBeliefSet();
  FolSignature signature = (FolSignature)bb.getSignature();
    if (jj_2_1(2)) {
      sorts = sortdecl();
     for(Sort s : sorts)
     {
       signature.add(s);
     }
      label_1:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case TYPE:
          ;
          break;
        default:
          jj_la1[0] = jj_gen;
          break label_1;
        }
        declar(signature);
      }
    } else {
      ;
    }
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case FORALL:
      case EXISTS:
      case NOT:
      case TAUTOLOGY:
      case CONTRADICTION:
      case LBRA:
      case CONSTANT:
      case VARIABLE:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_2;
      }
      ff = formula(signature);
     bb.add(ff);
    }
   {if (true) return bb;}
    throw new Error("Missing return statement in function");
  }

  final public FolFormula formula(FolSignature signature) throws ParseException {
  Token t;
  FolFormula temp;
  Pair<Integer, FolFormula > ex;
  FolFormula reval;
  Variable v = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case CONSTANT:
    case VARIABLE:
      temp = atom(signature);
      ex = formulaex(signature);
              reval = helper(temp, ex);
      break;
    case LBRA:
      jj_consume_token(LBRA);
      temp = formula(signature);
      jj_consume_token(RBRA);
      ex = formulaex(signature);
          reval = helper(temp, ex);
      break;
    case FORALL:
      jj_consume_token(FORALL);
      t = jj_consume_token(VARIABLE);
      jj_consume_token(DDOT);
      temp = formula(signature);
      ex = formulaex(signature);
              v = new Variable(t.image);
              reval = new ForallQuantifiedFormula(helper(temp, ex), v);
      break;
    case EXISTS:
      jj_consume_token(EXISTS);
      t = jj_consume_token(VARIABLE);
      jj_consume_token(DDOT);
      temp = formula(signature);
      ex = formulaex(signature);
              v = new Variable(t.image);
              reval = new ExistsQuantifiedFormula(helper(temp, ex), v);
      break;
    case NOT:
      jj_consume_token(NOT);
      temp = formula(signature);
      ex = formulaex(signature);
              reval = new Negation(helper(temp, ex));
      break;
    case CONTRADICTION:
      jj_consume_token(CONTRADICTION);
              reval = new Contradiction();
      break;
    case TAUTOLOGY:
      jj_consume_token(TAUTOLOGY);
              reval = new Tautology();
      break;
    default:
      jj_la1[2] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return reval;}
    throw new Error("Missing return statement in function");
  }

  final public Pair<Integer, FolFormula> formulaex(FolSignature signature) throws ParseException {
  Integer status = 0; // 0 nothing, 1 and, 2 or
  FolFormula form = null;
  Pair<Integer, FolFormula> reval = new Pair<Integer, FolFormula>();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case OR:
    case AND:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case AND:
        jj_consume_token(AND);
        form = formula(signature);
        status = 1;
        break;
      case OR:
        jj_consume_token(OR);
        form = formula(signature);
        status = 2;
        break;
      default:
        jj_la1[3] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    default:
      jj_la1[4] = jj_gen;
      ;
    }
    reval.setFirst(status);
    reval.setSecond(form);
    {if (true) return reval;}
    throw new Error("Missing return statement in function");
  }

  final public FOLAtom atom(FolSignature signature) throws ParseException {
  String identifier;
  Term t;
  FOLAtom reval;
  Predicate p;
  int count = 0;
  Sort type;
  List<Term<?>> terms = new LinkedList<Term<?>>();
    identifier = identifier();
    p = getPredicate(identifier, signature);
    type = getArgumentType(p, count++);
    if(p==null && this.forceDefinitions)
    {
      {if (true) throw new ParseException("Predicate '" + identifier + "' was not defined. Do not set the force flag for definitions or define the predicate beforehand.");}
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRA:
      jj_consume_token(LBRA);
      t = term(signature, type);
          terms.add(t);
          type = getArgumentType(p, count++);
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[5] = jj_gen;
          break label_3;
        }
        jj_consume_token(COMMA);
        t = term(signature, type);
            terms.add(t);
            type = getArgumentType(p, count++);
      }
      jj_consume_token(RBRA);
      break;
    default:
      jj_la1[6] = jj_gen;
      ;
    }
    if(p == null)
    {
      p = new Predicate(identifier, terms.size());
      signature.add(p);
        }
        reval = new FOLAtom(p, terms);
    {if (true) return reval;}
    throw new Error("Missing return statement in function");
  }

  final public Term<?> term(FolSignature signature, Sort type) throws ParseException {
  Term temp;
  String fname;
  int count = 0;
  Sort subtype;
  Functor f;
  List<Term<?>> terms = new LinkedList<Term<?>>();
  Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case CONSTANT:
    case VARIABLE:
      fname = identifier();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case LBRA:
        jj_consume_token(LBRA);
            f = getFunctor(fname, signature);
            if(f == null && this.forceDefinitions)
            {
              {if (true) throw new ParseException("Functor with name '" + fname + "' was not definied. Define it beforehand or did not use the force-delaration flag.");}
            }
            subtype = getArgumentType(f, count++);
        temp = term(signature, subtype);
                terms.add(temp);
                subtype = getArgumentType(f, count++);
        label_4:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case COMMA:
            ;
            break;
          default:
            jj_la1[7] = jj_gen;
            break label_4;
          }
          jj_consume_token(COMMA);
          temp = term(signature, subtype);
              terms.add(temp);
              subtype = getArgumentType(f, count++);
        }
        jj_consume_token(RBRA);
            {if (true) return new FunctionalTerm(getOrCreateFunctor(fname, terms, type, signature), terms);}
        break;
      default:
        jj_la1[8] = jj_gen;
        ;
      }
          if(fname.charAt(0) >= 65 && fname.charAt(0) <= 90)
          {
            {if (true) return new Variable(fname);}
          }
          else
          {
            {if (true) return getOrCreateConstant(fname, type, signature);}
          }
      break;
    case NUMBER:
      t = jj_consume_token(NUMBER);
      {if (true) return new NumberTerm(Integer.parseInt(t.image));}
      break;
    default:
      jj_la1[9] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  final public String identifier() throws ParseException {
  Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case CONSTANT:
      t = jj_consume_token(CONSTANT);
      break;
    case VARIABLE:
      t = jj_consume_token(VARIABLE);
      break;
    default:
      jj_la1[10] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return t.image;}
    throw new Error("Missing return statement in function");
  }

  final public List<Sort > sortdecl() throws ParseException {
  Set<Constant > temp;
  Sort act;
  List<Sort > reval = new LinkedList<Sort >();
  String name;
    label_5:
    while (true) {
      name = identifier();
            act = new Sort(name);
            reval.add(act);
      jj_consume_token(EQUAL);
      jj_consume_token(CURLBRA);
      temp = constants(act);
      jj_consume_token(CURRBRA);
          for(Constant c : temp)
          {
            act.add(c);
          }
      if (jj_2_2(2)) {
        ;
      } else {
        break label_5;
      }
    }
    {if (true) return reval;}
    throw new Error("Missing return statement in function");
  }

  final public Set<Constant > constants(Sort type) throws ParseException {
  Token t;
  Set<Constant > reval = new HashSet<Constant >();
    t = jj_consume_token(CONSTANT);
    reval.add(new Constant(t.image, type));
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[11] = jj_gen;
        break label_6;
      }
      jj_consume_token(COMMA);
      t = jj_consume_token(CONSTANT);
      reval.add(new Constant(t.image, type));
    }
    {if (true) return reval;}
    throw new Error("Missing return statement in function");
  }

  final public void declar(FolSignature signature) throws ParseException {
  String name;
  TypedStructure fbs = null;
    jj_consume_token(TYPE);
    jj_consume_token(LBRA);
    name = identifier();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRA:
    case RBRA:
      fbs = endPredicateDeclar(signature, name);
      break;
    case EQUAL:
      fbs = endFunctorDeclar(signature, name);
      break;
    default:
      jj_la1[12] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    /*
    if(ftor)
    {
      List<String > one_elem = new LinkedList<String >();
      one_elem.add(name);
      fbs = new Functor(name2,
      	getSorts(signature, sorts),
      	getSorts(signature, one_elem).get(0));
    }
    else
    {
      fbs = new Predicate(name, getSorts(signature, sorts));
    }
    */
    signature.add(fbs);
  }

  final public TypedStructure endPredicateDeclar(FolSignature signature, String name) throws ParseException {
  String sort;
  Predicate reval = null;
  List<String > sorts = new LinkedList<String >();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LBRA:
      jj_consume_token(LBRA);
      sort = identifier();
      sorts.add(sort);
      label_7:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMMA:
          ;
          break;
        default:
          jj_la1[13] = jj_gen;
          break label_7;
        }
        jj_consume_token(COMMA);
        sort = identifier();
        sorts.add(sort);
      }
      jj_consume_token(RBRA);
      break;
    default:
      jj_la1[14] = jj_gen;
      ;
    }
    jj_consume_token(RBRA);
    reval = signature.getPredicate(name);
    if(reval != null)
    {
      {if (true) throw new ParseException("Predicate with name '" + name + "' already exists. Proof your kb for duplicate declarations.");}
    }
    reval = new Predicate(name, getSorts(signature, sorts));
    {if (true) return reval;}
    throw new Error("Missing return statement in function");
  }

  final public TypedStructure endFunctorDeclar(FolSignature signature, String sortName) throws ParseException {
  String name;
  String tempType;
  List<String >sorts = new LinkedList<String >();
  TypedStructure fbs;
    jj_consume_token(EQUAL);
    name = identifier();
    jj_consume_token(LBRA);
    tempType = identifier();
          sorts.add(tempType);
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[15] = jj_gen;
        break label_8;
      }
      jj_consume_token(COMMA);
      tempType = identifier();
            sorts.add(tempType);
    }
    jj_consume_token(RBRA);
    jj_consume_token(RBRA);
    List<String > one_elem = new LinkedList<String >();
    one_elem.add(sortName);
    fbs = new Functor(name,
        getSorts(signature, sorts),
        getSorts(signature, one_elem).get(0));
    {if (true) return fbs;}
    throw new Error("Missing return statement in function");
  }

  private boolean jj_2_1(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_1(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(0, xla); }
  }

  private boolean jj_2_2(int xla) {
    jj_la = xla; jj_lastpos = jj_scanpos = token;
    try { return !jj_3_2(); }
    catch(LookaheadSuccess ls) { return true; }
    finally { jj_save(1, xla); }
  }

  private boolean jj_3R_10() {
    Token xsp;
    xsp = jj_scanpos;
    if (jj_scan_token(23)) {
    jj_scanpos = xsp;
    if (jj_scan_token(24)) return true;
    }
    return false;
  }

  private boolean jj_3_1() {
    if (jj_3R_9()) return true;
    return false;
  }

  private boolean jj_3_2() {
    if (jj_3R_10()) return true;
    if (jj_scan_token(EQUAL)) return true;
    return false;
  }

  private boolean jj_3R_9() {
    Token xsp;
    if (jj_3_2()) return true;
    while (true) {
      xsp = jj_scanpos;
      if (jj_3_2()) { jj_scanpos = xsp; break; }
    }
    return false;
  }

  /** Generated Token Manager. */
  public FolParserBTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private Token jj_scanpos, jj_lastpos;
  private int jj_la;
  private int jj_gen;
  final private int[] jj_la1 = new int[16];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x100,0x181e0c0,0x181e0c0,0x1800,0x1800,0x20000000,0x10000,0x20000000,0x10000,0x1c00000,0x1800000,0x20000000,0x30400,0x20000000,0x10000,0x20000000,};
   }
  final private JJCalls[] jj_2_rtns = new JJCalls[2];
  private boolean jj_rescan = false;
  private int jj_gc = 0;

  /** Constructor with InputStream. */
  public FolParserB(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public FolParserB(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new FolParserBTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 16; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
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
    for (int i = 0; i < 16; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor. */
  public FolParserB(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new FolParserBTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 16; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 16; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Constructor with generated Token Manager. */
  public FolParserB(FolParserBTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 16; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  /** Reinitialise. */
  public void ReInit(FolParserBTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 16; i++) jj_la1[i] = -1;
    for (int i = 0; i < jj_2_rtns.length; i++) jj_2_rtns[i] = new JJCalls();
  }

  private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      if (++jj_gc > 100) {
        jj_gc = 0;
        for (int i = 0; i < jj_2_rtns.length; i++) {
          JJCalls c = jj_2_rtns[i];
          while (c != null) {
            if (c.gen < jj_gen) c.first = null;
            c = c.next;
          }
        }
      }
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  static private final class LookaheadSuccess extends java.lang.Error { }
  final private LookaheadSuccess jj_ls = new LookaheadSuccess();
  private boolean jj_scan_token(int kind) {
    if (jj_scanpos == jj_lastpos) {
      jj_la--;
      if (jj_scanpos.next == null) {
        jj_lastpos = jj_scanpos = jj_scanpos.next = token_source.getNextToken();
      } else {
        jj_lastpos = jj_scanpos = jj_scanpos.next;
      }
    } else {
      jj_scanpos = jj_scanpos.next;
    }
    if (jj_rescan) {
      int i = 0; Token tok = token;
      while (tok != null && tok != jj_scanpos) { i++; tok = tok.next; }
      if (tok != null) jj_add_error_token(kind, i);
    }
    if (jj_scanpos.kind != kind) return true;
    if (jj_la == 0 && jj_scanpos == jj_lastpos) throw jj_ls;
    return false;
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
  private int[] jj_lasttokens = new int[100];
  private int jj_endpos;

  private void jj_add_error_token(int kind, int pos) {
    if (pos >= 100) return;
    if (pos == jj_endpos + 1) {
      jj_lasttokens[jj_endpos++] = kind;
    } else if (jj_endpos != 0) {
      jj_expentry = new int[jj_endpos];
      for (int i = 0; i < jj_endpos; i++) {
        jj_expentry[i] = jj_lasttokens[i];
      }
      jj_entries_loop: for (java.util.Iterator<?> it = jj_expentries.iterator(); it.hasNext();) {
        int[] oldentry = (int[])(it.next());
        if (oldentry.length == jj_expentry.length) {
          for (int i = 0; i < jj_expentry.length; i++) {
            if (oldentry[i] != jj_expentry[i]) {
              continue jj_entries_loop;
            }
          }
          jj_expentries.add(jj_expentry);
          break jj_entries_loop;
        }
      }
      if (pos != 0) jj_lasttokens[(jj_endpos = pos) - 1] = kind;
    }
  }

  /** Generate ParseException. */
  public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[31];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 16; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 31; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    jj_endpos = 0;
    jj_rescan_token();
    jj_add_error_token(0, 0);
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

  private void jj_rescan_token() {
    jj_rescan = true;
    for (int i = 0; i < 2; i++) {
    try {
      JJCalls p = jj_2_rtns[i];
      do {
        if (p.gen > jj_gen) {
          jj_la = p.arg; jj_lastpos = jj_scanpos = p.first;
          switch (i) {
            case 0: jj_3_1(); break;
            case 1: jj_3_2(); break;
          }
        }
        p = p.next;
      } while (p != null);
      } catch(LookaheadSuccess ls) { }
    }
    jj_rescan = false;
  }

  private void jj_save(int index, int xla) {
    JJCalls p = jj_2_rtns[index];
    while (p.gen > jj_gen) {
      if (p.next == null) { p = p.next = new JJCalls(); break; }
      p = p.next;
    }
    p.gen = jj_gen + xla - jj_la; p.first = token; p.arg = xla;
  }

  static final class JJCalls {
    int gen;
    Token first;
    int arg;
    JJCalls next;
  }

}
