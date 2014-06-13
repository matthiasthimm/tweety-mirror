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
/* Generated By:JJTree&JavaCC: Do not edit this line. ASPParserConstants.java */
package net.sf.tweety.lp.asp.parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface ASPParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int IMPL = 5;
  /** RegularExpression Id. */
  int NEG = 6;
  /** RegularExpression Id. */
  int SPECIAL_PREFIX = 7;
  /** RegularExpression Id. */
  int DEFNOT = 8;
  /** RegularExpression Id. */
  int DOT = 9;
  /** RegularExpression Id. */
  int COMMA = 10;
  /** RegularExpression Id. */
  int PAR_OPEN = 11;
  /** RegularExpression Id. */
  int PAR_CLOSE = 12;
  /** RegularExpression Id. */
  int CPAR_OPEN = 13;
  /** RegularExpression Id. */
  int CPAR_CLOSE = 14;
  /** RegularExpression Id. */
  int PIPE = 15;
  /** RegularExpression Id. */
  int ZPAR_OPEN = 16;
  /** RegularExpression Id. */
  int ZPAR_CLOSE = 17;
  /** RegularExpression Id. */
  int OR = 18;
  /** RegularExpression Id. */
  int PLUS = 19;
  /** RegularExpression Id. */
  int MUL = 20;
  /** RegularExpression Id. */
  int DIV = 21;
  /** RegularExpression Id. */
  int LESS = 22;
  /** RegularExpression Id. */
  int LESSEQ = 23;
  /** RegularExpression Id. */
  int GREATER = 24;
  /** RegularExpression Id. */
  int GREATEREQ = 25;
  /** RegularExpression Id. */
  int EQUAL = 26;
  /** RegularExpression Id. */
  int EQUAL_OLD = 27;
  /** RegularExpression Id. */
  int NOTEQUAL = 28;
  /** RegularExpression Id. */
  int NUMBER = 29;
  /** RegularExpression Id. */
  int CONSTANT = 30;
  /** RegularExpression Id. */
  int VARIABLE = 31;
  /** RegularExpression Id. */
  int COMMENT = 32;
  /** RegularExpression Id. */
  int LETTER = 33;
  /** RegularExpression Id. */
  int DIGIT = 34;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\r\"",
    "\"\\n\"",
    "\":-\"",
    "\"-\"",
    "\"#\"",
    "\"not\"",
    "\".\"",
    "\",\"",
    "\"(\"",
    "\")\"",
    "\"[\"",
    "\"]\"",
    "\"|\"",
    "\"{\"",
    "\"}\"",
    "\";\"",
    "\"+\"",
    "\"*\"",
    "\"/\"",
    "\"<\"",
    "\"<=\"",
    "\">\"",
    "\">=\"",
    "\"=\"",
    "\"==\"",
    "\"!=\"",
    "<NUMBER>",
    "<CONSTANT>",
    "<VARIABLE>",
    "<COMMENT>",
    "<LETTER>",
    "<DIGIT>",
    "\":\"",
  };

}