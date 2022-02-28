package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.ConditionProfile;

public class IfExprNode extends SchemeExpression {

    @Child private SchemeExpression conditionNode;
    @Child private SchemeExpression thenExpr;
    @Child private SchemeExpression elseExpr;

    private final ConditionProfile condition = ConditionProfile.createCountingProfile();

    public IfExprNode(SchemeExpression conditionNode, SchemeExpression thenExpr, SchemeExpression elseExpr) {
        this.conditionNode = conditionNode;
        this.thenExpr = thenExpr;
        this.elseExpr = elseExpr;
    }

    //TODO do some research about this ConditionProfile
    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        if (condition.profile(evaluateCondition(virtualFrame))) {
            return thenExpr.executeGeneric(virtualFrame);
        } else {
            if (elseExpr != null) {
                return elseExpr.executeGeneric(virtualFrame);
            }
            //TODO represent correct <void>
            return null;
        }
    }

    private boolean evaluateCondition(VirtualFrame virtualFrame) {
        Object result = conditionNode.executeGeneric(virtualFrame);
        //TODO is here nil value? Everything except #f is true
        return !(result instanceof Boolean) || (boolean) result;
    }

    @Override
    public void setTailRecursiveAsTrue() {
        super.setTailRecursiveAsTrue();
        thenExpr.setTailRecursiveAsTrue();
        if (elseExpr != null) {
            elseExpr.setTailRecursiveAsTrue();
        }
    }
}
