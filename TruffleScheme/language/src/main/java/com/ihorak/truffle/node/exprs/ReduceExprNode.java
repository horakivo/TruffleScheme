package com.ihorak.truffle.node.exprs;

import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.exprs.builtin.BinaryOperationNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public class ReduceExprNode extends SchemeExpression {

    @SuppressWarnings("FieldMayBeFinal")
    @Child
    private BinaryOperationNode operation;
    @SuppressWarnings("FieldMayBeFinal")
    @Children
    private SchemeExpression[] arguments;

    private final Object initialValue;


    public ReduceExprNode(BinaryOperationNode operation, List<SchemeExpression> arguments, Object initialValue) {
        this.operation = operation;
        this.arguments = arguments.toArray(new SchemeExpression[0]);
        this.initialValue = initialValue;
    }

    //pozor na polymorfismus
    @Override
    @ExplodeLoop
    public Object executeGeneric(VirtualFrame virtualFrame) {
        int index = 0;
        Object result = initialValue;

        //pro - and / because if this condition is not here then for (- 1 2) = -1 we would do (- 0 1 2) = -3
        if (arguments.length > 1) {
            result = arguments[index].executeGeneric(virtualFrame);
            index++;
        }

        for (int i = index; i < arguments.length; i++) {
            result = operation.execute(result, arguments[i].executeGeneric(virtualFrame));
        }

        return result;
    }
}
