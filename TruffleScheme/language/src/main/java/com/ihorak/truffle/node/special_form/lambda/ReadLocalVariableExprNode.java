package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;


public abstract class ReadLocalVariableExprNode extends SchemeExpression {

    protected final int frameSlotIndex;
    private final SchemeSymbol symbol;

    public ReadLocalVariableExprNode(int frameSlotIndex, SchemeSymbol symbol) {
        this.frameSlotIndex = frameSlotIndex;
        this.symbol = symbol;
    }

    @Specialization(guards = "isLong(frame)")
    protected long readLong(VirtualFrame frame) {
        return frame.getLong(frameSlotIndex);
    }

    @Specialization(guards = "isBoolean(frame)")
    protected boolean readBoolean(VirtualFrame frame) {
        return frame.getBoolean(frameSlotIndex);
    }

    @Specialization(guards = "isDouble(frame)")
    protected double readDouble(VirtualFrame frame) {
        return frame.getDouble(frameSlotIndex);
    }

    @Specialization(replaces = {"readLong", "readBoolean", "readDouble"}, guards = "isObject(frame)")
    protected Object read(VirtualFrame frame) {
        return frame.getObject(frameSlotIndex);
    }

    @Fallback
    protected Object fallback(VirtualFrame frame) {
        throw new SchemeException(symbol + ": undefined\ncannot reference an identifier before its definition. FrameSlotKind: " + frame.getFrameDescriptor().getSlotKind(frameSlotIndex));
    }


    protected boolean isLong(VirtualFrame frame) {
        return frame.getFrameDescriptor().getSlotKind(frameSlotIndex) == FrameSlotKind.Long;
    }

    protected boolean isBoolean(VirtualFrame frame) {
        return frame.getFrameDescriptor().getSlotKind(frameSlotIndex) == FrameSlotKind.Boolean;
    }

    protected boolean isDouble(VirtualFrame frame) {
        return frame.getFrameDescriptor().getSlotKind(frameSlotIndex) == FrameSlotKind.Double;
    }

    protected boolean isObject(VirtualFrame frame) {
        return frame.getFrameDescriptor().getSlotKind(frameSlotIndex) == FrameSlotKind.Object;
    }


}
