/* Generated By:JJTree: Do not edit this line. ASTElementLst.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package net.sf.tweety.logicprogramming.asplibrary.parser;

public
class ASTElementLst extends SimpleNode {
  public ASTElementLst(int id) {
    super(id);
  }

  public ASTElementLst(ASPParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ASPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=e3d518956ffe81d39c84d355fb8fea7f (do not edit this line) */
