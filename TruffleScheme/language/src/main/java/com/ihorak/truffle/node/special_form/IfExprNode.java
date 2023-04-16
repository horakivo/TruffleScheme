package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.cast.BooleanCastNode;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.ConditionProfile;

public abstract class IfExprNode extends SchemeExpression {

    @Child private SchemeExpression condition;
    @Child private SchemeExpression thenExpr;


    public IfExprNode(SchemeExpression condition, SchemeExpression thenExpr) {
        this.condition = condition;
        this.thenExpr = thenExpr;
    }

    @Specialization
    protected Object doIf(VirtualFrame frame,
                          @Cached BooleanCastNode booleanCastNode,
                          @Cached("createCountingProfile()") ConditionProfile conditionProfile) {
        if (conditionProfile.profile(booleanCastNode.executeBoolean(condition.executeGeneric(frame)))) {
            return thenExpr.executeGeneric(frame);
        } else {
            return UndefinedValue.SINGLETON;
        }
    }
}
