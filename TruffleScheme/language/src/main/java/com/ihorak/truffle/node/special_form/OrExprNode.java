package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.cast.BooleanCastNode;
import com.ihorak.truffle.node.cast.BooleanCastNodeGen;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.ConditionProfile;


/**
 * Scheme is using Short-Circuit evaluation.
 * Short-circuit evaluation is the semantics of some boolean operators where the right argument is executed only
 * if first one is not sufficient to decide the result
 */
public abstract class OrExprNode extends SchemeExpression {

    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private SchemeExpression left;
    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private SchemeExpression right;


    public OrExprNode(SchemeExpression left, SchemeExpression right) {
        this.left = left;
        this.right = right;
    }

    @Specialization
    protected Object doOr(
            VirtualFrame frame,
            @Cached BooleanCastNode booleanCastNode,
            @Cached("createCountingProfile()") ConditionProfile evaluateLeftProfile) {
        var leftResult = left.executeGeneric(frame);
        var leftResultBool = booleanCastNode.executeBoolean(leftResult);

        if (evaluateLeftProfile.profile(leftResultBool)) {
            return leftResult;
        } else {
            return right.executeGeneric(frame);
        }
    }
}
