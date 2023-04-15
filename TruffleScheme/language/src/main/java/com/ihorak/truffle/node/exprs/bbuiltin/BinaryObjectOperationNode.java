package com.ihorak.truffle.node.exprs.bbuiltin;

import com.ihorak.truffle.node.SchemeNode;

public abstract class BinaryObjectOperationNode extends SchemeNode {

    public abstract Object execute(Object left, Object right);
}
