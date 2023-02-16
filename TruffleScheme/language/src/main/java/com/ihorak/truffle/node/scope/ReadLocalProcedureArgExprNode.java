package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;

public class ReadLocalProcedureArgExprNode extends SchemeExpression {

    private final int index;

    public ReadLocalProcedureArgExprNode(int index) {
        this.index = index;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return virtualFrame.getArguments()[index + 3];
    }
}
