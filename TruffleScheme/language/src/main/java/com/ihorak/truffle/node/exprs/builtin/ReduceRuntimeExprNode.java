package com.ihorak.truffle.node.exprs.builtin;

import com.ihorak.truffle.SchemeException;
import com.ihorak.truffle.node.SchemeExpression;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public class ReduceRuntimeExprNode extends SchemeExpression {

    @Child
    private BinaryOperationNode operation;

    private final Object initial;

    private final boolean supportNoArgs;

    public ReduceRuntimeExprNode(BinaryOperationNode operation, Object initial, boolean supportNoArgs) {
        this.operation = operation;
        this.initial = initial;
        this.supportNoArgs = supportNoArgs;
    }

    @Override
    public Object executeGeneric(VirtualFrame virtualFrame) {
        var arguments = virtualFrame.getArguments();
        if (arguments.length > 0) {

            if (arguments.length == 1 && !supportNoArgs) {
                throw new SchemeException(operation.toString() + ": arity mismatch; Expected number of arguments does not match the given number \n expected: at least 1 \n given: 0");
            }

            return doReduce(arguments);
        } else {
            throw new SchemeException("Interpret mistake. ReduceRunTimeNode was called with 0 arguments which shouldn't happen since the first one should be parent env.");
        }

    }

    //TODO can it be here?
    @ExplodeLoop
    private Object doReduce(Object[] arguments) {
        int index = 1;
        var result = initial;

        //special case for - and /
        if (arguments.length > 2) {
            result = arguments[1];
            index++;
        }

        for (int i = index; i < arguments.length; i++) {
            result = operation.execute(result, arguments[i]);
        }

        return result;
    }
}
