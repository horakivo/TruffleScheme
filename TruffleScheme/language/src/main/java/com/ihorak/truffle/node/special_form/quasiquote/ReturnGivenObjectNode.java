package com.ihorak.truffle.node.special_form.quasiquote;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;

public class ReturnGivenObjectNode extends SchemeExpression {

    private final Object object;

    public ReturnGivenObjectNode(Object object) {
        this.object = object;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return object;
    }
}
