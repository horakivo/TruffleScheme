package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class WriteLocalVariableAbstractNode extends SchemeExpression {

    public final int frameIndex;

    protected WriteLocalVariableAbstractNode(int frameIndex) {
        this.frameIndex = frameIndex;
    }


    protected void storeLong(VirtualFrame frame, long value) {
        frame.getFrameDescriptor().setSlotKind(frameIndex, FrameSlotKind.Long);
        frame.setLong(frameIndex, value);
    }

    protected void storeBoolean(VirtualFrame frame, boolean value) {
        frame.getFrameDescriptor().setSlotKind(frameIndex, FrameSlotKind.Boolean);
        frame.setBoolean(frameIndex, value);
    }

    protected void storeDouble(VirtualFrame frame, double value) {
        frame.getFrameDescriptor().setSlotKind(frameIndex, FrameSlotKind.Double);
        frame.setDouble(frameIndex, value);
    }

    protected void storeObject(VirtualFrame frame, Object value) {
        frame.getFrameDescriptor().setSlotKind(frameIndex, FrameSlotKind.Object);
        frame.setObject(frameIndex, value);
    }

    protected boolean isLongOrIllegal(VirtualFrame frame) {
        final FrameSlotKind kind = frame.getFrameDescriptor().getSlotKind(frameIndex);
        return kind == FrameSlotKind.Long || kind == FrameSlotKind.Illegal;
    }

    protected boolean isBooleanOrIllegal(VirtualFrame frame) {
        final FrameSlotKind kind = frame.getFrameDescriptor().getSlotKind(frameIndex);
        return kind == FrameSlotKind.Boolean || kind == FrameSlotKind.Illegal;
    }

    protected boolean isDoubleOrIllegal(VirtualFrame frame) {
        final FrameSlotKind kind = frame.getFrameDescriptor().getSlotKind(frameIndex);
        return kind == FrameSlotKind.Double || kind == FrameSlotKind.Illegal;
    }
}
