package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.*;


public abstract class ReadLocalVariableExprNode extends SchemeExpression {

    protected final int frameSlotIndex;
    private final SchemeSymbol symbol;

    public ReadLocalVariableExprNode(int frameSlotIndex, SchemeSymbol symbol) {
        this.frameSlotIndex = frameSlotIndex;
        this.symbol = symbol;
    }

    @Specialization(guards = "frame.isLong(frameSlotIndex)")
    protected long readLong(VirtualFrame frame) {
        return frame.getLong(frameSlotIndex);
    }

    @Specialization(guards = "frame.isBoolean(frameSlotIndex)")
    protected boolean readBoolean(VirtualFrame frame) {
        return frame.getBoolean(frameSlotIndex);
    }

    @Specialization(guards = "frame.isDouble(frameSlotIndex)")
    protected double readDouble(VirtualFrame frame) {
        return frame.getDouble(frameSlotIndex);
    }

    @Specialization(replaces = {"readLong", "readBoolean", "readDouble"}, guards = "frame.isObject(frameSlotIndex)")
    protected Object read(VirtualFrame frame) {
        return frame.getObject(frameSlotIndex);
    }

    @Fallback
    protected Object fallback(VirtualFrame frame) {
        throw new SchemeException(symbol + ": undefined\ncannot reference an identifier before its definition. FrameSlotKind: " + frame.getFrameDescriptor().getSlotKind(frameSlotIndex), this);
    }
}
