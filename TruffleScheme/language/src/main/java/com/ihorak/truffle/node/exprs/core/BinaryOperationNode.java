package com.ihorak.truffle.node.exprs.core;

import com.ihorak.truffle.node.SchemeNode;

public abstract class BinaryOperationNode extends SchemeNode {

    public abstract Object execute(Object left, Object right);
    public abstract boolean executeBoolean(Object left, Object right);
}
