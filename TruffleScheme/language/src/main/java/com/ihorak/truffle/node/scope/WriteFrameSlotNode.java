package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeNode;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

@ImportStatic(FrameSlotKind.class)
public abstract class WriteFrameSlotNode extends SchemeNode {

    @CompilationFinal
    private FrameDescriptor cachedDescriptor;

    private final int frameSlot;

    public abstract void executeWrite(VirtualFrame frame, Object value);

    public WriteFrameSlotNode(int frameSlot) {
        assert frameSlot >= 0;
        this.frameSlot = frameSlot;
    }

    @Specialization(guards = "isExpectedOrIllegal(frame, Long)")
    protected void writeLong(VirtualFrame frame, long value) {
        frame.setLong(frameSlot, value);
    }
    @Specialization(guards = "isExpectedOrIllegal(frame, Boolean)")
    protected void writeBoolean(VirtualFrame frame, boolean value) {
        frame.setBoolean(frameSlot, value);
    }

    @Specialization(guards = "isExpectedOrIllegal(frame, Double)")
    protected void writeDouble(VirtualFrame frame, double value) {
        frame.setDouble(frameSlot, value);
    }

    @Specialization(replaces = { "writeBoolean", "writeLong", "writeDouble" })
    protected void writeObject(VirtualFrame frame, Object value) {
        /* No-op if kind is already Object. */
        final FrameDescriptor descriptor = getFrameDescriptor(frame);
        descriptor.setSlotKind(frameSlot, FrameSlotKind.Object);

        frame.setObject(frameSlot, value);
    }

    protected boolean isExpectedOrIllegal(VirtualFrame frame, FrameSlotKind expectedKind) {
        final FrameDescriptor descriptor = getFrameDescriptor(frame);

        final FrameSlotKind kind = descriptor.getSlotKind(frameSlot);
        if (kind == expectedKind) {
            return true;
        } else if (kind == FrameSlotKind.Illegal) {
            descriptor.setSlotKind(frameSlot, expectedKind);
            return true;
        }
        return false;
    }

    private FrameDescriptor getFrameDescriptor(VirtualFrame frame) {
        if (cachedDescriptor == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            cachedDescriptor = frame.getFrameDescriptor();
        }

        return cachedDescriptor;
    }
}
