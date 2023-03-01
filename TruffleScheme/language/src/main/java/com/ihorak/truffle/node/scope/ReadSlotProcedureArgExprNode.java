package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;

public class ReadSlotProcedureArgExprNode extends SchemeExpression {

    private final int index;
    private final int argumentSlotIndex;

    public ReadSlotProcedureArgExprNode(int index, int argumentSlotIndex) {
        this.index = index;
        this.argumentSlotIndex = argumentSlotIndex;
    }


    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        return ((Object[]) virtualFrame.getObject(argumentSlotIndex))[index + 1];
    }
}
