package com.ihorak.truffle.node.builtin.comparison;

import com.ihorak.truffle.exceptions.SchemeException;
import com.ihorak.truffle.node.builtin.core.comparison.EqualBinaryNode;
import com.ihorak.truffle.node.builtin.core.comparison.EqualBinaryNodeGen;
import com.ihorak.truffle.node.callable.AlwaysInlinableProcedureNode;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Fallback;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class EqualBuiltinNode extends AlwaysInlinableProcedureNode {

    @Specialization(guards = "arguments.length == 2")
    protected boolean doTwoArgs(Object[] arguments,
                                @Cached EqualBinaryNode equalBinaryNode) {
        return equalBinaryNode.execute(arguments[0], arguments[1]);
    }

    @Fallback
    protected Object doThrow(Object[] arguments) {
        throw SchemeException.arityException(this, "equal?", 2, arguments.length);
    }
}
