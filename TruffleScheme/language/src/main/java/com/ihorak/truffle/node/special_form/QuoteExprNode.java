package com.ihorak.truffle.node.special_form;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;

public class QuoteExprNode extends SchemeExpression {

    private final Object datum;

    public QuoteExprNode(Object internalDataRepresentation) {
        this.datum = internalDataRepresentation;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return datum;
    }
}
