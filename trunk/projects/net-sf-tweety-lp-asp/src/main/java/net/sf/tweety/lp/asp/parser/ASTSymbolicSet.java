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
/* Generated By:JJTree: Do not edit this line. ASTSymbolicSet.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package net.sf.tweety.lp.asp.parser;

import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.logics.commons.syntax.Variable;

public
class ASTSymbolicSet extends SimpleNode {
  protected List<Variable> variables = new LinkedList<Variable>();
	
	public ASTSymbolicSet(int id) {
    super(id);
  }

  public ASTSymbolicSet(ASPParser p, int id) {
    super(p, id);
  }

  public void addVariable(String name) {
	  variables.add(new Variable(name));
  }

  /** Accept the visitor. **/
  public Object jjtAccept(ASPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=86a945d4d6ea5ef6c7472f17ecebb7d1 (do not edit this line) */
