package com.ihorak.truffle.node.special_form.lambda;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChild(value = "valueToStore")
@NodeField(name = "frameIndex", type = int.class)
@NodeField(name = "name", type = SchemeSymbol.class)
public abstract class WriteLocalVariableExprNode extends SchemeExpression {

    protected abstract int getFrameIndex();

    protected abstract SchemeSymbol getName();

    @Specialization
    protected Object writeLong(VirtualFrame frame, long value) {
        frame.getFrameDescriptor().setSlotKind(getFrameIndex(), FrameSlotKind.Long);
        frame.setLong(getFrameIndex(), value);
        return null;
    }

    @Specialization
    protected Object writeBoolean(VirtualFrame frame, boolean value) {
        frame.getFrameDescriptor().setSlotKind(getFrameIndex(), FrameSlotKind.Boolean);
        frame.setBoolean(getFrameIndex(), value);
        return null;
    }

    @Specialization
    protected Object writeDouble(VirtualFrame frame, double value) {
        frame.getFrameDescriptor().setSlotKind(getFrameIndex(), FrameSlotKind.Double);
        frame.setDouble(getFrameIndex(), value);
        return null;
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
        frame.getFrameDescriptor().setSlotKind(getFrameIndex(), FrameSlotKind.Object);
        frame.setObject(getFrameIndex(), value);
        return null;
    }
}
