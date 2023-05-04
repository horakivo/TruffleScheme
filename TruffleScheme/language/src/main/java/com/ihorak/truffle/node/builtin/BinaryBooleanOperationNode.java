package com.ihorak.truffle.node.builtin;

import com.ihorak.truffle.node.SchemeNode;
import com.oracle.truffle.api.dsl.GenerateUncached;

@GenerateUncached(inherit = true)
public abstract class BinaryBooleanOperationNode extends SchemeNode {

    public abstract boolean execute(Object left, Object right);
}
