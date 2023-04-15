package com.ihorak.truffle.node.callable;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.SchemeNode;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.frame.VirtualFrame;

@GenerateNodeFactory
public abstract class AlwaysInlinedMethodNode extends SchemeNode {

    public abstract Object execute(Object[] arguments);
}
