package com.ihorak.truffle.node.callable;

import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.runtime.PrimitiveProcedure;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;

//@GenerateUncached
public abstract class DispatchPrimitiveProcedureNode extends SchemeNode {

    public abstract Object execute(PrimitiveProcedure procedure, Object[] arguments);


    @Specialization(guards = "procedure == procedureCached", limit = "2")
    protected static Object doPrimitiveProcedureCached(
            PrimitiveProcedure procedure,
            Object[] arguments,
            @Cached("procedure") PrimitiveProcedure procedureCached,
            @Cached("createInlinableNode(procedureCached)") AlwaysInlinableProcedureNode inlinedMethodNode
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


    protected static AlwaysInlinableProcedureNode createInlinableNode(PrimitiveProcedure procedure) {
        return procedure.factory().createNode();
    }
}
