package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;

public class ReadProcedureArgExprNode extends SchemeExpression {

    private final int index;

    public ReadProcedureArgExprNode(int index) {
        this.index = index;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        //TODO offset by one because first one is global env?
        return virtualFrame.getArguments()[index + 1];
    }
}
