package com.ihorak.truffle.node.callable;

import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.type.ArbitraryArgsPrimitiveProcedure;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class DispatchPrimitiveProcedureNode extends SchemeNode {

    public abstract Object execute(VirtualFrame callerFrame, ArbitraryArgsPrimitiveProcedure procedure, Object[] arguments);


    @Specialization
    protected static Object doPrimitiveProcedure(
            VirtualFrame callerFrame,
            ArbitraryArgsPrimitiveProcedure procedure,
            Object[] arguments,
            @Cached("createInlinableNode(procedure)") AlwaysInlinedMethodNode inlinedMethodNode
    ) {
        return inlinedMethodNode.execute(callerFrame, arguments);
    }


    protected static AlwaysInlinedMethodNode createInlinableNode(ArbitraryArgsPrimitiveProcedure procedure) {
        return procedure.alwaysInlinableFactoryNode().createNode();
    }
}
