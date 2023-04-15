package com.ihorak.truffle.node;

import com.oracle.truffle.api.dsl.NodeChild;

@NodeChild("left")
@NodeChild("right")
public abstract class BinaryExprNode extends SchemeExpression {
}
