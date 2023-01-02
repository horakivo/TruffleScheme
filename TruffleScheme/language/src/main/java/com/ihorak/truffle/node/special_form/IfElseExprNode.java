package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.cast.BooleanCastExprNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.ConditionProfile;

public class IfElseExprNode extends SchemeExpression {


    @Child private BooleanCastExprNode condition;
    @Child private SchemeExpression thenExpr;
    @Child private SchemeExpression elseExpr;

    public IfElseExprNode(final BooleanCastExprNode condition, final SchemeExpression thenExpr, final SchemeExpression elseExpr) {
        this.condition = condition;
        this.thenExpr = thenExpr;
        this.elseExpr = elseExpr;
    }

    private final ConditionProfile conditionProfile = ConditionProfile.createCountingProfile();

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        if (conditionProfile.profile(condition.executeBoolean(frame))) {
            return thenExpr.executeGeneric(frame);
        } else {
            return elseExpr.executeGeneric(frame);
        }
    }

}
