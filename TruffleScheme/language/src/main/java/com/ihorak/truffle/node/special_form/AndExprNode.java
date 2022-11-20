package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.ConditionUtil;
import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.profiles.ConditionProfile;


/**
 * Scheme is using Short-Circuit evaluation.
 * Short-circuit evaluation is the semantics of some boolean operators where the right argument is executed only
 * if first one is not sufficient to decide the result
 */
public class AndExprNode extends SchemeExpression {

    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private SchemeExpression left;
    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private SchemeExpression right;
    private final ConditionProfile evaluateRightProfile = ConditionProfile.createCountingProfile();


    public AndExprNode(SchemeExpression left, SchemeExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        var leftValue = left.executeGeneric(frame);
        if (evaluateRightProfile.profile(isEvaluateRight(leftValue))) {
            return right.executeGeneric(frame);
        }

        return false;
    }

    private boolean isEvaluateRight(Object obj) {
        return ConditionUtil.convertObjectToBoolean(obj);
    }

//    @Override
//    public void setTailRecursiveAsTrue() {
//        super.setTailRecursiveAsTrue();
//        right.setTailRecursiveAsTrue();
//    }
}
