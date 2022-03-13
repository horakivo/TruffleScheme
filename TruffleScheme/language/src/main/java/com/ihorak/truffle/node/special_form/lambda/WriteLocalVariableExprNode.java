package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChild(value = "valueToStore")
public abstract class WriteLocalVariableExprNode extends SchemeExpression {

    private final int frameIndex;
    private final SchemeSymbol symbol;

    public WriteLocalVariableExprNode(int frameIndex, SchemeSymbol symbol) {
        this.frameIndex = frameIndex;
        this.symbol = symbol;
    }

    @Specialization(guards = "isLongOrIllegal(frame)")
    protected UndefinedValue writeLong(VirtualFrame frame, long value) {
        /* No-on if already long */
        frame.getFrameDescriptor().setSlotKind(frameIndex, FrameSlotKind.Long);
        frame.setLong(frameIndex, value);
        return UndefinedValue.SINGLETON;
    }

    @Specialization(guards = "isBooleanOrIllegal(frame)")
    protected Object writeBoolean(VirtualFrame frame, boolean value) {
        /* No-on if already boolean */
        frame.getFrameDescriptor().setSlotKind(frameIndex, FrameSlotKind.Boolean);
        frame.setBoolean(frameIndex, value);
        return UndefinedValue.SINGLETON;
    }

    @Specialization(guards = "isDoubleOrIllegal(frame)")
    protected Object writeDouble(VirtualFrame frame, double value) {
        /* No-on if already double */
        frame.getFrameDescriptor().setSlotKind(frameIndex, FrameSlotKind.Double);
        frame.setDouble(frameIndex, value);
        return UndefinedValue.SINGLETON;
    }


    /**
     * Generic write method that works for all possible types.
     * <p>
     * Why is this method annotated with {@link Specialization} and not {@link Fallback}? For a
     * {@link Fallback} method, the Truffle DSL generated code would try all other specializations
     * first before calling this method. We know that all these specializations would fail their
     * guards, so there is no point in calling them. Since this method takes a value of type
     * {@link Object}, it is guaranteed to never fail, i.e., once we are in this specialization the
     * node will never be re-specialized.
     */
    @Specialization(replaces = {"writeLong", "writeBoolean", "writeDouble"})
    protected Object write(VirtualFrame frame, Object value) {
        /*
         * Regardless of the type before, the new and final type of the local variable is Object.
         * Changing the slot kind also discards compiled code, because the variable type is
         * important when the compiler optimizes a method.
         *
         * No-op if kind is already Object.
         */
        frame.getFrameDescriptor().setSlotKind(frameIndex, FrameSlotKind.Object);
        frame.setObject(frameIndex, value);
        return UndefinedValue.SINGLETON;
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
