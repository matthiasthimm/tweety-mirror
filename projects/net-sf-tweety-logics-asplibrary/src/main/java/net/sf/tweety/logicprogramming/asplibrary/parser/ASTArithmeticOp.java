/* Generated By:JJTree: Do not edit this line. ASTArithmeticOp.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package net.sf.tweety.logicprogramming.asplibrary.parser;

public
class ASTArithmeticOp extends SimpleNode {
  protected String operator;
	
	public ASTArithmeticOp(int id) {
    super(id);
  }

  public ASTArithmeticOp(ASPParser p, int id) {
    super(p, id);
  }

  public void setOperator(String operator) {
	  this.operator = operator;
  }

  /** Accept the visitor. **/
  public Object jjtAccept(ASPParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=431ac1a41f36af36c19f765da6ec1707 (do not edit this line) */
