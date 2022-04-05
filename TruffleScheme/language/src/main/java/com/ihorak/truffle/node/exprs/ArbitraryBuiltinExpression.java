package com.ihorak.truffle.node.exprs;

import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.NodeChild;

@NodeChild(value = "arguments", type = ReadProcedureArgsExprNode.class)
@GenerateNodeFactory
public abstract class ArbitraryBuiltinExpression extends SchemeExpression {
}
