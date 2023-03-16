package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeNode;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;

@ImportStatic(FrameSlotKind.class)
public abstract class WriteFrameSlotNode extends SchemeNode {
    @CompilerDirectives.CompilationFinal private FrameDescriptor cachedDescriptor;
    private final int frameSlot;

    public abstract void executeWrite(Frame frame, Object value);

    public WriteFrameSlotNode(int frameSlot) {
        assert frameSlot >= 0;
        this.frameSlot = frameSlot;
    }

//    @Specialization(guards = "isExpectedOrIllegal(frame, Long)")
//    protected void writeLong(Frame frame, long value) {
//        frame.setLong(frameSlot, value);
//    }
//    @Specialization(guards = "isExpectedOrIllegal(frame, Boolean)")
//    protected void writeBoolean(Frame frame, boolean value) {
//        frame.setBoolean(frameSlot, value);
//    }
//
//    @Specialization(guards = "isExpectedOrIllegal(frame, Double)")
//    protected void writeDouble(Frame frame, double value) {
//        frame.setDouble(frameSlot, value);
//    }

    //@Specialization(replaces = { "writeBoolean", "writeLong", "writeDouble" })
    @Specialization
    protected void writeObject(Frame frame, Object value) {
        /* No-op if kind is already Object. */
        final FrameDescriptor descriptor = getFrameDescriptor(frame);
        descriptor.setSlotKind(frameSlot, FrameSlotKind.Object);

        frame.setObject(frameSlot, value);
    }

    protected boolean isExpectedOrIllegal(Frame frame, FrameSlotKind expectedKind) {
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

    private FrameDescriptor getFrameDescriptor(Frame frame) {
        if (cachedDescriptor == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            cachedDescriptor = frame.getFrameDescriptor();
        }

        assert frame.getFrameDescriptor() == cachedDescriptor;
        return cachedDescriptor;
    }
}
