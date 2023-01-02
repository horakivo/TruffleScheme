package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.cast.BooleanCastExprNode;
import com.ihorak.truffle.type.SchemeSymbol;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.ConditionProfile;

import java.util.List;

public class IfExprNode extends SchemeExpression {

    @Child private BooleanCastExprNode condition;
    @Child private SchemeExpression thenExpr;

    private final ConditionProfile conditionProfile = ConditionProfile.createCountingProfile();

    public IfExprNode(BooleanCastExprNode condition, SchemeExpression thenExpr) {
        this.condition = condition;
        this.thenExpr = thenExpr;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        if (conditionProfile.profile(condition.executeBoolean(frame))) {
            return thenExpr.executeGeneric(frame);
        } else {
            return UndefinedValue.SINGLETON;
        }
    }
}
