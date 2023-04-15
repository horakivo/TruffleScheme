package com.ihorak.truffle.node.callable;

import com.ihorak.truffle.node.SchemeNode;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;

@GenerateNodeFactory
public abstract class AlwaysInlinableProcedureNode extends SchemeNode {

    public abstract Object execute(Object[] arguments);
}
