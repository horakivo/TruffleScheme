package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

import static com.ihorak.truffle.node.special_form.lambda.FrameUtil.findAuxiliaryValue;
import static com.ihorak.truffle.node.special_form.lambda.FrameUtil.findGlobalEnv;


public abstract class ReadGlobalVariableExprNode extends SchemeExpression {

    private final SchemeSymbol symbol;
    private final int frameSlotIndex;

    public ReadGlobalVariableExprNode(SchemeSymbol symbol, int frameSlotIndex) {
        this.symbol = symbol;
        this.frameSlotIndex = frameSlotIndex;
    }


    @Specialization(guards = "isLong(frame)")
    protected long readLong(VirtualFrame frame) {
        return findGlobalEnv(frame).getLong(frameSlotIndex);
    }

    @Specialization(guards = "isBoolean(frame)")
    protected boolean readBoolean(VirtualFrame frame) {
        return findGlobalEnv(frame).getBoolean(frameSlotIndex);
    }

    @Specialization(guards = "isDouble(frame)")
    protected double readDouble(VirtualFrame frame) {
        return findGlobalEnv(frame).getDouble(frameSlotIndex);
    }

    @Specialization(guards = "isObject(frame)", replaces = {"readLong", "readBoolean", "readDouble"})
    protected Object read(VirtualFrame frame) {
        return findGlobalEnv(frame).getObject(frameSlotIndex);
    }

    @Specialization(replaces = "read")
    protected Object readRuntimeVariable(VirtualFrame frame) {
        return findAuxiliaryValue(frame, symbol);
    }

//    @Fallback
//    protected void fallback(VirtualFrame frame) {
//        throw new SchemeException(getSymbol() + ": undefined\ncannot reference an identifier before its definition. FrameSlotKind: " + findGlobalEnv(frame).getFrameDescriptor().getSlotKind(getFrameSlotIndex()));
//    }


    public boolean isLong(VirtualFrame frame) {
        return findGlobalEnv(frame).getFrameDescriptor().getSlotKind(frameSlotIndex) == FrameSlotKind.Long;
    }

    public boolean isBoolean(VirtualFrame frame) {
        return findGlobalEnv(frame).getFrameDescriptor().getSlotKind(frameSlotIndex) == FrameSlotKind.Boolean;
    }

    public boolean isDouble(VirtualFrame frame) {
        return findGlobalEnv(frame).getFrameDescriptor().getSlotKind(frameSlotIndex) == FrameSlotKind.Double;
    }

    public boolean isObject(VirtualFrame frame) {
        return findGlobalEnv(frame).getFrameDescriptor().getSlotKind(frameSlotIndex) == FrameSlotKind.Object;
    }

}
