package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeFunction;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class WriteBuiltinProcedureExprNode extends SchemeExpression {

    private final SchemeFunction procedure;
    private final int frameIndex;
    private final SchemeSymbol symbol;

    public WriteBuiltinProcedureExprNode(SchemeFunction procedure, int frameIndex, SchemeSymbol symbol) {
        this.procedure = procedure;
        this.frameIndex = frameIndex;
        this.symbol = symbol;
    }


    @Specialization
    protected Object writeFunction(VirtualFrame frame) {
        frame.getFrameDescriptor().setSlotKind(frameIndex, FrameSlotKind.Object);
        frame.setObject(frameIndex, procedure);

        return null;
    }
}
