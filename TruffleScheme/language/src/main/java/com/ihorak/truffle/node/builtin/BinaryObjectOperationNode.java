package com.ihorak.truffle.node.builtin;

import com.ihorak.truffle.node.SchemeNode;
import com.oracle.truffle.api.dsl.GenerateUncached;

@GenerateUncached(inherit = true)
public abstract class BinaryObjectOperationNode extends SchemeNode {

    public abstract Object execute(Object left, Object right);
}
