package com.ihorak.truffle.node.exprs.arithmetic;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeField(name = "plusOperation", type = BinaryOperationNode.class)
public abstract class ReducePlusExprNode extends SchemeExpression {

    protected abstract BinaryOperationNode getPlusOperation();

    @Specialization
    protected Object addAnyNumberOfArgs(VirtualFrame frame) {
        var operation = getPlusOperation();
        var arguments = frame.getArguments();
        Object result = 0L;

        for (int i = 1; i < arguments.length; i++) {
            result = operation.execute(result, arguments[i]);
        }

        return result;
    }
}
