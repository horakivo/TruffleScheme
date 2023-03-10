package com.ihorak.truffle.node.callable.TCO;

import com.ihorak.truffle.SchemeTruffleLanguage;
import com.ihorak.truffle.node.SchemeExpression;
import com.ihorak.truffle.node.callable.TCO.exceptions.SelfRecursiveTailCallException;
import com.ihorak.truffle.node.callable.TCO.exceptions.TailCallException;
import com.ihorak.truffle.node.scope.WriteLocalVariableExprNode;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.dsl.Executed;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.MaterializedFrame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;

import java.util.List;

public abstract class SelfRecursiveTailCallThrowerNode extends SchemeExpression {

    @Children
    private final WriteLocalVariableExprNode[] writeArguments;


    public SelfRecursiveTailCallThrowerNode(List<WriteLocalVariableExprNode> writeLocalVariableExprNodes) {
        this.writeArguments = writeLocalVariableExprNodes.toArray(WriteLocalVariableExprNode[]::new);
    }

    @Specialization
    protected Object doThrow(VirtualFrame frame) {
        prepareArgumentsForNextCall(frame);
        throw SelfRecursiveTailCallException.INSTANCE;
    }


    @ExplodeLoop
    private void prepareArgumentsForNextCall(VirtualFrame frame) {
        for (var expr : writeArguments) {
            expr.executeGeneric(frame);
        }
    }
}
