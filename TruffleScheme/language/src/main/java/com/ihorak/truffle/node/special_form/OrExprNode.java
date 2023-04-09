package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.cast.BooleanCastExprNode;
import com.ihorak.truffle.node.cast.BooleanCastExprNodeGen;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.ConditionProfile;


/**
 * Scheme is using Short-Circuit evaluation.
 * Short-circuit evaluation is the semantics of some boolean operators where the right argument is executed only
 * if first one is not sufficient to decide the result
 */
public class OrExprNode extends SchemeExpression {

    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private SchemeExpression left;
    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private SchemeExpression right;

    @Child
    private BooleanCastExprNode leftCast;
    private final ConditionProfile evaluateRightProfile = ConditionProfile.createCountingProfile();


    public OrExprNode(SchemeExpression left, SchemeExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        var leftValue = left.executeGeneric(frame);
        if (evaluateRightProfile.profile(castToBoolean(leftValue))) {
            return leftValue;
        } else {
            return right.executeGeneric(frame);

        }
    }

    private boolean castToBoolean(Object value) {
        if (leftCast == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            leftCast = insert(BooleanCastExprNodeGen.create(null));
        }

        return leftCast.executeBoolean(value);
    }
}
