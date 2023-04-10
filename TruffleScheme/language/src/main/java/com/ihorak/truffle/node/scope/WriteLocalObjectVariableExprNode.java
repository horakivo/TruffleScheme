package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChild("value")
public abstract class WriteLocalObjectVariableExprNode extends SchemeExpression {

    @CompilerDirectives.CompilationFinal
    private FrameDescriptor cachedDescriptor;

    protected final int frameSlot;

    public WriteLocalObjectVariableExprNode(int frameSlot) {
        this.frameSlot = frameSlot;
    }

    @Specialization
    protected Object doObject(VirtualFrame frame, Object value) {
        final FrameDescriptor descriptor = getFrameDescriptor(frame);
        descriptor.setSlotKind(frameSlot, FrameSlotKind.Object);

        frame.setObject(frameSlot, value);
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
