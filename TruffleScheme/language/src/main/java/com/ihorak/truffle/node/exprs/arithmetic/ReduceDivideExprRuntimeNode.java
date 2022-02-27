package com.ihorak.truffle.node.exprs.arithmetic;

import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

@NodeField(name = "divideOperation", type = BinaryOperationNode.class)
public abstract class ReduceDivideExprRuntimeNode extends SchemeExpression {

    protected abstract BinaryOperationNode getDivideOperation();

    @Specialization(guards = "frame.getArguments().length == 1")
    protected Object noRuntimeArguments(VirtualFrame frame) {
        throw new SchemeException("/: arity mismatch; Expected number of arguments does not match the given number \n expected: at least 1 \n given: 0");
    }

    @Specialization(guards = "frame.getArguments().length == 2")
    protected Object oneRuntimeArgument(VirtualFrame frame) {
        var argument = frame.getArguments()[1];
        if (argument instanceof Long) {
            return 1 / ((Long) argument).doubleValue();
        }

        throw new SchemeException("/: contract violation;\nExpected: number?\nGiven: " + argument);
    }

    @Specialization
    protected Object divideAnyNumberOfArgumentsRuntime(VirtualFrame frame) {
        var operation = getDivideOperation();
        var arguments = frame.getArguments();
        var result = arguments[1];

        for (int i = 2; i < arguments.length; i++) {
            result = operation.execute(result, arguments[i]);
        }

        return result;
    }
}
