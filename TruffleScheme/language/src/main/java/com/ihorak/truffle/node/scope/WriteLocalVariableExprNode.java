package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.SchemeSymbol;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.FrameSlotKind;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChild(value = "valueToStore", type = SchemeExpression.class)
public abstract class WriteLocalVariableExprNode extends WriteLocalVariableAbstractNode {

    private final SchemeSymbol symbol;

    public WriteLocalVariableExprNode(int frameIndex, SchemeSymbol symbol) {
        super(frameIndex);
        this.symbol = symbol;
    }

    @Specialization(guards = "isLongOrIllegal(frame)")
    protected UndefinedValue writeLong(VirtualFrame frame, long value) {
        storeLong(frame, value);
        return UndefinedValue.SINGLETON;
    }

    @Specialization(guards = "isBooleanOrIllegal(frame)")
    protected Object writeBoolean(VirtualFrame frame, boolean value) {
        /* No-on if already boolean */
        storeBoolean(frame, value);
        return UndefinedValue.SINGLETON;
    }

    @Specialization(guards = "isDoubleOrIllegal(frame)")
    protected Object writeDouble(VirtualFrame frame, double value) {
        /* No-on if already double */
        storeDouble(frame, value);
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
        storeObject(frame, value);
        return UndefinedValue.SINGLETON;
    }
}
