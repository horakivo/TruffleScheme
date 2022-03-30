package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.ConditionUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.ConditionProfile;

public class IfExprNode extends SchemeExpression {

    @Child private SchemeExpression conditionNode;
    @Child private SchemeExpression thenExpr;
    @Child private SchemeExpression elseExpr;

    private final ConditionProfile conditionProfile = ConditionProfile.createCountingProfile();

    public IfExprNode(SchemeExpression conditionNode, SchemeExpression thenExpr, SchemeExpression elseExpr) {
        this.conditionNode = conditionNode;
        this.thenExpr = thenExpr;
        this.elseExpr = elseExpr;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        var conditionResult = conditionNode.executeGeneric(frame);
        if (conditionProfile.profile(ConditionUtil.convertObjectToBoolean(conditionResult))) {
            return thenExpr.executeGeneric(frame);
        } else {
            if (elseExpr != null) {
                return elseExpr.executeGeneric(frame);
            }
            return UndefinedValue.SINGLETON;
        }
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
