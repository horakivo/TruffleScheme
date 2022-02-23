package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;

public class IfExprNode extends SchemeExpression {

    @Child
    private SchemeExpression testExpr;
    @Child
    private SchemeExpression thenExpr;
    @Child
    private SchemeExpression elseExpr;

    public IfExprNode(SchemeExpression testExpr, SchemeExpression thenExpr, SchemeExpression elseExpr) {
        this.testExpr = testExpr;
        this.thenExpr = thenExpr;
        this.elseExpr = elseExpr;
    }

    //TODO do some research about this ConditionProfile
    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        if (evaluateCondition(virtualFrame)) {
            return thenExpr.executeGeneric(virtualFrame);
        } else if (elseExpr != null) {
            return elseExpr.executeGeneric(virtualFrame);
        }

        //TODO represent correct <void>
        return null;
    }

    private boolean evaluateCondition(VirtualFrame virtualFrame) {
        Object result = testExpr.executeGeneric(virtualFrame);
        //TODO is here nil value? Everything except #f is true
        return !(result instanceof Boolean) || (boolean) result;
    }
}
