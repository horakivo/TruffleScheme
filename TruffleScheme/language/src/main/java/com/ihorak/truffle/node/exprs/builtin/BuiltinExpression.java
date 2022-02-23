package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.node.EvalArgumentsNode;
import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.dsl.NodeChild;

@NodeChild(value = "arguments", type = EvalArgumentsNode.class)
public abstract class BuiltinExpression extends SchemeExpression {
}
