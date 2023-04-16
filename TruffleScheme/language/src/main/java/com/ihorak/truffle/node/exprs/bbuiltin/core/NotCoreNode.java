package com.ihorak.truffle.node.exprs.bbuiltin.core;

import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.cast.BooleanCastNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class NotCoreNode extends SchemeNode {

    public abstract boolean execute(Object object);

    @Specialization
    protected boolean doNull(Object object, @Cached BooleanCastNode booleanCastNode) {
        return !booleanCastNode.executeBoolean(object);
    }
}
