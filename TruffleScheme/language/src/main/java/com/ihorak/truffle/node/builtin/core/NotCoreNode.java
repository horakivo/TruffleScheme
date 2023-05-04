package com.ihorak.truffle.node.builtin.core;

import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.BooleanCastNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;

@GenerateUncached
public abstract class NotCoreNode extends SchemeNode {

    public abstract boolean execute(Object object);

    @Specialization
    protected boolean doNull(Object object, @Cached BooleanCastNode booleanCastNode) {
        return !booleanCastNode.executeBoolean(object);
    }
}
