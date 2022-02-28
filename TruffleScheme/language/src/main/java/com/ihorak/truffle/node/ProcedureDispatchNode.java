package com.ihorak.truffle.node;

import com.ihorak.truffle.type.SchemeFunction;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.nodes.Node;

public abstract class ProcedureDispatchNode extends Node {

    public abstract Object executeDispatch(CallTarget callTarget, Object[] arguments);

    @Specialization(guards = "callTarget == directCallNode.getCallTarget()", limit = "2")
    protected static Object directlyDispatch(CallTarget callTarget,
                                             Object[] arguments,
                                             @Cached("create(callTarget)") DirectCallNode directCallNode) {
        return directCallNode.call(arguments);
    }

    @Specialization(replaces = "directlyDispatch")
    protected static Object indirectlyDispatch(
            CallTarget callTarget,
            Object[] arguments,
            @Cached IndirectCallNode indirectCallNode) {
        return indirectCallNode.call(callTarget, arguments);
    }

//TODO not neccesssary since I am checking the type before

//    @Fallback
//    protected static Object firstArgumentIsNotAFunction(
//            Object nonFunction,
//            @SuppressWarnings("unused") Object[] arguments) {
//        throw new Exception("'" + nonFunction + "' is not a function");
//    }
}
