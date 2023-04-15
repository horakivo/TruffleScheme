package com.ihorak.truffle.node.callable;

import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.type.ArbitraryArgsPrimitiveProcedure;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;

//@GenerateUncached
public abstract class DispatchPrimitiveProcedureNode extends SchemeNode {

    public abstract Object execute(ArbitraryArgsPrimitiveProcedure procedure, Object[] arguments);


    @Specialization(guards = "procedure == procedureCached", limit = "2")
    protected static Object doPrimitiveProcedureCached(
            ArbitraryArgsPrimitiveProcedure procedure,
            Object[] arguments,
            @Cached("procedure") ArbitraryArgsPrimitiveProcedure procedureCached,
            @Cached("createInlinableNode(procedureCached)") AlwaysInlinedMethodNode inlinedMethodNode
    ) {
        return inlinedMethodNode.execute(arguments);
    }

//    @Specialization(replaces = "doPrimitiveProcedureCached")
//    protected static Object doPrimitiveProcedureUncached(
//            ArbitraryArgsPrimitiveProcedure procedure,
//            Object[] arguments
//    ) {
//        return procedure.factory().getUncachedInstance().execute(arguments);
//    }


    protected static AlwaysInlinedMethodNode createInlinableNode(ArbitraryArgsPrimitiveProcedure procedure) {
        return procedure.factory().createNode();
    }
}
