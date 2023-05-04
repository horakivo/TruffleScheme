package com.ihorak.truffle.node.builtin.list;

import com.ihorak.truffle.node.builtin.core.list.AppendBinaryNode;
import com.ihorak.truffle.node.builtin.core.list.AppendBinaryNodeGen;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.ihorak.truffle.runtime.SchemeList;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.ExplodeLoop;

public abstract class AppendBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization(guards = "arguments.length == 2")
    protected Object doTwoArgs(Object[] arguments,
                               @Cached AppendBinaryNode appendBinaryNode) {
        return appendBinaryNode.execute(arguments[0], arguments[1]);
    }

    @ExplodeLoop
    @Specialization(guards = "arguments.length > 0", limit = "2")
    protected Object doArbitraryNumberOfArgsCached(Object[] arguments,
                                                   @Cached AppendBinaryNode appendBinaryNode,
                                                   @Cached("arguments.length") int cachedLength) {

        Object result = arguments[0];

        for (int i = 1; i < cachedLength; i++) {
            result = appendBinaryNode.execute(result, arguments[i]);
        }

        return result;
    }

    @ExplodeLoop
    @Specialization(guards = "arguments.length > 0", replaces = "doArbitraryNumberOfArgsCached")
    protected Object doArbitraryNumberOfArgsUncached(Object[] arguments,
                                                     @Cached AppendBinaryNode appendBinaryNode) {

        Object result = arguments[0];

        for (int i = 1; i < arguments.length; i++) {
            result = appendBinaryNode.execute(result, arguments[i]);
        }

        return result;
    }


    @Specialization(guards = "arguments.length == 0")
    protected Object doNoArgs(Object[] arguments) {
        return SchemeList.EMPTY_LIST;
    }
}
