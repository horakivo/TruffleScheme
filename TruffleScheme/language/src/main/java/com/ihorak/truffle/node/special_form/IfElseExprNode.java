package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.cast.BooleanCastNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.ConditionProfile;

public abstract class IfElseExprNode extends SchemeExpression {


    @Child public SchemeExpression condition;
    @Child public SchemeExpression thenExpr;
    @Child public SchemeExpression elseExpr;

    public IfElseExprNode(final SchemeExpression condition, final SchemeExpression thenExpr, final SchemeExpression elseExpr) {
        this.condition = condition;
        this.thenExpr = thenExpr;
        this.elseExpr = elseExpr;
    }

    @Specialization
    protected Object doIfElse(VirtualFrame frame,
                              @Cached BooleanCastNode booleanCastNode,
                              @Cached("createCountingProfile()") ConditionProfile conditionProfile) {
        if (conditionProfile.profile(booleanCastNode.executeBoolean(condition.executeGeneric(frame)))) {
            return thenExpr.executeGeneric(frame);
        } else {
            return elseExpr.executeGeneric(frame);
        }

    }
}
