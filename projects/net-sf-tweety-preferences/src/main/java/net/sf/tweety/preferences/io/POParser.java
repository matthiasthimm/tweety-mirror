/*
 *  This file is part of "Tweety", a collection of Java libraries for
 *  logical aspects of artificial intelligence and knowledge representation.
 *
 *  Tweety is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/* Generated By:JavaCC: Do not edit this line. POParser.java */
package net.sf.tweety.preferences.io;
import java.io.File;
import java.util.*;

import net.sf.tweety.commons.util.Triple;
import net.sf.tweety.preferences.*;

@SuppressWarnings("all")
public class POParser implements POParserConstants {
public POParser()
{
}

  public static PreferenceOrder < String > parse(File file) throws ParseException, java.io.FileNotFoundException
  {
    POParser parser;
    parser = new POParser(new java.io.FileInputStream(file));
    return parser.StringPreferenceOrder();
  }

  public static PreferenceOrder < String > parse(String filename) throws ParseException, java.io.FileNotFoundException
  {
    POParser parser;
    parser = new POParser(new java.io.FileInputStream(filename));
    return parser.StringPreferenceOrder();
  }

  public static void main(String args [])
  {
    try
    {
      PreferenceOrder < String > TestPO = new PreferenceOrder < String > ();
      // Error with reading in file, path has to be edited manually
      TestPO = parse("test.po");
      System.out.println(TestPO);
      System.out.println(TestPO.getDomainElements());
      TestPO.addPair("g", "h", Relation.LESS);
      System.out.println(TestPO.getDomainElements());
      System.out.println(TestPO);
    }
    catch (Exception e)
    {
      System.out.println("error while parsing: " + e);
    }
  }

  final public PreferenceOrder < String > StringPreferenceOrder() throws ParseException {
  Token t;
  Token t0, t1, t2;
  Set < String > singleElements = new HashSet < String > ();
  Set < Triple < String, String, Relation > > entries = new HashSet < Triple < String, String, Relation > > ();
    jj_consume_token(LBRA);
    label_1:
    while (true) {
      t = jj_consume_token(ELEMENT);
        singleElements.add(t.image);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 7:
        jj_consume_token(7);
        break;
      default:
        jj_la1[0] = jj_gen;
        ;
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ELEMENT:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_1;
      }
    }
    jj_consume_token(RBRA);
    jj_consume_token(EOL);
    label_2:
    while (true) {
      t0 = jj_consume_token(ELEMENT);
      t2 = jj_consume_token(REL);
      t1 = jj_consume_token(ELEMENT);
      if (t2.image.equals("<="))
      {
        Triple < String, String, Relation > ent = new Triple < String, String, Relation > (t0.image, t1.image, Relation.LESS_EQUAL);
                entries.add(ent);
      }
      else if (t2.image.equals("<"))
      {
        Triple < String, String, Relation > ent = new Triple < String, String, Relation > (t0.image, t1.image, Relation.LESS);
                entries.add(ent);
      } else
      {
        continue;
      }
      jj_consume_token(EOL);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ELEMENT:
        ;
        break;
      default:
        jj_la1[2] = jj_gen;
        break label_2;
      }
    }
    PreferenceOrder < String > n = new PreferenceOrder < String > (entries);
    {if (true) return n;}
    throw new Error("Missing return statement in function");
  }

  /** Generated Token Manager. */
  public POParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  /** Current token. */
  public Token token;
  /** Next token. */
  public Token jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[3];
  static private int[] jj_la1_0;
  static {
      jj_la1_init_0();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x80,0x10,0x10,};
   }

  /** Constructor with InputStream. */
  public POParser(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public POParser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new POParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 3; i++) jj_la1[i] = -1;
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
    for (int i = 0; i < 3; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public POParser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new POParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 3; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 3; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public POParser(POParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 3; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(POParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 3; i++) jj_la1[i] = -1;
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
    boolean[] la1tokens = new boolean[8];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 3; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 8; i++) {
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
