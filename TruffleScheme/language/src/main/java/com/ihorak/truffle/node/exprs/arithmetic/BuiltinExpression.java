package com.ihorak.truffle.node.exprs.arithmetic;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.scope.ReadProcedureArgExprNode;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.NodeChild;

@NodeChild(value = "arguments")
@GenerateNodeFactory
public abstract class BuiltinExpression extends SchemeExpression {
}
