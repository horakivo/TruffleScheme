package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.BooleanCastNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.ConditionProfile;


/**
 * Scheme is using Short-Circuit evaluation.
 * Short-circuit evaluation is the semantics of some boolean operators where the right argument is executed only
 * if first one is not sufficient to decide the result
 */
public abstract class AndExprNode extends SchemeExpression {

    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private SchemeExpression left;
    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private SchemeExpression right;


    public AndExprNode(SchemeExpression left, SchemeExpression right) {
        this.left = left;
        this.right = right;
    }

    @Specialization
    protected Object doAnd(
            VirtualFrame frame,
            @Cached("createCountingProfile()") ConditionProfile evaluateRightProfileArgProfile,
            @Cached BooleanCastNode booleanCastNode) {

        boolean leftResult = booleanCastNode.executeBoolean(left.executeGeneric(frame));
        if (evaluateRightProfileArgProfile.profile(leftResult)) {
            return right.executeGeneric(frame);
        } else {
            return false;
        }
    }
}
