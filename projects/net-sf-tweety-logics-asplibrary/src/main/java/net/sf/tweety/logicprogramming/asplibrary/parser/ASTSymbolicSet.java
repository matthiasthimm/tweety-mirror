/* Generated By:JJTree: Do not edit this line. ASTSymbolicSet.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package net.sf.tweety.logicprogramming.asplibrary.parser;

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
