/* Generated By:JJTree: Do not edit this line. ASTTermLst.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package net.sf.tweety.lp.asp.parser;

public
class ASTTermLst extends SimpleNode {
  public ASTTermLst(int id) {
    super(id);
  }

  public ASTTermLst(ASPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ASPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=3a54728f14fc871ce8c4fbeb9bfa9d7a (do not edit this line) */
