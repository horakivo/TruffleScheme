package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.node.SchemeNode;

public abstract class BinaryOperationNode extends SchemeNode {

    public abstract Object execute(Object left, Object right);

    public abstract long executeLong(long left, long right);
}
