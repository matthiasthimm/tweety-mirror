/* Generated By:JJTree: Do not edit this line. ASTListTerm.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package net.sf.tweety.logicprogramming.asplibrary.parser;

public
class ASTListTerm extends SimpleNode {
	public ASTListTerm(int id) {
    super(id);
  }

  public ASTListTerm(ASPParser p, int id) {
    super(p, id);
  }

  /** Accept the visitor. **/
  public Object jjtAccept(ASPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=ca382fedd441b39657b4f9e59f3f9843 (do not edit this line) */