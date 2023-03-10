package com.ihorak.truffle.node.scope;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.type.UndefinedValue;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeChild(value = "valueToStoreAndReturn", type = SchemeExpression.class)
public abstract class StoreAndReturnValueExprNode extends WriteLocalVariableAbstractNode {

    protected StoreAndReturnValueExprNode(int frameIndex) {
        super(frameIndex);
    }

    //TODO maybe consider all types

    @Specialization
    //@Specialization(replaces = {"writeLong", "writeBoolean", "writeDouble"})
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
