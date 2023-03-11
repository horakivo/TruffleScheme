package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.ImportStatic;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;

@ImportStatic(FrameSlotKind.class)
@NodeChild(value = "valueToStore")
public abstract class WriteLocalVariableExprNode extends SchemeExpression {



    @CompilationFinal
    private FrameDescriptor cachedDescriptor;
    public final int frameIndex;

    public WriteLocalVariableExprNode(int frameIndex) {
        this.frameIndex = frameIndex;
    }
//
//    @Specialization(guards = "isExpectedOrIllegal(frame, Long)")
//    protected Object writeLong(VirtualFrame frame, long value) {
//        frame.setLong(frameIndex, value);
//        return UndefinedValue.SINGLETON;
//    }
//
//    @Specialization(guards = "isExpectedOrIllegal(frame, Double)")
//    protected Object writeDouble(VirtualFrame frame, double value) {
//        frame.setDouble(frameIndex, value);
//        return UndefinedValue.SINGLETON;
//    }
//
//    @Specialization(guards = "isExpectedOrIllegal(frame, Boolean)")
//    protected Object writeBoolean(VirtualFrame frame, boolean value) {
//        frame.setBoolean(frameIndex, value);
//        return UndefinedValue.SINGLETON;
//    }

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
    //@Specialization(replaces = { "writeBoolean", "writeLong", "writeDouble" })
    protected Object writeObject(VirtualFrame frame, Object value) {
        /* No-op if kind is already Object. */
        final FrameDescriptor descriptor = getFrameDescriptor(frame);
        descriptor.setSlotKind(frameIndex, FrameSlotKind.Object);

        frame.setObject(frameIndex, value);
        return UndefinedValue.SINGLETON;
    }
    //@Specialization
//    @Specialization(replaces = {"writeLong", "writeBoolean", "writeDouble"})
//    protected Object write(VirtualFrame frame, Object value) {
//        /*
//         * Regardless of the type before, the new and final type of the local variable is Object.
//         * Changing the slot kind also discards compiled code, because the variable type is
//         * important when the compiler optimizes a method.
//         *
//         * No-op if kind is already Object.
//         */
//        storeObject(frame, value);
//        return UndefinedValue.SINGLETON;
//    }


    protected boolean isExpectedOrIllegal(Frame frame, FrameSlotKind expectedKind) {
        final FrameDescriptor descriptor = getFrameDescriptor(frame);

        final FrameSlotKind kind = descriptor.getSlotKind(frameIndex);
        if (kind == expectedKind) {
            return true;
        } else if (kind == FrameSlotKind.Illegal) {
            descriptor.setSlotKind(frameIndex, expectedKind);
            return true;
        }
        return false;
    }

    private FrameDescriptor getFrameDescriptor(Frame frame) {
        if (cachedDescriptor == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            cachedDescriptor = frame.getFrameDescriptor();
        }

        return cachedDescriptor;
    }

}
