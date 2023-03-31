package com.ihorak.truffle.node.callable;

import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.type.UserDefinedProcedure;
import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.IndirectCallNode;

@GenerateUncached
public abstract class DispatchNode extends SchemeNode {

    public abstract Object executeDispatch(UserDefinedProcedure userDefinedProcedure, Object[] arguments);

    @Specialization(
            guards = "userDefinedProcedure.getCallTarget() == cachedRootCallTarget",
            assumptions = "callTargetStableAssumption",
            limit = "3")
    protected static Object directlyDispatch(UserDefinedProcedure userDefinedProcedure,
                                             Object[] arguments,
                                             @Cached("create(userDefinedProcedure.getCallTarget())") DirectCallNode directCallNode,
                                             @Cached("userDefinedProcedure.getCallTarget()") RootCallTarget cachedRootCallTarget,
                                             @Cached("userDefinedProcedure.getCallTargetStableAssumption()") Assumption callTargetStableAssumption) {
        return directCallNode.call(arguments);
    }

    @Specialization(replaces = "directlyDispatch")
    protected static Object indirectlyDispatch(
            UserDefinedProcedure userDefinedProcedure,
            Object[] arguments,
            @Cached IndirectCallNode indirectCallNode) {
        return indirectCallNode.call(userDefinedProcedure.getCallTarget(), arguments);
    }
}
