package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.node.SchemeNode;

import java.math.BigInteger;

public abstract class BinaryOperationNode extends SchemeNode {

    public abstract Object execute(Object left, Object right);
}
