package com.ihorak.truffle.node.exprs.core;

import com.ihorak.truffle.node.SchemeNode;

public abstract class BinaryBooleanOperationNode extends SchemeNode {
    public abstract boolean execute(Object left, Object right);
}
