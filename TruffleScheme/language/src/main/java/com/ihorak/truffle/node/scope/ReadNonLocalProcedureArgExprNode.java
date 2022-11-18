package com.ihorak.truffle.node.scope;

import com.oracle.truffle.api.frame.VirtualFrame;

public class ReadNonLocalProcedureArgExprNode extends ContextualNode {


    private final int index;

    public ReadNonLocalProcedureArgExprNode(final int index, final int lexicalScopeDepth) {
        super(lexicalScopeDepth);
        this.index = index;
    }

    @Override
    public Object executeGeneric(final VirtualFrame virtualFrame) {
        return findCorrectVirtualFrame(virtualFrame).getArguments()[index + 1];
    }

}
