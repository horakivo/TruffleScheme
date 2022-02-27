package com.ihorak.truffle.node.exprs.arithmetic;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeField(name = "multiplicationOperation", type = BinaryOperationNode.class)
public abstract class ReduceMultiplyExprRuntimeNode extends SchemeExpression {

    protected abstract BinaryOperationNode getMultiplicationOperation();

    @Specialization
    protected Object multiplyAnyNumberOfArgsRuntime(VirtualFrame frame) {
        var operation = getMultiplicationOperation();
        var arguments = frame.getArguments();
        Object result = 1;

        for (int i = 1; i < arguments.length; i++) {
            result = operation.execute(result, arguments[i]);
        }

        return result;
    }
}
