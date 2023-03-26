package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeNode;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class StoreTailCallResultInFrameNode extends SchemeNode {

    @CompilationFinal
    private FrameDescriptor cachedDescriptor;
    private final int frameSlot;

    public abstract void execute(VirtualFrame frame, Object objectToStore);

    public StoreTailCallResultInFrameNode(int frameSlot) {
        this.frameSlot = frameSlot;
    }

    @Specialization
    protected void store(VirtualFrame frame, Object objectToStore) {
        final FrameDescriptor descriptor = getFrameDescriptor(frame);
        descriptor.setSlotKind(frameSlot, FrameSlotKind.Object);

        frame.setObject(frameSlot, objectToStore);
    }


    private FrameDescriptor getFrameDescriptor(VirtualFrame frame) {
        if (cachedDescriptor == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            cachedDescriptor = frame.getFrameDescriptor();
        }

        return cachedDescriptor;
    }
}
