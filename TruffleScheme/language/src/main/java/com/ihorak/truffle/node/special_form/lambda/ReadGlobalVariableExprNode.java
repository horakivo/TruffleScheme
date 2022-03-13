package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;

import static com.ihorak.truffle.node.special_form.lambda.FrameUtil.findAuxiliaryValue;


public abstract class ReadGlobalVariableExprNode extends SchemeExpression {

    private final SchemeSymbol symbol;
    private final int frameSlotIndex;
    @CompilationFinal
    private FrameDescriptor frameDescriptor;

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


    public boolean isLong(VirtualFrame frame) {
        return findGlobalFrameDescriptor(frame).getSlotKind(frameSlotIndex) == FrameSlotKind.Long;
    }

    public boolean isBoolean(VirtualFrame frame) {
        return findGlobalFrameDescriptor(frame).getSlotKind(frameSlotIndex) == FrameSlotKind.Boolean;
    }

    public boolean isDouble(VirtualFrame frame) {
        return findGlobalFrameDescriptor(frame).getSlotKind(frameSlotIndex) == FrameSlotKind.Double;
    }

    public boolean isObject(VirtualFrame frame) {
        return findGlobalFrameDescriptor(frame).getSlotKind(frameSlotIndex) == FrameSlotKind.Object;
    }

    public VirtualFrame findGlobalEnv(VirtualFrame frame) {
        VirtualFrame currentFrame = frame;

        while (currentFrame.getArguments().length != 0) {
            currentFrame = (VirtualFrame) currentFrame.getArguments()[0];
        }

        return currentFrame;
    }

    public FrameDescriptor findGlobalFrameDescriptor(VirtualFrame frame) {
        if (frameDescriptor == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            this.frameDescriptor = findGlobalEnv(frame).getFrameDescriptor();
        }
        return frameDescriptor;
    }


}
