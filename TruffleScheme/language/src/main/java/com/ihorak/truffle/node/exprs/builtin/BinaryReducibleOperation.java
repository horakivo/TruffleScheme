package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.NodeField;

@NodeField(name = "arguments", type = SchemeExpression[].class)
@NodeField(name = "operation", type = BinaryOperationNode.class)
public abstract class BinaryReducibleOperation extends SchemeExpression {

    protected abstract SchemeExpression[] getArguments();
    protected abstract BinaryOperationNode getOperation();

}
