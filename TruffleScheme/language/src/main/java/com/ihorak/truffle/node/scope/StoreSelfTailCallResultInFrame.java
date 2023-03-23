package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;


public class StoreSelfTailCallResultInFrame extends SchemeExpression {

    @Child
    private SchemeExpression valueToStore;
    private final int frameSlot;

    @CompilationFinal
    private FrameDescriptor cachedDescriptor;

    public StoreSelfTailCallResultInFrame(SchemeExpression valueToStore, int frameSlotIndex) {
        this.valueToStore = valueToStore;
        this.frameSlot = frameSlotIndex;
    }

    @Override
    public Object executeGeneric(VirtualFrame frame) {
        final FrameDescriptor descriptor = getFrameDescriptor(frame);
        descriptor.setSlotKind(frameSlot, FrameSlotKind.Object);

        frame.setObject(frameSlot, valueToStore.executeGeneric(frame));
        return UndefinedValue.SINGLETON;
    }

    private FrameDescriptor getFrameDescriptor(VirtualFrame frame) {
        if (cachedDescriptor == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            cachedDescriptor = frame.getFrameDescriptor();
        }

        return cachedDescriptor;
    }
}
