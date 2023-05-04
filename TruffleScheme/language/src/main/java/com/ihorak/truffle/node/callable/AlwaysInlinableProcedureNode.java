package com.ihorak.truffle.node.callable;

import com.ihorak.truffle.node.SchemeNode;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.GenerateUncached;

@GenerateNodeFactory
@GenerateUncached(inherit = true)
public abstract class AlwaysInlinableProcedureNode extends SchemeNode {

    public abstract Object execute(Object[] arguments);
}
