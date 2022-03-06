package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.NodeField;

public abstract class BinaryReducibleOperation extends SchemeExpression {

    protected final SchemeExpression[] arguments;
    protected final BinaryOperationNode operation;

    public BinaryReducibleOperation(SchemeExpression[] arguments, BinaryOperationNode operation) {
        this.arguments = arguments;
        this.operation = operation;
    }
}
