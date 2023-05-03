package com.ihorak.truffle.node.callable;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.SchemeNode;
import com.ihorak.truffle.node.builtin.polyglot.TranslateInteropExceptionNode;
import com.ihorak.truffle.runtime.UserDefinedProcedure;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.GenerateUncached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.library.CachedLibrary;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.IndirectCallNode;

@GenerateUncached
public abstract class DispatchUserProcedureNode extends SchemeNode {

    public abstract Object executeDispatch(Object procedure, Object[] arguments);

    @Specialization(guards = "userDefinedProcedure.callTarget() == procedureCached.callTarget()", limit = "3")
    protected static Object doCached(
            UserDefinedProcedure userDefinedProcedure,
            Object[] arguments,
            @Cached("userDefinedProcedure") UserDefinedProcedure procedureCached,
            @Cached("create(procedureCached.callTarget())") DirectCallNode directCallNode,
            @Cached("getArgumentsLength(arguments)") int givenNumberOfArgsCached) {
        if (arguments.length - 1 != procedureCached.expectedNumberOfArgs()) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            throw SchemeException.arityException(null, procedureCached.name(), procedureCached.expectedNumberOfArgs(), givenNumberOfArgsCached);
        }
        return directCallNode.call(arguments);
    }

    @Specialization(replaces = "doCached")
    protected static Object doUncached(
            UserDefinedProcedure userDefinedProcedure,
            Object[] arguments,
            @Cached IndirectCallNode indirectCallNode) {
        return indirectCallNode.call(userDefinedProcedure.callTarget(), arguments);
    }

    @Specialization(guards = "interopLibrary.isExecutable(foreignProcedure)", limit = "getInteropCacheLimit()")
    protected static Object callInteropProcedure(Object foreignProcedure, Object[] arguments,
                                                 @Cached TranslateInteropExceptionNode translateInteropExceptionNode,
                                                 @CachedLibrary("foreignProcedure") InteropLibrary interopLibrary) {
        return executeForeignProcedure(foreignProcedure, arguments, interopLibrary, translateInteropExceptionNode);
    }

    protected static int getArgumentsLength(Object[] arguments) {
        return arguments.length - 1; // first element is parent frame or null
    }

    @Fallback
    protected Object fallback(Object procedure, Object[] arguments) {
        throw SchemeException.notProcedure(procedure, this);
    }
}
